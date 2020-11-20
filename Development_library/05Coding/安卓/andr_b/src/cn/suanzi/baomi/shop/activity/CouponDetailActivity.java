package cn.suanzi.baomi.shop.activity;

import net.minidev.json.JSONObject;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.suanzi.baomi.base.ErrorCode;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.pojo.BatchCoupon;
import cn.suanzi.baomi.base.utils.AppUtils;
import cn.suanzi.baomi.base.utils.DialogUtils;
import cn.suanzi.baomi.base.utils.MyProgress;
import cn.suanzi.baomi.base.utils.TimeUtils;
import cn.suanzi.baomi.base.utils.ViewSolveUtils;
import cn.suanzi.baomi.shop.R;
import cn.suanzi.baomi.shop.ShopApplication;
import cn.suanzi.baomi.shop.ShopConst;
import cn.suanzi.baomi.shop.fragment.CouponHomeFragment;
import cn.suanzi.baomi.shop.model.ChangeCouponStatusTask;
import cn.suanzi.baomi.shop.model.GetCouponInfoTask;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 优惠券详情
 * 
 * @author yanfang.li
 */
public class CouponDetailActivity extends Activity {

	private final static String TAG = CouponDetailActivity.class.getSimpleName();
	public final static String DATE_FORMAT = "00:00";
	/** 传优惠券编码 */
	public final static String COUPON_CODE = "couponCode";
	public final static String COUPON_SOURCE = "couponSource";
	public final static String HOME_COUPON = "homeCoupon";
	public final static String LIST_COUPON = "listCoupon";
	public final static int ENABLE = 1;
	public final static int STOP = 0;
	public final static String STOP_STR = "停发";
	public final static String NO_TIME = "0";

