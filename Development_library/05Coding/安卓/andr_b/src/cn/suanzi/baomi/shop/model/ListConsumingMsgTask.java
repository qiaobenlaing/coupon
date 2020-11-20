// ---------------------------------------------------------
// @author    yanfang.li
// @version   1.0.0
// @copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
// --------------------------------------------------------- 
package cn.suanzi.baomi.shop.model;

import java.util.LinkedHashMap;

import net.minidev.json.JSONArray;
import android.R.integer;
import android.app.Activity;
import cn.suanzi.baomi.base.SzException;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.api.API;
import cn.suanzi.baomi.base.model.SzAsyncTask;

/**
 * 获得首页优惠券使用和E支付买单消息调用API
 * @author yanfnag.li
 */
public class ListConsumingMsgTask extends SzAsyncTask<String, integer, Integer>{
	
	private final static String TAG = "ListConsumingMsgTask";
	/** 定义一个正确的返回结果码 **/ 
	private final static int RIGHT_RET_CODE = 1;
	/** 定义一个错误的返回结果码 **/ 
	private final static int ERROR_RET_CODE = 0;
	/** 创建一个jSONObject对象 **/
	private JSONArray mResult;
	/** 回调方法  **/
	private Callback mCallback;
	

	/**
	 * 无参构造函数
	 * @param acti 调用者的上下文
	 */
	public ListConsumingMsgTask(Activity acti) {
		super(acti);
	}
	
	/**
	 * 有参构造函数
	 * @param acti 调用者的上下文
	 * @param callback 回调方法
	 */
	public ListConsumingMsgTask(Activity acti, Callback callback) {
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
	
/*	@Override
	protected void onPreExecute() {
		if(this.mActivity != null) {
			boolean firstFlag = DB.getBoolean(ShopConst.Key.IS＿FIRST_RUN);
			Log.d(TAG, "firstFlag=2"+firstFlag);
				if (firstFlag) {
					if (mProcessDialog != null) {
						mProcessDialog.dismiss();
					}
				} else {
					DB.saveBoolean(ShopConst.Key.IS＿FIRST_RUN,true);
				}
			
		}
	}*/
	
	/**
	 * 获得首页优惠券使用和E支付买单消息的数据去调用API
	 * @param 
	 */
	@Override                          
	protected Integer doInBackground(String... params) {
		LinkedHashMap<String, Object> reqParams = new LinkedHashMap<String, Object>();	
		reqParams.put("shopCode", params[0]);
		reqParams.put("tokenCode", params[1]);
		
		try {
			//调用API
			mResult = (JSONArray) API.reqShop("listConsumingMsg", reqParams);
			int retCode = ERROR_RET_CODE;
			if (mResult.size() != 0 || !"[]".equals(mResult.toString())) {
				retCode = RIGHT_RET_CODE; //1就代表方位成功
			}
			//返回执行码 retCode
			return retCode;
				
		} catch (SzException e) {
			this.mErrCode=e.getErrCode();
			return this.mErrCode.getCode();
		} 
	}
	
	/**
	 * 处理业务逻辑
	 * @param retCode 返回码
	 */
	@Override
	protected void handldBuziRet(int retCode) {
		if (retCode == RIGHT_RET_CODE) {
			mCallback.getResult(mResult);//把查询到的数据传递到主线程中
		} else {
			mCallback.getResult(null);
			Util.showToastZH("亲，没有数据加载");
		}
		
	}

}
