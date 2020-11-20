// ---------------------------------------------------------
// @author    Wei.fang
// @version   1.0.0
// @copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
// ---------------------------------------------------------
package com.huift.hfq.cust.fragment;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minidev.json.JSONObject;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import com.huift.hfq.base.ErrorCode;
import com.huift.hfq.base.TimeCountUtil;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.cust.R;

import com.huift.hfq.cust.model.ResetIdenCodeTask;
import com.huift.hfq.cust.model.ResetTask;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.umeng.analytics.MobclickAgent;

/**
 * 忘记密码
 * @author wensi.yu
 *
 */
public class ResetPwdFragment extends Fragment {
	
	private final static String TAG = ResetPwdFragment.class.getSimpleName();
	
	/** 手机号码长度*/
	private static final int PHONE_NUMBER = 11;
	/** 密码长度*/
	private static final int PWD_MINNUMBER = 6;
	private static final int PWD_MAXNUMBER = 20;
	/** 返回图片*/
	@ViewInject(R.id.iv_turn_in)
	private ImageView mIvBackup;
	/** 功能描述文本*/
	@ViewInject(R.id.tv_mid_content)
	private TextView mTvdesc;
	/** 手机号输入框*/
	@ViewInject(R.id.edt_reset_phonenum)
	private EditText mEdtPhonenum;
	/** 验证码输入框*/
	@ViewInject(R.id.edt_reset_iden_code)
	private EditText mEdtIdencode;
	/** 发送验证码按钮*/
	@ViewInject(R.id.btn_reset_send_iden_code)
	private TextView mBtnSendIdenCode;
	/** 密码输入框*/
	@ViewInject(R.id.edt_reset_pwd)
	private EditText mEdtPwd;
	/** 商圈协议按钮*/
	@ViewInject(R.id.tv_terms)
	private TextView mTvProtocol;
	/** 确认按钮*/
	@ViewInject(R.id.btn_register_confirm)
	private Button mBtnConfirm;
	/** 显示密码的标示 **/
	private Boolean mShowPassword = false;
	/** 显示验证码**/
	private String mIdenCode;
	
	public static ResetPwdFragment newInstance() { 
		Bundle args = new Bundle();
		ResetPwdFragment fragment = new ResetPwdFragment();
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(com.huift.hfq.base.R.layout.fragment_resetpwd, container, false);
		ViewUtils.inject(this, view);
		Util.addLoginActivity(getMyActivity());
		init(view);
		return view;
	}  
	
	/**
	 * 保证activity不为空
	 * @return activity
	 */
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
		//添加
		Util.addActivity(getMyActivity());
		//设置标题
		ImageView msg = (ImageView) view.findViewById(R.id.iv_add);
        msg.setVisibility(View.GONE);
		mTvdesc.setText(R.string.func_desc_reset);
		mIvBackup.setVisibility(View.VISIBLE);
		//去空格
		Util.inputFilterSpace(mEdtPhonenum);
		
		TextView tvShowPassword = (TextView) view.findViewById(R.id.tv_reset_showpassword);
		
