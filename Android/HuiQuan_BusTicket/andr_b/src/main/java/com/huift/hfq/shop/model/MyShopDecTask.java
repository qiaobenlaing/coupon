package com.huift.hfq.shop.model;

import java.util.LinkedHashMap;

import com.huift.hfq.base.ErrorCode;
import com.huift.hfq.base.SzException;
import com.huift.hfq.base.api.API;
import com.huift.hfq.base.model.SzAsyncTask;

import net.minidev.json.JSONObject;
import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

public class MyShopDecTask extends SzAsyncTask<String , String , Integer> {
	private final static String TAG = "MyShopDecTask" ;
	/**创建一个JSONArray对象**/
	private JSONObject mResult ;
	/**定义一个正确的返回结果码**/
	private final static int RIGHT_RET_CODE = 1 ;
	/**定义一个错误的返回结果码**/ 
	private final static int ERROR_RET_CODE = 0;
	
	/**
	 * 回调方法	
	 */
	private Callback callback ;
		
	/***
	 * @param acti 上下文
	 * @param callback 回调方法
	 */
	public MyShopDecTask(Activity acti , Callback callback) {
		super(acti);
		this.callback = callback ;
	}
	
	/**  
     * 回调方法的接口 
     */  
    public interface Callback{  
        public void getResult(JSONObject object) ;  
    }
	
	public MyShopDecTask(Activity acti) {
		super(acti);
	}

	/**
	 * onPostExecute()中的正常业务逻辑处理.
	 */
	@Override
	protected void handldBuziRet(int retCode) {
		if (retCode == RIGHT_RET_CODE) {
			callback.getResult(mResult) ;
		}else if(retCode==ERROR_RET_CODE){
			Log.d(this.mActivity.getClass().getSimpleName() , "" + retCode + " - " + ErrorCode.getMsg(retCode)) ;
			Toast.makeText(this.mActivity , ErrorCode.getMsg(retCode) ,Toast.LENGTH_SHORT).show() ;
		}
	}

	/**
	 * 调用API，查询店铺装修信息
	 */
	@Override
	protected Integer doInBackground(String... params) {
		LinkedHashMap<String, Object> reqParams = new LinkedHashMap<String , Object>() ;
		reqParams.put("shopCode" , params[0]) ;
		reqParams.put("tokenCode" , params[1]) ;
		try {
			int retCode = ERROR_RET_CODE;
			if (!("[]".equals(API.reqShop("getShopDecoration", reqParams).toString()))) {
				mResult = (JSONObject)API.reqShop("getShopDecoration", reqParams);
				//判断查询的一个对象不为空为空 就返回一个正确的编码
				if ( mResult != null || !"".equals(mResult.toJSONString()) ) {
					retCode = RIGHT_RET_CODE; //1 代表访问成功
				}
			}
			return retCode;
		} catch (SzException e) {
			this.mErrCode=e.getErrCode() ;
			return this.mErrCode.getCode() ;
		}
	}
}
