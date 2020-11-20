// ---------------------------------------------------------
// @author    yanfang.li
// @version   1.0.0
// @createTime 2015.5.22 
// @copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
// ---------------------------------------------------------
package com.huift.hfq.shop.model;

import java.util.LinkedHashMap;

import com.huift.hfq.base.ErrorCode;
import com.huift.hfq.base.SzException;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.api.API;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.model.SzAsyncTask;
import com.huift.hfq.base.pojo.UserToken;

import net.minidev.json.JSONObject;
import android.app.Activity;
import android.util.Log;

/**
 * 添加会员卡,调用API
 * @author yanfang.li
 */
public class SendMsgTask extends SzAsyncTask<String, Integer, Integer> {

	private final static String TAG = "AddCardTask";
	private JSONObject mResult;
	private Callback mCallback;
	
	/**
	 * 无参构造方法 
	 * @param acti 调用者的上下文
	 */
	public SendMsgTask(Activity acti) {
		super(acti);
	}
	
	/**
	 * 有参构造方法
	 * @param acti 调用者的上下文
	 * @param callback 回调方法
	 */
	public SendMsgTask(Activity acti, Callback callback) {
		super(acti);
		mCallback = callback;
	}

	/**  
     * 回调方法的接口  
     *  
     */
	public interface Callback{
		public void getResult(JSONObject result);
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (mActivity != null)
			if (mProcessDialog != null ) {
				mProcessDialog.dismiss();
			}
	}

	/**
	 * 调用API查询，添加会员卡    
	 * params[0] 会员卡的名称
	 * params[1] 会员卡的等级
	 * 
	 */
	@Override
	protected Integer doInBackground(String... params) {
		UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		String shopCode = userToken.getShopCode();
		String staffCode = userToken.getStaffCode();
		String tokenCode = userToken.getTokenCode();
		
		LinkedHashMap<String, Object> reqparams = new LinkedHashMap<String,Object>();
		String message = "";
		if (Util.isEmpty(params[1].trim())) {
			Log.d(TAG, "sendMsg12="+params[1]);
			message = "";
		} else {
			message = params[1];
			Log.d(TAG, "sendMsg13="+params[1]);
		}
		reqparams.put("shopCode", shopCode); 
		reqparams.put("userCode", params[0]); // 接收者编码
		reqparams.put("staffCode", staffCode); // 发送者编码
		reqparams.put("message", message);  // 内容
		reqparams.put("tokenCode", tokenCode); 
		
		try {
			//调用API
			mResult = (JSONObject) API.reqShop("sendMsg", reqparams);
			int retCode = Integer.parseInt(mResult.get("code").toString());
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
		if( retCode == ErrorCode.SUCC ){
			
			mCallback.getResult(mResult);
			
		}else{
			mCallback.getResult(null);
		}
	}

}
