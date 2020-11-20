package cn.suanzi.baomi.shop.fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.utils.ActivityUtils;
import cn.suanzi.baomi.base.utils.AppUtils;
import cn.suanzi.baomi.shop.R;
import cn.suanzi.baomi.shop.ShopConst;
import cn.suanzi.baomi.shop.activity.ActAddExplainActivity;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * @author wensi.yu
 * 添加营销活动主题，时间，地点
 */
public class ActAddFragment extends Fragment {
	
	private final static String TAG = "ActAddFragment";
	
	private final static String ACT_ZERO = "0";
	private final static int TIME_NUMBER = 9;
	private final static int TIME_ZERO = 0;
	/** 返回图片**/
	@ViewInject(R.id.layout_turn_in)
	private LinearLayout mIvBackup;
	/** 功能描述文本**/
	@ViewInject(R.id.tv_mid_content)
	private TextView mTvdesc;
	/** 点击下一步**/
	@ViewInject(R.id.btn_actadd_nextone)
	private Button mBtnActAddNextOne;
	/** 活动主题**/
	@ViewInject(R.id.et_actadd_theam)
	private EditText mEtActaddName;
	/** 活动开始时间**/
	@ViewInject(R.id.et_actadd_startTime)
	private EditText mEtActaddStartTime;
	/** 活动结束时间**/
	@ViewInject(R.id.et_actadd_endTime)
	private EditText mEtActaddEndTime;
	/** 活动地点**/
	@ViewInject(R.id.et_actadd_place)
	private EditText mEtActaddPlace;
	/** 当前时间*/
	private String mNowDate;
	/** 时间对话框年月日**/
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
	
	public static ActAddFragment newInstance() {
		Bundle args = new Bundle();
		ActAddFragment fragment = new ActAddFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_actlist_addtheam,container, false);// 说明v，注释 e.g:Fragment的view
		ViewUtils.inject(this, view);
		init();
		return view;
	}
	
	private Activity getMyActivity(){
		Activity act = getActivity();
		if (act == null){
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
		mTvdesc.setText(R.string.tv_actlist_title);
		//调用时间设置
		setActAddTime();
		//默认时间
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		mNowDate = sdf.format(new Date());
		mEtActaddStartTime.setText(mNowDate);
		mEtActaddEndTime.setText(mNowDate);
	}

	/**
	 *设置时间格式 
	 */
	private void setActAddTime(){
		Calendar cal=Calendar.getInstance();
		cal.setTime(new Date());
		//开始时间年月日
		mDatePickerDialog = new DatePickerDialog(getMyActivity(), new OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				
				mStartDatePicker = year +"-"+ ((monthOfYear+1) > TIME_NUMBER ? (monthOfYear+1) : ACT_ZERO + (monthOfYear+1)) + "-" + (dayOfMonth>9?dayOfMonth : ACT_ZERO + dayOfMonth);
				Log.i(TAG, "startDatePicker============"+mStartDatePicker);
				
				mDatePickerDialog.dismiss();
			}
		}, cal.get(cal.YEAR), cal.get(cal.MONTH), cal.get(cal.DAY_OF_MONTH));
		
		//开始时间时分秒
		mPickerStartTime = new TimePickerDialog(getMyActivity(), new OnTimeSetListener() {
			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
					mStartTimePicker = (hourOfDay > 9 ? hourOfDay : "0" + hourOfDay) + ":" + (minute > 9 ? minute : "0" + minute);
					
					String startTime = mStartDatePicker +" "+mStartTimePicker;
					Log.i(TAG, "startTime============="+startTime);   
					mEtActaddStartTime.setText(startTime);
					
					String nowtime = mNowDate;
					String startDate = mEtActaddStartTime.getText().toString();
					
					Log.d(TAG, "nowtime==============="+nowtime);
					Log.d(TAG, "startDate==============="+startDate);
					
					double actStartTime = Util.timeSizeData(getActivity(), startDate, nowtime)/1000;
					Log.d(TAG, "actStartTime==============="+actStartTime);
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
					mEndTimePicker = (hourOfDay > 9 ? hourOfDay : "0" + hourOfDay) + ":" + (minute > 9 ? minute : "0" + minute);
					
					String endTime = mEndDatePicker +" "+mEndTimePicker;
					Log.i(TAG, "endTime============="+endTime);
					mEtActaddEndTime.setText(endTime);
					
					
					String nowtime = mNowDate;
					String endDate = mEtActaddEndTime.getText().toString();
					
					double actEndTime = Util.timeSizeData(getActivity(), endDate, nowtime)/1000;
					Log.d(TAG, "actEndTime==============="+actEndTime);
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
	@OnClick(R.id.et_actadd_startTime)
	public void EtActAddStartTimeClick(View view) {	
		mPickerStartTime.show();
		mDatePickerDialog.show();
	}
	
	/**
	 * 弹出结束时间格式
	 */
	@OnClick(R.id.et_actadd_endTime)
	public void EtActAddEndTimeClick(View view) {	
		mPickerEndTime.show();
		mPickerDialog.show();
	}
	
	/**
	 * 点击下一步按钮到添加活动
	 * @param views
	 */
	@OnClick(R.id.btn_actadd_nextone)
	public void btnActAddNextClick(View view) {
		//活动主题
		String activityName = mEtActaddName.getText().toString();
		//活动开始时间
		String startTime = mEtActaddStartTime.getText().toString();
		//活动结束时间
		String endTime = mEtActaddEndTime.getText().toString();
		//活动地点
		String activityLocation = mEtActaddPlace.getText().toString();
		
		switch (view.getId()) {
		case R.id.btn_actadd_nextone:
			if (Util.isEmpty(activityName)){
				Util.getContentValidate(R.string.toast_actadd_theam);
				mEtActaddName.findFocus();
				break;
			}
			if (Util.isEmpty(startTime)){
				Util.getContentValidate(R.string.toast_actadd_starttime);
				break;
			}
			if (Util.isEmpty(endTime)){          
				Util.getContentValidate(R.string.toast_actadd_endtime);
				break;
			}
			//判断开始时间与结束时间
			double date = Util.timeSizeData(getMyActivity(), endTime, startTime);
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
			
			if (Util.isEmpty(activityLocation)){
				Util.getContentValidate(R.string.toast_actadd_address);
				mEtActaddPlace.findFocus();
				break;
			}
			//保存值
			SharedPreferences mSharedPreferences = getActivity().getSharedPreferences(ShopConst.ActAdd.ACT_ADD, Context.MODE_PRIVATE);
		    Editor editor = mSharedPreferences.edit();       
		    editor.putString("activityName", activityName);
		    editor.putString("startTime",startTime);
		    editor.putString("endTime",endTime);
		    editor.putString("activityLocation",activityLocation);
		    editor.commit();
			Intent intent = new Intent(getMyActivity(), ActAddExplainActivity.class);
			getMyActivity().startActivity(intent);
			break;

		default:         
			break;
		}
	}
	
	/**
	 * 点击返回按钮 返回到营销活动的列表主页
	 * @param view
	 */
	@OnClick(R.id.layout_turn_in)
	public void btnActAddBackClick(View view) {		
		getMyActivity().finish();
	}
}
