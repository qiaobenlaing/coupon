package com.huift.hfq.shop.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.minidev.json.JSONObject;
import android.app.Activity;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.huift.hfq.base.pojo.BussinessDistrictListBean;
import com.huift.hfq.shop.R;

import com.huift.hfq.base.ErrorCode;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.pojo.Shop;
import com.huift.hfq.base.utils.ActivityUtils;
import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.shop.activity.SettledCommitHomeActivity;
import com.huift.hfq.shop.activity.SettledCommitSuccActivity;
import com.huift.hfq.shop.model.ApplyEntryTask;
import com.huift.hfq.shop.model.GetBussinessDistrictListTask;
import com.huift.hfq.shop.model.SgetShopBasicInfoTask;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 门店信息
 * @author wensi.yu
 *
 */
public class SettledShopInfoFragment extends Fragment{
	
	private final static String TAG = "ShopInfoActivity";
	
	private static final String SETTLET_TITLE = "填写门店信息";
	private static final String START_TIMR = "8:00";
	private static final String END_TIMR = "20:00";
	/**返回图片*/
	private LinearLayout mIvBackup;
	/**功能描述文本*/
	private TextView mTvdesc;
	/**开始时间*/
	private EditText mShopStartTime;
	/**结束时间*/
	private EditText mShopEndTime;
	/**商户名称*/
	private EditText mShopName;
	/**联系电话*/
	private EditText mShopTel;
	/**店铺地址*/
	private EditText mShopAddress;
	/**手机号码*/
	private EditText mShopPhone;
	/**时间对话框时分秒*/
	private TimePickerDialog mPickerStartTimeDialog;
	private TimePickerDialog mPickerEndTimeDialog;
	/**设置开始时间**/
	private String mStartTimePicker;
	/**设置结束时间**/
	private String mEndTimePicker;
	/** 当前时间*/
	private String mNowDate;
	/** shop*/
	private Shop mShop;

