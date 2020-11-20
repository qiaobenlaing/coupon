package com.huift.hfq.shop.model;

import java.util.LinkedHashMap;

import com.huift.hfq.base.ErrorCode;
import com.huift.hfq.base.SzException;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.api.API;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.model.SzAsyncTask;
import com.huift.hfq.base.pojo.UserToken;

import net.minidev.json.JSONObject;
import android.app.Activity;
import com.huift.hfq.shop.R;

/**
 * 添加短信接受者
 * @author yanfang.li
 */
public class AddMRecipientTask extends SzAsyncTask<String , String , Integer> {
	
	private static final String TAG = AddMRecipientTask.class.getSimpleName();
	/** 手机号不能为空*/
	private static final int NULL_TEL = 60000;
	/** 手机号格式错误*/
	private static final int TEL_ERROR = 60001;
	/** 手机号重复*/
	private static final int TEL_REPEAT = 60003;
	/** 验证码不能为空*/
	private static final int NULL_CODE = 80012;
	/** 验证码错误*/
	private static final int CODE_ERROR = 80011;
	/** 超过设置数量*/
	private static final int TOO_MUCH = 87001;
	
	/**创建一个JSONObject对象**/
	private JSONObject mResult ;
	
	/**
	 * 构造函数
	 * @param acti
	 */
	public AddMRecipientTask(Activity acti) {
		super(acti) ;
	}

	/**
	 * 回调方法 	
	 */
	private Callback callback ;
		
	/**
	 * @param acti 上下文
	 * @param callback 回调方法
	 */
	public AddMRecipientTask(Activity acti , Callback callback) {
		super(acti) ;
		this.callback = callback ;
	}

	/**  
     * 回调方法的接口  
     */  
    public interface Callback{  
        public void getResult(boolean retFlag) ;  
    }
    
    /**
   	 * onPostExecute()中的正常业务逻辑处理.
   	 */
	@Override
	protected void handldBuziRet(int retCode) {
		if (retCode == ErrorCode.RIGHT_RET_CODE) {
			int code = Integer.parseInt(mResult.get("code").toString());
			switch (code) {
			case ErrorCode.SUCC:
				callback.getResult(true) ;
				break;
			case NULL_TEL: // 手机号码为空
				Util.getContentValidate(R.string.null_tel);
				callback.getResult(false) ;
				break;
			case TEL_ERROR:// 手机号格式错误
				Util.getContentValidate(R.string.tel_error);
				callback.getResult(false) ;
				break;
			case TEL_REPEAT: // 您已设置过改手机号码
				Util.getContentValidate(R.string.tel_repeat);
				callback.getResult(false) ;
				break;
			case NULL_CODE: // 验证码不能为空
				Util.getContentValidate(R.string.null_code);
				callback.getResult(false) ;
				break;
			case CODE_ERROR: // 验证码错误
				Util.getContentValidate(R.string.code_error);
				callback.getResult(false) ;
				break;
			case TOO_MUCH: // 商店设置手机号码最多只能设置5个
				Util.getContentValidate(R.string.too_much);
				callback.getResult(false) ;
				break;
			default:
				Util.getContentValidate(R.string.add_fail);
				callback.getResult(false);
				break;
			}
		}else{
			callback.getResult(false);
		}
	}
	
	/**
	 * 调用API,用于验证兑换券、代金券的验证和使用
	 */
	@Override
	protected Integer doInBackground(String... params) {
		UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		if (null == userToken) {
			return ErrorCode.RIGHT_RET_CODE;
		}
		LinkedHashMap<String , Object> reqParams = new LinkedHashMap<String , Object>() ;
		reqParams.put("shopCode" , userToken.getShopCode()) ; // 商家编码
		reqParams.put("creatorCode" , userToken.getStaffCode()) ; // 创建人的编码
		reqParams.put("staffName" , params[0]) ; // 员工编码
		reqParams.put("mobileNbr" , params[1]) ; // 手机号码
		reqParams.put("validateCode" , params[2]) ; // 验证码
		reqParams.put("tokenCode" , userToken.getTokenCode()) ;//需要令牌认证
		try {
			mResult = (JSONObject) API.reqShop("addMRecipient" , reqParams) ;
			int retCode = ErrorCode.ERROR_RET_CODE;
			if (mResult != null && !"".equals(mResult.toJSONString())) {
				retCode = ErrorCode.RIGHT_RET_CODE;
			}
			return retCode;
		} catch (SzException e) {
			this.mErrCode = e.getErrCode() ;
			return this.mErrCode.getCode() ;
		}
	}
}
