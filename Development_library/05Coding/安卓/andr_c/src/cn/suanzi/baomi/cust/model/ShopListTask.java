package cn.suanzi.baomi.cust.model;

import java.util.LinkedHashMap;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import android.app.Activity;
import cn.suanzi.baomi.base.SzException;
import cn.suanzi.baomi.base.api.API;
import cn.suanzi.baomi.base.data.DB;
import cn.suanzi.baomi.base.model.SzAsyncTask;
import cn.suanzi.baomi.base.pojo.UserToken;

public class ShopListTask extends SzAsyncTask<String, Integer, Integer> {
	private final static String TAG = "ShopListTask";
	/** 定义一个正确的返回结果码 **/
	private final static int RIGHT_RET_CODE = 1;
	/** 定义一个错误的返回结果码 **/
	private final static int ERROR_RET_CODE = 0;
	/** 调用API返回对象 **/
	private JSONObject mResult;
	/** 回调方法 **/
	private Callback mCallback;

	public ShopListTask(Activity acti, Callback callback) {
		super(acti);
		this.mCallback = callback;
	}
	
	@Override
	protected void onPreExecute() {
		if (mActivity != null) {
			if (mProcessDialog != null) {
				mProcessDialog.dismiss();
			}
		}
	}
	
	/**
	 * 回调方法的接口
	 * 
	 */
	public interface Callback {
		// 传递参数
		// 是异步请求的结果
		public void getResult(JSONObject result);
	}

	@Override
	protected Integer doInBackground(String... params) {
		
		UserToken mUserToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		String userCode = "";
		if (DB.getBoolean(DB.Key.CUST_LOGIN)) {
			userCode = mUserToken.getUserCode();// 用户编码
		} else {
			userCode = "";
		}
		LinkedHashMap<String, Object> reqparams = new LinkedHashMap<String, Object>();
		reqparams.put("searchWord", params[0]);
		reqparams.put("type", params[1]);
		reqparams.put("longitude", params[2]);
		reqparams.put("latitude", params[3]);
		reqparams.put("userCode", userCode);
		reqparams.put("city", params[4]);
		reqparams.put("page", params[5]);
		reqparams.put("moduleValue", params[6]);
		reqparams.put("content", params[7]);
		reqparams.put("order", params[8]);
		reqparams.put("filter", params[9]);

		try {
			// 调用API
			mResult = (JSONObject) API.reqCust("searchShop", reqparams);
			int retCode = ERROR_RET_CODE;
			//判断查询的一个对象不为空为空 就返回一个正确的编码
			JSONArray arResult = (JSONArray) mResult.get("shopList");
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
		} else {
			mCallback.getResult(null);
		}
	}
}
