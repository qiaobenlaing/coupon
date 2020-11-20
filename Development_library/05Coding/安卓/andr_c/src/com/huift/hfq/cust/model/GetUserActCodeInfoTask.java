package com.huift.hfq.cust.model;

import java.util.LinkedHashMap;

import com.huift.hfq.cust.application.CustConst;
import com.huift.hfq.cust.fragment.HomeFragment;

import net.minidev.json.JSONObject;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import com.huift.hfq.base.ErrorCode;
import com.huift.hfq.base.SzException;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.api.API;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.model.SzAsyncTask;
import com.huift.hfq.base.pojo.Citys;
import com.huift.hfq.base.pojo.UserToken;

/**
 * 获得用户活动码详情
 * @author yanfang.li
 */
public class GetUserActCodeInfoTask extends SzAsyncTask<String, String, Integer> {

	private final static String TAG = GetUserActCodeInfoTask.class.getSimpleName();
	/** 调用API返回对象*/
	private JSONObject mResult;
	/** 回调方法 **/
	private Callback mCallback;
	
	/**
	 * 回调方法的接口
	 */
	public interface Callback {
		public void getResult(JSONObject jsonobject) ;
	}
	
	/**
	 * 构造函数
	 * @param acti
	 */
	public GetUserActCodeInfoTask(Activity acti) {
		super(acti);
	}
	
	/**
	 * @param acti 上下文
	 * @param mCallback 回调方法
	 */
	public GetUserActCodeInfoTask(Activity acti, Callback mCallback) {
		super(acti);
		this.mCallback = mCallback;
	}

	@Override
	protected void onPreExecute() {
		if (null != mActivity && null != mProcessDialog) {
			mProcessDialog.dismiss();
		}
	}

	/**
	 * 业务逻辑操作
	 */
	@Override
	protected void handldBuziRet(int retCode) {
		if (retCode == ErrorCode.RIGHT_RET_CODE) {
			mCallback.getResult(mResult) ;
		}else {
			mCallback.getResult(null);
		}
	}

	/**
	 * 调用Api
	 */
	@Override
	protected Integer doInBackground(String... params) {
		UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		String tokenCode = userToken.getTokenCode();// 用户登录后获得的令牌
		// 请求参数
		LinkedHashMap<String, Object> reqParams = new LinkedHashMap<String, Object>() ;
		reqParams.put("actCode" , params[0]) ;
		reqParams.put("tokenCode", tokenCode) ;
		try {
			int retCode = ErrorCode.ERROR_RET_CODE;
			mResult = (JSONObject)API.reqCust("getUserActCodeInfo", reqParams);
			//判断查询的一个对象不为空为空 就返回一个正确的编码
			if ( mResult != null || !"".equals(mResult.toJSONString())) {
				retCode = ErrorCode.RIGHT_RET_CODE; //1 代表访问成功
			}
			return retCode;
		} catch (SzException e) {
			this.mErrCode = e.getErrCode() ;
			return this.mErrCode.getCode() ;
		}
	}
}
