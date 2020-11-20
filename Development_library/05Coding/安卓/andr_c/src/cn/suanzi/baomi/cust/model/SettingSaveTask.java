package cn.suanzi.baomi.cust.model;

import java.util.LinkedHashMap;

import net.minidev.json.JSONObject;
import android.app.Activity;
import android.widget.Toast;
import cn.suanzi.baomi.base.ErrorCode;
import cn.suanzi.baomi.base.SzException;
import cn.suanzi.baomi.base.api.API;
import cn.suanzi.baomi.base.data.DB;
import cn.suanzi.baomi.base.model.SzAsyncTask;
import cn.suanzi.baomi.base.pojo.UserToken;

public class SettingSaveTask extends SzAsyncTask<String, Integer, Integer> {
	private final static String TAG = SettingTask.class.getSimpleName();
	/** 定义一个正确的返回结果码 **/
	private final static int RIGHT_RET_CODE = 1;
	/** 定义一个错误的返回结果码 **/
	private final static int ERROR_RET_CODE = 0;

	/** 调用API返回对象 **/
	private JSONObject mResult;

	/** 回调方法 **/
	private SaveCallback mCallback;

	/** 获得一个用户信息对象 **/
	private UserToken mUserToken;
	/** 用户登录后获得的令牌 **/
	private String mTokenCode;

	public SettingSaveTask(Activity acti, SaveCallback callback) {
		super(acti);
		this.mCallback = callback;
	}

	@Override
	protected void handldBuziRet(int retCode) {
		// RIGHT_RET_CODE的值是1 如果retCode访问成功的发 返回的是1
		if (retCode == RIGHT_RET_CODE) {
			mCallback.onSuccess(mResult);
		} else {
			mCallback.onError(retCode);
			if (retCode == ERROR_RET_CODE) {
				Toast.makeText(this.mActivity, "服务器异常" + ErrorCode.getMsg(retCode), Toast.LENGTH_SHORT).show();
			}
		}
	}

	@Override
	protected Integer doInBackground(String... params) {
		mUserToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		mTokenCode = mUserToken.getTokenCode();// 用户登录后获得的令牌
		LinkedHashMap<String, Object> reqparams = new LinkedHashMap<String, Object>();
		reqparams.put("isBroadcastOn", params[0]);
		reqparams.put("isMsgBingOn", params[1]);
		reqparams.put("isCouponMsgOn", params[2]);
		reqparams.put("userCode", mUserToken.getUserCode());
		reqparams.put("tokenCode", mTokenCode);

		try {
			// 调用API
			mResult = (JSONObject) API.reqCust("updateUserSetting", reqparams);
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
	public interface SaveCallback {
		/**
		 * 传递参数
		 * 
		 * @param result
		 *            是异步请求的结果
		 */
		public void onSuccess(JSONObject result);

		/**
		 * 传递参数
		 * 
		 * @param code
		 *            错误码
		 */
		public void onError(int code);
	}
}
