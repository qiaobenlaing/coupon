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
 * 改变订单状态
 * @author qian.zhou
 */
public class UpdateProductOrderStatusTask extends SzAsyncTask<String , String , Integer> {
	private final static String TAG  = "UpdateProductOrderStatusTask" ;
	
	public static final int RETCODE_CODE = 60511;
	/**创建一个JSONObject对象**/
	private JSONObject mResult ;
	private Callback mCallback;
	
	/**
	 * 构造函数
	 * @param acti
	 */
	public UpdateProductOrderStatusTask(Activity acti) {
		super(acti) ;
	}
	
	/**
	 * 有参构造方法
	 * @param acti 调用者的上下文
	 * @param callback 回调方法
	 */
	public UpdateProductOrderStatusTask(Activity acti, Callback callback) {
		super(acti);
		mCallback = callback;
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (mActivity != null) {
			if (mProcessDialog != null) {
				mProcessDialog.dismiss();
			}
		}
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
		} else{
			mCallback.getResult(retCode);
		}
	}

	/**
	 * 调用API，修改订单状态
	 */
	@Override
	protected Integer doInBackground(String... params) {
		UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		String tokenCode = userToken.getTokenCode();
		LinkedHashMap<String, Object> reqParams = new LinkedHashMap<String , Object>() ;
		reqParams.put("orderCode" , params[0]) ;
		reqParams.put("orderStatus" , params[1]) ;
		reqParams.put("status" , params[2]) ;
		reqParams.put("tableNbr" , params[3]) ;
		reqParams.put("tokenCode" , tokenCode) ;
		
		try {
			mResult=(JSONObject) API.reqShop("updateProductOrderStatus" , reqParams) ;
			// 如果成功，保存到数据库
			return Integer.parseInt(String.valueOf(mResult.get("code"))) ;
		} catch (SzException e) {
			this.mErrCode = e.getErrCode() ;
			return this.mErrCode.getCode() ;
		}
	}
}
