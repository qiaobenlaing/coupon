// ---------------------------------------------------------
// @author    yanfang.li
// @version   1.0.0
// @createTime 2015.5.22 
// @copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
// ---------------------------------------------------------
package com.huift.hfq.shop.fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import net.minidev.json.JSONObject;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.huift.hfq.shop.R;

import com.huift.hfq.base.Util;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.pojo.BatchCoupon;
import com.huift.hfq.base.pojo.Shop;
import com.huift.hfq.base.utils.Calculate;
import com.huift.hfq.base.utils.DialogUtils;
import com.huift.hfq.base.utils.ViewSolveUtils;
import com.huift.hfq.shop.ShopConst;
import com.huift.hfq.shop.activity.CouponSetPreViewActivity;
import com.huift.hfq.shop.model.GetSendCouponByTimeTask;
import com.huift.hfq.shop.model.SgetShopBasicInfoTask;
import com.huift.hfq.shop.utils.DateUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 优惠券详细设置
 * @author yanfang.li
 */
public class CouponSetFullCutFragment extends Fragment implements TextWatcher {

	private final static String TAG = CouponSetFullCutFragment.class.getSimpleName();
	private final static int IS_CONSUME = 1;
	private final static int NO_CONSUME = 0;
	/** 商户有签约 */
	private final static int HAVE_BANK_CARD = 1;
	/** 上个页面传值的对象 **/
	public final static String BATCH = "batch";
	/** 布局的视图 **/
	private View view;
	private TextView mTvCouponName;
	/** 优惠券截止使用时间 的对话框 */
	private DatePickerDialog mPickerStartUse;
	/** 优惠券截止使用时间 的对话框 */
	private DatePickerDialog mPickerEndUse;
	/** 优惠券截止领用时间的对话框 */
	private DatePickerDialog mPickerEndDraw;
	/** 开始领用时间*/
	private DatePickerDialog mPickerStartDraw;
	/** 每天开始使用时间 */
	private TimePickerDialog mPickerStartTime;
	/** 每天结束使用时间 */
	private TimePickerDialog mPickerEndTime;
	/** 优惠券开始使用日期 月 */
	private EditText mEdtStartUseDate;
	/** 优惠券截止使用日期 */
	private EditText mEdtEndtUseDate;
	/** 优惠券截止领用日期 */
//	private EditText mEdtDrawDate;
	/** N元购当时开始的时间 */
	private EditText mEdtStartUseTime;
	/** N元购结束的时间 */
	private EditText mEdtEndUseTime;
	/** 满多少元可领用 */
	private EditText mEdtCsmDraw;
	/** 使用的扩展规则 */
	private EditText mEdtExRuleDes;
	/** 每满多少元可以使用优惠券 */
	private EditText mEdtAvailable;
	/** 单人可领取 */
	private EditText mEdtSingleDraw;
	/** 单笔可使用的张数 */
	private EditText mEdtSingleUse;
	/** 满就送 送多少张 */
	private EditText mEdtSingleSend;
	/** 当前时间 */
	private String mNowDate;
	/** 得到传过来的对象 */
	private BatchCoupon mBatch;
	/** 截止领用的时间 */
	private CheckBox mCkbDrawDate;
	/** 是否满就送 */
	private CheckBox mCkbFullSend;
	/** 开始使用时间 */
	private CheckBox mCkbStartDate;
	/** 每天使用时间 */
	private CheckBox mCkbStartTime;
	/** 发行的张数 */
	private CheckBox mCkbVolumeNum;
	/** 单人领取的张数 */
	private CheckBox mCkbSingleDrawNum;
	/** 下一步 */
	private Button mBtnCouponNext;
	/** 开始使用时间 */
	private String startUseTime;
	/** 结束时间 */
	private String endUseTime;
	/** 优惠券面值 */
	private EditText mEdtInsteadPrice;
	/** 优惠券发行总量 */
	private EditText mEdtIssueCount;
	/** 得到此张优惠券需要的金额 */
	private EditText mEdtGetPayPrice;
	/** 开始领用时间*/
	private EditText mEdtStartDrawDate;
	/** 截止领用时间*/
	private EditText mEdtEndDrawDate;
	/** 开始时间和现在的时间差 */
	private int startTime;
	/** 结束时间和现在的时间差 */
	private int endTime;
	/** 领取时间和现在的时间差 */
	private int drawTime;

	public static CouponSetFullCutFragment newInstance() {

		Bundle args = new Bundle();
		CouponSetFullCutFragment fragment = new CouponSetFullCutFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// 获取上个页面传的值
		mBatch = (BatchCoupon) getActivity().getIntent().getSerializableExtra(BATCH);
		int layoutId = 0;
		if (mBatch.getCouponType().equals(ShopConst.Coupon.DEDUCT)) { // 抵扣券
			layoutId = R.layout.fragment_coupon_dedcut;
		} else if (mBatch.getCouponType().equals(ShopConst.Coupon.DISCOUNT)) { // 折扣券
			layoutId = R.layout.fragment_coupon_discount;
		} else if (mBatch.getCouponType().equals(ShopConst.Coupon.EXCHANGE_VOUCHER)) { // 兑换券
			layoutId = R.layout.fragment_coupon_real;
		} else if (mBatch.getCouponType().equals(ShopConst.Coupon.VOUCHER)) { // 代金券
			layoutId = R.layout.fragment_coupon_voucher;
		}
		if (layoutId != 0) {
			view = inflater.inflate(layoutId, container, false);
			ViewUtils.inject(this, view);
			Util.addActivity(getActivity());
			Util.addLoginActivity(getActivity());
			findView(view); // 获得视图
			initData(); // 初始化数据
		} else {
			// 暂不处理
		}
		return view;
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		startTime = 0;
		endTime = 0;
		drawTime = 0;
		// 设置没分界面的标题
		mCkbStartTime.setChecked(true);
		if (mBatch.getCouponType().equals(ShopConst.Coupon.DEDUCT)) { // 抵扣券
			mTvCouponName.setText(getResources().getString(R.string.coupon_deduct));
		} else if (mBatch.getCouponType().equals(ShopConst.Coupon.DISCOUNT)) { // 折扣券
			mTvCouponName.setText(getResources().getString(R.string.coupon_discount));
			mEdtSingleUse.setEnabled(false);
			mEdtSingleSend.setEnabled(false);
		} else if (mBatch.getCouponType().equals(ShopConst.Coupon.EXCHANGE_VOUCHER)) { // 兑换券
			mTvCouponName.setText(getResources().getString(R.string.exchange_voucher));
		} else if (mBatch.getCouponType().equals(ShopConst.Coupon.VOUCHER)) { // 代金券
			mTvCouponName.setText(getResources().getString(R.string.voucher));
			mEdtGetPayPrice.addTextChangedListener(this);
		} 
		// 得到时间
		getTimeSet();
		mCkbStartDate.setChecked(true);
		mCkbDrawDate.setChecked(true);
		mCkbDrawDate.setOnCheckedChangeListener(chbCheckListener);
		mCkbFullSend.setOnCheckedChangeListener(chbCheckListener);
		mCkbStartDate.setOnCheckedChangeListener(chbCheckListener);
		mCkbStartTime.setOnCheckedChangeListener(chbCheckListener);
		mCkbVolumeNum.setOnCheckedChangeListener(chbCheckListener);
		mCkbSingleDrawNum.setOnCheckedChangeListener(chbCheckListener);
		mEdtInsteadPrice.addTextChangedListener(this);
		mEdtIssueCount.addTextChangedListener(this);
		mEdtCsmDraw.addTextChangedListener(this);
		mEdtAvailable.addTextChangedListener(this);
		mEdtExRuleDes.addTextChangedListener(this);
		mEdtSingleSend.addTextChangedListener(this);
		mEdtSingleDraw.addTextChangedListener(this);
		mEdtSingleUse.addTextChangedListener(this);
		getSendCouponByTime(); // 满就送
	}

