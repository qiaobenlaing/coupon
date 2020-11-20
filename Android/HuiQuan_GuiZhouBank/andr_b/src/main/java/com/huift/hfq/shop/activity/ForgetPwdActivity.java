package com.huift.hfq.shop.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huift.hfq.base.ErrorCode;
import com.huift.hfq.base.TimeCountUtil;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.pojo.UserToken;
import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.shop.R;
import com.huift.hfq.shop.model.ResetIdenCodeTask;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

import net.minidev.json.JSONObject;

/**
 * 验证原密码
 * @author qian.zhou
 */
public class ForgetPwdActivity extends Activity {
	public static final String VALIDATE_CODE = "validateCode";
	/** 登陆用户手机号码*/
	private String mPhone;
	/**获取验证码**/
	private String mIdenCode;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forget_pwd);
		Util.addActivity(ForgetPwdActivity.this);
		AppUtils.setActivity(this);
		AppUtils.setContext(getApplicationContext());
		ViewUtils.inject(this);
		//初始化视图
		init();
	}
	
	//初始化方法
	private void init() {
		//头部标题
		LinearLayout ivTurn = (LinearLayout) findViewById(R.id.layout_turn_in);
		ivTurn.setVisibility(View.VISIBLE);
		TextView tvContent = (TextView) findViewById(R.id.tv_mid_content);
		tvContent.setText(getResources().getString(R.string.title_forget_pwd));
		TextView tvPhoen = (TextView) findViewById(R.id.tv_phone);//点击获取验证码后
		TextView tvBeforePhoen = (TextView) findViewById(R.id.tv_my_phone);//点击获取验证码前
		UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		mPhone = userToken.getMobileNbr();
		String strBegin = mPhone.substring(0, 3);
		String strEnd = mPhone.substring(7, 11);
		tvPhoen.setText(strBegin + "****" + strEnd);
		tvBeforePhoen.setText(strBegin + "****" + strEnd);
		//初始化数据操作
		initData();
	}
	
	/**
	 * 数据操作
	 */
	public void initData(){
		//点击验证码获取验证码
		final TextView tvValidateCode = (TextView) findViewById(R.id.btn_validate_code);
		//忘记密码
		final LinearLayout lyValidate = (LinearLayout) findViewById(R.id.ly_validate);
		final LinearLayout lyForgetPwd = (LinearLayout) findViewById(R.id.ly_forget_pwd);
		final EditText etValidateCode = (EditText) findViewById(R.id.et_validate_code);
		lyForgetPwd.setVisibility(View.GONE);
		Button btnOk = (Button) findViewById(R.id.btn_ok);//确定
		tvValidateCode.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				lyValidate.setVisibility(View.GONE);
				lyForgetPwd.setVisibility(View.VISIBLE);
				//发送验证码
				TimeCountUtil timeCountUtil = new TimeCountUtil(ForgetPwdActivity.this, 60000, 1000,tvValidateCode);
				timeCountUtil.start();
				//获取验证码
				tvValidateCode.setEnabled(false);
				new ResetIdenCodeTask(ForgetPwdActivity.this, new ResetIdenCodeTask.Callback() {
					@Override
					public void getResult(JSONObject mResult) {
						tvValidateCode.setEnabled(true);
						if (mResult != null || mResult.size() == 0) {
							if (mResult.get("code").toString().equals(String.valueOf(ErrorCode.SUCC))) {
								tvValidateCode.setEnabled(true);
								mIdenCode = mResult.get("valCode").toString();
							} else {
								tvValidateCode.setEnabled(true);
							}
						}
					}
				}).execute(mPhone);
			}
		});
		
		btnOk.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String validateCode = etValidateCode.getText().toString();
				switch (v.getId()) {
				case R.id.btn_ok:
					if (Util.isEmpty(validateCode)) {
						Util.getContentValidate(R.string.input_msg_pwd);
						break;
					}
					if (!validateCode.equals(mIdenCode)) {
						Util.getContentValidate(R.string.input_validate_error);
						break;
					}
				default:
					Intent intent = new Intent(ForgetPwdActivity.this, UpdatePwdActivity.class);
					startActivity(intent);
					break;
				}
			}
		});
	}
	
	/**
	 * 点击返回查看到活动列表
	 * @param view
	 */
	@OnClick(R.id.layout_turn_in)
	public void btnBackClick(View view) {
		finish();
	}
	
	public void onResume(){
    	super.onResume();
        AppUtils.setActivity(this);
        AppUtils.setContext(getApplicationContext());
    }
}
