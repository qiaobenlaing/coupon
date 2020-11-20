package com.huift.hfq.cust.model;

import java.util.LinkedHashMap;

import net.minidev.json.JSONObject;
import android.app.Activity;
import com.huift.hfq.base.ErrorCode;
import com.huift.hfq.base.SzException;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.api.API;
import com.huift.hfq.base.model.SzAsyncTask;

/**
 * 保存app当前打开的地理位置
 * @author yanfang.li
 */
public class RecordUserAddressTask extends SzAsyncTask<String, Integer, Integer> {

	public RecordUserAddressTask(Activity acti) {
		super(acti);
	}
	
	@Override
	protected void onPreExecute() {
		if (mActivity != null && mProcessDialog != null) {
			mProcessDialog.dismiss();
		}
	}
	
	@Override
	protected Integer doInBackground(String... params) {
		LinkedHashMap<String, Object> reqparams = new LinkedHashMap<String, Object>();
		reqparams.put("city", params[0]);
		reqparams.put("deviceNbr", Util.md5(params[1]));
		reqparams.put("mobileNbr", params[2]);
		try {
			JSONObject result = (JSONObject) API.reqCust("recordUserAddress", reqparams);
			return ErrorCode.SUCC;
		} catch (SzException e) {
			this.mErrCode = e.getErrCode();
			return this.mErrCode.getCode();
		}
	}

	@Override
	protected void handldBuziRet(int retCode) {
		
	}

}
