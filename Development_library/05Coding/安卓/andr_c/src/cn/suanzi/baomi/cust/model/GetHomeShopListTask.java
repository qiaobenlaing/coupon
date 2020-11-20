package cn.suanzi.baomi.cust.model;

import java.util.LinkedHashMap;

import net.minidev.json.JSONObject;
import android.app.Activity;
import cn.suanzi.baomi.base.SzException;
import cn.suanzi.baomi.base.api.API;
import cn.suanzi.baomi.base.data.DB;
import cn.suanzi.baomi.base.model.SzAsyncTask;
import cn.suanzi.baomi.base.pojo.UserToken;

/**
 * 首页商家列表
 * 
 * @author yanfang.li
 */
public class GetHomeShopListTask extends SzAsyncTask<String, String, Integer> {

	private final static String TAG = GetHomeShopListTask.class.getSimpleName();
	/** 定义一个正确的返回结果码 **/
	private final static int RIGHT_RET_CODE = 1;
	/** 定义一个错误的返回结果码 **/
	private final static int ERROR_RET_CODE = 0;
	/** 创建一个JSONObject对象 **/
	private JSONObject mResult;
	// 回调方法
	private Callback mCallback;

	/**
	 * 构造函数
	 * 
	 * @param acti
	 */
	public GetHomeShopListTask(Activity acti) {
		super(acti);
	}

	/**
	 * 构造函数
	 * 
	 * @param acti
	 */
	public GetHomeShopListTask(Activity acti, Callback callback) {
		super(acti);
		mCallback = callback;
	}

	@Override
	protected void onPreExecute() {
		if (mActivity != null && null != mProcessDialog) {
			mProcessDialog.dismiss();
		}
	}

	/**
	 * 回调方法的接口
	 * 
	 */
	public interface Callback {
		public void getResult(JSONObject result);
	}

	/**
	 * 调用API
	 */
	@Override
	protected Integer doInBackground(String... params) {
		try {
			UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
			String userCode = "";
			if (null != userToken) {
				userCode = userToken.getTokenCode();
			}
			LinkedHashMap<String, Object> reqParams = new LinkedHashMap<String, Object>();
			reqParams.put("userCode", userCode);
			reqParams.put("longitude", params[0]);
			reqParams.put("latitude", params[1]);
			reqParams.put("page", params[2]);
			reqParams.put("city", params[3]);

			mResult = (JSONObject) API.reqCust("getHomeShopList", reqParams);
			int retCode = ERROR_RET_CODE;
			if (mResult != null && !"".equals(mResult.toString())) {
				retCode = RIGHT_RET_CODE;
			}
			return retCode;
		} catch (SzException e) {
			this.mErrCode = e.getErrCode();
			return this.mErrCode.getCode();
		}
	}

	/**
	 * 业务逻辑操作
	 */
	@Override
	protected void handldBuziRet(int retCode) {
		if (retCode == RIGHT_RET_CODE) {
			mCallback.getResult(mResult);
		} else {
			mCallback.getResult(null);
		}
	}
}
