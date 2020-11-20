package com.huift.hfq.cust.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.cust.R;

import com.huift.hfq.cust.activity.H5ShopDetailActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.umeng.analytics.MobclickAgent;

/**
 * 扫码失败
 * @author wensi.yu
 *
 */
public class ScanFailFragment extends Fragment {

	public static ScanFailFragment newInstance() {
		Bundle args = new Bundle();
		ScanFailFragment fragment = new ScanFailFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_scanfail, container, false);
		ViewUtils.inject(this, v);
		Util.addActivity(getMyActivity());
		Util.addLoginActivity(getMyActivity());
		TextView failShop = (TextView) v.findViewById(R.id.tv_fail_shop);
		failShop.setVisibility(View.VISIBLE);
		return v;
	}
	
	private Activity getMyActivity() {
		Activity act = getActivity();
		if (act == null) {
			act = AppUtils.getActivity();
		}
		return act;
	}    
	
	/**
	 * 点击
	 * @param view
	 */
	@OnClick({R.id.btn_scan_fail_back,R.id.btn_scan_fail_again})
	public void trunIdenCode(View view) {
		switch (view.getId()) {  
		case R.id.btn_scan_fail_back://返回
			H5ShopDetailActivity.setCurrentPage("backup");
			Util.exit();
			break;
			
		case R.id.btn_scan_fail_again://重新扫描
			Util.exitLogin(); 
			break;

		default:
			break;
		}
	}
	
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(ScanFailFragment.class.getSimpleName()); 
	}
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(ScanFailFragment.class.getSimpleName()); //统计页面
	}
}
