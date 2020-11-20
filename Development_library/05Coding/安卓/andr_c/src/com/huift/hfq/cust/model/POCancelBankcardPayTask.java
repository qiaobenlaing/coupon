package com.huift.hfq.cust.model;

import java.util.LinkedHashMap;

import net.minidev.json.JSONObject;
import android.app.Activity;
import android.util.Log;
import android.widget.Toast;
import com.huift.hfq.base.SzException;
import com.huift.hfq.base.api.API;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.model.SzAsyncTask;
import com.huift.hfq.base.pojo.UserToken;

public class POCancelBankcardPayTask extends SzAsyncTask<String, Integer, Integer> {
	
	private final static String TAG = POCancelBankcardPayTask.class.getSimpleName();
	// 定义返回成功的结果码
	private static final int RIGHT_RET_CODE = 50000;
	// 定义返回失败的结果码
	private static final int ERROR_RET_CODE = 20000;
	// 定义支付已经取消的结果码
	private static final int ERROR_PAY_CANCLE_ALREADY = 50403;

	private JSONObject mResult;

	private Callback mCallback;



	public interface Callback {
		public void getResult(boolean result);
	}

	public POCancelBankcardPayTask(Activity acti, Callback mCallback) {
		super(acti);
		this.mCallback = mCallback;
	}

	public POCancelBankcardPayTask(Activity acti) {
		super(acti);
	}

	@Override
	protected Integer doInBackground(String... params) {
		// 获得一个用户信息对象
		UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		String tokenCode = userToken.getTokenCode();// 需要令牌认证
		
		LinkedHashMap<String, Object> reqparams = new LinkedHashMap<String, Object>();
		reqparams.put("consumeCode", params[0]);
		reqparams.put("tokenCode", tokenCode);

		// 调用API
		try {
			mResult = (JSONObject) API.reqCust("pOCancelBankcardPay", reqparams);
			int retCode = ERROR_RET_CODE;
			if (mResult != null) {
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
		Log.d(TAG, "retCode >>>" +retCode);
		switch (retCode) {
		case RIGHT_RET_CODE:
			mCallback.getResult(true);
			break;
		case ERROR_RET_CODE:
			Toast.makeText(mActivity, "失败", Toast.LENGTH_SHORT).show();
			mCallback.getResult(false);
			break;
		case ERROR_PAY_CANCLE_ALREADY:
			Toast.makeText(mActivity, "该支付已经取消", Toast.LENGTH_SHORT).show();
			mCallback.getResult(false);
			break;
		default:
			break;
		}
	}

}
