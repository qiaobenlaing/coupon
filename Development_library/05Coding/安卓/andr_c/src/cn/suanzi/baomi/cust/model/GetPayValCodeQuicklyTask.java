package cn.suanzi.baomi.cust.model;

import java.util.LinkedHashMap;

import net.minidev.json.JSONObject;
import android.app.Activity;
import cn.suanzi.baomi.base.SzException;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.api.API;
import cn.suanzi.baomi.base.data.DB;
import cn.suanzi.baomi.base.model.SzAsyncTask;
import cn.suanzi.baomi.base.pojo.UserToken;
import cn.suanzi.baomi.cust.R;

/**
 * 获取快捷支付的证码
 * 
 * @author ying.cheng
 * 
 */
public class GetPayValCodeQuicklyTask extends SzAsyncTask<String, Integer, Integer> {
	/** 定义一个成功的结果码 */
	private final static int RIGHT_RET_CODE = 50000;
	/** 定义一个失败的结果码 */
	private final static int ERROR_RET_CODE = 20000;
	/** 定义一个账号姓名不正确的结果码 */
	private final static int ERROR_COPY_NAME = 50050;
	/** 定义一个证件号码不正确的结果码 */
	private final static int ERROR_CERTIFICATE_NUM = 50051;
	/** 定义一个银行卡号不正确的结果码 */
	private final static int ERROR_BANK_NUM = 50052;
	/** 定义一个证件类型不正确的结果码 */
	private final static int ERROR_CERTIFICATE_TYPE = 50054;
	/** 该卡已经签订支付协议的结果码 */
	private final static int ERROR_ALREADY_PROCOTOL = 50055;
	/** 预留手机号码不正确 */
	private final static int ERROR_MOBILE_NUM = 60002;
	/** 银行编码错误 */
	private final static int ERROR_BANK_CODE = -1;
	/** 返回为空 */
	private final static int ERROR_CODE = 0;

	private Callback mCallback;

	private JSONObject mResult;

	private String valCode;

	private String bankAccountCode;
	private String errormsg;
	private UserToken mUserToken;
	private String mUserCode = null;
	private String mTokenCode = null;

	public GetPayValCodeQuicklyTask(Activity acti) {
		super(acti);
	}

	public GetPayValCodeQuicklyTask(Activity acti, Callback callback) {
		super(acti);
		this.mCallback = callback;
	}

	public interface Callback {
		public void getResult(String result);
	}

	@Override
	protected void onPreExecute() {
		if (mActivity != null && mProcessDialog != null) {
			mProcessDialog.dismiss();
		}
	}

	@Override
	protected Integer doInBackground(String... params) {
		// 获取令牌
		mUserToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		mUserCode = mUserToken.getUserCode();
		mTokenCode = mUserToken.getTokenCode();

		LinkedHashMap<String, Object> reqparams = new LinkedHashMap<String, Object>();
		reqparams.put("userCode", mUserCode);
		reqparams.put("accountName", params[0]);
		reqparams.put("idType", Integer.parseInt(params[1]));
		reqparams.put("idNbr", params[2]);
		reqparams.put("bankCard", params[3]);
		reqparams.put("mobileNbr", params[4]);
		reqparams.put("consumeCode", params[5]);
		reqparams.put("tokenCode", mTokenCode);

		try {
			mResult = (JSONObject) API.reqCust("getPayValCodeQuicklyModify", reqparams);
			if (mResult != null || !"".equals(mResult.toString())) {
				String code = mResult.get("code").toString();
				if (null != code && !Util.isEmpty(code)) {
					if ("50000".equals(code) || "20000".equals(code) || "50050".equals(code) || "50051".equals(code)
							|| "50052".equals(code) || "50054".equals(code) || "50055".equals(code)
							|| "60002".equals(code)) {
						int retCode = Integer.parseInt(mResult.get("code").toString());
						if (retCode == RIGHT_RET_CODE) {
							valCode = mResult.get("valCode").toString();
							bankAccountCode = mResult.get("bankAccountCode").toString();
						}
						return retCode;
					} else {
						errormsg = mResult.get("retmsg").toString();
						return ERROR_BANK_CODE;
					}

				} else {
					return ERROR_CODE;
				}
			}
		} catch (SzException e) {
			this.mErrCode = e.getErrCode();
			return this.mErrCode.getCode();// 返回错误编码
		}

		return null;
	}

	@Override
	protected void handldBuziRet(int retCode) {
		switch (retCode) {
		case RIGHT_RET_CODE:
			mCallback.getResult(valCode + "##" + bankAccountCode);
			break;
		case ERROR_RET_CODE:
			mCallback.getResult(null);
			break;
		case ERROR_COPY_NAME:
			mCallback.getResult(null);
			Util.getContentValidate(R.string.error_name);
			break;
		case ERROR_CERTIFICATE_NUM:
			mCallback.getResult(null);
			Util.getContentValidate(R.string.error_identity_number);
			break;
		case ERROR_BANK_NUM:
			mCallback.getResult(null);
			Util.getContentValidate(R.string.error_bank_code);
			break;
		case ERROR_CERTIFICATE_TYPE:
			mCallback.getResult(null);
			Util.getContentValidate(R.string.error_credentials_type);
			break;
		case ERROR_ALREADY_PROCOTOL:
			mCallback.getResult(null);
			Util.getContentValidate(R.string.signed_protocol);
			break;
		case ERROR_MOBILE_NUM:
			mCallback.getResult(null);
			Util.getContentValidate(R.string.error_reserve_tel);
			break;
		case ERROR_BANK_CODE:
			mCallback.getResult(null);
			Util.showToastZH(errormsg);
			break;
		case ERROR_CODE:
			mCallback.getResult(null);
			Util.getContentValidate(R.string.try_again);
			break;

		default:
			break;
		}
	}
}
