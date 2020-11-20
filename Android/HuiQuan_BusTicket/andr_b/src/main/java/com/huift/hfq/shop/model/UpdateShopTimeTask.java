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
import android.os.Handler;

/**
 * 修改店铺信息时间
 * @author qian.zhou
 */
public class UpdateShopTimeTask extends SzAsyncTask<String , String , Integer> {
	private final static String TAG  = "UpdateShopLogoTask" ;
	/**创建一个JSONObject对象**/
	private JSONObject mResult ;
	private Callback mCallback;
	/** 获得一个用户信息对象 **/
	private UserToken mUserToken;
	/** 商家编码 **/
	private String mShopCode;
	/** 用户登录后获得的令牌 **/
	private String mTokenCode;
	/** 传递对象的key值*/
	public final static String SHOP_OBJ = "shop";
	
	/**
	 * 构造函数
	 * @param acti
	 */
	public UpdateShopTimeTask(Activity acti) {
		super(acti) ;
	}
	
	/**
	 * 有参构造方法
	 * @param acti 调用者的上下文
	 * @param callback 回调方法
	 */
	public UpdateShopTimeTask(Activity acti, Callback callback) {
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
		} else {
			mCallback.getResult(retCode);
		}
	}

	/**
	 * 调用API
	 */
	@Override
	protected Integer doInBackground(String... params) {
		// 获得一个用户信息对象
		mUserToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		mShopCode = mUserToken.getShopCode();// 商家编码
		mTokenCode = mUserToken.getTokenCode();// 用户登录后获得的令牌
		
		LinkedHashMap<String, Object> reqParams = new LinkedHashMap<String , Object>() ;
		reqParams.put("shopCode" , mShopCode) ;
		reqParams.put("updateKey", params[0]);
		reqParams.put("updateValue", params[1]);
		reqParams.put("tokenCode" , mTokenCode) ;
		
		try {
			mResult = (JSONObject) API.reqShop("updateShop" , reqParams) ;
			// 如果成功，保存到数据库
			return Integer.parseInt(String.valueOf(mResult.get("code"))) ;
		} catch (SzException e) {
			this.mErrCode = e.getErrCode() ;
			return this.mErrCode.getCode() ;
		}
	}
}
