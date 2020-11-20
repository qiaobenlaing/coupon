package com.huift.hfq.cust.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.base.utils.ViewSolveUtils;
import com.huift.hfq.cust.R;

import com.huift.hfq.cust.application.CustConst;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.umeng.analytics.MobclickAgent;

/**
 * 微信公共帐号页面
 * @author qian.zhou
 */
public class SysWechatFragment extends Fragment {
	private static final String TAG = "SysWechatFragment";
	/** 加载网页版 **/
	private WebView mWebview;
	/** 数据加载进度条*/
	private LinearLayout mLyNoData;
	/** 正在加载的进度条*/
	private ProgressBar mProgView;
	/** 没有数据加载的图片*/
	private ImageView mIvView ;
	
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
		init(view);
		return view ;
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
	
	//初始化数据
	private void init(View view) {
		ImageView ivBack = (ImageView) view.findViewById(R.id.iv_turn_in);
		ivBack.setVisibility(View.VISIBLE);
		TextView tvContent = (TextView) view.findViewById(R.id.tv_mid_content);
		tvContent.setText(getResources().getString(R.string.public_weixin));
		mWebview = (WebView) view.findViewById(R.id.wb_actdetail);
		mLyNoData =  (LinearLayout) view.findViewById(R.id.ly_nodata);
		mProgView = (ProgressBar) view.findViewById(R.id.prog_nodata);
		mIvView = (ImageView) view.findViewById(R.id.iv_nodata);
		
		// 设置WebView属性，能够执行Javascript脚本
		mWebview.getSettings().setJavaScriptEnabled(true);
		
		if(Util.isNetworkOpen(getActivity())){
			ViewSolveUtils.setNoData(mWebview, mLyNoData, mIvView, mProgView, CustConst.DATA.LOADIMG); // 正在加载
			mWebview.setWebChromeClient(new WebChromeClient(){
     		   @Override
     			public void onProgressChanged(WebView view, int newProgress) {
     				super.onProgressChanged(view, newProgress);
     				 if(newProgress == 100){  
     					ViewSolveUtils.setNoData(mWebview, mLyNoData, mIvView, mProgView, CustConst.DATA.HAVE_DATA); // 已经加载完了
     			      } 
     			}
     	    });
            //加载需要显示的网页
			mWebview.loadUrl("http://mp.weixin.qq.com/s?__biz=MzA4ODA1NjM1OA==&mid=244465386&idx=1&sn=e501cac4fdba1b9c389183711109d372#rd");
    		// 设置Web视图
			mWebview.setWebViewClient(new HelloWebViewClient());
		} else{
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
		
	/**点击返回图标返回上一级**/
	@OnClick(R.id.iv_turn_in)
	public void ivbackupClick(View view) {
		getMyActivity().finish() ;
	}
	
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(SysWechatFragment.class.getSimpleName());
	}
	
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(SysWechatFragment.class.getSimpleName()); // 统计页面
	}

}
