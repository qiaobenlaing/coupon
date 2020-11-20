package com.huift.hfq.shop.model;

import android.app.Activity;

import com.huift.hfq.base.SzException;
import com.huift.hfq.base.api.API;
import com.huift.hfq.base.model.SzAsyncTask;

import net.minidev.json.JSONObject;

import java.util.LinkedHashMap;

/**
 * 查询金额
 * @author wensi.yu
 *
 */
public class ScanTask extends SzAsyncTask<String, String, Integer> {

	/**创建一个JSONObject对象*/
	private JSONObject mResult;
	/** 定义一个正确的返回结果码 */ 
	private final static int RIGHT_RET_CODE = 1;
	/** 定义一个错误的返回结果码 */ 
	private final static int ERROR_RET_CODE = 0;
	/**回调方法*/ 	
	private Callback callback;
	
	public ScanTask(Activity acti) {
		super(acti);
	}

	/**
	 * 回调方法的构造函数
	 * @param acti
	 */
	public ScanTask(Activity acti,Callback callback) {
		super(acti);
		this.callback=callback;
	}
	
	/**  
     * 回调方法的接口  
     *  
     */  
    public interface Callback{  
        public void getResult(JSONObject mResult);  
    }
	
	/**
	 * 查询金额
	 */
	@Override
	protected Integer doInBackground(String... params) {
		//获得所用活动信息
		LinkedHashMap<String, Object> reqParams = new LinkedHashMap<String, Object>();
		reqParams.put("consumeCode", params[0]);
		reqParams.put("tokenCode", params[1]);		
		
		try {
			int retCode = ERROR_RET_CODE;
			mResult = (JSONObject)API.reqShop("getRealPay", reqParams);

			if (null != mResult) {
				retCode = RIGHT_RET_CODE; //1 代表访问成功
			}
			return retCode;
			
		} catch (SzException e) {
			this.mErrCode = e.getErrCode();
			return this.mErrCode.getCode();
		}
		
	}
	
	/**
	 * 业务逻辑处理
	 */
	@Override
	protected void handldBuziRet(int retCode) {
		if (retCode == RIGHT_RET_CODE) {
			callback.getResult(mResult);
		}else{ 
			if(retCode == ERROR_RET_CODE) {
				callback.getResult(null);
			}
		}
	}

}
