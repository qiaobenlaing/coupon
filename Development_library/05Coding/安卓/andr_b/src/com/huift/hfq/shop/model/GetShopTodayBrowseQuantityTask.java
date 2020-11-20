package com.huift.hfq.shop.model;

import java.util.LinkedHashMap;

import com.huift.hfq.base.SzException;
import com.huift.hfq.base.api.API;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.model.SzAsyncTask;
import com.huift.hfq.base.pojo.UserToken;

import android.app.Activity;
import android.util.Log;

/**
 * 获得商户当前日期浏览量
 * @author qian.zhou
 */
public class GetShopTodayBrowseQuantityTask extends SzAsyncTask<String, String, Integer>{

	private final static String TAG = "ActListCountTask";

	/***创建一个JSONObject对象**/
	private String mResult;
	/** 定义一个正确的返回结果码 **/ 
	private final static int RIGHT_RET_CODE = 1;
	/** 定义一个错误的返回结果码 **/ 
	private final static int ERROR_RET_CODE = 0;
	/**回调方法**/ 	
	private Callback callback;
	
	/**
	 * 构造函数
	 * @param acti
	 */
	public GetShopTodayBrowseQuantityTask(Activity acti) {
		super(acti);
	}
	
	/**
	 * 回调方法的构造函数
	 * @param acti
	 */
	public GetShopTodayBrowseQuantityTask(Activity acti,Callback callback) {
		super(acti);
		this.callback = callback;
	}
	
	/**  
     * 回调方法的接口  
     *  
     */  
    public interface Callback{  
        public void getResult(String mResult);  
    }

    /**
	 * 调用api
	 */
	@Override
	protected Integer doInBackground(String... params) {
		UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		LinkedHashMap<String, Object> reqParams = new LinkedHashMap<String, Object>();
		reqParams.put("shopCode", userToken.getShopCode());
		reqParams.put("tokenCode", userToken.getTokenCode());	
		
		try {
			int retCode = ERROR_RET_CODE;
			mResult = (String)API.reqShop("getShopTodayBrowseQuantity", reqParams);
			Log.i(TAG, "mResult==============="+mResult);
			//判断查询的一个对象不为空为空 就返回一个正确的编码
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
		if (retCode == RIGHT_RET_CODE) {
			callback.getResult(mResult);
		}else{ 
			callback.getResult(null);
		}
	}
}
