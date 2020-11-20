package com.huift.hfq.cust.model;

import java.util.LinkedHashMap;

import net.minidev.json.JSONObject;
import android.app.Activity;
import com.huift.hfq.base.ErrorCode;
import com.huift.hfq.base.SzException;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.api.API;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.model.SzAsyncTask;
import com.huift.hfq.base.pojo.UserToken;
import com.huift.hfq.cust.R;

/**
 * 点击关注立即调用
 * @author qian.zhou
 */
public class FocusCardListTask extends SzAsyncTask<String, Integer, Integer> {
	/**创建一个JSONObject对象**/
	private JSONObject mResult ;

	/**
	 * 构造函数
	 * @param acti
	 */
	public FocusCardListTask(Activity acti) {
		super(acti);
	}
	
	/**
	 * 回调方法 	
	 */
	private Callback callback ;
		
	/**
	 * @param acti 上下文
	 * @param callback 回调方法
	 */
	public FocusCardListTask(Activity acti , Callback callback) {
		super(acti) ;
		this.callback = callback ;
	}
	
	@Override
	protected void onPreExecute() {
		if (mActivity != null && mProcessDialog != null) {
			mProcessDialog.dismiss();
		}
	}
	
	@Override
	protected void handldBuziRet(int retCode) {
		if (retCode == ErrorCode.SUCC) {
			callback.getResult(mResult) ;
			Util.getContentValidate(R.string.focus_succ);
		}else{
			callback.getResult(null) ;
			Util.getContentValidate(R.string.focus_fail);
		}
	}
	
	/**  
     * 回调方法的接口  
     */  
    public interface Callback{  
        public void getResult(JSONObject object) ;  
    }
    
	@Override
	protected Integer doInBackground(String... params) {
		LinkedHashMap<String, Object> reqParams = new LinkedHashMap<String, Object>() ;
		UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		String userCode = userToken.getUserCode();
		String tokenCode = userToken.getTokenCode();
		reqParams.put("userCode" , userCode) ;
		reqParams.put("shopCode" , params[0]) ;
		reqParams.put("tokenCode" ,tokenCode) ;
		try {
			mResult =  (JSONObject) API.reqCust("followShop", reqParams);
			int retCode = Integer.parseInt(mResult.get("code").toString());
			return retCode;
		} catch (SzException e) {
			this.mErrCode = e.getErrCode() ;
			return this.mErrCode.getCode() ;
		}
	}
}
