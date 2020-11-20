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
 * 解除绑定银行卡的异步任务
 * @author yingchen
 *
 */
public class TerminateBankAccount extends SzAsyncTask<String, Integer, Integer> {
	private static final String TAG = TerminateBankAccount.class.getSimpleName(); 
	/**定义一个成功的结果码*/
	private final static int RIGHT_RET_CODE = 50000;
	/**定义一个失败的结果码*/
	private final static int ERROR_RET_CODE = 20000;
	/**定义一个编码错误的结果码*/
	private final static int ERROR_FALSE_CODE = 50057;
	/**定义一个银行卡已经解除协议了的结果码*/
	private final static int ERROR_ALREADY_CANCLE_BLIND = 50059;
	
	private final static int ERROR_CODE = -1;
	private final static int BANK_ERROR_CODE = 0;
	
	private JSONObject mResult;
	
	private Callback mCallback;
	private String errormsg = "请联系客服";

	public TerminateBankAccount(Activity acti, Callback mCallback) {
		super(acti);
		this.mCallback = mCallback;
	}

	public interface Callback{
		public void getResult(boolean result);
	}
	
	public TerminateBankAccount(Activity acti) {
		super(acti);
	}

	@Override
	protected Integer doInBackground(String... params) {
		UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		String tokenCode = userToken.getTokenCode();
		
		LinkedHashMap<String, Object> reqparams = new  LinkedHashMap<String, Object>();
		reqparams.put("bankAccountCode", params[0]);
		reqparams.put("tokenCode", tokenCode);
		
		try {
			mResult = (JSONObject) API.reqCust("terminateBankAccount", reqparams);
			/*int retCode = ERROR_RET_CODE;
			if(mResult!=null){
				if(mResult.get("code")==null){
					retCode = ERROR_CODE;
				}else{
					retCode = Integer.parseInt(mResult.get("code").toString());
				}
			}
			return retCode;*/
			if(mResult!=null&&mResult.get("code")!=null){
				Log.d(TAG, "TerminateBankAccount=="+mResult.toString());
				String code = mResult.get("code").toString();
				if("50000".equals(code)  //API定义的结果码
						||"20000".equals(code)
						||"50057".equals(code)
						||"50059".equals(code)){
					return Integer.parseInt(code);
				}else{  //B开头的错误   银行返回的错误
					if(mResult.get("retmsg")!=null){
						errormsg = mResult.get("retmsg").toString();
					}
					return BANK_ERROR_CODE;
				}
			}
		} catch (SzException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	protected void handldBuziRet(int retCode) {
		switch (retCode) {
		case RIGHT_RET_CODE:
			mCallback.getResult(true);
			break;
		case ERROR_RET_CODE:
			Toast.makeText(mActivity, "解除协议失败", Toast.LENGTH_SHORT).show();
			mCallback.getResult(false);
			break;
		case ERROR_CODE:
			Toast.makeText(mActivity, "解除协议失败", Toast.LENGTH_SHORT).show();
			mCallback.getResult(false);
			break;
		case ERROR_FALSE_CODE:
			Toast.makeText(mActivity, "银行卡编码错误", Toast.LENGTH_SHORT).show();
			mCallback.getResult(false);
			break;
		case ERROR_ALREADY_CANCLE_BLIND:
			Toast.makeText(mActivity, "银行卡已经解除协议了", Toast.LENGTH_SHORT).show();
			mCallback.getResult(false);
			break;
		case BANK_ERROR_CODE:
			Toast.makeText(mActivity, errormsg, Toast.LENGTH_SHORT).show();
			mCallback.getResult(false);
			break;

		default:
			break;
		}
	}
}
