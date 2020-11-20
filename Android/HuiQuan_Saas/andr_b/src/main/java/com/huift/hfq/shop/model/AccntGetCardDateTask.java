package com.huift.hfq.shop.model;

import java.util.LinkedHashMap;

import com.huift.hfq.base.SzException;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.api.API;
import com.huift.hfq.base.model.SzAsyncTask;

import net.minidev.json.JSONArray;
import android.app.Activity;
import android.util.Log;
import com.huift.hfq.shop.R;

/**
 * 通过日期按日查询银行卡列表
 * @author wensi.yu
 *
 */
public class AccntGetCardDateTask extends SzAsyncTask<String, String, Integer> {

	/***创建一个JSONArray对象**/
	private JSONArray mResult;
	/** 定义一个正确的返回结果码 **/ 
	private final static int RIGHT_RET_CODE = 1;
	/** 定义一个错误的返回结果码 **/ 
	private final static int ERROR_RET_CODE = 0;
	/**回调方法**/ 	
	private Callback callback;
	
	/**
	 * 构造方法
	 * @param acti
	 */
	public AccntGetCardDateTask(Activity acti) {
		super(acti);
		
	}
	
	/**
	 * 回调方法的构造函数
	 * @param acti
	 */
	public AccntGetCardDateTask(Activity acti,Callback callback) {
		super(acti);
		this.callback=callback;
	}
	
	/**  
     * 回调方法的接口  
     *  
     */  
    public interface Callback{  
        public void getResult(JSONArray mResult);  
    }

	/**
	 * 调用api按日查询列表
	 */
	@Override
	protected Integer doInBackground(String... params) {
		//获得所用活动信息
		LinkedHashMap<String, Object> reqParams = new LinkedHashMap<String, Object>();		
		reqParams.put("shopCode", params[0]);
		reqParams.put("actionTime", params[1]);
		reqParams.put("tokenCode", params[2]);
		
		try {
			int retCode = ERROR_RET_CODE;
			if(!("[]".equals(API.reqShop("getBankCardCountBill", reqParams).toString()))){
				mResult = (JSONArray)API.reqShop("getBankCardCountBill", reqParams);
				//判断查询的一个对象不为空为空 就返回一个正确的编码
				if (mResult != null || !"".equals(mResult.toJSONString()) ) {
					retCode = RIGHT_RET_CODE; //1 代表访问成功
				}
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
		}else {
			if(retCode == ERROR_RET_CODE){
				Util.getContentValidate(R.string.toast_todaydata);
			}
		}
	}
}
