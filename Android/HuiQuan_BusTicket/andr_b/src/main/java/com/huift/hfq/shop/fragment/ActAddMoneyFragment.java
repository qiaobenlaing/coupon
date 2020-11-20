package com.huift.hfq.shop.fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minidev.json.JSONObject;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import com.huift.hfq.shop.R;

import com.huift.hfq.base.Util;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.pojo.UserToken;
import com.huift.hfq.base.utils.ActivityUtils;
import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.shop.activity.ActListActivity;
import com.huift.hfq.shop.model.ActAddMoneyTask;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 添加红包
 * @author wensi.yu
 *
 */
public class ActAddMoneyFragment extends Fragment {

	private static final String FUNCTION_TITLE = "红包设置";
	private static final String FUNCTION_COMMIT ="提交";
	private static final int MONEY_START = 1;
	private static final int MONEY_TOTAL = 100;
	private final static String ACT_ZERO = "0";
	private final static int TIME_NUMBER = 9;
	private final static int TIME_ZERO = 0;
	/** 返回**/
	@ViewInject(R.id.layout_turn_in)
	private LinearLayout mIvBackup;
	/** 功能描述**/
	@ViewInject(R.id.tv_mid_content)
	private TextView mTvFinction;
	/** 提交**/
	@ViewInject(R.id.tv_msg)
	private TextView mBtnCommit;
	/** 金额区间（上限）**/
	@ViewInject(R.id.et_actadd_moneynumberend)
	private EditText mUpperPrice;
	/** 金额区间（下限））**/
	@ViewInject(R.id.et_actadd_moneynumberstart)
	private EditText mLowerPrice;
	/** 发行红包总额**/
	@ViewInject(R.id.et_actadd_moneyamount)
	private EditText mTotalValue;
	/** 每天限制发红包数量**/
	@ViewInject(R.id.et_actadd_count)
	private EditText mNbrPerDay;
	/** 发行总数量**/
	@ViewInject(R.id.et_actadd_moneynumber)
	private EditText mTotalVolume;
	/** 红包使用有效期**/
	@ViewInject(R.id.et_actadd_term)
	private EditText mValidityPeriod;
	/** 活动开始时间**/
	@ViewInject(R.id.tv_actaddmoney_start)
	private EditText mEtActaddMoneyStart;
	/** 活动结束时间**/
	@ViewInject(R.id.tv_actaddmoney_end)
	private EditText mEtActaddMoneyEnd;
	/** 当前时间*/
	private String mNowDate;
	/** 时间设置对话框**/
	private DatePickerDialog mDatePickerDialog;
	private DatePickerDialog mPickerDialog;
	/** 时间对话框时分秒**/
	private TimePickerDialog mPickerStartTime;
	private TimePickerDialog mPickerEndTime;
	/** 设置开始时间**/
	private String mStartDatePicker;
	private String mStartTimePicker;
	/** 设置结束时间**/
	private String mEndDatePicker;
	private String mEndTimePicker;
	/** 判断选择的时间不能小于今天*/
	private boolean mStartDateFlag = false;
	private boolean mEndDateFlag = false;

