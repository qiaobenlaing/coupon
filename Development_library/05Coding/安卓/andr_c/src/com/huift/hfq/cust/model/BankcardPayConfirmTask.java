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

/**
 * 
 * @author yingchen
 * 线上银行卡确认支付的异步任务
 */
public class BankcardPayConfirmTask extends SzAsyncTask<String, Integer, Integer>{
	private static final String TAG = BankcardPayConfirmTask.class.getSimpleName();
	
	//定义正确的返回结果码
	private final static int RIGHT_RET_CODE = 50000;
	//定义失败的返回结果码
	private final static int ERROR_RET_CODE = 20000;
	//定义银行账户为空的结果码
	private final static int ERROR_RET_EMPTY = 50056;
	//定义银行账户编码错误的结果码
	private final static int ERROR_BANKACCOUNTCODE_FALSE = 50057;
	//定义消费订单不存在的结果码
	private final static int ERROR_ORDER_NOEXSIT = 50900;
	//定义银行返回错误
	private final static int ERROR_BANK_CODE = -99999;
	
	//银行返回错误信息
	private String BANK_MSG = "";
		
	/** 调用API返回对象 **/
	private JSONObject mResult;
	
	private Callback callback;
	
	private String orderCode = "";
	
	/**调用API返回的结果码  以便进入支付结果参数传入*/
	private String returnCode = "";
	
	public BankcardPayConfirmTask(Activity acti) {
		super(acti);
	}
	
	
	public BankcardPayConfirmTask(Activity acti, Callback callback) {
		super(acti);
		this.callback = callback;
	}


	public  interface Callback{
		//  result为true时返回retCode    false时返回returnCode
		public void getResult(Boolean result,String returnMessage);
	}

	@Override
	protected void handldBuziRet(int retCode) {
		if(callback==null){
			return;
		}
		
		switch (retCode) {
		case RIGHT_RET_CODE:
			callback.getResult(true,orderCode);
			break;
		case ERROR_RET_CODE:
			Toast.makeText(mActivity, "失败", Toast.LENGTH_SHORT).show();
			callback.getResult(false,returnCode);
			break;
		case ERROR_RET_EMPTY:
			Toast.makeText(mActivity, "银行账户为空", Toast.LENGTH_SHORT).show();
			callback.getResult(false,returnCode);
			break;
		case ERROR_BANKACCOUNTCODE_FALSE:
			Toast.makeText(mActivity, "银行账户编码错误", Toast.LENGTH_SHORT).show();
			callback.getResult(false,returnCode);
			break;
		case ERROR_ORDER_NOEXSIT:
			Toast.makeText(mActivity, "消费订单不存在", Toast.LENGTH_SHORT).show();
			callback.getResult(false,returnCode);
			break;
			
		case ERROR_BANK_CODE:
			Toast.makeText(mActivity, BANK_MSG, Toast.LENGTH_SHORT).show();
			callback.getResult(false,returnCode);
			break;
			
		default:
			callback.getResult(false,returnCode);
			break;
		}
	}

	@Override
	protected Integer doInBackground(String... params) {
		// 获得一个用户信息对象
		UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		String tokenCode = userToken.getTokenCode();
		
		LinkedHashMap<String, Object> reqparams = new LinkedHashMap<String, Object>();
		reqparams.put("consumeCode", params[0]);
		reqparams.put("bankAccountCode", params[1]);
		reqparams.put("valCode", params[2]);
		reqparams.put("tokenCode", tokenCode);
		try {
			mResult = (JSONObject) API.reqCust("bankcardPayConfirm", reqparams);
			int retCode = ERROR_RET_CODE;
			if(mResult!=null){
				Log.d(TAG,"BankcardPayConfirmTask==="+mResult.toString());
				String reCode = mResult.get("code").toString();
				returnCode = reCode;
				if(!reCode.equalsIgnoreCase("50000")
				&& !reCode.equalsIgnoreCase("20000")
				&& !reCode.equalsIgnoreCase("50056")
				&& !reCode.equalsIgnoreCase("50057")
				&& !reCode.equalsIgnoreCase("50900"))
				{
					retCode = ERROR_BANK_CODE;		
					this.BANK_MSG = mResult.get("retmsg").toString();
				}
				else{
					retCode  = Integer.parseInt(mResult.get("code").toString());
					if(retCode == 50000){
						orderCode = mResult.get("orderCode").toString();
					}
				}	
			}
			return retCode;
		} catch (SzException e) {
			this.mErrCode = e.getErrCode();
			return this.mErrCode.getCode();// 返回错误编码
		}
	}

}
