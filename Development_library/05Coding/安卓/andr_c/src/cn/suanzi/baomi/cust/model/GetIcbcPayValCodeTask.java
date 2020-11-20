package cn.suanzi.baomi.cust.model;

import java.util.LinkedHashMap;

import net.minidev.json.JSONObject;
import android.app.Activity;
import android.util.Log;
import android.widget.Toast;
import cn.suanzi.baomi.base.SzException;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.api.API;
import cn.suanzi.baomi.base.data.DB;
import cn.suanzi.baomi.base.model.SzAsyncTask;
import cn.suanzi.baomi.base.pojo.UserToken;

public class GetIcbcPayValCodeTask extends SzAsyncTask<String, Integer, Integer> {
	private final static String TAG = GetIcbcPayValCodeTask.class.getSimpleName();
	/** 定义一个正确的返回结果码 **/
	private final static int RIGHT_RET_CODE = 50000;
	/** 定义一个错误的返回结果码 **/
	private final static int ERROR_RET_CODE = 20000;
	/** 定义银行账户为空的结果码 */
	private final static int ERROR_EMPTY_ACCOUNT = 50056;
	/** 定义银行账户编码错误的结果码 */
	private final static int ERROR_BANK_ACCOUNT = 50057;
	/** 定义支付订单不存在的结果码 */
	private final static int ERROR_ORDER_NOTEXSIT = 50900;
	/** 定义预留手机号错误的结果码 */
	private final static int ERROR_MOBILE_NUMBER = 60005;

	private final static int BANK_ERROR_CODE = -1;
	private final static int ERROR_CODE = 0;
	/** 调用API返回对象 **/
	private JSONObject mResult;

	/** 回调方法 **/
	private Callback mCallback;

	/** 获得一个用户信息对象 **/
	private UserToken mUserToken;
	/** 用户登录后获得的令牌 **/
	private String mTokenCode;

	private String valCode = "";

	private String errormsg = "";

	public GetIcbcPayValCodeTask(Activity acti, Callback callback) {
		super(acti);
		this.mCallback = callback;
	}

	@Override
	protected void onPreExecute() {
		if (mActivity != null && mProcessDialog != null) {
			mProcessDialog.dismiss();
		}
	}

	@Override
	protected void handldBuziRet(int retCode) {
		switch (retCode) {
		case RIGHT_RET_CODE:
			mCallback.getResult(valCode);
			break;
		case ERROR_RET_CODE:
			mCallback.getResult(null);
			break;
		case ERROR_EMPTY_ACCOUNT:
			Toast.makeText(mActivity, "银行账户为空", Toast.LENGTH_SHORT).show();
			mCallback.getResult(null);
			break;
		case ERROR_BANK_ACCOUNT:
			Toast.makeText(mActivity, "银行账户编码错误", Toast.LENGTH_SHORT).show();
			mCallback.getResult(null);
			break;
		case ERROR_ORDER_NOTEXSIT:
			Toast.makeText(mActivity, "支付订单不存在", Toast.LENGTH_SHORT).show();
			mCallback.getResult(null);
			break;
		case ERROR_MOBILE_NUMBER:
			Toast.makeText(mActivity, "预留手机号错误", Toast.LENGTH_SHORT).show();
			mCallback.getResult(null);
			break;
		case BANK_ERROR_CODE:
			Toast.makeText(mActivity, errormsg, Toast.LENGTH_SHORT).show();
			mCallback.getResult(null);
			break;

		default:
			mCallback.getResult(null);
			break;
		}
	}

	@Override
	protected Integer doInBackground(String... params) {
		mUserToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		mTokenCode = mUserToken.getTokenCode();// 用户登录后获得的令牌
		LinkedHashMap<String, Object> reqparams = new LinkedHashMap<String, Object>();
		reqparams.put("consumeCode", params[0]);
		reqparams.put("bankAccountCode", params[1]);
		reqparams.put("mobileNbr", params[2]);
		reqparams.put("tokenCode", mTokenCode);

		try {
			// 调用API
			mResult = (JSONObject) API.reqCust("getIcbcPayValCode", reqparams);
			int retCode = ERROR_RET_CODE;
			if (mResult != null) {
				String code = mResult.get("code").toString();
				if (null == code || Util.isEmpty(code)) {
					retCode = ERROR_CODE;

				} else {
					if ("50000".equals(code) || "20000".equals(code) || "50056".equals(code) || "50057".equals(code)
							|| "50900".equals(code) || "60005".equals(code)) {
						retCode = Integer.parseInt(code);
						/*
						 * if(retCode == RIGHT_RET_CODE){
						 * if(mResult.get("valCode")==null){ return
						 * ERROR_RET_CODE; } valCode =
						 * mResult.get("valCode").toString(); Log.e("yingchen",
						 * "retCode=="+retCode+"---valCode=="+valCode); }
						 */
					} else {
						errormsg = mResult.get("retmsg").toString();
						retCode = BANK_ERROR_CODE;
					}
				}
			}
			return retCode;
		} catch (SzException e) {
			this.mErrCode = e.getErrCode();
			Log.d(TAG, "errorcode=" + this.mErrCode.getCode());
			return this.mErrCode.getCode();// 返回错误编码
		}
	}

	/**
	 * 回调方法的接口
	 * 
	 */
	public interface Callback {
		/**
		 * 传递参数
		 * 
		 * @param result
		 *            是异步请求的结果
		 */
		public void getResult(String result);
	}
}
