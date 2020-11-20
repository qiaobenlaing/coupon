package com.huift.hfq.shop.model;

import java.util.LinkedHashMap;

import com.huift.hfq.base.SzException;
import com.huift.hfq.base.api.API;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.model.SzAsyncTask;
import com.huift.hfq.base.pojo.UserToken;

import net.minidev.json.JSONObject;
import android.app.Activity;
import android.util.Log;

/**
 * 用户推荐码
 * @author wensi.yu
 *
 */
public class GetShopInviteCodeTask extends SzAsyncTask<String, String, Integer> {
	
	private final static String TAG = GetShopInviteCodeTask.class.getSimpleName();

	/***创建一个JSONObject对象**/
	private JSONObject mResult;
	/** 定义一个正确的返回结果码 **/ 
	private final static int RIGHT_RET_CODE = 1;
	/** 定义一个错误的返回结果码 **/ 
	private final static int ERROR_RET_CODE = 0;
	/**回调方法**/ 	
	private Callback callback;
	
	public GetShopInviteCodeTask(Activity acti) {
		super(acti);
	}
	
	/**
	 * 回调方法的构造函数
	 * @param acti
	 */
	public GetShopInviteCodeTask(Activity acti,Callback callback) {
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
	
    @Override
   	protected void onPreExecute() {
   		super.onPreExecute();
   		if (null != mActivity) {
   			if (null != mProcessDialog) {
   				mProcessDialog.dismiss();
   			}
   		}
   	}
    
	/**
	 * 获得邀请码
	 */
	@Override
	protected Integer doInBackground(String... params) {
		UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		String shopCode = "";
		if(DB.getBoolean(DB.Key.CUST_LOGIN)){
			shopCode = userToken.getShopCode();
		}else{
			shopCode = "";
		}
		String tokenCode = userToken.getTokenCode();
		
		LinkedHashMap<String, Object> reqParams = new LinkedHashMap<String, Object>();
		reqParams.put("shopCode",shopCode); 
		reqParams.put("month",params[0]); 
		reqParams.put("tokenCode",tokenCode); 
		
		try {
			mResult = (JSONObject) API.reqShop("getShopInviteCode", reqParams);
			Log.i(TAG, "mResult==============="+mResult);
			int retCode = ERROR_RET_CODE;
			if (mResult != null ) {
				retCode = RIGHT_RET_CODE; //1 代表访问成功
			}
			return retCode;
			
		} catch (SzException e) {
			this.mErrCode = e.getErrCode();
			return this.mErrCode.getCode();
		}
	}
	
	@Override
	protected void handldBuziRet(int retCode) {
		if(retCode == RIGHT_RET_CODE){
			callback.getResult(mResult);
		}
		else if(retCode == ERROR_RET_CODE){
			callback.getResult(null);
		}
	}
}
