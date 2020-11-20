// ---------------------------------------------------------
// @author    yanfang.li
// @version   1.0.0
// @createTime 2015.5.22 
// @copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
// ---------------------------------------------------------
package com.huift.hfq.shop.model;

import java.util.LinkedHashMap;

import com.huift.hfq.base.SzException;
import com.huift.hfq.base.api.API;
import com.huift.hfq.base.model.SzAsyncTask;

import net.minidev.json.JSONArray;
import android.app.Activity;

/**
 * 每批次优惠券消费人次数据,调用API
 * @author yanfang.li
 */
public class ListCouponPersonTrendTask extends SzAsyncTask<String, Integer, Integer> {

	private final static String TAG = "ListCouponPersonTrendTask";
	/** 定义一个正确的返回结果码 **/ 
	private final static int RIGHT_RET_CODE = 1;
	/** 定义一个错误的返回结果码 **/ 
	private final static int ERROR_RET_CODE = 0;
	/** 回调方法 **/
	private Callback mCallback;
	/** 调用API返回对象 **/
	private JSONArray mResult;
	
	/**
	 * 无参构造方法 
	 * @param acti 调用者的上下文
	 */
	public ListCouponPersonTrendTask(Activity acti) {
		super(acti);
	}
	
	/**
	 * 有参构造方法
	 * @param acti 调用者的上下文
	 * @param callback 回调方法
	 */
	public ListCouponPersonTrendTask(Activity acti, Callback callback) {
		super(acti);
		mCallback = callback;
	}

	/**  
     * 回调方法的接口  
     *  
     */
	public interface Callback{
		/**
		 * 传递参数
		 * @param result 是异步请求的结果
		 */
		public void getResult(JSONArray result);
	}

	/**
	 * 调用API查询，统计会员卡
	 * params[0] 是统计会员卡信息方法的输入参数 shopCode
	 * params[1] 是令牌认证的编码
	 * 
	 */
	@Override
	protected Integer doInBackground(String... params) {
		LinkedHashMap<String, Object> reqparams = new LinkedHashMap<String,Object>();
		reqparams.put("shopCode", params[0]);
		reqparams.put("tokenCode", params[1]);
		
		try {
			//调用API
			mResult = (JSONArray) API.reqShop("listCouponPersonTrend", reqparams);
			/*int retCode = ERROR_RET_CODE;
			//判断查询的一个对象不为空为空 就返回一个正确的编码
			if ( mResult.size() != 0 || !"[]".equals(mResult.toJSONString()) ) {
				retCode = RIGHT_RET_CODE; //1 代表访问成功
			}*/
			
			return RIGHT_RET_CODE;
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
			/*mCallback.getResult(null);*/
//			Util.getContentValidate(mActivity, mActivity.getResources().getString(R.string.toast_nulldata));
		}
	}

}
