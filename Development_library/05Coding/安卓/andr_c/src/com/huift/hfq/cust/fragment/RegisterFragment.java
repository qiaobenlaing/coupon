// ---------------------------------------------------------
// @author    Wei.fang
// @version   1.0.0
// @createTime 2015.5.4
// @copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
// ---------------------------------------------------------
package com.huift.hfq.cust.fragment;

import net.minidev.json.JSONObject;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import com.huift.hfq.base.ErrorCode;
import com.huift.hfq.base.TimeCountUtil;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.base.utils.PhoneInfoUtils;
import com.huift.hfq.cust.R;

import com.huift.hfq.cust.activity.ActThemeDetailActivity;
import com.huift.hfq.cust.application.CustConst;
import com.huift.hfq.cust.model.RegisterIdenCodeTask;
import com.huift.hfq.cust.model.RegisterTask;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.umeng.analytics.MobclickAgent;

/**
 * 注册
 * @author wensi.yu
 */
public class RegisterFragment extends Fragment {
	
	private final static String TAG = RegisterFragment.class.getSimpleName();
	
	/** 手机号码长度*/
	private static final int PHONE_NUMBER = 11;
	/** 密码长度*/
	private static final int PWD_MINNUMBER = 6;
	private static final int PWD_MAXNUMBER = 20;
	/** 返回图片*/
	@ViewInject(R.id.iv_turn_in)
	private ImageView mIvBackup;
	/** 功能描述文本 */
	@ViewInject(R.id.tv_mid_content)
	private TextView mTvdesc;
	/** 手机号输入框 */
	@ViewInject(R.id.edt_register_phonenum)
	private EditText mEdtPhonenum;
	/** 验证码输入框 */
	@ViewInject(R.id.edt_register_idencode)
	private EditText mEdtIdencode;
	/** 发送验证码按钮 */
	@ViewInject(R.id.btn_register_send_iden_code)
	private TextView mBtnSendIdenCode;
	/** 密码输入框 */
	@ViewInject(R.id.edt_register_pwd)
	private EditText mEdtPwd;
	/** 推荐码输入框 */
	@ViewInject(R.id.edt_business_num)
	private EditText mEdtRecommendedCode;
	/** 遵守协议复选框 */
	@ViewInject(R.id.chb_obey_protocol)
	private CheckBox mChkObeyProtocol;
	/** 商圈协议按钮 */
	@ViewInject(R.id.tv_terms)
	private TextView mTvProtocol;
	/** 确认按钮 */
	@ViewInject(R.id.btn_register_confirm)
	private Button mBtnConfirm;
	/** 获取验证码**/
	private String mIdenCode = null;
	/** 显示密码的标示 **/
	private Boolean mShowPassword = false;
	/** 保存 **/
	private SharedPreferences mSharedPreferences;
	
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
		ViewUtils.inject(this, view);
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
		Util.addActivity(getMyActivity());
		Util.addLoginActivity(getMyActivity());
		//设置标题
		ImageView msg = (ImageView) view.findViewById(R.id.iv_add);
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
	 * 验证码按钮
	 * @param view
	 */
	@OnClick(R.id.btn_register_send_iden_code)
	public void idenCodeClick(View view) {
		String registerPhonenum = (mEdtPhonenum.getText().toString());// 手机号码
		String registerIdencode = mEdtIdencode.getText().toString();// 验证码
		
		switch (view.getId()) {
		case R.id.btn_register_send_iden_code:
			if (Util.isEmpty(registerPhonenum)) {
				getDateInfo(R.string.toast_register_phone);
				break;
			}
			//手机号码格式不正确
			if (Util.isPhone(getMyActivity(), registerPhonenum)) {
				break;
			}
			//手机的长度
			if (registerPhonenum.length() != 11) {
				getDateInfo(R.string.toast_register_format);
				break;
			}
			// 获取验证码
			mBtnSendIdenCode.setEnabled(false);
			new RegisterIdenCodeTask(getMyActivity(), new RegisterIdenCodeTask.Callback() {
				@Override
				public void getResult(JSONObject result) {
					mBtnSendIdenCode.setEnabled(true);
					if (null == result) {
						return;
					} 
					if (result.get("code").toString().equals(String.valueOf(ErrorCode.SUCC))) {
						mIdenCode = result.get("valCode").toString();
						Log.d(TAG, "接受发送的验证码=============="+mIdenCode);
						//发送验证码
						TimeCountUtil timeCountUtil = new TimeCountUtil(getMyActivity(), 60000, 1000,mBtnSendIdenCode);
						timeCountUtil.start();
					}
				}
			}).execute(registerPhonenum);
			
			break;
		default:
			break;
		}
	}
	
