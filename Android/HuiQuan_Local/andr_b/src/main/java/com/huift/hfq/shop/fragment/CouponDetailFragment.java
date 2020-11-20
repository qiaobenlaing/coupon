package com.huift.hfq.shop.fragment;

import net.minidev.json.JSONObject;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.huift.hfq.shop.R;

import com.huift.hfq.base.ErrorCode;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.pojo.BatchCoupon;
import com.huift.hfq.base.utils.MyProgress;
import com.huift.hfq.shop.ShopConst;
import com.huift.hfq.shop.activity.CouponListActitivity;
import com.huift.hfq.shop.model.ChangeCouponStatusTask;
import com.huift.hfq.shop.model.GetCouponInfoTask;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 优惠券详情
 * @author yanfang.li
 */
public class CouponDetailFragment extends Fragment {
	
	private final static String TAG = CouponDetailFragment.class.getSimpleName();
	public final static String  DATE_FORMAT = "00:00";
	/**传优惠券编码*/
	public final static String COUPON_CODE = "couponCode";
	public final static int ENABLE = 1;
	public final static int STOP = 0;
	public final static String STOP_STR = "停发";
	public final static String NO_TIME = "0";
	
	/**优惠券编码*/
	private String couponCode;
	/**优惠券启用停用*/
	private TextView mTvEdit;
	/**进度条*/
	private TextView mTvDrawpercentage;
	private MyProgress mPgDrawPerson;
	private BatchCoupon mBatch;
	
