package cn.suanzi.baomi.cust.activity;

import java.io.Serializable;
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
import android.widget.ProgressBar;
import cn.jpush.android.util.ac;
import cn.suanzi.baomi.base.Const;
import cn.suanzi.baomi.base.ErrorCode;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.data.DB;
import cn.suanzi.baomi.base.model.LoginTask;
import cn.suanzi.baomi.base.pojo.BatchCoupon;
import cn.suanzi.baomi.base.pojo.Messages;
import cn.suanzi.baomi.base.pojo.Shop;
import cn.suanzi.baomi.base.pojo.User;
import cn.suanzi.baomi.base.pojo.UserToken;
import cn.suanzi.baomi.base.utils.ActivityUtils;
import cn.suanzi.baomi.base.utils.AppUtils;
import cn.suanzi.baomi.base.utils.DialogUtils;
import cn.suanzi.baomi.base.utils.ShareCouponUtil;
import cn.suanzi.baomi.cust.R;
import cn.suanzi.baomi.cust.application.CustConst;
import cn.suanzi.baomi.cust.fragment.BatchCouponDetailFragment;
import cn.suanzi.baomi.cust.fragment.CouponBuyFragment;
import cn.suanzi.baomi.cust.fragment.CouponDetailFragment;
import cn.suanzi.baomi.cust.fragment.ShopDetailFragment;
import cn.suanzi.baomi.cust.fragment.ShopPayBillFragment;
import cn.suanzi.baomi.cust.fragment.VipChatFragment;
import cn.suanzi.baomi.cust.model.AddNotTakeoutOrderTask;
import cn.suanzi.baomi.cust.model.AddTakeoutOrderTask;
import cn.suanzi.baomi.cust.model.GetUserInfo;
import cn.suanzi.baomi.cust.model.GetUserShopRecordTask;
import cn.suanzi.baomi.cust.util.CommUtils;
import cn.suanzi.baomi.cust.util.SkipActivityUtil;

import com.lidroid.xutils.ViewUtils;
import com.umeng.analytics.MobclickAgent;

/**
 * 商店详情的H5 
 * @author yanfang.li
 */
public class H5ShopDetailActivity extends Activity {

