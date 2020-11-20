package cn.suanzi.baomi.cust.model;

import java.util.LinkedHashMap;

import net.minidev.json.JSONArray;
import android.app.Activity;
import cn.suanzi.baomi.base.ErrorCode;
import cn.suanzi.baomi.base.SzException;
import cn.suanzi.baomi.base.api.API;
import cn.suanzi.baomi.base.model.SzAsyncTask;

/**
 * 获取滚动信息
 * 
 * @author yanfang.li
 */
public class GetHomeTabListTask extends SzAsyncTask<String, Integer, Integer> {
	private final static String TAG = GetHomeTabListTask.class.getSimpleName();

	/** 调用API返回对象 **/
	private JSONArray mResult;

	/** 回调方法 **/
	private Callback mCallback;

	public GetHomeTabListTask(Activity acti, Callback callback) {
		super(acti);
		this.mCallback = callback;
	}

	/**
	 * 回调方法的接口
	 * 
	 */
	public interface Callback {
		public void getResult(JSONArray result);
	}

	@Override
	protected void onPreExecute() {
		if (mActivity != null && mProcessDialog != null) {
			mProcessDialog.dismiss();
		}
	}

	@Override
	protected Integer doInBackground(String... params) {
		try {
//			LinkedHashMap<String, Object> reqparams = new LinkedHashMap<String, Object>();
//			reqparams.put("city", params[0]);
			// 调用API
			mResult = (JSONArray) API.reqCust("getHomeTabList", null);
			int retCode = ErrorCode.ERROR_RET_CODE;
			// 判断查询的一个对象不为空为空 就返回一个正确的编码
			if (mResult.size() != 0 || !"[]".equals(mResult.toString())) {
				retCode = ErrorCode.RIGHT_RET_CODE; // 1 代表访问成功
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
		if (retCode == ErrorCode.RIGHT_RET_CODE) {
			mCallback.getResult(mResult);
		} else {
			mCallback.getResult(null);
		}
	}
}
