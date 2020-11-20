package com.huift.hfq.cust.activity;

import java.net.URLDecoder;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.huift.hfq.base.Const;
import com.huift.hfq.base.ErrorCode;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.api.Tools;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.model.LoginTask;
import com.huift.hfq.base.pojo.BatchCoupon;
import com.huift.hfq.base.pojo.Citys;
import com.huift.hfq.base.pojo.Share;
import com.huift.hfq.base.pojo.UserToken;
import com.huift.hfq.base.utils.ActivityUtils;
import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.base.utils.DialogUtils;
import com.huift.hfq.cust.R;

import com.huift.hfq.cust.application.CustConst;
import com.huift.hfq.cust.fragment.BuyPromotionFragment;
import com.huift.hfq.cust.fragment.HomeFragment;
import com.huift.hfq.cust.model.GrabCouponTask;
import com.huift.hfq.cust.util.SkipActivityUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.umeng.analytics.MobclickAgent;

/**
 * 基本的所有H5页面
 * 
 * @author qian.zhou
 */
@SuppressLint("SetJavaScriptEnabled")
public class ActThemeDetailActivity extends Activity {

	public final static String TAG = ActThemeDetailActivity.class.getSimpleName();
	public final static String TYPE = "type";
	public final static String THEME_URL = "themeURl";
	public final static String ACTIVITY_CODE = "activityCode";
	public final static String BOUNDS_CODE = "boundsCode";
	/** 游戏hg */
	private final static String HG = "hg";
	/** HQ*/
	private final static String HQ = "hq";
	/** HS*/
	private final static String HS = "hs";
	/** 登录 */
	private final static String LOGIN = "login";
	/** 返回 */
	private final static String BACK_UP = "backup";
	/** 分享 */
	private final static String SHARE = "share";
	/** 退款*/
	private final static String REFUND = "refund";
	/** 打电话 */
	private final static String CALL = "call";
	/** 活动预定*/
	private final static String ORDER_ACT = "orderAct";
	/** 进入店铺*/
	private final static String GOTO_SHOP = "gotoShop";
	/** 进入店铺详情*/
	private final static String GET_SHOP_INFO = "getShopInfo";
	/** 抢优惠券*/
	private final static String ROB_COUPON = "robCoupon";
	/** 注册*/
	private final static String REGISTER = "register";
	/** 优惠券列表*/
	private final static String GET_USER_COUPONINFO = "getUserCouponInfo";
	/** 验证码*/
	private final static String GET_USER_INVITE_CODE = "getUserInviteCode";
	/** 请求码 */
	public static final int REQ_CODE = 100;
	/** 成功的返回码 */
	public static final int LOGIN_SUCC = 201;
	/** 加载网页版 */
	private WebView mWebview;
	/** 所属类别 */
	private String mType;
	/** 正在加载的进度条 */
	private ProgressBar mProgView;
	/** 用户编码 */
	private String mUserCode;
	/** 用于加载支付结果后 返回键跳转到店铺详情界面 */
	private String mShopCode;
	/** 回退箭*/
	private LinearLayout mLyTurnIn;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AppUtils.setActivity(this);
		AppUtils.setContext(getApplicationContext());
		setContentView(R.layout.activity_theme_actdetail);
		ViewUtils.inject(this);
		Util.addHomeActivity(this); // 注册
		ActivityUtils.add(this);
		init();
	}
	
	/**
	 * 出事化方法
	 */
	private void init() {
		mLyTurnIn = (LinearLayout) findViewById(R.id.ly_turn_in);
		Intent intent = this.getIntent();
		mType = intent.getStringExtra(TYPE);
		Log.d(TAG, "mType = " + mType);
		mWebview = (WebView) findViewById(R.id.wb_actdetail);
		mProgView = (ProgressBar) findViewById(R.id.prog_nodata);
		if (DB.getBoolean(DB.Key.CUST_LOGIN)) {
			UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
			mUserCode = userToken.getUserCode();
		} else {
			mUserCode = "";
		}

		// 设置WebView属性，能够执行Javascript脚本
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
						} else if (REFUND.equals(action)) {
							sharePrefix = "hs://refund?params=";
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
						// 登录
						else if (LOGIN.equals(action)) {
							SkipActivityUtil.login(Const.Login.ACT_THEME, REQ_CODE);
						}
						// 分享
						else if (SHARE.equals(action)) {
							setshare(jsonParams);
						}
						// 进入店铺
						else if (GOTO_SHOP.equals(action)) {
							Intent shop = new Intent(ActThemeDetailActivity.this, ShopDetailActivity.class);
							shop.putExtra("shopCode", value);
							startActivity(shop);
							finish();
						} 
						// 抢优惠券
						else if (ROB_COUPON.equals(action)) {
							grabCoupon(value);
							
						}
						// 店铺详情的h5
						else if (action.contains(GET_SHOP_INFO)) {
							SkipActivityUtil.skipNewShopDetailActivity(ActThemeDetailActivity.this, value);
						}
						// 注册
						else if (REGISTER.equals(action)) { 
							if (DB.getBoolean(DB.Key.CUST_LOGIN)) {
								Intent newIntent = new Intent(ActThemeDetailActivity.this, NewRegisterActivity.class);
								startActivity(newIntent);
								finish();
							} else {
								Intent reginIntent = new Intent(ActThemeDetailActivity.this, RegisterActivity.class);
								startActivity(reginIntent);
							}
						} 
						// 优惠券列表
						else if (GET_USER_COUPONINFO.equals(action)) {
							Intent couponIntent = new Intent(ActThemeDetailActivity.this, HomeActivity.class);
							couponIntent.putExtra(LoginTask.ALL_LOGIN, CustConst.Login.COUPON_LOGIN);
							startActivity(couponIntent);
							if (null != Util.homeActivityList || Util.homeActivityList.size() > 0) {
								Util.exitHome();
							}
						} 
						// 我的推荐码页
						else if (GET_USER_INVITE_CODE.equals(action)) {
							Intent codeIntent = new Intent(ActThemeDetailActivity.this, MyRecommonedActivity.class);
							startActivity(codeIntent);
						} 
						// 打电话
						else if (CALL.equals(action)) {
							takePhone(value);
						} else if (REFUND.equals(action)) {
							Intent intent = new Intent(ActThemeDetailActivity.this, ActThemeDetailActivity.class);
							intent.putExtra(ActThemeDetailActivity.TYPE, CustConst.HactTheme.HOME_ACTIVITY);
							intent.putExtra(ActThemeDetailActivity.THEME_URL, jsonParams);
							startActivity(intent);
						} 
						// 预定活动
						else if (ORDER_ACT.equals(action)) {
							Intent intent = new Intent(ActThemeDetailActivity.this, BuyPromotionActivity.class);
							intent.putExtra(BuyPromotionFragment.ACTIVITY, value);
							startActivity(intent);
						}
						return true;
					} else {
						return true;
					}
				}
			});

			// 首页的活动图片
			if (CustConst.HactTheme.HOME_ACTIVITY.equals(mType)) {
				LinearLayout lyTurnIn = (LinearLayout) findViewById(R.id.ly_turn_in);
				String webURL = this.getIntent().getStringExtra(THEME_URL);
				Log.d(TAG, "webURL >>> " + webURL);
				if (!Util.isEmpty(webURL) && (webURL.contains("http://") || webURL.contains("https://"))) {
					lyTurnIn.setVisibility(View.VISIBLE);
					mWebview.loadUrl(webURL);
				} else {
					lyTurnIn.setVisibility(View.GONE);
					mWebview.loadUrl(Const.H5_URL + webURL + "&userCode=" + mUserCode);
				}

			}
			// 惠币的介绍
			else if (CustConst.HactTheme.HUIBI_INTRO.equals(mType)) {
				mWebview.loadUrl(Const.H5_URL + "Browser/huibiIntro");
			}
			// 注册活动
			else if (CustConst.HactTheme.NEW_REGISTER.equals(mType)) {
				mWebview.loadUrl(Const.H5_URL + "Browser/regAct");
			}
			// 法律协议
			else if (CustConst.HactTheme.HONE_SYSTERM.equals(mType)) {
				mWebview.loadUrl(Const.H5_URL + "Browser/cProtocol");
			}
			// 支付成功
			else if (CustConst.HactTheme.PAY_SUCCESS.equals(mType)) {
				String consumeCode = intent.getStringExtra("consumeCode");
				mShopCode = intent.getStringExtra("shop_code");
				mWebview.loadUrl(Const.H5_URL + "Browser/paySucc?consumeCode=" + consumeCode);
			}
			// 支付失败
			else if (CustConst.HactTheme.PAY_Fail.equals(mType)) {
				mShopCode = intent.getStringExtra("shop_code");
				mWebview.loadUrl(Const.H5_URL + "Browser/payFail");
			} 
			// 关于惠圈
			else if (CustConst.HactTheme.ABOUT_HUIQUAN.equals(mType)) {
				mWebview.loadUrl(Const.H5_URL + "Browser/cAbout");
			}
			//使用优惠券码成功
			else if(CustConst.HactTheme.PAY_COUPON_USE.equals(mType)){
				// 获取值
				SharedPreferences preferences =getSharedPreferences(CustConst.Key.CITY_OBJ,Context.MODE_PRIVATE);
				String cityName = preferences.getString(CustConst.Key.CITY_NAME, null);
				String longitude ,latitude;
				if (Util.isEmpty(cityName)) {
					Citys citys = DB.getObj(HomeFragment.CITYS, Citys.class);
					longitude = String.valueOf(citys.getLongitude());
					latitude = String.valueOf(citys.getLatitude());
				} else {
					longitude = preferences.getString(CustConst.Key.CITY_LONG, null);
					latitude = preferences.getString(CustConst.Key.CITY_LAT, null);
				}
				
				mLyTurnIn.setVisibility(View.GONE);
				String couponCode = intent.getStringExtra("COUPONCODE");
				Log.d(TAG, "longitude="+longitude+"&latitude="+latitude);
				mWebview.loadUrl(Const.H5_URL + "Browser/exchangeCouponRet?couponCode="+couponCode+"&longitude="+longitude+"&latitude="+latitude);
			}
			//商品/服务的h5详情
			else if(CustConst.HactTheme.SHOP_PRODUCT_SERVICE.equals(mType)){
				int productId = getIntent().getIntExtra("productId", 0);
				mLyTurnIn.setVisibility(View.GONE);
				mWebview.loadUrl(Const.H5_URL+"Browser/shopProductInfo?productId="+productId+"&userCode="+mUserCode);
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
	 * 抢优惠券
	 * @param value
	 */
	
	private void grabCoupon (String value) {

		if (DB.getBoolean(DB.Key.CUST_LOGIN)) {
			/** 获得一个用户信息对象 **/
			BatchCoupon batchCoupon = new BatchCoupon();
			batchCoupon.setBatchCouponCode(value);
			new GrabCouponTask(ActThemeDetailActivity.this, new GrabCouponTask.Callback() {
				@Override
				public void getResult(int resultCode) {
					if (resultCode == ErrorCode.SUCC) { 
						Intent Intent = new Intent(ActThemeDetailActivity.this, HomeActivity.class);
						Intent.putExtra(LoginTask.ALL_LOGIN, CustConst.Login.COUPON_LOGIN);
						startActivity(Intent);
						finish();
					} else {
						Log.d(TAG, "失败");
						Util.getContentValidate(R.string.coupon_failed);
					}
				}
			}).execute(value, "2");
		} else {
			SkipActivityUtil.login(Const.Login.ACT_THEME, REQ_CODE);
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
		String shareURL = share.getLink();
		String filePath = Util.isEmpty(share.getIcon()) ? Tools.getFilePath(ActThemeDetailActivity.this) + Tools.APP_ICON : "" ;
		String logoUrl = Util.isEmpty(share.getIcon()) ? "" : Const.IMG_URL + share.getIcon() ;
		describe = Util.isEmpty(describe) ? "惠圈" : describe;
		Tools.showGrameShare(ActThemeDetailActivity.this, shareURL, describe, title, filePath,logoUrl);
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
	 * 从支付结果页面跳转到H5店铺详情页面
	 */
	public void skipToH5ShopDetail() {
		if (CustConst.HactTheme.PAY_SUCCESS.equals(mType) || CustConst.HactTheme.PAY_Fail.equals(mType)) {
			Intent intent = new Intent(this, H5ShopDetailActivity.class);
			intent.putExtra(H5ShopDetailActivity.SHOP_CODE, mShopCode);
			this.startActivity(intent);
		} else {
			finish();
		}
	}

	/**
	 * 点击返回查看到活动列表
	 * 
	 * @param view
	 */
	@OnClick(R.id.iv_turn_in)
	public void btnActAddDetailBackClick(View view) {
		ActivityUtils.finishAll();
		skipToH5ShopDetail();
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
			} else if (CustConst.HactTheme.NEW_REGISTER.equals(mType)){
				ActivityUtils.finishAll();
				finish();
			} else {
				skipToH5ShopDetail();
			}
			break;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(ActThemeDetailActivity.class.getSimpleName()); 
	}
	
	@Override
	public void onResume() {
		super.onResume();
		AppUtils.setActivity(this);
		AppUtils.setContext(getApplicationContext());
		MobclickAgent.onPageStart(ActThemeDetailActivity.class.getSimpleName()); //统计页面
	}
}