	public final static String TAG = H5ShopDetailActivity.class.getSimpleName();
	/** 从外卖支付成功后返回的订单详情的orderCode */
	public static final String PAY_RESULT_ORDERCODE = "payresultcode";
	/** h5的类型*/
	public static final String TYPE = "type";
	/** 店铺的code */
	public final static String SHOP_CODE = "shopCode";
	/** 我的优惠券 */
	public final static String USER_COUPON_CODE = "useCouponCode";
	/** https的类型 hs*/
	private final static String HS = "hs";
	/** 订单ho*/
	private final static String HO = "ho";
	/** 登录 */
	private final static String LOGIN = "login";
	/** 去别的商店逛逛 */
	private final static String SHOP = "shop";
	/** 返回 */
	private final static String BACK_UP = "backup";
	/** 打电话 */
	private final static String CALL = "call";
	/** 分享 */
	private final static String SHARE = "share";
	/** 买单 */
	private final static String ICBC_PAY = "icbcPay";
	/** 进入批次优惠券的详情界面 */
	private final static String BATCH_COUPON_INFO = "batchCouponInfo";
	/** 进入单张优惠券的详情界面 */
	private final static String COUPON_INFO = "couponInfo";
	/** 留言 */
	private final static String FEED_BACK = "feedback";
	/** 查看相册 */
	private final static String SHOP_ALBUM = "shopAlbum";
	/** 使用优惠券 */
	private final static String USE_COUPON = "useCoupon";
	/** 查看大图 */
	private final static String BIG_IMG = "bigImg";
	/** 优惠券购买*/
	private final static String COUPON_PAY = "couponPay";
	/** 进入活动界面*/
	private final static String GET_ACT_INFO = "getActInfo";
	/** 实物券和体验券的使用 */
	public static final String PAY_STATUS = "payStatus";
	/** 点餐 */
	public static final String ORDER = "order";
	/** 外卖 */
	public static final String TAKE_OUT = "takeOut";
	/** 预定 */
	public static final String RESERVE = "reserve";
	/** 订单 */
	public static final String ORDER_LIST = "orderList";
	/** 订单编码 */
	public static final String ORDER_CODE = "orderCode";
	/** 地址 */
	public static final String ARRRESS_ID = "userAddressId";
	/** 优惠券批次号*/
	public static final String BATCH_COUPON_CODE = "batchCouponCode";
	/** 优惠类型*/
	public static final String COUPON_TYPE = "couponType";
	/** 进入店铺详情*/
	private final static String GET_SHOP_INFO = "getShopInfo";
	/** 纠错*/
	public static final String JIU_CUO = "jiucuo";
	/** 请求码 */
	public static final int REQ_CODE = 100;
	/** 成功的返回码 */
	public static final int LOGIN_SUCC = 201;
	/** api返回的失败 0 */
	public static final int API_FAIL = 0;
	/** 分享的描述内容*/
	public static final String SHARE_DESCRIBE = "describe";
	/** 分享的标题*/
	public static final String SHARE_TITLE = "title";
	/** 加载网页版 */
	private WebView mWebview;
	/** 正在加载的进度条 */
	private ProgressBar mProgView;
	/** 当前处在哪里 */
	private static int mCurrentPage = 0;
	/** 当前处在待结单中 */
	private static int mReceiveOrderPage = 0;
	/** 商店编码 */
	private String mShopCode = "";
	/** 用户编码 */
	private String mUserCode = "";
	/** 订单编码 */
	private String mOrderCode;
	/** 地址的Id */
	private String mUserAddressId;
	/** 加载的类型 */
	private String mType;
	/** 批次优惠券*/
	private String mBatchCouponCode;
	/** 优惠券类型*/
	private String mCouponType;
	
