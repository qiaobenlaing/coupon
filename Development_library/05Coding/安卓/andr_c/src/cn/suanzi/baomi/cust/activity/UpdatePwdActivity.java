package cn.suanzi.baomi.cust.activity;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import cn.suanzi.baomi.base.Const;
import cn.suanzi.baomi.base.ErrorCode;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.data.DB;
import cn.suanzi.baomi.base.model.LoginTask;
import cn.suanzi.baomi.base.pojo.UserToken;
import cn.suanzi.baomi.base.utils.AppUtils;
import cn.suanzi.baomi.base.utils.SetPwdUtils;
import cn.suanzi.baomi.cust.R;
import cn.suanzi.baomi.cust.model.UpdatePwdTask;

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
	/** 用户手机号码*/
	private String mUserPhone;
	private Button mBtnOk;
	/**显示密码的标示**/
	private Boolean mShowPassword = false;
	/** 新密码输入框*/
	private EditText mEtNewPwd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_upp_pwd);
		Util.addHomeActivity(this);
		ViewUtils.inject(this);
		init();
		AppUtils.setActivity(this);
		AppUtils.setContext(getApplicationContext());
	}
	
	//初始化方法
	private void init() {
		// 获得一个用户信息对象
		UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		mUserPhone = userToken.getMobileNbr();//登陆用户手机号码
		mDoriginalPwd = userToken.getPassword();
		//头部标题
		ImageView ivTurn = (ImageView) findViewById(R.id.iv_turn_in);
		ivTurn.setVisibility(View.VISIBLE);
		TextView tvContent = (TextView) findViewById(R.id.tv_mid_content);
		tvContent.setText(getResources().getString(R.string.title_update_pwd));
		//新密码
		mEtNewPwd = (EditText) findViewById(R.id.et_new_pwd);
		TextView tvShowPassword = (TextView) findViewById(R.id.tv_showpassword);
		mBtnOk = (Button) findViewById(R.id.btn_ok);//确定
		tvShowPassword.setOnClickListener(showPassListener);
		mBtnOk.setOnClickListener(okListener);
	}
	
	/**
	 * 确定按钮的点击事件
	 */
	OnClickListener okListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			String newPwd = mEtNewPwd.getText().toString();
			boolean updatePwd = SetPwdUtils.pwdform(UpdatePwdActivity.this, newPwd);
			mDnewPwd = Util.md5(newPwd);
			switch (v.getId()) {
			case R.id.btn_ok:
				//新密码输入不为空
				if(Util.isEmpty(newPwd)){
					Util.getContentValidate(R.string.input_newpwd);
					break;
				}
				//密码长度格式
				if(updatePwd == false){
					Util.getContentValidate(R.string.toast_register_between);
					mEtNewPwd.findFocus();
					break;
				}
				if(mDoriginalPwd.equals(mDnewPwd)){
					Util.getContentValidate(R.string.pwd_fit);
					break;
				}
			default:
				mBtnOk.setEnabled(false);
				updatePwd();
		    }
		}
	};
	
	/**
	 * 查看数字密码是否显示与隐藏
	 */
	OnClickListener showPassListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (!mShowPassword) {
                //设置EditText文本为可见的
				mEtNewPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                //设置EditText文本为隐藏的
            	mEtNewPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
			mShowPassword = !mShowPassword;
			mEtNewPwd.postInvalidate();
			//切换后将EditText光标置于末尾
            CharSequence charSequence = mEtNewPwd.getText();
            if (charSequence instanceof Spannable) {
                Spannable spanText = (Spannable) charSequence;
                Selection.setSelection(spanText, charSequence.length());
            }
		}
	};
	
	// 修改密码
	public void updatePwd() {
		String theirtype = String.valueOf(Util.NUM_ONE);
		mBtnOk.setEnabled(false);
		new UpdatePwdTask(this, new UpdatePwdTask.Callback() {
			@Override
			public void getResult(JSONObject object) {
				mBtnOk.setEnabled(true);
				if (String.valueOf(ErrorCode.SUCC).equals(object.get("code").toString())) {
					Util.getContentValidate(R.string.upp_succ);
					Intent intent = new Intent(UpdatePwdActivity.this, LoginActivity.class);
					intent.putExtra(LoginTask.ALL_LOGIN, Const.Login.EXIT_LOGIN);
					startActivity(intent);
					finish();
				} else {
					Util.getContentValidate(R.string.upp_fail);
				}
			}
		}).execute(mUserPhone, mDoriginalPwd, mDnewPwd, theirtype);
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
