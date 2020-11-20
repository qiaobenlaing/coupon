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
 * 设置支付密码
 * @author yingchen
 *
 */
public class SetPayPwdTask extends SzAsyncTask<String, Integer, Integer> {
	private static final String TAG = SetPayPwdTask.class.getSimpleName();
	/**定义一个成功的结果码*/
	private static final int RIGHT_RET_CODE = 50000;
	/**定义一个失败的结果码*/
	private static final int ERR0R_RET_CODE = 20000;
	
	private CallBack mCallBack;
	
	private JSONObject mResult;
	
	public SetPayPwdTask(Activity acti) {
		super(acti);
	}

	
	public SetPayPwdTask(Activity acti, CallBack mCallBack) {
		super(acti);
		this.mCallBack = mCallBack;
	}

	public interface CallBack{
		public void getResult(boolean success);
	}

	@Override
	protected Integer doInBackground(String... params) {
		UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		LinkedHashMap<String, Object> reqparams = new LinkedHashMap<String, Object>();
		reqparams.put("userCode", userToken.getUserCode());
		reqparams.put("payPwd", params[0]);
		reqparams.put("confirmPayPwd", params[1]);
		reqparams.put("tokenCode", userToken.getTokenCode());
		
		try {
			mResult = (JSONObject) API.reqCust("setPayPwd", reqparams);
			int retCode = ERR0R_RET_CODE;
			if(null != mResult){
				Log.d(TAG, "SetPayPwdTask == "+mResult.toString());
				retCode = Integer.parseInt(mResult.get("code").toString());
			}
			return retCode;
			
		} catch (SzException e) {
			this.mErrCode=e.getErrCode();
			return this.mErrCode.getCode();	
		}
	}
	
	@Override
	protected void handldBuziRet(int retCode) {
		switch (retCode) {
		case RIGHT_RET_CODE:
			Util.getContentValidate(R.string.set_pay_password_success);
			mCallBack.getResult(true);
			break;
		case ERR0R_RET_CODE:
			Util.getContentValidate(R.string.set_pay_password_fail);
			mCallBack.getResult(false);
			break;

		default:
			Util.getContentValidate(R.string.set_pay_password_fail);
			mCallBack.getResult(false);
			
			break;
		}
	}

}
