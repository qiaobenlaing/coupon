// ---------------------------------------------------------
// @author    yanfang.li
// @version   1.0.0
// @copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
// ---------------------------------------------------------
package com.huift.hfq.shop.model;

import android.app.Activity;

import com.huift.hfq.base.SzException;
import com.huift.hfq.base.api.API;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.model.SzAsyncTask;
import com.huift.hfq.base.pojo.UserToken;

import net.minidev.json.JSONObject;

import java.util.LinkedHashMap;

/**
 * 获得会员卡的统计信息,调用API
 * @author yanfang.li
 */
public class CountCardTask extends SzAsyncTask<String, Integer, Integer> {

	/** 定义一个正确的返回结果码 **/
	private final static int RIGHT_RET_CODE = 1;
	/** 定义一个错误的返回结果码 **/ 
	private final static int ERROR_RET_CODE = 0;
	/** 回调方法 **/
	private Callback mCallback;
	/** 调用API返回对象 **/
	private JSONObject mResult;
	
	/**
	 * 无参构造方法 
	 * @param acti 调用者的上下文
	 */
	public CountCardTask(Activity acti) {
		super(acti);
	}
	
	/**
	 * 有参构造方法
	 * @param acti 调用者的上下文
	 * @param callback 回调方法
	 */
	public CountCardTask(Activity acti, Callback callback) {
		super(acti);
		mCallback = callback;
	}

	/**  
     * 回调方法的接口  
     *  
     */
	public interface Callback{
		public void getResult(JSONObject rsult);
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (mActivity != null ) {
			if (mProcessDialog != null ) {
				mProcessDialog.dismiss();
			}
		}
	}

	/**
	 * 调用API查询，统计会员卡
	 * params[0] 是统计会员卡信息方法的输入参数 shopCode
	 * params[1] 是令牌认证的编码
	 * 
	 */
	@Override
	protected Integer doInBackground(String... params) {
		UserToken userToken= DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
	    String shopCode = userToken.getShopCode();// 商家编码
	    String tokenCode = userToken.getTokenCode();// 用户登录后获得的令牌	
		LinkedHashMap<String, Object> reqparams = new LinkedHashMap<String,Object>();
		reqparams.put("shopCode", shopCode);
		reqparams.put("tokenCode", tokenCode);
		try {
			int retCode = ERROR_RET_CODE;
			if (!("[]".equals(API.reqShop("countCard", reqparams).toString()))) {
				mResult = (JSONObject)API.reqShop("countCard", reqparams);
				
				//判断查询的一个对象不为空为空 就返回一个正确的编码
				if ( mResult != null || !"".equals(mResult.toJSONString()) ) {
					retCode = RIGHT_RET_CODE; //1 代表访问成功
				}
			}
			 
			return retCode;
		} catch (SzException e) {
			
			this.mErrCode = e.getErrCode();
			return this.mErrCode.getCode();//返回错误编码
		}
		 
	}
	
	/**
	 * 处理查询结果的返回值 
	 * @param retCode 执行成功返回码
	 */
	@Override
	protected void handldBuziRet(int retCode) {
		
		//RIGHT_RET_CODE的值是1  如果retCode访问成功的发 返回的是1  
		if ( retCode == RIGHT_RET_CODE ) {
			mCallback.getResult(mResult);
		} else {
			mCallback.getResult(null);
		}
	}

}
