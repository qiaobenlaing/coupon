package com.huift.hfq.cust.activity;


import net.minidev.json.JSONObject;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.huift.hfq.base.ErrorCode;
import com.huift.hfq.base.TimeCountUtil;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.pojo.UserToken;
import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.cust.R;

import com.huift.hfq.cust.model.ResetIdenCodeTask;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 验证原密码
 * @author qian.zhou
 */
public class ForgetPwdActivity extends Activity {
	public static final String TAG = "OriginalPwdActivity";
	/** 获得一个用户信息对象 **/
	private UserToken mUserToken;
	/** 登陆用户手机号码*/
	private String mPhone;
	/**获取验证码**/
	private String mIdenCode;
	/** 验证码*/
	private TextView mTvValidateCode;
	/** 验证码输入框*/
	private EditText mEtValidateCode;
	/** 在没发验证码之前的提示*/
	private LinearLayout mLyValidate;
	/** 已将验证码发送成功*/
	private LinearLayout mLyForgetPwd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forget_pwd);
		Util.addActivity(ForgetPwdActivity.this);
		AppUtils.setActivity(this);
		AppUtils.setContext(getApplicationContext());
		ViewUtils.inject(this);
		init();
	}
	
	//初始化方法
	private void init() {
		// 获得一个用户信息对象
		mUserToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		mPhone = mUserToken.getMobileNbr();
		//头部标题
		ImageView ivTurn = (ImageView) findViewById(R.id.iv_turn_in);
		ivTurn.setVisibility(View.VISIBLE);
		TextView tvContent = (TextView) findViewById(R.id.tv_mid_content);
		tvContent.setText(getResources().getString(R.string.title_forget_pwd));
		//忘记密码
		mLyValidate = (LinearLayout) findViewById(R.id.ly_validate);
		mLyForgetPwd = (LinearLayout) findViewById(R.id.ly_forget_pwd);
		setForget(true);
		TextView tvPhoen = (TextView) findViewById(R.id.tv_phone);//点击获取验证码后
		TextView tvBeforePhoen = (TextView) findViewById(R.id.tv_my_phone);//点击获取验证码前
		mEtValidateCode = (EditText) findViewById(R.id.et_validate_code);
		Button btnOk = (Button) findViewById(R.id.btn_ok);//确定
		String strBegin = mPhone.substring(0, 3);
		String strEnd = mPhone.substring(7, 11);
		tvPhoen.setText(strBegin + "****" + strEnd);
		tvBeforePhoen.setText(strBegin + "****" + strEnd);
		//点击验证码获取验证码
		mTvValidateCode  = (TextView) findViewById(R.id.btn_validate_code);
		mTvValidateCode.setOnClickListener(clickListener);
		btnOk.setOnClickListener(okListener);
	}
	
	/**
	 * 确定按钮的点击事件
	 */
	OnClickListener okListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			String validateCode = mEtValidateCode.getText().toString();
			switch (v.getId()) {
			case R.id.btn_ok:
				if (Util.isEmpty(validateCode)) {
					Util.getContentValidate(R.string.input_msg_pwd);
					break;
				}
				if(!validateCode.equals(mIdenCode)){
					Util.getContentValidate(R.string.input_validate_error);
					break;
				}
			default:
				Intent intent = new Intent(ForgetPwdActivity.this, UpdatePwdActivity.class);
				startActivity(intent);
				break;
			}
		}
	};
	
	/**
	 * 验证码的点击事件
	 */
	OnClickListener clickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			setForget(false);
			//发送验证码
			TimeCountUtil timeCountUtil = new TimeCountUtil(ForgetPwdActivity.this, 60000, 1000,mTvValidateCode);
			timeCountUtil.start();
			//获取验证码
			mTvValidateCode.setEnabled(false);
			new ResetIdenCodeTask(ForgetPwdActivity.this, new ResetIdenCodeTask.Callback() {
				@Override
				public void getResult(JSONObject mResult) {
					if(mResult != null || mResult.size() == 0){
						if (mResult.get("code").toString().equals(String.valueOf(ErrorCode.SUCC))) {
							mTvValidateCode.setEnabled(true);
							mIdenCode = mResult.get("valCode").toString();
							Log.d(TAG, "接受发送的验证码=============="+mIdenCode);
						}
						else {
							mTvValidateCode.setEnabled(true);
						}
					}
				}
			}).execute(mPhone);
		}
	};
	
	/**
	 * 设置的忘记密码的提示
	 * @param forget
	 */
	public void setForget(boolean forget){
		if (forget) {
			mLyForgetPwd.setVisibility(View.GONE);
			mLyValidate.setVisibility(View.VISIBLE);
		} else {
			mLyForgetPwd.setVisibility(View.VISIBLE);
			mLyValidate.setVisibility(View.GONE);
		}
	}
	
	/**
	 * 点击返回查看到活动列表
	 * @param view
	 */
	@OnClick(R.id.iv_turn_in)
	public void btnBackClick(View view) {
		finish();
	}
	
	public void onResume(){
    	super.onResume();
        AppUtils.setActivity(this);
        AppUtils.setContext(getApplicationContext());
   }
}
