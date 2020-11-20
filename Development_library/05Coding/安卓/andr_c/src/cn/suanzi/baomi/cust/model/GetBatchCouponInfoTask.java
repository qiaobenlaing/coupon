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
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.api.API;
import cn.suanzi.baomi.base.data.DB;
import cn.suanzi.baomi.base.model.SzAsyncTask;
import cn.suanzi.baomi.base.pojo.UserToken;

/**
 * 同一批次的优惠券详情
 * 
 * @author yanfang.li
 */
public class GetBatchCouponInfoTask extends SzAsyncTask<String, Integer, Integer> {

	private final static String TAG = GetBatchCouponInfoTask.class.getSimpleName();

	private JSONObject mResult;
	private Callback mCallback;
	/** 定义一个正确的返回结果码 **/
	private final static int RIGHT_RET_CODE = 1;
	/** 定义一个错误的返回结果码 **/
	private final static int ERROR_RET_CODE = 0;

	/**
	 * 无参构造方法
	 * 
	 * @param acti
	 *            调用者的上下文
	 */
	public GetBatchCouponInfoTask(Activity acti) {
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
	public GetBatchCouponInfoTask(Activity acti, Callback callback) {
		super(acti);
		mCallback = callback;
	}

	/**
	 * 回调方法的接口
	 * 
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

	/**
	 * 调用API查询，添加会员卡
	 */
	@Override
	protected Integer doInBackground(String... params) {
		String tokenCode = "";
		String userCode = "";
		if (DB.getBoolean(DB.Key.CUST_LOGIN)) { // 登录
			UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
			tokenCode = userToken.getTokenCode();
			userCode = userToken.getUserCode();
		}
		LinkedHashMap<String, Object> reqparams = new LinkedHashMap<String, Object>();
		reqparams.put("batchCouponCode", params[0]);
		reqparams.put("longitude", params[1]);
		reqparams.put("latitude", params[2]);
		try {
			if (Util.isEmpty(userCode)) {
				Log.d(TAG, "getUserCouponInfo  >>> null");
				// 不需要token验证
				mResult = (JSONObject) API.reqCust("getBatchCouponInfo", reqparams);
			} else {
				Log.d(TAG, "getUserCouponInfo  >>> not null");
				// 需要token验证
				reqparams.put("userCode", userCode);
				reqparams.put("tokenCode", tokenCode);
				mResult = (JSONObject) API.reqCust("getBatchCouponInfoHasUser", reqparams);
			}
			int retCode = ERROR_RET_CODE;
			if (null != mResult || !"".equals(mResult.toString())) {
				retCode = RIGHT_RET_CODE;
			}
			return retCode;
		} catch (SzException e) {

			this.mErrCode = e.getErrCode();
			return this.mErrCode.getCode();// 返回错误编码
		}

	}

	/**
	 * 处理查询结果的返回值
	 * 
	 * @param retCode执行成功返回码
	 */
	@Override
	protected void handldBuziRet(int retCode) {
		// RIGHT_RET_CODE的值是1 如果retCode访问成功的发 返回的是1
		if (retCode == RIGHT_RET_CODE) {
			mCallback.getResult(mResult);
		} else {
			mCallback.getResult(null);
		}
	}

}
