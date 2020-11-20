package cn.suanzi.baomi.cust.model;

import java.util.LinkedHashMap;

import net.minidev.json.JSONObject;

import android.app.Activity;
import android.util.Log;
import cn.suanzi.baomi.base.SzException;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.api.API;
import cn.suanzi.baomi.base.data.DB;
import cn.suanzi.baomi.base.model.SzAsyncTask;
import cn.suanzi.baomi.base.pojo.UserToken;

public class RemindToShopTask extends SzAsyncTask<String,Integer,Integer> {
	private static final String TAG = RemindToShopTask.class.getSimpleName();
	/**定义一个成功的结果码*/
	private static final int RIGHT_RET_CODE = 50000;
	/**定义一个失败的结果码*/
	private static final int ERROR_RET_CODE = 20000;
	/**定义一个重复操作的结果码*/
	private static final int REPEAT_OPEARTE_CODE = 78001;
	
	private JSONObject mResult;
	
	private CallBack mCallBack;
	
	public RemindToShopTask(Activity acti) {
		super(acti);
	}

	public RemindToShopTask(Activity acti, CallBack mCallBack) {
		super(acti);
		this.mCallBack = mCallBack;
	}


	public  interface CallBack{
		void getResult(boolean result);
	}
	
	@Override
	protected Integer doInBackground(String... params) {
		 UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		 String userCode = userToken.getUserCode();
		 String tokenCode = userToken.getTokenCode();
		
		LinkedHashMap<String, Object>  reqparams = new LinkedHashMap<String, Object>();
		reqparams.put("shopCode", params[0]);
		reqparams.put("userCode", userCode);
		reqparams.put("actionType", params[1]);
		reqparams.put("tokenCode", tokenCode);
		
		try {
			mResult = (JSONObject) API.reqCust("remindToShop", reqparams);
			int retCode = ERROR_RET_CODE;
			if(null != mResult){
				Log.d(TAG, "RemindToShopTask==="+mResult.toString());
				retCode = Integer.parseInt(mResult.get("code").toString());
			}
			return retCode;
		} catch (SzException e) {
			this.mErrCode = e.getErrCode();
			return this.mErrCode.getCode();//返回错误编码
		}
		
	}


	@Override
	protected void handldBuziRet(int retCode) {
		Log.d(TAG, "retCode==="+retCode);
		switch (retCode) {
		case RIGHT_RET_CODE: 
			mCallBack.getResult(true);
			break;
		case ERROR_RET_CODE: 
			Util.showToastZH("操作失败,请联系惠圈相关人员");
			mCallBack.getResult(false);
			break;
		case REPEAT_OPEARTE_CODE: 
			Util.showToastZH("请勿重复操作");
			mCallBack.getResult(false);
			break;

		default:
			Util.showToastZH("操作失败,请联系惠圈相关人员");
			mCallBack.getResult(false);
			break;
		}
	}


}
