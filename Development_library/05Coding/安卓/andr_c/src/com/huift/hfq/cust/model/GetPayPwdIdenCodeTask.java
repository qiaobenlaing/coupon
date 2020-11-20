package com.huift.hfq.cust.model;

import java.util.LinkedHashMap;

import net.minidev.json.JSONObject;
import android.R.integer;
import android.app.Activity;
import android.util.Log;
import com.huift.hfq.base.SzException;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.api.API;
import com.huift.hfq.base.model.SzAsyncTask;
import com.huift.hfq.cust.R;

/**
 * 获取设置支付密码时的短信验证码
 * @author yingchen
 *
 */
public class GetPayPwdIdenCodeTask extends SzAsyncTask<String, integer, Integer>  {

	private final static String TAG = "GetPayPwdIdenCodeTask";
	
	/**定义一个成功的结果码*/
	private static final int RIGHT_RET_CODE = 50000;
	/**请求失败*/
	private static final int INDENCODE_CODE_ERROR = 80010;
	/**定义一个请求失败的结果码*/
	private static final int ERROR_RET_CODE = -1;
	
	/**调用API返回对象 **/
	private JSONObject mResult;
	/**回调方法**/ 	
	private Callback mCallback;
	
	/**返回的验证码*/
	private static String returnCode = "";
	
	/**
	 * 构造函数
	 * @param acti
	 */
	public GetPayPwdIdenCodeTask(Activity acti) {
		super(acti);
	}
	
	/**
	 * 回调方法的构造函数
	 * @param acti
	 */
	public GetPayPwdIdenCodeTask(Activity acti,Callback callback) {
		super(acti);
		this.mCallback = callback;
	}
	
	/**  
     * 回调方法的接口  
     *  
     */  
    public interface Callback{  
        public void getResult(String idenCode);  
    }

    @Override
	protected Integer doInBackground(String... params) {
    	LinkedHashMap<String, Object> reqparams = new LinkedHashMap<String, Object>();
    	reqparams.put("mobileNbr", params[0]);
    	reqparams.put("action", "spp");
    	reqparams.put("appType", "c");
    	
    	try {
    		mResult = (JSONObject) API.reqComm("getValidateCode", reqparams);
    		int retCode = ERROR_RET_CODE;
    		if(null != mResult){
    			Log.d(TAG, "GetPayPwdIdenCodeTask === "+mResult.toString());
    			retCode = Integer.parseInt(mResult.get("code").toString());
    			if(retCode == RIGHT_RET_CODE){
    				returnCode = (null == mResult.get("valCode"))?"":mResult.get("valCode").toString();
    			}
    		}		
    		return retCode;
		} catch (SzException e) {
			this.mErrCode=e.getErrCode();
			return this.mErrCode.getCode();	
		}
		
	}
    
	@Override
	protected void handldBuziRet(int retCode) {
		Log.d(TAG, "retCode == "+retCode);
		switch (retCode) {
		case RIGHT_RET_CODE:
			mCallback.getResult(returnCode);
			break;

		case INDENCODE_CODE_ERROR:
			mCallback.getResult(null);
			Util.getContentValidate(R.string.get_pay_iden_code_fail);
			break;
			
		case ERROR_RET_CODE:
			mCallback.getResult(null);
			Util.getContentValidate(R.string.get_pay_iden_code_fail);
			break;
			
		default:
			mCallback.getResult(null);
			Util.getContentValidate(R.string.get_pay_iden_code_fail);
			break;
		}
	}

	
	
}	
