// ---------------------------------------------------------
// @author    yanfang.li
// @version   1.0.0
// @createTime 2015.5.22 
// @copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
// ---------------------------------------------------------
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

/**
 * 得到未读消息
 * 
 * @author yanfang.li
 */
public class CountAllTypeMsgTask extends SzAsyncTask<String, Integer, Integer> {

	private final static String TAG = CountAllTypeMsgTask.class.getSimpleName();
	/** 定义一个正确的返回结果码 **/
	private final static int RIGHT_RET_CODE = 1;
	/** 定义一个错误的返回结果码 **/
	private final static int ERROR_RET_CODE = 0;
	private JSONObject mResult;
	private Callback mCallback;

	/**
	 * 无参构造方法
	 * 
	 * @param acti
	 *            调用者的上下文
	 */
	public CountAllTypeMsgTask(Activity acti) {
		super(acti);
	}

	/**
	 * 有参构造方法
	 * 
	 * @param acti
	 *            调用者的上下文
	 * @param callback
	 *            回调方法
	 */
	public CountAllTypeMsgTask(Activity acti, Callback callback) {
		super(acti);
		mCallback = callback;
	}

	/**
	 * 回调方法的接口
	 */
	public interface Callback {
		public void getResult(JSONObject result);
	}

	@Override
	protected void onPreExecute() {
		if (mActivity != null && mProcessDialog != null) {
			mProcessDialog.dismiss();
		}
	}

	@Override
	protected Integer doInBackground(String... params) {

		UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		if (null != userToken) {
		String userCode = userToken.getUserCode();
		String tokenCode = userToken.getTokenCode();

			LinkedHashMap<String, Object> reqparams = new LinkedHashMap<String, Object>();
			reqparams.put("userCode", userCode); // 商店编号
			reqparams.put("tokenCode", tokenCode); // 需要令牌认证
			try {
				// 调用API
				mResult = (JSONObject) API.reqCust("countAllTypeMsg", reqparams);
				Log.d(TAG, "异常=mResult=" + mResult.toString());
				int retCode = ERROR_RET_CODE;
				if (mResult != null || !"".equals(mResult.toString())) {
					retCode = RIGHT_RET_CODE; // 1 代表访问成功
				}
				return retCode;
			} catch (SzException e) {

				this.mErrCode = e.getErrCode();
				Log.d(TAG, "异常=" + mErrCode.getCode());
				return this.mErrCode.getCode();// 返回错误编码
			}
		}
		return ERROR_RET_CODE;
	}

	/**
	 * 处理查询结果的返回值
	 * 
	 * @param retCode执行成功返回码
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