	/**
	 * 获得空间
	 * 
	 * @param view
	 */
	private void findView(View view) {
		// 返回按钮
		LinearLayout inReturn = (LinearLayout) view.findViewById(R.id.layout_turn_in);
		inReturn.setVisibility(View.VISIBLE);
		mTvCouponName = (TextView) view.findViewById(R.id.tv_mid_content);
		mCkbDrawDate = (CheckBox) view.findViewById(R.id.draw_time);
		mCkbFullSend = (CheckBox) view.findViewById(R.id.full_send);
		mCkbStartDate = (CheckBox) view.findViewById(R.id.use_date);
		mCkbStartTime = (CheckBox) view.findViewById(R.id.use_time);
		mCkbVolumeNum = (CheckBox) view.findViewById(R.id.ckb_volume_num);
		mCkbSingleDrawNum = (CheckBox) view.findViewById(R.id.ckb_single_draw);
		mEdtAvailable = (EditText) view.findViewById(R.id.edt_coupon_csm_money);
		mEdtCsmDraw = (EditText) view.findViewById(R.id.edt_csmdraw_coupon);
		mEdtStartUseDate = (EditText) view.findViewById(R.id.edt_startuse_date);
		mEdtEndtUseDate = (EditText) view.findViewById(R.id.edt_enduse_date);
		mEdtStartUseTime = (EditText) view.findViewById(R.id.edt_startuse_time);
		mEdtEndUseTime = (EditText) view.findViewById(R.id.edt_enduse_time);
		mEdtSingleDraw = (EditText) view.findViewById(R.id.edt_single_draw);
		mEdtSingleUse = (EditText) view.findViewById(R.id.edt_single_use);
		mEdtSingleSend = (EditText) view.findViewById(R.id.edt_single_send);
		mBtnCouponNext = (Button) view.findViewById(R.id.btn_coupon_next);
		getBtnStatus(Util.NUM_ZERO); // 下一步是不可编辑的
		mEdtInsteadPrice = (EditText) view.findViewById(R.id.edt_coupon_cost);
		mEdtIssueCount = (EditText) view.findViewById(R.id.edt_coupon_issue_count);
		mEdtExRuleDes = (EditText) view.findViewById(R.id.edt_exRuleDes);
		mEdtGetPayPrice = (EditText) view.findViewById(R.id.edt_get_pay);
		mEdtStartDrawDate = (EditText) view.findViewById(R.id.edt_startDraw_date); 
		mEdtEndDrawDate = (EditText) view.findViewById(R.id.edt_endDraw_date);
	}

	/**
	 * 设置优惠券的作用
	 * @param couponType 优惠券类型
	 */
	private void setFunction(String couponType, View view) {
		Shop shop = DB.getObj(DB.Key.SHOP_INFO, Shop.class);
		if (null != view) {
			final LinearLayout lyFunction = (LinearLayout) view.findViewById(R.id.ly_exRuleDes);
			if (null != shop) {
				Log.d(TAG, "setFunction >>> 1, " + shop.getIsAcceptBankCard());
				if (shop.getIsAcceptBankCard() == HAVE_BANK_CARD) { // 商户有和工行卡签约
					lyFunction.setVisibility(View.VISIBLE);
				} else {
					lyFunction.setVisibility(View.GONE);
				}
			} else {
				Log.d(TAG, "setFunction >>> 2 , " + shop.getIsAcceptBankCard());
				new SgetShopBasicInfoTask(getActivity(), new SgetShopBasicInfoTask.Callback() {
					@Override
					public void getResult(JSONObject object) {
						if (object == null) {
							DB.saveObj(DB.Key.SHOP_INFO, null);
						} else {
							Shop shop = Util.json2Obj(object.toString(), Shop.class);
							DB.saveObj(DB.Key.SHOP_INFO, shop);
							if (shop.getIsAcceptBankCard() == HAVE_BANK_CARD) { // 商户有和工行卡签约
								lyFunction.setVisibility(View.VISIBLE);
							} else {
								lyFunction.setVisibility(View.GONE);
							}
						}
					}
				}).execute();

			}
		}
	}

