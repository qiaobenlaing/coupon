package cn.suanzi.baomi.cust.activity;

import net.minidev.json.JSONObject;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import cn.suanzi.baomi.base.ErrorCode;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.pojo.BatchCoupon;
import cn.suanzi.baomi.base.utils.ActivityUtils;
import cn.suanzi.baomi.base.utils.AppUtils;
import cn.suanzi.baomi.cust.R;
import cn.suanzi.baomi.cust.fragment.ICBCCardFragment;
import cn.suanzi.baomi.cust.fragment.ICBCOnlinePayFragment;
import cn.suanzi.baomi.cust.model.BankCardPayTask;
import cn.suanzi.baomi.cust.model.GetUserCouponInfoTask;
import cn.suanzi.baomi.cust.model.MposTask;
import cn.suanzi.baomi.cust.model.ZeroPayTask;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * N元购的使用
 * @author ad
 *
 */
public class PayMoneyActivity extends Activity {
	private final static String TAG = "PayMoneyActivity";
	public static final String BATCH_CPOUPON = "batchCoupon";
	public static final String USE_CPOUPON_CODE = "batchCouponCode";
	public static final String SHOP_OBJ = "shopObj";
	public static final String PAY_CARD = "0";
	public static final String PAY_MONEY = "1";
	/** 消费编码 */
	private String mConsumeCode;
	/** 返回 */
	private ImageView mIvReturn;
	/** 优惠券对象 */
	private BatchCoupon mBatchCoupon;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_payment_detail);
		Util.addActivity(PayMoneyActivity.this);
		ViewUtils.inject(this);
		AppUtils.setActivity(this);
		AppUtils.setContext(getApplicationContext());
		ActivityUtils.add(this);

		init();
	}

	//
	private void init() {
		TextView tvContent = (TextView) findViewById(R.id.tv_mid_content);
		tvContent.setText(getResources().getString(R.string.icbc_pay));
		mIvReturn = (ImageView) findViewById(R.id.iv_turn_in);
		mIvReturn.setVisibility(View.VISIBLE);
		getUserCouponInfo(); // 获取优惠券信息
	}

	/**
	 * 优惠券详细信息
	 */
	private void getUserCouponInfo() {
		String userCouponCode = this.getIntent().getStringExtra(USE_CPOUPON_CODE);
		Log.d(TAG, " userCouponCode >>>> " + userCouponCode);
		if (userCouponCode != null) {
			new GetUserCouponInfoTask(this, new GetUserCouponInfoTask.Callback() {
				@Override
				public void getResult(JSONObject result) {
					if (result == null) {
						return;
					} else {
						Gson gson = new Gson();
						mBatchCoupon = gson.fromJson(result.toString(), new TypeToken<BatchCoupon>() {
						}.getType());
						if (mBatchCoupon != null) {
							setBatchCoupon();
						}
					}
				}
			}).execute(userCouponCode);
		}
	}

	/**
	 * 给控件赋值
	 */
	private void setBatchCoupon() {
		// 初始化控件
		TextView tvShopName = (TextView) findViewById(R.id.tv_shop_name);
		TextView tvCouponDetail = (TextView) findViewById(R.id.tv_coupon_detail);
		TextView tvPayMoney = (TextView) findViewById(R.id.tv_pay_money);
		TextView tvPayCard = (TextView) findViewById(R.id.tv_paycard); // 刷卡支付
		TextView tvOnLinePay = (TextView) findViewById(R.id.tv_onlinepay); // 在线支付
		TextView tvCouponType = (TextView) findViewById(R.id.tv_coupontype);

		if (mBatchCoupon != null) {
			tvShopName.setText(mBatchCoupon.getShopName());
			tvCouponDetail.setText(mBatchCoupon.getRemark());
			tvPayMoney.setText(mBatchCoupon.getInsteadPrice() + "元");
		}

		tvCouponType.setText(mBatchCoupon.getFunction());
		// 刷卡支付
		tvPayCard.setOnClickListener(cardListener);
		mIvReturn.setOnClickListener(cardListener);
		tvOnLinePay.setOnClickListener(cardListener);
	}

	/**
	 * 刷卡支付
	 */
	private OnClickListener cardListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.iv_turn_in: // 返回
				finish();
				break;
			case R.id.tv_paycard: // 刷卡
				getCard();
				break;
			case R.id.tv_onlinepay: // 在线支付
				payOnlineTask();
				break;

			default:
				break;
			}

		}
	};

	/**
	 * 在线支付的异步
	 */
	public void payOnlineTask() {
		final TextView onLinePay = (TextView) findViewById(R.id.tv_onlinepay);
		if (null == mBatchCoupon) {
			return;
		}
		String platBonusString = "0"; // 红包金额
		String shopBonusString = "0"; // 商家红包
		String couponNum = "1"; // 优惠券张数
		String params[] = { mBatchCoupon.getShopCode(), mBatchCoupon.getInsteadPrice(),
				mBatchCoupon.getBatchCouponCode(), couponNum, platBonusString, shopBonusString,"0"};
		onLinePay.setEnabled(false);
		new BankCardPayTask(this, new BankCardPayTask.Callback() {

			@Override
			public void getResult(String result) {
				onLinePay.setEnabled(true);
				if (result != null) { // 在线支付成功 result----返回的订单编码##订单编号
					String[] split = result.split("##");
					String consumeCode = split[0];
					String orderNbr = split[1];
					Intent intent = new Intent(PayMoneyActivity.this, ICBCOnlinePayActivity.class);
					// intent.putExtra(ICBCOnlinePayFragment.CONSUME_CODE,
					// mOrderCode);//订单号-------
					intent.putExtra(ICBCOnlinePayFragment.SHOP_CODE, mBatchCoupon.getShopCode());
					intent.putExtra(ICBCOnlinePayFragment.SHOP_NAME, mBatchCoupon.getShopName());
					intent.putExtra(ICBCOnlinePayFragment.REAL_PAY, mBatchCoupon.getInsteadPrice());
					intent.putExtra(ICBCOnlinePayFragment.REAL_CONSUMECODE, consumeCode);
					intent.putExtra(ICBCOnlinePayFragment.ORDERNBR, orderNbr);
					startActivity(intent);
				} else {
					// TODO
				}

			}
			
		}).execute(params);
	}

	// 刷卡支付
	private void getCard() {
		new MposTask(this, new MposTask.Callback() {
			@Override
			public void getResult(JSONObject result) {
				if (result == null) { return; }
				if (String.valueOf(ErrorCode.SUCC).equals(result.get("code").toString())) {

					mConsumeCode = result.get("consumeCode").toString();
					if (mBatchCoupon != null) {
						Intent intent = new Intent(PayMoneyActivity.this, ICBCCardPayActivity.class);
						intent.putExtra(ICBCCardFragment.ORDER_CODE, mConsumeCode);
						intent.putExtra(ICBCCardFragment.ORDER_STYPE, PAY_CARD);
						intent.putExtra(ICBCCardFragment.SHOP_URL, mBatchCoupon.getLogoUrl());
						intent.putExtra(ICBCCardFragment.SHOP_CODE, mBatchCoupon.getShopCode());
						intent.putExtra(CouponQrcodeActivity.BATCH_CPOUPON, mBatchCoupon);
						startActivity(intent);
					}

				}
				/*
				 * SharedPreferences mSharedPreferences =
				 * getSharedPreferences(CustConst.ConsumeCode.CONSUME_CODE,
				 * Context.MODE_PRIVATE); Editor editor =
				 * mSharedPreferences.edit(); editor.putString("consumeCode",
				 * mConsumeCode); editor.commit();
				 */
			}
		}).execute(mBatchCoupon.getShopCode(), mBatchCoupon.getUserCouponCode(), "0", "0",
				mBatchCoupon.getInsteadPrice());
	}

	/**
	 * 实物券和体验券
	 */
	private void zeroPay() {
		String parems[] = { mBatchCoupon.getShopCode(), mBatchCoupon.getUserCouponCode() };
		new ZeroPayTask(this, new ZeroPayTask.Callback() {

			@Override
			public void getResult(int code, JSONObject result) {
				if (code == ErrorCode.FAIL) {
					return;
				} else if (code == ErrorCode.SUCC) {
					String consumeCode = result.get("consumeCode").toString();
					Intent intent = new Intent(PayMoneyActivity.this, CouponQrcodeActivity.class);
					intent.putExtra(CouponQrcodeActivity.BATCH_CPOUPON, mBatchCoupon);
					intent.putExtra(CouponQrcodeActivity.CONSUMER_CODE, consumeCode);
					startActivity(intent);
				} else {
					return;
				}
			}
		}).execute(parems);
	}

	/**
	 * 点击返回查看到活动列表
	 * 
	 * @param view
	 */
	@OnClick(R.id.iv_turn_in)
	public void btnBackClick(View view) {
		finish();
	}

	public void onResume() {
		super.onResume();
		AppUtils.setActivity(this);
		AppUtils.setContext(getApplicationContext());
	}
}
