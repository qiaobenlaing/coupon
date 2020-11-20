package com.huift.hfq.shop.model;

import java.util.LinkedHashMap;

import com.huift.hfq.base.ErrorCode;
import com.huift.hfq.base.SzException;
import com.huift.hfq.base.api.API;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.model.SzAsyncTask;
import com.huift.hfq.base.pojo.Shop;
import com.huift.hfq.base.pojo.UserToken;
import com.huift.hfq.shop.fragment.RestaurantSetFragment;

import net.minidev.json.JSONObject;
import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

/**
 * 修改商家配送方案
 * @author qian.zhou
 */
public class EditShopDeliveryTask extends SzAsyncTask<String , String , Integer> {
	private final static String TAG  = "EditShopDeliveryTask" ;
	/**创建一个JSONObject对象**/
	private JSONObject mResult ;
	private Callback mCallback;
	/** 获得一个用户信息对象 **/
	private UserToken mUserToken;
	/** 用户登录后获得的令牌 **/
	private String mTokenCode;
	
	/**
	 * 构造函数
	 * @param acti
	 */
	public EditShopDeliveryTask(Activity acti) {
		super(acti) ;
	}
	
	/**
	 * 有参构造方法
	 * @param acti 调用者的上下文
	 * @param callback 回调方法
	 */
	public EditShopDeliveryTask(Activity acti, Callback callback) {
		super(acti);
		mCallback = callback;
	}
	
	/**  
     * 回调方法的接口  
     */
	public interface Callback{
		/**
		 * @param result 是异步请求的结果
		 */
		public void getResult(int retCode);
	}

    /**
   	 * onPostExecute()中的正常业务逻辑处理.
   	 */
	@Override
	protected void handldBuziRet(final int retCode) {
		if (retCode == ErrorCode.SUCC) {
			 new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						mCallback.getResult(retCode);
					}
		   }, 1000);
			Intent intent = new Intent();
			mActivity.setResult(RestaurantSetFragment.RESULT_UPP_DELIVERY, intent);
		    mActivity.finish(); 
		}else{
			mCallback.getResult(retCode);
		}
	}

	/**
	 * 调用API
	 */
	@Override
	protected Integer doInBackground(String... params) {
		Log.d(TAG, "EditShopDeliveryTask==="+params[0]);
		
		// 获得一个用户信息对象
		mUserToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		mTokenCode = mUserToken.getTokenCode();// 用户登录后获得的令牌
		LinkedHashMap<String, Object> reqParams = new LinkedHashMap<String , Object>() ;
		reqParams.put("deliveryId", params[0]);
		reqParams.put("shopCode", params[1]);
		reqParams.put("deliveryDistance", params[2]);
		reqParams.put("requireMoney", params[3]);
		reqParams.put("deliveryFee", params[4]);
		reqParams.put("tokenCode" , mTokenCode) ;
		try {
			mResult = (JSONObject) API.reqShop("editShopDeliveryBatchAd" , reqParams) ;
			// 如果成功，保存到数据库
			return Integer.parseInt(String.valueOf(mResult.get("code"))) ;
		} catch (SzException e) {
			this.mErrCode = e.getErrCode() ;
			return this.mErrCode.getCode() ;
		}
	}
}