	/**
	 * checkBo选择事件
	 */
	private OnCheckedChangeListener chbCheckListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			switch (buttonView.getId()) {
			case R.id.draw_time: // 领券
				if (isChecked) {
					mCkbFullSend.setChecked(false);
					mEdtCsmDraw.setText("");
					mEdtStartDrawDate.setText(startUseTime); // 开始领用时间
					mEdtEndDrawDate.setText(endUseTime); // 截止领用时间
					mCkbFullSend.setButtonDrawable(R.drawable.radio_no);
					mCkbDrawDate.setButtonDrawable(R.drawable.radio_yes);
				} else {
					mCkbFullSend.setButtonDrawable(R.drawable.radio_yes);
					mCkbDrawDate.setButtonDrawable(R.drawable.radio_no);
					mCkbFullSend.setChecked(true);
					mEdtStartDrawDate.setText("0");
					mEdtEndDrawDate.setText("0");
				}
				break;
			case R.id.full_send: // 是否是满就减
				if (isChecked) {
					mCkbFullSend.setButtonDrawable(R.drawable.radio_yes);
					mCkbDrawDate.setButtonDrawable(R.drawable.radio_no);
					mCkbDrawDate.setChecked(false);
					mEdtStartDrawDate.setText("0");
					mEdtEndDrawDate.setText("0");
					isNotNullEdt(mEdtCsmDraw); // 选中不能为空
				} else {
					mCkbDrawDate.setChecked(true);
					mEdtCsmDraw.setText("");
					mEdtStartDrawDate.setText(startUseTime);
					mEdtEndDrawDate.setText(endUseTime);
					mCkbFullSend.setButtonDrawable(R.drawable.radio_no);
					mCkbDrawDate.setButtonDrawable(R.drawable.radio_yes);
				}
				break;
			case R.id.use_date:// 使用日期
				if (isChecked) {
					mCkbStartDate.setButtonDrawable(R.drawable.multiple_choice);
					mEdtStartUseDate.setText(startUseTime);
					mEdtEndtUseDate.setText(endUseTime);
				} else {
					mCkbStartDate.setButtonDrawable(R.drawable.multiple_nochoice);
					mEdtStartUseDate.setText("0");
					mEdtEndtUseDate.setText("0");
				}
				break;
			case R.id.use_time: // 使用时间
				if (isChecked) {
					mCkbStartTime.setButtonDrawable(R.drawable.multiple_choice);
					mEdtStartUseTime.setText("09:00");
					mEdtEndUseTime.setText("18:00");
				} else {
					mCkbStartTime.setButtonDrawable(R.drawable.multiple_nochoice);
					mEdtStartUseTime.setText("0");
					mEdtEndUseTime.setText("0");
				}
				break;
			case R.id.ckb_volume_num: // 发行总量
				if (isChecked) {
					mCkbVolumeNum.setButtonDrawable(R.drawable.multiple_choice);
					isNotNullEdt(mEdtIssueCount); // 选中不能为空
				} else {
					mCkbVolumeNum.setButtonDrawable(R.drawable.multiple_nochoice);
					mEdtIssueCount.setText(""); // 清空数据
				}
				break;
			case R.id.ckb_single_draw: // 单笔可领取的张数
				if (isChecked) {
					mCkbSingleDrawNum.setButtonDrawable(R.drawable.multiple_choice);
					isNotNullEdt(mEdtSingleDraw); // 选中不能为空
				} else {
					mCkbSingleDrawNum.setButtonDrawable(R.drawable.multiple_nochoice);
					mEdtSingleDraw.setText("");
				}
				break;

			default:
				break;
			}

		}
	};

	/**
	 * 设置时间的方法
	 */
	private void getTimeSet() {
		// 给截止使用时间和截止领用时间默认当前时间 选择是自己修改时间
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		mNowDate = sdf.format(new Date());

		Time time = new Time("GMT+8");
		time.setToNow();
		JSONObject dateObj = DateUtil.setDate(time.year, (time.month + 1), time.monthDay);
		if (dateObj == null || "".equals(dateObj.toString())) {
			Log.e(TAG, "时间转换为空");
			return; // TODO
		}
		int oneDay = Integer.parseInt(dateObj.get(DateUtil.ONE_DAY).toString());
		int oneMonth = Integer.parseInt(dateObj.get(DateUtil.ONE_MONTH).toString());
		int oneYear = Integer.parseInt(dateObj.get(DateUtil.ONE_YEAR).toString());
		int weekDay = Integer.parseInt(dateObj.get(DateUtil.WEEK_DAY).toString());
		int weekMonth = Integer.parseInt(dateObj.get(DateUtil.WEEK_MONTH).toString());
		int weekYear = Integer.parseInt(dateObj.get(DateUtil.WEEK_YEAR).toString());
		// 使用日期开始日期默认为明天，截止日期按一周7天默认
		startUseTime = getUppDate(oneYear, oneMonth, oneDay, IS_CONSUME);
		endUseTime = getUppDate(weekYear, weekMonth, weekDay, IS_CONSUME);
		// 开始和结束使用时间
		mEdtStartUseDate.setText(startUseTime);
		mEdtEndtUseDate.setText(endUseTime);
		// 开始领用和结束使用时间
		mEdtStartDrawDate.setText(startUseTime);
		mEdtEndDrawDate.setText(endUseTime);
		// 每天开始使用时间和结束使用时间
		mEdtEndUseTime.setText("18:00");
		mEdtStartUseTime.setText("09:00");

		// 获取一个日历
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());

		// 结束使用时间
		mPickerEndUse = new DatePickerDialog(getActivity(), new OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				mEdtEndtUseDate.setText(getUppDate(year, monthOfYear, dayOfMonth, NO_CONSUME));
				endTime++;
				if (endTime % 2 == 0) {
					getDate(Util.timeSizes(getActivity(), mEdtEndtUseDate.getText().toString(), mNowDate));
				}
				mPickerEndUse.dismiss();
			}
		}, c.get(c.YEAR), c.get(c.MONTH), c.get(c.DAY_OF_MONTH));

		// 开始使用时间
		mPickerStartUse = new DatePickerDialog(getActivity(), new OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				mEdtStartUseDate.setText(getUppDate(year, monthOfYear, dayOfMonth, NO_CONSUME));
				startTime++;
				if (startTime % 2 == 0) {
					getDate(Util.timeSizes(getActivity(), mEdtStartUseDate.getText().toString(), mNowDate));
				}
				mPickerStartUse.dismiss();
			}
		}, c.get(c.YEAR), c.get(c.MONTH), c.get(c.DAY_OF_MONTH));

		// 截止领用时间
		mPickerEndDraw = new DatePickerDialog(getActivity(), new OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				mEdtEndDrawDate.setText(getUppDate(year, monthOfYear, dayOfMonth, NO_CONSUME));
				drawTime++;
				if (drawTime % 2 == 0) {
					getDate(Util.timeSizes(getActivity(), mEdtEndDrawDate.getText().toString(), mNowDate));
				}
				mPickerEndDraw.dismiss();
			}
		}, c.get(c.YEAR), c.get(c.MONTH), c.get(c.DAY_OF_MONTH));
		
		// 开始领用时间
		mPickerStartDraw = new DatePickerDialog(getActivity(), new OnDateSetListener() {
			
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				mEdtStartDrawDate.setText(getUppDate(year, monthOfYear, dayOfMonth, NO_CONSUME));
				drawTime++;
				if (drawTime % 2 == 0) {
					getDate(Util.timeSizes(getActivity(), mEdtStartDrawDate.getText().toString(), mNowDate));
				}
				mPickerStartDraw.dismiss();
			}
		}, c.get(c.YEAR), c.get(c.MONTH), c.get(c.DAY_OF_MONTH));
		
		
		
		// 每天看开始时间
		mPickerStartTime = Util.getTimeDialog(getActivity(), mEdtStartUseTime);
		// 每天结束使用时间
		mPickerEndTime = Util.getTimeDialog(getActivity(), mEdtEndUseTime);

		// 给mEdtLimitCsmTime点击事件，使时间对话框显示出来
		mEdtStartUseDate.setOnClickListener(EdtTimeClick);
		// 给mEdtEndtUseDate点击事件，使时间对话框显示出来
		mEdtEndtUseDate.setOnClickListener(EdtTimeClick);
		mEdtEndDrawDate.setOnClickListener(EdtTimeClick);
		mEdtStartDrawDate.setOnClickListener(EdtTimeClick);
		mEdtEndUseTime.setOnClickListener(EdtTimeClick);
		mEdtStartUseTime.setOnClickListener(EdtTimeClick);

	}

	/**
	 * 修改得到时间
	 * 
	 * @param year
	 * @param month
	 * @param day
	 * @param index
	 * @return
	 */
	private String getUppDate(int year, int month, int day, int index) {
		String time = "";
		if (index == NO_CONSUME) {
			time = year + "-" + ((month + 1) > 9 ? (month + 1) : "0" + (month + 1)) + "-" + (day > 9 ? day : "0" + day);
		} else {
			time = year + "-" + (month > 9 ? month : "0" + month) + "-" + (day > 9 ? day : "0" + day);
			Log.d(TAG, "timeindex  >>>. " + time);
		}
		return time;
	}

	/**
	 * 时间输入框的点击事件，使时间对话框显示出来
	 */
	private OnClickListener EdtTimeClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.edt_startuse_date: // 开始使用时间
				mPickerStartUse.setTitle("请选择开始使用日期");
				mPickerStartUse.show();
				mCkbStartDate.setChecked(true);
				mCkbStartTime.setButtonDrawable(R.drawable.multiple_choice);
				break;
			case R.id.edt_enduse_date: // 结束使用时间
				mPickerEndUse.setTitle("请选择结束使用日期");
				mPickerEndUse.show();
				mCkbStartDate.setChecked(true);
				mCkbStartTime.setButtonDrawable(R.drawable.multiple_choice);
				break;
			case R.id.edt_startDraw_date: // 开始领取的时间
				mPickerStartDraw.setTitle("请选择开始领取日期");
				mPickerStartDraw.show();
				mCkbFullSend.setChecked(false);
				mCkbDrawDate.setChecked(true);
				mCkbFullSend.setButtonDrawable(R.drawable.radio_no);
				mCkbDrawDate.setButtonDrawable(R.drawable.radio_yes);
				mEdtCsmDraw.setText("");
				break;
			case R.id.edt_endDraw_date: // 截止领取的时间
				mPickerEndDraw.setTitle("请选择截止领取日期");
				mPickerEndDraw.show();
				mCkbFullSend.setChecked(false);
				mCkbDrawDate.setChecked(true);
				mCkbFullSend.setButtonDrawable(R.drawable.radio_no);
				mCkbDrawDate.setButtonDrawable(R.drawable.radio_yes);
				mEdtCsmDraw.setText("");
				break;
			case R.id.edt_startuse_time: // 每天开始使用时间
				mPickerStartTime.setTitle("请选择消费开始时间");
				mCkbStartTime.setChecked(true);
				mCkbStartTime.setButtonDrawable(R.drawable.multiple_choice);
				mPickerStartTime.show();
				break;
			case R.id.edt_enduse_time: // 每天截止使用时间
				mPickerEndTime.setTitle("请选择消费结束时间");
				mCkbStartTime.setChecked(true);
				mCkbStartTime.setButtonDrawable(R.drawable.multiple_choice);
				mPickerEndTime.show();
				break;

			default:
				break;
			}

		}
	};

	/**
	 * 判断是否满就送
	 */
	private void getSendCouponByTime() {

		String[] params = { mEdtStartUseDate.getText().toString(), mEdtEndtUseDate.getText().toString() };
		Log.d(TAG, "12111111 >>> " + mEdtStartUseDate.getText().toString() + ",mEdtEndtUseDate>>"
				+ mEdtEndtUseDate.getText().toString());
		new GetSendCouponByTimeTask(getActivity(), new GetSendCouponByTimeTask.Callback() {

			@Override
			public void getResult(JSONObject result) {
				if (result == null) {
					return;
				} else {
					String canSetSendCoupon = "";
					try {
						canSetSendCoupon = result.get("canSetSendCoupon").toString();
						Log.d(TAG, "12111111 >>> " + canSetSendCoupon);
						if (canSetSendCoupon.equals(NO_CONSUME + "")) { // 不能再选满就送
							mCkbFullSend.setChecked(false);
							mCkbFullSend.setEnabled(false);
							mCkbDrawDate.setChecked(true);
							mCkbDrawDate.setEnabled(false);
							mEdtCsmDraw.setEnabled(false);
							mCkbFullSend.setButtonDrawable(R.drawable.radio_ban);
						} else {
							mCkbFullSend.setChecked(false);
							mCkbFullSend.setEnabled(true);
							mCkbDrawDate.setChecked(true);
							mEdtCsmDraw.setEnabled(true);
							mCkbDrawDate.setEnabled(true);
							mCkbFullSend.setButtonDrawable(R.drawable.radio_no);
						}
					} catch (Exception e) {
						Log.e(TAG, "转换出错");
					}
				}
			}
		}).execute(params);
	}

	/**
	 * 判断选的的时间比今天小
	 * 
	 * @param datetime
	 */
	private void getDate(double datetime) {
		if (datetime < 0) {
			Util.getContentValidate(R.string.toast_time_now);
			getBtnStatus(Util.NUM_ZERO);
		} else {
			getBtnStatus(Util.NUM_ONE);
		}
	}

	/**
	 * 下一步设置时间 和返回的点击事件
	 * 
	 * @param v
	 *            视图
	 */
	@OnClick({ R.id.btn_coupon_next, R.id.layout_turn_in })
	private void btnNextClic(View v) {
		switch (v.getId()) {
		case R.id.layout_turn_in:
			getActivity().finish();
			break;
		case R.id.btn_coupon_next:
			// 优惠券说明
			final EditText edtRemark = (EditText) view.findViewById(R.id.edt_coupon_use_remark);
			final String insteadPrice = mEdtInsteadPrice.getText().toString();
			final String startUseDate = mEdtStartUseDate.getText().toString();
			final String endUseDate = mEdtEndtUseDate.getText().toString();
			final String drawStartDate = mEdtStartDrawDate.getText().toString();
			final String drawEndDate = mEdtEndDrawDate.getText().toString();
			String available = mEdtAvailable.getText().toString();
			final String startDayTime = mEdtStartUseTime.getText().toString();
			final String endDayTime = mEdtEndUseTime.getText().toString();
			// 发行总张数
			if (mBatch.getCouponType().equals(ShopConst.Coupon.DEDUCT)) { // 抵扣券
				// 判断优惠券的面值是否大于0 和 放行张数是否大于0
				if (getJudge(mEdtInsteadPrice, available)) {
					break;
				}
				// 判断优惠券领用张数和使用张数是否满足条件
				if (judeDrawCoupon(mEdtIssueCount, mEdtSingleDraw, mEdtSingleUse)) {
					break;
				}

			} else if (mBatch.getCouponType().equals(ShopConst.Coupon.DISCOUNT)) { // 折扣券
				// 判断优惠券的面值是否大于0 
				if (getJudge(mEdtInsteadPrice, "")) {
					break;
				}
				// 判断优惠券领用张数和使用张数是否满足条件
				if (judeDrawCoupon(mEdtIssueCount, mEdtSingleDraw, mEdtSingleUse)) {
					break;
				}
				String newCardDc = Calculate.getNum(mEdtInsteadPrice.getText().toString());
				newCardDc = Calculate.subZeroAndDot(newCardDc); // 去掉小数位后的零
				if (newCardDc.length() > 3) {
					Util.getContentValidate(R.string.card_gddc);
					break;
				}
				try {
					double discount = Double.parseDouble(mEdtInsteadPrice.getText().toString());
					if (discount < 0 || discount > 10) {
						Util.getContentValidate(R.string.card_gddc);
						break;
					}
				} catch (Exception e) {
					Log.e(TAG, "会员卡折扣出错=" + e.getMessage());
				}

			} else if (mBatch.getCouponType().equals(ShopConst.Coupon.VOUCHER)) { // 代金券
				double insteadMoney = ViewSolveUtils.getInputMoney(mEdtInsteadPrice); // 面值 
				double getPayPrice = ViewSolveUtils.getInputMoney(mEdtGetPayPrice); // 购买金额
				if (insteadMoney < getPayPrice) { // 代金券的面值要比购买金额大或者相等
					Util.getContentValidate(R.string.vouche_money);
					break;
				} else if (insteadMoney <= 0 ) {
					Util.getContentValidate(R.string.no_boring);
					break;
				}
				// 判断优惠券领用张数和使用张数是否满足条件
				if (judeDrawCoupon(mEdtIssueCount, mEdtSingleDraw)) {
					break;
				}
			} else if (mBatch.getCouponType().equals(ShopConst.Coupon.EXCHANGE_VOUCHER)) { // 兑换券
				// 判断优惠券领用张数和使用张数是否满足条件
				if (judeDrawCoupon(mEdtIssueCount, mEdtSingleDraw)) {
					break;
				}
			}
		    // 选中使用日期
			if (mCkbStartDate.isChecked()) {
				// 开始使用日期
				double startCsmate = Util.timeSizes(getActivity(), startUseDate, mNowDate);
				// 结束使用日期
				double endCsmDate = Util.timeSizes(getActivity(),endUseDate, mNowDate);
				double useDateSub = Util.timeSizes(getActivity(),endUseDate ,startUseDate);
				Log.d(TAG, "startUseDate:" + startUseDate+",endUseDate:" + endUseDate + ",useDateSub：" + useDateSub);
				// 选择的时间不能比今天晚
				if (endCsmDate < 0 || startCsmate < 0) {
					Util.getContentValidate(R.string.toast_time_now);
					break;
				} else if (useDateSub < 0) {
					Util.getContentValidate(R.string.toast_timeuse_small);
					break;
				}
			} 
			
			// 选中每天使用时间
			if (mCkbStartTime.isChecked()) { 
				// 限时购
				String endTime = mNowDate + " " + endDayTime + ":00";
				String startTime = mNowDate + " " + startDayTime + ":00";
				long useTime = Util.timeSize(getActivity(), endTime, startTime);
				// 限时购每天开始使用时间要比结束时间晚
				if (useTime <= 0) {
					Util.getContentValidate(R.string.toast_time_late);
					break;
				}
			} 
			
			
			// 截止领取日期和使用日期被选中
			if (mCkbDrawDate.isChecked() && mCkbStartDate.isChecked()) { 
				// 开始领用日期
				double drawStart = Util.timeSizes(getActivity(), drawStartDate, mNowDate);
				// 结束领用日期
				double drawEnd = Util.timeSizes(getActivity(), drawEndDate, mNowDate);
				// 截止领用时间要比领用截止的时间
				double startSub = Util.timeSizes(getActivity(), startUseDate, drawStartDate);
				double drawSub = Util.timeSizes(getActivity(), drawEndDate, drawStartDate);
				if (drawEnd < 0 || drawStart < 0) {
					Util.getContentValidate(R.string.toast_time_now);
					break;
				} else if (startSub < 0) {
					Util.showToastZH("开始领用时间必须比开始使用时间早");
					break;
				} else if (drawSub < 0) {
					Util.showToastZH("开始领用时间必须比截止领用时间早");
					break;
				}
			// 只选截止领取时间没有选中开始时间
			} else if (mCkbDrawDate.isChecked() && !mCkbStartDate.isChecked()) {// 开始领用日期
				double drawStart = Util.timeSizes(getActivity(), drawStartDate, mNowDate);
				// 结束领用日期
				double drawEnd = Util.timeSizes(getActivity(), drawEndDate, mNowDate);
				double drawSub = Util.timeSizes(getActivity(), drawEndDate, drawStartDate);
				if (drawEnd < 0 || drawStart < 0) {
					Util.getContentValidate(R.string.toast_time_now);
					break;
				}  else if (drawSub < 0) {
					Util.showToastZH("开始领用时间必须比截止领用时间早");
					break;
				}}
			
			if (mBatch.getCouponType().equals(ShopConst.Coupon.VOUCHER)) {
				double getPayPrice = ViewSolveUtils.getInputMoney(mEdtGetPayPrice); // 购买金额
				if (getPayPrice == 0) {
					DialogUtils.showDialog(getActivity(), Util.getString(R.string.cue), Util.getString(R.string.voucher_content), 
							 Util.getString(R.string.continue_setting), Util.getString(R.string.continue_modify), new DialogUtils().new OnResultListener() {
						@Override
						public void onOK() {
							super.onOK();
							setPreview(startDayTime, endDayTime, startUseDate, endUseDate, edtRemark, drawStartDate,drawEndDate,insteadPrice);
						}
					});
					break;
				} else {
					setPreview(startDayTime, endDayTime, startUseDate, endUseDate, edtRemark, drawStartDate,drawEndDate,insteadPrice);
				}
			} else {
				setPreview(startDayTime, endDayTime, startUseDate, endUseDate, edtRemark, drawStartDate,drawEndDate,insteadPrice);
			}
			break;

		default:
			break;
		}

	}

	
	/**
	 * 预览
	 */
	private void setPreview (String startDayTime , String endDayTime,String startUseDate,String endUseDate,EditText edtRemark ,
			String drawStartDate,String drawEndDate,String insteadPrice) {
		// **************   跳转钱设值   *****************
		Intent intent = new Intent(getActivity(), CouponSetPreViewActivity.class);
		// 传值的对象
		BatchCoupon batch = new BatchCoupon();
		
		String isSend = ""; // 是否满就送
		String sendRequired = ""; // 满就送的金额
		String limitedSendNbr = ""; // 满就送 可送多少张
		// 每人限使用多少张 若没输入默认为1
		String nbrPerPerson = TextUtils.isEmpty(mEdtSingleDraw.getText()) ? "1" : mEdtSingleDraw.getText().toString();
		// 如果选中单人可领取的数量，值就为 输入的值如果没选中就默认为0
		nbrPerPerson = mCkbSingleDrawNum.isChecked() ?  nbrPerPerson : "0";
		// 判断每满多少元可送一张优惠券
		if (mCkbFullSend.isChecked()) {
			isSend = "1"; // 选中的值为1
			double fullSend = ViewSolveUtils.getInputMoney(mEdtCsmDraw);
			// 单笔满就送的张数
			double singleUseSend = TextUtils.isEmpty(mEdtSingleSend.getText()) ? 0 : ViewSolveUtils.getInputMoney(mEdtSingleSend);
			// 限领张数
			int nbrPerDrawCount = Integer.parseInt(nbrPerPerson);
			if (fullSend <= 0) {
				Util.getContentValidate(R.string.toast_fullsend);
				return;
			} else if (singleUseSend <= 0) {
				Util.getContentValidate(R.string.single_fullsend);
				return;
			} else if (nbrPerDrawCount > 0 && singleUseSend > nbrPerDrawCount) {
				Util.getContentValidate(R.string.single_draw);
				return;
			}
			// mEdtSingleSend为空的时候 默认为1
			limitedSendNbr = TextUtils.isEmpty(mEdtSingleSend.getText()) ? "1" : mEdtSingleSend.getText().toString();
			sendRequired = fullSend + "";
		} else {
			isSend ="0"; // 没有选中默认位0
			sendRequired = "0";
			limitedSendNbr = "0"; // 此券不是满就送
		}
		// 设置每天时间为长期有效 0表示没有勾选
		if (startDayTime.equals("0") && endDayTime.equals("0")) {
			startDayTime = getString(R.string.day_start_time); // 开始使用时间
			endDayTime = getString(R.string.day_end_time);// 截止使用时间
		}
		// 设置有效期 0表示没有勾选
		if (startUseDate.equals("0") && endUseDate.equals("0")) {
			startUseDate = getString(R.string.year_day); // 有效期开始使用时间
			endUseDate = getString(R.string.year_day); // 有效期结束使用时间
		}
		// 优惠券说明
		String remark = TextUtils.isEmpty(edtRemark.getText()) ? "" : edtRemark.getText().toString();
		// 满多少可用
		String availablePrice = TextUtils.isEmpty(mEdtAvailable.getText()) ? "0" : mEdtAvailable.getText().toString();
		// 截止领取时间
		drawEndDate = drawEndDate.equals("0") ? getString(R.string.year_day) : drawEndDate;
		// 开始领取时间
		drawStartDate = drawStartDate.equals("0") ? getString(R.string.year_day) : drawStartDate;
		// 每张券 可以干嘛
		String function = TextUtils.isEmpty(mEdtExRuleDes.getText()) ? "" : mEdtExRuleDes.getText().toString();
		// 没笔可消费的张数
		String limitedNbr = TextUtils.isEmpty(mEdtSingleUse.getText()) ? "1" : mEdtSingleUse.getText().toString();
		// 发行的张数
		int issueCount = TextUtils.isEmpty(mEdtIssueCount.getText()) ? 0 : ViewSolveUtils.getInputNum(mEdtIssueCount);
		issueCount = mCkbVolumeNum.isChecked() ? issueCount : 999999;
		
		double discountPercent = 0; // 没有折扣
		String payPrice = "0";// 得到一张优惠券需要付多少钱
		if (mBatch.getCouponType().equals(ShopConst.Coupon.DEDUCT)) { // 抵扣券
			batch = new BatchCoupon(mBatch.getCouponType(), issueCount,
					startUseDate, endUseDate, startDayTime, endDayTime, drawStartDate, drawEndDate, isSend, sendRequired,
					remark, discountPercent + "", insteadPrice, availablePrice, function, limitedNbr, nbrPerPerson,
					limitedSendNbr, payPrice);

		} else if (mBatch.getCouponType().equals(ShopConst.Coupon.DISCOUNT)) { // 折扣券
			String insteadMoney = "0"; // 当这张优惠券为折扣券时输入金额为1转而使用 discountPercent
			// 折扣
			discountPercent = !Util.isEmpty(insteadPrice) ? Double.parseDouble(insteadPrice) : discountPercent;
			batch = new BatchCoupon(mBatch.getCouponType(), issueCount,startUseDate, endUseDate, startDayTime,
					endDayTime, drawStartDate, drawEndDate, isSend, sendRequired,remark, discountPercent + "", insteadMoney,
					availablePrice, function, limitedNbr, nbrPerPerson,limitedSendNbr, payPrice);
	
		} else if (mBatch.getCouponType().equals(ShopConst.Coupon.EXCHANGE_VOUCHER)) { // 兑换券
			batch = new BatchCoupon(mBatch.getCouponType(),issueCount, startUseDate,endUseDate,startDayTime, endDayTime,
					drawStartDate, drawEndDate, isSend, sendRequired, remark, discountPercent + "", insteadPrice, availablePrice,
					function, limitedNbr, nbrPerPerson, limitedSendNbr, insteadPrice);
			
		} else if (mBatch.getCouponType().equals(ShopConst.Coupon.VOUCHER)) { // 代金券
			payPrice = mEdtGetPayPrice.getText().toString();
			batch = new BatchCoupon(mBatch.getCouponType(), issueCount, startUseDate,endUseDate,startDayTime, endDayTime, 
					drawStartDate, drawEndDate, isSend, sendRequired, remark, discountPercent + "", insteadPrice, availablePrice,function,
					limitedNbr, nbrPerPerson, limitedSendNbr,payPrice);
			
		}
		intent.putExtra(BATCH, batch);
		startActivity(intent);
	}

	/**
	 * 判断面值和发行总量是否大于零
	 * 
	 * @param edtInsteadPrice
	 * @param available
	 * @return
	 */
	private boolean getJudge(EditText edtInsteadPrice, String available) {
		boolean flag = false;
		switch (1) {
		case 1:
			double value = ViewSolveUtils.getInputMoney(edtInsteadPrice);
			// 判断发行面值大于0
			if (value > 0) {
				flag = false;
			} else {
				Util.getContentValidate(R.string.coupon_value);
				flag = true;
				break;
			}
			// 输入金额满多少就见是否大于发行的金额
			if (!Util.isEmpty(available)) {
				
				double availableValue = ViewSolveUtils.getInputMoney(available);
				if (availableValue <= 0 || availableValue < value) {
					Util.getContentValidate(R.string.input_money);
					flag = true;
					break;
				} else {
					flag = false;
				}
			} else {
				flag = false;
			}
			break;

		default:
			break;
		}

		return flag;
	}

	/**
	 * 判断发行的张数和单笔领取的张数，单笔使用张数 是否满足条件
	 * @param edtIssueNbr 发行的张数
	 * @param edtSingleDraw 领用的张数
	 * @param edtSingleUse 单笔使用的张数
	 * @return true 条件不成立 false 条件成立 可以继续下去
	 */
	private boolean judeDrawCoupon(EditText edtIssueNbr, EditText edtSingleDraw, EditText edtSingleUse) {
		boolean flag = false;
		int issueTotalNum = ViewSolveUtils.getInputNum(edtIssueNbr);// 发行张数
		int singleDrawNum = ViewSolveUtils.getInputNum(edtSingleDraw); // 单个领取张数
		int singleUseNum = ViewSolveUtils.getInputNum(edtSingleUse); // 单笔使用张数
		if (mCkbSingleDrawNum.isChecked() && mCkbVolumeNum.isChecked()) { // 都被选中的情况
		    if (issueTotalNum == 0) { // 发行的张数大于0
		    	Util.getContentValidate(R.string.volume_total_num);
		    	flag = true;
		    } else if (singleDrawNum == 0) { // 输入张数为0
				Util.getContentValidate(R.string.draw_num);
				flag = true;
			} else if (singleDrawNum > issueTotalNum) { // 领取张数大于发行张数
				Util.getContentValidate(R.string.draw_coupon_num);
				flag = true;
			} else if (singleUseNum == 0) { // 使用张数为0
				Util.getContentValidate(R.string.use_num);
				flag = true;
			} else if (singleUseNum > issueTotalNum) { // 使用张数大于领取张数
				Util.getContentValidate(R.string.use_coupon_num);
				flag = true;
			} else {
				flag = false;
			}
		} else if (mCkbVolumeNum.isChecked()) { // 已有发行张数
			if (issueTotalNum == 0) { // 发行的张数大于0
		    	Util.getContentValidate(R.string.volume_total_num);
		    	flag = true;
		    } else if (singleUseNum == 0) { // 使用张数为0
				Util.getContentValidate(R.string.use_num);
				flag = true;
			} else if (singleUseNum > issueTotalNum) { // 使用张数大于领取张数
				Util.getContentValidate(R.string.issue_coupon_num);
				flag = true;
			} else {
				flag = false;
			}
		} else if (mCkbSingleDrawNum.isChecked()) { // 选中单人领取张数
			if (singleDrawNum == 0) { // 输入张数为0
				Util.getContentValidate(R.string.draw_num);
				flag = true;
			} if (singleUseNum == 0) { // 使用张数为0
				Util.getContentValidate(R.string.use_num);
				flag = true;
			} else if (singleUseNum > singleDrawNum) { // 使用张数大于领取张数
				Util.getContentValidate(R.string.use_coupon_num);
				flag = true; 
			} else {
				flag = false;
			}
		}
		
		return flag;
	}
	/**
	 * 判断发行的张数和单笔领取的张数，单笔使用张数 是否满足条件
	 * @param edtIssueNbr 发行的张数
	 * @param edtSingleDraw 领用的张数
	 * @return true 条件不成立 false 条件成立 可以继续下去
	 */
	private boolean judeDrawCoupon(EditText edtIssueNbr, EditText edtSingleDraw) {
		boolean flag = false;
		int issueTotalNum = ViewSolveUtils.getInputNum(edtIssueNbr);// 发行张数
		int singleDrawNum = ViewSolveUtils.getInputNum(edtSingleDraw); // 单个领取张数
		if (mCkbSingleDrawNum.isChecked() && mCkbVolumeNum.isChecked()) { // 都被选中的情况
			if (issueTotalNum == 0) { // 发行的张数大于0
				Util.getContentValidate(R.string.volume_total_num);
				flag = true;
			} else if (singleDrawNum == 0) { // 输入张数为0
				Util.getContentValidate(R.string.draw_num);
				flag = true;
			} else if (singleDrawNum > issueTotalNum) { // 领取张数大于发行张数
				Util.getContentValidate(R.string.draw_coupon_num);
				flag = true;
			} else {
				flag = false;
			}
		} else if (mCkbVolumeNum.isChecked()) { // 已有发行张数
			if (issueTotalNum == 0) { // 发行的张数大于0
				Util.getContentValidate(R.string.volume_total_num);
				flag = true;
			} else {
				flag = false;
			}
		} else if (mCkbSingleDrawNum.isChecked()) { // 选中单人领取张数
			if (singleDrawNum == 0) { // 输入张数为0
				Util.getContentValidate(R.string.draw_num);
				flag = true;
			} else {
				flag = false;
			}
		}
		
		return flag;
	}
	
	// 输入钱
	@Override
	public void afterTextChanged(Editable s) {
		int isNull = Util.NUM_ZERO; // 是空的
		int notNull = Util.NUM_ONE; // 不为空
		Calculate.getStrInputMoney(mEdtInsteadPrice);
		Calculate.getStrInputMoney(mEdtAvailable);
		Calculate.getStrInputMoney(mEdtCsmDraw);
		// 发行 这里是6种券都有的添加字段
		if (mCkbVolumeNum.isChecked()) {
			// 发行总量
			isNotNullEdt(mEdtIssueCount);
		} else if (mCkbSingleDrawNum.isChecked() && !TextUtils.isEmpty(mEdtSingleDraw.getText())) {
			// 单人领取
			isNotNullEdt(mEdtSingleDraw);
		} else if (mCkbFullSend.isChecked()) {
			if (TextUtils.isEmpty(mEdtCsmDraw.getText())) {
				getBtnStatus(isNull);
			} else {
				if (mBatch.getCouponType().equals(ShopConst.Coupon.DISCOUNT)) {
					// 满就送 送多少张
					isNotNullEdt(mEdtSingleSend);
				} else {
					getBtnStatus(notNull);
				}
			}
		} else {
			getBtnStatus(notNull);
		}
		// 各自判断
		if (mBatch.getCouponType().equals(ShopConst.Coupon.DEDUCT)
				|| mBatch.getCouponType().equals(ShopConst.Coupon.DISCOUNT)) { // 抵扣券和折扣券
			if (!TextUtils.isEmpty(mEdtInsteadPrice.getText()) && !TextUtils.isEmpty(mEdtAvailable.getText())
					&& !TextUtils.isEmpty(mEdtSingleUse.getText())) {
				getBtnStatus(notNull);
			} else {
				getBtnStatus(isNull);
			}
		} else if (mBatch.getCouponType().equals(ShopConst.Coupon.EXCHANGE_VOUCHER)) { // 兑换券
			if (!TextUtils.isEmpty(mEdtExRuleDes.getText()) && !TextUtils.isEmpty(mEdtInsteadPrice.getText())) {
				getBtnStatus(notNull);
			} else {
				getBtnStatus(isNull);
			}
		} else if (mBatch.getCouponType().equals(ShopConst.Coupon.VOUCHER)) { // 代金券
			Calculate.getStrInputMoney(mEdtGetPayPrice); // 我需要花钱去买优惠券
			if (!TextUtils.isEmpty(mEdtExRuleDes.getText()) && !TextUtils.isEmpty(mEdtInsteadPrice.getText()) && !TextUtils.isEmpty(mEdtGetPayPrice.getText())) {
				getBtnStatus(notNull);
			} else {
				getBtnStatus(isNull);
			}
		}

	}

	/**
	 * 判断预览按钮的状态
	 * @param status 状态 0 is null 1 not null
	 */
	private void getBtnStatus(int status) {
		if (Util.NUM_ZERO == status) {
			mBtnCouponNext.setEnabled(false);
			mBtnCouponNext.setTextColor(getResources().getColor(R.color.coupon_fontgrey));
			mBtnCouponNext.setBackgroundColor(Color.GRAY);
		} else {
			mBtnCouponNext.setEnabled(true);
			mBtnCouponNext.setTextColor(getResources().getColor(R.color.white));
			mBtnCouponNext.setBackgroundResource(R.drawable.login_btn);
		}
	}

	// 输入后
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {

	}

	// 输入中
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		if (!TextUtils.isEmpty(mEdtCsmDraw.getText())) {
			mCkbFullSend.setChecked(true);
			mCkbDrawDate.setChecked(false);
			mCkbFullSend.setButtonDrawable(R.drawable.radio_yes);
			mCkbDrawDate.setButtonDrawable(R.drawable.radio_no);
			mEdtStartDrawDate.setText("0");
			mEdtEndDrawDate.setText("0");
		}
		// 发行总量
		if (!TextUtils.isEmpty(mEdtIssueCount.getText())) {
			mCkbVolumeNum.setChecked(true);
			mCkbVolumeNum.setButtonDrawable(R.drawable.multiple_choice);
		} else {
			mCkbVolumeNum.setChecked(false);
			mCkbVolumeNum.setButtonDrawable(R.drawable.multiple_nochoice);
		}
		// 单人领取
		if (!TextUtils.isEmpty(mEdtSingleDraw.getText())) {
			mCkbSingleDrawNum.setChecked(true);
			mCkbSingleDrawNum.setButtonDrawable(R.drawable.multiple_choice);
		} else {
			mCkbSingleDrawNum.setChecked(false);
			mCkbSingleDrawNum.setButtonDrawable(R.drawable.multiple_nochoice);
		}
	}

	/**
	 * 判断输入的输入框是否为空
	 * @param editText 输入框
	 */
	private void isNotNullEdt(EditText editText) {
		// 满就送 送多少张 0代表是空的 1不为空
		int status = TextUtils.isEmpty(editText.getText()) ? Util.NUM_ZERO : Util.NUM_ONE;
		getBtnStatus(status);
	}

}
