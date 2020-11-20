package com.huift.hfq.cust.activity;

import java.net.URLDecoder;

import com.huift.hfq.base.Const;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.pojo.NewShop;
import com.huift.hfq.base.pojo.Shop;
import com.huift.hfq.base.pojo.UserToken;
import com.huift.hfq.base.utils.ActivityUtils;
import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.cust.R;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.GeolocationPermissions.Callback;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class NewShopInfoActivtyToH5Activity extends Activity {
	protected static final String TAG = NewShopInfoActivtyToH5Activity.class.getSimpleName();
	/** 加载网页版 */
	private WebView mWebview;
	/** 正在加载的进度条 */
	private ProgressBar mProgView;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_h5);
		AppUtils.setActivity(this); 
		AppUtils.setContext(getApplicationContext());
		ActivityUtils.add(this);
		
		mWebview = (WebView) findViewById(R.id.wb_actdetail);
		mProgView = (ProgressBar) findViewById(R.id.prog_nodata);
		
		String actionType = getIntent().getStringExtra("ACTION_TYPE");
		Shop shop = (Shop) getIntent().getSerializableExtra("SHOP_INFO");
		
		String userCode = "";
		if (DB.getBoolean(DB.Key.CUST_LOGIN)) {
			UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
			userCode = userToken.getUserCode();
		} else {
			userCode = "";
		}
		
		
		
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
			
			mWebview.setWebViewClient(new WebViewClient(){
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
				
				//点击
				@Override
				public boolean shouldOverrideUrlLoading(WebView view, String url) {
					String arr[] = URLDecoder.decode(url).split("://|\\?");
					String action = "", key = "", value = "", https = "";
					
					
					https = arr[0];
					action = arr[1];
					key = arr[2];
					
					Log.d(TAG, "https==="+https);
					Log.d(TAG, "action==="+action);
					Log.d(TAG, "key==="+key);
					
					String valueAr[] = key.split("://|\\?|=|\\&");
					value = valueAr[1];
					Log.d(TAG, "value==="+value);
					
					for (int i = 0; i < valueAr.length; i++) {
						Log.d(TAG,"valueAr[" +i+"]==="+valueAr[i]);
					}
					
					/*for (int i = 0; i < size; i++) {
						if (i == 0) {
							https = arr[i];
						}
						if (i == 1) {
							action = arr[i];
						}
						if (i == 2) {
							key = arr[i];
							//String valueAr[] = key.split("://|\\?|=|\\&");
							Log.d(TAG, "hsurl  ==== valueAr = " + key);
							for (int m = 0; m < valueAr.length; m++) {
								Log.d(TAG, "hsurl  ==== valueAr = " + m +" ---  "+ valueAr[m]);
								if (m == 1) {
									value = valueAr[m];
								}
								if (m < (valueAr.length - 1)) {
									if (ORDER_CODE.equals(valueAr[m])) { // 订单编码
										mOrderCode = valueAr[m + 1];
										Log.d(TAG, "hsurl  ==== valueAr = " + valueAr[m] +" ---  "+ valueAr[m + 1]);
									} else if (ARRRESS_ID.equals(valueAr[m])) { // 地址的编码
										mUserAddressId = valueAr[m + 1];
										Log.d(TAG, "hsurl  ==== valueAr = " + valueAr[m] +" ---  "+ valueAr[m + 1]);
									} else if (BATCH_COUPON_CODE.equals(valueAr[m])) { // 批次号
										mBatchCouponCode = valueAr[m + 1];
									} else if (COUPON_TYPE.equals(valueAr[m])) {
										mCouponType = valueAr[m + 1];
									}
								}
							}
						}
					}*/
					return super.shouldOverrideUrlLoading(view, url);
				}

			});
			
			
			//加载网页
			if("take_order".equals(actionType)){ //点餐
				mWebview.loadUrl(Const.H5_URL + "Browser/cEatOrder?shopCode=" + shop.getShopCode() + "&userCode=" + userCode);
			}else if("take_out".equals(actionType)){ //外卖
				mWebview.loadUrl(Const.H5_URL + "Browser/cTakeawayOrder?shopCode=" + shop.getShopCode() + "&userCode=" + userCode);
			}else if("book_food".equals(actionType)){ //预定
				//TODO
			}else if("order_food".equals(actionType)){ //订单
				mWebview.loadUrl(Const.H5_URL + "Browser/cGetOrder??shopCode=" + shop.getShopCode() + "&userCode=" + userCode);
			}
			
		}else{
			Util.getToastBottom(this, "请连接网络");
			mProgView.setVisibility(View.GONE);
		}
		
		
		
		
	}
}
