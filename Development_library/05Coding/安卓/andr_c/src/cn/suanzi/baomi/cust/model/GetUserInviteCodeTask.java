package cn.suanzi.baomi.cust.model;

import java.util.LinkedHashMap;

import net.minidev.json.JSONObject;
import android.app.Activity;
import android.util.Log;
import cn.suanzi.baomi.base.SzException;
import cn.suanzi.baomi.base.api.API;
import cn.suanzi.baomi.base.data.DB;
import cn.suanzi.baomi.base.model.SzAsyncTask;
import cn.suanzi.baomi.base.pojo.UserToken;

/**
 * 获得邀请码
 * @author wensi.yu
 *
 */
public class GetUserInviteCodeTask extends SzAsyncTask<String, String, Integer> {
	
	private final static String TAG = GetUserInviteCodeTask.class.getSimpleName();

	/***创建一个JSONObject对象**/
	private JSONObject mResult;
	/** 定义一个正确的返回结果码 **/ 
	private final static int RIGHT_RET_CODE = 1;
	/** 定义一个错误的返回结果码 **/ 
	private final static int ERROR_RET_CODE = 0;
	/**回调方法**/ 	
	private Callback callback;
	
	public GetUserInviteCodeTask(Activity acti) {
		super(acti);
	}
	
	/**
	 * 回调方法的构造函数
	 * @param acti
	 */
	public GetUserInviteCodeTask(Activity acti,Callback callback) {
		super(acti);
		this.callback=callback;
	}

	/**  
     * 回调方法的接口  
     *  
     */  
    public interface Callback{  
        public void getResult(JSONObject mResult);  
    }
    
	
	/**
	 * 获得邀请码
	 */
	@Override
	protected Integer doInBackground(String... params) {
		UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		String userCode = "";
		if (DB.getBoolean(DB.Key.CUST_LOGIN)) {
			userCode = userToken.getUserCode();
		} else {
			userCode = "";
		}
		String tokenCode = userToken.getTokenCode();
		LinkedHashMap<String, Object> reqParams = new LinkedHashMap<String, Object>();
		reqParams.put("userCode",userCode); 
		reqParams.put("tokenCode",tokenCode); 
		
		try {
			mResult = (JSONObject) API.reqCust("getUserInviteCode", reqParams);
			Log.i(TAG, "mResult==============="+mResult);
			int retCode = ERROR_RET_CODE;
			if (mResult != null ) {
				retCode = RIGHT_RET_CODE; //1 代表访问成功
			}
			return retCode;
			
		} catch (SzException e) {
			this.mErrCode = e.getErrCode();
			return this.mErrCode.getCode();
		}
	}
	
	@Override
	protected void handldBuziRet(int retCode) {
		if(retCode == RIGHT_RET_CODE){
			callback.getResult(mResult);
			
		} else if (retCode == ERROR_RET_CODE){
			callback.getResult(null);
		}
	}
}
