package cn.suanzi.baomi.cust.model;

import java.util.LinkedHashMap;

import android.app.Activity;
import android.util.Log;
import cn.suanzi.baomi.base.SzException;
import cn.suanzi.baomi.base.api.API;
import cn.suanzi.baomi.base.data.DB;
import cn.suanzi.baomi.base.model.SzAsyncTask;
import cn.suanzi.baomi.base.pojo.UserToken;

/**
 * 判断是否报名
 * 
 * @author wensi.yu
 * 
 */
public class IsUserJoinActTask extends SzAsyncTask<String, Integer, Integer> {

	private final static String TAG = "IsUserJoinActTask";

	/** 创建一个Boolean对象 */
	private Boolean mResult;
	/** 定义一个正确的返回结果码 */
	private final static int RIGHT_RET_CODE = 1;
	/** 定义一个错误的返回结果码 */
	private final static int ERROR_RET_CODE = 0;
	/** 回调方法 */
	private Callback callback;

	/**
	 * 构造函数
	 * 
	 * @param acti
	 */
	public IsUserJoinActTask(Activity acti) {
		super(acti);

	}

	/**
	 * 回调方法的构造函数
	 * 
	 * @param acti
	 */
	public IsUserJoinActTask(Activity acti, Callback callback) {
		super(acti);
		this.callback = callback;
	}

	/**
	 * 回调方法的接口
	 * 
	 */
	public interface Callback {
		public void getResult(boolean result);
	}

	@Override
	protected void onPreExecute() {
		if (null != mActivity && null != mProcessDialog) {
			mProcessDialog.dismiss();
		}
	}

	/**
	 * 是否报名
	 */
	@Override
	protected Integer doInBackground(String... params) {
		UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		String userCode = userToken.getUserCode();
		String tokenCode = userToken.getTokenCode();
		LinkedHashMap<String, Object> reqparams = new LinkedHashMap<String, Object>();
		reqparams.put("userCode", userCode);
		reqparams.put("actCode", params[0]);
		reqparams.put("tokenCode", tokenCode);

		try {
			mResult = (Boolean) API.reqCust("isUserJoinAct", reqparams);
			Log.d(TAG, "是否已经报过名=============" + mResult);
			int retCode = ERROR_RET_CODE;
			if (mResult == true) {
				retCode = RIGHT_RET_CODE;
			}
			return retCode;
		} catch (SzException e) {
			this.mErrCode = e.getErrCode();
			return this.mErrCode.getCode();
		}
	}

	/**
	 * 业务逻辑处理
	 */
	@Override
	protected void handldBuziRet(int retCode) {
		if (retCode == RIGHT_RET_CODE) {
			callback.getResult(true);
		} else {
			if (retCode == ERROR_RET_CODE) {
				callback.getResult(false);
			}
		}
	}
}
