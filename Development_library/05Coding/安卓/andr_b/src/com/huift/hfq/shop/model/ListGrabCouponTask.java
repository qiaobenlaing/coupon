package com.huift.hfq.shop.model;

import java.util.LinkedHashMap;

import com.huift.hfq.base.SzException;
import com.huift.hfq.base.api.API;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.model.SzAsyncTask;
import com.huift.hfq.base.pojo.UserToken;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import android.app.Activity;

/**
 * 领取优惠券列表
 * @author yanfang.li
 *
 */
public class ListGrabCouponTask extends SzAsyncTask<String, String, Integer>{

	private final static String TAG = ListGrabCouponTask.class.getSimpleName();

	/***创建一个JSONObject对象**/
	private JSONObject mResult;
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
	public ListGrabCouponTask(Activity acti) {
		super(acti);
	}
	
	/**
	 * 回调方法的构造函数
	 * @param acti
	 */
	public ListGrabCouponTask(Activity acti,Callback callback) {
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
	 * 红包详情
	 */
	@Override
	protected Integer doInBackground(String... params) {
		//获得所用活动信息
		LinkedHashMap<String, Object> reqParams = new LinkedHashMap<String, Object>();
		UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		reqParams.put("batchCouponCode", params[0]);
		reqParams.put("page", params[1]);
		reqParams.put("tokenCode", userToken.getTokenCode());	
		
		try {
			int retCode = ERROR_RET_CODE;
			mResult = (JSONObject)API.reqShop("listGrabCoupon", reqParams);
			//判断查询的一个对象不为空为空 就返回一个正确的编码
			if (mResult != null || !"".equals(mResult.toString())) {
				JSONArray grabCouponAr = (JSONArray) mResult.get("couponList");
				if (grabCouponAr.size() > 0 || !"[]".equals(grabCouponAr.toString())) {
					retCode = RIGHT_RET_CODE; 
				} else {
					retCode = ERROR_RET_CODE; 
				}
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
