package com.huift.hfq.shop.model;

import java.util.LinkedHashMap;

import com.huift.hfq.base.ErrorCode;
import com.huift.hfq.base.SzException;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.api.API;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.model.SzAsyncTask;
import com.huift.hfq.base.pojo.UserToken;

import net.minidev.json.JSONObject;
import android.app.Activity;
import android.util.Log;
import com.huift.hfq.shop.R;

/**
 * 餐后支付--结算
 * @author wensi.yu
 */
public class SubmitEndTask extends SzAsyncTask<String, String, Integer>{
	
	private final static String TAG = "SubmitEndTask";
	
	private final static int NOT_COMMIT = 60514;
	/** 创建一个JSONObject对象*/
	private JSONObject mResult;
	/** 定义一个正确的返回结果码 */ 
	private final static int RIGHT_RET_CODE = 1;
	/** 定义一个错误的返回结果码 */ 
	private final static int ERROR_RET_CODE = 0;
	/**回调方法**/ 	
	private Callback callback;

	/**
	 * 构造函数
	 * @param acti
	 */
	public SubmitEndTask(Activity acti) {
		super(acti);
	}
	
	/**
	 * 回调方法的构造函数
	 * @param acti
	 */
	public SubmitEndTask(Activity acti,Callback callback) {
		super(acti);
		this.callback = callback;
	}
	
	/**  
     * 回调方法的接口  
     *  
     */  
    public interface Callback{  
        public void getResult(JSONObject mResult);  
    }

	/**
	 * 业务处理
	 */
	@Override
	protected Integer doInBackground(String... params) {
		UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		String tokenCode = userToken.getTokenCode();
		LinkedHashMap<String, Object> reqParams = new LinkedHashMap<String, Object>();
		reqParams.put("orderCode", params[0]);
		reqParams.put("actualOrderAmount", params[1]);
		reqParams.put("orderProductList", params[2]);
		reqParams.put("tokenCode", tokenCode);		
		try {
			mResult = (JSONObject)API.reqShop("submitEnd", reqParams);
			Log.i(TAG, "mResult==============="+mResult);
			return Integer.parseInt(String.valueOf(mResult.get("code"))) ;
			
		} catch (SzException e) {
			this.mErrCode = e.getErrCode();
			return this.mErrCode.getCode();
		}
	}

	@Override
	protected void handldBuziRet(int retCode) {
		if (retCode == ErrorCode.SUCC) {
			callback.getResult(mResult);
		} else {
			if(retCode == ErrorCode.FAIL){
				callback.getResult(null);
				Util.getContentValidate(R.string.myafter_order_commit_fail);
			} else if (retCode == NOT_COMMIT) {
				callback.getResult(null);
				Util.getContentValidate(R.string.not_commit);
			}
		}
	}
}
