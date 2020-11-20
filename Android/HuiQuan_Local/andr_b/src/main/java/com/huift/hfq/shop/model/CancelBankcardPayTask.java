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
import com.huift.hfq.shop.R;

/**
 * 取消结算
 * @author qian.zhou
 */
public class CancelBankcardPayTask extends SzAsyncTask<String , String , Integer> {
	
	/** 支付已经取消*/
	private static final int ORDER_CANCEL = 50403;
	/** 创建一个JSONObject对象**/
	private JSONObject mResult ;
	
	/**
	 * 构造函数
	 * @param acti
	 */
	public CancelBankcardPayTask(Activity acti) {
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
	public CancelBankcardPayTask(Activity acti , Callback callback) {
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
	 * 调用API，取消结算
	 */
	@Override
	protected Integer doInBackground(String... params) {
		UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		String tokenCode = userToken.getTokenCode();
		LinkedHashMap<String , Object> reqParams = new LinkedHashMap<String , Object>() ;
		reqParams.put("consumeCode" , params[0]) ;//订单编码
		reqParams.put("tokenCode" , tokenCode) ;//需要令牌认证
		try {
			mResult = (JSONObject) API.reqShop("cancelBankcardPay" , reqParams) ;
			// 如果成功，保存到数据库
			return Integer.parseInt(String.valueOf(mResult.get("code"))) ;
		} catch (SzException e) {
			this.mErrCode = e.getErrCode() ;
			return this.mErrCode.getCode() ;
		}
	}
	
	 /**
   	 * onPostExecute()中的正常业务逻辑处理.
   	 */
	@Override
	protected void handldBuziRet(int retCode) {
		if (retCode == ErrorCode.SUCC) {
			callback.getResult(mResult) ;
		}else{
			if (retCode == ErrorCode.FAIL) {
				Util.showToastZH( "取消失败了");
			} else if (retCode == ORDER_CANCEL) {
				Util.getContentValidate(R.string.toast_order_cancel);
			}
			
		}
	}
}
