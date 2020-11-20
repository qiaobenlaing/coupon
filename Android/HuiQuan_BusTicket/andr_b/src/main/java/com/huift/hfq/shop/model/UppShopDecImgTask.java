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
 * 修改店铺装修的图片信息
 * @author qian.zhou 
 */
public class UppShopDecImgTask extends SzAsyncTask<String, String, Integer> {
	private final static String TAG = "UppShopDecImgTask";
	/**创建一个JSONArray对象**/
	private JSONObject mResult;
	//回调方法
	private Callback mCallback;
	
	/**
	 * 构造函数
	 * @param acti
	 */
	public UppShopDecImgTask(Activity acti) {
		super(acti);
	}
	
	/**
	 * 构造函数
	 * @param acti
	 */
	public UppShopDecImgTask(Activity acti, Callback callback) {
		super(acti);
		mCallback = callback;
	}

    /**
     * 业务逻辑操作
     */
	@Override
	protected void handldBuziRet(int retCode) {
		if (retCode == ErrorCode.SUCC) {
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					mCallback.getResult(mResult);
				}
			}, 1000);
		} 
		else{
			mCallback.getResult(null);
		}
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if(mProcessDialog != null){
			mProcessDialog.dismiss();
		}
	}
	
	/**  
     * 回调方法的接口  
     *  
     */
	public interface Callback{
		/**
		 * 传递参数
		 * @param result 是异步请求的结果
		 */
		public void getResult(JSONObject result);
	}

	/**
	 * 调用API
	 */
	@Override
	protected Integer doInBackground(String... params) {
		UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		LinkedHashMap<String, Object> reqParams = new LinkedHashMap<String, Object>();		
		reqParams.put("decorationCode", params[0]);
		reqParams.put("imgUrl", params[1]);
		reqParams.put("title", params[2]);
		reqParams.put("tokenCode", userToken.getTokenCode());
		try {
			mResult = (JSONObject) API.reqShop("updateShopDecoration", reqParams);
			//如果调用api成功，返回正确的结果码
			return Integer.parseInt(String.valueOf(mResult.get("code")).toString()); 
		} catch (SzException e) {
			this.mErrCode = e.getErrCode();
			return this.mErrCode.getCode();
		}
	}
}
