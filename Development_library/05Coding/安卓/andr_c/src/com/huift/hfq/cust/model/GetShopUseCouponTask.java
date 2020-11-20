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

public class GetShopUseCouponTask extends SzAsyncTask<String, Integer, Integer> {
	private static final String TAG = GetShopUseCouponTask.class.getSimpleName();
	private JSONObject mResult;
	private CallBack mCallBack;
	
	public GetShopUseCouponTask(Activity acti) {
		super(acti);
	}
	
	public GetShopUseCouponTask(Activity acti, CallBack mCallBack) {
		super(acti);
		this.mCallBack = mCallBack;
	}

	public interface CallBack{
		void getResult(JSONObject result);
	}
	@Override
	protected Integer doInBackground(String... params) {
		String userCode = "";
		String tokenCode = "";
		
		if (DB.getBoolean(DB.Key.CUST_LOGIN)) {
			UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
			userCode = userToken.getUserCode();
			tokenCode = userToken.getTokenCode();
		}
		
		LinkedHashMap<String, Object> reqparams = new LinkedHashMap<String, Object>();
		reqparams.put("shopCode", params[0]);
		reqparams.put("userCode", userCode);
		reqparams.put("tokenCode", tokenCode);
		
		try {
			mResult = (JSONObject) API.reqCust("getShopUserCoupon", reqparams);
			int retCode = ErrorCode.ERROR_RET_CODE;
			if(null != mResult){
				Log.d(TAG, "GetShopUseCouponTask=="+mResult.toString());
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
		Log.d(TAG, "GetShopUseCouponTask====code"+retCode);
		switch (retCode) {
		case ErrorCode.RIGHT_RET_CODE: //成功
			mCallBack.getResult(mResult);
			break;
			
		case ErrorCode.ERROR_RET_CODE: //失败
			mCallBack.getResult(null);
			Util.showToastZH("获取优惠券信息失败，请联系惠圈人员");
			break;

		default: //其他情况
			mCallBack.getResult(null);
			Util.showToastZH("获取优惠券信息失败，请联系惠圈人员");
			break;
		}
	}

}
