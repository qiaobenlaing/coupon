package com.huift.hfq.cust.model;

import java.util.LinkedHashMap;

import net.minidev.json.JSONObject;
import android.app.Activity;
import android.util.Log;
import com.huift.hfq.base.SzException;
import com.huift.hfq.base.api.API;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.model.SzAsyncTask;
import com.huift.hfq.base.pojo.UserToken;

/**
 * 买单扫描
 * @author yanfang.li
 */
public class GetUserShopRecordTask extends SzAsyncTask<String, Integer, Integer> {
	
	
	private final static String TAG = GetUserShopRecordTask.class.getSimpleName();
	/** 定义一个正确的返回结果码 **/
	private final static int RIGHT_RET_CODE = 1;
	/** 定义一个错误的返回结果码 **/
	private final static int ERROR_RET_CODE = 0;

	/** 调用API返回对象 **/
	private JSONObject mResult;

	/** 回调方法 **/
	private Callback mCallback;


	public GetUserShopRecordTask(Activity acti, Callback callback) {
		super(acti);
		this.mCallback = callback;
	}

	@Override
	protected Integer doInBackground(String... params) {
		
		LinkedHashMap<String, Object> reqparams = new LinkedHashMap<String, Object>();
		UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		String userCode = userToken.getUserCode();
		String tokenCode = userToken.getTokenCode();
		Log.d(TAG, "GetUserShopRecordTask >>> " + userCode);
		reqparams.put("userCode", userCode);
		reqparams.put("shopCode", params[0]);
		reqparams.put("tokenCode", tokenCode);
		try {
			mResult = (JSONObject) API.reqCust("getUserShopRecord", reqparams);
			
			int retCode = ERROR_RET_CODE;
			if(mResult!=null || !"".equals(mResult.toString())){
				retCode = RIGHT_RET_CODE;
			}
			return retCode;
			
		} catch (SzException e) {
			this.mErrCode = e.getErrCode();
			return this.mErrCode.getCode();//返回错误编码
		}
	}

	/**
	 * 回调方法的接口
	 */
	public interface Callback {
		public void getResult(int resultCode);
	}
	
	@Override
	protected void handldBuziRet(int retCode) {
		if(mCallback==null){
			return;
		}
		if (retCode == RIGHT_RET_CODE) {
			int code = Integer.parseInt(mResult.get("code").toString());
			mCallback.getResult(code);
		} else {
			mCallback.getResult(ERROR_RET_CODE);
		}
	}
	
}
