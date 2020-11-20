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
 * 查询完成订单的商家列表
 * 
 * @author qian.zhou
 */
public class MyOrderManagerTask extends SzAsyncTask<String, String, Integer> {
	/** 创建一个JSONObject对象 **/
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
		public void getResult(JSONObject JSONobject);
	}

	/**
	 * 构造函数
	 * 
	 * @param acti启动本对象的Activity
	 */
	public MyOrderManagerTask(Activity acti) {
		super(acti);
	}

	/**
	 * @param acti
	 *            上下文
	 * @param callback
	 *            回调方法
	 */
	public MyOrderManagerTask(Activity acti, Callback callback) {
		super(acti);
		this.mCallback = callback;
	}

	@Override
	protected void onPreExecute() {
		if (mActivity != null && mProcessDialog != null) {
			mProcessDialog.dismiss();
		}
	}

	/**
	 * onPostExecute()中的正常业务逻辑处理.
	 */
	@Override
	protected void handldBuziRet(int retCode) {
		if (retCode == RIGHT_RET_CODE) {
			mCallback.getResult(mResult);
		} else if (retCode == ERROR_RET_CODE) {
			mCallback.getResult(null);
		}
	}

	/**
	 * 调用API
	 */
	@Override
	protected Integer doInBackground(String... params) {
		// 获得一个用户信息对象
		UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		LinkedHashMap<String, Object> reqParams = new LinkedHashMap<String, Object>();
		reqParams.put("userCode", userToken.getUserCode());
		reqParams.put("isFinish", params[0]);
		reqParams.put("page", Integer.parseInt(params[1]));
		reqParams.put("tokenCode", userToken.getTokenCode());
		try {
			mResult = (JSONObject) API.reqCust("getUserOrderList", reqParams);
			int retCode = ERROR_RET_CODE;
			// 判断查询的一个对象不为空为空 就返回一个正确的编码
			JSONArray arResult = (JSONArray) mResult.get("orderList");
			if (mResult == null || !"[]".equals(arResult.toString()) || arResult.size() != 0) {
				retCode = RIGHT_RET_CODE; // 1 代表访问成功
			}
			return retCode;
		} catch (SzException e) {
			this.mErrCode = e.getErrCode();
			return this.mErrCode.getCode();
		}
	}
}
