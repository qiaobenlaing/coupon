package com.huift.hfq.shop.model;

import java.util.LinkedHashMap;

import com.huift.hfq.base.ErrorCode;
import com.huift.hfq.base.SzException;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.api.API;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.model.SzAsyncTask;
import com.huift.hfq.base.pojo.UserToken;

import net.minidev.json.JSONObject;
import android.app.Activity;

/**
 * 取消订单
 * 
 * @author qian.zhou
 */
public class CancelOrderTask extends SzAsyncTask<String, Integer, Integer> {
	/** 创建一个JSONObject对象 **/
	private JSONObject mResult;

	/**
	 * 构造函数
	 * 
	 * @param acti
	 */
	public CancelOrderTask(Activity acti) {
		super(acti);
	}

	/**
	 * 回调方法
	 */
	private Callback callback;

	/**
	 * @param acti
	 *            上下文
	 * @param callback
	 *            回调方法
	 */
	public CancelOrderTask(Activity acti, Callback callback) {
		super(acti);
		this.callback = callback;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (mProcessDialog != null) {
			mProcessDialog.dismiss();
		}
	}

	@Override
	protected void handldBuziRet(int retCode) {
		if (retCode == ErrorCode.SUCC) {
			Util.showToastZH("取消交易");
		} else {
			Util.showToastZH("取消交易失败,请客户手动取消交易");
		}
	}

	/**
	 * 回调方法的接口
	 */
	public interface Callback {
		public void onSuccess();

		public void onError();
	}

	@Override
	protected Integer doInBackground(String... params) {
		LinkedHashMap<String, Object> reqParams = new LinkedHashMap<String, Object>();
		UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		String tokenCode = userToken.getTokenCode();
		reqParams.put("consumeCode", params[0]);
		reqParams.put("tokenCode", tokenCode);
		try {
			int retCode = 0;
			int flag = 0;
			// 调用API
			while (true) {
				mResult = (JSONObject) API.reqComm("cancelPay", reqParams);
				retCode = Integer.parseInt(mResult.get("code").toString());
				if (retCode == ErrorCode.SUCC) {
					break;
				} else {
					flag++;
					if (flag > 5) {
						break;
					}
					continue;
				}
			}
			return retCode;
		} catch (SzException e) {
			this.mErrCode = e.getErrCode();
			return this.mErrCode.getCode();
		}
	}
}
