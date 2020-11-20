//---------------------------------------------------------
//@author    yanfang.li
//@version   1.0.0
//@copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
//---------------------------------------------------------
package cn.suanzi.baomi.cust.model;

import java.util.LinkedHashMap;

import net.minidev.json.JSONObject;
import android.app.Activity;
import cn.suanzi.baomi.base.SzException;
import cn.suanzi.baomi.base.api.API;
import cn.suanzi.baomi.base.data.DB;
import cn.suanzi.baomi.base.model.SzAsyncTask;
import cn.suanzi.baomi.base.pojo.UserToken;

/**
 * 获得顾客还未使用的且有效的优惠券数量
 * 
 * @author yanfang.li
 */
public class CountUserNotUsedCouponTask extends SzAsyncTask<String, Integer, Integer> {
	private final static String TAG = "CountUserNotUsedCouponTask";
	/** 定义一个正确的返回结果码 **/
	private final static int RIGHT_RET_CODE = 1;
	/** 定义一个错误的返回结果码 **/
	private final static int ERROR_RET_CODE = 0;

	/** 调用API返回对象 **/
	private JSONObject mResult;

	/** 回调方法 **/
	private Callback mCallback;

	public CountUserNotUsedCouponTask(Activity acti, Callback callback) {
		super(acti);
		this.mCallback = callback;
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

	@Override
	protected Integer doInBackground(String... params) {
		// 获得一个用户信息对象
		UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		String userCode = userToken.getUserCode();
		String tokenCode = userToken.getTokenCode();

		LinkedHashMap<String, Object> reqparams = new LinkedHashMap<String, Object>();
		reqparams.put("userCode", userCode);
		reqparams.put("tokenCode", tokenCode);

		try {
			// 调用API
			mResult = (JSONObject) API.reqCust("countUserNotUsedCoupon", reqparams);
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
