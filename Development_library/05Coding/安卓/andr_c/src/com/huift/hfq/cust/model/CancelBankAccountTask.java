package com.huift.hfq.cust.model;

import java.util.LinkedHashMap;

import com.huift.hfq.cust.activity.MyHomeBankListActivity;

import net.minidev.json.JSONObject;
import android.app.Activity;
import android.content.Intent;
import com.huift.hfq.base.ErrorCode;
import com.huift.hfq.base.SzException;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.api.API;
import com.huift.hfq.base.model.SzAsyncTask;
import com.huift.hfq.cust.R;

/**
 * 取消添加银行卡
 * @author wensi.yu
 *
 */
public class CancelBankAccountTask extends SzAsyncTask<String, String, Integer> {
	
	private final static String TAG = CancelBankAccountTask.class.getSimpleName();
	
	/** 创建一个JSONObject对象**/
	private JSONObject mResult;
	/**
	 * 构造函数
	 * @param acti
	 */
	public CancelBankAccountTask(Activity acti) {
		super(acti);
	}


	/**
	 * 取消添加银行卡
	 */
	@Override
	protected Integer doInBackground(String... params) {
		LinkedHashMap<String, Object> reqParams = new LinkedHashMap<String, Object>();
		reqParams.put("orderNbr", params[0]);
		reqParams.put("tokenCode", params[1]);
		
		try {
			mResult = (JSONObject) API.reqShop("cancelBankAccount", reqParams);
			int retCode = Integer.parseInt(mResult.get("code").toString());
			return Integer.parseInt(String.valueOf(retCode));
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
		if(retCode == ErrorCode.SUCC){
			Util.getContentValidate(R.string.cancele_success);
			Intent intent = new Intent(mActivity, MyHomeBankListActivity.class);
			this.mActivity.startActivity(intent);
			this.mActivity.finish();
		}else{
			if(retCode == ErrorCode.FAIL){
				Util.getContentValidate(R.string.cancel_fail);
			}
		}
	}
}
