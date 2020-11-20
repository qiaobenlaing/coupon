package com.huift.hfq.shop.model;

import java.util.LinkedHashMap;

import com.huift.hfq.base.SzException;
import com.huift.hfq.base.api.API;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.model.SzAsyncTask;
import com.huift.hfq.base.pojo.UserToken;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import android.app.Activity;
import android.util.Log;

/**
 * 查询报名列表
 * @author wensi.yu
 *
 */
public class ListActParticipantTask extends SzAsyncTask<String, String, Integer>  {

	private final static String TAG = "ListActParticipantTask";
	
	/***创建一个JSONObject对象**/
	private JSONObject mResult;
	/** 定义一个正确的返回结果码 **/ 
	private final static int RIGHT_RET_CODE = 1;
	/** 定义一个错误的返回结果码 **/ 
	private final static int ERROR_RET_CODE = 0;
	/**回调方法**/ 	
	private Callback callback;
	
	/**
	 * 构造函数
	 * @param acti
	 */
	public ListActParticipantTask(Activity acti) {
		super(acti);
	}
	
	/**
	 * 回调方法的构造函数
	 * @param acti
	 */
	public ListActParticipantTask(Activity acti,Callback callback) {
		super(acti);
		this.callback=callback;
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (mProcessDialog != null) {
			mProcessDialog.dismiss();
		}
	}
	
	/**  
     * 回调方法的接口  
     *  
     */  
    public interface Callback{  
        public void getResult(JSONObject mResult);  
    }

	/**
	 * 报名列表
	 */
	@Override
	protected Integer doInBackground(String... params) {
		//获得一个用户信息对象
		UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		String tokenCode = userToken.getTokenCode();//需要令牌认证
		LinkedHashMap<String, Object> reqParams = new LinkedHashMap<String, Object>();
		reqParams.put("activityCode",params[0]);//活动归属
		reqParams.put("page", Integer.parseInt(params[1]));
		reqParams.put("tokenCode", tokenCode);	
		
		try {
			int retCode = ERROR_RET_CODE;
			mResult = (JSONObject)API.reqShop("listActParticipant", reqParams);
			Log.i(TAG,"mResult=================="+mResult);
			//判断查询的一个对象不为空为空 就返回一个正确的编码
			JSONArray ArResult = (JSONArray) mResult.get("participantList");
			if (mResult == null || ArResult.size() != 0 || !"[]".equals(ArResult.toString())) {
				retCode = RIGHT_RET_CODE; //1 代表访问成功
			}
			return retCode;
			
		} catch (SzException e) {
			this.mErrCode = e.getErrCode();
			return this.mErrCode.getCode();
		}
	}
	
	/**
	 * 业务逻辑处理
	 */
	@Override
	protected void handldBuziRet(int retCode) {
		if (retCode == RIGHT_RET_CODE) {
			callback.getResult(mResult);
		}else{ 
			if(retCode == ERROR_RET_CODE) {
				callback.getResult(null);
			}
		}
	}

}
