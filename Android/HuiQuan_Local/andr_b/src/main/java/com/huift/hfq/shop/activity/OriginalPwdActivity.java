package com.huift.hfq.shop.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.huift.hfq.shop.R;

import com.huift.hfq.base.Util;
import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.base.utils.SetPwdUtils;
import com.huift.hfq.shop.model.getValPwdTask;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.umeng.analytics.MobclickAgent;

/**
 * 验证原密码
 * @author qian.zhou
 */
public class OriginalPwdActivity extends Activity {
	public static final String TAG = "OriginalPwdActivity";
	/** 加密后的MD5的密码*/
	private String mDoriginalPwd;
	/**显示密码的标示**/
	private Boolean mShowPassword = false;
	/** 原密码*/
	private EditText mEtOriginPwd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_original_password);
		Util.addActivity(OriginalPwdActivity.this);
		ViewUtils.inject(this);
		init();
		AppUtils.setActivity(this);
		AppUtils.setContext(getApplicationContext());
	}
	
	//初始化方法
	private void init() {
		//头部标题
		LinearLayout ivTurn = (LinearLayout) findViewById(R.id.layout_turn_in);
		ivTurn.setVisibility(View.VISIBLE);
		TextView tvContent = (TextView) findViewById(R.id.tv_mid_content);
		tvContent.setText(getResources().getString(R.string.validate_originalpwd));
		mEtOriginPwd = (EditText) findViewById(R.id.et_original_pwd);
		Button btnOk = (Button) findViewById(R.id.btn_ok);//确定
		TextView tvForgetPwd = (TextView) findViewById(R.id.tv_forget_pwd);//忘记密码
		TextView tvShowPassword = (TextView) findViewById(R.id.ck_original_showpassword);
		//点击事件
		tvForgetPwd.setOnClickListener(pwdListener);
		tvShowPassword.setOnClickListener(pwdListener);
		btnOk.setOnClickListener(confirmListener);
	}
	
	/**
	 * 确认按钮的点击事件
	 */
	OnClickListener confirmListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			//友盟统计
			MobclickAgent.onEvent(OriginalPwdActivity.this, "original_ok");
			String originPwd = mEtOriginPwd.getText().toString();
			boolean updatePwd = SetPwdUtils.pwdform(OriginalPwdActivity.this, originPwd);
			mDoriginalPwd = Util.md5(originPwd);
			switch (v.getId()) {
			case R.id.btn_ok:
				//原密码输入不为空
				if (Util.isEmpty(originPwd)) {
					Util.getContentValidate(R.string.input_originalpwd);
					break;
				}
				//密码长度格式
				if (updatePwd == false) {
					Util.getContentValidate(R.string.toast_register_between);
					mEtOriginPwd.findFocus();
					break;
				}
				getvalPwd(mDoriginalPwd);
			}
		}
	};
	
	/**
	 * 获得商家原密码
	 */
	public void getvalPwd(String pwd){
		new getValPwdTask(OriginalPwdActivity.this, new getValPwdTask.Callback() {
			@Override
			public void getResult(int code) {
				Log.d(TAG, "code >>>>>>>>>>>>" + code);
				if (Util.NUM_ONE == code) {
					Intent intent = new Intent(OriginalPwdActivity.this, UpdatePwdActivity.class);
					intent.putExtra(UpdatePwdActivity.ORIGINAL_PWD, mDoriginalPwd);
					startActivity(intent);
					finish();
				} else {
					Util.getContentValidate(R.string.input_originalpwd_error);
				}
				
			}
		}).execute(pwd);
	}
	
	/**
	 * 点击事件
	 */
	OnClickListener pwdListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			//显示或隐藏数字密码
			case R.id.ck_original_showpassword:
				if (!mShowPassword) {
                    //设置EditText文本为可见的
					mEtOriginPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    //设置EditText文本为隐藏的
                	mEtOriginPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
				mShowPassword = !mShowPassword;
				mEtOriginPwd.postInvalidate();
				//切换后将EditText光标置于末尾
                CharSequence charSequence = mEtOriginPwd.getText();
                if (charSequence instanceof Spannable) {
                    Spannable spanText = (Spannable) charSequence;
                    Selection.setSelection(spanText, charSequence.length());
                }
				break;
				//点击忘记密码
			case R.id.tv_forget_pwd:
				//友盟统计
				MobclickAgent.onEvent(OriginalPwdActivity.this, "original_forget");
				Intent intent = new Intent(OriginalPwdActivity.this, ForgetPwdActivity.class);
				startActivity(intent);
				break;
			default:
				break;
			}
		}
	};
	
	/**
	 * 初始化数据
	 */
	public void initData(){
		
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
    	MobclickAgent.onPageStart("MainScreen"); //统计页面
        AppUtils.setActivity(this);
        AppUtils.setContext(getApplicationContext());
    }
	
	/**
	 * 友盟统计
	 */
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("MainScreen"); 
	}
}
