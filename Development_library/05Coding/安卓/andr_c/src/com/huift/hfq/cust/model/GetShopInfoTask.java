package com.huift.hfq.cust.model;

import java.util.LinkedHashMap;

import net.minidev.json.JSONObject;
import android.app.Activity;
import android.util.Log;
import com.huift.hfq.base.ErrorCode;
import com.huift.hfq.base.SzException;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.api.API;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.model.SzAsyncTask;
import com.huift.hfq.base.pojo.UserToken;


public class GetShopInfoTask extends SzAsyncTask<String, Integer, Integer> {
	private static final String TAG = GetShopInfoTask.class.getSimpleName();

	
	private CallBack mCallback;
	
	private JSONObject mResult;
	
	public GetShopInfoTask(Activity acti) {
		super(acti);
	}

	public GetShopInfoTask(Activity acti, CallBack callback) {
		super(acti);
		this.mCallback = callback;
	}


	public interface CallBack{
		void getResult(JSONObject result);
	}
	
	@Override
	protected Integer doInBackground(String... params) {
		UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		String userCode = "";
		
		if(null != userToken && DB.getBoolean(DB.Key.CUST_LOGIN)){
			userCode = userToken.getUserCode();
		}
		Log.d(TAG, "userCode==="+userCode);
		LinkedHashMap<String, Object> reqparams = new LinkedHashMap<String, Object>();
		reqparams.put("shopCode", params[0]);
		reqparams.put("userCode", userCode);
		
		try {
			mResult = (JSONObject) API.reqCust("getShopInfo", reqparams);
			int retCode = ErrorCode.ERROR_RET_CODE;
			if(null!=mResult){
				Log.d(TAG, "GetShopInfoTask==="+mResult.toString());
				retCode = ErrorCode.RIGHT_RET_CODE;
			}
			return retCode;
		} catch (SzException e) {
			e.printStackTrace();
			this.mErrCode = e.getErrCode();
			return this.mErrCode.getCode();
		}
		
	
	}

	@Override
	protected void handldBuziRet(int retCode) {
		Log.d(TAG, "GetShopInfoTask====code"+retCode);
		switch (retCode) {
		case ErrorCode.RIGHT_RET_CODE: //成功
			mCallback.getResult(mResult);
			break;
			
		case ErrorCode.ERROR_RET_CODE: //失败
			mCallback.getResult(null);
			Util.showToastZH("获取店铺信息失败，请联系惠圈人员");
			break;

		default: //其他情况
			mCallback.getResult(null);
			Util.showToastZH("获取店铺信息失败，请联系惠圈人员");
			break;
		}
	}
}
