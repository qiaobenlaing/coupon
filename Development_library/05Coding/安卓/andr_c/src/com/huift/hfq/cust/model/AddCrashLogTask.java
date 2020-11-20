package com.huift.hfq.cust.model;

import java.util.LinkedHashMap;

import net.minidev.json.JSONObject;
import android.app.Activity;
import com.huift.hfq.base.SzException;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.api.API;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.model.SzAsyncTask;
import com.huift.hfq.base.pojo.UserToken;

public class AddCrashLogTask extends SzAsyncTask<String, Integer, Integer> {
	/**成功的结果码*/
	private final static int RIGHT_RET_C0DE = 50000;
	/**失败的结果码*/
	private final static int ERROR_RET_CODE = 20000;
	
	private final static  int ERROR_C0DE = -1;
	
	private Callback mCallback;
	
	/** 获得一个用户信息对象 **/
	private UserToken mUserToken;
	/** 用户登录后获得的令牌 **/
	private String mTokenCode = "";
	/**用户编码*/
	private String mUserCode = "";
	/** 手机唯一标识*/
	private String mUnId = "";
	private JSONObject mResult;
	
	public AddCrashLogTask(Activity acti) {
		super(acti);
	}
	
	public AddCrashLogTask(Activity acti, Callback mCallback) {
		super(acti);
		this.mCallback = mCallback;
	}

	public  interface Callback{
		public void getResult(boolean result);
	}
	@Override
	protected Integer doInBackground(String... params) {
		mUserToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		if(mUserToken!=null){
			mUserCode = mUserToken.getUserCode();
			mTokenCode = mUserToken.getTokenCode();
		}
		mUnId = Util.getUqId(mActivity);
		
		LinkedHashMap<String, Object> reqparams = new LinkedHashMap<String, Object>();
		reqparams.put("url", params[0]);
		reqparams.put("userCode", mUserCode);
		reqparams.put("eqpCode", Util.md5(mUnId));
		reqparams.put("appType", 1);
		reqparams.put("tokenCode", mTokenCode);
		int retCode = ERROR_C0DE;
		try {
			mResult = (JSONObject) API.reqComm("addCrashLog", reqparams);
			if(mResult!=null){
				if(mResult.get("code")!=null){
					retCode = Integer.parseInt(mResult.get("code").toString());
					return retCode;
				}
			}
		} catch (SzException e) {
			e.printStackTrace();
		}
		return retCode;
	}
	
	
	@Override
	protected void handldBuziRet(int retCode) {
		switch (retCode) {
		case RIGHT_RET_C0DE:
			mCallback.getResult(true);
			break;
		case ERROR_RET_CODE:
			mCallback.getResult(false);
			break;
		default:
			break;
		}
	}

}
