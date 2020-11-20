// ---------------------------------------------------------
// @author    yanfang.li
// @version   1.0.0
// @createTime 2015.5.22 
// @copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
// ---------------------------------------------------------
package com.huift.hfq.shop.model;

import java.util.LinkedHashMap;

import com.huift.hfq.base.SzException;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.api.API;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.model.SzAsyncTask;
import com.huift.hfq.base.pojo.UserToken;

import net.minidev.json.JSONObject;
import android.app.Activity;

/**
 * 查询会员和优惠券的消息
 * @author yanfang.li
 */
public class ListMsgTask extends SzAsyncTask<String, Integer, Integer> {

	private final static String TAG = "ListMsgTask";
	/** 定义一个正确的返回结果码 **/ 
	private final static int RIGHT_RET_CODE = 1;
	/** 定义一个错误的返回结果码 **/ 
	private final static int ERROR_RET_CODE = 0;
	//回调方法
	private Callback mCallback;
	//调用API返回对象
	private JSONObject mResult;
	
	/**
	 * 无参构造方法 
	 * @param acti 调用者的上下文
	 */
	public ListMsgTask(Activity acti) {
		super(acti);
	}
	
	/**
	 * 有参构造方法
	 * @param acti 调用者的上下文
	 * @param callback 回调方法
	 */
	public ListMsgTask(Activity acti, Callback callback) {
		super(acti);
		mCallback = callback;
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (mProcessDialog != null) {
			mProcessDialog.dismiss();
		}
	}
	
	/**  
     * 回调方法的接口  
     *  
     */
	public interface Callback{
		public void getResult(JSONObject result);
	}

	/**
	 * 调用API查询，统计会员卡
	 * params[0] 商家编码
	 * params[1] 是令牌认证的编码
	 * 
	 */
	@Override
	protected Integer doInBackground(String... params) {
		UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN,UserToken.class);
		String ShopCode = userToken.getShopCode();
		String tokenCode = userToken.getTokenCode();
		LinkedHashMap<String, Object> reqparams = new LinkedHashMap<String,Object>();
		reqparams.put("shopCode", ShopCode);
		reqparams.put("page", params[0]);
		reqparams.put("tokenCode", tokenCode);
		
		try {
			// 调用API
			mResult = (JSONObject) API.reqShop("listMsg", reqparams);
			int retCode = ERROR_RET_CODE;
			// 判断查询的一个对象不为空为空 就返回一个正确的编码
			if ( !(mResult == null || "".equals(mResult.toJSONString()) ) ) {
				retCode = RIGHT_RET_CODE; //1 代表访问成功
			}
			
			return retCode;
		} catch (SzException e) {
			
			this.mErrCode = e.getErrCode();
			return this.mErrCode.getCode();//返回错误编码
		}
		 
	}
	
	/**
	 * 处理查询结果的返回值 
	 * @param retCode执行成功返回码
	 */
	@Override
	protected void handldBuziRet(int retCode) {
		
		//RIGHT_RET_CODE的值是1  如果retCode访问成功的发 返回的是1  
		if ( retCode == RIGHT_RET_CODE ) {
			
			mCallback.getResult(mResult);
			
		} else {
			if (retCode == ERROR_RET_CODE ) {
				mCallback.getResult(null);
				Util.showToastZH("暂无消息");
			}
		}
	}

}
