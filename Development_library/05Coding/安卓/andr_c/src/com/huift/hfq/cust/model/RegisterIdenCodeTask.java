package com.huift.hfq.cust.model;

import java.util.LinkedHashMap;

import com.huift.hfq.cust.activity.LoginActivity;

import net.minidev.json.JSONObject;
import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import com.huift.hfq.base.ErrorCode;
import com.huift.hfq.base.SzException;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.api.API;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.model.SzAsyncTask;
import com.huift.hfq.cust.R;

/***
 * @author wensi.yu
 * 获取注册验证码
 */
public class RegisterIdenCodeTask extends SzAsyncTask<String, integer, Integer>  {

	private final static String TAG = "RegisterIdenCodeTask";
	
	/**请求失败*/
	public static final int INDENCODE_CODE_ERROR = 80010;
	/**手机号码已经使用*/
	public static final int INDENCODE_REGISTER_ERROR = 60003;
	/**调用API返回对象 **/
	private JSONObject mResult;
	/**回调方法**/ 	
	private Callback callback;
	/**登陆的状态*/
	private boolean mLoginFlag;
	
	/**
	 * 构造函数
	 * @param acti
	 */
	public RegisterIdenCodeTask(Activity acti) {
		super(acti);
	}
	
	/**
	 * 回调方法的构造函数
	 * @param acti
	 */
	public RegisterIdenCodeTask(Activity acti,Callback callback) {
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
	
	/**
	 * 提交数据到服务器，获取验证码。
	 * 
	 * @param params [0]手机号；[1]动作。
	 */
	@Override
	protected Integer doInBackground(String... params) {
		LinkedHashMap<String, Object> reqParams= new LinkedHashMap<String, Object>();
		reqParams.put("mobileNbr",params[0]);
		reqParams.put("action","r");
		reqParams.put("appType","c");//顾客端
		
		try {
			mResult = (JSONObject) API.reqComm("getValidateCode", reqParams);
			long retIdenCode = (Long)mResult.get("code");
			Log.d(TAG,"retIdenCode============="+retIdenCode);
			return Integer.parseInt(String.valueOf(retIdenCode));
			
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
		if ( retCode == ErrorCode.SUCC ) {
			callback.getResult(mResult);
			
		} else {
			if (retCode == ErrorCode.FAIL ) {
				callback.getResult(null);
				Util.getContentValidate(R.string.toast_register_indencodefail);
			 }
			else if(retCode == INDENCODE_REGISTER_ERROR){
				callback.getResult(null);
				mLoginFlag = DB.getBoolean(DB.Key.CUST_LOGIN);
				if (mLoginFlag) {
					Util.getContentValidate(R.string.toast_register_phoneuser);
				}else{
					Util.getContentValidate(R.string.toast_register_login);
					Intent it = new Intent(this.mActivity, LoginActivity.class);
					this.mActivity.startActivity(it);
				}
			}
		}
	}
}
