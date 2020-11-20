package com.huift.hfq.shop.model;

import android.app.Activity;
import android.widget.Toast;

import com.huift.hfq.base.ErrorCode;
import com.huift.hfq.base.SzException;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.api.API;
import com.huift.hfq.base.model.SzAsyncTask;

import net.minidev.json.JSONObject;

import java.util.LinkedHashMap;

public class MainDecImgTask extends SzAsyncTask<String, Integer, Integer> {
	/** 定义一个正确的返回结果码 **/
	private final static int RIGHT_RET_CODE = 1;
	/** 定义一个错误的返回结果码 **/
	private final static int ERROR_RET_CODE = 0;

	/** 调用API返回对象 **/
	private JSONObject mResult;

	/** 回调方法 **/
	private Callback mCallback;

	public MainDecImgTask(Activity acti, Callback callback) {
		super(acti);
		this.mCallback = callback;
	}

	@Override
	protected void handldBuziRet(int retCode) {
		// RIGHT_RET_CODE的值是1 如果retCode访问成功的发 返回的是1
		if (retCode == RIGHT_RET_CODE) {
			Util.showToastZH( "提交成功！");
			if (mCallback != null){
				mCallback.getResult(mResult);
			}
		} else {
				if (mCallback != null){
					mCallback.getResult(null);
				}
				Toast.makeText(this.mActivity, ErrorCode.getMsg(retCode),Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	protected Integer doInBackground(String... params) {
		
		LinkedHashMap<String, Object> reqparams = new LinkedHashMap<String, Object>();
		reqparams.put("shopCode", params[0]);
		reqparams.put("decorationCode", params[1]);
		reqparams.put("tokenCode", params[2]);

		try {
			// 调用API
			mResult = (JSONObject) API.reqShop("setMainShopDecImg", reqparams);
			int retCode = ERROR_RET_CODE;
			// 判断查询的一个对象不为空为空 就返回一个正确的编码
			if (!(mResult == null || "".equals(mResult.toJSONString()))) {
				retCode = RIGHT_RET_CODE; // 1 代表访问成功
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
