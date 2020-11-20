// ---------------------------------------------------------
// @author    
// @version   1.0.0
// @copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
// ---------------------------------------------------------
package com.huift.hfq.shop.fragment;

import net.minidev.json.JSONObject;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import com.huift.hfq.shop.R;

import com.huift.hfq.base.ErrorCode;
import com.huift.hfq.base.TimeCountUtil;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.shop.model.ResetIdenCodeTask;
import com.huift.hfq.shop.model.ResetTask;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * @author wensi.yu
 *	忘记密码
 */
public class ResetPwdFragment extends Fragment {
	
	private final static String TAG = "ResetPwdFragment";
	
	/** 手机号码长度*/
	private static final int PHONE_NUMBER = 11;
	/** 密码长度*/
	private static final int PWD_MINNUMBER = 6;
	private static final int PWD_MAXNUMBER = 20;
	/** 返回图片*/
	@ViewInject(R.id.layout_turn_in)
	private LinearLayout mIvBackup;
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
	/** 确认按钮*/
	@ViewInject(R.id.btn_register_confirm)
	private Button mBtnConfirm;
	/** 显示密码的标示**/
	private Boolean mShowPassword = false;
	/** 获取验证码**/
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
		init(view);
		return view;
	}
	
	private Activity getMyActivity(){
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
		//保存
		Util.addActivity(getMyActivity());
		Util.addLoginActivity(getMyActivity());
		//设置标题
		TextView msg = (TextView) view.findViewById(R.id.tv_msg);
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
                    //设置EditText文本为可见的
					mEdtPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    //设置EditText文本为隐藏的
                	mEdtPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
				mShowPassword = !mShowPassword;
				mEdtPwd.postInvalidate();
				//切换后将EditText光标置于末尾
                CharSequence charSequence = mEdtPwd.getText();
                if (charSequence instanceof Spannable) {
                    Spannable spanText = (Spannable) charSequence;
                    Selection.setSelection(spanText, charSequence.length());
                }
			}
		});
		
		/**点击密码框后面的图标隐藏软键盘**/
		tvShowPassword.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				 if (actionId == EditorInfo.IME_ACTION_DONE) { 
		               InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE); 
		               imm.hideSoftInputFromWindow(v.getWindowToken(), 0); 
		               //隐藏软键盘
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
			if (Util.isPhone(getActivity(), resetPhonenum)) {
				break;
			}
			
			if (resetPhonenum.length() != PHONE_NUMBER) {
				getDateInfo(R.string.toast_register_format);
				break;
			}
			
			//获取验证码
			mBtnSendIdenCode.setEnabled(false);
			new ResetIdenCodeTask(getMyActivity(), new ResetIdenCodeTask.Callback() {
				@Override
				public void getResult(JSONObject result) {
					mBtnSendIdenCode.setEnabled(true);
					if (null == result) {
						return;
					}
					
					if (result.get("code").toString().equals(String.valueOf(ErrorCode.SUCC))) {
						//发送验证码
						TimeCountUtil timeCountUtil = new TimeCountUtil(getActivity(), 60000, 1000,mBtnSendIdenCode);
						timeCountUtil.start();
						mIdenCode = result.get("valCode").toString();
						Log.d(TAG, "接受发送的验证码=============="+mIdenCode);
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
			if (Util.isEmpty(resetPhonenum)) {
				getDateInfo(R.string.toast_register_phone);
				mEdtPhonenum.setInputType(InputType.TYPE_CLASS_PHONE);//只能输入电话
				break;
			}
			if (resetPhonenum.length() != PHONE_NUMBER) {
				getDateInfo(R.string.toast_register_format);
				break;
			}
			//手机号码格式不正确
			if (Util.isPhone(getActivity(), resetPhonenum)) {
				break;
			}
			if (Util.isEmpty(resetIdencode)) {
				getDateInfo(R.string.toast_register_indencode);
				break;
			}
			
			//验证码不正确
			if (!Util.isEmpty(mIdenCode)) {
				Log.d(TAG, "接受发送的验证码aa=============="+mIdenCode);
				if (!mIdenCode.equals(resetIdencode)){
					getDateInfo(R.string.toast_register_incode);
					break;
				}
			}
			else {
				getDateInfo(R.string.toast_register_incode);
				break;
			}
			
			if (Util.isEmpty(mEdtPwd.getText().toString())) {
				getDateInfo(R.string.toast_register_pwd);
				break;
			}
			if (mEdtPwd.getText().length() < PWD_MINNUMBER || mEdtPwd.getText().length() > PWD_MAXNUMBER) {
				getDateInfo(R.string.toast_register_between);
				break;
			}
			//忘记密码
			mBtnConfirm.setEnabled(false);
			new ResetTask(getActivity(), new ResetTask.Callback() {
				@Override
				public void getResult(JSONObject result) {
					if (null == result) {
						return;
					}
					
					if (result.get("code").toString().equals(String.valueOf(ErrorCode.SUCC))) {
						mBtnConfirm.setEnabled(true);
						
					}
					else {
						mBtnConfirm.setEnabled(true);
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
	@OnClick(R.id.layout_turn_in)
	public void backUp(View view){
		getMyActivity().finish();
	}
}
