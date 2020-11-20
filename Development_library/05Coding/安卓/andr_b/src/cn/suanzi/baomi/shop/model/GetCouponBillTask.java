package cn.suanzi.baomi.shop.model;

import java.util.LinkedHashMap;

import net.minidev.json.JSONArray;
import android.app.Activity;
import android.util.Log;
import cn.suanzi.baomi.base.SzException;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.api.API;
import cn.suanzi.baomi.base.data.DB;
import cn.suanzi.baomi.base.model.SzAsyncTask;
import cn.suanzi.baomi.base.pojo.UserToken;
import cn.suanzi.baomi.shop.R;

/**
 * 查询优惠券的对账
 * @author wensi.yu
 *
 */
public class GetCouponBillTask extends SzAsyncTask<String, String, Integer>{

	private final static String TAG = "GetCouponBill";
	
	/***创建一个JSONArray对象**/
	private JSONArray mResult;
	/** 定义一个正确的返回结果码 **/ 
	private final static int RIGHT_RET_CODE = 1;
	/** 定义一个错误的返回结果码 **/ 
	private final static int ERROR_RET_CODE = 0;
	/**回调方法**/ 	
	private Callback callback;
	
	public GetCouponBillTask(Activity acti) {
		super(acti);
	}
	
	/**
	 * 回调方法的构造函数
	 * @param acti
	 */
	public GetCouponBillTask(Activity acti,Callback callback) {
		super(acti);
		this.callback=callback;
	}
	
	/**  
     * 回调方法的接口  
     *  
     */  
    public interface Callback{  
        public void getResult(JSONArray mResult);  
    }
	

	/**
	 * 查询优惠券
	 */
	@Override
	protected Integer doInBackground(String... params) {
		UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		String tokenCode = userToken.getTokenCode();
		LinkedHashMap<String, Object> reqParams = new LinkedHashMap<String, Object>();
		reqParams.put("shopCode", params[0]);
		reqParams.put("startDate", params[1]);
		reqParams.put("endDate", params[2]);
		reqParams.put("tokenCode", tokenCode);		
		
		try {
			mResult = (JSONArray)API.reqShop("getCouponBill", reqParams);
			Log.d(TAG, "mResult==============="+mResult);
			//判断查询的一个对象不为空为空 就返回一个正确的编码
			int retCode = ERROR_RET_CODE;
			if (null != mResult) {
				retCode = RIGHT_RET_CODE; //1 代表访问成功
			}
			Log.d(TAG, "retCode============"+retCode);
			return retCode;
			
		} catch (SzException e) {
			this.mErrCode = e.getErrCode();
			return this.mErrCode.getCode();
		}
	}
	
	/**
	 * 处理业务逻辑
	 */
	@Override
	protected void handldBuziRet(int retCode) {
		if (retCode == RIGHT_RET_CODE) {
			callback.getResult(mResult);
		}else{ 
			if(retCode == ERROR_RET_CODE) {
				Util.getContentValidate(R.string.toast_coupon_error);
				callback.getResult(null);
			}
		}
	}
}
