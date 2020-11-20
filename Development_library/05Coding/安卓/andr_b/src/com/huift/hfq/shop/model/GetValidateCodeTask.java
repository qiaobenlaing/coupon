package com.huift.hfq.shop.model;

import java.util.LinkedHashMap;

import com.huift.hfq.base.ErrorCode;
import com.huift.hfq.base.SzApplication;
import com.huift.hfq.base.SzException;
import com.huift.hfq.base.TimeCountUtil;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.api.API;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.model.SzAsyncTask;
import com.huift.hfq.base.pojo.UserRegister;
import com.huift.hfq.base.pojo.UserToken;
import com.huift.hfq.shop.activity.LoginActivity;
import com.huift.hfq.shop.activity.RegisterActivity;
import com.huift.hfq.shop.activity.ResetPwdActivity;
import com.huift.hfq.shop.model.AccntStatListTask.Callback;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import com.huift.hfq.shop.R;

/**
 * @author wensi.yu
 * 获取验证码
 */
public class GetValidateCodeTask extends SzAsyncTask<String, integer, Integer>  {

	private final static String TAG = GetValidateCodeTask.class.getSimpleName();
	
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
	/** 调用API返回对象 **/
	private JSONObject mResult;
	/**回调方法**/ 	
	private Callback callback;
	
	/**
	 * 构造函数
	 * @param acti
	 */
	public GetValidateCodeTask(Activity acti) {
		super(acti);
	}
	
	/**
	 * 回调方法的构造函数
	 * @param acti
	 */
	public GetValidateCodeTask(Activity acti,Callback callback) {
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
		UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		reqParams.put("mobileNbr",params[0]);
		reqParams.put("action",params[1]); // 动作 ： r 是注册或者激活  f:找回密码  mr：添加接收短信的人
		reqParams.put("appType",params[2]);//商家端:s  顾客端 ： c
		if (null != userToken) {
			JSONObject extra = new JSONObject();
			extra.put("shopCode", userToken.getShopCode());
			reqParams.put("extra",extra.toString());
		}
		
		try {
			mResult = (JSONObject) API.reqComm("getValidateCode", reqParams);
			long retIdenCode = (Long)mResult.get("code");
			Log.d(TAG, "Msgcode==========="+retIdenCode);
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
			Log.d(TAG, "valCode========="+valCode);
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
			else if (retCode == INDENCODE_CODE_ERROR) {
				callback.getResult(null);
				Util.getContentValidate(R.string.indencode_code_error);
			 }
			else if (retCode == INDENCODE_REGISTER_ERROR) {
				callback.getResult(null);
				Util.getContentValidate(R.string.indencode_register_error);
			} else {
				callback.getResult(null);
			}
		}
	}
}
