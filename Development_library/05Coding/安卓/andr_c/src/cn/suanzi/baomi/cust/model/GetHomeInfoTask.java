package cn.suanzi.baomi.cust.model;

import java.util.LinkedHashMap;

import net.minidev.json.JSONArray;
import android.app.Activity;
import cn.suanzi.baomi.base.SzException;
import cn.suanzi.baomi.base.api.API;
import cn.suanzi.baomi.base.model.SzAsyncTask;

/**
 * 首页API
 * 
 * @author yanfang.li
 */
public class GetHomeInfoTask extends SzAsyncTask<String, String, Integer> {
	private final static String TAG = GetHomeInfoTask.class.getSimpleName();
	/** 定义一个正确的返回结果码 **/
	private final static int RIGHT_RET_CODE = 1;
	/** 定义一个错误的返回结果码 **/
	private final static int ERROR_RET_CODE = 0;
	/** 创建一个JSONArray对象 **/
	private JSONArray mResult;
	// 回调方法
	private Callback mCallback;

	/**
	 * 构造函数
	 * 
	 * @param acti
	 */
	public GetHomeInfoTask(Activity acti) {
		super(acti);
	}

	/**
	 * 构造函数
	 * 
	 * @param acti
	 */
	public GetHomeInfoTask(Activity acti, Callback callback) {
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
		public void getResult(JSONArray result);
	}

	/**
	 * 调用API
	 */
	@Override
	protected Integer doInBackground(String... params) {
		LinkedHashMap<String, Object> reqParams = new LinkedHashMap<String, Object>();
		reqParams.put("city", params[0]);
		try {
			mResult = (JSONArray) API.reqCust("getHomeInfo", reqParams);
			int retCode = ERROR_RET_CODE;
			if (mResult.size() > 0 && !"[]".equals(mResult.toString())) {
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
