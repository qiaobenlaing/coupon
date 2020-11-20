package com.huift.hfq.shop.model;

import java.util.LinkedHashMap;

import com.huift.hfq.base.ErrorCode;
import com.huift.hfq.base.SzException;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.api.API;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.model.SzAsyncTask;
import com.huift.hfq.base.pojo.UserToken;
import com.huift.hfq.shop.fragment.ActMoneyDetailFragment;

import net.minidev.json.JSONObject;
import android.app.Activity;
import android.util.Log;
import com.huift.hfq.shop.R;

/**
 * 启用
 * @author wensi.yu
 *
 */
public class ChangeBonusStatusTask extends SzAsyncTask<String, String, Integer>{

	private final static String TAG = "ChangeBonusStatusTask";
	
	/***创建一个JSONObject对象**/
	private JSONObject mResult;
	/**回调方法**/ 	
	private Callback callback;
	private int status;
	
	public ChangeBonusStatusTask(Activity acti) {
		super(acti);
	}

	/**
	 * 回调方法的构造函数
	 * @param acti
	 */
	public ChangeBonusStatusTask(Activity acti,Callback callback) {
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

	/**
	 * 改变状态
	 */
	@Override
	protected Integer doInBackground(String... params) {
		UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		String tokenCode = userToken.getTokenCode();
		LinkedHashMap<String, Object> reqParams = new LinkedHashMap<String, Object>();
		reqParams.put("bonusCode", params[0]);
		reqParams.put("status", params[1]);	
		reqParams.put("tokenCode", tokenCode);	
		
		try {
			status = Integer.parseInt(params[1]);
			mResult = (JSONObject)API.reqShop("changeBonusStatus", reqParams);
			Log.i(TAG, "mResult==============="+mResult);
			int retCode = Integer.parseInt(mResult.get("code").toString());
			return retCode;
			
		} catch (SzException e) {
			this.mErrCode = e.getErrCode();
			return this.mErrCode.getCode();
		}
	}
	
	
	@Override
	protected void handldBuziRet(int retCode) {
		if (retCode == ErrorCode.SUCC) {
			if (ActMoneyDetailFragment.ENABLE_STATUS == status) { // 启用成功
				Util.getContentValidate(R.string.tv_actmoney_enablemoney);
			} else if (ActMoneyDetailFragment.STOP_STATUS == status) { // 停用成功
				Util.getContentValidate(R.string.tv_actmoney_usermoney);
			}
			callback.getResult(mResult);
		}else{ 
			if(retCode == ErrorCode.FAIL) {
				Util.getContentValidate(R.string.tv_actmoney_errore);
				callback.getResult(null);
			}
		}
	}

}
