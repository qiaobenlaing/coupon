package com.huift.hfq.cust.model;

import net.minidev.json.JSONArray;
import android.app.Activity;
import android.util.Log;
import com.huift.hfq.base.SzException;
import com.huift.hfq.base.api.API;
import com.huift.hfq.base.model.SzAsyncTask;

/**
 * 查询所有的错误信息
 * @author wenis.yu
 *
 */
public class ErrorInforTask  extends SzAsyncTask<String , String , Integer> {
	
	private final static String TAG  = "ErrorInforTask" ;
	
	/***创建一个JSONArray对象**/
	private JSONArray mResult;
	/** 定义一个正确的返回结果码 **/ 
	private final static int RIGHT_RET_CODE = 1;
	/** 定义一个错误的返回结果码 **/ 
	private final static int ERROR_RET_CODE = 0;
	private Callback mCallback;

	public ErrorInforTask(Activity acti) {
		super(acti);
	}
	
	/**
	 * 有参构造方法
	 * @param acti 调用者的上下文
	 * @param callback 回调方法
	 */
	public ErrorInforTask(Activity acti, Callback callback) {
		super(acti);
		mCallback = callback;
	}
	
	/**  
     * 回调方法的接口  
     */
	public interface Callback{
		public void getResult(JSONArray mResult);
	}

	@Override
	protected Integer doInBackground(String... params) {
		try {
			mResult = (JSONArray) API.reqCust("errorInfo" , null) ;
			int retCode = ERROR_RET_CODE;
			if (null != mResult) {
				retCode = RIGHT_RET_CODE; //1 代表访问成功
			}
			Log.d(TAG, "retCode=="+retCode);
			return retCode;
		} catch (SzException e) {
			this.mErrCode = e.getErrCode() ;
			return this.mErrCode.getCode() ;
		}
	}
	
	@Override
	protected void handldBuziRet(int retCode) {
		if (retCode == RIGHT_RET_CODE) {
			mCallback.getResult(mResult);
		}else if (retCode == ERROR_RET_CODE) {
			mCallback.getResult(null);
		}
	}
}
