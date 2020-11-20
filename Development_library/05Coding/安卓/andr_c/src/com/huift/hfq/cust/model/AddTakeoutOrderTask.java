package com.huift.hfq.cust.model;

import java.util.LinkedHashMap;

import net.minidev.json.JSONObject;
import android.app.Activity;
import com.huift.hfq.base.ErrorCode;
import com.huift.hfq.base.SzException;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.api.API;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.model.SzAsyncTask;
import com.huift.hfq.base.pojo.UserToken;
import com.huift.hfq.cust.R;

/**
 * 添加外卖订单地址和备注
 * @author yanfang.li
 * 
 */
public class AddTakeoutOrderTask extends SzAsyncTask<String, Integer, Integer> {

	private JSONObject mResult;
	/** 回调*/
	private Callback callback;
	/** 定义一个错误的返回结果码 **/
	private final static int ERROR_RET_CODE = 0;

	public AddTakeoutOrderTask(Activity acti) {
		super(acti);
	}

	public AddTakeoutOrderTask(Activity acti, Callback callback) {
		super(acti);
		this.callback = callback;
	}

	public interface Callback {
		public void getResult(int result);
	}

	/**
	 * 添加银行卡的异步任务
	 */
	@Override
	protected Integer doInBackground(String... params) {
		UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		String tokenCode = userToken.getTokenCode();
		LinkedHashMap<String, Object> reqparams = new LinkedHashMap<String, Object>();
		reqparams.put("orderCode", params[0]);
		reqparams.put("userAddressId", params[1]);
		reqparams.put("remark", params[2]);
		reqparams.put("tokenCode", tokenCode);

		try {
			int retCode = ERROR_RET_CODE; // 返回值  初始化
			mResult = (JSONObject) API.reqCust("addTakeoutOrderOtherInfo", reqparams);
			if (null != mResult && !"".equals(mResult.toJSONString())) {
				retCode = Integer.parseInt(mResult.get("code").toString());
			}
			return retCode;
		} catch (SzException e) {
			this.mErrCode = e.getErrCode();
			return this.mErrCode.getCode();
		}
	}

	@Override
	protected void handldBuziRet(int retCode) {
		if (callback == null) { return; }
		switch (retCode) {
		case ErrorCode.SUCC: // 成功  50000
			callback.getResult(retCode);
			break;
		case ErrorCode.API_INTERNAL_ERR: // 失败 20000
			Util.getContentValidate(R.string.please_try_again);
			callback.getResult(ERROR_RET_CODE);
			break;
		case ERROR_RET_CODE: // 失败 20000
			Util.getContentValidate(R.string.please_try_again);
			callback.getResult(ERROR_RET_CODE);
			break;
		default:
			break;
		}
	}

}