	public static ActAddMoneyFragment newInstance() {
		Bundle args = new Bundle();
		ActAddMoneyFragment fragment = new ActAddMoneyFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_actlist_addmoney,container, false);
		ViewUtils.inject(this, view);
		init();
		return view;
	}

	private Activity getMyActivity() {
		Activity act = getActivity();
		if (act == null) {
			act = AppUtils.getActivity();
		}
		return act;
	}

	/**
	 * 初始化
	 */
	private void init() {
		//添加
		Util.addActivity(getMyActivity());
		ActivityUtils.add(getActivity());
		//设置标题
		mIvBackup.setVisibility(View.VISIBLE);
		mTvFinction.setText(FUNCTION_TITLE);
		mBtnCommit.setText(FUNCTION_COMMIT);
		//调用时间设置
		setActAddTime();
		//默认时间
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		mNowDate = sdf.format(new Date());
		mEtActaddMoneyStart.setText(mNowDate);
		mEtActaddMoneyEnd.setText(mNowDate);
	}

	/**
	 *设置时间格式
	 */
	private void setActAddTime(){
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.setTimeInMillis(System.currentTimeMillis());
		//开始时间年月日
		mDatePickerDialog = new DatePickerDialog(getMyActivity(), new OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
								  int dayOfMonth) {
				mStartDatePicker = year + "-" + ((monthOfYear+1) > TIME_NUMBER ? (monthOfYear+1) : ACT_ZERO + (monthOfYear+1)) + "-" + (dayOfMonth > 9 ? dayOfMonth : ACT_ZERO+dayOfMonth);

				mDatePickerDialog.dismiss();
			}
		}, cal.get(cal.YEAR), cal.get(cal.MONTH), cal.get(cal.DAY_OF_MONTH));

		//开始时间时分秒
		mPickerStartTime = new TimePickerDialog(getMyActivity(), new OnTimeSetListener() {
			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				mStartTimePicker = (hourOfDay > 9 ? hourOfDay : "0" + hourOfDay) + ":" + (minute > 9 ? minute : "0" + minute) + ":" + "00";
				String startTime = mStartDatePicker +"  "+mStartTimePicker;
				mEtActaddMoneyStart.setText(startTime);

				double actStartTime = Util.timeSize(getMyActivity(), mEtActaddMoneyStart.getText().toString(), mNowDate) / 1000;
				if (actStartTime < TIME_ZERO ) {
					mStartDateFlag = true;
				} else {
					mStartDateFlag = false;
				}

			}
		}, cal.get(cal.HOUR_OF_DAY), cal.get(cal.MINUTE), true);

		//结束时间年月日
		mPickerDialog = new DatePickerDialog(getMyActivity(), new OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
								  int dayOfMonth) {
				mEndDatePicker = year +"-"+ ((monthOfYear+1) > TIME_NUMBER ? (monthOfYear+1) : ACT_ZERO + (monthOfYear+1)) + "-" + (dayOfMonth>9?dayOfMonth : ACT_ZERO + dayOfMonth);

				mPickerDialog.dismiss();
			}
		}, cal.get(cal.YEAR), cal.get(cal.MONTH), cal.get(cal.DAY_OF_MONTH));

		//结束时间时分秒
		mPickerEndTime = new TimePickerDialog(getMyActivity(), new OnTimeSetListener() {
			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				mEndTimePicker = (hourOfDay > 9 ? hourOfDay : "0" + hourOfDay) + ":" + (minute > 9 ? minute : "0" + minute) + ":" + "00";

				String endTime = mEndDatePicker +"  "+mEndTimePicker;
				mEtActaddMoneyEnd.setText(endTime);

				double actEndTime = Util.timeSize(getMyActivity(), mEtActaddMoneyEnd.getText().toString(), mNowDate) / 1000;
				if (actEndTime < TIME_ZERO) {
					mEndDateFlag = true;
				} else {
					mEndDateFlag = false;
				}

			}
		}, cal.get(cal.HOUR_OF_DAY), cal.get(cal.MINUTE), true);
	}

	/**
	 * 弹出开始时间格式
	 */
	@OnClick(R.id.tv_actaddmoney_start)
	public void EtActAddStartTimeClick(View view) {
		//mDatePickerDialog.show();
		mPickerStartTime.show();
		mDatePickerDialog.show();
	}

	/**
	 * 弹出结束时间格式
	 */
	@OnClick(R.id.tv_actaddmoney_end)
	public void EtActAddEndTimeClick(View view) {
		//mPickerDialog.show();
		mPickerEndTime.show();
		mPickerDialog.show();
	}

	/**
	 * 点击提交到红包列表
	 * @param view
	 */
	@OnClick(R.id.tv_msg)
	public void btnActAddCommitClick(View view) {
		//获得一个用户信息对象
		UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		String shopCode = userToken.getShopCode();//商家编码
		String creatorCode = userToken.getStaffCode();//发起活动人编码
		String tokenCode = userToken.getTokenCode();//需要令牌认证
		//金额区间上限
		String upperPrice = mUpperPrice.getText().toString();
		//金额区间下限
		String lowerPrice = mLowerPrice.getText().toString();
		//发行红包总额
		String totalValue = mTotalValue.getText().toString();
		//每天限制发行红包的数量
		String nbrPerDay = mNbrPerDay.getEditableText().toString();
		//发行总数量
		String totalVolume = mTotalVolume.getText().toString();
		//红包使用有效期限
		String validityPeriod = mValidityPeriod.getText().toString();
		//开始时间
		String addMoneyStart = mEtActaddMoneyStart.getText().toString();
		//结束时间
		String addMoneyEnd = mEtActaddMoneyEnd.getText().toString();

		switch (view.getId()) {
			case R.id.tv_msg:
				//验证    [0-9]*
				Pattern pattern = Pattern.compile("[0-9]*");
				Matcher matcher1 = pattern.matcher(totalVolume);
				Matcher matcher2 = pattern.matcher(upperPrice);
				Matcher matcher3 = pattern.matcher(lowerPrice);
				Matcher matcher4 = pattern.matcher(totalValue);
				Matcher matcher5 = pattern.matcher(nbrPerDay);
				Matcher matcher6 = pattern.matcher(validityPeriod);

				//红包数量
				if (Util.isEmpty(totalVolume)){
					Util.getContentValidate(R.string.toast_actmoney_totalvolume);
					mTotalVolume.findFocus();
					break;
				}
				if (Integer.parseInt(totalVolume) == TIME_ZERO){
					Util.getContentValidate(R.string.toast_actmoney_totalvolumezero);
					mTotalVolume.findFocus();
					break;
				}
				//红包数量类型不正确
				if (!matcher1.matches()){
					Util.getContentValidate(R.string.toast_actmoney_moneytype);
					mTotalVolume.findFocus();
					break;
				}
				//请输入金额区间上限
				if (Util.isEmpty(upperPrice)){
					Util.getContentValidate(R.string.toast_actmoney_upperprice);
					mUpperPrice.findFocus();
					break;
				}
				//金额区间上限类型不正确
				if (!matcher2.matches()){
					Util.getContentValidate(R.string.toast_actmoney_upperpricetype);
					mTotalVolume.findFocus();
					break;
				}
				//请输入金额区间下限
				if (Util.isEmpty(lowerPrice)){
					Util.getContentValidate(R.string.toast_actmoney_lowerprice);
					mLowerPrice.findFocus();
					break;
				}
				//红包金额区间最少1元
				if (Integer.parseInt(lowerPrice) < MONEY_START){
					Util.getContentValidate(R.string.toast_actmoney_lowerpricemoney);
					break;
				}
				//红包金额的区间下限不能大于上限
				if (Integer.parseInt(lowerPrice) > Integer.parseInt(upperPrice)){
					Util.getContentValidate(R.string.toast_actmoney_or);
					break;
				}
				//金额区间下限类型不正确
				if (!matcher3.matches()){
					Util.getContentValidate(R.string.toast_actmoney_lowerpricetpye);
					mTotalVolume.findFocus();
					break;
				}
				//请输入发行红包总额
				if (Util.isEmpty(totalValue)){
					Util.getContentValidate(R.string.toast_actmoney_totalvalue);
					mTotalValue.findFocus();
					break;
				}
				//发行红包总额类型不正确
				if (!matcher4.matches()){
					Util.getContentValidate(R.string.toast_actmoney_totalvaluetype);
					mTotalVolume.findFocus();
					break;
				}
				//红包发行总额不得低于最大金额
				if (Integer.parseInt(totalValue) < Integer.parseInt(upperPrice)){
					Util.getContentValidate(R.string.toast_actmoney_totalValue);
					mTotalValue.findFocus();
					break;
				}
				//红包金额最少100元
				if (Integer.parseInt(totalValue) < MONEY_TOTAL){
					Util.getContentValidate(R.string.toast_actmoney_totalvaluemin);
					mTotalValue.findFocus();
					break;
				}
				//请输入每天限制发行红包的数量
				if (Util.isEmpty(nbrPerDay)){
					Util.getContentValidate(R.string.toast_actmoney_nbrperday);
					mNbrPerDay.findFocus();
					break;
				}
				//发送红包数量类型不正确
				if (!matcher5.matches()){
					Util.getContentValidate(R.string.toast_actmoney_nbrpertype);
					mTotalVolume.findFocus();
					break;
				}
				//请输入红包使用有效期限
			/*if(Util.isEmpty(validityPeriod)){
				Util.getContentValidate(getMyActivity(), getString(R.string.toast_actmoney_validityperiod));
				mValidityPeriod.findFocus();
				break;
			}
			if(Integer.parseInt(validityPeriod) == TIME_ZERO){
				Util.getContentValidate(getMyActivity(), getString(R.string.toast_actmoney_totalvolumezero));
				mValidityPeriod.findFocus();
				break;
			}
			//有效期限类型不正确
			if(!matcher5.matches()){
				Util.getContentValidate(getMyActivity(), getString(R.string.toast_actmoney_validityperiodtype));
				mTotalVolume.findFocus();
				break;
			}*/
				//判断开始时间与结束时间
				double date = Util.timeSize(getMyActivity(), addMoneyEnd, addMoneyStart);
				if (mStartDateFlag){
					Util.getContentValidate(R.string.toast_time_start_now);
					break;
				}
				if (mEndDateFlag){
					Util.getContentValidate(R.string.toast_time_end_now);
					break;
				}

				if (date <= TIME_ZERO) {
					Util.getContentValidate(R.string.toast_actlist_time);
					break;
				}

				mBtnCommit.setEnabled(false);
				new ActAddMoneyTask(getMyActivity(), new ActAddMoneyTask.Callback() {

					@Override
					public void getResult(JSONObject result) {
						mBtnCommit.setEnabled(true);
						if (result == null){
							return;
						}

						if (result.get("code").toString().equals(String.valueOf("50000"))){

							Intent intent = new Intent(getMyActivity(), ActListActivity.class);
							getMyActivity().startActivity(intent);
						}

					}
				}).execute(shopCode,creatorCode,upperPrice,lowerPrice,
						totalValue,totalVolume,String.valueOf(Util.NUM_ZERO),addMoneyStart,addMoneyEnd,tokenCode);

				break;
			default:
				break;
		}
	}


	/**
	 * 点击返回到红包列表
	 * @param view
	 */
	@OnClick(R.id.layout_turn_in)
	public void btnActAddMoneyClick(View view) {
		getMyActivity().finish();
	}
}
