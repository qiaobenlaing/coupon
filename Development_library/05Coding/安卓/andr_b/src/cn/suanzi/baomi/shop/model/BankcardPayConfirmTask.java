package cn.suanzi.baomi.shop.model;
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
import cn.suanzi.baomi.shop.R;

/**
 * 确认结算
 * @author wensi.yu
 *
 */
public class BankcardPayConfirmTask extends SzAsyncTask<String, Integer, Integer>{
	private static final String TAG = BankcardPayConfirmTask.class.getSimpleName();
	
	/** 定义正确的返回结果码*/
	private final static int RIGHT_RET_CODE = 50000;
	/** 定义失败的返回结果码*/
	private final static int ERROR_RET_CODE = 20000;
	/** 定义银行账户为空的结果码*/
	private final static int ERROR_RET_EMPTY = 50056;
	/** 定义银行账户编码错误的结果码*/
	private final static int ERROR_BANKACCOUNTCODE_FALSE = 50057;
	/** 定义消费订单不存在的结果码*/
	private final static int ERROR_ORDER_NOEXSIT = 50900;
	/** 定义银行返回错误*/
	private final static int ERROR_BANK_CODE = -99999;
	
	/** 银行返回错误信息*/
	private String BANK_MSG = "";
		
	/** 调用API返回对象 **/
	private JSONObject mResult;
	
	private Callback mCallback;
	
	private String orderCode = "";
	
	/**调用API返回的结果码  以便进入支付结果参数传入*/
	private String returnCode = "";
	
	public BankcardPayConfirmTask(Activity acti) {
		super(acti);
	}
	
	public BankcardPayConfirmTask(Activity acti, Callback callback) {
		super(acti);
		this.mCallback = callback;
	}

	public  interface Callback{
		//  result为true时返回retCode    false时返回returnCode
		public void getResult(JSONObject rsult);
	}

	@Override
	protected Integer doInBackground(String... params) {
		UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		String tokenCode = userToken.getTokenCode();
		LinkedHashMap<String, Object> reqparams = new LinkedHashMap<String, Object>();
		reqparams.put("consumeCode", params[0]);
		reqparams.put("bankAccountCode", params[1]);
		reqparams.put("isUseFirstDeduction", params[2]);
		reqparams.put("tokenCode", tokenCode);
		reqparams.put("payChanel", "1");
		try {
			mResult = (JSONObject) API.reqShop("bankcardPayConfirm", reqparams);
			int retCode = ERROR_RET_CODE;
			if(mResult != null){
				Log.d(TAG,"BankcardPayConfirmTask ==="+mResult.toString());
				String reCode = mResult.get("code").toString();
				if(!reCode.equalsIgnoreCase("50000")
				&& !reCode.equalsIgnoreCase("20000")
				&& !reCode.equalsIgnoreCase("50056")
				&& !reCode.equalsIgnoreCase("50057")
				&& !reCode.equalsIgnoreCase("50900"))
				{
					retCode = ERROR_BANK_CODE;		
					this.BANK_MSG = mResult.get("retmsg").toString();
				}  else {
					retCode  = Integer.parseInt(mResult.get("code").toString());
				}
			}
			return retCode;
		} catch (SzException e) {
			this.mErrCode = e.getErrCode();
			return this.mErrCode.getCode();// 返回错误编码
		}
	}
	
	@Override
	protected void handldBuziRet(int retCode) {
		if(mCallback==null){
			return;
		}
		
		switch (retCode) {
		case RIGHT_RET_CODE:
			mCallback.getResult(mResult);
			break;
		case ERROR_RET_CODE:
			mCallback.getResult(null);
			Util.getContentValidate(R.string.toast_print_error);
			break;
		case ERROR_RET_EMPTY:
			mCallback.getResult(null);
			Util.getContentValidate(R.string.toast_bank_nothing);
			break;
		case ERROR_BANKACCOUNTCODE_FALSE:
			mCallback.getResult(null);
			Util.getContentValidate(R.string.toast_bank_code_error);
			break;
		case ERROR_ORDER_NOEXSIT:
			mCallback.getResult(null);
			Util.getContentValidate(R.string.toast_bank_noexis);
			break;
			
		case ERROR_BANK_CODE:
			mCallback.getResult(null);
			Toast.makeText(mActivity, BANK_MSG, Toast.LENGTH_SHORT).show();
			break;
			
		default:
			mCallback.getResult(null);
			break;
		}
	}

}
