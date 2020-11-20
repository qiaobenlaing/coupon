package com.huift.hfq.shop.fragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.huift.hfq.shop.R;

import com.huift.hfq.base.Util;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 微信公共帐号页面
 * @author qian.zhou
 */
public class SysWechatFragment extends Fragment {
	private static final String TAG = "SysWechatFragment";
	/** 加载网页版 **/
	private WebView webview;
	/** 加载进度条 */
	private ProgressDialog progressDialog;
	
	/**
	 * 需要传递参数时有利于解耦
	 * @return SysWechatFragment
	 */
	public static SysWechatFragment newInstance() {
		Bundle args = new Bundle() ;
		SysWechatFragment fragment = new SysWechatFragment() ;
		fragment.setArguments(args) ;
		return fragment ;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater , ViewGroup container ,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_syswechat , container ,false) ;
		ViewUtils.inject(this, view) ;
		Util.addLoginActivity(getActivity());
		init(view);
		return view ;
	}
	
	//初始化数据
	private void init(View view) {
		LinearLayout ivBack = (LinearLayout) view.findViewById(R.id.layout_turn_in);
		ivBack.setVisibility(View.VISIBLE);
		TextView tvContent = (TextView) view.findViewById(R.id.tv_mid_content);
		tvContent.setText(getResources().getString(R.string.public_weixin));
		webview = (WebView) view.findViewById(R.id.wb_actdetail);
		// 设置WebView属性，能够执行Javascript脚本
		webview.getSettings().setJavaScriptEnabled(true);
		// 创建ProgressDialog对象
        progressDialog = new ProgressDialog(getActivity());
        // 设置进度条风格，风格为圆形，旋转的
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // 设置ProgressDialog 提示信息
        progressDialog.setMessage("正在加载数据.....");
        // 设置ProgressDialog 的进度条是否不明确
        progressDialog.setIndeterminate(false);
        progressDialog.show();
        webview.setWebChromeClient(new WebChromeClient(){
 		   @Override
 			public void onProgressChanged(WebView view, int newProgress) {
 				super.onProgressChanged(view, newProgress);
 				 if(newProgress == 100){  
 					progressDialog.dismiss();
 			      } 
 			}
 	    });
		// 加载需要显示的网页
		webview.loadUrl("http://mp.weixin.qq.com/s?__biz=MzA4ODA1NjM1OA==&mid=244465386&idx=1&sn=e501cac4fdba1b9c389183711109d372#rd");
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
		
	/**点击返回图标返回上一级**/
	@OnClick(R.id.layout_turn_in)
	public void ivbackupClick(View view) {
		getActivity().finish() ;
	}

}
