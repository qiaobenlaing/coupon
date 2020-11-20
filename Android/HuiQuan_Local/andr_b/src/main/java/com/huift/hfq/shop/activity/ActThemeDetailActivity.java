package com.huift.hfq.shop.activity;

import java.net.URLDecoder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.GeolocationPermissions.Callback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import com.huift.hfq.shop.R;

import com.huift.hfq.base.Const;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.api.Tools;
import com.huift.hfq.base.model.SetPayResultTask.OnResultListener;
import com.huift.hfq.base.pojo.Share;
import com.huift.hfq.base.utils.ActivityUtils;
import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.base.utils.DialogUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.umeng.analytics.MobclickAgent;

/**
 * 基本的所有H5页面
 * 
 * @author qian.zhou
 */
public class ActThemeDetailActivity extends Activity {

	public final static String TAG = ActThemeDetailActivity.class.getSimpleName();
	public final static String THEME_URL = "themeURl";
	/** 游戏hg */
	private final static String HG = "hg";
	/** HQ*/
	private final static String HQ = "hq";
	/** HS*/
	private final static String HS = "hs";
	/** 返回 */
	private final static String BACK_UP = "backup";
	/** 分享 */
	private final static String SHARE = "share";
	/** 打电话 */
	private final static String CALL = "call";
	/** 请求码 */
	public static final int REQ_CODE = 100;
	/** 成功的返回码 */
	public static final int LOGIN_SUCC = 201;
	/** 加载网页版 */
	private WebView mWebview;
	/** 正在加载的进度条 */
	private ProgressBar mProgView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AppUtils.setActivity(this);
		AppUtils.setContext(getApplicationContext());
		setContentView(R.layout.activity_theme_actdetail);
		ViewUtils.inject(this);
		ActivityUtils.add(this);
		init();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		AppUtils.setActivity(this);
		AppUtils.setContext(getApplicationContext());
	}
	
	/**
	 * 初始化方法
	 */
	private void init() {
		mWebview = (WebView) findViewById(R.id.wb_actdetail);
		mProgView = (ProgressBar) findViewById(R.id.prog_nodata);
		mWebview.getSettings().setJavaScriptEnabled(true);
		// 判断是否有网络
		if (Util.isNetworkOpen(this)) {
			// 设置WebView属性，能够执行Javascript脚本
			mWebview.getSettings().setJavaScriptEnabled(true);
			mWebview.getSettings().setSupportZoom(true);
			mWebview.getSettings().setBuiltInZoomControls(false);
			// 启用数据库
			mWebview.getSettings().setDatabaseEnabled(true);
			String dir = this.getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath(); 
			mWebview.getSettings().setGeolocationDatabasePath(dir);
			// 启用地理位置
			mWebview.getSettings().setGeolocationEnabled(true);
			// 开启DomStorage缓存
			mWebview.getSettings().setDomStorageEnabled(true);
			// 设置权限
			mWebview.setWebChromeClient(new WebChromeClient(){
				// 配置权限
				@Override
				public void onGeolocationPermissionsShowPrompt(String origin, Callback callback) {
					callback.invoke(origin, true, false);
					super.onGeolocationPermissionsShowPrompt(origin, callback);
				}
			});
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
					// 截取支值
					String arr[] = URLDecoder.decode(url).split("://|\\?|=");
					String action = "", https = "", key = "", value = "";

					int size = arr.length;
					for (int i = 0; i < size; i++) {
						if (i == 0) {
							https = action = arr[i];
						}
						if (i == 1) {
							action = arr[i];
						}
						if (i == 2) {
							key = arr[i];
						}
						if (i == 3) {
							value = arr[i];
						}
					}
					Log.d(TAG, "urlH5 : " + URLDecoder.decode(url));
					// 获取分享的字符串
					String sharePrefix = "";
					if (https.equals(HG)) {
						sharePrefix = "hg://share?params=";
					} else if (https.equals(HQ)) {
						sharePrefix = "hq://share?params=";
					} else if (https.equals(HS)) {
						if (SHARE.equals(action)) {
							sharePrefix = "hs://share?params=";
						} 
					}
					String jsonParams = "";
					// 等到分享的字符串
					if (URLDecoder.decode(url).startsWith(sharePrefix)) {
						jsonParams = URLDecoder.decode(url).substring(sharePrefix.length());
					}
					
					// ***** 局部的一些方法
					if (https.equals(HG) || https.equals(HQ) || https.equals(HS)) {
						// 返回
						if (BACK_UP.equals(action)) {
							finish();
						}
						// 分享
						else if (SHARE.equals(action)) {
							setshare(jsonParams);
						}
						// 打电话
						else if (CALL.equals(action)) {
							takePhone(value);
						} 
						return true;
					} else {
						return true;
					}
				}
			});
			
			LinearLayout lyTurnIn = (LinearLayout) findViewById(R.id.ly_turn_in);
			String webURL = this.getIntent().getStringExtra(THEME_URL);
			Log.d(TAG, "webURL >>> " + webURL);
			if (!Util.isEmpty(webURL) && (webURL.contains("http://") || webURL.contains("https://"))) {
				lyTurnIn.setVisibility(View.VISIBLE);
				mWebview.loadUrl(webURL);
			} else {
				lyTurnIn.setVisibility(View.GONE);
				mWebview.loadUrl(Const.H5_URL + webURL);
				Log.d(TAG, "图片路径为：" + Const.H5_URL + webURL);
			}
		} else {
			Util.getToastBottom(this, "请连接网络");
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case REQ_CODE:
			if (resultCode == LOGIN_SUCC) {
				init(); // 重新加载
			}
			break;
		default:
			break;
		}
	}
	
	/**
	 * 分享
	 * @param jsonParams 分享的连接
	 */
	private void setshare (String jsonParams) {
		Share share = Util.json2Obj(jsonParams, Share.class);
		String describe = share.getContent();
		String title = share.getTitle();
		String filePath = Util.isEmpty(share.getIcon()) ? Tools.getFilePath(this) + Tools.APP_ICON : "";
		String logoUrl = Util.isEmpty(share.getIcon()) ? "" : Const.IMG_URL + share.getIcon();
		describe = Util.isEmpty(describe) ? "惠圈" : describe;
		if (!Util.isEmpty(share.getLink())) {
			Tools.showGrameShare(this, share.getLink(), describe, title, filePath, logoUrl);
		}
		
	}
	
	/**
	 * @param telNum 电话号码
	 */
	private void takePhone(final String telNum) {

		DialogUtils.showDialog(this,Util.getString(R.string.dialog_title),Util.getString(R.string.dialog_tel),Util.getString(R.string.dialog_ok),Util.getString(R.string.dialog_cancel),new DialogUtils().new OnResultListener() {
			@Override
			public void onOK() {
				if (!Util.isEmpty(telNum)) {
					Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(getString(R.string.tel) + telNum));
					startActivity(intent);
				} else {
					Util.getContentValidate(R.string.tel_null);
				}
				// 友盟统计
				MobclickAgent.onEvent(ActThemeDetailActivity.this, "myhome_fragment_phone");
			}

		});
	}


	/**
	 * 点击返回查看到活动列表
	 * 
	 * @param view
	 */
	@OnClick(R.id.iv_turn_in)
	public void btnActAddDetailBackClick(View view) {
		ActivityUtils.finishAll();
	}

	/**
	 * 返回按钮事件
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			if (mWebview.canGoBack()) {
				mWebview.goBack();
				return true;
			} else {
				ActivityUtils.finishAll();
				finish();
			}
			break;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

}
