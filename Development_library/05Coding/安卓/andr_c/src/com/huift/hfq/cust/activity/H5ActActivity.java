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
import com.huift.hfq.base.api.Tools;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.model.LoginTask;
import com.huift.hfq.base.pojo.Share;
import com.huift.hfq.base.pojo.UserToken;
import com.huift.hfq.base.utils.ActivityUtils;
import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.cust.R;

import com.lidroid.xutils.ViewUtils;

/**
 * 活动的分享
 * @author yanfang.li
 */
public class H5ActActivity extends Activity {

	public final static String TAG = H5ShopDetailActivity.class.getSimpleName();
	/** 活动的code */
	public final static String ACTIVITY_CODE = "activityCode";
	/** 活动的url*/
	public final static String ACTIVITY_URL = "activityURL";
	/** 我的优惠券 */
	/** https的类型 hs */
	private final static String HS = "hs";
	/** 游戏hg */
	private final static String HG = "hg";
	/** 登录 */
	private final static String LOGIN = "login";
	/** 返回 */
	private final static String BACK_UP = "backup";
	/** 分享 */
	private final static String SHARE = "share";
	/** 请求码 */
	public static final int REQ_CODE = 100;
	/** 成功的返回码 */
	public static final int LOGIN_SUCC = 201;
	/** api返回的失败 0 */
	public static final int API_FAIL = 0;

	/** 加载网页版 */
	private WebView mWebview;
	/** 正在加载的进度条 */
	private ProgressBar mProgView;
	/** 用户编码 */
	private String mUserCode = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AppUtils.setActivity(this);
		AppUtils.setContext(getApplicationContext());
		setContentView(R.layout.activity_h5);
		ActivityUtils.add(this);
		ViewUtils.inject(this);
		init();
	}

	public void onResume() {
		super.onResume();
		AppUtils.setActivity(this);
		AppUtils.setContext(getApplicationContext());
	}

	private void init() {
		mWebview = (WebView) findViewById(R.id.wb_actdetail);
		mProgView = (ProgressBar) findViewById(R.id.prog_nodata);
		String activityCode = this.getIntent().getStringExtra(ACTIVITY_CODE);
		String activityURl = this.getIntent().getStringExtra(ACTIVITY_URL);
		if (DB.getBoolean(DB.Key.CUST_LOGIN)) {
			UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
			mUserCode = userToken.getUserCode();
		} else {
			mUserCode = "";
		}
		// 判断是否有网络
		if (Util.isNetworkOpen(this)) {
			// 设置WebView属性，能够执行Javascript脚本
			mWebview.getSettings().setJavaScriptEnabled(true);
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

				@Override
				public boolean shouldOverrideUrlLoading(WebView view, String url) {
					String sharePrefix = "hg://share?params=";
					String jsonParams = "";
					// 等到分享的字符串
					if (URLDecoder.decode(url).startsWith(sharePrefix)) {
						jsonParams = URLDecoder.decode(url).substring(sharePrefix.length());
					}
					Log.d(TAG, "sharePrefix >> " + sharePrefix);
					Log.d(TAG, "sharePrefix.length >> length >> " + sharePrefix.length());

					String arr[] = URLDecoder.decode(url).split("://|\\?");
					String action = "", https = "";
					int size = arr.length;
					for (int i = 0; i < size; i++) {
						if (i == 0) {
							https = arr[i];
						}
						if (i == 1) {
							action = arr[i];
						}
					}
					Log.d(TAG, "sharePrefix. jsonString  >> length >> " + URLDecoder.decode(url));
					if (HG.equals(https)) {
						// 返回
						if (BACK_UP.equals(action)) {
							finish();
						}
						// 登录
						else if (LOGIN.equals(action)) {
							login(Const.Login.H5_ACT);
						}
						// 分享
						else if (SHARE.equals(action)) {
							Share share = Util.json2Obj(jsonParams, Share.class);
							String describe = share.getContent();
							String title = share.getTitle();
							String shareURL = share.getLink();
							String filePath = "";
							String logoUrl = share.getIcon();
							Tools.showGrameShare(H5ActActivity.this, shareURL, describe, title, filePath, logoUrl);
						}
						return true;
					}
					// 其他
					else {
						return false;
					}

				}

			});
			Log.d(TAG, "userCodeiiiii >>> " + activityURl + ", activityCode >>> " +activityCode);
			mWebview.loadUrl(Const.H5_URL + activityURl + activityCode + "&userCode=" + mUserCode);
		} else {
			Util.getToastBottom(this, "请连接网络");
			mProgView.setVisibility(View.GONE);
		}
	}

	/**
	 * 跳转登
	 */
	private void login(String loginType) {
		Intent intent = new Intent(this, LoginActivity.class);
		intent.putExtra(LoginTask.ALL_LOGIN, loginType);
		startActivityForResult(intent, REQ_CODE);
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
				finish();
			}
			break;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
}
