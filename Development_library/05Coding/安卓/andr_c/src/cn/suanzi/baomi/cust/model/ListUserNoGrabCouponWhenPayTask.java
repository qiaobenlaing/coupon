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

/**
 * 查询商家发行的优惠券的异步任务类
 * @author yingchen
 *
 */
public class ListUserNoGrabCouponWhenPayTask extends SzAsyncTask<String, Integer, Integer> {
	private static final String TAG = ListUserNoGrabCouponWhenPayTask.class.getSimpleName();
	/**定义一个正确的返回结果码**/
	private final static int RIGHT_RET_CODE = 1 ;
	/**定义一个错误的返回结果码**/ 
	private final static int ERROR_RET_CODE = 0;
	
	public interface CallBack{
		public void getResult(JSONArray result);
	}
	
	private CallBack mCallBack;
	
	private JSONArray mResult;
	public ListUserNoGrabCouponWhenPayTask(Activity acti, CallBack mCallBack) {
		super(acti);
		this.mCallBack = mCallBack;
	}

	@Override
	protected Integer doInBackground(String... params) {
		UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		String userCode = userToken.getUserCode();
		String tokenCode = userToken.getTokenCode();
		
		LinkedHashMap<String, Object> reqparams = new LinkedHashMap<String, Object>();
		reqparams.put("userCode", userCode);
		reqparams.put("shopCode", params[0]);
		reqparams.put("consumeAmount", params[1]);
		reqparams.put("couponType", params[2]);
		reqparams.put("tokenCode", tokenCode);
		
		try {
			mResult = (JSONArray) API.reqCust("listUserNoGrabCouponWhenPay", reqparams);
			/* Object reqCust = API.reqCust("listUserNoGrabCouponWhenPay", reqparams);
			 Log.d(TAG, "obejct==="+reqCust.toString());*/
			int retCode = ERROR_RET_CODE;
			if(null!=mResult&&!"[]".equals(mResult.toString())){
				Log.d(TAG, "ListUserNoGrabCouponWhenPayTask==="+mResult.toString());
				retCode = RIGHT_RET_CODE;
			}
			return retCode;
		} catch (SzException e) {
			this.mErrCode = e.getErrCode();
			return this.mErrCode.getCode();
		}
	}
	
	@Override
	protected void handldBuziRet(int retCode) {
		if(retCode == RIGHT_RET_CODE){
			mCallBack.getResult(mResult);
		}else if(retCode == ERROR_RET_CODE){
			mCallBack.getResult(null);
		}
	}

}
