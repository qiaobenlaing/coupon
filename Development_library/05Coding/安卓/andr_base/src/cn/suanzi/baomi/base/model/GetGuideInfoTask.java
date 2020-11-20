package cn.suanzi.baomi.base.model;

import java.util.LinkedHashMap;

import net.minidev.json.JSONObject;
import android.app.Activity;
import android.util.Log;
import cn.suanzi.baomi.base.ErrorCode;
import cn.suanzi.baomi.base.SzException;
import cn.suanzi.baomi.base.api.API;
import cn.suanzi.baomi.base.model.SzAsyncTask;

/**
 * 获得欢迎页图片
 * 
 * @author qian.zhou
 */
public class GetGuideInfoTask extends SzAsyncTask<String, String, Integer> {

	private final static String TAG = GetGuideInfoTask.class.getSimpleName();
	
	/** b端*/
	public final static String ANDR_B = "0";
	/** c端*/
	public final static String ANDR_C = "1";

	/*** 创建一个JSONObject对象 **/
	private JSONObject mResult;
	/** 回调方法 **/
	private Callback callback;

	/**
	 * 构造函数
	 * 
	 * @param acti
	 */
	public GetGuideInfoTask(Activity acti) {
		super(acti);
	}

	/**
	 * 回调方法的构造函数
	 * 
	 * @param acti
	 */
	public GetGuideInfoTask(Activity acti, Callback callback) {
		super(acti);
		this.callback = callback;
	}

	@Override
	protected void onPreExecute() {
		if (mActivity != null && mProcessDialog != null) {
			mProcessDialog.dismiss();
		}
	}

	/**
	 * 回调方法的接口
	 * 
	 */
	public interface Callback {
		public void getResult(JSONObject mResult);
	}

	/**
	 * 欢迎页图片
	 */
	@Override
	protected Integer doInBackground(String... params) {
		LinkedHashMap<String, Object> reqParams = new LinkedHashMap<String, Object>();
		reqParams.put("appType", params[0]);
		try {
			int retCode = ErrorCode.ERROR_RET_CODE;
			mResult = (JSONObject) API.reqComm("getGuideInfo", reqParams);
			Log.i(TAG, "mResult===============" + mResult);
			// 判断查询的一个对象不为空为空 就返回一个正确的编码
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
		if (retCode == ErrorCode.RIGHT_RET_CODE) {
			callback.getResult(mResult);
		} else {
			callback.getResult(null);
		}
	}
}
