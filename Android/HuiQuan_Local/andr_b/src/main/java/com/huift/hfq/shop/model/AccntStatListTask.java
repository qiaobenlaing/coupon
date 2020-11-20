package com.huift.hfq.shop.model;

import java.util.LinkedHashMap;

import com.huift.hfq.base.SzException;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.api.API;
import com.huift.hfq.base.model.SzAsyncTask;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import android.app.Activity;
import android.util.Log;
import com.huift.hfq.shop.R;

/**
 * @author wensi.yu
 * 按日获得银行卡刷卡对账的统计列表
 *
 */
public class AccntStatListTask extends SzAsyncTask<String, String, Integer> {
	
	private final static String TAG = "AccntStatListTask";
	
	/** 创建一个JSONObject对象*/
	private JSONObject mResult;
	/** 定义一个正确的返回结果码 */ 
	private final static int RIGHT_RET_CODE = 1;
	/** 定义一个错误的返回结果码 */ 
	private final static int ERROR_RET_CODE = 0;
	/** 回调方法*/ 	
	private Callback callback;
	
	/**
	 * 构造函数
	 * @param acti
	 */
	public AccntStatListTask(Activity acti) {
		super(acti);
	}
	
	/**
	 * 回调方法的构造函数
	 * @param acti
	 */
	public AccntStatListTask(Activity acti,Callback callback) {
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
	 * 调用api 获得银行卡刷卡对账的统计列表
	 * 
	 */
	@Override
	protected Integer doInBackground(String... params) {
		//获得所用活动信息
		LinkedHashMap<String, Object> reqParams = new LinkedHashMap<String, Object>();		
		reqParams.put("shopCode", params[0]);
		reqParams.put("page", Integer.parseInt(params[1]));
		reqParams.put("time", Integer.parseInt(params[2]));
		reqParams.put("tokenCode", params[3]);
		Log.i("listBankCardCountBill", "商家编码："+params[0]+"页面："+Integer.parseInt(params[1])+"tokenCode:"+params[3]);
		
		try {
			int retCode = ERROR_RET_CODE;
			mResult = (JSONObject)API.reqShop("listBankCardCountBill", reqParams);
			Log.i(TAG, "AccntStatListTask.mResult========================"+mResult);
			JSONArray ArResult = (JSONArray) mResult.get("bankCardBillList");
			//判断查询的一个对象不为空为空 就返回一个正确的编码
			if (mResult != null || ArResult.size() != 0 || !"[]".equals(ArResult.toString())) {
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
		}else if(retCode == ERROR_RET_CODE){
			callback.getResult(null);
			Util.getContentValidate(R.string.toast_moredata);
		}
	}
}
