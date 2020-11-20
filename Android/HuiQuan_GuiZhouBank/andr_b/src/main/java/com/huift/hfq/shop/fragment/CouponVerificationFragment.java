package com.huift.hfq.shop.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.huift.hfq.base.ErrorCode;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.pojo.Coupon;
import com.huift.hfq.base.utils.DialogUtils;
import com.huift.hfq.shop.R;
import com.huift.hfq.shop.ShopConst;
import com.huift.hfq.shop.model.UseCouponTask;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

import net.minidev.json.JSONObject;

/**
 * 对兑换券和代金券的验证
 * @author qian.zhou
 */
public class CouponVerificationFragment extends Fragment {
	public static final String COUPON_OBG = "coupon";
	/** 保存券信息*/
	private Coupon mCoupon;
	/** 验证按钮*/
	private Button mBtnVerifivcation;
	/** 验证*/
	private TextView mTvCouponStatus;

	/**    
	 * 需要传递参数时有利于解耦
	 */
	public static CouponVerificationFragment newInstance() {
		Bundle args = new Bundle();
		CouponVerificationFragment fragment = new CouponVerificationFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_coupon_verification, container, false);
		ViewUtils.inject(this, view);
		Util.addLoginActivity(getActivity());
		init(view);
		return view;
	}

	// 初始化方法
	private void init(View view) {
		TextView tvTitle = (TextView) view.findViewById(R.id.tv_mid_content);
		tvTitle.setText(Util.getString(R.string.coupon_verification));
		TextView tvCouponTitle = (TextView) view .findViewById(R.id.tv_activity_title);//券标题
		TextView tvSecCode = (TextView) view .findViewById(R.id.tv_seccode);//验证码
		mTvCouponStatus = (TextView) view .findViewById(R.id.tv_activity_status);//状态
		TextView tvUseDate = (TextView) view .findViewById(R.id.tv_userdate);//使用日期
		TextView tvUseTime = (TextView) view .findViewById(R.id.tv_usertime);//每天使用时间
		TextView tvuseIntro = (TextView) view .findViewById(R.id.tv_userintro);//使用说明
		mBtnVerifivcation = (Button) view .findViewById(R.id.btn_activity_verification);//验证
		//取值
		Intent intent = getActivity().getIntent();
		mCoupon = (Coupon) intent.getSerializableExtra(COUPON_OBG);
		if (mCoupon != null) {
			mBtnVerifivcation.setOnClickListener(verificationListener);
			tvCouponTitle.setText(!Util.isEmpty(mCoupon.getFunction()) ? mCoupon.getFunction() : "");
			tvSecCode.setText(!Util.isEmpty(mCoupon.getCouponCode()) ? mCoupon.getCouponCode() : "");
			if (!Util.isEmpty(mCoupon.getStatus())&& !Util.isEmpty(mCoupon.getIsExpire())) {
				getStatus(mTvCouponStatus, mBtnVerifivcation, mCoupon.getStatus(), mCoupon.getIsExpire());
			} else {
				Toast.makeText(getActivity(), "获取的状态是空的", Toast.LENGTH_SHORT).show();
			}
			String startUseTime = "";
			String endUseTime = "";
			if (!Util.isEmpty(mCoupon.getStartUsingTime())){
				if (mCoupon.getStartUsingTime().length()>=10){
					startUseTime = mCoupon.getStartUsingTime().substring(0, 10);
				}else {
					startUseTime=mCoupon.getStartUsingTime();
				}
			}
			if (!Util.isEmpty(mCoupon.getExpireTime())){
				if (mCoupon.getExpireTime().length()>=10){
					endUseTime = mCoupon.getExpireTime().substring(0, 10);
				}else {
					endUseTime = mCoupon.getExpireTime();
				}
			}
			tvUseDate.setText(startUseTime + "  -  " + endUseTime);
			tvUseTime.setText(!Util.isEmpty(mCoupon.getDayStartUsingTime()) || !Util.isEmpty(mCoupon.getDayEndUsingTime()) ? mCoupon.getDayStartUsingTime() + "  -  " + mCoupon.getDayEndUsingTime() : "");
			tvuseIntro.setText(!Util.isEmpty(mCoupon.getRemark()) ? mCoupon.getRemark() : "");
		}
	}
	
	/**
	 * 验证
	 */
	OnClickListener verificationListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			DialogUtils.showInputDialog(getActivity(),getString(R.string.myafter_order__table_title),getString(R.string.input_moneyAccount), getString(R.string.ok), getString(R.string.no), new DialogUtils().new OnResListener(){
				@Override
				public void onOk(String... params) {
					if (TextUtils.isEmpty(params[0])){
						Util.getContentValidate(R.string.toast_consumptionamount_no);
					}else {
						useCoupon(params[0]);
					}
				}
			});
