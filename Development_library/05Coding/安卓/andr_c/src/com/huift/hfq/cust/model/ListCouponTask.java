package com.huift.hfq.cust.model;

import java.util.LinkedHashMap;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import android.app.Activity;
import com.huift.hfq.base.SzException;
import com.huift.hfq.base.api.API;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.model.SzAsyncTask;
import com.huift.hfq.base.pojo.UserToken;

/**
 * 获得商家的优惠信息
 * 
 * @author yanfang.li
 */
public class ListCouponTask extends SzAsyncTask<String, Integer, Integer> {

	private final static String TAG = ListCouponTask.class.getSimpleName();
	/** 定义一个正确的返回结果码 **/
	private final static int RIGHT_RET_CODE = 1;
	/** 定义一个错误的返回结果码 **/
	private final static int ERROR_RET_CODE = 0;

	/** 调用API返回对象 **/
	private JSONObject mResult;

	/** 回调方法 **/
	private Callback mCallback;

	public ListCouponTask(Activity acti, Callback callback) {
		super(acti);
		this.mCallback = callback;
	}

	/**
	 * 回调方法的接口
	 * 
	 */
	public interface Callback {
		public void getResult(JSONObject result);
	}

	@Override
	protected void onPreExecute() {
		if (mActivity != null && mProcessDialog != null) {
			mProcessDialog.dismiss();
		}
	}

	@Override
	protected Integer doInBackground(String... params) {
		LinkedHashMap<String, Object> reqparams = new LinkedHashMap<String, Object>();
		UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		String userCode = "";
		if (null != userToken) {
			userCode = userToken.getUserCode();
		} else {
			userCode = ""; // 为空
		}
		reqparams.put("couponType", params[0]);
		reqparams.put("searchWord", params[1]);
		reqparams.put("longitude", params[2]);
		reqparams.put("latitude", params[3]);
		reqparams.put("page", params[4]);
		reqparams.put("city", params[5]);
		reqparams.put("userCode", userCode);

		try {
			// 调用API
			mResult = (JSONObject) API.reqCust("listCoupon", reqparams);
			JSONArray couponAr = (JSONArray) mResult.get("couponList");
			int retCode = ERROR_RET_CODE;
			if (couponAr.size() != 0 || !"[]".equals(couponAr.toJSONString())) {
				retCode = RIGHT_RET_CODE; // 1 代表访问成功
			}
			return retCode;
		} catch (SzException e) {
			this.mErrCode = e.getErrCode();
			return this.mErrCode.getCode();
		} catch (Exception e) {
			return 0;
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
}
