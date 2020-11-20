// ---------------------------------------------------------
// @author    yanfang.li
// @version   1.0.0
// @copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
// ---------------------------------------------------------
package com.huift.hfq.shop.model;

import java.util.LinkedHashMap;

import com.huift.hfq.base.SzException;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.api.API;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.model.SzAsyncTask;
import com.huift.hfq.base.pojo.UserToken;

import net.minidev.json.JSONObject;
import android.app.Activity;
import com.huift.hfq.shop.R;

/**
 * 停用启用优惠券
 * 
 * @author yanfang.li
 */
public class ChangeCouponStatusTask extends SzAsyncTask<String, Integer, Integer> {

	private final static String TAG = ChangeCouponStatusTask.class.getSimpleName();
	/** 定义一个正确的返回结果码 **/
	private final static int RIGHT_RET_CODE = 1;
	/** 定义一个错误的返回结果码 **/
	private final static int ERROR_RET_CODE = 0;

	/** 创建一个jSONObject对象 **/
	private JSONObject mResult;

	/** 回调方法 **/
	private Callback callback;
	private int mStatus = 0;

	/**
	 * 构造方法
	 * 
	 * @param acti
	 */
	public ChangeCouponStatusTask(Activity acti) {
		super(acti);
	}

	/**
	 * 惨构造方法
	 * 
	 * @param acti
	 *            上下文
	 * @param callback
	 *            回调方法
	 */
	public ChangeCouponStatusTask(Activity acti, Callback callback) {
		super(acti);
		this.callback = callback;
	}

	/**
	 * 访问服务器的方法，调用API的入口 params[0] shopCode 是商家编码
	 */
	@Override
	protected Integer doInBackground(String... params) {
		try {
			LinkedHashMap<String, Object> reqParams = new LinkedHashMap<String, Object>();
			UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
			String tokenCode = userToken.getTokenCode();
			reqParams.put("batchCouponCode", params[0]);
			reqParams.put("isAvailable", params[1]);
			reqParams.put("tokenCode", tokenCode);
			
			mStatus = Integer.parseInt( params[1]);
			// 调用API
			mResult = (JSONObject) API.reqShop("changeCouponStatus", reqParams);

			int retCode = ERROR_RET_CODE;// 定义doInBackground返回结果码
			// 查询所有商家信息不为空的话 就是查询成功
			if (!(mResult == null || "".equals(mResult.toJSONString()))) {

				retCode = RIGHT_RET_CODE;// 1 就代表查询有数据，请求api成功
			}
			return retCode;
		} catch (SzException e) {
			this.mErrCode = e.getErrCode();
			return this.mErrCode.getCode();
		}
	}

	/**
	 * 回调方法的接口
	 * 
	 */
	public interface Callback {
		public void getResult(JSONObject result);
	}

	/**
	 * 处理正常的业务逻辑
	 */
	@Override
	protected void handldBuziRet(int retCode) {
		if (retCode == RIGHT_RET_CODE) {
			callback.getResult(mResult);// 调用自己编写的回调方法
			if (mStatus == 0) { // 启用
				Util.getContentValidate(R.string.tv_actmoney_usermoney);
			} else if (mStatus == 1) { // 停用
				Util.getContentValidate(R.string.tv_actmoney_enablemoney);
			}
		} else {
			callback.getResult(null);// 调用自己编写的回调方法
		}

	}

}
