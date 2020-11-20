//---------------------------------------------------------
//@author    Zhonghui.Dong
//@version   1.0.0
//@createTime 2015.6.2
//@copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
//---------------------------------------------------------
package com.huift.hfq.cust.fragment;

import net.minidev.json.JSONObject;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.huift.hfq.base.SzApplication;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.pojo.Citys;
import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.cust.R;

import com.huift.hfq.cust.application.CustConst;
import com.huift.hfq.cust.model.ListFollowedShopTask;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.umeng.analytics.MobclickAgent;

/**
 * 按商户位置由近及远显示商家信息
 * 
 * @author qian.zhou
 */
public class CardHomeFragment extends BaseFragment {
	private static final String TAG = CardHomeFragment.class.getSimpleName();
	public static final String STATUS = "status";
	public static final String STATUS_FOCUS = "1";
	public static final String STATUS_COMMON = "0";
	public String IsFocus = String.valueOf(Util.NUM_ONE);
	/** 关注商家按钮 **/
	@ViewInject(R.id.tv_focus)
	TextView mTvFocus;
	/** 足迹商家按钮 **/
	@ViewInject(R.id.tv_common)
	TextView mTvCommon;
	/** 附近 **/
	@ViewInject(R.id.tv_nearby)
	TextView mTvNearby;
	/** 活动 */
	@ViewInject(R.id.tv_act)
	TextView mTvAct;
	/** Fragment管理器 */
	private FragmentManager mFragmentManager;
	private FragmentTransaction mFragmentTransaction;
	private View v;
	/** 页码 **/
	private int mPage = Util.NUM_ONE;
	private String mLongitude;// 经度
	private String mLatitude;// 经度

	/**
	 * 需要传递参数时有利于解耦
	 */
	public static CardHomeFragment newInstance() {
		Bundle args = new Bundle();
		CardHomeFragment fragment = new CardHomeFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (v != null) { return v; }
		v = inflater.inflate(R.layout.fragment_cardhome, container, false);
		ViewUtils.inject(this, v);
		SzApplication.setCurrActivity(getMyActivity());
		Util.addHomeActivity(getMyActivity());
		init();
		return v;
	}

	private Activity getMyActivity() {
		Activity act = getActivity();
		if (act == null) {
			act = AppUtils.getActivity();
		}
		return act;
	}

	@Override
	public void viewVisible() {
		super.viewVisible();
	}

	/**
	 * 初始化
	 */
	private void init() {
		SharedPreferences preferences = getMyActivity().getSharedPreferences(CustConst.Key.CITY_OBJ,Context.MODE_PRIVATE);
		String cityName = preferences.getString(CustConst.Key.CITY_NAME, null);
		Log.d("TAG", "取出DB的定位城市为 ：：：：：：： " + cityName);
		if (Util.isEmpty(cityName)) {
			Citys citys = DB.getObj(HomeFragment.CITYS, Citys.class);
			mLongitude = String.valueOf(citys.getLongitude());
			mLatitude = String.valueOf(citys.getLatitude());
			Log.d("TAG", "取出DB的定位城市的经度为 ：：：：：：： " + mLongitude);
			Log.d("TAG", "取出DB的定位城市的纬度为 ：：：：：：： " + mLatitude);
		} else {
			mLongitude = preferences.getString(CustConst.Key.CITY_LONG, null);
			mLatitude = preferences.getString(CustConst.Key.CITY_LAT, null);
		}
		// 标题
		ImageView ivTurnin = (ImageView) v.findViewById(R.id.iv_turn_in);
		ivTurnin.setVisibility(View.GONE);
		TextView tvTitle = (TextView) v.findViewById(R.id.tv_mid_content);
		tvTitle.setText(R.string.card_home);
		if (Util.isNetworkOpen(getMyActivity())) {// 判断是否联网
			focus();
		} else {
			// mBtnCardListFocus.setChecked(true);
			// mBtnCardNearby.setChecked(false);
			Util.getToastBottom(getMyActivity(), "请连接网络");
		}

		mFragmentManager = getMyActivity().getFragmentManager();
		// 新人注册
		ImageView ivNewRegister = (ImageView) getActivity().findViewById(R.id.tv_new_register);
		ivNewRegister.setVisibility(View.GONE);
	}

