package cn.suanzi.baomi.cust.activity;

import net.minidev.json.JSONObject;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import cn.suanzi.baomi.base.ErrorCode;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.model.CancelPayTask;
import cn.suanzi.baomi.base.model.SetPayResultTask;
import cn.suanzi.baomi.base.pojo.BatchCoupon;
import cn.suanzi.baomi.base.utils.AppUtils;
import cn.suanzi.baomi.base.utils.DialogUtils;
import cn.suanzi.baomi.base.utils.TimeUtils;
import cn.suanzi.baomi.cust.R;
import cn.suanzi.baomi.cust.fragment.CouponDetailFragment;
import cn.suanzi.baomi.cust.model.GetUserCouponInfoTask;
import cn.suanzi.baomi.cust.model.ZeroPayTask;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 实物券和体验券使用
 * @author yanfang.li
 * 
 */
public class UseCouponActivity extends Activity {
	private static final String TAG = UseCouponActivity.class.getSimpleName();
	public static final String BATCH_CPOUPON = "batchcoupon";
	public static final String USE_CPOUPON_CODE = "batchCouponCode";
	public static final String CONSUMER_CODE = "consumecode";
	public static final String TYPE = "skipType";
	public static final String COUPON_DETAIL = "couponDetail";
	public static final String SHOP_DETAIL = "shopDetail";
	public static final String SHOP_OBJ = "shopObj";
	private final static String USE_TIME = "00:00";

