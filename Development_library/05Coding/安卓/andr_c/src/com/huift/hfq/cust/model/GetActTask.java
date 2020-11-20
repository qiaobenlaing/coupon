package com.huift.hfq.cust.model;

import java.util.LinkedHashMap;

import net.minidev.json.JSONObject;
import android.app.Activity;
import com.huift.hfq.base.SzException;
import com.huift.hfq.base.api.API;
import com.huift.hfq.base.model.SzAsyncTask;

/**
 * 首页活动入口
 * 
 * @author yanfnag.li
 */
public class GetActTask extends SzAsyncTask<String, Integer, Integer> {
	private final static String TAG = GetActTask.class.getSimpleName();
	/** 定义一个正确的返回结果码 **/
	private final static int RIGHT_RET_CODE = 1;
	/** 定义一个错误的返回结果码 **/
	private final static int ERROR_RET_CODE = 0;

	/** 调用API返回对象 **/
	private JSONObject mResult;

	/** 回调方法 **/
	private Callback mCallback;

	public GetActTask(Activity acti, Callback callback) {
		super(acti);
		this.mCallback = callback;
	}

	@Override
	protected void onPreExecute() {
		if (mActivity != null && mProcessDialog != null) {
			mProcessDialog.dismiss();
		}
	}

	@Override
	protected void handldBuziRet(int retCode) {
		// RIGHT_RET_CODE的值是1 如果retCode访问成功的发 返回的是1
		if (retCode == RIGHT_RET_CODE) {
			mCallback.getResult(mResult);
		} else {
			mCallback.getResult(null);
		}
	}

	@Override
	protected Integer doInBackground(String... params) {

		LinkedHashMap<String, Object> reqparams = new LinkedHashMap<String, Object>();
		reqparams.put("pos", params[0]);

		try {
			// 调用API
			Object result = (Object) API.reqCust("getAct", reqparams);
			int retCode = ERROR_RET_CODE;
			if (null == result || "[]".equals(result.toString())) {
				retCode = ERROR_RET_CODE;
			} else {
				mResult = (JSONObject) result;
				// 判断查询的一个对象不为空为空 就返回一个正确的编码
				if (!(mResult == null || "".equals(mResult.toJSONString()))) {
					retCode = RIGHT_RET_CODE; // 1 代表访问成功
				}
			}
			return retCode;
		} catch (SzException e) {
			this.mErrCode = e.getErrCode();
			return this.mErrCode.getCode();// 返回错误编码
		}
	}

	/**
	 * 回调方法的接口
	 * 
	 */
	public interface Callback {
		/**
		 * 传递参数
		 * 
		 * @param result
		 *            是异步请求的结果
		 */
		public void getResult(JSONObject result);
	}

}
