package com.huift.hfq.shop.model;

import java.util.LinkedHashMap;

import com.huift.hfq.base.ErrorCode;
import com.huift.hfq.base.SzException;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.api.API;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.model.SzAsyncTask;
import com.huift.hfq.base.pojo.UserToken;

import net.minidev.json.JSONObject;
import android.app.Activity;

/**
 * 验证活动，用于活动验证
 * @author qian.zhou
 */
public class ValUserActCodeTask extends SzAsyncTask<String , String , Integer> {
	/**创建一个JSONObject对象**/
	private JSONObject mResult ;
	
	/**
	 * 构造函数
	 * @param acti
	 */
	public ValUserActCodeTask(Activity acti) {
		super(acti) ;
	}

	/**
	 * 回调方法 	
	 */
	private Callback callback ;
		
	/**
	 * @param acti 上下文
	 * @param callback 回调方法
	 */
	public ValUserActCodeTask(Activity acti , Callback callback) {
		super(acti) ;
		this.callback = callback ;
	}

	/**  
     * 回调方法的接口  
     */  
    public interface Callback{  
        public void getResult(JSONObject object) ;  
    }
    
    /**
   	 * onPostExecute()中的正常业务逻辑处理.
   	 */
	@Override
	protected void handldBuziRet(int retCode) {
		if (retCode == ErrorCode.SUCC) {
			callback.getResult(mResult) ;
		}else{
			Util.showToastZH("验证失败了，请重试");
		}
	}
	
	/**
	 * 调用API,用于活动的验证
	 */
	@Override
	protected Integer doInBackground(String... params) {
		UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		LinkedHashMap<String , Object> reqParams = new LinkedHashMap<String , Object>() ;
		reqParams.put("actCode" , params[0]) ;//活动验证码
		reqParams.put("userActCodeId" , params[1]) ;//活动验证码id
		reqParams.put("shopCode" , userToken.getShopCode()) ;//商家编码
		reqParams.put("tokenCode" , userToken.getTokenCode()) ;//需要令牌认证
		try {
			mResult = (JSONObject) API.reqShop("valUserActCode" , reqParams) ;
			// 如果成功，保存到数据库
			return Integer.parseInt(String.valueOf(mResult.get("code"))) ;
		} catch (SzException e) {
			this.mErrCode = e.getErrCode() ;
			return this.mErrCode.getCode() ;
		}
	}
}