	/**
	 * 点击注册确定
	 * @param view
	 */
	@OnClick(R.id.btn_register_confirm)
	public void turnRegisterConfirmClick(View view) {
		String registerPhonenum = (mEdtPhonenum.getText().toString());// 手机号码
		String registerIdencode = mEdtIdencode.getText().toString();// 验证码
		String registerPwd = Util.md5(mEdtPwd.getText().toString());// 密码
		String recommendedCode = mEdtRecommendedCode.getText().toString();// 推荐人手机号
		//保存注册的用户名和密码
		SharedPreferences mSharedPreferences = getMyActivity().getSharedPreferences("register", Context.MODE_PRIVATE);
	    Editor editor =   mSharedPreferences.edit(); 
	    editor.putString("registerPhonenum", registerPhonenum);
	    editor.putString("registerPwd", registerPwd);
	    editor.commit();    
		
		switch (view.getId()) {
		case R.id.btn_register_confirm:
			//请输入手机号码
			if (Util.isEmpty(registerPhonenum)) {
				getDateInfo(R.string.toast_register_phone);
				break;
			}
			//用户名长度
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
			mIdenCode = "123456";// 临时的数据
			if (!Util.isEmpty(mIdenCode)) {
				Log.d(TAG, "接受发送的验证码aa=============="+mIdenCode);
				if(!mIdenCode.equals(registerIdencode)){
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
			//遵守惠圈商户协议
			if (mChkObeyProtocol.isChecked() == false) {
				getDateInfo(R.string.toast_chb_obey);
				break;
			}
			
			// 手机的唯一标示IMEI
			String IMEI = PhoneInfoUtils.getPhoneIMEI(getMyActivity());
			Log.d(TAG, "手机的唯一标示IMEI >>> " +IMEI);
			// 执行注册
			mBtnConfirm.setEnabled(false);
			new RegisterTask(getMyActivity(), new RegisterTask.Callback() {
				@Override
				public void getResult(JSONObject result) {
					mBtnConfirm.setEnabled(true);
					if (null == result) {
						return;
					}
				}
			}).execute(registerPhonenum,registerIdencode, registerPwd, recommendedCode,IMEI);
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
	@OnClick({R.id.iv_turn_in,R.id.chb_obey_protocol,R.id.tv_terms})
	public void trunIdenCode(View view) {
		switch (view.getId()) {
		case R.id.iv_turn_in:
			getMyActivity().finish();
			break;
		case R.id.chb_obey_protocol://是否选中
			if (mChkObeyProtocol.isChecked()) {
				mChkObeyProtocol.setButtonDrawable(R.drawable.login_ok);
			} else {
				mChkObeyProtocol.setButtonDrawable(R.drawable.login_checkbox);
			}
			break;
		case R.id.tv_terms://商圈协议
			String type = CustConst.HactTheme.HONE_SYSTERM;
			Intent it = new Intent(getMyActivity(), ActThemeDetailActivity.class);
			it.putExtra(ActThemeDetailActivity.TYPE, type);
			getMyActivity().startActivity(it);
			break;
			
		default:
			break;
		}
	}
	
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(RegisterFragment.class.getSimpleName()); 
	}
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(RegisterFragment.class.getSimpleName()); //统计页面
	}
}