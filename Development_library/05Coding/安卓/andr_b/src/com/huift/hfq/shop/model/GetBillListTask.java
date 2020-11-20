package com.huift.hfq.shop.model;

import java.util.LinkedHashMap;

import com.huift.hfq.base.SzException;
import com.huift.hfq.base.api.API;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.model.SzAsyncTask;
import com.huift.hfq.base.pojo.UserToken;

import net.minidev.json.JSONObject;
import android.app.Activity;
import android.util.Log;

/**
 * 账单的列表
 * @author wensi.yu
 *
 */
public class GetBillListTask extends SzAsyncTask<String, String, Integer>{

	private final static String TAG = "GetBillListTask";
	
	/** 创建一个JSONObject对象*/
	private JSONObject mResult;
	/** 定义一个正确的返回结果码 */ 
	private final static int RIGHT_RET_CODE = 1;
	/** 定义一个错误的返回结果码 */ 
	private final static int ERROR_RET_CODE = 0;
	/**回调方法**/ 	
	private Callback callback;

	public GetBillListTask(Activity acti) {
		super(acti);
	}
	
	/**
	 * 回调方法的构造函数
	 * @param acti
	 */
	public GetBillListTask(Activity acti,Callback callback) {
		super(acti);
		this.callback = callback;
	}
	
	/**  
     * 回调方法的接口  
     *  
     */  
    public interface Callback{  
        public void getResult(JSONObject mResult);  
    }
    
    @Override
   	protected void onPreExecute() {
   		if (mActivity != null && mProcessDialog != null) {
   			mProcessDialog.dismiss();
   		}
   	}
	
    @Override
	protected Integer doInBackground(String... params) {
		UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		String shopCode = userToken.getShopCode();//商家编码
		String tokenCode = userToken.getTokenCode();//需要令牌认证
		LinkedHashMap<String, Object> reqParams = new LinkedHashMap<String, Object>();
		reqParams.put("shopCode", shopCode);
		reqParams.put("page", Integer.parseInt(params[0]));
		reqParams.put("timeLimit",params[1]);
		reqParams.put("billType",params[2]);
		reqParams.put("searchWord",params[3]);
		reqParams.put("tokenCode", tokenCode);		
		try {
			int retCode = ERROR_RET_CODE;
			mResult = (JSONObject)API.reqShop("getBillList", reqParams);
			if (mResult != null || !"".equals(mResult.toString())) {
				Log.d(TAG, "GetBillListTask==="+mResult.toString());
				retCode = RIGHT_RET_CODE; //1 代表访问成功
			}
			return retCode;
			
		} catch (SzException e) {
			this.mErrCode = e.getErrCode();
			return this.mErrCode.getCode();
		}
	}

	/**
	 * 正常业务逻辑处理
	 */
	@Override
	protected void handldBuziRet(int retCode) {
		if (retCode == RIGHT_RET_CODE) {
			callback.getResult(mResult);
		}else{ 
			callback.getResult(null);
		}
	}

}
