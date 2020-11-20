package com.huift.hfq.cust.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import com.huift.hfq.base.Const;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.cust.R;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.umeng.analytics.MobclickAgent;

/**
 * 红包详情页面
 * @author qian.zhou
 */
@SuppressLint("SetJavaScriptEnabled")
public class MyRedBagListFragment extends Fragment {
	private static final String TAG = "MyRedBagListFragment";
	public static final String BOUNDS_CODE = "boundcodes";
	/** 加载网页版 **/
	private WebView webview;
	private String mBoundcodes;

	/**
	 * 需要传递参数时有利于解耦
	 */
	public static MyRedBagListFragment newInstance() {
		Bundle args = new Bundle();
		MyRedBagListFragment fragment = new MyRedBagListFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_myredbaglist, container, false);
		ViewUtils.inject(this, v);
		Util.addActivity(getMyActivity());
		Util.addLoginActivity(getMyActivity());
		init(v);
		return v;
	}
	
	/**
	 * 保证activity不为空
	 * @return activity
	 */
	private Activity getMyActivity() {
		Activity act = getActivity();
		if (act == null) {
			act = AppUtils.getActivity();
		}
		return act;
	}
	
	// 初始化方法
	private void init(View v) {
		TextView tvcontent = (TextView) v.findViewById(R.id.tv_mid_content);//标题
		tvcontent.setText(getResources().getString(R.string.redbao_list));
		ImageView ivShare = (ImageView) v.findViewById(R.id.iv_share_all);//分享
		ivShare.setVisibility(View.GONE);
		// 取值
		Intent intent = getMyActivity().getIntent();
		// 红包编码
		mBoundcodes = intent.getStringExtra(BOUNDS_CODE);
		Log.d(TAG, "获取的红包编码为：：：：：：：：： " + mBoundcodes);
		webview = (WebView) v.findViewById(R.id.wb_redbag_actdetail);
		// 设置WebView属性，能够执行Javascript脚本
		webview.getSettings().setJavaScriptEnabled(true);
		if(Util.isNetworkOpen(getActivity())){
			// 加载需要显示的网页
			webview.loadUrl(Const.H5_URL + "Browser/grabBonus/bonusCode/" + mBoundcodes);
			// 设置Web视图
			webview.setWebViewClient(new HelloWebViewClient());
		} else {
			Util.getToastBottom(getActivity(), "请连接网络");

		}
	}

	// Web视图
	private class HelloWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
	}
	
	/**
	 * 点击事件
	 */
	@OnClick({ R.id.iv_turn_in, R.id.ry_serch })
	private void lineBankClick(View v) {
		switch (v.getId()) {
		case R.id.iv_turn_in:
			getMyActivity().finish();
			break;
		}
	}
	
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(MyRedBagListFragment.class.getSimpleName()); 
	}
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(MyRedBagListFragment.class.getSimpleName()); //统计页面
	}
}