//			DialogUtils.showDialog(getActivity(), getString(R.string.cue),getString(R.string.isverification), getString(R.string.ok), getString(R.string.no), new DialogUtils().new OnResultListener() {
//				@Override
//				public void onOK() {
//					useCoupon();
//				}
//			});
		}
	};
	
	/**
	 * 对兑换券和代金券的验证
	 */
	public void useCoupon(String money){
		new UseCouponTask(getActivity(), new UseCouponTask.Callback() {
			@Override
			public void getResult(JSONObject result) {
				if (result != null) {
					if (String.valueOf(result.get("code")).equals(String.valueOf(ErrorCode.SUCC))) {
						mBtnVerifivcation.setBackgroundResource(R.drawable.verification_fail);
						mBtnVerifivcation.setEnabled(false);
						Util.getContentValidate(R.string.verification_success);
						mTvCouponStatus.setText(R.string.verified);
					} else {
						if (result.get("msg")!=null){
							String msg=(String) result.get("msg");
							Util.showToastZH(msg);
						}else {
							Util.getContentValidate(R.string.verification_fail);
						}
					}
				}
			}
		}).execute(mCoupon.getUserCode(), mCoupon.getUserCouponCode(),money);
	}
	
	/**
	 * 根据返回的状态编码显示状态
	 */
	public void getStatus(TextView tvstatus, Button btnVerification, String  status, String isExpire) {
		if (status.equals(ShopConst.CouponVerification.USERD)) {
			tvstatus.setText(R.string.verified);
			btnVerification.setBackgroundResource(R.drawable.verification_fail);
			btnVerification.setEnabled(false);
			
		} else if (status.equals(ShopConst.CouponVerification.AVAILABLE) && (String.valueOf(Util.NUM_ONE).equals(isExpire))) {
			tvstatus.setText(R.string.unverified);
			btnVerification.setBackgroundResource(R.drawable.login_btn);
			btnVerification.setEnabled(true);
		} else if (status.equals(ShopConst.CouponVerification.REQUEST_REFUNDED)) {
			tvstatus.setText(R.string.request_refund);
			btnVerification.setBackgroundResource(R.drawable.verification_fail);
			btnVerification.setEnabled(false);
			
		} else if (status.equals(ShopConst.CouponVerification.REFUNDED)) {
			tvstatus.setText(R.string.unavailable);
			btnVerification.setBackgroundResource(R.drawable.verification_fail);
			btnVerification.setEnabled(false);
			
		} else if (status.equals(ShopConst.CouponVerification.EXPIRED)) {
			
			tvstatus.setText(R.string.expired);
			btnVerification.setBackgroundResource(R.drawable.verification_fail);
			btnVerification.setEnabled(false);
		} else {
			tvstatus.setText(R.string.unverified);
			btnVerification.setBackgroundResource(R.drawable.verification_fail);
			btnVerification.setEnabled(false);
		}
	}
	
	/** 点击返回图标返回上一级 **/
	@OnClick(R.id.layout_turn_in)
	public void ivbackupClick(View view) {
		getActivity().finish();
	}
}
