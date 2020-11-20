package com.huift.hfq.shop.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.huift.hfq.shop.R;

import com.huift.hfq.base.Util;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.pojo.CouponBill;
import com.huift.hfq.base.pojo.UserToken;
import com.huift.hfq.shop.model.GetCouponBillTask;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 优惠券对账
 * @author wensi.yu
 *
 */
public class AccntCouponFragment extends Fragment {

	private static final String COUPON_TITLE = "优惠券对账";
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
	@ViewInject(R.id.et_accntcoupon_start)
	private TextView mTvStartTime;
	/** 结束时间 */
	@ViewInject(R.id.et_accntcoupon_end)
	private TextView mTvEndTime;
	/** 统计 */
	@ViewInject(R.id.btn_couponok)
	private Button mBtnCouponOk;
	/** 时间设置对话框**/
	private DatePickerDialog mDatePickerDialog;
	private DatePickerDialog mPickerDialog;
	/** 优惠券的类*/
	private JSONObject couponBillObject;
	/** N元购*/
	@ViewInject(R.id.tv_couponn_full)
	private TextView mTvCouponnFull;
	@ViewInject(R.id.tv_couponn_all)
	private TextView mTvCouponnAll;
	@ViewInject(R.id.tv_couponn_our)
	private TextView mTvCouponnOur;
	/** 满就减*/
	@ViewInject(R.id.tv_couponfullreduce_full)
	private TextView mTvFullReduceFull;
	@ViewInject(R.id.tv_couponfullreduce_all)
	private TextView mTvFullReduceAll;
	@ViewInject(R.id.tv_couponfullreduce_our)
	private TextView mTvFullReduceOur;
	/** 限时购*/
	@ViewInject(R.id.tv_coupontime_full)
	private TextView mTvCoupontimeFull;
	@ViewInject(R.id.tv_coupontiem_all)
	private TextView mTvCoupontimeAll;
	@ViewInject(R.id.tv_coupontime_our)
	private TextView mTvCoupontimeOur;
	/** 实物劵*/
	@ViewInject(R.id.tv_couponmatter_full)
	private TextView mTvCouponmatterFull;
	@ViewInject(R.id.tv_couponmatter_all)
	private TextView mTvCouponmatterAll;
	@ViewInject(R.id.tv_couponmatter_our)
	private TextView mTvCouponmatterOur;
	/** 体验券*/ 
	@ViewInject(R.id.tv_couponexperience_full)
	private TextView mTvCouponexperienceFull;
	@ViewInject(R.id.tv_couponexperience_all)
	private TextView tvCouponexperiencerAll;
	@ViewInject(R.id.tv_couponexperience_our)
	private TextView tvCouponexperienceOur;
	
	public static AccntCouponFragment newInstance() {
		Bundle args = new Bundle();
		AccntCouponFragment fragment = new AccntCouponFragment();
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_accnt_coupon, container,false);
		ViewUtils.inject(this, view);
		Util.addActivity(getActivity());
		Util.addLoginActivity(getActivity());
		init(view);
		return view;
	}

	/**
	 * 初始化
	 * @param view
	 */
	private void init(View view) {
		//设置标题
		mTvdesc.setText(COUPON_TITLE);
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
	@OnClick(R.id.et_accntcoupon_start)
	public void EtActAddStartTimeClick(View view) {	
		mDatePickerDialog.show();
	}
	
	/**
	 * 弹出结束时间格式
	 */
	@OnClick(R.id.et_accntcoupon_end)
	public void EtActAddEndTimeClick(View view) {		
		mPickerDialog.show();
	}
	
	/**
	 * 跳转
	 * @param view
	 */
	@OnClick({R.id.layout_turn_in,R.id.btn_couponok})
	public void trunIdenCode(View view) {
		switch (view.getId()) {
		case R.id.layout_turn_in:
			getActivity().finish();
			break;
		case R.id.btn_couponok:
			UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
			String shopCode = userToken.getShopCode();//商家编码
			String tokenCode = userToken.getTokenCode();//需要令牌认证
			//时间
			String couponStartTime = mTvStartTime.getText().toString();
			String couponEndTime = mTvEndTime.getText().toString();
			
			//判断开始时间与结束时间
			/*double date = Util.timeSizes(getActivity(), couponEndTime, couponStartTime);
			if (date <= TIME_ZERO) {
				Util.getContentValidate(getActivity(), getActivity().getResources().getString(R.string.toast_actlist_time));
				break;
			}*/
			
			new GetCouponBillTask(getActivity(), new GetCouponBillTask.Callback() {
				@Override
				public void getResult(JSONArray result) {
					if (null == result){
						return;
					}
					List<CouponBill> couponBillData = new ArrayList<CouponBill>();
					for (int i = 0; i < result.size(); i++) {
						couponBillObject = (JSONObject) result.get(i);
						String type = couponBillObject.get("couponType").toString();
						if (Integer.parseInt(type) == 1){// N元购
							getCouponType(mTvCouponnFull,mTvCouponnAll,mTvCouponnOur);
						}
						
						if (Integer.parseInt(type) == 3){//满就减
							getCouponType(mTvFullReduceFull,mTvFullReduceAll,mTvFullReduceOur);
						}
						
						if (Integer.parseInt(type) == 4){//限时购
							getCouponType(mTvCoupontimeFull,mTvCoupontimeAll,mTvCoupontimeOur);
						}
						
						if (Integer.parseInt(type) == 5){//实物券
							getCouponType(mTvCouponmatterFull,mTvCouponmatterAll,mTvCouponmatterOur);
						}
						
						if (Integer.parseInt(type) == 6){//体验券
							getCouponType(mTvCouponexperienceFull,tvCouponexperiencerAll,tvCouponexperienceOur);
						}
					}   
				}
			}).execute(shopCode,couponStartTime,couponEndTime);
			
			break;

		default:
			break;
		}
	}
	
	private void getCouponType(TextView couponFull,TextView couponAll,TextView hqAmount){
		couponFull.setText(couponBillObject.get("usedCount").toString());
		couponAll.setText(couponBillObject.get("usedAmount").toString());
		hqAmount.setText(couponBillObject.get("hqAmount").toString());
	}
}
