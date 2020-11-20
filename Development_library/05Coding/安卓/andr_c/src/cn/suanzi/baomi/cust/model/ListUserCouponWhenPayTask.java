package cn.suanzi.baomi.cust.model;

import java.util.LinkedHashMap;

import net.minidev.json.JSONArray;
import android.app.Activity;
import android.util.Log;
import cn.suanzi.baomi.base.SzException;
import cn.suanzi.baomi.base.api.API;
import cn.suanzi.baomi.base.data.DB;
import cn.suanzi.baomi.base.model.SzAsyncTask;
import cn.suanzi.baomi.base.pojo.UserToken;

public class ListUserCouponWhenPayTask extends SzAsyncTask<String, Integer, Integer> {
	private static final String TAG = ListUserCouponWhenPayTask.class.getSimpleName();
	private  JSONArray mResult;
	
	private static final int RIGHT_CODE = 1;
	private static final int ERROR_CODE = -1;

	
	public  interface CallBack{
		public void getResult(JSONArray result);
	}
	
	private CallBack mCallBack;
	
	public ListUserCouponWhenPayTask(Activity acti, CallBack mCallBack) {
		super(acti);
		this.mCallBack = mCallBack;
	}
	
	@Override
	protected Integer doInBackground(String...  params) {
		// 获得一个用户信息对象
		UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		String tokenCode = userToken.getTokenCode();
		String userCode = userToken.getUserCode();
		
		LinkedHashMap<String, Object> reqparams = new LinkedHashMap<String, Object>();
		reqparams.put("userCode",userCode);
		reqparams.put("shopCode", params[0]);
		reqparams.put("consumeAmount", params[1]);
		reqparams.put("couponType", params[2]);
		reqparams.put("tokenCode",tokenCode );
		
		try {
			mResult = (JSONArray) API.reqCust("listUserCouponWhenPay", reqparams);
			if(mResult!=null||!"[]".equals(mResult.toString())){
				Log.d(TAG,"ListUserCouponWhenPayTask==="+mResult.toString());
				return RIGHT_CODE;
			}else{
				return ERROR_CODE;
			}
			
		} catch (SzException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	@Override
	protected void handldBuziRet(int retCode) {
		switch (retCode) {
		case RIGHT_CODE:
			mCallBack.getResult(mResult);
			break;
			
		case ERROR_CODE:
			mCallBack.getResult(null);
			break;

		default:
			break;
		}
	}


}
