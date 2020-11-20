package com.huift.hfq.cust.activity;

import java.net.URLDecoder;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import com.huift.hfq.base.Const;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.model.LoginTask;
import com.huift.hfq.base.model.UserTokenModel;
import com.huift.hfq.base.pojo.UserToken;
import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.cust.R;

import com.huift.hfq.cust.application.CustConst;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 新人注册成功界面
 * @author qian.zhou
 */
public class NewRegisterActivity extends Activity {
	/** 加载网页版 **/
	private WebView mWebview;
	/** 正在加载的进度条*/
	private ProgressBar mProgView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_theme_actdetail);
		ViewUtils.inject(this);
		AppUtils.setActivity(this);
		AppUtils.setContext(getApplicationContext());
		Util.addHomeActivity(this);
		init();
	}

	private void init() {
		mWebview = (WebView) findViewById(R.id.wb_actdetail);
		mProgView = (ProgressBar) findViewById(R.id.prog_nodata);
		UserToken token = UserTokenModel.getHomeById("1001");
		// 设置WebView属性，能够执行Javascript脚本
		mWebview.getSettings().setJavaScriptEnabled(true);
		if(Util.isNetworkOpen(this)){
			mWebview.getSettings().setSupportZoom(true);
			mWebview.getSettings().setBuiltInZoomControls(false);
			mWebview.setWebViewClient(new WebViewClient() {
				@Override
				public void onPageStarted(WebView view, String url, Bitmap favicon) {
					super.onPageStarted(view, url, favicon);
					mProgView.setVisibility(View.VISIBLE);
				}
				// h5加载完
				@Override
				public void onPageFinished(WebView view, String url) {
					mProgView.setVisibility(View.GONE);
				}
				// 加载出错
				@Override
				public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
					super.onReceivedError(view, errorCode, description, failingUrl);
				}
				// 拦截
				@Override
				public boolean shouldOverrideUrlLoading(WebView view, String url) {
					String arr[] = URLDecoder.decode(url).split("://|\\?|=");
					String action = "", https = "" ,key = "", value = "";
					int size = arr.length;
					for (int i = 0; i < size; i++) {
						if (i == 0) {
							https = action = arr[i];
						}
						  if (i == 1) {
							action = arr[i];
						} if (i == 2) {	
							key = arr[i];
						} if (i == 3) {
							value = arr[i];
						}
					} 
				if (https.equals("hq")) {
					boolean mLoginFlag = DB.getBoolean(DB.Key.CUST_LOGIN);
					Log.d("TAG", "路径是：：" + action);
					System.out.println("action is " + action + " key is " + key + " value is " + value);
					if ("getUserInviteCode".equals(action)) {//我的推荐码页
						finish();
						Intent codeIntent = new Intent(NewRegisterActivity.this, MyRecommonedActivity.class);
						startActivity(codeIntent);
					} else if ("getUserCouponInfo".equals(action)) {
						Intent couponIntent = new Intent(NewRegisterActivity.this, HomeActivity.class);
						couponIntent.putExtra(LoginTask.ALL_LOGIN, CustConst.Login.COUPON_LOGIN);
						startActivity(couponIntent);
						if (null != Util.homeActivityList || Util.homeActivityList.size() > 0) {
							Util.exitHome();
						}
					}
					return true;
				} else {
					return false;
				}
				}
			});
			mWebview.loadUrl(Const.H5_URL + "Browser/regSucc?userCode=" + token.getUserCode());
		} else{
			Util.getToastBottom(this, "请连接网络");
		}
	}
      
	/**
	 * 点击返回查看到活动列表
	 * @param view
	 */
	@OnClick(R.id.iv_turn_in)
	public void btnActAddDetailBackClick(View view) {
		Intent couponIntent = new Intent(this, HomeActivity.class);
		startActivity(couponIntent);
		if (null != Util.homeActivityList || Util.homeActivityList.size() > 0) {
			Util.exitHome();
		}
	}
	
	/**
	 * 重写点击返回按钮方法，点击一次土司提示，两次退出程序
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent couponIntent = new Intent(this, HomeActivity.class);
			startActivity(couponIntent);
			if (null != Util.homeActivityList || Util.homeActivityList.size() > 0) {
				Util.exitHome();
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	 public void onResume(){
	    	super.onResume();
	        AppUtils.setActivity(this);
	        AppUtils.setContext(getApplicationContext());
	 }
}