		tvShowPassword.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!mShowPassword) {
					// 设置EditText文本为可见的
					mEdtPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
				} else {
					// 设置EditText文本为隐藏的
					mEdtPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
				}
				mShowPassword = !mShowPassword;
				mEdtPwd.postInvalidate();
				// 切换后将EditText光标置于末尾
				CharSequence charSequence = mEdtPwd.getText();
				if (charSequence instanceof Spannable) {
					Spannable spanText = (Spannable) charSequence;
					Selection.setSelection(spanText, charSequence.length());
				}
			}
		});

		/** 点击密码框后面的图标隐藏软键盘 **/
		tvShowPassword.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE) {
					InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
					// 隐藏软键盘
					return true;
				}
				return false;
			}
		});
	}

	/**
	 * 验证码
	 * @param view
	 */
	@OnClick(R.id.btn_reset_send_iden_code)
	public void resetIdenCode(View view){
		String resetPhonenum = (mEdtPhonenum.getText().toString());//手机号码
		switch (view.getId()) {
		case R.id.btn_reset_send_iden_code:
			if (Util.isEmpty(resetPhonenum)) {
				getDateInfo(R.string.toast_register_phone);
				break;
			}
			//手机号码格式不正确
			if (Util.isPhone(getMyActivity(), resetPhonenum)) {
				break;
			}
			
			//手机号码格式不正确
			if (resetPhonenum.length() != PHONE_NUMBER) {
				getDateInfo(R.string.toast_register_format);
				break;
			}
			//获取验证码
			mBtnSendIdenCode.setEnabled(false);
			new ResetIdenCodeTask(getMyActivity(), new ResetIdenCodeTask.Callback() {
				@Override
				public void getResult(JSONObject mResult) {
					mBtnSendIdenCode.setEnabled(true);
					if (null == mResult) {
						return;
					}
					if (mResult.get("code").toString().equals(String.valueOf(ErrorCode.SUCC))) {
						//发送验证码
						TimeCountUtil timeCountUtil = new TimeCountUtil(getMyActivity(), 60000, 1000,mBtnSendIdenCode);
						timeCountUtil.start();
						mBtnSendIdenCode.setEnabled(true);
						mIdenCode = mResult.get("valCode").toString();
						Log.d(TAG, "接受发送的验证码=============="+mIdenCode);
					}
					else {
						mBtnSendIdenCode.setEnabled(true);
					}
				}
			}).execute(resetPhonenum);
			
			break;
		default:
			break;
		}
	}
	
	/**
	 * 密码确定按钮
	 * @param view
	 */
	@OnClick(R.id.btn_register_confirm)
	public void turnResetConfirmClick(View view){
		String resetPhonenum = (mEdtPhonenum.getText().toString());//手机号码
		String resetIdencode = mEdtIdencode.getText().toString();//验证码
		String resetPwd = Util.md5(mEdtPwd.getText().toString());//密码
		
		switch (view.getId()) {
		case R.id.btn_register_confirm:
			//请输入手机号码
			if (Util.isEmpty(resetPhonenum)) {
				getDateInfo(R.string.toast_register_phone);
				break;
			}
			//手机号码格式不正确
			if (resetPhonenum.length() != PHONE_NUMBER) {
				getDateInfo(R.string.toast_register_format);
				break;
			}
			//手机号码格式不正确
			if (Util.isPhone(getMyActivity(), resetPhonenum)) {
				break;
			}
			//请输入验证码
			if (Util.isEmpty(resetIdencode)) {
				getDateInfo(R.string.toast_register_indencode);
				break;
			}
			//验证码不正确
			if (!Util.isEmpty(mIdenCode)) {
				Log.d(TAG, "接受发送的验证码aa=============="+mIdenCode);
				if(!mIdenCode.equals(resetIdencode)){
					getDateInfo(R.string.toast_register_indencodeerror);
					break;
				}
			}
			else {
				getDateInfo(R.string.toast_register_indencodeerror);
				break;
			}
			//请输入密码
			if (Util.isEmpty(mEdtPwd.getText().toString())) {
				getDateInfo(R.string.toast_register_pwd);
				break;
			}
			//密码需由为6-20的字符组成
			if (mEdtPwd.getText().length() < PWD_MINNUMBER || mEdtPwd.getText().length() > PWD_MAXNUMBER) {
				getDateInfo(R.string.toast_register_between);
				break;
			}
			//密码只允许字母和数字 (TODO)
			Pattern pattern = Pattern.compile("^[A-Za-z0-9]+$"); 
			Matcher matcherPwd = pattern.matcher(resetPwd); 
			if (!matcherPwd.matches()) {
				getDateInfo(R.string.toast_register_password);
				break;
			}
			//忘记密码
			mBtnConfirm.setEnabled(false);
			new ResetTask(getMyActivity(), new ResetTask.Callback() {
				@Override
				public void getResult(JSONObject result) {
					mBtnConfirm.setEnabled(true);
					if (null == result) {
						return;
					}
				}
			}).execute(resetPhonenum,resetIdencode,resetPwd);
			break;        
		default:
			break;
		}
	}
	
	/**
	 * 提示的消息
	 */
	private void getDateInfo(int StrignId) {
		Util.getContentValidate(StrignId);
	}
	
	/**
	 * 返回
	 * @param view
	 */
	@OnClick(R.id.iv_turn_in)
	public void backUp(View view){
		getMyActivity().finish();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(ResetPwdFragment.class.getSimpleName()); 
	}
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(ResetPwdFragment.class.getSimpleName()); //统计页面
	}
}
