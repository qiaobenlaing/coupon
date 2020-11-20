package com.huift.hfq.cust.model;

import java.util.LinkedHashMap;

import com.huift.hfq.cust.activity.HomeActivity;
import com.huift.hfq.cust.activity.NewRegisterActivity;
import com.huift.hfq.cust.application.CustConst;
import com.huift.hfq.cust.application.CustConst.IsOpenAct;

import net.minidev.json.JSONObject;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import com.huift.hfq.base.Const;
import com.huift.hfq.base.ErrorCode;
import com.huift.hfq.base.SzException;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.api.API;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.model.LoginTask;
import com.huift.hfq.base.model.SzAsyncTask;
import com.huift.hfq.base.model.UserTokenModel;
import com.huift.hfq.base.pojo.UserToken;
import com.huift.hfq.cust.R;

/**
 * @author wensi.yu
 * 注册
 *
 */
public class RegisterTask extends SzAsyncTask<String, Integer, Integer> { // 继承SzAsyncTask

	private final static String TAG = "RegisterTask";
	
	/**手机号码格式不正确*/
	public static final int REGISTER_PHONE_ERR = 60001;
	/**手机号码已经被使用*/
	public static final int REGISTER_PHONE_MAKE = 60003;
	/**密码格式不正确*/
	public static final int REGISTER_PWD_ERROR = 60015;
	/**验证码不正确*/
	public static final int INDENCODE_ERROR = 80011;
	/**邀请码不存在*/
	public static final int RECOMMONED_NO_EXIST = 50508;
	/** 该手机注册的账号数量达到上限*/
	public static final int REGISTER_PHONE_NUM = 50509;
	/** 注册成功后缓存到数据库的对象*/
	private UserToken mUserToken;
	/** 创建一个JSONObject对象**/
	private JSONObject result;
	/**回调方法*/
	private Callback callback;

	/**
	 * 构造函数
	 * 
	 * @param acti
	 */
	public RegisterTask(Activity acti) {
		super(acti);
	}
	
	/**
	 * 回调方法的构造函数
	 * @param acti
	 */
	public RegisterTask(Activity acti,Callback callback) {
		super(acti);
		this.callback = callback;
	}
	
	/**  
     * 回调方法的接口  
     *  
     */  
    public interface Callback{  
        public void getResult(JSONObject result);  
    }

	/***
	 * 提交数据到服务器，进行注册验证 [0]手机号 [1]验证码 [2]密码 [3]推荐码
	 */
	@Override
	protected Integer doInBackground(String... params) {
		try {
			
			LinkedHashMap<String, Object> reqParams = new LinkedHashMap<String, Object>();
			reqParams.put("mobileNbr", params[0]);
			reqParams.put("validateCode", params[1]);
			reqParams.put("password", params[2]);
			reqParams.put("recomNbr", params[3]);
			reqParams.put("deviceNbr", Util.md5(params[4]));

			Log.i("TAG","手机号码：" + params[0] + "验证码：" + params[1]+ "密码：" + params[2] + "推荐人：" + params[3]);
			result = (JSONObject) API.reqCust("register", reqParams);
			Log.i(TAG, "result============="+result);
			// 如果注册成功，保存到数据库
			Long retCode = (Long) result.get("code");
			Log.i(TAG, "retCode=============="+retCode);
			return Integer.parseInt(String.valueOf(retCode));
		} catch (SzException e) {
			this.mErrCode = e.getErrCode();
			return this.mErrCode.getCode();
		}
	}

	/**
	 * onPostExecute()中的正常业务逻辑处理.
	 */
	@Override
	protected void handldBuziRet(int retCode) {
		if (retCode == ErrorCode.SUCC) {
			Util.getContentValidate(R.string.toast_register_ok);
			callback.getResult(result);
			SharedPreferences mSharedPreferences = this.mActivity.getSharedPreferences("register", Context.MODE_PRIVATE);
			String name = mSharedPreferences.getString("registerPhonenum", "");
			String pass = mSharedPreferences.getString("registerPwd", "");
			//传过来的registerid
			String regId = null;
			if(Util.isEmpty(regId)){
				regId = "";
			}else{
				regId = DB.getStr(CustConst.JPush.JPUSH_REGID);
				Log.d(TAG, "RegisterSave="+regId);
			}
			
			//保存userCode
			String userCode = result.get("userCode").toString();
			mUserToken = new UserToken();
			mUserToken.setId("1001");
			mUserToken.setUserCode(userCode);
			UserTokenModel.saveToken(mUserToken);
			
			//登录的状态
			String loginStatus = mActivity.getIntent().getStringExtra(LoginTask.ALL_LOGIN);
			DB.saveBoolean(CustConst.IS_REGISTER, true);
			
			String isOpenAct = DB.getStr(Const.IS_OPENREGACT);
			if(IsOpenAct.OPEN.equals(isOpenAct)){
				new LoginTask(mActivity, new LoginTask.Callback() {
					@Override
					public void getResult(int result) {
						if (ErrorCode.SUCC == result ) {
							if (null != Util.homeActivityList || Util.homeActivityList.size() > 0) {
								Util.exitHome();
							}
						} 
					}
				}, NewRegisterActivity.class,loginStatus).execute(name,pass,regId);
			} else{
				new LoginTask(mActivity, new LoginTask.Callback() {
					@Override
					public void getResult(int result) {
						if (ErrorCode.SUCC == result) {
							if (null != Util.homeActivityList || Util.homeActivityList.size() > 0) {
								Util.exitHome();
							}
						} 
					}
				}, HomeActivity.class,loginStatus).execute(name,pass,regId);
			}
		} else {
			if (retCode == ErrorCode.FAIL ) {
				callback.getResult(null);
				Util.getContentValidate(R.string.toast_register_error);
			}
			else if(retCode == REGISTER_PHONE_ERR){
				callback.getResult(null);
				Util.getContentValidate(R.string.toast_register_format);
			}
			else if(retCode == REGISTER_PHONE_MAKE){
				callback.getResult(null);
				Util.getContentValidate(R.string.toast_register_phoneuser);
			}
			else if(retCode == REGISTER_PWD_ERROR){
				callback.getResult(null);
				Util.getContentValidate(R.string.toast_register_pwdformaterroe);
			}
			else if(retCode == INDENCODE_ERROR){
				callback.getResult(null);
				Util.getContentValidate(R.string.toast_register_indencodeerroe);
			}
			else if (retCode == RECOMMONED_NO_EXIST){
				callback.getResult(null);
				Util.getContentValidate(R.string.toast_register_recommoned);
			}
			// 手机不能注册多个账号
			else if (retCode == REGISTER_PHONE_NUM){
				callback.getResult(null);
				Util.getContentValidate(R.string.register_phone_num);
			}
		}
	}
}
