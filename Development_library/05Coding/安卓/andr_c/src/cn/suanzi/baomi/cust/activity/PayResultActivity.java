package cn.suanzi.baomi.cust.activity;

import java.net.URLDecoder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import cn.suanzi.baomi.base.Const;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.utils.ActivityUtils;
import cn.suanzi.baomi.cust.R;
import cn.suanzi.baomi.cust.application.CustConst;
import cn.suanzi.baomi.cust.fragment.CouponDetailFragment;

public class PayResultActivity extends Activity {
	private final static String TAG = PayResultActivity.class.getSimpleName();
	public final static String PAY_RESULT = "pay_result";
	public final static String CONSUMECODE = "consumeCode";
	public final static String SHOPE_CODE = "shopcode";
	public final static String IS_BANK_PAY = "is_bank_pay";// 用来标识是银行卡支付还是
															// 外卖等产品的支付
															// 用来对应完成的按钮是跳转到H5店铺详情
															// 还是订单页面
	public final static String ORDER_CODE = "order_code"; // 外卖等产品支付成功后跳转到H5订单详情

	public final static String FAIL_RET_CODE = "fail_ret_code"; //支付失败时  错误的结果码
	/** 退款*/
	private final static String REFUND = "refund";
	
	private WebView mShowResult;

	private TextView mFinishTextView;

	private boolean mPayResult;

	private String mConsumeCode;

	private String mShopCode;

	private String mOrderCode;
	private boolean mIsBankPay; //

	private String fail_ret_code = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//****记录买单流程*********
		ActivityUtils.add(this);
		setContentView(R.layout.activity_pay_result);

		mPayResult = getIntent().getBooleanExtra(PAY_RESULT, false);
		
		if(mPayResult){
			mOrderCode = getIntent().getStringExtra(ORDER_CODE);
		}else{
			fail_ret_code = getIntent().getStringExtra(FAIL_RET_CODE);
		}

		mConsumeCode = getIntent().getStringExtra(CONSUMECODE);

		mShopCode = getIntent().getStringExtra(SHOPE_CODE);
		Log.d(TAG, "mShopCodePay Result " + mShopCode);
		mIsBankPay = getIntent().getBooleanExtra(IS_BANK_PAY, true);

				
		mFinishTextView = (TextView) findViewById(R.id.finish);

		mShowResult = (WebView) findViewById(R.id.pay_result);
		
		
		// 判断是否有网络
		if (Util.isNetworkOpen(this)) {
			mShowResult.getSettings().setJavaScriptEnabled(true);
			// ****记录买单流程*********
			ActivityUtils.add(this);
			mShowResult.setWebViewClient(new WebViewClient() {
				@Override
				public boolean shouldOverrideUrlLoading(WebView view, String url) {
					
					String arr[] = URLDecoder.decode(url).split("://|\\?|=");
					String action = "", key = "", value = "";

					int size = arr.length;
					for (int i = 0; i < size; i++) {
						if(i == 1){
							action = arr[i];
						}if(i == 2){	
							key = arr[i];
						}if(i == 3){
							value = arr[i];
						}
					} 
					
					String sharePrefix = "";
						if (REFUND.equals(action)) {
							sharePrefix = "hs://refund?params=";
						}
					String jsonParams = "";
					// 等到分享的字符串
					if (URLDecoder.decode(url).startsWith(sharePrefix)) {
						jsonParams = URLDecoder.decode(url).substring(sharePrefix.length());
					}
					
					Log.e(TAG, "action==="+action+",key==="+key+",value==="+value);
					if("getUserCouponDetail".equals(action)){
						//支付成功点击优惠券---》优惠券详情
						Intent coupDetailIntent = new Intent(PayResultActivity.this, CouponDetailActivity.class);
						coupDetailIntent.putExtra(CouponDetailFragment.USER_COUPON_CODE, value);
						startActivity(coupDetailIntent);
					} else if (REFUND.equals(action)) {
						Intent intent = new Intent(PayResultActivity.this, ActThemeDetailActivity.class);
						intent.putExtra(ActThemeDetailActivity.TYPE, CustConst.HactTheme.HOME_ACTIVITY);
						intent.putExtra(ActThemeDetailActivity.THEME_URL, jsonParams);
						startActivity(intent);
					}
					
					/*else if("setFreeValPay".equals(action)){ //支付成功 提示开启免验证码支付
						Intent intent = new Intent(PayResultActivity.this, SettingNoIndenPayActivity.class);
						startActivity(intent);
						ActivityUtils.finishAll();
					} */
					
					
					return true;
				}
			});

			if (mPayResult && !("".equals(mConsumeCode))) {
				// 加载需要显示的网页
				mShowResult.loadUrl(Const.H5_URL + "Browser/paySucc?consumeCode=" + mConsumeCode);
			}else if (!mPayResult) {
				Log.d(TAG, "支付失败错误代码"+fail_ret_code);
				Log.d(TAG, Const.H5_URL + "Browser/payFail?errCode="+fail_ret_code);
				mShowResult.loadUrl(Const.H5_URL + "Browser/payFail?errCode="+fail_ret_code);
			}
		} else {
			Util.getToastBottom(this, "请连接网络");
		}
		
		// 完成按钮的点击事件
		mFinishTextView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				ActivityUtils.finishAll();
				
				//如果是外卖 点餐 点击完成到订单页面(H5)
				if (!mIsBankPay) {
					Intent intent = new Intent(PayResultActivity.this, H5ShopDetailActivity.class);
					intent.putExtra(H5ShopDetailActivity.SHOP_CODE, mShopCode);
					intent.putExtra("type", CustConst.HactTheme.H5TAKEOUT_DETAIL);
					intent.putExtra(H5ShopDetailActivity.PAY_RESULT_ORDERCODE, mOrderCode);
					PayResultActivity.this.startActivity(intent);
				}
				
				
				/*Intent intent = new Intent(PayResultActivity.this, NewShopInfoActivity.class);
				intent.putExtra("shopCode", mShopCode);
				startActivity(intent);*/
			}
		});

	}

	/**
	 * 从支付结果页面跳转到H5店铺详情页面
	 */
	public void skipToH5() {
		Intent intent = new Intent(this, H5ShopDetailActivity.class);
		intent.putExtra(H5ShopDetailActivity.SHOP_CODE, mShopCode);
		if (mIsBankPay) { // 银行支付---->跳转到H5店铺详情
			intent.putExtra("type", CustConst.HactTheme.H5SHOP_DETAIL);
		} else { // 外卖支付---->跳转到H5外卖订单详情
			intent.putExtra("type", CustConst.HactTheme.H5TAKEOUT_DETAIL);
			intent.putExtra(H5ShopDetailActivity.PAY_RESULT_ORDERCODE, mOrderCode);
			this.startActivity(intent);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:

			break;
		default:
			break;
		}
		return true;
	}

}
