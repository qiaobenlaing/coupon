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

/**
 * 附近
 * 
 * @author qian.zhou
 */
public class ListNearShopTask extends SzAsyncTask<String, String, Integer> {
	private static final String TAG = ListNearShopTask.class.getSimpleName();
	/** 创建一个JSONArray对象 **/
	private JSONObject mResult;
	/** 定义一个正确的返回结果码 **/
	private final static int RIGHT_RET_CODE = 1;
	/** 定义一个错误的返回结果码 **/
	private final static int ERROR_RET_CODE = 0;
	/** 回调方法 **/
	private Callback mCallback;

	/**
	 * 回调方法的接口
	 */
	public interface Callback {
		public void getResult(JSONObject jsonobject);
	}

	/**
	 * 构造函数
	 * 
	 * @param acti
	 */
	public ListNearShopTask(Activity acti) {
		super(acti);
	}

	/**
	 * @param acti
	 *            上下文
	 * @param mCallback
	 *            回调方法
	 */
	public ListNearShopTask(Activity acti, Callback mCallback) {
		super(acti);
		this.mCallback = mCallback;
	}

	@Override
	protected void onPreExecute() {
		if (mActivity != null && mProcessDialog != null) {
			mProcessDialog.dismiss();
		}
	}

	/**
	 * 调用API
	 */
	@Override
	protected Integer doInBackground(String... params) {
		UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		String userCode = userToken.getUserCode();
		String tokenCode = userToken.getTokenCode();
		LinkedHashMap<String, Object> reqParams = new LinkedHashMap<String, Object>();
		reqParams.put("city", params[0]);
		reqParams.put("longitude", params[1]);
		reqParams.put("latitude", params[2]);
		reqParams.put("page", Integer.parseInt(params[3]));
		reqParams.put("userCode", userCode);
		reqParams.put("tokenCode", tokenCode);
		try {
			mResult = (JSONObject) API.reqCust("listNearShopB", reqParams);
			int retCode = ERROR_RET_CODE;
			// 判断查询的一个对象不为空为空 就返回一个正确的编码
			JSONArray arResult = (JSONArray) mResult.get("shopList");
			if (mResult == null || !"[]".equals(arResult.toString()) || arResult.size() != 0) {
				retCode = RIGHT_RET_CODE; // 1 代表访问成功
			}
			return retCode;
		} catch (SzException e) {
			this.mErrCode = e.getErrCode();
			return this.mErrCode.getCode();
		}
	}

	/**
	 * 业务逻辑代码
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
