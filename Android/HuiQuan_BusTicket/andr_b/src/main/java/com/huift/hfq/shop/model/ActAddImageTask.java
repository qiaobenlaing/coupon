package com.huift.hfq.shop.model;

import java.util.LinkedHashMap;

import com.huift.hfq.base.SzException;
import com.huift.hfq.base.api.API;
import com.huift.hfq.base.model.SzAsyncTask;

import net.minidev.json.JSONArray;
import android.app.Activity;

public class ActAddImageTask extends SzAsyncTask<String, String, Integer> {
	
	private final static String TAG = "ActAddImageTask";
	
	/** 创建一个JSONArray对象*/
	private JSONArray mResult;
	/** 定义一个正确的返回结果码 */ 
	private final static int RIGHT_RET_CODE = 1;
	/** 定义一个错误的返回结果码 */ 
	private final static int ERROR_RET_CODE = 0;
	/**回调方法**/ 	
	private Callback callback;
	
	public ActAddImageTask(Activity acti) {
		super(acti);
	}

	/**
	 * 回调方法的构造函数
	 * @param acti
	 */
	public ActAddImageTask(Activity acti,Callback callback) {
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
	 * 调用图片上传
	 */
	@Override
	protected Integer doInBackground(String... params) {
		LinkedHashMap<String, Object> reqParams = new LinkedHashMap<String, Object>();	
		reqParams.put("Img", params[0]);
		reqParams.put("tokenCode", params[1]);
		try {
			int retCode = ERROR_RET_CODE;
			mResult = (JSONArray)API.reqComm("uploadImg", reqParams);
			if (mResult != null || !"".equals(mResult.toJSONString()) ) {
				retCode = RIGHT_RET_CODE; //1 代表访问成功
			}
			return  retCode;
			
		} catch (SzException e) {
			this.mErrCode = e.getErrCode();
			return this.mErrCode.getCode();
		}
	}
	
	@Override
	protected void handldBuziRet(int retCode) {
		if (retCode == RIGHT_RET_CODE) {
			callback.getResult(mResult);
		}else {
			/*if(retCode == ERROR_RET_CODE){
				Toast.makeText(this.mActivity, "没有对应的刷卡对账列表",Toast.LENGTH_SHORT).show();
			}*/
		}
	}


}
