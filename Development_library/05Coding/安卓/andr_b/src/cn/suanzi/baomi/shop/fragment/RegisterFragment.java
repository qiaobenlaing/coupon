// ---------------------------------------------------------
// @author    
// @version   1.0.0
// @createTime 2015.5.4
// @copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
// ---------------------------------------------------------
package cn.suanzi.baomi.shop.fragment;

import net.minidev.json.JSONObject;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import cn.suanzi.baomi.base.ErrorCode;
import cn.suanzi.baomi.base.TimeCountUtil;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.utils.AppUtils;
import cn.suanzi.baomi.shop.R;
import cn.suanzi.baomi.shop.model.RegisterIdenCodeTask;
import cn.suanzi.baomi.shop.model.RegisterTask;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 注册
 * @author wensi.yu
 * 
 */
public class RegisterFragment extends Fragment {
	
	private final static String TAG = "RegisterFragment";
	
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
	@ViewInject(R.id.edt_register_phonenum)
	private EditText mEdtPhonenum;
	/** 验证码输入框*/
	@ViewInject(R.id.edt_register_idencode)
	private EditText mEdtIdencode;
	/** 发送验证码按钮*/
	@ViewInject(R.id.btn_register_send_iden_code)
	private TextView mBtnSendIdenCode;
	/** 密码输入框*/
	@ViewInject(R.id.edt_register_pwd)
	private EditText mEdtPwd;
	/** 确认按钮*/
	@ViewInject(R.id.btn_register_ok)
	private Button mBtnConfirm;
	/** 显示密码的标示*/
	private Boolean mShowPassword = false;
	/** 获取验证码*/
	private String mIdenCode;
	
	public static RegisterFragment newInstance() { 
		Bundle args = new Bundle();
		RegisterFragment fragment = new RegisterFragment();
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_register, container,false);
		ViewUtils.inject(this,view);
		init(view);
		return view;
	}
	
	private Activity getMyActivity(){
		Activity act = getActivity();
		if (act == null){
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
		mTvdesc.setText(R.string.func_desc_register);
		mIvBackup.setVisibility(View.VISIBLE);
		//去空格
		Util.inputFilterSpace(mEdtPhonenum);
		
		TextView tvShowPassword = (TextView) view.findViewById(R.id.tv_register_showpassword);
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
	 * 验证码按钮
	 * @param view
	 */
	@OnClick(R.id.btn_register_send_iden_code)
	public void idenCodeClick(View view){
		String registerPhonenum = (mEdtPhonenum.getText().toString());//手机号码
		switch (view.getId()) {
		case R.id.btn_register_send_iden_code:
			if (Util.isEmpty(registerPhonenum)){
				getDateInfo(R.string.toast_register_phone);
				break;
			}
			//手机号码格式不正确
			if (Util.isPhone(getMyActivity(), registerPhonenum)) {
				break;
			}
			if (registerPhonenum.length() != PHONE_NUMBER) {
				getDateInfo(R.string.toast_register_format);
				break;
			}
			// 获取验证码	
			mBtnSendIdenCode.setEnabled(false);
			new RegisterIdenCodeTask(getMyActivity(), new RegisterIdenCodeTask.Callback() {
				@Override
				public void getResult(JSONObject mResult) {
					mBtnSendIdenCode.setEnabled(true);
					if (null == mResult) {
						return;
					}
					if (mResult.get("code").toString().equals(String.valueOf(ErrorCode.SUCC))) {
						TimeCountUtil timeCountUtil = new TimeCountUtil(getActivity(),60000, 1000,mBtnSendIdenCode);
						timeCountUtil.start();
						mIdenCode = mResult.get("valCode").toString();
						Log.d(TAG, "接受发送的验证码=============="+mIdenCode);
					}
				}
			}).execute(registerPhonenum);
			
			break;
		default:
			break;
		}
	}
	
	/** 
	 * 点击确定
	 * @param view
	 */
	@OnClick(R.id.btn_register_ok)
	public void turnRegisterConfirmClick(View view){
		String registerPhonenum = mEdtPhonenum.getText().toString();//手机号码
		String registerIdencode = mEdtIdencode.getText().toString();//验证码
		String registerPwd = Util.md5(mEdtPwd.getText().toString());//密码
		
		//保存注册的用户名和密码
		SharedPreferences mSharedPreferences = getActivity().getSharedPreferences("register", Context.MODE_PRIVATE);
	    Editor editor = mSharedPreferences.edit(); 
	    editor.putString("registerPhonenum", registerPhonenum);
	    editor.putString("registerPwd", registerPwd);
	    editor.commit();
		
		switch (view.getId()) {
		case R.id.btn_register_ok://点击ok
			//请输入手机号码
			if (Util.isEmpty(registerPhonenum)) {
				Util.getContentValidate(R.string.toast_register_phone);
				getDateInfo(R.string.toast_register_phone);
				break;
			}
			//手机号码格式不正确
			if (registerPhonenum.length() != PHONE_NUMBER) {
				getDateInfo(R.string.toast_register_format);
				break;
			}
			//手机号码格式不正确
			if (Util.isPhone(getMyActivity(), registerPhonenum)) {
				break;
			}
			//请输入验证码
			if (Util.isEmpty(registerIdencode)) {
				getDateInfo(R.string.toast_register_indencode);
				break;
			}
			//验证码不正确
			if (!Util.isEmpty(mIdenCode)) {
				Log.d(TAG, "接受发送的验证码aa=============="+mIdenCode);
				if(!mIdenCode.equals(registerIdencode)){
					getDateInfo(R.string.toast_register_incode);
					mEdtIdencode.findFocus();
					break;
				}
			}
			else {
				getDateInfo(R.string.toast_register_incode);
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
			//账号激活
			mBtnConfirm.setEnabled(false);
			new RegisterTask(getActivity(), new RegisterTask.Callback() {
				@Override
				public void getResult(JSONObject result) {
					mBtnConfirm.setEnabled(true);
					if (null == result) {
						return;
					}
				}
			}).execute(registerPhonenum,registerIdencode,registerPwd);
			
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
	public void trunIdenCode(View view){
		getMyActivity().finish();
	}
}