	public static SettledShopInfoFragment newInstance() {
		Bundle args = new Bundle();
		SettledShopInfoFragment fragment = new SettledShopInfoFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_shopinfo, container,false);
		ViewUtils.inject(this,view);
		ActivityUtils.add(getMyActivity());
		Util.addActivity(getMyActivity());
		init(view);
		return view;
	}

	private void init(View view) {
		mIvBackup = (LinearLayout) view.findViewById(R.id.layout_turn_in);
		mIvBackup.setVisibility(View.VISIBLE);
		mTvdesc = (TextView) view.findViewById(R.id.tv_mid_content);
		mTvdesc.setText(SETTLET_TITLE);
		mShopName = (EditText) view.findViewById(R.id.et_shopinfo_shop);//店铺简称
		mShopTel = (EditText) view.findViewById(R.id.et_shopinfo_tel);//联系电话
		mShopStartTime = (EditText) view.findViewById(R.id.et_shopinfo_starttime);//营业开始时间
		mShopEndTime = (EditText) view.findViewById(R.id.et_shopinfo_endtime);//营业结束时间
		mShopAddress = (EditText) view.findViewById(R.id.et_shopinfo_address);//店铺地址
		mShopPhone = (EditText) view.findViewById(R.id.et_shopinfo_phone);//手机号码
		Log.d(TAG, "登录状态====="+DB.getBoolean(DB.Key.CUST_LOGIN));
		if (DB.getBoolean(DB.Key.CUST_LOGIN)) {
			sGetShopBasicInfo ();
		}
		//默认时间
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		mNowDate = sdf.format(new Date());
		mShopStartTime.setText(START_TIMR);
		mShopEndTime.setText(END_TIMR);
		//调用时间设置
		setActAddTime();
	}
	
	private Activity getMyActivity(){
		Activity act = getActivity();
		if (act == null){
			act = AppUtils.getActivity();
		}
		return act;       
	}
	
	/**
	 * 选择时间
	 */
	private void setActAddTime () {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.setTimeInMillis(System.currentTimeMillis());
		
		//开始时间时分秒
		mPickerStartTimeDialog = new TimePickerDialog(getMyActivity(), new OnTimeSetListener() {
			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
					mStartTimePicker = (hourOfDay > 9 ? hourOfDay : "0" + hourOfDay) + ":" + (minute > 9 ? minute : "0" + minute);
					mShopStartTime.setText(mStartTimePicker);
					mPickerStartTimeDialog.dismiss();
				}
		}, cal.get(cal.HOUR_OF_DAY), cal.get(cal.MINUTE), true);
		
		//结束时间时分秒
		mPickerEndTimeDialog = new TimePickerDialog(getMyActivity(), new OnTimeSetListener() {
			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
					mEndTimePicker = (hourOfDay > 9 ? hourOfDay : "0" + hourOfDay) + ":" + (minute > 9 ? minute : "0" + minute);
					mShopEndTime.setText(mEndTimePicker);
					mPickerEndTimeDialog.dismiss();
				}
		}, cal.get(cal.HOUR_OF_DAY), cal.get(cal.MINUTE), true);
	}
	
	/**
	 * 获得商铺的基本信息
	 */
	private void sGetShopBasicInfo () {
		new SgetShopBasicInfoTask(getMyActivity(),new SgetShopBasicInfoTask.Callback() {
			@Override
			public void getResult(JSONObject result) {
				Log.d(TAG, "进来了。。。。。。。。。。。");
				if (null == result) {
					return;
				}
				
				mShop = Util.json2Obj(result.toString(), Shop.class);
				
				//商铺名称
				if (Util.isEmpty(mShop.getShopName())) {
					mShopName.setText("");
				}else{
					mShopName.setText(mShop.getShopName());
				}
				//联系电话
				if (Util.isEmpty(mShop.getTel())) {
					mShopTel.setText("");
				}else{
					mShopTel.setText(mShop.getTel());
				}
				//店铺地址
				if ( Util.isEmpty(mShop.getProvince()) || Util.isEmpty(mShop.getCity()) || Util.isEmpty(mShop.getStreet())) {
					mShopAddress.setText("");
				}else{
					mShopAddress.setText(mShop.getProvince() + mShop.getCity() +  mShop.getStreet());
				}
				//手机号码
				if ( Util.isEmpty(mShop.getMobileNbr())) {
					mShopPhone.setText("");
				}else{
					mShopPhone.setText(mShop.getMobileNbr());
				}
			}
		}).execute();
	}
	
	/**
	 * 弹出开始和结束时间
	 */
	@OnClick({R.id.et_shopinfo_starttime,R.id.et_shopinfo_endtime,R.id.layout_turn_in,R.id.btn_shopinfo_apply})
	public void EtActAddStartTimeClick(View view) {	
		switch (view.getId()) {
		case R.id.et_shopinfo_starttime:
			mPickerStartTimeDialog.show();
			break;
		case R.id.et_shopinfo_endtime:
			mPickerEndTimeDialog.show();
			break;
		case R.id.layout_turn_in://返回
			getMyActivity().finish();
			break;
		case R.id.btn_shopinfo_apply://提交申请
			String shopName = mShopName.getText().toString();
			String shopTel = mShopTel.getText().toString();
			String shopStartTime = mShopStartTime.getText().toString();
			String shopEndTime = mShopEndTime.getText().toString();
			String shopAddress = mShopAddress.getText().toString();
			String shopPhone = mShopPhone.getText().toString();
			
			if (Util.isEmpty(shopName)) {
				getDataInfo();
				break;
			}
			
			if (Util.isEmpty(shopTel)) {
				getDataInfo();
				break;
			}
			
			//判断开始时间与结束时间
			long date = Util.timeSizeDay(getMyActivity(), shopEndTime, shopStartTime);
			if (date <= Util.NUM_ZERO) {
				Log.d(TAG, "date=" + date);
				Util.getContentValidate(R.string.toast_shopinfo_time);
				break;
			}
			
			if (Util.isEmpty(shopAddress)) {
				getDataInfo();
				break;
			}
			if (Util.isEmpty(shopPhone)) {
				getDataInfo();
				break;
			}
			
			//手机号码格式不正确
			if(Util.isPhone(getMyActivity(), shopPhone)){
				break;
			}
			
			if (DB.getBoolean(DB.Key.CUST_LOGIN)) {
				startActivity(new Intent(getMyActivity(),SettledCommitHomeActivity.class ));
			}else{
				new ApplyEntryTask(getMyActivity(), new ApplyEntryTask.Callback() {
					@Override
					public void getResult(JSONObject result) {
						Log.d(TAG, "进来了。。。。。。。。。。。");
						if (null == result) {
							return;
						}
						
						if (Integer.parseInt(result.get("code").toString()) == ErrorCode.SUCC) {
							Log.d(TAG, "提交成功。。。。。。。。。。");
							Util.getContentValidate(R.string.toast_commitsucc_succ);
							startActivity(new Intent(getMyActivity(),SettledCommitSuccActivity.class ));
						}
					}

				}).execute(shopName,shopTel,shopStartTime,shopEndTime,shopAddress,shopPhone);
			}
			
			break;
			
		default:
			break;
		}
	}
	
	/**
	 * 提示信息不能为空
	 */
	private void getDataInfo() {
		Util.getContentValidate(R.string.shopinfo_data);
	}
	
	public void onResume(){
    	super.onResume();
        AppUtils.setActivity(getMyActivity());
    }
}
