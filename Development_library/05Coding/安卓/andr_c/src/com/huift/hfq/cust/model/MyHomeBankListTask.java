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
import com.huift.hfq.cust.R;

/**
 * 银行卡列表的异步任务
 * 
 * @author wensi.yu
 *
 */
public class MyHomeBankListTask extends SzAsyncTask<String, String, Integer> {

	private final static String TAG = MyHomeBankListTask.class.getSimpleName();
	
	/***创建一个JSONArray对象**/
	private JSONObject mResult;
	/** 定义一个正确的返回结果码 **/ 
	private final static int RIGHT_RET_CODE = 1;
	/** 定义一个错误的返回结果码 **/ 
	private final static int ERROR_RET_CODE = 0;
	/**回调方法**/ 	
	private Callback mCallback;
	
	/**
	 * 构造函数
	 * @param acti
	 */
	public MyHomeBankListTask(Activity acti) {
		super(acti);
		
	}

	/**
	 * 回调方法的构造函数
	 * @param acti
	 */
	public MyHomeBankListTask(Activity acti,Callback callback) {
		super(acti);
		this.mCallback=callback;
	}

	/**  
     * 回调方法的接口  
     *  
     */  
    public interface Callback{  
        public void getResult(JSONObject object);  
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
		reqParams.put("page",params[0]);
		reqParams.put("tokenCode",tokenCode);	
		
		try {
			int retCode = ERROR_RET_CODE;
			mResult = (JSONObject) API.reqCust("getBankAccountList", reqParams);
			if(!("[]".equals(mResult.toString()))){
				//判断返回的对象是否为空
				if(mResult != null || !"".equals(mResult.toJSONString())){
					//返回正确结果码
					retCode = RIGHT_RET_CODE;
				}
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
		if(retCode == RIGHT_RET_CODE){
			mCallback.getResult(mResult);
		}
		else {
			mCallback.getResult(null);
			Util.getContentValidate(R.string.no_bank_list);
		}
	}
}
