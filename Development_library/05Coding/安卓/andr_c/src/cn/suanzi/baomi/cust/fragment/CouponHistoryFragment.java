//---------------------------------------------------------
//@author    yanfang.li
//@version   1.0.0
//@createTime 2015.6.2
//@copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
//---------------------------------------------------------
package cn.suanzi.baomi.cust.fragment;


import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.utils.AppUtils;
import cn.suanzi.baomi.cust.R;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.umeng.analytics.MobclickAgent;

/**
 * 优惠券历史页面
 * @author yanfang.li
 */
public class CouponHistoryFragment extends Fragment {

	private final static String TAG = "CouponHistoryFragment";
	/** Fragment管理器 */
	private FragmentManager mFragmentManager;
	private FragmentTransaction mFragmentTransaction;
	/** 已失效*/ 
	@ViewInject(R.id.unavailable)
	private RadioButton mRbtnUnavailable;
	/** 已使用*/
	@ViewInject(R.id.available)
	private RadioButton mRbtnAvailable;
	static View sView;
	static CouponHistoryFragment mFragment;
	int type = 3; //  优惠券类型， 3-> 已失效  2->已使用
	
	public static CouponHistoryFragment newInstance() {
		
		if(mFragment != null){
			return mFragment;
		}
		Bundle args = new Bundle();
		mFragment = new CouponHistoryFragment();
		mFragment.setArguments(args);
		return mFragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if(sView != null){
			return sView;
		}
		sView = inflater.inflate(R.layout.fragment_couponhistory, container, false);
		ViewUtils.inject(this, sView);
		changeFragment(getMyActivity(),R.id.linear_couponlist_content,new CouponUnusedFragment());
		Util.addLoginActivity(getMyActivity());
		return sView;
		
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
	 * @param id
	 * @param fragment
	 */
	public void changeFragment(Activity activity,int id, Fragment fragment) {
		mFragmentManager = activity.getFragmentManager();
		mFragmentTransaction = mFragmentManager.beginTransaction();
		mFragmentTransaction.replace(id, fragment);
		mFragmentTransaction.addToBackStack(null);
		mFragmentTransaction.commit();
	}
	

	/**
	 * 切换选项卡
	 * 
	 * @param v
	 */
	@OnClick({ R.id.unavailable, R.id.available})
	private void changeFragment(View v) {
		switch (v.getId()) {
		case R.id.unavailable:
			mRbtnUnavailable.setEnabled(false);
			mRbtnAvailable.setEnabled(true);
			changeFragment(getMyActivity(),R.id.linear_couponlist_content,new CouponUnusedFragment());
			break;
		case R.id.available:
			mRbtnUnavailable.setEnabled(true);
			mRbtnAvailable.setEnabled(false);
			changeFragment(getMyActivity(),R.id.linear_couponlist_content,new CouponUsedFragment());
			break;
		default:
			break;
		}
	//	getMyAvailableCoupon();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(CouponHistoryFragment.class.getSimpleName()); // 统计页面
	};
	
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(CouponHistoryFragment.class.getSimpleName());
	}
}
