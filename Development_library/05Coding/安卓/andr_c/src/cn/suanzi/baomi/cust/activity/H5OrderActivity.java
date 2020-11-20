package cn.suanzi.baomi.cust.activity;

import java.net.URLDecoder;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import cn.suanzi.baomi.base.Const;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.utils.ActivityUtils;
import cn.suanzi.baomi.base.utils.AppUtils;
import cn.suanzi.baomi.cust.R;

import com.lidroid.xutils.ViewUtils;

/**
 * 点餐的H5
 * @author yanfang.li
 */
public class H5OrderActivity extends Activity {

	private final static String TAG = ActThemeDetailActivity.class.getSimpleName();
	/** 店铺的code */
	public final static String SHOP_CODE = "shopCode";
	/** 订单类型 */
	public final static String ORDER_TYPE = "orderType";
	/** https的类型 点餐 */
	private final static String HO = "ho";
	/** https的类型 外卖 */
	private final static String HW = "hw";
	/** https的类型 我的订单 */
	private final static String HM = "hm";
	/** 回退 */
	private final static String BACK_UP = "backup";

	/** 加载网页版 */
	private WebView mWebview;
	/** 正在加载的进度条 */
	private ProgressBar mProgView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AppUtils.setActivity(this);
		AppUtils.setContext(getApplicationContext());
		setContentView(R.layout.activity_h5);
		ViewUtils.inject(this);
		Util.addHomeActivity(this); // 注册
		ActivityUtils.add(this);
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
		final String shopCode = this.getIntent().getStringExtra(SHOP_CODE);

		// 设置WebView属性，能够执行Javascript脚本
		mWebview.getSettings().setJavaScriptEnabled(true);
		mWebview.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
		mWebview.getSettings().setUseWideViewPort(true);
		mWebview.getSettings().setLoadWithOverviewMode(true);
		mWebview.setWebViewClient(new WebViewClient() {

			@Override
			public void onPageFinished(WebView view, String url) {
				mProgView.setVisibility(View.GONE);
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				String arr[] = URLDecoder.decode(url).split("://|\\?|=");
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
					}
					if (i == 3) {
						value = arr[i];
					}
				}
				Log.d(TAG, "hsurl   444 >>>>=" + url + ",action >>> " + action + ">>> key >>> " + key
						+ " >>> value >>> " + value.toString());
				// 点餐 ：为ha
				if (HO.equals(https)) {
					// 回退
					goBack(action);
				}
				// 外卖 ：hw
				else if (HW.equals(https)) {
					// 回退
					goBack(action);
				}
				// 我的订单 ：hm
				else if (HM.equals(https)) {
					// 回退
					goBack(action);
				}

				return true;
			}

		});

		// 加载网页
		if (Util.isNetworkOpen(this)) {
			String ordertype = this.getIntent().getStringExtra("orderType");
			// 外卖
			if (ordertype.equals(H5ShopDetailActivity.TAKE_OUT)) {
				mWebview.loadUrl(Const.H5_URL + "Browser/cTakeawayOrder?shopCode=" + shopCode);
			}
			// 点单
			else if (ordertype.equals(H5ShopDetailActivity.ORDER)) {
				mWebview.loadUrl(Const.H5_URL + "Browser/cEatOrder?shopCode=" + shopCode);
			}
			// 预定
			else if (ordertype.equals(H5ShopDetailActivity.RESERVE)) {
				mWebview.loadUrl(Const.H5_URL + "Browser/cEatOrder?shopCode=" + shopCode);
			}
			// 订单
			else if (ordertype.equals(H5ShopDetailActivity.ORDER_LIST)) {
				mWebview.loadUrl(Const.H5_URL + "Browser/cGetOrder?shopCode=" + shopCode);
			}
		} else {
			Util.getToastBottom(this, "请连接网络");
		}
	}

	/**
	 * 回退
	 * 
	 * @param action
	 *            动作
	 */
	private void goBack(String action) {
		// 回退
		if (BACK_UP.equals(action)) {
			finish();
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
