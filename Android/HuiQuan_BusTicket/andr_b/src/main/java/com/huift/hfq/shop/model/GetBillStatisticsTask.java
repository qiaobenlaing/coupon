package com.huift.hfq.shop.model;

import android.app.Activity;
import android.util.Log;

import com.huift.hfq.base.SzException;
import com.huift.hfq.base.api.API;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.model.SzAsyncTask;
import com.huift.hfq.base.pojo.UserToken;

import net.minidev.json.JSONObject;

import java.util.LinkedHashMap;

/**
 * 获得统计信息
 * @author wensi.yu
 *
 */
public class GetBillStatisticsTask extends SzAsyncTask<String, String, Integer>{
	
	private final static String TAG = "GetBillStatisticsTask";
	
	/** 创建一个JSONObject对象*/
	private JSONObject mResult;
	/** 定义一个正确的返回结果码 */ 
	private final static int RIGHT_RET_CODE = 1;
	/** 定义一个错误的返回结果码 */ 
	private final static int ERROR_RET_CODE = 0;
	/**回调方法**/ 	
	private Callback callback;

	public GetBillStatisticsTask(Activity acti) {
		super(acti);
	}
	
	/**
	 * 回调方法的构造函数
	 * @param acti
	 */
	public GetBillStatisticsTask(Activity acti,Callback callback) {
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
    
    /**
     * timeLimit
     * 		1-今天  2-最近一3-最近一月    4-全部
     * billType
     * 		1-顾客清单  2-退款清单  3-消费未结算账单  4-补贴未结算账单   5-支付结算对账  6-补贴结算对账  7-账单查询
     * searchWord
     * 		输入的值
     */
    @Override
	protected Integer doInBackground(String... params) {
		UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		String shopCode = userToken.getShopCode();//商家编码
		String tokenCode = userToken.getTokenCode();//需要令牌认证
		LinkedHashMap<String, Object> reqParams = new LinkedHashMap<String, Object>();
		reqParams.put("shopCode",shopCode);
		reqParams.put("timeLimit",params[0]);
		reqParams.put("billType",params[1]);
		reqParams.put("searchWord",params[2]);
		reqParams.put("tokenCode",tokenCode);		
		try {
			mResult = (JSONObject)API.reqShop("getBillStatistics", reqParams);
			int retCode = ERROR_RET_CODE;
			if (null != mResult) {
				retCode = RIGHT_RET_CODE; //1 代表访问成功
			}
			Log.d(TAG, "retCode=="+retCode);
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
		} else if (retCode == ERROR_RET_CODE){ 
			callback.getResult(null);
		}
	}
}
