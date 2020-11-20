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

public class GetSignCardValCode extends SzAsyncTask<String, Integer, Integer> {
	private final static String TAG = "GetSignCardValCode";

	/** 定义一个正确的返回结果码 **/
	private final static int RIGHT_RET_CODE = 50000;
	/** 定义一个错误的返回结果码 **/
	private final static int ERROR_RET_CODE = 20000;
	/** 定义请求短信验证码报错 可能是填写信息错误 */
	private final static int ERROR_FALSE_MESSGE_CODE = 50053;
	/** 定义该卡已经签订支付协议的结果码 */
	private final static int ERROR_FALSE_REPEAT_CODE = 50055;

	/** 定义订单号错误的结果码 */
	private final static int ERROR_FALSE_ODERNBR = 50058;

	private final static int ERROR_BANK_CODE = -99999;

	private final static int ERROR_CODE = 0;
	/** 获得一个用户信息对象 **/
	private UserToken mUserToken;
	/** 用户登录后获得的令牌 **/
	private String mTokenCode;

	/** 调用API返回对象 **/
	private JSONObject mResult;

	/** 回调方法 **/
	private Callback mCallback;

	private String valCode;

	private String errormsg = "";

	public GetSignCardValCode(Activity acti, Callback callback) {
		super(acti);
		this.mCallback = callback;
	}

	@Override
	protected void onPreExecute() {
		if (null != mActivity && mProcessDialog != null) {
			mProcessDialog.dismiss();
		}
	}

	/**
	 * 回调方法的接口
	 * 
	 */
	public interface Callback {
		public void getResult(String result);
	}

	@Override
	protected Integer doInBackground(String... params) {
		try {
			mUserToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
			mTokenCode = mUserToken.getTokenCode();// 用户登录后获得的令牌
			LinkedHashMap<String, Object> reqparams = new LinkedHashMap<String, Object>();
			reqparams.put("orderNbr", params[0]);
			reqparams.put("tokenCode", mTokenCode);
			// 调用API
			// mResult = (JSONObject) API.reqCust("getSignCardValCode",
			// reqparams);
			mResult = (JSONObject) API.reqCust("getSignCardValCode", reqparams);
			if (mResult != null) {
				if (mResult.get("code") == null) { return ERROR_CODE; }
				if ("50000".equals(mResult.get("code").toString()) || "20000".equals(mResult.get("code").toString())
						|| "50053".equals(mResult.get("code").toString())
						|| "50053".equals(mResult.get("code").toString())
						|| "50055".equals(mResult.get("code").toString())
						|| "50058".equals(mResult.get("code").toString())) {
					int retCode = Integer.parseInt(mResult.get("code").toString());
					if (retCode == RIGHT_RET_CODE) {
						if (mResult.get("valCode") == null) { return ERROR_CODE; }
						valCode = mResult.get("valCode").toString();
					}
					return retCode;
				} else {
					if (mResult.get("retmsg") == null) { return ERROR_CODE; }
					errormsg = mResult.get("retmsg").toString();
					return ERROR_BANK_CODE;
				}
			}
		} catch (SzException e) {
			this.mErrCode = e.getErrCode();
			Log.d(TAG, "e.getErrCode()=" + e.getErrCode());
			return this.mErrCode.getCode();// 返回错误编码
		}
		return null;
	}

	@Override
	protected void handldBuziRet(int retCode) {
		if (mCallback == null) { return; }

		switch (retCode) {
		case RIGHT_RET_CODE:
			mCallback.getResult(valCode);
			break;

		case ERROR_RET_CODE:
			mCallback.getResult(null);
			break;

		case ERROR_FALSE_MESSGE_CODE:
			mCallback.getResult(null);
			Toast.makeText(mActivity, "信息填写错误", Toast.LENGTH_SHORT).show();
			break;

		case ERROR_FALSE_REPEAT_CODE:
			mCallback.getResult(null);
			Toast.makeText(mActivity, "该卡已经签订支付协议", Toast.LENGTH_SHORT).show();
			break;

		case ERROR_FALSE_ODERNBR:
			mCallback.getResult(null);
			Toast.makeText(mActivity, "订单号错误", Toast.LENGTH_SHORT).show();
			break;
		case ERROR_BANK_CODE:
			mCallback.getResult(null);
			// 待修改
			if (Util.isEmpty(errormsg)) {
				Toast.makeText(mActivity, "失败，请联系客服", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(mActivity, errormsg, Toast.LENGTH_SHORT).show();
			}
			break;
		case ERROR_CODE:
			mCallback.getResult(null);
			Toast.makeText(mActivity, "失败，请联系客服", Toast.LENGTH_SHORT).show();
			break;

		default:
			mCallback.getResult(null);
			break;
		}
	}
}
