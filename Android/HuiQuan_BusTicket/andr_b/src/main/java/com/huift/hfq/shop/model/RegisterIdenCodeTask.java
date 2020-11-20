package com.huift.hfq.shop.model;

import android.R.integer;
import android.app.Activity;
import android.content.Intent;

import com.huift.hfq.base.ErrorCode;
import com.huift.hfq.base.SzException;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.api.API;
import com.huift.hfq.base.model.SzAsyncTask;
import com.huift.hfq.shop.R;
import com.huift.hfq.shop.activity.LoginActivity;

import net.minidev.json.JSONObject;

import java.util.LinkedHashMap;

/**
 * @author wensi.yu
 * 获取验证码
 */
public class RegisterIdenCodeTask extends SzAsyncTask<String, integer, Integer>  {

	/**手机号码未提交审核**/
	public static final int REGISTER_INSPECT_NO = 60200;
	/**手机号码审核还未通过，请耐心等待**/
	public static final int REGISTER_INSPECT_WAIT = 60201;
	/**手机号码审核通过**/
	public static final int REGISTER_INSPECT_OK = 60202;
	/**商家已经审核过不能重复审核**/
	public static final int REGISTER_INSPECT_AGAIN = 60203;
	/**请求失败*/
	public static final int INDENCODE_CODE_ERROR = 80010;
	/**手机号码已经使用*/
	public static final int INDENCODE_REGISTER_ERROR = 60003;
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
	public RegisterIdenCodeTask(Activity acti) {
		super(acti);
	}

	/**
	 * 回调方法的构造函数
	 * @param acti
	 */
	public RegisterIdenCodeTask(Activity acti,Callback callback) {
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
		LinkedHashMap<String, Object> reqParams= new LinkedHashMap<String, Object>();
		reqParams.put("mobileNbr",params[0]);
		reqParams.put("action","r");
		reqParams.put("appType","s");//商家端
		try {
			mResult = (JSONObject) API.reqComm("getValidateCode", reqParams);
			long retIdenCode = (Long)mResult.get("code");
			return Integer.parseInt(String.valueOf(retIdenCode));

		} catch (SzException e) {
			this.mErrCode = e.getErrCode();
			return this.mErrCode.getCode();//返回错误编码
		}
	}

	/**
	 * onPostExecute()中的正常业务逻辑处理.
	 */
	@Override
	protected void handldBuziRet(int retCode) {
		if ( retCode == ErrorCode.SUCC ) {
			callback.getResult(mResult);
			String valCode =  mResult.get("valCode").toString();
		} else {
			if (retCode == ErrorCode.FAIL) {
				Util.getContentValidate(R.string.toast_register_indencodeerror);
				callback.getResult(null);
			}
			else if(retCode == REGISTER_INSPECT_NO){
				callback.getResult(null);
				Util.getContentValidate(R.string.toast_inspect_no);
			}
			else if(retCode == REGISTER_INSPECT_WAIT){
				callback.getResult(null);
				Util.getContentValidate(R.string.toast_inspect_wait);
			}
			else if(retCode == REGISTER_INSPECT_OK){
				callback.getResult(null);
				Util.getContentValidate(R.string.toast_inspect_ok);
				Intent it = new Intent(mActivity, LoginActivity.class);
				mActivity.startActivity(it);
			}
			else if(retCode == REGISTER_INSPECT_AGAIN){
				callback.getResult(null);
				Util.getContentValidate(R.string.toast_inspect_again);
			}
		}
	}
}
