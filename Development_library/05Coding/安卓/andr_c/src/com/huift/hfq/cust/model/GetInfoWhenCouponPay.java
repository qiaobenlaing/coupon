package com.huift.hfq.cust.model;

import java.util.LinkedHashMap;

import net.minidev.json.JSONObject;
import android.app.Activity;
import android.util.Log;
import com.huift.hfq.base.SzException;
import com.huift.hfq.base.api.API;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.model.SzAsyncTask;
import com.huift.hfq.base.pojo.UserToken;

/**
 * 获取优惠券对象
 * @author yingchen
 *
 */
public class GetInfoWhenCouponPay extends SzAsyncTask<String, Integer, Integer> {
	private static final String TAG = GetInfoWhenCouponPay.class.getSimpleName();
	
	//定义一个请求成功的结果码
	private static final int RIGHT_CODE = 50000;
	//定义一个请求失败的结果码
	private static final int ERROR_CODE = 20000;
	
	private CallBack mCallBack;
	
	private JSONObject mResult;
	public GetInfoWhenCouponPay(Activity acti) {
		super(acti);
	}
	
	public interface CallBack{
		public void  getResult(JSONObject result);
	}
	
	
	public GetInfoWhenCouponPay(Activity acti, CallBack mCallBack) {
		super(acti);
		this.mCallBack = mCallBack;
	}


	@Override
	protected Integer doInBackground(String... params) {
		// 获得一个用户信息对象
		UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		String userCode = userToken.getUserCode();// 用户编码
		String tokenCode = userToken.getTokenCode();// 需要令牌认证
				
		LinkedHashMap<String, Object> reqparams = new LinkedHashMap<String, Object>();
		reqparams.put("batchCouponCode", params[0]);
		reqparams.put("userCode", userCode);
		reqparams.put("tokenCode", tokenCode);
		
		int retCode = ERROR_CODE;
		try {
			mResult = (JSONObject) API.reqCust("getInfoWhenCouponPay", reqparams);
			if(null != mResult){
				Log.d(TAG, "GetInfoWhenCouponPay123456789==="+reqparams.toString());
				Log.d(TAG, "GetInfoWhenCouponPay==="+mResult.toString());
				retCode = RIGHT_CODE;
			}
		} catch (SzException e) {
			retCode = ERROR_CODE;
			e.printStackTrace();
		}
		return retCode;
	}
	
	
	@Override
	protected void handldBuziRet(int retCode) {
		switch (retCode) {
		case RIGHT_CODE:
			mCallBack.getResult(mResult);
			break;
		case ERROR_CODE:
			mCallBack.getResult(null);
			break;

		default:
			break;
		}
	}

}