	/**
	 * 查询关注商家
	 */
	public void focus()//
	{
		new ListFollowedShopTask(getMyActivity(), new ListFollowedShopTask.Callback() {
			@Override
			public void getResult(JSONObject jsonobject) {
				if (null != getMyActivity()) {
					setAllText();
					if (jsonobject == null) {
						// mBtnCardListFocus.setChecked(false);
						// mBtnCardNearby.setChecked(true);
						// 给附近商家按钮一个默认背景颜色
						setText(mTvNearby);
						changeFragment(getMyActivity(), R.id.linear_cardlist_content, new MyNearlyListFragment());

					} else {
						setText(mTvFocus);
						// 给关注商家按钮一个默认背景颜色
						changeFragment(getMyActivity(), R.id.linear_cardlist_content, new MyFocusListFragment());
					}
				}
			}
		}).execute(mLongitude, mLatitude, String.valueOf(mPage));
	}

	/**
	 * 更换页面
	 * 
	 * @param id
	 * @param fragment
	 */
	public void changeFragment(Activity activity, int id, Fragment fragment) {
		if (null != activity) {
			mFragmentManager = activity.getFragmentManager();
			mFragmentTransaction = mFragmentManager.beginTransaction();
			mFragmentTransaction.replace(id, fragment);
			mFragmentTransaction.addToBackStack(null);
			mFragmentTransaction.commit();
		}
	}

	/**
	 * 添加fragment的跳转
	 * 
	 * @param v
	 */
	@OnClick({ R.id.tv_focus, R.id.tv_common, R.id.tv_nearby, R.id.tv_act })
	public void onClick(View v) {
		setAllText();
		switch (v.getId()) {
		case R.id.tv_focus:// 加载到关注商家页面
			changeFragment(getMyActivity(), R.id.linear_cardlist_content, new MyFocusListFragment());
			IsFocus = String.valueOf(Util.NUM_ONE);
			setText(mTvFocus);
			break;
		case R.id.tv_common:// 加载一般商家页面
			changeFragment(getMyActivity(), R.id.linear_cardlist_content, new MyCommonListFragment());
			IsFocus = String.valueOf(Util.NUM_ZERO);
			setText(mTvCommon);
			break;
		case R.id.tv_nearby:// 附近
			changeFragment(getMyActivity(), R.id.linear_cardlist_content, new MyNearlyListFragment());
			IsFocus = String.valueOf(Util.NUM_ZERO);
			setText(mTvNearby);
			break;
		case R.id.tv_act:// 活动
			changeFragment(getMyActivity(), R.id.linear_cardlist_content, new ActIssueFragment());
			setText(mTvAct);
			break;
		default:
			break;
		}
	}

	/**
	 * 设置所有
	 */
	private void setAllText() {
		mTvNearby.setEnabled(true);
		mTvFocus.setEnabled(true);
		mTvCommon.setEnabled(true);
		mTvAct.setEnabled(true);
		// 设置字体
		mTvFocus.setTextColor(getResources().getColor(R.color.gray));
		mTvNearby.setTextColor(getResources().getColor(R.color.gray));
		mTvCommon.setTextColor(getResources().getColor(R.color.gray));
		mTvAct.setTextColor(getResources().getColor(R.color.gray));
		// 设置背景
		mTvFocus.setBackgroundResource(0);
		mTvNearby.setBackgroundResource(0);
		mTvCommon.setBackgroundResource(0);
		mTvAct.setBackgroundResource(0);
	}

	/**
	 * 设置控件
	 * 
	 * @param textView
	 */
	private void setText(TextView textView) {
		textView.setEnabled(false);
		textView.setTextColor(getActivity().getResources().getColor(R.color.red));
		textView.setBackgroundResource(R.drawable.bottom_red_border);
	}

	/**
	 * 友盟统计
	 */
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(CardHomeFragment.class.getSimpleName()); // 统计页面
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(CardHomeFragment.class.getSimpleName());
	}
}
