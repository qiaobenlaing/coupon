package com.huift.hfq.shop.model;

import java.util.LinkedHashMap;

import com.huift.hfq.base.ErrorCode;
import com.huift.hfq.base.SzException;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.api.API;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.model.SzAsyncTask;
import com.huift.hfq.base.pojo.UserToken;
import com.huift.hfq.shop.activity.SettledCommitSuccActivity;

import net.minidev.json.JSONObject;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import com.huift.hfq.shop.R;

/**
 *提交申请入驻 
 * @author wensi.yu
 *
 */
public class ApplyEntryTask extends SzAsyncTask<String , String , Integer> {
	
	/**创建一个JSONObject对象**/
	private JSONObject mResult ;
	private Callback mCallback;

	public ApplyEntryTask(Activity acti) {
		super(acti);
	}
	
	/**
	 * 有参构造方法
	 * @param acti 调用者的上下文
	 * @param callback 回调方法
	 */
	public ApplyEntryTask(Activity acti, Callback callback) {
		super(acti);
		mCallback = callback;
	}
	
	/**  
     * 回调方法的接口  
     */
	public interface Callback{
		public void getResult(JSONObject mResult);
	}
	
	@Override
	protected Integer doInBackground(String... params) {
		UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		String shopCode = "";
		if (DB.getBoolean(DB.Key.CUST_LOGIN)) {
			shopCode = userToken.getShopCode();
		}else{
			shopCode = "";
		}
		LinkedHashMap<String, Object> reqParams = new LinkedHashMap<String , Object>() ;
		reqParams.put("shopCode" , shopCode) ;
		reqParams.put("shopName" , params[0]) ;
		reqParams.put("tel" , params[1]) ;
		reqParams.put("startTime" , params[2]) ;
		reqParams.put("endTime" , params[3]) ;
		reqParams.put("street" , params[4]) ;
		reqParams.put("mobileNbr" , params[5]) ;
		reqParams.put("bank_id" , params[6]) ;
		
		try {
			mResult = (JSONObject) API.reqShop("applyEntry", reqParams) ;
			// 如果成功，保存到数据库
			return Integer.parseInt(String.valueOf(mResult.get("code"))) ;
		} catch (SzException e) {
			this.mErrCode = e.getErrCode() ;
			return this.mErrCode.getCode() ;
		}
	}
	
	@Override
	protected void handldBuziRet(int retCode) {
		if (retCode == ErrorCode.SUCC) {
			mCallback.getResult(mResult);
		}else{
			if (retCode == ErrorCode.USER_NOT_AUTHORIZED) {
				Intent intent = new Intent(mActivity, SettledCommitSuccActivity.class);
				mActivity.startActivity(intent);
			}else{
				Util.getContentValidate(R.string.toast_commitsucc_fail);
			}
		}
	}
}
