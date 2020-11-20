// ---------------------------------------------------------
// @author    Wei.fang
// @version   1.0.0
// @createTime 2015.5.4
// @copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
// ---------------------------------------------------------
package com.huift.hfq.cust.fragment;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import com.huift.hfq.base.Const;
import com.huift.hfq.base.ErrorCode;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.model.LoginTask;
import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.cust.R;

import com.huift.hfq.cust.activity.ActThemeDetailActivity;
import com.huift.hfq.cust.activity.H5ActActivity;
import com.huift.hfq.cust.activity.H5ShopDetailActivity;
import com.huift.hfq.cust.activity.HomeActivity;
import com.huift.hfq.cust.activity.RegisterActivity;
import com.huift.hfq.cust.activity.ResetPwdActivity;
import com.huift.hfq.cust.application.CustConst;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.umeng.analytics.MobclickAgent;

/**
 * 登录
 * @author wensi.yu
 * 
 */
public class LoginFragment extends Fragment {

	private final static String TAG = "LoginFragment";

	/** 用户名编辑框 */
	@ViewInject(R.id.edt_login_username)
	private EditText mEdtUsername;
	/** 密码编辑框 */
	@ViewInject(R.id.edt_login_password)
	private EditText mEdtPassword;
	/** 忘记密码文本框（用作按钮） */
	@ViewInject(R.id.tv_forget_pwd)
	private TextView mTvForgetPwd;
	/** 登录按钮 */
	@ViewInject(R.id.btn_login)
	private Button mBtnLogin;
	/** 注册按钮 */
	@ViewInject(R.id.btn_register)
	private TextView mBtnRegest;
	/** 显示密码的标示 **/
	private Boolean mShowPassword = false;
	/** 取没有登录的标示 */
	private String mLoginFlag;
	/** 进度条 */
	private ProgressBar mPrLogin;

