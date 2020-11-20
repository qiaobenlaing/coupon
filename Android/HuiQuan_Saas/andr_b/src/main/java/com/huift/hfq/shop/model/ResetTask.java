package com.huift.hfq.shop.model;

import android.app.Activity;
import android.content.Intent;

import com.huift.hfq.base.ErrorCode;
import com.huift.hfq.base.SzApplication;
import com.huift.hfq.base.SzException;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.api.API;
import com.huift.hfq.base.model.SzAsyncTask;
import com.huift.hfq.shop.R;
import com.huift.hfq.shop.activity.LoginActivity;

import net.minidev.json.JSONObject;

import java.util.LinkedHashMap;

/**
 * @author wensi.yu
 *  忘记密码
 */
public class ResetTask extends SzAsyncTask<String, Integer, Integer> {

	/**用户不存在**/
	public static final int INDENCODE_CODE_NOUSER = 20207;
	/**验证码不正确*/
	public static final int INDENCODE_ERROR = 80011;
	
	/** 创建一个JSONObject对象**/
	private JSONObject result;
	/**回调方法*/
	private Callback callback;
	
	/**
	 * 构造函数
	 * @param acti
	 */
	public ResetTask(Activity acti) {
		super(acti);
		
	}
	
	/**
	 * 回调方法的构造函数
	 * @param acti
	 */
	public ResetTask(Activity acti,Callback callback) {
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
	 * 提交数据到服务器，忘记密码。
	 * @param params [0]手机号；[1]验证码 ，[2]密码。
	 */
	@Override
	protected Integer doInBackground(String... params) {
		//忘记密码
		try {
			LinkedHashMap<String, Object> reqParams = new LinkedHashMap<String, Object>();
			reqParams.put("mobileNbr",params[0]);
			reqParams.put("validateCode",params[1]);
			reqParams.put("password",params[2]);
			reqParams.put("type", SzApplication.getInstance().getCurrAppType());

			//得到结果
			result = (JSONObject) API.reqComm("findPwd", reqParams);
			//得到结果码
			Long retCode = (Long) result.get("code");

			return Integer.parseInt(String.valueOf(retCode));
			
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
			Util.getContentValidate(R.string.toast_resetpwd_ok);
			Intent it = new Intent(this.mActivity, LoginActivity.class);
			this.mActivity.startActivity(it);
			callback.getResult(result);
		} else {
			if (retCode == ErrorCode.FAIL ) {
				callback.getResult(null);
				Util.getContentValidate(R.string.toast_resetpwd_fail);
			}
			else if(retCode == INDENCODE_CODE_NOUSER){
				callback.getResult(null);
				Util.getContentValidate(R.string.toast_login_nouser);
			}
			else if(retCode == INDENCODE_ERROR){
				callback.getResult(null);
				Util.getContentValidate(R.string.toast_register_incode);
			}
		}
	}

}
