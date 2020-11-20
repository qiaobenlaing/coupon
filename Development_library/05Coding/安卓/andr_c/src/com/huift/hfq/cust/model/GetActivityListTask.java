//---------------------------------------------------------
//@author    yanfang.li
//@version   1.0.0
//@createTime 2015.6.2
//@copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
//---------------------------------------------------------
package com.huift.hfq.cust.model;

import java.util.LinkedHashMap;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import android.app.Activity;
import com.huift.hfq.base.SzException;
import com.huift.hfq.base.api.API;
import com.huift.hfq.base.model.SzAsyncTask;

/**
 * 获取某一类型的活动/某一商家的活动/某一普通用户发起的活动
 * @author yanfang.li
 */
public class GetActivityListTask extends SzAsyncTask<String, Integer, Integer> {
	private final static String TAG = "GetActivityListTask";
	/** 定义一个正确的返回结果码 **/
	private final static int RIGHT_RET_CODE = 1;
	/** 定义一个错误的返回结果码 **/
	private final static int ERROR_RET_CODE = 0;
	/** 调用API返回对象 **/
	private JSONObject mResult;
	/** 回调方法 **/
	private Callback mCallback;

	public GetActivityListTask(Activity acti, Callback callback) {
		super(acti);
		this.mCallback = callback;
	}

	/**
	 * 回调方法的接口
	 */
	public interface Callback {
		public void getResult(JSONObject result);
	}

	@Override
	protected Integer doInBackground(String... params) {
		LinkedHashMap<String, Object> reqparams = new LinkedHashMap<String, Object>();
		reqparams.put("type", params[0]);
		reqparams.put("shopCode", params[1]);
		reqparams.put("longitude", params[2]);
		reqparams.put("latitude", params[3]);
		reqparams.put("page", params[4]);

		try {
			int retCode = ERROR_RET_CODE;
			// 调用API
			mResult = (JSONObject) API.reqCust("getActivityList", reqparams);
			//判断查询的一个对象不为空为空 就返回一个正确的编码
			JSONArray arResult = (JSONArray) mResult.get("activityList");
			if (mResult == null || !"[]".equals(arResult.toString()) || arResult.size() != 0) {
				retCode = RIGHT_RET_CODE; //1 代表访问成功
			}
			return retCode;
		} catch (SzException e) {
			this.mErrCode = e.getErrCode();
			return this.mErrCode.getCode();// 返回错误编码
		}
	}

	@Override
	protected void handldBuziRet(int retCode) {
		// RIGHT_RET_CODE的值是1 如果retCode访问成功的发 返回的是1
		if (retCode == RIGHT_RET_CODE) {
			mCallback.getResult(mResult);
		} else if(retCode == ERROR_RET_CODE){
			mCallback.getResult(null);
		}
	}
}
