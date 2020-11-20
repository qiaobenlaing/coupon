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
 * 查询工行特惠信息的异步任务类
 * @author qian.zhou
 */
public class UserTotalBonusValueTask extends SzAsyncTask<String, String, Integer> {

	private final static String TAG = UserTotalBonusValueTask.class.getSimpleName();
	/** 定义一个正确的返回结果码 **/
	private final static int RIGHT_RET_CODE = 1;
	/** 定义一个错误的返回结果码 **/
	private final static int ERROR_RET_CODE = 0;
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
	public UserTotalBonusValueTask(Activity acti) {
		super(acti);
	}
	
	/**
	 * @param acti 上下文
	 * @param mCallback 回调方法
	 */
	public UserTotalBonusValueTask(Activity acti, Callback mCallback) {
		super(acti);
		this.mCallback = mCallback;
	}
	
	@Override
	protected void onPreExecute() {
		if (mProcessDialog != null) {
			mProcessDialog.dismiss();
		}
	}
	
	/**
	 * 业务逻辑操作
	 */
	@Override
	protected void handldBuziRet(int retCode) {
		if (retCode == RIGHT_RET_CODE) {
			mCallback.getResult(mResult) ;
		}else if(retCode==ERROR_RET_CODE){
			mCallback.getResult(null);
		}
	}

	/**
	 * 调用Api
	 */
	@Override
	protected Integer doInBackground(String... params) {
		UserToken token = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		String userCode = token.getUserCode();
		String tokenCode = token.getTokenCode();
		LinkedHashMap<String, Object> reqParams = new LinkedHashMap<String, Object>() ;
		reqParams.put("userCode" , userCode) ;
		reqParams.put("shopCode" , params[0]) ;
		reqParams.put("tokenCode" ,tokenCode) ;
		try {
			int retCode = ERROR_RET_CODE;
			mResult = (JSONObject)API.reqCust("userTotalBonusValue", reqParams);
			//判断查询的一个对象不为空为空 就返回一个正确的编码
			if ( !(mResult == null || "".equals(mResult.toJSONString()))) {
				retCode = RIGHT_RET_CODE; //1 代表访问成功
			}
			return retCode;
		} catch (SzException e) {
			this.mErrCode = e.getErrCode() ;
			return this.mErrCode.getCode() ;
		}
	}
}
