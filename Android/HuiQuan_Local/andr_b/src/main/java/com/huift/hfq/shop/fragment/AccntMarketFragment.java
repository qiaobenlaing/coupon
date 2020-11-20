package com.huift.hfq.shop.fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.huift.hfq.shop.R;

import com.huift.hfq.base.Util;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class AccntMarketFragment extends Fragment {

	private final static String TAG = "AccntMarketFragment";
	
	private static final String MARKET_TITLE = "营销活动对账";
	private final static String ACT_ZERO = "0";
	private final static int TIME_NUMBER = 9;
	private final static int TIME_ZERO = 0;
	/** 返回图片 */
	@ViewInject(R.id.layout_turn_in)
	private LinearLayout mIvBackup;
	/** 功能描述文本 */
	@ViewInject(R.id.tv_mid_content)
	private TextView mTvdesc;
	/** 开始时间*/
	@ViewInject(R.id.et_accntmarket_start)
	private TextView mTvStartTime;
	/** 结束时间 */
	@ViewInject(R.id.et_accntmarket_end)
	private TextView mTvEndTime;
	/** 统计 */
	@ViewInject(R.id.btn_market)
	private Button mBtnMarket;
	/** 时间设置对话框**/
	private DatePickerDialog mDatePickerDialog;
	private DatePickerDialog mPickerDialog;
	
	public static AccntMarketFragment newInstance() {
		Bundle args = new Bundle();
		AccntMarketFragment fragment = new AccntMarketFragment();
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_accnt_market, container,false);
		ViewUtils.inject(this, view);
		init(view);
		return view;
	}

	/**
	 * 初始化
	 * @param view
	 */
	private void init(View view) {
		//添加
		Util.addActivity(getActivity());
		Util.addLoginActivity(getActivity());
		//设置标题
		mTvdesc.setText(MARKET_TITLE);
		mIvBackup.setVisibility(View.VISIBLE);
		//默认时间
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		mTvStartTime.setText(sdf.format(new Date()));
		mTvEndTime.setText(sdf.format(new Date()));
		//调用时间设置
		setActAddTime();
	}
	
	/**
	 *设置时间格式 
	 */
	private void setActAddTime(){
		Calendar cal=Calendar.getInstance();
		cal.setTime(new Date());
		//开始时间格式
		mDatePickerDialog = new DatePickerDialog(getActivity(), new OnDateSetListener() {
			
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				mTvStartTime.setText(year + "-" + ((monthOfYear+1) > TIME_NUMBER ? (monthOfYear+1) : ACT_ZERO + (monthOfYear+1)) + "-" + (dayOfMonth > 9 ? dayOfMonth : ACT_ZERO+dayOfMonth));
				mDatePickerDialog.dismiss();
			}
		}, cal.get(cal.YEAR), cal.get(cal.MONTH), cal.get(cal.DAY_OF_MONTH));
		//结束时间格式
		mPickerDialog = new DatePickerDialog(getActivity(), new OnDateSetListener() {
			
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				mTvEndTime.setText(year +"-"+ ((monthOfYear+1) > TIME_NUMBER ? (monthOfYear+1) : ACT_ZERO + (monthOfYear+1)) + "-" + (dayOfMonth>9?dayOfMonth : ACT_ZERO + dayOfMonth));
				mPickerDialog.dismiss();
			}
		}, cal.get(cal.YEAR), cal.get(cal.MONTH), cal.get(cal.DAY_OF_MONTH));
		
	}
	
	/**
	 * 弹出开始时间格式
	 */
	@OnClick(R.id.et_accntmarket_start)
	public void EtActAddStartTimeClick(View view) {	
		mDatePickerDialog.show();
	}
	
	/**
	 * 弹出结束时间格式
	 */
	@OnClick(R.id.et_accntmarket_end)
	public void EtActAddEndTimeClick(View view) {		
		mPickerDialog.show();
	}
	
	/**
	 * 返回
	 * @param view
	 */
	@OnClick({R.id.layout_turn_in,R.id.btn_market})
	public void trunIdenCode(View view) {
		switch (view.getId()) {
		case R.id.layout_turn_in:
			getActivity().finish();
			break;
		case R.id.btn_market:
			//营销活动
			TextView tvRemarketFull =  (TextView) view.findViewById(R.id.tv_remarket_full);
			TextView tvRemarketAll =  (TextView) view.findViewById(R.id.tv_remarket_all);
			TextView tvRemarketOur =  (TextView) view.findViewById(R.id.tv_remarket_our);
			//红包统计
			TextView tvMoneyFull =  (TextView) view.findViewById(R.id.tv_money_full);
			TextView tvMoneyAll =  (TextView) view.findViewById(R.id.tv_remarket_all);
			TextView tvMoneyOur =  (TextView) view.findViewById(R.id.tv_remarket_our);
			//时间
			String remarketStartTime = mTvStartTime.getText().toString();
			String remarketEndTime = mTvEndTime.getText().toString();
			//判断开始时间与结束时间
			double date = Util.timeSizes(getActivity(), remarketEndTime, remarketStartTime);
			if (date <= TIME_ZERO) {
				Util.getContentValidate(R.string.toast_actlist_time);
				break;
			}
			
			break;

		default:
			break;  
		}
		
	}
}
