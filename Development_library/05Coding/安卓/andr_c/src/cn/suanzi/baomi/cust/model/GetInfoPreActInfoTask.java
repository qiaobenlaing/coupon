package cn.suanzi.baomi.cust.model;

import java.util.LinkedHashMap;

import net.minidev.json.JSONObject;
import android.app.Activity;
import android.util.Log;
import cn.suanzi.baomi.base.ErrorCode;
import cn.suanzi.baomi.base.SzException;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.api.API;
import cn.suanzi.baomi.base.data.DB;
import cn.suanzi.baomi.base.model.SzAsyncTask;
import cn.suanzi.baomi.base.pojo.UserToken;
import cn.suanzi.baomi.cust.R;

/**
 * 获取购买活动 填写订单是具体信息
 * @author yingchen
 *
 */
public class GetInfoPreActInfoTask extends SzAsyncTask<String, Integer, Integer> {
	private static final String TAG = GetInfoPreActInfoTask.class.getSimpleName();
	/**定义成功的结果码*//*
	private static final int RIGHT_RET_CODE = 1;
	*//**定义失败的结果码*//*
	private static final int ERROR_RET_CODE = -1;*/
	
	private CallBack mCallBack;

	private JSONObject mResult;
	
	public GetInfoPreActInfoTask(Activity acti) {
		super(acti);
	}
	
	public GetInfoPreActInfoTask(Activity acti, CallBack mCallBack) {
		super(acti);
		this.mCallBack = mCallBack;
	}

	public interface CallBack{
		void getResult(JSONObject result);
	}
	
	@Override
	protected Integer doInBackground(String... params) {
		UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		String userCode = userToken.getUserCode();
		String tokenCode = userToken.getTokenCode();
		LinkedHashMap<String, Object> reqparams = new LinkedHashMap<String, Object>();
		reqparams.put("userCode", userCode);
		reqparams.put("actCode", params[0]);
		reqparams.put("tokenCode", tokenCode);
		
		try {
			mResult = (JSONObject)API.reqCust("getInfoPreActInfo", reqparams);
			int retCode = ErrorCode.ERROR_RET_CODE;
			if(null!=mResult){
				Log.d(TAG, "GetInfoPreActInfoTask==="+mResult.toString());
				retCode = ErrorCode.RIGHT_RET_CODE;
			}
			return retCode;
		} catch (SzException e) {
			this.mErrCode = e.getErrCode();
			return this.mErrCode.getCode();
		}
		
	}
	
	
	@Override
	protected void handldBuziRet(int retCode) {
		switch (retCode) {
		case  ErrorCode.RIGHT_RET_CODE:
			mCallBack.getResult(mResult);
			break;
		case  ErrorCode.ERROR_RET_CODE:
			Util.getContentValidate(R.string.no_promotion_exsit);
			mCallBack.getResult(null);
			break;

		default:
			mCallBack.getResult(null);
			break;
		}
	}
 

}