	/** 使用完了 */
	private TextView mTvDefineUse;
	private ImageView mIvDefineUse;
	/** 使用完了 */
	private Button mBtnDefineUse;
	/** 优惠券对象 */
	private BatchCoupon mBatchCoupon;
	/** “正在处理”进度条 */
	protected ProgressDialog mProcessDialog = null;
	/** 是否支付成功 */
	private boolean mPayFlag = false;
	/** 跳转的类 */
	private String mSkipType;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_usecoupon);
		ViewUtils.inject(this);
		init(); // 初始
		AppUtils.setActivity(this);
		AppUtils.setContext(getApplicationContext());
	}

	/**
	 * 出事化视图
	 */
	private void init() {
		mPayFlag = false;
		// mBatchCoupon = (BatchCoupon)
		// getIntent().getSerializableExtra(BATCH_CPOUPON);
		mSkipType = getIntent().getStringExtra(TYPE);
		// mShop = (Shop) getIntent().getSerializableExtra(SHOP_OBJ);
		mBtnDefineUse = (Button) findViewById(R.id.btn_define_use); // 使用时间
		mTvDefineUse = (TextView) findViewById(R.id.tv_define_use); // 使用完了
		mIvDefineUse = (ImageView) findViewById(R.id.iv_define_use); // 使用完了的标志
		getUserCouponInfo();
	}

	/**
	 * 优惠券详细信息
	 */
	private void getUserCouponInfo() {
		String userCouponCode = this.getIntent().getStringExtra(USE_CPOUPON_CODE);
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
		TextView tvShopName = (TextView) findViewById(R.id.tv_mid_content); // 商家名称
		ImageView ivShophead = (ImageView) findViewById(R.id.iv_shophead); // 商家头像
		TextView tvFunction = (TextView) findViewById(R.id.tv_coupon_function); // 可以干嘛
		TextView tvUseDate = (TextView) findViewById(R.id.tv_use_date); // 使用期限
		TextView tvUseTime = (TextView) findViewById(R.id.tv_use_time); // 使用时间
		if (null == mBatchCoupon) { return; }
		Util.showImage(this, mBatchCoupon.getLogoUrl(), ivShophead);
		tvShopName.setText(mBatchCoupon.getShopName());
		tvFunction.setText(mBatchCoupon.getFunction());
		tvUseDate.setText(mBatchCoupon.getFunction());
		// 有效期
		String useTime = TimeUtils.getTime(mBatchCoupon);
		if (Util.isEmpty(useTime)) {
			tvUseDate.setText(getString(R.string.no_limit_time));
		} else {
			Log.d(TAG, "startDate=" + useTime);
			tvUseDate.setText(useTime);
		}
		// 使用时间
		if (Util.isEmpty(mBatchCoupon.getDayStartUsingTime()) || USE_TIME.equals(mBatchCoupon.getDayStartUsingTime())) {
			tvUseTime.setText(getString(R.string.use_coupon_time) + getString(R.string.no_limit_time));
		} else {
			tvUseTime.setText(getString(R.string.use_coupon_time) + mBatchCoupon.getDayStartUsingTime() + " - "
					+ mBatchCoupon.getDayEndUsingTime());
		}

	};

	/**
	 * 消费
	 * @param consumeCode
	 */
	private void setPayResult(final String consumeCode) {
		String[] params = { consumeCode, "", "SUCCESS" };
		mBtnDefineUse.setEnabled(false);
		new SetPayResultTask(this, new SetPayResultTask.OnResultListener() {

			@Override
			public void onSuccess() {
				mPayFlag = true;
				mBtnDefineUse.setEnabled(false);
				mIvDefineUse.setVisibility(View.VISIBLE);
				mBtnDefineUse.setBackgroundResource(R.drawable.shape_gray_btn);
				mTvDefineUse.setText(getString(R.string.define_succ));
				mBtnDefineUse.setText(getString(R.string.define_succ));
				if (mProcessDialog != null) {
					mProcessDialog.dismiss();
				}
			}

			@Override
			public void onError() {
				mPayFlag = false;
				payFail(); // 支付失败
				delOrder(consumeCode);
			}
		}).execute(params);

	}

	/**
	 * 实物券和体验券
	 */
	private void zeroPay() {
		mProcessDialog = new ProgressDialog(this);
		mProcessDialog.setCancelable(false);
		mProcessDialog.setMessage(getString(R.string.progess_pay));
		mProcessDialog.show();
		String parems[] = { mBatchCoupon.getShopCode(), mBatchCoupon.getUserCouponCode() };
		new ZeroPayTask(UseCouponActivity.this, new ZeroPayTask.Callback() {
			@Override
			public void getResult(int code, JSONObject result) {
				if (code == ErrorCode.FAIL) {
					payFail(); // 支付失败
				} else if (code == ErrorCode.SUCC) {
					mPayFlag = true;
					mBtnDefineUse.setEnabled(false);
					mIvDefineUse.setVisibility(View.VISIBLE);
					mBtnDefineUse.setBackgroundResource(R.drawable.shape_gray_btn);
					mTvDefineUse.setText(getString(R.string.define_succ));
					mBtnDefineUse.setText(getString(R.string.define_succ));
					if (mProcessDialog != null) {
						mProcessDialog.dismiss();
					}

				} else {
					mPayFlag = false;
					payFail(); // 支付失败
				}
			}
		}).execute(parems);
	}

	/**
	 * 取消订单
	 */
	public void delOrder(String consumeCode) {
		new CancelPayTask(this, new CancelPayTask.Callback() {
			@Override
			public void getResult(JSONObject result) {
				if (result != null) {
					if (String.valueOf(ErrorCode.SUCC).equals(result.get("code").toString())) {
						Util.getContentValidate(R.string.del_order_success);						finish();
					} else {
						Util.getContentValidate(R.string.del_order_fail);
					}
				}
			}
		}).execute(consumeCode);
	}

	/**
	 * 支付失败
	 */
	private void payFail() {
		if (mProcessDialog != null) {
			mProcessDialog.dismiss();
		}
		mBtnDefineUse.setEnabled(false);
		mIvDefineUse.setVisibility(View.INVISIBLE);
		mBtnDefineUse.setBackgroundResource(R.drawable.shape_gray_btn);
		mBtnDefineUse.setText(getString(R.string.define_fail));
		mTvDefineUse.setText(getString(R.string.define_error));
	}

	/**
	 * 点击事件
	 * 
	 * @param view
	 */
	@OnClick({ R.id.iv_turn_in, R.id.btn_define_use })
	private void onClick(View view) {
		switch (view.getId()) {
		case R.id.iv_turn_in:
			finishClick();
			break;
		case R.id.btn_define_use:
			DialogUtils.showDialog(this, Util.getString(R.string.dialog_title), Util.getString(R.string.dialog_csm_conent), Util.getString(R.string.dialog_ok), Util.getString(R.string.dialog_cancel),new DialogUtils().new OnResultListener() {

				@Override
				public void onOK() {
					if (Util.isEmpty(mBatchCoupon.getShopCode())) {
						Util.getContentValidate(R.string.re_use);
					} else {
						zeroPay();
					}
				}
				@Override
				public void onCancel() {
				}
			});
			break;
		default:
			break;
		}
	}

	/**
	 * 重写点击返回按钮方法，点击一次土司提示，两次退出程序
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finishClick();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 回退
	 */
	private void finishClick() {
		if (mSkipType.equals(COUPON_DETAIL)) { // 跳转到优惠券详情
			Intent intent = new Intent(this, CouponDetailActivity.class);
			intent.putExtra(CouponDetailFragment.PAY_STATUS, mPayFlag);
			setResult(CouponDetailFragment.RESULT_SUCC, intent);
		} else { // 跳转商家详情界面
			Intent intent = new Intent(this, H5ShopDetailActivity.class);
			intent.putExtra(H5ShopDetailActivity.PAY_STATUS, mPayFlag);
			intent.putExtra(H5ShopDetailActivity.USER_COUPON_CODE, mBatchCoupon.getUserCouponCode());
			setResult(H5ShopDetailActivity.LOGIN_SUCC, intent);
		}
		finish();
	}

	public void onResume() {
		super.onResume();
		AppUtils.setActivity(this);
		AppUtils.setContext(getApplicationContext());
	}
}
