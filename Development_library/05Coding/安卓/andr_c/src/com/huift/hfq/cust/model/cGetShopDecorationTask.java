package com.huift.hfq.cust.model;

import java.util.LinkedHashMap;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import android.app.Activity;
import com.huift.hfq.base.SzException;
import com.huift.hfq.base.api.API;
import com.huift.hfq.base.model.SzAsyncTask;

/**
 * 商店详情中的环境
 * 
 * @author ad
 */
public class cGetShopDecorationTask extends SzAsyncTask<String, Integer, Integer> {
	private final static String TAG = cGetShopDecorationTask.class.getSimpleName();
	/** 定义一个正确的返回结果码 **/
	private final static int RIGHT_RET_CODE = 1;
	/** 定义一个错误的返回结果码 **/
	private final static int ERROR_RET_CODE = 0;

	/** 调用API返回对象 **/
	private JSONObject mResult;
	/** 回调方法 **/
	private Callback mCallback;

	public cGetShopDecorationTask(Activity acti, Callback callback) {
		super(acti);
		this.mCallback = callback;
	}

	@Override
	protected void handldBuziRet(int retCode) {
		if (retCode == RIGHT_RET_CODE) {
			mCallback.getResult(mResult);
		} else {
			mCallback.getResult(null);
		}
	}

	@Override
	protected void onPreExecute() {
		if (mActivity != null && null != mProcessDialog) {
			mProcessDialog.dismiss();
		}
	}

	@Override
	protected Integer doInBackground(String... params) {

		LinkedHashMap<String, Object> reqparams = new LinkedHashMap<String, Object>();
		reqparams.put("shopCode", params[0]);
		reqparams.put("page", params[1]);

		try {
			// 调用API
			mResult = (JSONObject) API.reqCust("cGetShopDecoration", reqparams);
			int retCode = ERROR_RET_CODE;
			// 判断查询的一个对象不为空为空 就返回一个正确的编码
			if (!(mResult == null || "".equals(mResult.toString()))) {
				JSONArray shopDecoList = (JSONArray) mResult.get("shopDecoList");
				if (!(shopDecoList.size() == 0 || "[]".equals(shopDecoList.toString()))) {
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
		public void getResult(JSONObject result);
	}
}
