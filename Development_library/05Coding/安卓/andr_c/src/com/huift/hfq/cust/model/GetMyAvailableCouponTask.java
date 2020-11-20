//---------------------------------------------------------
//@author    yanfang.li
//@version   1.0.0
//@createTime 2015.6.2
//@copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
//---------------------------------------------------------
package com.huift.hfq.cust.model;

import java.util.LinkedHashMap;

import net.minidev.json.JSONObject;
import android.app.Activity;
import com.huift.hfq.base.SzException;
import com.huift.hfq.base.api.API;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.model.SzAsyncTask;
import com.huift.hfq.base.pojo.UserToken;

/**
 * 获取用户已领的优惠券列表
 * 
 * @author yanfang.li
 */
public class GetMyAvailableCouponTask extends SzAsyncTask<String, String, Integer> {

	private final static String TAG = "GetMyAvailableCouponTask";
	/** 创建一个JSONObject对象 **/
	private JSONObject mResult;
	/** 定义一个正确的返回结果码 **/
	private final static int RIGHT_RET_CODE = 1;
	/** 定义一个错误的返回结果码 **/
	private final static int ERROR_RET_CODE = 0;
	/** 回调方法 **/
	private Callback mCallback;

	/**
	 * 回调方法的接口
	 */
	public interface Callback {
		public void getResult(JSONObject result);
	}

	/**
	 * 构造函数
	 * 
	 * @param acti
	 */
	public GetMyAvailableCouponTask(Activity acti) {
		super(acti);
	}

	/**
	 * @param acti
	 *            上下文
	 * @param mCallback
	 *            回调方法
	 */
	public GetMyAvailableCouponTask(Activity acti, Callback mCallback) {
		super(acti);
		this.mCallback = mCallback;
	}

	@Override
	protected void onPreExecute() {
		if (mActivity != null && null != mProcessDialog) {
			mProcessDialog.dismiss();
		}
	}

	/**
	 * 调用API
	 * 
	 * @param params
	 *            [0] 用户编码
	 * @param params
	 *            [1] 商家编码
	 * @param params
	 *            [2] 优惠券的状态
	 * @param params
	 *            [3] 页面
	 * @param params
	 *            [4] 需要令牌认证
	 */
	@Override
	protected Integer doInBackground(String... params) {
		LinkedHashMap<String, Object> reqParams = new LinkedHashMap<String, Object>();
		UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		String userCode = userToken.getUserCode();
		String tokenCode = userToken.getTokenCode();
		reqParams.put("userCode", userCode);
		reqParams.put("shopCode", "");
		reqParams.put("status", Integer.parseInt(params[0]));
		reqParams.put("page", Integer.parseInt(params[1]));
		reqParams.put("longitude", params[2]);
		reqParams.put("latitude", params[3]);
		reqParams.put("tokenCode", tokenCode);
		try {
			int retCode = ERROR_RET_CODE;
			mResult = (JSONObject) API.reqCust("listUserCouponByStatus", reqParams);
			// 判断查询的一个对象不为空为空 就返回一个正确的编码
			if (!(mResult == null || "".equals(mResult.toJSONString()))) {
				retCode = RIGHT_RET_CODE; // 1 代表访问成功
			}
			return retCode;
		} catch (SzException e) {
			this.mErrCode = e.getErrCode();
			return this.mErrCode.getCode();
		}
	}

	/**
	 * 业务逻辑代码
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
