package com.huift.hfq.cust.model;

import java.util.LinkedHashMap;

import net.minidev.json.JSONObject;
import android.app.Activity;
import android.util.Log;
import com.huift.hfq.base.SzException;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.api.API;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.model.SzAsyncTask;
import com.huift.hfq.base.pojo.UserToken;
import com.huift.hfq.cust.R;

/**
 * 验证支付密码是否正确
 * @author yingchen
 *
 */
public class ValidatePayPwdTask extends SzAsyncTask<String, Integer, Integer>{
	private static final String TAG = ValidatePayPwdTask.class.getSimpleName();
	/**定义一个成功的结果码*/
	private static final int RIGHT_CODE = 1;
	/**定义一个请求失败的结果码*/
	private static final int ERROR_CODE = 0;
	private CallBack mCallBack;
	
	private JSONObject mResult;
	
	public ValidatePayPwdTask(Activity acti) {
		super(acti);
	}
	
	public ValidatePayPwdTask(Activity acti, CallBack mCallBack) {
		super(acti);
		this.mCallBack = mCallBack;
	}


	public interface CallBack{
		public void getResult(JSONObject result);
	}
	
	@Override
	protected Integer doInBackground(String... params) {
		UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		
		LinkedHashMap<String, Object> reqparams = new LinkedHashMap<String, Object>();
		reqparams.put("userCode", userToken.getUserCode());
		reqparams.put("payPwd", params[0]);
		reqparams.put("tokenCode", userToken.getTokenCode());
		
		try {
			mResult = (JSONObject) API.reqCust("validatePayPwd", reqparams);
			if(null != mResult){
				Log.d(TAG, "ValidatePayPwdTask == "+mResult.toString());
				return RIGHT_CODE;
			}else{
				return ERROR_CODE;
			}
		} catch (SzException e) {
			this.mErrCode=e.getErrCode();
			return this.mErrCode.getCode();	
		}
		
		
	}

	@Override
	protected void handldBuziRet(int retCode) {
		switch (retCode) {
		case RIGHT_CODE:
			mCallBack.getResult(mResult);
			break;
		case ERROR_CODE:
			mCallBack.getResult(null);
			Util.getContentValidate(R.string.check_pay_password_fail);
		default:
			mCallBack.getResult(null);
			Util.getContentValidate(R.string.check_pay_password_fail);
			break;
		}
	}
}
