package com.huift.hfq.cust.model;

import java.util.LinkedHashMap;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import android.app.Activity;
import android.util.Log;
import com.huift.hfq.base.SzException;
import com.huift.hfq.base.api.API;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.model.SzAsyncTask;
import com.huift.hfq.base.pojo.UserToken;

/**
 * 获得顾客活动报名的列表
 * 
 * @author wensi.yu
 * 
 */
public class ListUserActTask extends SzAsyncTask<String, String, Integer> {

	private final static String TAG = "ListUserActTask";

	/*** 创建一个JSONObject对象 **/
	private JSONObject mResult;
	/** 定义一个正确的返回结果码 **/
	private final static int RIGHT_RET_CODE = 1;
	/** 定义一个错误的返回结果码 **/
	private final static int ERROR_RET_CODE = 0;
	/** 回调方法 **/
	private Callback callback;

	public ListUserActTask(Activity acti) {
		super(acti);
	}

	/**
	 * 回调方法的构造函数
	 * 
	 * @param acti
	 */
	public ListUserActTask(Activity acti, Callback callback) {
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
		if (mActivity != null && mProcessDialog != null) {
			mProcessDialog.dismiss();
		}
	}

	/**
	 * 我的活动列表
	 */
	@Override
	protected Integer doInBackground(String... params) {
		UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		String tokenCode = userToken.getTokenCode();
		LinkedHashMap<String, Object> reqParams = new LinkedHashMap<String, Object>();
		reqParams.put("userCode", params[0]);
		reqParams.put("longitude", params[1]);
		reqParams.put("latitude", params[2]);
		reqParams.put("page", Integer.parseInt(params[3]));
		reqParams.put("tokenCode", tokenCode);

		try {
			Log.d(TAG, "查询。。。。。。。。。。");
			int retCode = ERROR_RET_CODE;
			mResult = (JSONObject) API.reqCust("listUserAct", reqParams);
			Log.d(TAG, "mResult===============" + mResult);
			// 判断查询的一个对象不为空为空 就返回一个正确的编码
			JSONArray ArResult = (JSONArray) mResult.get("userActList");
			if (mResult == null || ArResult.size() != 0 || !"[]".equals(ArResult.toString())) {
				retCode = RIGHT_RET_CODE; // 1 代表访问成功
			}
			Log.d(TAG, "我的活动===============" + retCode);
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
