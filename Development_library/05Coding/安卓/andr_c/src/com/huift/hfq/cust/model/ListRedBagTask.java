package com.huift.hfq.cust.model;

import java.util.LinkedHashMap;

import net.minidev.json.JSONArray;
import android.app.Activity;
import android.util.Log;
import com.huift.hfq.base.ErrorCode;
import com.huift.hfq.base.SzException;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.api.API;
import com.huift.hfq.base.model.SzAsyncTask;
import com.huift.hfq.cust.R;

/**
 * 获得商家的优惠信息
 * @author yanfang.li
 */
public class ListRedBagTask extends SzAsyncTask<String, Integer, Integer> {
	private final static String TAG = "ListCouponTask";
	/** 调用API返回对象 **/
	private JSONArray mResult;

	/** 回调方法 **/
	private Callback mCallback;

	public ListRedBagTask(Activity acti, Callback callback) {
		super(acti);
		this.mCallback = callback;
	}

	/**
	 * 回调方法的接口
	 * 
	 */
	public interface Callback {
		public void getResult(JSONArray mResult);
	}

	@Override
	protected Integer doInBackground(String... params) {
		LinkedHashMap<String, Object> reqparams = new LinkedHashMap<String, Object>();
		reqparams.put("userCode", params[0]);
		reqparams.put("tokenCode", params[1]);

		try {
			// 调用API
			Object tmp = API.reqCust("getMyBonus", reqparams);
			Log.d(TAG, "我来了======================"+tmp);
			mResult = (JSONArray) tmp;
			int retCode = ErrorCode.ERROR_RET_CODE;
			if (mResult != null) {     
				retCode = ErrorCode.RIGHT_RET_CODE; // 1 代表访问成功
			}
			return retCode;
		} catch (SzException e) {
			this.mErrCode = e.getErrCode();
			return this.mErrCode.getCode();
		}
	}

	@Override
	protected void handldBuziRet(int retCode) {
		// RIGHT_RET_CODE的值是1 如果retCode访问成功的发 返回的是1
		if (retCode == ErrorCode.RIGHT_RET_CODE) {
			mCallback.getResult(mResult);
		} else {
			mCallback.getResult(null);
			Util.getContentValidate(R.string.toast_data_fail);
		}
	}
}
