package com.huift.hfq.cust.model;

import java.util.LinkedHashMap;

import net.minidev.json.JSONObject;
import android.app.Activity;
import android.util.Log;
import com.huift.hfq.base.SzException;
import com.huift.hfq.base.api.API;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.model.SzAsyncTask;
import com.huift.hfq.base.pojo.UserToken;

/**
 * 获得用户活动详情
 * 
 * @author wensi.yu
 * 
 */
public class GetUserActInfo extends SzAsyncTask<String, String, Integer> {

	private final static String TAG = "GetUserActInfo";

	/*** 创建一个JSONObject对象 **/
	private JSONObject mResult;
	/** 定义一个正确的返回结果码 **/
	private final static int RIGHT_RET_CODE = 1;
	/** 定义一个错误的返回结果码 **/
	private final static int ERROR_RET_CODE = 0;
	/** 回调方法 **/
	private Callback callback;

	public GetUserActInfo(Activity acti) {
		super(acti);

	}

	/**
	 * 回调方法的构造函数
	 * 
	 * @param acti
	 */
	public GetUserActInfo(Activity acti, Callback callback) {
		super(acti);
		this.callback = callback;
	}

	/**
	 * 回调方法的接口
	 * 
	 */
	public interface Callback {
		public void getResult(JSONObject mResult);
	}

	@Override
	protected void onPreExecute() {
		if (null != mActivity && mProcessDialog != null) {
			mProcessDialog.dismiss();
		}
	}

	/**
	 * 获得用户活动详情
	 */
	@Override
	protected Integer doInBackground(String... params) {
		UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		String tokenCode = userToken.getTokenCode();
		LinkedHashMap<String, Object> reqParams = new LinkedHashMap<String, Object>();
		reqParams.put("userActCode", params[0]);
		reqParams.put("tokenCode", tokenCode);

		try {
			Log.d(TAG, "访问了。。。。");
			int retCode = ERROR_RET_CODE;
			mResult = (JSONObject) API.reqCust("getUserActInfo", reqParams);
			Log.d(TAG, "查询报名人数===============" + mResult);
			// 判断查询的一个对象不为空为空 就返回一个正确的编码
			if (mResult != null) {
				retCode = RIGHT_RET_CODE; // 1 代表访问成功
			}
			Log.d(TAG, "查询============" + retCode);
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
		} else {
			if (retCode == ERROR_RET_CODE) {
				callback.getResult(null);
			}
		}
	}

}
