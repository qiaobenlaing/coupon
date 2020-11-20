package com.huift.hfq.cust.fragment;

import com.umeng.analytics.MobclickAgent;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.huift.hfq.cust.R;
/**
 * 优惠券兑换码使用结果
 * @author yingchen
 *
 */
public class CouponCodeUseFragment extends Fragment {
	/**
	 * 需要传递参数时有利于解耦
	 */
	public static CouponCodeUseFragment newInstance(){
		Bundle args = new Bundle();
		CouponCodeUseFragment fragment = new CouponCodeUseFragment();
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_coupon_code_use, container, false);
		return view;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(CouponCodeUseFragment.class.getSimpleName()); //统计页面
	}
	
	@Override
	public void onPause() {
	    super.onPause();
	    MobclickAgent.onPageEnd(CouponCodeUseFragment.class.getSimpleName()); 
	}
}