	/**
	 * 设置当前返回参数
	 */
	public static void setCurrentPage (String type) {
		if (type.equals("backup")) {
			mCurrentPage = 3;
			mReceiveOrderPage = 0;
		} else {
			mReceiveOrderPage = 4;
		}
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_h5);
		AppUtils.setActivity(this); 
		AppUtils.setContext(getApplicationContext());
		ActivityUtils.add(this);
		ViewUtils.inject(this); // Xutil的控件
		init();
	}

	private void init() {
		mWebview = (WebView) findViewById(R.id.wb_actdetail);
		mProgView = (ProgressBar) findViewById(R.id.prog_nodata);
		
		mType = this.getIntent().getStringExtra(TYPE);
		mShopCode = this.getIntent().getStringExtra(SHOP_CODE);
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

				@Override
				public boolean shouldOverrideUrlLoading(WebView view, String url) {
					// 外卖的堂食
					if (url.startsWith(Const.H5_URL + "Browser/cEatOrder?")
							|| url.startsWith(Const.H5_URL + "Browser/cTakeawayOrder")
							|| url.startsWith(Const.H5_URL + "Browser/cGetOrder?")) {
						mCurrentPage = 1;
					}
					// 商店详情
					else if (url.startsWith(Const.H5_URL + "Browser/getShopInfo/shopCode/")) {
						mCurrentPage = 0;
					}
					// 外卖订单
					else if (url.startsWith(Const.H5_URL + "Browser/cOrderSubmit?")) {
						mCurrentPage = 2;
					}
					// 堂食订单
					else if (url.startsWith(Const.H5_URL + "Browser/cEatSubmit?")) {
						mCurrentPage = 3;
					}
					// 选择地址
					else if (url.startsWith(Const.H5_URL + "Browser/getAddressList?")) {
						mCurrentPage = 4;
					}
					// 填写自己接受地址
					else if (url.startsWith(Const.H5_URL + "Browser/editAddress?")) {
						mCurrentPage = 5;
					} 
					// 商品详情
					else if (url.startsWith(Const.H5_URL + "Browser/shopProductInfo?") || url.startsWith(Const.H5_URL + "Browser/shopProductList?")) {
						Log.d(TAG, "shopProductInfo  1111");
						mCurrentPage = 6; 
					}
					// 待结单
					else if (url.startsWith(Const.H5_URL + "Browser/unReceiveOrder?")) {
						mReceiveOrderPage = 4;
						mCurrentPage = -1;
					} else {
						mCurrentPage = -1;
					}
					String arr[] = URLDecoder.decode(url).split("://|\\?");
					String action = "", key = "", value = "", https = "";
					int size = arr.length;
					for (int i = 0; i < size; i++) {
						if (i == 0) {
							https = arr[i];
						}
						if (i == 1) {
							action = arr[i];
						}
						if (i == 2) {
							key = arr[i];
							String valueAr[] = key.split("://|\\?|=|\\&");
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
					}
					Log.d(TAG, "hsurl   444 >>>>=  ??? " + mCurrentPage + " --- " + mReceiveOrderPage + " --- "+ mOrderCode);
					Log.d(TAG, "hsurl   444 >>>>=  *** " + mUserAddressId + " --- " + mUserAddressId);
					Log.d(TAG, "hsurl   444 >>>>=" + URLDecoder.decode(url) + ",action >>> " + action + ">>> key >>> " + key
							+ " >>> value >>> " + value.toString());

					// 拦截https为hs开头的连接
					if (HS.equals(https)) {
						// 返回
						if (BACK_UP.equals(action)) {
							finish();
						}
						// 登录
						else if (LOGIN.equals(action)) {
							login(Const.Login.SHOP_DETAIL);
						}
						// 打电话
						else if (CALL.equals(action)) {
							CommUtils.takePhone(value);
						}
						// 分享
						else if (SHARE.equals(action)) {
							if (!Util.isEmpty(value)) {
								BatchCoupon coupon = Util.json2Obj(value, BatchCoupon.class);
								ShareCouponUtil.shareCoupon(H5ShopDetailActivity.this, coupon); // 分享
							}
						}
						// 买单
						else if (ICBC_PAY.equals(action)) {
							if (DB.getBoolean(DB.Key.CUST_LOGIN)) {
								if (!Util.isEmpty(value)) {
									Shop shop = Util.json2Obj(value, Shop.class);
									if (Util.isEmpty(shop.getOrderType())) {
										SkipActivityUtil.skipPayBillActivity(H5ShopDetailActivity.this, shop, "",ShopPayBillFragment.JOIN_DISCOUNT,ShopPayBillFragment.NOT_MEAL);
									} else if (shop.getOrderType().equals(ShopPayBillFragment.EAT)) {
										SkipActivityUtil.skipPayBillActivity(H5ShopDetailActivity.this, shop, "",ShopPayBillFragment.JOIN_DISCOUNT,ShopPayBillFragment.EAT);
									} else if (shop.getOrderType().equals(ShopPayBillFragment.TAKE_OUT)) {
										SkipActivityUtil.skipPayBillActivity(H5ShopDetailActivity.this, shop, "",ShopPayBillFragment.NOT_JOIN_DISCOUNT,ShopPayBillFragment.TAKE_OUT);
									}
								}
							}
						}
						// 进入批次优惠券详情界面
						else if (BATCH_COUPON_INFO.equals(action)) {
							Log.d(TAG, "COUPON_INFO  >>> " + mBatchCouponCode + " , mCouponType : " + mCouponType);
							Intent intent = new Intent(H5ShopDetailActivity.this, BatchCouponDetailActivity.class);
							intent.putExtra(BatchCouponDetailFragment.BATCH_COUPON_CODE, mBatchCouponCode);
							intent.putExtra(BatchCouponDetailFragment.BATCH_COUPON_TYPE, mCouponType);
							startActivity(intent);
						}
						// 进入单张优惠券详情界面
						else if (COUPON_INFO.equals(action)) {
							Intent intent = new Intent(H5ShopDetailActivity.this, CouponDetailActivity.class);
							intent.putExtra(CouponDetailFragment.USER_COUPON_CODE, value);
							startActivity(intent);
						}
						// 留言
						else if (FEED_BACK.equals(action)) {
							if (DB.getBoolean(DB.Key.CUST_LOGIN)) {
								getMsg(mShopCode, value);
							}
						}
						// 查看大图
						else if (SHOP_ALBUM.equals(action)) {
							Intent intent = new Intent(H5ShopDetailActivity.this, MyPhotoAlbumActivity.class);
							intent.putExtra(MyPhotoAlbumActivity.SHOP_CODE, mShopCode);
							startActivity(intent);
						}
						// 使用优惠券
						else if (USE_COUPON.equals(action)) {
							Log.d(TAG, "hsurl value >>> " + value);
							if (!Util.isEmpty(value)) {
								if (DB.getBoolean(DB.Key.CUST_LOGIN)) {
									BatchCoupon coupon = Util.json2Obj(value, BatchCoupon.class);
									useCoupon(coupon, value);
								}
							}
						}
						// 纠错
						else if (JIU_CUO.equals(action)) {
							if (DB.getBoolean(DB.Key.CUST_LOGIN)) {
								Intent intent = new Intent(H5ShopDetailActivity.this, ImageUploadActivity.class);
								DB.saveStr(CustConst.TO_SHOP_CODE, mShopCode);
								startActivity(intent);
							} else {
								Intent intent = new Intent(H5ShopDetailActivity.this, LoginActivity.class);
								startActivity(intent);
							}
						}
						// 查看大图
						else if (BIG_IMG.equals(action)) {
							Intent intent = new Intent(H5ShopDetailActivity.this, SingleBigPhotoActivity.class);
							intent.putExtra(SingleBigPhotoActivity.IMAGEURL, value);
							startActivity(intent);
						} 
						// 购买优惠券
						else if (COUPON_PAY.equals(action)) {
							Intent intent = new Intent(H5ShopDetailActivity.this, CouponBuyActivity.class);
							intent.putExtra(CouponBuyFragment.COUPON, value);
							startActivity(intent);
						} 
						// 进入活动详情h5
						else if (GET_ACT_INFO.equals(action)) {
							Intent intent = new Intent(H5ShopDetailActivity.this, ActThemeDetailActivity.class);
							intent.putExtra(ActThemeDetailActivity.TYPE, CustConst.HactTheme.HOME_ACTIVITY);
							intent.putExtra(ActThemeDetailActivity.THEME_URL, "Browser/getActInfo?activityCode=" + value + "&appType=1");
							startActivity(intent);
							
						} 
						// 进入店铺
						else if (GET_SHOP_INFO.equals(action)) {
							SkipActivityUtil.skipNewShopDetailActivity(H5ShopDetailActivity.this, value);
						}
						return true;
					}
					// 订单 ho
					else if (HO.equals(https)) {
						if (ICBC_PAY.equals(action)) {
							if (!Util.isEmpty(value)) {
								Log.d(TAG, "mCurrentPage >>> " + mCurrentPage);
								Shop shop = Util.json2Obj(value, Shop.class);
								// shop.getEatPayType() 值为1或者为0的时候 都是买单 若为 2 就是支付界面

								if (null != shop.getUserAddressId()) {
									addTakeoutOrder(shop);
									mCurrentPage = 2;
								} else {
									getUserShopRecord(shop);
//									addNotTakeoutOrder(shop);
								}
							}
						} else if (LOGIN.equals(action)) {
							login(Const.Login.SHOP_DETAIL);
						} else if (SHOP.equals(action)) {// 跳转到商家列表
							SkipActivityUtil.skipShopList(); 
						}
						return true;
					}
					// 其他
					else {
						return false;
					}

				}

			});

			// 商店详情
			if (CustConst.HactTheme.H5SHOP_DETAIL.equals(mType)) {
				mWebview.loadUrl(Const.H5_URL + "Browser/getShopInfo?shopCode=" + mShopCode + "&userCode=" + mUserCode);
			}
			// 我的订单
			else if (CustConst.HactTheme.H5TAKEOUT_DETAIL.equals(mType)) {
				mOrderCode = this.getIntent().getStringExtra(PAY_RESULT_ORDERCODE);
				mWebview.loadUrl(Const.H5_URL + "Browser/unReceiveOrder?orderCode=" + mOrderCode);
			} 
			// 订单详情界面
			else if (CustConst.HactTheme.ORDER_DETAIL.equals(mType)) {
				mOrderCode = this.getIntent().getStringExtra(PAY_RESULT_ORDERCODE);
				mWebview.loadUrl(Const.H5_URL + "Browser/unReceiveOrder?orderCode=" + mOrderCode);
				mReceiveOrderPage = 4;
				mCurrentPage = -1;
				// 加菜
			} else if (CustConst.HactTheme.ADD_MENU.equals(mType)) {
				mOrderCode = this.getIntent().getStringExtra(PAY_RESULT_ORDERCODE);
				mWebview.loadUrl(Const.H5_URL + "Browser/cEatOrder?shopCode=" + mShopCode + "&userCode=" + mUserCode + "&orderCode=" + mOrderCode);
				mCurrentPage = 1;
			} else if("take_order".equals(mType)){ //点餐
				Log.d(TAG, "mShopCode:"+ mShopCode + ",mUserCode:" + mUserCode);
					mWebview.loadUrl(Const.H5_URL + "Browser/cEatOrder?shopCode=" + mShopCode + "&userCode=" + mUserCode);
			}else if("take_out".equals(mType)){ //外卖
					mWebview.loadUrl(Const.H5_URL + "Browser/cTakeawayOrder?shopCode=" + mShopCode + "&userCode=" + mUserCode);
			}else if("book_food".equals(mType)){ //预定
					//TODO
			}else if("order_food".equals(mType)){ //订单
					mWebview.loadUrl(Const.H5_URL + "Browser/cGetOrder??shopCode=" + mShopCode + "&userCode=" + mUserCode);
			}
			Log.d(TAG, "mReceiveOrderPage >>> " +mReceiveOrderPage + " >> page >> " +mCurrentPage );
		} else {
			Util.getToastBottom(this, "请连接网络");
			mProgView.setVisibility(View.GONE);
		}
	}

	/**
	 * 添加外卖的地址
	 */
	private void addTakeoutOrder(final Shop shop) {
		String params[] = { shop.getOrderCode(), shop.getUserAddressId(), shop.getRemark() };
		new AddTakeoutOrderTask(this, new AddTakeoutOrderTask.Callback() {

			@Override
			public void getResult(int resultCode) {
				switch (resultCode) {
				case ErrorCode.SUCC:
					SkipActivityUtil.skipPayBillActivity(H5ShopDetailActivity.this, shop, shop.getOrderNbr(),ShopPayBillFragment.NOT_JOIN_DISCOUNT,ShopPayBillFragment.TAKE_OUT);
					break;
				case API_FAIL:
					Util.getContentValidate(R.string.not_take_out);
					break;

				default:
					break;
				}

			}
		}).execute(params);
	}
	
	/**
	 * 是否扫描进入支付界面
	 * @param shop
	 */
	private void getUserShopRecord (final Shop shop) {
		if (Util.isEmpty(mShopCode)) {
			return;
		}
		new GetUserShopRecordTask(this, new GetUserShopRecordTask.Callback() {
			
			@Override
			public void getResult(int resultCode) {
				Log.d(TAG, "resultCode==="+resultCode);
				switch (resultCode) {
				case 1: // 进入扫描界面
					Intent intent = new Intent(H5ShopDetailActivity.this, ScanMealActivity.class);
					intent.putExtra(ScanActivity.SHOP_OBJ, shop);
					startActivity(intent);
					break;
				case 0: // 继续支付
					addNotTakeoutOrder(shop);
					break;

				default:
					break;
				}
				
			}
		}).execute(mShopCode);
	}

	/**
	 * 添加堂食
	 */
	private void addNotTakeoutOrder(final Shop shop) {
		String params[] = { shop.getOrderCode(), shop.getUserAddressId(), shop.getRemark() };
		new AddNotTakeoutOrderTask(this, new AddNotTakeoutOrderTask.Callback() {

			@Override
			public void getResult(int resultCode) {
				switch (resultCode) {
				case ErrorCode.SUCC:
					if (shop.getEatPayType() == 1 || shop.getEatPayType() == 0) {
						SkipActivityUtil.skipPayBillActivity(H5ShopDetailActivity.this, shop, shop.getOrderNbr(),ShopPayBillFragment.JOIN_DISCOUNT,ShopPayBillFragment.EAT);
						mCurrentPage = 3;
					} else if (shop.getEatPayType() == 2) {
						mWebview.loadUrl(Const.H5_URL + "Browser/unReceiveOrder?orderCode=" + mOrderCode);
						mReceiveOrderPage = 4;
					}
					break;
				case API_FAIL:
					Util.getContentValidate(R.string.not_take_out);
					break;

				default:
					break;
				}

			}
		}).execute(params);
	}

	/**
	 * 优惠券的使用
	 * 
	 * @param coupon 优惠券对象
	 * @param value  h5 传的值
	 */
	private void useCoupon(BatchCoupon coupon, String value) {
		// 判断优惠券在不在使用时间范围内
		if (CustConst.Coupon.REAL_COUPON.equals(coupon.getCouponType())
				|| CustConst.Coupon.EXPERIENCE.equals(coupon.getCouponType())) { // 实物券和体验券
			if (Util.isEmpty(coupon.getUserCouponCode())) {
				return;
			}
			Intent intent = new Intent(H5ShopDetailActivity.this, UseCouponActivity.class);
			intent.putExtra(UseCouponActivity.USE_CPOUPON_CODE, coupon.getUserCouponCode()); 
			intent.putExtra(UseCouponActivity.TYPE, UseCouponActivity.SHOP_DETAIL);
			startActivityForResult(intent, ShopDetailFragment.REQ_CODE);

		} else if (CustConst.Coupon.DEDUCT.equals(coupon.getCouponType())
				|| CustConst.Coupon.DISCOUNT.equals(coupon.getCouponType())
				|| CustConst.Coupon.REG_DEDUCT.equals(coupon.getCouponType())
				|| CustConst.Coupon.INVITE_DEDUCT.equals(coupon.getCouponType())) { // 抵扣券和折扣券
			Shop shop = Util.json2Obj(value, Shop.class);
			SkipActivityUtil.skipPayBillActivity(H5ShopDetailActivity.this, shop, "",ShopPayBillFragment.JOIN_DISCOUNT,ShopPayBillFragment.NOT_MEAL);
		} else if (CustConst.Coupon.N_BUY.equals(coupon.getCouponType())) {
			Intent intent = new Intent(H5ShopDetailActivity.this, PayMoneyActivity.class);
			intent.putExtra(PayMoneyActivity.USE_CPOUPON_CODE, coupon.getUserCouponCode());
			startActivity(intent);
		} else { // 兑换券和代金券
			Log.d(TAG, "getUserCouponCode : " + coupon.getBatchCouponCode() + " , type : " + coupon.getCouponType());
			Intent intent = new Intent(H5ShopDetailActivity.this, BatchCouponDetailActivity.class);
			intent.putExtra(BatchCouponDetailFragment.BATCH_COUPON_CODE, coupon.getBatchCouponCode());
			intent.putExtra(BatchCouponDetailFragment.BATCH_COUPON_TYPE, coupon.getCouponType());
			startActivity(intent);
		}
	}

	/**
	 * 留言
	 * 
	 * @param shopCode
	 *            商店编码
	 * @param shopName
	 *            商店名称
	 */
	private void getMsg(String shopCode, String shopName) {
		User user = DB.getObj(DB.Key.CUST_USER, User.class);
		if (null == user) {
			GetUserInfo.getUserInfo(this);
		}
		// 友盟统计
		MobclickAgent.onEvent(this, "shopdetail_message");
		Messages message = new Messages();
		message.setShopCode(shopCode);
		if (!Util.isEmpty(shopName)) {
			message.setShopName(shopName);
		} else {
			message.setShopName("商家");
		}
		Intent intent = new Intent(this, VipChatActivity.class);
		intent.putExtra(VipChatFragment.MSG_OBJ, (Serializable) message);
		startActivity(intent);

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
			String url = mWebview.getUrl();
			Log.d(TAG, "url&&&&&"+url);
			if(url.endsWith("bigImg")){
				mWebview.loadUrl(Const.H5_URL + "Browser/getShopInfo?shopCode=" + mShopCode + "&userCode=" + mUserCode);
				mCurrentPage = 0;
				return true;
			}
			if (mCurrentPage == 1) { // 外卖和点餐
				Log.d(TAG, "mOrderCode =  >>>  1  ");
				this.finish();
				mCurrentPage = 0;
				return true;
			} else if (mCurrentPage == 0 || (mReceiveOrderPage == 0 && mCurrentPage == -1)) { // 商店详情
				Log.d(TAG, "mOrderCode =  >>> 2 ");
				this.finish();
			} else if (mCurrentPage == 2) { // 外卖买单
				Log.d(TAG, "mOrderCode =  >>> " + mOrderCode);
				mWebview.loadUrl(Const.H5_URL + "Browser/cTakeawayOrder?shopCode=" + mShopCode + "&userCode="
						+ mUserCode + "&orderCode=" + mOrderCode);
				mCurrentPage = 1;
				return true;
			} else if (mCurrentPage == 3) { // 堂食买单
				mWebview.loadUrl(Const.H5_URL + "Browser/cEatOrder?shopCode=" + mShopCode + "&userCode=" + mUserCode
						+ "&orderCode=" + mOrderCode);
				mCurrentPage = 1;
				return true;
			} else if (mReceiveOrderPage == 4) { // 待结单
				mReceiveOrderPage = 0;
				mCurrentPage = 1;
				mWebview.loadUrl(Const.H5_URL + "Browser/cGetOrder?shopCode=" + mShopCode + "&userCode=" + mUserCode); 
				return true;
			} else if (mCurrentPage == 4) { // 地址
				mCurrentPage = 2;
				mWebview.loadUrl(Const.H5_URL + "Browser/cOrderSubmit?orderCode=" + mOrderCode + "&userAddressId="
						+ mUserAddressId);
				return true;
			} else if (mCurrentPage == 5) { // 编辑地址
				mCurrentPage = 4;
				mWebview.loadUrl(Const.H5_URL + "Browser/getAddressList?userCode=" + mUserCode + "&orderCode="
						+ mOrderCode + "&userAddressId=" + mUserAddressId);
				return true;
			} else if (mCurrentPage == 6) { // 商品详情
				if (mWebview.canGoBack()) {
					mWebview.goBack();
					return true;
				} else {
					this.finish();
				}
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
		MobclickAgent.onPageEnd(H5ShopDetailActivity.class.getSimpleName()); 
	}
	
	@Override
	public void onResume() {
		super.onResume();
		AppUtils.setActivity(this);
		AppUtils.setContext(getApplicationContext());
		MobclickAgent.onPageStart(H5ShopDetailActivity.class.getSimpleName()); //统计页面
	}
	
}
