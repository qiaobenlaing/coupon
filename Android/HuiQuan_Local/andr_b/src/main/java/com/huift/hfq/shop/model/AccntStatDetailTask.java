package com.huift.hfq.shop.model;

import java.util.LinkedHashMap;

import com.huift.hfq.base.SzException;
import com.huift.hfq.base.api.API;
import com.huift.hfq.base.model.SzAsyncTask;

import net.minidev.json.JSONArray;
import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

/**
 * @author wensi.yu
 * 按日获得银行卡刷卡对账的统计列表
 */
public class AccntStatDetailTask extends SzAsyncTask<String, String, Integer> {
	
	private final static String TAG = "AccntStatDetailTask";
	/**创建一个JSONArray对象**/
	private JSONArray mResult;
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
	public AccntStatDetailTask(Activity acti) {
		super(acti);
	}
	
	/**
	 * 回调方法的构造函数
	 * @param acti
	 */
	public AccntStatDetailTask(Activity acti,Callback callback) {
		super(acti);
		this.callback=callback;
	}
	
	/**  
     * 回调方法的接口  
     */  
    public interface Callback{  
        public void getResult(JSONArray object);  
    }

	/**
	 * 调用api 获得对账详情
	 * [0]商家编号 [1]页码   [2]日期   [3]认证
	 */
	@Override
	protected Integer doInBackground(String... params) {
		//获得所用活动信息
		LinkedHashMap<String, Object> reqParams = new LinkedHashMap<String, Object>();		
		reqParams.put("shopCode", params[0]);
		reqParams.put("page", Integer.parseInt(params[1]));
		reqParams.put("datetime", params[2]);
		reqParams.put("tokenCode", params[3]);
		Log.d("***********","刷卡详情商家编码："+params[0]+"刷卡详情页面："+Integer.parseInt(params[1])+"刷卡详情时间："+params[2]+"刷卡详情tokenCode:"+params[3] );
		try {
			//调用api
			mResult = (JSONArray) API.reqShop("listBankCardBill", reqParams);
			int  retCode = ERROR_RET_CODE;
			//判断查询的一个对象不为空为空 就返回一个正确的编码
			if ( !(mResult == null || mResult.size() == 0 || "".equals(mResult.toJSONString())) ) {
				Log.i(TAG, "AccntStatDetailTask.mResult=============="+mResult);
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
		}else {
			if(retCode == ERROR_RET_CODE){
				Toast.makeText(this.mActivity, "没有刷卡对账的列表详情",Toast.LENGTH_SHORT).show();
			}
		}
	}
}
