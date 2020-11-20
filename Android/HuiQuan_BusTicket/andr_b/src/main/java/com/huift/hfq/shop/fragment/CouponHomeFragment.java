// ---------------------------------------------------------
// @author    yanfang.li
// @version   1.0.0
// @copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
// ---------------------------------------------------------
package com.huift.hfq.shop.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.huift.hfq.base.SzApplication;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.pojo.BatchCoupon;
import com.huift.hfq.base.pojo.CountCoupon;
import com.huift.hfq.base.pojo.Shop;
import com.huift.hfq.base.utils.MyProgress;
import com.huift.hfq.base.utils.ViewSolveUtils;
import com.huift.hfq.shop.R;
import com.huift.hfq.shop.ShopApplication;
import com.huift.hfq.shop.ShopConst;
import com.huift.hfq.shop.activity.CouponCsmDetailActivity;
import com.huift.hfq.shop.activity.CouponDetailActivity;
import com.huift.hfq.shop.activity.CouponListActitivity;
import com.huift.hfq.shop.activity.CouponSettingActitivity;
import com.huift.hfq.shop.model.GetCouponHomePageTask;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

/**
 * 优惠券首页
 *
 * @author yanfang.li
 */
public class CouponHomeFragment extends BaseFragment {

	private final static String TAG = "CouponHomeFragment";
	private final static String IS_NUll = "0";
	public final static String COUPON_FISRT = "couponFirst";
	private final static int IS_EXPIRE = 0; // 过期
	private final static int HOME_COUPON_FLAG = 200; // 请求编码
	public final static int HOME_COUPON_SUCC = 201; // 响应编码
	public final static int ENABLE = 1;
	public final static int STOP = 0;
	public final static String STOP_STR = "停发";
	public final static String ENABLE_STR = "启用";

