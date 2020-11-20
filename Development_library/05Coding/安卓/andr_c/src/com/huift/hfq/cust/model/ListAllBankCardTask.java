package com.huift.hfq.cust.model;

import java.util.LinkedHashMap;

import net.minidev.json.JSONArray;
import android.app.Activity;
import android.util.Log;
import com.huift.hfq.base.ErrorCode;
import com.huift.hfq.base.SzException;
import com.huift.hfq.base.api.API;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.model.SzAsyncTask;
import com.huift.hfq.base.pojo.UserToken;

/**
 * 获取银行卡列表
 * @author yanfang.li
 *
 */
public class ListAllBankCardTask extends SzAsyncTask<String, String, Integer> {

	private final static String TAG = ListAllBankCardTask.class.getSimpleName();
	
	/***创建一个JSONArray对象**/
	private JSONArray mResult;
	/**回调方法**/ 	
	private Callback mCallback;
	
	/**
	 * 构造函数
	 * @param acti
	 */
	public ListAllBankCardTask(Activity acti) {
		super(acti);
		
	}

	/**
	 * 回调方法的构造函数
	 * @param acti
	 */
	public ListAllBankCardTask(Activity acti,Callback callback) {
		super(acti);
		this.mCallback=callback;
	}

	/**  
     * 回调方法的接口  
     *  
     */  
    public interface Callback{  
        public void getResult(JSONArray result);  
    }
	
	/**
	 * 调用API 
	 * 
	 */
	@Override
	protected Integer doInBackground(String... params) {
		// 获得一个用户信息对象
		UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		String userCode = userToken.getUserCode();// 用户编码
		String tokenCode = userToken.getTokenCode();// 需要令牌认证
		
		//银行卡列表信息
		LinkedHashMap<String, Object> reqParams = new LinkedHashMap<String, Object>();
		reqParams.put("userCode", userCode);
		reqParams.put("tokenCode",tokenCode);	
		
		try {
			int retCode = ErrorCode.ERROR_RET_CODE;
			mResult = (JSONArray) API.reqCust("listAllBankCard", reqParams);
			//判断返回的对象是否为空
			if(!"[]".equals(mResult.toJSONString()) || mResult.size() > 0){
				//返回正确结果码
				retCode = ErrorCode.RIGHT_RET_CODE;
			}
			return retCode;
		} catch (SzException e) {
			this.mErrCode = e.getErrCode();
			return this.mErrCode.getCode();// 返回错误编码
		}catch (Exception e) {
			return 0;// 返回错误编码
		}
	}

	/**
	 * 业务逻辑处理
	 */
	@Override
	protected void handldBuziRet(int retCode) {
		Log.d(TAG, "handldBuziRet:"+retCode);
		if(retCode == ErrorCode.RIGHT_RET_CODE){
			mCallback.getResult(mResult);
		} else {
			mCallback.getResult(null);
//			Util.getContentValidate(R.string.no_bank_list);
		}
	}
}
