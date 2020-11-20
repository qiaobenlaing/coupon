package com.huift.hfq.shop.model;

import java.util.LinkedHashMap;

import com.huift.hfq.base.SzException;
import com.huift.hfq.base.api.API;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.model.SzAsyncTask;
import com.huift.hfq.base.pojo.UserToken;

import net.minidev.json.JSONArray;
import android.app.Activity;

/**
 * 查询门店订单信息列表
 * @author qian.zhou
 */
public class GetNTakeoutOrderTask extends SzAsyncTask<String , String , Integer> {
	/**创建一个JSONArray对象**/
	private JSONArray mResult ;
	/**定义一个正确的返回结果码**/
	private final static int RIGHT_RET_CODE = 1 ;
	/**定义一个错误的返回结果码**/ 
	private final static int ERROR_RET_CODE = 0;
	/**回调方法**/
	private Callback mCallback ;

	/**
	 * 回调方法的接口
	 */
	public interface Callback {
		public void getResult(JSONArray JSONobject) ;
	}
	
	/**
	 * 构造函数
	 * @param acti启动本对象的Activity
	 */
	public GetNTakeoutOrderTask(Activity acti) {
		super(acti) ;
	}

	/**
	 * @param acti 上下文
	 * @param callback 回调方法
	 */
	public GetNTakeoutOrderTask(Activity acti , Callback callback) {
		super(acti) ;
		this.mCallback = callback;
	}

	/**
	 * onPostExecute()中的正常业务逻辑处理.
	 */
	@Override
	protected void handldBuziRet(int retCode) {
		if (retCode == RIGHT_RET_CODE) {
			mCallback.getResult(mResult) ;
		}else if(retCode == ERROR_RET_CODE){
			mCallback.getResult(null);
		}
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
	 * 调用API，获得所有员工信息列表
	 */
	@Override
	protected Integer doInBackground(String... params) {
		UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		LinkedHashMap<String, Object> reqParams = new LinkedHashMap<String, Object>() ;
		reqParams.put("shopCode" , userToken.getShopCode()) ;
		reqParams.put("orderType" , params[0]) ;
		reqParams.put("keyWard" , params[1]) ;
		reqParams.put("value" , params[2]) ;
		reqParams.put("unit" , params[3]) ;
		reqParams.put("status" , params[4]) ;
		reqParams.put("tokenCode" , userToken.getTokenCode()) ;
		try {
			mResult = (JSONArray)API.reqShop("getOrderB", reqParams);
			int retCode = ERROR_RET_CODE;
			//判断查询的一个对象不为空为空 就返回一个正确的编码
			if (mResult.size() != 0 || !"[]".equals(mResult.toString())) {
				retCode = RIGHT_RET_CODE; //1 代表访问成功
			}
			return retCode;
		}catch (SzException e) {
			this.mErrCode = e.getErrCode() ;
			return this.mErrCode.getCode() ;
		}
	}
}