	/** 每批次消费走势图 */
	private LinearLayout mLyGrowthChart;
	/** 每批次消费走势图 */
	private LinearLayout mLyCsmChart;
	/** 视图 */
	private View mView;
	/** 优惠券批次消费数据 */
	private String[][] mChartDate;
	/** 优惠人次消费数据 */
	private String[][] mChartCsmPDate;
	/** 商家对象 */
	private Shop mShop;
	private boolean mCouponAdd = false;
	/** 优惠券编码 */
	private String mCouponCode;
	/** 优惠券对象 */
	private BatchCoupon mBatch;
	/** 进度条 */
	private MyProgress mPgDrawCoupon;
	/** 进度条的描述,领取的进度条的数额 */
	private TextView mTvCouponPercentage;
	/** 正在加载数据 */
	private ProgressBar mProgNoData;
	/** 优惠券*/
	private RelativeLayout mRlCouponFirst; // 优惠券背景
	/** 定义全局变量*/
	private ShopApplication mShopApplication;
	/** 得到是否入驻的标示*/
	private boolean mSettledflag;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_coupon_home, container, false);
		ViewUtils.inject(this, mView);
		// 出事化方法
		init(mView);
		Util.addHomeActivity(getActivity());
		// 保存互动的activity
		SzApplication.setCurrActivity(getActivity());
		DB.saveStr(ShopConst.Key.HOME, getClass().getSimpleName());
		return mView;
	}

	@Override
	public void viewVisible() {
		super.viewVisible();
		DB.saveStr(ShopConst.Key.HOME, getClass().getSimpleName());
		boolean jpushFlag = DB.getBoolean(ShopConst.Key.JPUSH_COUPONACCEPT);
		String first = DB.getStr(COUPON_FISRT);
		Log.d(TAG, "first=" + first);
		if (!Util.isEmpty(first)) {
			if (first.equals(Util.NUM_ONE + "")) {
				DB.saveStr(COUPON_FISRT, Util.NUM_TWO + "");
			} else {
				if (jpushFlag) {
					DB.saveBoolean(ShopConst.Key.JPUSH_COUPONACCEPT, false);
					getCouponHomePage();
				}
			}
		}
	}

	public static CouponHomeFragment newInstance() {
		Bundle args = new Bundle();
		CouponHomeFragment fragment = new CouponHomeFragment();
		fragment.setArguments(args);
		return fragment;
	}

	/**
	 * 出事化放方法
	 *
	 * @param view 视图
	 */
	private void init(View view) {
		mShopApplication =  (ShopApplication) getActivity().getApplication();
		mSettledflag = mShopApplication.getSettledflag();
		// Jpush接受到消息
		DB.saveStr(COUPON_FISRT, Util.NUM_TWO + "");
		DB.saveBoolean(ShopConst.Key.JPUSH_COUPONACCEPT, false);
		mLyGrowthChart = (LinearLayout) view.findViewById(R.id.ly_coupon_csmchart);
		mLyCsmChart = (LinearLayout) view.findViewById(R.id.ly_coupon_pchart);
		mPgDrawCoupon = (MyProgress) view.findViewById(R.id.progress_drawcoupon); // 领取的进度条
		mTvCouponPercentage = (TextView) view.findViewById(R.id.coupon_percentage); // 领取的进度条的数额
		mProgNoData = (ProgressBar) view.findViewById(R.id.prog_center_nodata); // 正在加载数据
		mRlCouponFirst = (RelativeLayout) mView.findViewById(R.id.rl_couponfirst); // 优惠券背景

		LinearLayout lyturnin = (LinearLayout) view.findViewById(R.id.layout_turn_in);
		lyturnin.setVisibility(View.GONE);

		// 设置scrollView滚动条的位置
		ScrollView svCoupon = (ScrollView) view.findViewById(R.id.sv_coupon);
		svCoupon.smoothScrollTo(0, 0);
		TextView tvSet = (TextView) view.findViewById(R.id.tv_msg);
		tvSet.setBackgroundResource(R.drawable.accntlist_add);
		mShop = DB.getObj(DB.Key.SHOP_INFO, Shop.class);
		TextView tvShopName = (TextView) view.findViewById(R.id.tv_mid_content);
		if (mShop != null) {
			if (!Util.isEmpty(mShop.getShopTitle())) {
				tvShopName.setText(mShop.getShopTitle());
			}
		}
		getCouponHomePage();
		// 设置progress的位置
		ViewSolveUtils.setProgLocation(getActivity(),mProgNoData);
	}

	/**
	 * 获取本页所有信息
	 */
	private void getCouponHomePage() {
		new GetCouponHomePageTask(getActivity(), new GetCouponHomePageTask.Callback() {
			@Override
			public void getResult(JSONObject result) {
				mProgNoData.setVisibility(View.GONE);
				if (result == null) { return; }
//				try {
				// 优惠券统计信息
				JSONObject couponStatistics = (JSONObject) result.get("couponStatistics");
				// 最近一批的优惠券信息
				JSONObject couponBatch = null;
				if (null != result.get("recentCouponInfo") && !"[]".equals(result.get("recentCouponInfo").toString())) {

					couponBatch = (JSONObject) result.get("recentCouponInfo");
				}
				// 优惠券消费走势信息
				JSONArray couponCsmTrend = (JSONArray) result.get("CouponConsumptionTrendInfo");
				// 优惠券消费人次走势信息
				JSONArray couponCsmPersonTrend = (JSONArray) result.get("CouponConsumptionPersonTrend");
				// 优惠券统计信息UI修改
				if (couponStatistics != null && !Util.isEmpty(couponStatistics.toString())) {
					couponStatistic(couponStatistics);
				}

				// 最近一批的优惠券信息UI修改
				if (couponBatch != null && !Util.isEmpty(couponBatch.toString())) {
					mRlCouponFirst.setVisibility(View.VISIBLE);
					couponBatch(couponBatch);
				} else {
					mRlCouponFirst.setVisibility(View.GONE);
				}

				// 优惠券消费走势信息UI修改
				if (couponCsmTrend.size() != 0 && !"[]".equals(couponCsmTrend.toJSONString())) {
					mChartDate = new String[couponCsmTrend.size()][2];
					for (int i = 0; i < couponCsmTrend.size(); i++) {
						JSONObject couponCsmObj = (JSONObject) couponCsmTrend.get(i);
						String batchNbr = couponCsmObj.get("batchNbr").toString();// 批次
						String amount = couponCsmObj.get("amount").toString();// 消费数额
						mChartDate[i][0] = batchNbr;
						mChartDate[i][1] = amount;
					}
				} else {
					mChartDate = new String[1][2];
					mChartDate[0][0] = "0";
					mChartDate[0][1] = "0";
				}
				initView(mChartDate, 1);

				// 优惠券人均消费走势信息UI修改
				if (couponCsmPersonTrend.size() != 0 && !"[]".equals(couponCsmPersonTrend.toJSONString())) {
					mChartCsmPDate = new String[couponCsmPersonTrend.size()][2];
					for (int i = 0; i < couponCsmPersonTrend.size(); i++) {
						JSONObject couponCsmPersonObj = (JSONObject) couponCsmPersonTrend.get(i);
						String batchNbr = couponCsmPersonObj.get("batchNbr").toString();// 批次
						String amount = couponCsmPersonObj.get("amount").toString();// 消费数额
						mChartCsmPDate[i][0] = batchNbr;
						mChartCsmPDate[i][1] = amount;
					}
				} else {
					mChartCsmPDate = new String[1][2];
					mChartCsmPDate[0][0] = "0";
					mChartCsmPDate[0][1] = "0";
				}
				initView(mChartCsmPDate, 0);

//				} catch (Exception e) {
//					Log.e(TAG, "首页优惠券转换出错=" + e.getMessage());
//				}
			}
		}).execute();
	}

	/**
	 * 最近批次的优惠券
	 */
	private void couponBatch(JSONObject result) {
		TextView tvMoney = (TextView) mView.findViewById(R.id.tv_money); // 钱
		TextView tvInsteadPrice = (TextView) mView.findViewById(R.id.tv_insteadPrice); // 面值
		TextView tvSymbol = (TextView) mView.findViewById(R.id.tv_symbol); // 折扣单位
		TextView tvCouponType = (TextView) mView.findViewById(R.id.tv_coupontype); // 实物券的类型
		TextView tvCouponBatch = (TextView) mView.findViewById(R.id.coupon_batch); // 批次
		TextView tvCouponAvailable = (TextView) mView.findViewById(R.id.coupon_available); // 满多少可减
		Button btnDraw = (Button) mView.findViewById(R.id.btn_draw); // 领取人员按钮
		LinearLayout lyCouponBgroud = (LinearLayout) mView.findViewById(R.id.ly_couponpic); // 优惠券背景
		LinearLayout lyUnavailable = (LinearLayout) mView.findViewById(R.id.ly_home_unavailable); // 优惠券失效
		mBatch = Util.json2Obj(result.toString(), BatchCoupon.class);
		mCouponCode = mBatch.getBatchCouponCode(); // 优惠券的编码
		int isExpire = mBatch.getIsExpire(); // 是否过期
		// 可以干嘛
		String canMoney = "";
		// 面值
		String insteadPrice = "";
		// 优惠券类型名称
		String couponTypeName = "";
		if (ShopConst.Coupon.DEDUCT.equals(mBatch.getCouponType())) { // 抵扣券
			lyCouponBgroud.setBackgroundResource(R.drawable.left_dedct);
			getCouponBg(isExpire, R.drawable.left_dedct, lyCouponBgroud, lyUnavailable);
			canMoney = "满" + mBatch.getAvailablePrice() + "元立减";
			couponTypeName = getString(R.string.coupon_deduct);
			tvSymbol.setVisibility(View.GONE);

			tvMoney.setVisibility(View.VISIBLE);
			insteadPrice = mBatch.getInsteadPrice();
			tvInsteadPrice.setTextSize(22);
			tvCouponType.setVisibility(View.VISIBLE);
		} else if (ShopConst.Coupon.DISCOUNT.equals(mBatch.getCouponType())) { // 折扣券
			getCouponBg(isExpire, R.drawable.left_discount, lyCouponBgroud, lyUnavailable);
			canMoney = "满" + mBatch.getAvailablePrice() + "可享受折扣";
			couponTypeName = getString(R.string.coupon_discount);
			insteadPrice = mBatch.getDiscountPercent();
			tvSymbol.setVisibility(View.VISIBLE);
			tvMoney.setVisibility(View.GONE);
			tvInsteadPrice.setTextSize(22);
			tvCouponType.setVisibility(View.VISIBLE);
		} else if (ShopConst.Coupon.N_BUY.equals(mBatch.getCouponType())) { // N元购
			getCouponBg(isExpire, R.drawable.left_nbuy, lyCouponBgroud, lyUnavailable);
			canMoney = mBatch.getFunction();
			tvSymbol.setVisibility(View.GONE);
			tvMoney.setVisibility(View.VISIBLE);
			insteadPrice = mBatch.getInsteadPrice();
			couponTypeName = getString(R.string.coupon_nbuy);
			tvInsteadPrice.setTextSize(22);
			tvCouponType.setVisibility(View.VISIBLE);
			// 实物券和体验券
		} else if (ShopConst.Coupon.REAL_COUPON.equals(mBatch.getCouponType())
				|| ShopConst.Coupon.EXPERIENCE.equals(mBatch.getCouponType())) {
			tvMoney.setVisibility(View.GONE);
			tvSymbol.setVisibility(View.GONE);
			canMoney = mBatch.getFunction();
			tvInsteadPrice.setTextSize(16);
			tvCouponType.setVisibility(View.GONE);
			if (ShopConst.Coupon.REAL_COUPON.equals(mBatch.getCouponType())) {
				getCouponBg(isExpire, R.drawable.left_real, lyCouponBgroud, lyUnavailable);
				insteadPrice = getString(R.string.coupon_real);
			} else {
				getCouponBg(isExpire, R.drawable.left_experience, lyCouponBgroud, lyUnavailable);
				insteadPrice = getString(R.string.coupon_exp);
			}
		} else if (ShopConst.Coupon.EXCHANGE_VOUCHER.equals(mBatch.getCouponType())) { // 兑换券
			getCouponBg(isExpire, R.drawable.left_ex_voucher, lyCouponBgroud, lyUnavailable);
			canMoney = mBatch.getFunction();
			tvSymbol.setVisibility(View.GONE);
			tvMoney.setVisibility(View.VISIBLE);
			insteadPrice = mBatch.getInsteadPrice();
			couponTypeName = getString(R.string.exchange_voucher);
			tvInsteadPrice.setTextSize(22);
			tvCouponType.setVisibility(View.VISIBLE);
			// 实物券和体验券
		} else if (ShopConst.Coupon.VOUCHER.equals(mBatch.getCouponType())) { // 代金券
			getCouponBg(isExpire, R.drawable.left_voucher, lyCouponBgroud, lyUnavailable);
			canMoney = mBatch.getFunction();
			tvSymbol.setVisibility(View.GONE);
			tvMoney.setVisibility(View.VISIBLE);
			insteadPrice = mBatch.getInsteadPrice();
			couponTypeName = getString(R.string.voucher);
			tvInsteadPrice.setTextSize(22);
			tvCouponType.setVisibility(View.VISIBLE);
		}

		tvCouponAvailable.setText(canMoney); // 满多少可干嘛
		tvInsteadPrice.setText(insteadPrice); // 面值多少
		tvCouponBatch.setText("批次: " + mBatch.getBatchNbr()); // 批次
		tvCouponType.setText(couponTypeName);

		if (mBatch.getTotalVolume() == -1) { // 发行张数无限张
			mPgDrawCoupon.setVisibility(View.GONE);
			mTvCouponPercentage.setText("领取张数：" + mBatch.getTakenCount() );
		} else { // 发行张数大于0
			mPgDrawCoupon.setVisibility(View.VISIBLE);
			setDrawStatus(mBatch.getIsAvailable()); // 设置进度条
		}

		OnClickListener clickListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (v.getId() == R.id.btn_draw) {
					Intent intent = new Intent(getActivity(), CouponCsmDetailActivity.class);
					intent.putExtra(CouponCsmDetailFragment.BATCH_COUPON_CODE, mBatch.getBatchCouponCode());
					startActivity(intent); // TODO
				} else if (v.getId() == R.id.rl_couponfirst) {
					if (Util.isEmpty(mBatch.getBatchCouponCode())) { return; }
					Intent intent = new Intent(getActivity(), CouponDetailActivity.class);
					intent.putExtra(CouponDetailActivity.COUPON_CODE, mBatch.getBatchCouponCode());
					intent.putExtra(CouponDetailActivity.COUPON_SOURCE, CouponDetailActivity.HOME_COUPON);
					startActivityForResult(intent, HOME_COUPON_FLAG);
				}
			}
		};
		mRlCouponFirst.setOnClickListener(clickListener);
		btnDraw.setOnClickListener(clickListener);

	}

	/**
	 * 设置进度条
	 */
	private void setDrawStatus(int isAvailable) {
		try {
			int takenCount = ViewSolveUtils.getInputNum(mBatch.getTakenCount());
			if (Util.isEmpty(mBatch.getTakenCount()) || takenCount <= 0) {
				mPgDrawCoupon.setMax(mBatch.getTotalVolume());
				mPgDrawCoupon.setProgress(0);
				if (isAvailable == STOP) { // 停发
					mTvCouponPercentage.setText(STOP_STR + " (0/" + mBatch.getTotalVolume() + ")"); // 批次
				} else {
					mTvCouponPercentage.setText("未领取 (0/" + mBatch.getTotalVolume() + ")"); // 批次
				}
			} else {
				mPgDrawCoupon.setMax(mBatch.getTotalVolume());
				mPgDrawCoupon.setProgress(takenCount);
				if (isAvailable == STOP) { // 停发
					mTvCouponPercentage.setText(STOP_STR + "(" + mBatch.getTakenCount() + "/" + mBatch.getTotalVolume()
							+ ")"); // 批次
				} else {
					mTvCouponPercentage.setText("已领取" + mBatch.getTakenPercent() + "% (" + mBatch.getTakenCount() + "/"
							+ mBatch.getTotalVolume() + ")"); // 批次
				}
			}

		} catch (Exception e) {
			Log.e(TAG, "优惠券批次转换出错=" + e.getMessage()); // TODO
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
			case HOME_COUPON_FLAG:
				if (resultCode == HOME_COUPON_SUCC) {
					if (null != mBatch) {
						String couponStatus = data.getExtras().getString(CouponListActitivity.COUPON_STATUS); // 如果是启动就是停发
						String couponCode = data.getExtras().getString(CouponListActitivity.COUPON_CODE);
						Log.d(TAG, "homemCouponSorce=" + couponStatus + "couponStatus=" + couponCode);
						int status = 0;
						if (couponStatus.equals(STOP_STR)) { // 如果是停发就是启发状态
							status = ENABLE;
						} else if (couponStatus.equals(ENABLE_STR)) {
							status = STOP;
						} else {
							status = mBatch.getIsAvailable();
						}
						if (mBatch.getTotalVolume() == -1) { // 发行张数无限张
							mPgDrawCoupon.setVisibility(View.GONE);
							mTvCouponPercentage.setText("领取张数：" + mBatch.getTakenCount() );
						} else { // 发行张数大于0
							mPgDrawCoupon.setVisibility(View.VISIBLE);
							if (couponCode.equals(mBatch.getBatchCouponCode())) {
								setDrawStatus(status);
							} else {
								setDrawStatus(mBatch.getIsAvailable());
							}
						}
					}
				}
				break;

			default:
				break;
		}
	}

	/**
	 * 设置图片背景
	 *
	 * @param isExpire
	 *            是否过期
	 * @param imageId
	 *            图片的Id
	 * @param linearLayout
	 *            图片控件
	 */
	private void getCouponBg(int isExpire, int imageId, LinearLayout linearLayout, LinearLayout lyUnavailable) {
		if (isExpire == IS_EXPIRE) { // 过期
			linearLayout.setBackgroundResource(R.drawable.available_n);
			lyUnavailable.setVisibility(View.VISIBLE);
		} else {
			lyUnavailable.setVisibility(View.GONE);
			linearLayout.setBackgroundResource(imageId);
		}
	}

	/**
	 * 统计信息
	 *
	 * @param result
	 *            优惠券统计信息的
	 */
	private void couponStatistic(JSONObject result) {
		// 发放优惠券的批次
		TextView tvBatch = (TextView) mView.findViewById(R.id.tv_coupon_batch);
		// 共抵扣的金额
		TextView tvDdtMoney = (TextView) mView.findViewById(R.id.tv_coupon_ddt_money);
		// 带来的消费金额
		TextView tvCsmMoney = (TextView) mView.findViewById(R.id.tv_coupon_csm_money);
		// 带来的消费人次
		TextView tvCsmPerson = (TextView) mView.findViewById(R.id.tv_coupon_csm_person);
		// 当前未使用的优惠券
		TextView tvUncsmSum = (TextView) mView.findViewById(R.id.tv_coupon_uncsm_sum);
		CountCoupon coupon = Util.json2Obj(result.toString(), CountCoupon.class);
		if (Util.isEmpty(coupon.getNbrOfBatch())) {
			tvBatch.setText(IS_NUll + "次");
		}
		if (Util.isEmpty(coupon.getNbrOfDeductionPrice())) {
			tvDdtMoney.setText(IS_NUll + "元");
		}
		if (Util.isEmpty(coupon.getTotalPrice())) {
			tvCsmMoney.setText(IS_NUll + "元");
		}
		if (Util.isEmpty(coupon.getTotalPersonAmount())) {
			tvCsmPerson.setText(IS_NUll + "人");
		}
		if (Util.isEmpty(coupon.getRestOfCoupon())) {
			tvUncsmSum.setText(IS_NUll + "张");
		}

		tvBatch.setText(coupon.getNbrOfBatch() + "次");
		tvDdtMoney.setText(coupon.getNbrOfDeductionPrice() + "元");
		tvCsmMoney.setText(coupon.getTotalPrice() + "元");
		tvCsmPerson.setText(coupon.getTotalPersonAmount() + "次");
		tvUncsmSum.setText(coupon.getRestOfCoupon() + "张");
	}

	/**
	 * 条形统计图
	 *
	 * @param chartDate
	 *            数据参数
	 * @param flag
	 *            判断是那种的统计图
	 */
	private void initView(String[][] chartDate, int flag) {
		View vChart; // 显示统计图的视图
		if (flag == 1) {
			vChart = CardHomeFragment.newInstance().getBarChart("批次", "消费数额", chartDate, getActivity(), Util.NUM_THIRD);
			mLyGrowthChart.removeAllViews();
			mLyGrowthChart.addView(vChart, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		} else if (flag == 0) {
			vChart = CardHomeFragment.newInstance().getBarChart("批次", "人数", chartDate, getActivity(), Util.NUM_ZERO);
			mLyCsmChart.removeAllViews();
			mLyCsmChart.addView(vChart, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		}

	}

	/**
	 * 查看我发布的优惠券
	 *
	 * @param view
	 */
	@OnClick({ R.id.rl_issuecoupon, R.id.coupon_arrow })
	private void skipClick(View view) {
		if (null == mBatch) {
			Util.getContentValidate(R.string.no_coupon);
			return;
		}
		String couponCode = "";
		if (null == mBatch.getBatchCouponCode() || Util.isEmpty(mBatch.getBatchCouponCode())) {
			couponCode = "";
		} else {
			couponCode = mBatch.getBatchCouponCode();
		}
		Intent intent = new Intent(getActivity(), CouponListActitivity.class);
		intent.putExtra(CouponListActitivity.COUPON_CODE, couponCode);
		startActivityForResult(intent, HOME_COUPON_FLAG);
	}

	/**
	 * 设置优惠券
	 *
	 * @param view
	 */
	@OnClick(R.id.tv_msg)
	private void setCouponClick(View view) {
		if (mSettledflag) {
			Intent intent = new Intent(getActivity(), CouponSettingActitivity.class);
			startActivity(intent);
		} else {
			mShopApplication.getDateInfo(getActivity());
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		mCouponAdd = DB.getBoolean(ShopConst.Key.COUPON_ADD);
		if (mCouponAdd) {
			DB.saveBoolean(ShopConst.Key.COUPON_ADD, false);
			getCouponHomePage();
		}
	}
}
