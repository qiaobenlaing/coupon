package com.huift.hfq.cust.model;

import java.util.LinkedHashMap;

import net.minidev.json.JSONObject;
import android.app.Activity;
import android.util.Log;
import com.huift.hfq.base.ErrorCode;
import com.huift.hfq.base.SzException;
import com.huift.hfq.base.api.API;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.model.SzAsyncTask;
import com.huift.hfq.base.pojo.UserToken;

public class GetUserActListTask extends SzAsyncTask<String, Integer, Integer> {
	private static final String TAG = GetUserActListTask.class.getSimpleName();
	private JSONObject mResult;
	private CallBack mCallBack;
	public GetUserActListTask(Activity acti) {
		super(acti);
	}

	public GetUserActListTask(Activity acti, CallBack mCallBack) {
		super(acti);
		this.mCallBack = mCallBack;
	}

	@Override
	protected void onPreExecute() {
		if (mActivity != null && mProcessDialog != null) {
			mProcessDialog.dismiss();
		}
	}
	public interface CallBack{
		void getResult(JSONObject result); 
	}
	@Override
	protected Integer doInBackground(String... params) {
		UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		String userCode = userToken.getUserCode();
		String tokenCode = userToken.getTokenCode();// 用户登录后获得的令牌
		
		LinkedHashMap<String, Object> reqparams = new LinkedHashMap<String, Object>();
		reqparams.put("userCode", userCode);
		reqparams.put("type", params[0]);
		reqparams.put("page", Integer.parseInt(params[1]));
		reqparams.put("tokenCode", tokenCode);
		
		int retCode = ErrorCode.ERROR_RET_CODE;
		try {
			mResult = (JSONObject) API.reqCust("getUserActList", reqparams);
			if(null != mResult){
				Log.d(TAG, "GetUserActListTask==="+mResult.toString());
				retCode = ErrorCode.RIGHT_RET_CODE;
			}
		} catch (SzException e) {
			this.mErrCode = e.getErrCode();
			return this.mErrCode.getCode();
		}
		
		return retCode;
	}
	
	@Override
	protected void handldBuziRet(int retCode) {
		Log.d(TAG, "retCode=="+retCode);
		switch (retCode) {
		case ErrorCode.RIGHT_RET_CODE:
			mCallBack.getResult(mResult);
			break;
		case ErrorCode.ERROR_RET_CODE:
			mCallBack.getResult(null);
			break;

		default:
			mCallBack.getResult(null);
			break;
		}
	}


}
