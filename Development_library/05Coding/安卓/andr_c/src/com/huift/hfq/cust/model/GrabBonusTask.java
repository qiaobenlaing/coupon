package com.huift.hfq.cust.model;

import java.util.LinkedHashMap;

import net.minidev.json.JSONObject;
import android.app.Activity;
import com.huift.hfq.base.SzException;
import com.huift.hfq.base.api.API;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.model.SzAsyncTask;
import com.huift.hfq.base.pojo.UserToken;

/**
 * 抢红包
 * @author qian.zhou
 */
public class GrabBonusTask extends SzAsyncTask<String, Integer, Integer> {
	private final static String TAG = "GrabBonusTask";
	private final static int SUCC_CODE  = 1;
	private final static int ERROR_CODE  = 2;
	private JSONObject mResult;
	private Callback mCallback; 
	
	/**
	 * 构造函数
	 */
	public GrabBonusTask(Activity acti) {
		super(acti);
	}
	
	@Override
	protected void onPreExecute() {
		if (null != mActivity && mProcessDialog != null) {
			mProcessDialog.dismiss();
		}
	}
	
	/**
	 * 有参构造方法
	 * @param acti 调用者的上下文
	 * @param callback 回调方法
	 */
	public GrabBonusTask(Activity acti, Callback callback) {
		super(acti);
		mCallback = callback;
	}
	
	/**  
     * 回调方法的接口  
     *  
     */
	public interface Callback{
		public void getResult(JSONObject result);
	}

	@Override
	protected Integer doInBackground(String... params) {
		// 获得一个用户信息对象
		UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		String userCode = userToken.getUserCode();
		String tokenCode = userToken.getTokenCode();
		LinkedHashMap<String, Object> reqparams = new LinkedHashMap<String, Object>();
		reqparams.put("userCode", userCode);
		reqparams.put("bonusCode", params[0]);
		reqparams.put("tokenCode", tokenCode);

		try {
			// 调用API
		    mResult = (JSONObject) API.reqCust("grabBonus", reqparams);
			int retCode = ERROR_CODE;
			if (mResult != null || !"".equals(mResult.toJSONString())) {
				retCode = SUCC_CODE;
			}
			return retCode;
		} catch (SzException e) {
			this.mErrCode = e.getErrCode();
			return this.mErrCode.getCode();//返回错误编码
		}
	}

	@Override
	protected void handldBuziRet(int retCode) {
		if (retCode == SUCC_CODE) {
			mCallback.getResult(mResult);
		} else {
			mCallback.getResult(null);
		}
	}
}
