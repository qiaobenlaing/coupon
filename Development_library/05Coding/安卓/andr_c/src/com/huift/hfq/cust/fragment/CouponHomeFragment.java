//---------------------------------------------------------
//@author    yanfang.li
//@version   1.0.0
//@copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
//---------------------------------------------------------
package com.huift.hfq.cust.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import com.huift.hfq.base.SzApplication;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.cust.R;

import com.huift.hfq.cust.activity.CouponSearchActivity;
import com.huift.hfq.cust.application.CustConst;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.umeng.analytics.MobclickAgent;

/**
 * 优惠券界面
 * 
 * @author
 */
public class CouponHomeFragment extends BaseFragment {
	public static final String STATUS = "status";
	public static final String STATUS_EFFECTIVE = "1";
	public static final String STATUS_HISTORY = "0";
	public static final String COUPON_FIRST = "couponFirst";
	public static final String HOME_TIPS = "homeTips";
	private final static String TAG = CouponHomeFragment.class.getSimpleName();
	/** 有效优惠券按钮 **/
	@ViewInject(R.id.btn_coupon_effect)
	private RadioButton mBtnCouponEffect;
	/** 历史优惠券按钮 **/
	@ViewInject(R.id.btn_coupon_history)
	private RadioButton mBtnCouponHistory;
	/** 添加 **/
	@ViewInject(R.id.iv_cardlist_add)
	private ImageView mIvCardListAdd;
	@ViewInject(R.id.rg_coupon_type)
	private RadioGroup couponTypeGroup;

	/** Fragment管理器 */
	private FragmentManager mFragmentManager;
	private FragmentTransaction mFragmentTransaction;
	private View view;
	private boolean mGrabFlag = false;

	public static CouponHomeFragment newInstance() {
		Bundle args = new Bundle();
		CouponHomeFragment fragment = new CouponHomeFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void viewVisible() {
		super.viewVisible();
		String first = DB.getStr(COUPON_FIRST);
		mGrabFlag = DB.getBoolean(CustConst.Key.GRAB_COUPON);
		boolean tipsFlag = DB.getBoolean(HOME_TIPS); // 消息提示
		if (!Util.isEmpty(first)) {
			if (first.equals(Util.NUM_ONE + "")) {
				DB.saveStr(COUPON_FIRST, Util.NUM_TWO + "");
				DB.saveBoolean(HOME_TIPS, false); // 提示消息
			} else {
				if (mGrabFlag || tipsFlag) {
					DB.saveBoolean(CustConst.Key.GRAB_COUPON, false);
					DB.saveBoolean(HOME_TIPS, false);
					try {
						init();
					} catch (Exception e) {
						Log.e(TAG, "加载出错=" + e.getMessage()); // TODO
					}
				}
			}
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		if (view != null) { return view; }
		view = inflater.inflate(R.layout.fragment_couponhome, container, false);
		ViewUtils.inject(this, view);
		DB.saveStr(COUPON_FIRST, Util.NUM_TWO + "");
		init();
		SzApplication.setCurrActivity(getMyActivity());
		Util.addHomeActivity(getMyActivity());
		mIvCardListAdd.setVisibility(View.GONE);
		return view;
	}

	private Activity getMyActivity() {
		Activity act = getActivity();
		if (act == null) {
			act = AppUtils.getActivity();
		}
		return act;
	}
	
	/**
	 * 更换页面
	 * 
	 * @param id
	 * @param fragment
	 */
	public void changeFragment(Activity activity, int id, Fragment fragment) {
		mFragmentManager = activity.getFragmentManager();
		mFragmentTransaction = mFragmentManager.beginTransaction();
		mFragmentTransaction.replace(id, fragment);
		mFragmentTransaction.addToBackStack(null);
		mFragmentTransaction.commit();
	}

	/**
	 * 初始化方法
	 */
	private void init() {
		// 进去默认的页面
		changeFragment(getMyActivity(), R.id.ly_coupone_content, CouponEffectFragment.newInstance());
		couponTypeGroup.check(mBtnCouponEffect.getId());
		mBtnCouponHistory.setEnabled(true);
		mBtnCouponEffect.setEnabled(false);
		//新人注册
		ImageView ivNewRegister = (ImageView) getActivity().findViewById(R.id.tv_new_register);
		ivNewRegister.setVisibility(View.GONE);
	}
	
	/** 
	 * 切换选项卡
	 * 
	 * @param v
	 */
	@OnClick({ R.id.btn_coupon_effect, R.id.btn_coupon_history, R.id.iv_cardlist_add })
	private void changeFragment(View v) {
		switch (v.getId()) {
		case R.id.btn_coupon_effect:
			changeFragment(getMyActivity(), R.id.ly_coupone_content, CouponEffectFragment.newInstance());
			mBtnCouponHistory.setEnabled(true);
			mBtnCouponEffect.setEnabled(false);
			break;
		case R.id.btn_coupon_history:
			changeFragment(getMyActivity(), R.id.ly_coupone_content, CouponHistoryFragment.newInstance());
			mBtnCouponHistory.setEnabled(false);
			mBtnCouponEffect.setEnabled(true);
			break;
		case R.id.iv_cardlist_add:// 搜索商家页面
			startActivity(new Intent(getMyActivity(), CouponSearchActivity.class));
			// 友盟统计
			MobclickAgent.onEvent(getMyActivity(), "coupon_search");
			break;
		default:
			break;
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(CouponHomeFragment.class.getSimpleName()); // 统计页面
	};
	
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(CouponHomeFragment.class.getSimpleName());
	}
}
