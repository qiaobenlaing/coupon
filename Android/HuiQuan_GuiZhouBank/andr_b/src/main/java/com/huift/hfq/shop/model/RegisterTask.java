package com.huift.hfq.shop.model;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.huift.hfq.base.ErrorCode;
import com.huift.hfq.base.SzException;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.api.API;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.model.LoginTask;
import com.huift.hfq.base.model.SzAsyncTask;
import com.huift.hfq.shop.R;
import com.huift.hfq.shop.ShopConst;
import com.huift.hfq.shop.activity.HomeActivity;
import com.huift.hfq.shop.activity.LoginActivity;

import net.minidev.json.JSONObject;

import java.util.LinkedHashMap;

/**
 * @author wensi.yu
 * 激活账号
 *
 */
public class RegisterTask extends SzAsyncTask<String, Integer, Integer> { // 继承SzAsyncTask

	/**手机号码格式不正确**/
	public static final int REGISTER_PHONE_ERR = 60001;
	/** 手机号码已经被使用**/
	public static final int REGISTER_PHONE_MAKE = 60003;
	/**密码格式不正确***/
	public static final int REGISTER_PWD_ERROR = 60015;
	/**验证码不正确*/
	public static final int INDENCODE_ERROR = 80011;
	/**员工不存在*/
	public static final int INDENCODE_NOTHING = 80042;
	/** 创建一个JSONObject对象**/
	private JSONObject result;
	/** 回调方法*/
	private Callback callback;
	private String mobileNbr;
	private String password;
	
	/**
	 * 构造函数
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
	 * 激活
	 * [0]手机号  [1]验证码  [2]密码
	 */
	@Override
	protected Integer doInBackground(String... params) {
		try {
			LinkedHashMap<String, Object> reqParams = new LinkedHashMap<String, Object>();
			reqParams.put("mobileNbr",params[0]);
			reqParams.put("validateCode",params[1]);
			reqParams.put("password",params[2]);
			reqParams.put("bank_id",params[3]);
			
			result = (JSONObject) API.reqShop("activate", reqParams);
			//如果注册成功，保存到数据库
			String retCode =  result.get("code").toString();
			return Integer.parseInt(retCode);
		
		} catch (SzException e) {
			this.mErrCode=e.getErrCode();
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
			// 销毁前面没销毁的
			Util.exit(); 
			callback.getResult(result);
			//注册之后登录
			SharedPreferences mSharedPreferences = this.mActivity.getSharedPreferences("register", Context.MODE_PRIVATE);
			String name = mSharedPreferences.getString("registerPhonenum", "");
			String pass = mSharedPreferences.getString("registerPwd", "");
			String zoneId = mSharedPreferences.getString("zoneId", "");
			//传过来的registerid
			String regId = DB.getStr(ShopConst.JPUSH_REGID);
			//登录的状态
			String loginStatus = mActivity.getIntent().getStringExtra(LoginTask.ALL_LOGIN);
			new LoginTask(mActivity, new LoginTask.Callback() {
				@Override
				public void getResult(int result) {
					if (result == ErrorCode.FAIL){
						Intent intent = new Intent(mActivity, LoginActivity.class);
						mActivity.startActivity(intent);
						mActivity.finish();
					}
				}
			} , HomeActivity.class,loginStatus).execute(name,pass,zoneId,regId);
			
		} else {
			if(retCode == ErrorCode.FAIL){
				callback.getResult(null);
				Util.getContentValidate(R.string.toast_register_format);
			} else if(retCode == REGISTER_PHONE_ERR){
				callback.getResult(null);
				Util.getContentValidate(R.string.toast_register_format);
			} else if(retCode == REGISTER_PHONE_MAKE){
				callback.getResult(null);
				Util.getContentValidate(R.string.toast_register_phoneuser);
			} else if(retCode == REGISTER_PWD_ERROR){
				callback.getResult(null);
				Util.getContentValidate(R.string.toast_register_pwdformaterroe);
			} else if(retCode == INDENCODE_ERROR){
				callback.getResult(null);
				Util.getContentValidate(R.string.toast_register_incode);
			} else if(retCode == INDENCODE_NOTHING){
				callback.getResult(null);
				Util.getContentValidate(R.string.toast_register_nouser);
			}else {
				callback.getResult(null);
				String msg;
				try {
					msg= (String) result.get("msg");
				}catch (NullPointerException e){
					msg="激活失败，错误码："+retCode;
				}
				Util.showToastZH(msg);
			}
		}
	}
}
