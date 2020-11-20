package cn.suanzi.baomi.cust.model;

import java.util.LinkedHashMap;

import net.minidev.json.JSONObject;
import android.app.Activity;
import android.util.Log;
import cn.suanzi.baomi.base.SzException;
import cn.suanzi.baomi.base.api.API;
import cn.suanzi.baomi.base.model.SzAsyncTask;

/**
 * 店铺详情搜索关键字
 * 
 * @author yanfang.li
 * 
 */
public class ListSearchWordsTask extends SzAsyncTask<String, Integer, Integer> {
	private final static String TAG = ListSearchWordsTask.class.getSimpleName();
	/** 定义一个正确的返回结果码 **/
	private final static int RIGHT_RET_CODE = 1;
	/** 定义一个错误的返回结果码 **/
	private final static int ERROR_RET_CODE = 0;
	/** 调用API返回对象 **/
	private JSONObject mResult;
	/** 回调方法 **/
	private Callback mCallback;

	public ListSearchWordsTask(Activity acti, Callback callback) {
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
	 * 回调方法的接口
	 * 
	 */
	public interface Callback {
		// 传递参数
		// 是异步请求的结果
		public void getResult(JSONObject result);
	}

	@Override
	protected void handldBuziRet(int retCode) {
		// RIGHT_RET_CODE的值是1 如果retCode访问成功的发 返回的是1
		if (retCode == RIGHT_RET_CODE) {
			mCallback.getResult(mResult);
		} else if (retCode == ERROR_RET_CODE) {
			mCallback.getResult(null);
		}
	}

	@Override
	protected Integer doInBackground(String... params) {
		LinkedHashMap<String, Object> reqparams = new LinkedHashMap<String, Object>();
		reqparams.put("city", params[0]);
		try {
			// 调用API
			mResult = (JSONObject) API.reqCust("listSearchWords", reqparams);
			int retCode = ERROR_RET_CODE;
			// 判断查询的一个对象不为空为空 就返回一个正确的编码
			if (!(mResult == null || "[]".equals(mResult.toJSONString()))) {
				retCode = RIGHT_RET_CODE; // 1 代表访问成功
			}
			return retCode;
		} catch (SzException e) {
			this.mErrCode = e.getErrCode();
			Log.d(TAG, "mErrCode  >>> " + mErrCode);
			return this.mErrCode.getCode();// 返回错误编码
		}
	}

}
