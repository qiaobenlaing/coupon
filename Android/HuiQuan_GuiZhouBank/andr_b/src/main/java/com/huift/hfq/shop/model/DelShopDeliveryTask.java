package com.huift.hfq.shop.model;

import java.util.LinkedHashMap;

import com.huift.hfq.base.ErrorCode;
import com.huift.hfq.base.SzException;
import com.huift.hfq.base.api.API;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.model.SzAsyncTask;
import com.huift.hfq.base.pojo.UserToken;

import net.minidev.json.JSONObject;
import android.app.Activity;

/**
 * 删除店铺的配送方案
 * @author qian.zhou
 */
public class DelShopDeliveryTask extends SzAsyncTask<String , String , Integer> {
	private final static String TAG = DelShopDeliveryTask.class.getSimpleName();
	/**创建一个JSONObject对象**/
	private JSONObject mResult ;
	
	/**
	 * 构造函数
	 * @param acti
	 */
	public DelShopDeliveryTask(Activity acti) {
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
	public DelShopDeliveryTask(Activity acti , Callback callback) {
		super(acti) ;
		this.callback = callback ;
	}

	/**  
     * 回调方法的接口  
     */  
    public interface Callback{  
        public void getResult(int retCode) ;  
    }
    
	/**
	 * 调用API，删除员工信息
	 */
	@Override
	protected Integer doInBackground(String... params) {
		UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		String tokenCode = userToken.getTokenCode();
		LinkedHashMap<String , Object> reqParams = new LinkedHashMap<String , Object>() ;
		reqParams.put("deliveryId" , params[0]) ;//配送id
		reqParams.put("tokenCode" , tokenCode) ;//需要令牌认证
		try {
			mResult = (JSONObject) API.reqShop("delShopDelivery" , reqParams) ;
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
			callback.getResult(retCode) ;
		}else{
			callback.getResult(retCode) ;
		}
	}
}