	public static CouponDetailFragment newInstance() {
		Bundle args = new Bundle();
		CouponDetailFragment fragment = new CouponDetailFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_coupondetail,container, false);
		ViewUtils.inject(this, view);
		Util.addLoginActivity(getActivity());
		couponCode = getActivity().getIntent().getStringExtra(COUPON_CODE);
		LinearLayout ivReurn = (LinearLayout) view.findViewById(R.id.layout_turn_in);
		ivReurn.setVisibility(View.VISIBLE);
		TextView tvTitle = (TextView) view.findViewById(R.id.tv_mid_content);
		tvTitle.setText(getResources().getString(R.string.coupon_detail));
		mTvEdit = (TextView) view.findViewById(R.id.tv_msg);
		mTvEdit.setText(getResources().getString(R.string.tv_actmoney_enable));
		mTvDrawpercentage = (TextView) view.findViewById(R.id.coupon_drawpercentage);  // 领取人数的进度条
		mPgDrawPerson = (MyProgress) view.findViewById(R.id.progress_drawP);  // 领取人数的进度条
		getCouponInfo(view);
		return view;
	}
	
	/**
	 * 获得优惠券的详细列表
	 */
	private void getCouponInfo (final View view) {
		new GetCouponInfoTask(getActivity(),new GetCouponInfoTask.Callback() {
			
			@Override
			public void getResult(JSONObject result) {
				if (result == null) {
					return;
				}
				couponBatch (result,view);
			}
		}).execute(couponCode);
	}
	
	private void couponBatch (JSONObject result,View view) {
		
		TextView tvCouponBatch = (TextView) view.findViewById(R.id.tv_couponbatch); // 优惠券批次
		TextView tvTotalvolume = (TextView) view.findViewById(R.id.tv_totalvolume); // 总张数
		TextView tvAvailablePrice = (TextView) view.findViewById(R.id.tv_availablePrice);  // 满多少可使用
		TextView tvStartUseDate = (TextView) view.findViewById(R.id.tv_startUseDate);  // 开始使用时间
		TextView tvEndUseDate = (TextView) view.findViewById(R.id.tv_endUseDate);  // 结束使用时间
		TextView tvCouponType = (TextView) view.findViewById(R.id.tv_couponType);  // 什么类型的优惠券 面值多少
		TextView tvEndDrawDate = (TextView) view.findViewById(R.id.tv_endDrawDate);  // 截止领取时间
		MyProgress pgUsePerson = (MyProgress) view.findViewById(R.id.progress_use);  // 使用人数进度
		TextView tvUsepercentage = (TextView) view.findViewById(R.id.coupon_usepercentage);  // 使用人数的进度条
		TextView tvCouponRemark = (TextView) view.findViewById(R.id.tv_couponremark);  // 使用说明
	    mBatch = Util.json2Obj(result.toString(), BatchCoupon.class);
		
		// 可以干嘛
		String canMoney = "";
		// 面值
		String insteadPrice = "";
		// 优惠券类型名称
		String couponTypeName = "";
		if (ShopConst.Coupon.DEDUCT.equals(mBatch.getCouponType())) { // 抵扣券
			canMoney = "满" + mBatch.getAvailablePrice()+"元立减" + mBatch.getInsteadPrice() + "元";
			couponTypeName = getString(R.string.coupon_deduct);
		} else if (ShopConst.Coupon.DISCOUNT.equals(mBatch.getCouponType())) { //折扣券
			canMoney = "满" + mBatch.getAvailablePrice()+"元打" + mBatch.getDiscountPercent() + "折";
			couponTypeName = getString(R.string.coupon_discount);
			
		} else if (ShopConst.Coupon.N_BUY.equals(mBatch.getCouponType())) { // N元购
			canMoney = mBatch.getFunction();
			insteadPrice = mBatch.getInsteadPrice();
			couponTypeName = getString(R.string.coupon_nbuy);
			// 实物券和体验券
		} else if (ShopConst.Coupon.REAL_COUPON.equals(mBatch.getCouponType()) || ShopConst.Coupon.EXPERIENCE.equals(mBatch.getCouponType())) {
			if (ShopConst.Coupon.REAL_COUPON.equals(mBatch.getCouponType())) {
				couponTypeName = getString(R.string.coupon_real);
			} else {
				couponTypeName = getString(R.string.coupon_exp);
			}
			canMoney = mBatch.getFunction();
		}
		tvCouponBatch.setText("优惠券批次： "+ mBatch.getBatchNbr());
		tvTotalvolume.setText("优惠券数量： "+ mBatch.getTotalVolume());
		tvAvailablePrice.setText(couponTypeName + "：" + canMoney); // 满多少可干嘛 
		
		if (!Util.isEmpty(mBatch.getIsAvailable()+"")) {
			// 1-停用；0-启用
			if (mBatch.getIsAvailable() == STOP) { 
				mTvEdit.setText(getString(R.string.tv_actmoney_enable));
			} else {
				mTvEdit.setText(getString(R.string.tv_actmoney_user));
			}
		}
		
		// TODO
		if (Util.isEmpty(mBatch.getStartUsingTime()) || mBatch.getStartUsingTime().equals(NO_TIME)) {
			tvStartUseDate.setVisibility(View.GONE);
		} else {
			tvStartUseDate.setVisibility(View.VISIBLE);
			tvStartUseDate.setText("使用日期：" + mBatch.getStartUsingTime() + " - " + mBatch.getExpireTime() );
		}
		
		if (Util.isEmpty(mBatch.getDayStartUsingTime()) || mBatch.getDayStartUsingTime().equals(DATE_FORMAT)) {
			tvEndUseDate.setVisibility(View.GONE);
		} else {
			tvEndUseDate.setVisibility(View.VISIBLE);
			tvEndUseDate.setText("每天使用时间：" + mBatch.getDayStartUsingTime() + " - " + mBatch.getDayEndUsingTime());
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
			Log.e(TAG, "优惠券详情转换="+e.getMessage()); // TODO
		}
		
			// 1-可送；0-不可送
			if (send == Util.NUM_ONE) { // 不可送
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
		
		try{
			if (null == mBatch.getTakenCount() || Util.isEmpty(mBatch.getTakenCount())) {
				return;
			}
			int takenCount = Integer.parseInt(mBatch.getTakenCount());
			getStopCoupon(mTvEdit.getText().toString()); // 已领取的进度条
			// 使用的百分比
			int userCount = mBatch.getUsedCount();
			if (userCount <= 0) {
				tvUsepercentage.setText("未使用 (0/"+ mBatch.getTakenCount() +")"); //批次
				pgUsePerson.setMax(takenCount);
				pgUsePerson.setProgress(0);
			} else {
				
				pgUsePerson.setMax(takenCount);
				pgUsePerson.setProgress(userCount);
				tvUsepercentage.setText("已使用"+ mBatch.getUsedPercent() +"% ("+ mBatch.getUsedCount() + "/"+ mBatch.getTakenCount() +")"); //批次
			}
			
			
		} catch (Exception e) {
			Log.e(TAG, "优惠券批次转换出错="+e.getMessage()); //TODO
		}
		
	}
	
	/**
	 * 退出
	 */
	@OnClick(R.id.layout_turn_in)
	public void turnClick (View view) {
		Intent intent = new Intent(getActivity(), CouponListActitivity.class);
		intent.putExtra(CouponListFragment.COUPON_STATUS, mTvEdit.getText().toString());
		Log.e(TAG, "status:"+mTvEdit.getText().toString());
		getActivity().setResult(CouponListFragment.COUPON_SUCCESS, intent);
		getActivity().finish();
	}
	
	/**
	 * 启用 停用
	 * @param view
	 */
	@OnClick(R.id.tv_msg)
	private void clickStatus (View view) {
		final String stop = getString(R.string.tv_actmoney_user);
		final String enable = getString(R.string.tv_actmoney_enable);
		String statusStr = mTvEdit.getText().toString();
		if (stop.equals(statusStr)) { // 停用
			new AlertDialog.Builder(getActivity()).setTitle("温馨提示").setMessage("您确定要停用优惠券领取吗？").
			setPositiveButton("确定", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					mTvEdit.setEnabled(false);
					changeCouponStatus (STOP,stop,enable);
				}
			}).setNegativeButton("取消", null).show();
		} else { // 启用
			changeCouponStatus (ENABLE,enable,stop);
		}
	}
	
	/**
	 * 停用启用优惠券
	 */
	private void changeCouponStatus (int status,final String stop,final String enable) {
		new ChangeCouponStatusTask(getActivity(), new ChangeCouponStatusTask.Callback() {
			@Override
			public void getResult(JSONObject result) {
				mTvEdit.setEnabled(true);
				if (result == null) {
					mTvEdit.setText(stop);
				} else {
					int code = Integer.parseInt(result.get("code").toString());
					if (code == ErrorCode.SUCC) {
						mTvEdit.setText(enable);
						getStopCoupon (enable);
					} else {
						mTvEdit.setText(stop);
					}
				}
			}
		}).execute(couponCode,status+"");
	}
	
	private void getStopCoupon (String stop) {
		if (null == mBatch.getTakenCount() || Util.isEmpty(mBatch.getTakenCount())) {
			return;
		}
		int takenCount = Integer.parseInt(mBatch.getTakenCount());
		if (!stop.equals(STOP_STR)) {
			if (Util.isEmpty(mBatch.getTakenCount()) || takenCount <= 0) {
				mTvDrawpercentage.setText(STOP_STR + " (0/"+ mBatch.getTotalVolume() +")"); //批次
				mPgDrawPerson.setMax(mBatch.getTotalVolume());
				mPgDrawPerson.setProgress(0);
			} else {
				mTvDrawpercentage.setText(STOP_STR +" ("+ mBatch.getTakenCount() + "/"+ mBatch.getTotalVolume() +")"); //批次
				mPgDrawPerson.setMax(mBatch.getTotalVolume());
				mPgDrawPerson.setProgress(takenCount);
			}
		} else {
			if (Util.isEmpty(mBatch.getTakenCount()) || takenCount <= 0) {
				mTvDrawpercentage.setText("未领取 (0/"+ mBatch.getTotalVolume() +")"); //批次
				mPgDrawPerson.setMax(mBatch.getTotalVolume());
				mPgDrawPerson.setProgress(0);
			} else {
				mTvDrawpercentage.setText("已领取"+ mBatch.getTakenPercent() +"% ("+ mBatch.getTakenCount() + "/"+ mBatch.getTotalVolume() +")"); //批次
				mPgDrawPerson.setMax(mBatch.getTotalVolume());
				mPgDrawPerson.setProgress(takenCount);
			}
		}
	}
	
}
