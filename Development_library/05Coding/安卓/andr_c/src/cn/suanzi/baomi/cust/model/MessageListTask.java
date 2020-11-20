package cn.suanzi.baomi.cust.model;

import java.util.LinkedHashMap;

import net.minidev.json.JSONObject;
import android.app.Activity;
import android.util.Log;
import cn.suanzi.baomi.base.SzException;
import cn.suanzi.baomi.base.api.API;
import cn.suanzi.baomi.base.data.DB;
import cn.suanzi.baomi.base.model.SzAsyncTask;
import cn.suanzi.baomi.base.pojo.UserToken;

public class MessageListTask extends SzAsyncTask<String, Integer, Integer> {
	private final static String TAG = "ShopListTask";
	/** 定义一个正确的返回结果码 **/
	private final static int RIGHT_RET_CODE = 1;
	/** 定义一个错误的返回结果码 **/
	private final static int ERROR_RET_CODE = 0;

	/** 调用API返回对象 **/
	private JSONObject mResult;

	/** 回调方法 **/
	private Callback mCallback;

	/** 获得一个用户信息对象 **/
	private UserToken mUserToken;

	/** 用户登录后获得的令牌 **/
	private String mTokenCode;

	public MessageListTask(Activity acti, Callback callback) {
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
		mUserToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		mTokenCode = mUserToken.getTokenCode();// 用户登录后获得的令牌
		LinkedHashMap<String, Object> reqparams = new LinkedHashMap<String, Object>();
		reqparams.put("userCode", mUserToken.getUserCode());
		reqparams.put("type", params[0]);
		reqparams.put("page", params[1]);
		reqparams.put("tokenCode", mTokenCode);

		try {
			// 调用API
			mResult = (JSONObject) API.reqCust("getMessageList", reqparams);
			Log.d(TAG, "mResult=" + mResult.toString());
			int retCode = ERROR_RET_CODE;
			// 判断查询的一个对象不为空为空 就返回一个正确的编码
			if (mResult != null || !"".equals(mResult.toJSONString())) {
				retCode = RIGHT_RET_CODE; // 1 代表访问成功
			}
			return retCode;
		} catch (SzException e) {
			Log.e("===MessageListTask===",
					"e.getLocalizedMessage: " + e.getLocalizedMessage() + " e.getMessage" + e.getMessage());
			e.printStackTrace();
			this.mErrCode = e.getErrCode();
			int code = this.mErrCode.getCode();
			Log.d(TAG, "e.getErrCode()=" + e.getErrCode());
			return code;// 返回错误编码
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
		 * @param mResult
		 *            是异步请求的结果
		 */
		public void getResult(JSONObject mResult);
	}

}
