package com.huift.hfq.shop.model;

import android.app.Activity;

import com.huift.hfq.base.ErrorCode;
import com.huift.hfq.base.SzException;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.api.API;
import com.huift.hfq.base.model.SzAsyncTask;
import com.huift.hfq.shop.R;

import net.minidev.json.JSONObject;

import java.util.LinkedHashMap;

/**
 * @author wensi.yu
 * 密码重置获取验证码
 */
public class ResetIdenCodeTask extends SzAsyncTask<String, Integer, Integer>  {
	/**用户不存在**/
	public static final int INDENCODE_CODE_NOUSER = 20207;
	/** 定义一个正确的返回结果码 **/
	private final static int RIGHT_RET_CODE = 1;
	/** 定义一个错误的返回结果码 **/
	private final static int ERROR_RET_CODE = 0;
	/** 调用API返回对象 **/
	private JSONObject mResult;
	/**回调方法**/
	private Callback callback;

	/**
	 * 构造函数
	 * @param acti
	 */
	public ResetIdenCodeTask(Activity acti) {
		super(acti);

	}

	/**
	 * 回调方法的构造函数
	 * @param acti
	 */
	public ResetIdenCodeTask(Activity acti,Callback callback) {
		super(acti);
		this.callback = callback;
	}

	/**
	 * 回调方法的接口
	 *
	 */
	public interface Callback{
		public void getResult(JSONObject mResult);
	}

	/**
	 * 提交数据到服务器，获取验证码。
	 * @param params [0]手机号；[1]动作。
	 */
	@Override
	protected Integer doInBackground(String... params) {
		//获取验证码
		LinkedHashMap<String, Object> reqParams= new LinkedHashMap<String, Object>();
		reqParams.put("mobileNbr",params[0]);
		reqParams.put("action","f");
		reqParams.put("appType","s");//商家端
		try {
			mResult = (JSONObject) API.reqComm("getValidateCode", reqParams);
			long retIdenCode = (Long)mResult.get("code");
			return Integer.parseInt(String.valueOf(retIdenCode));

		} catch (SzException e) {
			this.mErrCode=e.getErrCode();
			return this.mErrCode.getCode();
		}
	}

	/**
	 * onPostExecute()中的正常业务逻辑处理.
	 */
	@Override
	protected void handldBuziRet(int retCode) {
		if ( retCode == ErrorCode.SUCC ) {
			callback.getResult(mResult);

		} else {
			if (retCode == ErrorCode.FAIL ) {
				callback.getResult(null);
				Util.getContentValidate(R.string.toast_register_indencodeerror);

			}
			else if(retCode == INDENCODE_CODE_NOUSER){
				callback.getResult(null);
				Util.getContentValidate(R.string.toast_login_nouser);

			}
		}
	}
}
