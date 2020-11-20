package com.huift.hfq.shop.activity;

import net.minidev.json.JSONObject;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.huift.hfq.shop.R;

import com.huift.hfq.base.ErrorCode;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.base.utils.SetPwdUtils;
import com.huift.hfq.shop.model.UpdatePwdTask;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 修改原密码
 * @author qian.zhou
 */
public class UpdatePwdActivity extends Activity {
	public static final String TAG = "UpdatePwdActivity";
	public  static final String ORIGINAL_PWD = "originalPwd";
	/** 加密后的MD5的密码*/
	private String mDnewPwd;
	/** 加密后的原密码*/
	private String mDoriginalPwd;
	private Button mBtnOk;
	/**显示密码的标示**/
	private Boolean mShowPassword = false;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_upp_pwd);
		Util.addActivity(this);
		ViewUtils.inject(this);
		AppUtils.setActivity(this);
		AppUtils.setContext(getApplicationContext());
		init();
	}
	
	//初始化方法
	private void init() {
		Intent intent = UpdatePwdActivity.this.getIntent();
		//头部标题
		LinearLayout ivTurn = (LinearLayout) findViewById(R.id.layout_turn_in);
		ivTurn.setVisibility(View.VISIBLE);
		TextView tvContent = (TextView) findViewById(R.id.tv_mid_content);
		tvContent.setText(getResources().getString(R.string.title_update_pwd));
		
		mDoriginalPwd = intent.getStringExtra(ORIGINAL_PWD);
		//新密码
		final EditText etNewPwd = (EditText) findViewById(R.id.et_new_pwd);
		CheckBox ckShowPassword = (CheckBox) findViewById(R.id.ck_showpassword);
		ckShowPassword.setVisibility(View.VISIBLE);
		ckShowPassword.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!mShowPassword) {
                    //设置EditText文本为可见的
					etNewPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    //设置EditText文本为隐藏的
                	etNewPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
				mShowPassword = !mShowPassword;
				etNewPwd.postInvalidate();
				//切换后将EditText光标置于末尾
                CharSequence charSequence = etNewPwd.getText();
                if (charSequence instanceof Spannable) {
                    Spannable spanText = (Spannable) charSequence;
                    Selection.setSelection(spanText, charSequence.length());
                }
			}
		});
		mBtnOk = (Button) findViewById(R.id.btn_ok);//确定
		mBtnOk.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String newPwd = etNewPwd.getText().toString();
				boolean updatePwd = SetPwdUtils.pwdform(UpdatePwdActivity.this, newPwd);
				mDnewPwd = Util.md5(newPwd);
				switch (v.getId()) {
				case R.id.btn_ok:
					//新密码输入不为空
					if (Util.isEmpty(newPwd)) {
						Util.getContentValidate(R.string.input_newpwd);
						break;
					}
					//密码长度格式
					if (updatePwd == false) {
						Util.getContentValidate(R.string.toast_register_between);
						etNewPwd.findFocus();
						break;
					}
					if (!Util.isEmpty(mDoriginalPwd)) {
						if (mDoriginalPwd.equals(mDnewPwd)) {
							Util.getContentValidate(R.string.pwd_fit);
							break;
						}
					} 
				default:
					mBtnOk.setEnabled(false);
					updatePwd();
			    }
			}
		});
	}
	
	// 修改密码
	public void updatePwd() {
		String theirtype = String.valueOf(Util.NUM_ZERO);
		mBtnOk.setEnabled(false);
		new UpdatePwdTask(this, new UpdatePwdTask.Callback() {
			@Override
			public void getResult(JSONObject object) {
				mBtnOk.setEnabled(true);
				if (String.valueOf(ErrorCode.SUCC).equals(object.get("code").toString())) {
					Util.getContentValidate(R.string.update_succ);
					Intent intent = new Intent(UpdatePwdActivity.this, LoginActivity.class);
				    startActivity(intent);
				} else {
					Util.getContentValidate(R.string.update_fail);
				}
			}
		}).execute(mDoriginalPwd, mDnewPwd, theirtype);
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
