// ---------------------------------------------------------
// @author    yanfang.li
// @version   1.0.0
// @copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
// ---------------------------------------------------------
package com.huift.hfq.shop.model;

import java.util.LinkedHashMap;

import com.huift.hfq.base.SzException;
import com.huift.hfq.base.api.API;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.model.SzAsyncTask;
import com.huift.hfq.base.pojo.UserToken;

import net.minidev.json.JSONArray;
import android.app.Activity;

/**
 * 获得不同类型优惠券的统计信息
 * @author yanfang.li
 */
public class GetCouponInfoByTypeTask extends SzAsyncTask<String, String, Integer> {
	
	private final static String TAG = "GetCouponInfoByTypeTask";
	/***创建一个JSONArray对象**/
	private JSONArray mResult;
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
	public GetCouponInfoByTypeTask(Activity acti) {
		super(acti);
	}
	
	/**
	 * 回调方法的构造函数
	 * @param acti
	 */
	public GetCouponInfoByTypeTask(Activity acti,Callback callback) {
		super(acti);
		this.callback=callback;
	}
	
	/**  
     * 回调方法的接口  
     *  
     */  
    public interface Callback{  
        public void getResult(JSONArray result);  
    }
    
    @Override
    protected void onPreExecute() {
    	super.onPreExecute();
    	if (mActivity != null) {
    		if (mProcessDialog != null) {
    			mProcessDialog.dismiss();
    		}
    	}
    }

	/**
	 * params[0] 商家编码
	 * params[1] 令牌认证
	 */
	@Override
	protected Integer doInBackground(String... params) {
		UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		String shopCode = userToken.getShopCode();
		String tokenCode = userToken.getTokenCode();
		//获得所用活动信息
		LinkedHashMap<String, Object> reqParams = new LinkedHashMap<String, Object>();		
		reqParams.put("shopCode", shopCode);
		reqParams.put("tokenCode", tokenCode);
		
		try {
			int retCode = ERROR_RET_CODE;
			if(!("[]".equals(API.reqShop("getCouponInfoByType", reqParams).toString()))){
				mResult = (JSONArray)API.reqShop("getCouponInfoByType", reqParams);
				
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
	 * 正常业务逻辑处理
	 */
	@Override
	protected void handldBuziRet(int retCode) {
		if (retCode == RIGHT_RET_CODE) {
			callback.getResult(mResult);
		}else {
			callback.getResult(null);
		}
	}
}
