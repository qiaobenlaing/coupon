package com.huift.hfq.cust.model;

import java.util.LinkedHashMap;

import net.minidev.json.JSONObject;
import android.app.Activity;
import android.util.Log;
import com.huift.hfq.base.ErrorCode;
import com.huift.hfq.base.SzException;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.api.API;
import com.huift.hfq.base.model.SzAsyncTask;
import com.huift.hfq.cust.R;

/**
 * 退出登录
 * @author qian.zhou
 */
public class LogoffTask extends SzAsyncTask<String , String , Integer> {
	private final static String TAG = "LogoffTask" ;
	/**创建一个JSONObject对象**/
	private JSONObject mResult ;
	/**
	 * 构造函数
	 * @param acti
	 */
	public LogoffTask(Activity acti) {
		super(acti) ;
	}

	/**
	 * 回调方法 	
	 */
	private Callback callback ;
		
	/**
	 * @param acti 上下文
	 * @param callback 回调方法
	 */
	public LogoffTask(Activity acti , Callback callback) {
		super(acti) ;
		this.callback = callback ;
	}

	/**  
     * 回调方法的接口  
     */  
    public interface Callback{  
        public void getResult(JSONObject object) ;  
    }
    
    /**
   	 * onPostExecute()中的正常业务逻辑处理.
   	 */
	@Override
	protected void handldBuziRet(int retCode) {
		if (retCode == ErrorCode.SUCC) {
			callback.getResult(mResult) ;
		} else{
			Util.getContentValidate(R.string.app_exit_fail);
		}
	}

	/**
	 * 调用API，退出登录
	 */
	@Override
	protected Integer doInBackground(String... params) {
		LinkedHashMap<String , Object> reqParams = new LinkedHashMap<String , Object>() ;
		reqParams.put("tokenCode" , params[0]) ;//商家编码
		reqParams.put("appType" , Integer.parseInt(params[1])) ;
		reqParams.put("registrationId" , params[2]) ;
		try {
			mResult = (JSONObject) API.reqComm("logoff" , reqParams) ;
			Log.d(TAG, "mresult=="+mResult);
			// 如果成功，保存到数据库
			return Integer.parseInt(String.valueOf(mResult.get("code").toString())) ;
		} catch (SzException e) {
			this.mErrCode = e.getErrCode() ;
			return this.mErrCode.getCode() ;
		}
	}
}