	/** 优惠券编码 */
	private String mCouponCode;
	/** 优惠券启用停用 */
	private TextView mTvEdit;
	/** 进度条 */
	private TextView mTvDrawpercentage;
	private MyProgress pgDrawPerson; // 领取人数的进度条
	private BatchCoupon mBatch;
	private String mCouponSorce;
	/** 定义全局变量*/
	private ShopApplication mShopApplication;
	/** 得到是否入驻的标示*/
	private boolean mSettledflag;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_coupondetail);
		ViewUtils.inject(this);
		mCouponCode = getIntent().getStringExtra(COUPON_CODE);
		mCouponSorce = getIntent().getStringExtra(COUPON_SOURCE);
		Log.d(TAG, "homemCouponSorce=" + mCouponSorce);
		LinearLayout ivReurn = (LinearLayout) findViewById(R.id.layout_turn_in);
		ivReurn.setVisibility(View.VISIBLE);
		TextView tvTitle = (TextView) findViewById(R.id.tv_mid_content);
		mTvEdit = (TextView) findViewById(R.id.tv_msg);
		tvTitle.setText(getResources().getString(R.string.coupon_detail));
		// mTvEdit.setText(getResources().getString(R.string.tv_actmoney_enable));
		mTvDrawpercentage = (TextView) findViewById(R.id.coupon_drawpercentage); // 领取人数的进度条
		pgDrawPerson = (MyProgress) findViewById(R.id.progress_drawP); // 领取人数的进度条
		mShopApplication =  (ShopApplication)getApplication();
		mSettledflag = mShopApplication.getSettledflag();
		getCouponInfo();
		AppUtils.setActivity(this);
		AppUtils.setContext(getApplicationContext());
	}

	/**
	 * 获得优惠券的详细列表
	 */
	private void getCouponInfo() {
		new GetCouponInfoTask(this, new GetCouponInfoTask.Callback() {

			@Override
			public void getResult(JSONObject result) {
				if (result == null) { return; }
				couponBatch(result);
			}
		}).execute(mCouponCode);
	}

	private void couponBatch(JSONObject result) {

		TextView tvCouponBatch = (TextView) findViewById(R.id.tv_couponbatch); // 优惠券批次
		TextView tvTotalvolume = (TextView) findViewById(R.id.tv_totalvolume); // 总张数
		TextView tvAvailablePrice = (TextView) findViewById(R.id.tv_availablePrice); // 满多少可使用
		TextView tvStartUseDate = (TextView) findViewById(R.id.tv_startUseDate); // 开始使用时间
		TextView tvUseDayDate = (TextView) findViewById(R.id.tv_endUseDate); // 每天使用时间
		TextView tvCouponType = (TextView) findViewById(R.id.tv_couponType); // 什么类型的优惠券
																				// 面值多少
		TextView tvEndDrawDate = (TextView) findViewById(R.id.tv_endDrawDate); // 截止领取时间
		MyProgress pgUsePerson = (MyProgress) findViewById(R.id.progress_use); // 使用人数进度
		TextView tvUsepercentage = (TextView) findViewById(R.id.coupon_usepercentage); // 使用人数的进度条
		LinearLayout lyProgess = (LinearLayout) findViewById(R.id.ly_progess); // 领取人数的进度条
		TextView tvCouponRemark = (TextView) findViewById(R.id.tv_couponremark); // 使用说明
		TextView tvDrawPerson = (TextView) findViewById(R.id.tv_draw_person); // 领取人数
		mBatch = Util.json2Obj(result.toString(), BatchCoupon.class);
		
		if (mBatch.getIsExpire() == 0) { // 过期
			mTvEdit.setVisibility(View.GONE);
		} else { // 没有过期
			mTvEdit.setVisibility(View.VISIBLE);
		}
		
		// 可以干嘛
		String canMoney = "";
		// 优惠券类型名称
		String couponTypeName = "";
		if (ShopConst.Coupon.DEDUCT.equals(mBatch.getCouponType())) { // 抵扣券
			canMoney = "满" + mBatch.getAvailablePrice() + "元立减" + mBatch.getInsteadPrice() + "元";
			couponTypeName = getString(R.string.coupon_deduct);
		} else if (ShopConst.Coupon.DISCOUNT.equals(mBatch.getCouponType())) { // 折扣券
			canMoney = "满" + mBatch.getAvailablePrice() + "元打" + mBatch.getDiscountPercent() + "折";
			couponTypeName = getString(R.string.coupon_discount);

		} else if (ShopConst.Coupon.N_BUY.equals(mBatch.getCouponType())) { // N元购
			canMoney = mBatch.getFunction();
			couponTypeName = getString(R.string.coupon_nbuy);
			// 实物券和体验券
		} else if (ShopConst.Coupon.REAL_COUPON.equals(mBatch.getCouponType())
				|| ShopConst.Coupon.EXPERIENCE.equals(mBatch.getCouponType())) {
			if (ShopConst.Coupon.REAL_COUPON.equals(mBatch.getCouponType())) {
				couponTypeName = getString(R.string.coupon_real);
			} else {
				couponTypeName = getString(R.string.coupon_exp);
			}
			canMoney = mBatch.getFunction();
		} else if (ShopConst.Coupon.EXCHANGE_VOUCHER.equals(mBatch.getCouponType())) { // 兑换券
			couponTypeName = Util.getString(R.string.exchange_voucher);
			canMoney = mBatch.getFunction();
		} else if (ShopConst.Coupon.VOUCHER.equals(mBatch.getCouponType())) { // 代金券
			couponTypeName = Util.getString(R.string.voucher);
			canMoney = mBatch.getFunction();
		}
		
		
		tvCouponBatch.setText("批次： " + mBatch.getBatchNbr());
		String volumeString = mBatch.getTotalVolume() == -1 ? "发行数量不限张数" : mBatch.getTotalVolume() + "";
		tvTotalvolume.setText("数量： " + volumeString);
		tvAvailablePrice.setText(couponTypeName + "：" + canMoney); // 满多少可干嘛

		if (!Util.isEmpty(mBatch.getIsAvailable() + "")) {
			// 1-停用；0-启用
			if (mBatch.getIsAvailable() == STOP) {
				mTvEdit.setText(getString(R.string.tv_actmoney_enable));
			} else {
				mTvEdit.setText(getString(R.string.tv_actmoney_user));
			}
		}

		// 使用时间
		String useTime = TimeUtils.getBTime(mBatch);
		tvStartUseDate.setVisibility(View.VISIBLE);
		if (Util.isEmpty(useTime)) {
			tvStartUseDate.setText(Util.getString(R.string.use_coupon_time) + Util.getString(R.string.no_limit_time));
		} else {
			tvStartUseDate.setText(useTime);
		}

		// 每天使用时间
		String useDate = TimeUtils.getDate(mBatch);
		tvUseDayDate.setVisibility(View.VISIBLE);
		if (Util.isEmpty(useDate)) {
			tvUseDayDate.setText(this.getResources().getString(R.string.use_day_date)
					+ this.getResources().getString(R.string.day_use));
		} else {
			tvUseDayDate.setText(this.getResources().getString(R.string.use_day_date) + useDate);
		}

		if (!Util.isEmpty(mBatch.getRemark())) {
			tvCouponRemark.setText("使用说明:" + mBatch.getRemark());
			tvCouponRemark.setVisibility(View.VISIBLE);
		} else {
			tvCouponRemark.setText("使用说明:暂无说明");
			tvCouponRemark.setVisibility(View.VISIBLE);
		}
		double sendRequired = 0;
		int send = 0;
		try {
			if (Util.isEmpty(mBatch.getIsSend())) {
				send = Integer.parseInt(mBatch.getIsSend());
			}
			if (!Util.isEmpty(mBatch.getSendRequired())) {
				sendRequired = Double.parseDouble(mBatch.getSendRequired());
			}
		} catch (Exception e) {
			Log.e(TAG, "优惠券详情转换=" + e.getMessage()); // TODO
		}

		// 1-可送；0-不可送
		if (send != Util.NUM_ONE) { // 不可送
			if (sendRequired > 0) {
				tvEndDrawDate.setVisibility(View.GONE);
				tvCouponType.setVisibility(View.VISIBLE);
				tvCouponType.setText("满就送：每满" + mBatch.getSendRequired() + "元送一张优惠券");
			} else {
				tvCouponType.setVisibility(View.GONE);
				tvEndDrawDate.setVisibility(View.VISIBLE);
				tvEndDrawDate.setText("截止领取：" + mBatch.getEndTakingTime());
			}
		} else {
			tvCouponType.setVisibility(View.GONE);
			tvEndDrawDate.setVisibility(View.VISIBLE);
			tvEndDrawDate.setText("截止领取：" + mBatch.getEndTakingTime());
		}

		try {
			int takenCount = 0;
			if (null == mBatch.getTakenCount() || Util.isEmpty(mBatch.getTakenCount())) {
				takenCount = 0;
			} else {
				takenCount = Integer.parseInt(mBatch.getTakenCount());
			}
			if (mBatch.getTotalVolume() == -1) { // 发行无限张数
				lyProgess.setVisibility(View.GONE);
				tvDrawPerson.setVisibility(View.GONE);
			} else {
				lyProgess.setVisibility(View.VISIBLE);
				tvDrawPerson.setVisibility(View.VISIBLE);
				getStopCoupon(mTvEdit.getText().toString());
			}
			
			// 使用的百分比
			int userCount = mBatch.getUsedCount();
			if (userCount <= 0) {
				tvUsepercentage.setText("未使用 (0/" + mBatch.getTakenCount() + ")"); // 批次
				pgUsePerson.setMax(takenCount);
				pgUsePerson.setProgress(0);
			} else {
				takenCount = Integer.parseInt(mBatch.getTakenCount());
				pgUsePerson.setMax(takenCount);
				pgUsePerson.setProgress(userCount);
				tvUsepercentage.setText("已使用" + mBatch.getUsedPercent() + "% (" + mBatch.getUsedCount() + "/" + mBatch.getTakenCount() + ")"); // 批次
			}

		} catch (Exception e) {
			Log.e(TAG, "优惠券批次转换出错=" + e.getMessage()); // TODO
		}

	}

	/**
	 * 退出
	 */
	@OnClick(R.id.layout_turn_in)
	private void turnClick(View view) {
		finishClick();
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
		Intent intent = null;
		if (mCouponSorce.equals(LIST_COUPON)) {
			intent = new Intent(this, CouponListActitivity.class);
		} else if (mCouponSorce.equals(HOME_COUPON)) {
			intent = new Intent(this, HomeActivity.class);
		}
		intent.putExtra(CouponListActitivity.COUPON_STATUS, mTvEdit.getText().toString());
		String couponCode = "";
		if (null != mBatch) {
			couponCode = mBatch.getBatchCouponCode();
		} else {
			couponCode = "";
		}
		intent.putExtra(CouponListActitivity.COUPON_CODE, couponCode);
		if (mCouponSorce.equals(LIST_COUPON)) {
			setResult(CouponListActitivity.COUPONS_SUCCESS, intent);
		} else if (mCouponSorce.equals(HOME_COUPON)) {
			setResult(CouponHomeFragment.HOME_COUPON_SUCC, intent);
		}
		finish();
	}

	/**
	 * 启用 停用
	 * 
	 * @param view
	 */
	@OnClick(R.id.tv_msg)
	private void clickStatus(View view) {
		if (mSettledflag) {
			final String stop = getString(R.string.tv_actmoney_user);
			final String enable = getString(R.string.tv_actmoney_enable);
			String statusStr = mTvEdit.getText().toString();
			if (stop.equals(statusStr)) { // 停用
				DialogUtils.showDialog(this, this.getResources().getString(R.string.dialog_title), this.getResources()
						.getString(R.string.dialog_coupon_draw), this.getResources().getString(R.string.dialog_ok), this
						.getResources().getString(R.string.dialog_cancel),new DialogUtils().new OnResultListener() {

					@Override
					public void onOK() {
						mTvEdit.setEnabled(false);
						changeCouponStatus(STOP, stop, enable);
					}

					@Override
					public void onCancel() {
						Log.d(TAG, "取消");
					}
				});
			} else { // 启用
				mTvEdit.setEnabled(false);
				changeCouponStatus(ENABLE, enable, stop);
			}
		} else {
			mShopApplication.getDateInfo(this);
		}
	}

	/**
	 * 停用启用优惠券
	 */
	private void changeCouponStatus(int status, final String stop, final String enable) {
		new ChangeCouponStatusTask(this, new ChangeCouponStatusTask.Callback() {

			@Override
			public void getResult(JSONObject result) {
				mTvEdit.setEnabled(true);
				if (result == null) {
					mTvEdit.setText(stop);
				} else {
					int code = Integer.parseInt(result.get("code").toString());
					if (code == ErrorCode.SUCC) {
						mTvEdit.setText(enable);
						getStopCoupon(enable);
					} else {
						mTvEdit.setText(stop);
					}
				}

			}
		}).execute(mCouponCode, status + "");
	}

	private void getStopCoupon(String stop) {
		int takenCount = ViewSolveUtils.getInputNum(mBatch.getTakenCount());
		if (!stop.equals(STOP_STR)) {
			if (Util.isEmpty(mBatch.getTakenCount()) || takenCount <= 0) {
				pgDrawPerson.setMax(mBatch.getTotalVolume());
				pgDrawPerson.setProgress(0);
				mTvDrawpercentage.setText(STOP_STR + " (0/" + mBatch.getTotalVolume() + ")"); // 批次
			} else {
				pgDrawPerson.setMax(mBatch.getTotalVolume());
				pgDrawPerson.setProgress(takenCount);
				mTvDrawpercentage.setText(STOP_STR + " (" + mBatch.getTakenCount() + "/" + mBatch.getTotalVolume() + ")"); // 批次
			}
		} else {
			if (Util.isEmpty(mBatch.getTakenCount()) || takenCount <= 0) {
				mTvDrawpercentage.setText("未领取 (0/" + mBatch.getTotalVolume() + ")"); // 批次
				pgDrawPerson.setMax(mBatch.getTotalVolume());
				pgDrawPerson.setProgress(0);
			} else {
				pgDrawPerson.setMax(mBatch.getTotalVolume());
				pgDrawPerson.setProgress(takenCount);
				mTvDrawpercentage.setText("已领取" + mBatch.getTakenPercent() + "% (" + mBatch.getTakenCount() + "/" + mBatch.getTotalVolume() + ")"); // 批次
			}
		}
	}

	public void onResume() {
		super.onResume();
		AppUtils.setActivity(this);
		AppUtils.setContext(getApplicationContext());
	}

}
