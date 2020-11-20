package cn.suanzi.baomi.cust.activity;

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
import android.widget.ImageView;
import android.widget.TextView;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.data.DB;
import cn.suanzi.baomi.base.pojo.UserToken;
import cn.suanzi.baomi.base.utils.AppUtils;
import cn.suanzi.baomi.base.utils.SetPwdUtils;
import cn.suanzi.baomi.cust.R;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

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
	/** 登陆人的密码*/
	private String mloginPwd;
	/** 原密码输入框*/
	private EditText mEtOriginPwd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_original_password);
		Util.addActivity(OriginalPwdActivity.this);
		ViewUtils.inject(this);
		Util.addHomeActivity(this);
		/*AppUtils.setActivity(this);
		AppUtils.setContext(getApplicationContext());*/
		init();
	}
	
	//初始化方法
	private void init() {
		UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		//头部标题
		ImageView ivTurn = (ImageView) findViewById(R.id.iv_turn_in);
		ivTurn.setVisibility(View.VISIBLE);
		TextView tvContent = (TextView) findViewById(R.id.tv_mid_content);
		tvContent.setText(getResources().getString(R.string.validate_originalpwd));
		ImageView ivAdd = (ImageView) findViewById(R.id.iv_add);
		ivAdd.setVisibility(View.GONE);
		if (!Util.isEmpty(userToken.getPassword())) {
			//获取登陆人的原密码
			mloginPwd = userToken.getPassword();
			mEtOriginPwd = (EditText) findViewById(R.id.et_original_pwd);
			Button btnOk = (Button) findViewById(R.id.btn_ok);//确定
			TextView tvForgetPwd = (TextView) findViewById(R.id.tv_forget_pwd);//忘记密码
			TextView tvShowPassword = (TextView) findViewById(R.id.tv_original_showpassword);
			tvShowPassword.setOnClickListener(showListener);
			tvForgetPwd.setOnClickListener(showListener);
			btnOk.setOnClickListener(okListener);
		}
	}
	
	/**
	 * 确定按钮的点击事件
	 */
	OnClickListener okListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			String originPwd = mEtOriginPwd.getText().toString();
			boolean updatePwd = SetPwdUtils.pwdform(OriginalPwdActivity.this, originPwd);
			mDoriginalPwd = Util.md5(originPwd);
			Log.d(TAG, "加密前的密码为：：" + originPwd);
			Log.d(TAG, "输入的密码的格式为：：" + mDoriginalPwd);
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
				if (mDoriginalPwd.equals(mloginPwd)) {//密码输入正确
					Intent intent = new Intent(OriginalPwdActivity.this, UpdatePwdActivity.class);
					intent.putExtra(UpdatePwdActivity.ORIGINAL_PWD, mDoriginalPwd);
					startActivity(intent);
					finish();
					break;
				} else {
					Util.getContentValidate(R.string.input_originalpwd_error);
				}
			}
		}
	};
	
	/**
	 * 显示或隐藏密码 忘记密码的点击事件
	 */
	OnClickListener showListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.tv_original_showpassword:
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
			case R.id.tv_forget_pwd:
				Intent intent = new Intent(OriginalPwdActivity.this, ForgetPwdActivity.class);
				startActivity(intent);
				break;
			default:
				break;
			}
		}
	};
	
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
