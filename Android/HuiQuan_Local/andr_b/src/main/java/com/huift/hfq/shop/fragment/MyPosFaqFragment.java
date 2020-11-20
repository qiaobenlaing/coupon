package com.huift.hfq.shop.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.huift.hfq.shop.R;

import com.huift.hfq.base.Util;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * POS FAQ刷卡问题解答
 * 
 * @author qian.zhou
 */
public class MyPosFaqFragment extends Fragment {
	private static final String TAG = "MyPosFaqFragment";
	/** 加载网页版 **/
	private WebView webview;

	/**
	 * 需要传递参数时有利于解耦
	 */
	public static MyPosFaqFragment newInstance() {
		Bundle args = new Bundle();
		MyPosFaqFragment fragment = new MyPosFaqFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_myposfaq, container, false);
		ViewUtils.inject(this, view);
		Util.addLoginActivity(getActivity());
		init(view);
		return view;
	}

	// 初始化方法
	private void init(View view) {
		LinearLayout ivBack = (LinearLayout) view.findViewById(R.id.layout_turn_in);// 返回
		ivBack.setVisibility(View.VISIBLE);// 显示
		ivBack.setBackgroundResource(R.drawable.backup);
		TextView tvTitle = (TextView) view.findViewById(R.id.tv_mid_content);
		tvTitle.setText(getResources().getString(R.string.pos_problem));
		TextView tvMsg = (TextView) view.findViewById(R.id.tv_msg);
		tvMsg.setVisibility(View.GONE);

		webview = (WebView) view.findViewById(R.id.wb_faq_actdetail);
		// 设置WebView属性，能够执行Javascript脚本
		webview.getSettings().setJavaScriptEnabled(true);
		// 加载需要显示的网页
		webview.loadUrl("http://baomi.suanzi.cn/Api/Browser/posFaq");
		// 设置Web视图
		webview.setWebViewClient(new HelloWebViewClient());
	}

	// Web视图
	private class HelloWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
	}

	/** 点击返回图标返回上一级 **/
	@OnClick(R.id.layout_turn_in)
	public void ivbackupClick(View view) {
		getActivity().finish();
	}
}
