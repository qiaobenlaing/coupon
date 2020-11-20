package cn.suanzi.baomi.shop.fragment;

import net.minidev.json.JSONObject;
import cn.suanzi.baomi.base.ErrorCode;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.pojo.Activitys;
import cn.suanzi.baomi.base.utils.DialogUtils;
import cn.suanzi.baomi.shop.R;
import cn.suanzi.baomi.shop.ShopConst;
import cn.suanzi.baomi.shop.model.ValUserActCodeTask;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import cn.suanzi.baomi.base.ErrorCode;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.pojo.Coupon;
import cn.suanzi.baomi.base.utils.DialogUtils;
import cn.suanzi.baomi.shop.R;
import cn.suanzi.baomi.shop.ShopConst;
import cn.suanzi.baomi.shop.model.UseCouponTask;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 活动的验证
 * @author qian.zhou
 */
public class ActivityVerificationFragment extends Fragment {
	private static final String TAG = ActivityVerificationFragment.class.getSimpleName();
	public static final String ACTIVITY_OBJ = "activitys";
	/** 保存券信息*/
	private Activitys mActivitys;
	/** 验证按钮*/
	private Button mBtnVerifivcation;

	/**    
	 * 需要传递参数时有利于解耦
	 */
	public static ActivityVerificationFragment newInstance() {
		Bundle args = new Bundle();
		ActivityVerificationFragment fragment = new ActivityVerificationFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_activity_verification, container, false);
		ViewUtils.inject(this, view);
		Util.addLoginActivity(getActivity());
		init(view);
		return view;
	}

	// 初始化方法
	private void init(View view) {
		TextView tvTitle = (TextView) view.findViewById(R.id.tv_mid_content);
		tvTitle.setText(Util.getString(R.string.activity_verification));
		TextView tvCouponTitle = (TextView) view .findViewById(R.id.tv_activity_title);//券标题
		TextView tvSecCode = (TextView) view .findViewById(R.id.tv_seccode);//验证码
		TextView tvCouponStatus = (TextView) view .findViewById(R.id.tv_activity_status);//状态
		TextView tvActivtyTime = (TextView) view .findViewById(R.id.tv_activity_time);//活动时间
		TextView tvActivityAddress = (TextView) view .findViewById(R.id.tv_activity_address);//活动地址
		TextView tvContacts = (TextView) view .findViewById(R.id.tv_contacts);//联系人
		mBtnVerifivcation = (Button) view .findViewById(R.id.btn_activity_verification);//验证
		//取值
		Intent intent = getActivity().getIntent();
		mActivitys = (Activitys) intent.getSerializableExtra(ACTIVITY_OBJ);
		if (mActivitys != null) {
			mBtnVerifivcation.setOnClickListener(verificationListener);
			//活动名称
			tvCouponTitle.setText(!Util.isEmpty(mActivitys.getActivityName()) ? mActivitys.getActivityName() : "");
			//验证码
			tvSecCode.setText(!Util.isEmpty(mActivitys.getActCode()) ? mActivitys.getActCode() : "");
			//活动状态
			if (!Util.isEmpty(mActivitys.getStatus())) {
				getStatus(tvCouponStatus, mBtnVerifivcation, mActivitys.getStatus());
			} else {
				//
				Log.d(TAG, "获取的状态是空的....");
			}
			//活动时间
			String startTime = !Util.isEmpty(mActivitys.getStartTime()) ? mActivitys.getStartTime() : "";
			String endTime = !Util.isEmpty(mActivitys.getEndTime()) ? mActivitys.getEndTime() : "";
			tvActivtyTime.setText(startTime + " 至   " + endTime);
			//活动地址
			tvActivityAddress.setText(!Util.isEmpty(mActivitys.getActivityLocation()) ? mActivitys.getActivityLocation() : "");
			//活动联系人
			tvContacts.setText(!Util.isEmpty(mActivitys.getName()) ? mActivitys.getName() : "");
		}
	}
	
	/**
	 * 验证
	 */
	OnClickListener verificationListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			DialogUtils.showDialog(getActivity(), getString(R.string.cue),getString(R.string.isverification), getString(R.string.ok), getString(R.string.no), new DialogUtils().new OnResultListener() {
				@Override
				public void onOK() {
					useActivity();
				} 
			});
		}
	};
	
	/**
	 * 对活动的验证
	 */
	public void useActivity(){
		new ValUserActCodeTask(getActivity(), new ValUserActCodeTask.Callback() {
			@Override
			public void getResult(JSONObject result) {
				if (result == null) {
					return;
				} else {
					if (String.valueOf(ErrorCode.SUCC).equals(result.get("code").toString())) {
						mBtnVerifivcation.setBackgroundResource(R.drawable.verification_fail);
						Util.getContentValidate(R.string.verification_success);
					} else {
						Util.getContentValidate(R.string.verification_fail);
					}
				}
			}
		}).execute(mActivitys.getActCode(), mActivitys.getUserActCodeId());
	}
	
	/**
	 * 根据返回的状态编码显示状态
	 */
	public void getStatus(TextView tvstatus, Button btnVerification, String  status) {
		if (status.equals(ShopConst.ActivityVerification.APPLIED_REFUNDED)) {
			tvstatus.setText(R.string.applied_refund);
			btnVerification.setBackgroundResource(R.drawable.verification_fail);
			btnVerification.setEnabled(false);
			
		} else if (status.equals(ShopConst.ActivityVerification.UNVERIFIED)) {
			tvstatus.setText(R.string.unverified);
			btnVerification.setBackgroundResource(R.drawable.login_btn);
			btnVerification.setEnabled(true);
			
		} else if (status.equals(ShopConst.ActivityVerification.REFUNDED)) {
			tvstatus.setText(R.string.refund);
			btnVerification.setBackgroundResource(R.drawable.verification_fail);
			btnVerification.setEnabled(false);
			
		} else if (status.equals(ShopConst.ActivityVerification.VERIFIED)) {
			tvstatus.setText(R.string.verified);
			btnVerification.setBackgroundResource(R.drawable.verification_fail);
			btnVerification.setEnabled(false);
			
		}  else {
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