	public static LoginFragment newInstance() {
		Bundle args = new Bundle();
		LoginFragment fragment = new LoginFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_login, container, false);
		ViewUtils.inject(this, v);
		Util.addLoginActivity(getMyActivity());
		Util.addHomeActivity(getMyActivity());
		// 登录的对象
		DB.saveStr(CustConst.LoginKey.LOGITN_MAIN, getClass().getSimpleName());
		DB.saveStr(CustConst.LoginKey.LOGIN, getClass().getSimpleName());
		init(v);
		return v;
	}

	private Activity getMyActivity() {
		Activity act = getActivity();
		if (act == null) {
			act = AppUtils.getActivity();
		}
		return act;
	}

	/**
	 * 设置密码显示和隐藏
	 */
	private void init(View view) {
		mPrLogin = (ProgressBar) view.findViewById(R.id.pr_login_data);
		// 添加
		Util.addActivity(getMyActivity());
		// 去空格
		Util.inputFilterSpace(mEdtUsername);
		mBtnRegest.setText(R.string.login_title);
		// 用取值是否登陆
		Intent intent = getMyActivity().getIntent();
		mLoginFlag = intent.getStringExtra(LoginTask.ALL_LOGIN);
		TextView tvShowPassword = (TextView) view.findViewById(R.id.tv_login_showpassword);

		tvShowPassword.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!mShowPassword) {
					// 设置EditText文本为可见的
					mEdtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
				} else {
					// 设置EditText文本为隐藏的
					mEdtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
				}
				mShowPassword = !mShowPassword;
				mEdtPassword.postInvalidate();
				// 切换后将EditText光标置于末尾
				CharSequence charSequence = mEdtPassword.getText(); 
				if (charSequence instanceof Spannable) {
					Spannable spanText = (Spannable) charSequence;
					Selection.setSelection(spanText, charSequence.length());
				}
			}
		});

		/** 点击密码框后面的图标隐藏软键盘 **/
		tvShowPassword.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE) {
					InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(
							Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
					// 隐藏软键盘
					return true;
				}
				return false;
			}
		});
	}

	/**
	 * 登陆相关操作
	 */
	@OnClick({R.id.btn_login,R.id.btn_register,R.id.tv_forget_pwd})
	public void loginBtnClick(View view) {
		// 友盟统计
		MobclickAgent.onEvent(getMyActivity(), "login_login");
		String mobileNbr = mEdtUsername.getText().toString();
		String password = null;
		if ("".equals(mEdtPassword.getText().toString()) && mEdtPassword.getText().toString() == null) {
			password = "";
		} else {
			password = Util.md5(mEdtPassword.getText().toString());
		}
		// 保存登录的用户名和密码
		SharedPreferences mSharedPreferences = getMyActivity().getSharedPreferences(CustConst.LoginSave.LOGIN_KEEP,Context.MODE_PRIVATE);
		final Editor editor = mSharedPreferences.edit();
		editor.putString("mobileNbr", mobileNbr);
		editor.putString("password", password);
		editor.commit();

		// 传过来的registerid
		String regId = Util.isEmpty(DB.getStr(Const.JPUSH_REGID)) ? "" : DB.getStr(Const.JPUSH_REGID);
		
		switch (view.getId()) {
		case R.id.btn_login:
			mPrLogin.setVisibility(View.VISIBLE);
			if (Util.getLoginValidate(getMyActivity(), mobileNbr)) {
				mPrLogin.setVisibility(View.GONE);
				break;
			}
			// 手机号码格式不正确
			if (Util.isPhone(getMyActivity(), mobileNbr)) {
				mPrLogin.setVisibility(View.GONE);
				break;
			}
			if (Util.isEmpty(mEdtPassword.getText().toString())) {
				mPrLogin.setVisibility(View.GONE);
				Util.getContentValidate(R.string.toast_register_pwd);
				mEdtPassword.findFocus();
				break;
			}
			// 执行登录
			final String loginStatus = getMyActivity().getIntent().getStringExtra(LoginTask.ALL_LOGIN);
			Log.d(TAG, "loginStatus:" + loginStatus);
			mBtnLogin.setEnabled(false);
			new LoginTask(getMyActivity(), new LoginTask.Callback() {
				@Override
				public void getResult(int result) {
					mPrLogin.setVisibility(View.GONE);
					mBtnLogin.setEnabled(true);
					if (null == String.valueOf(result) && result == ErrorCode.FAIL) {
						DB.saveBoolean(DB.Key.CUST_LOGIN, false);
						editor.clear();
						editor.commit();
					} else if (result == ErrorCode.SUCC) {
						DB.saveBoolean(DB.Key.CUST_LOGIN, true);
						if (null != loginStatus && loginStatus.equals(Const.Login.SHOP_DETAIL)) {
							Intent intent = new Intent(getMyActivity(), H5ShopDetailActivity.class);
							getMyActivity().setResult(H5ShopDetailActivity.LOGIN_SUCC, intent);
						} else if (null != loginStatus && loginStatus.equals(Const.Login.ACT_THEME)) {
							Intent intent = new Intent(getMyActivity(), ActThemeDetailActivity.class);
							getMyActivity().setResult(H5ActActivity.LOGIN_SUCC, intent);
						} else if (null != loginStatus && loginStatus.equals(Const.Login.H5_ACT)) {
							Intent intent = new Intent(getMyActivity(), H5ActActivity.class);
							getMyActivity().setResult(ActThemeDetailActivity.LOGIN_SUCC, intent);
						}
					}
				}
			}, HomeActivity.class, loginStatus).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,mobileNbr, password, regId);
			break;
			
		case R.id.btn_register://注册
			Intent it = new Intent(getMyActivity(), RegisterActivity.class);
			getMyActivity().startActivity(it);
			// 友盟统计
			MobclickAgent.onEvent(getMyActivity(), "login_register");
			break;
			
		case R.id.tv_forget_pwd://忘记密码
			Intent intent = new Intent(getMyActivity(), ResetPwdActivity.class);
			getMyActivity().startActivity(intent);
			break;
			
		default:
			break;
		}
	}

	/**
	 * 忘记密码的相关操作
	 */
	@OnClick(R.id.tv_forget_pwd)
	public void trunForgetPwd(View view) {
		Intent it = new Intent(getMyActivity(), ResetPwdActivity.class);
		getMyActivity().startActivity(it);
		// 友盟统计
		MobclickAgent.onEvent(getMyActivity(), "login_forget_pwd");
	}

	/**
	 * 友盟统计
	 */
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(LoginFragment.class.getSimpleName());
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(LoginFragment.class.getSimpleName()); // 统计页面
	}
}
