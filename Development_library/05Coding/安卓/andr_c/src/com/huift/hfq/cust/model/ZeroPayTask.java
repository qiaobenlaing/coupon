package com.huift.hfq.cust.model;

import java.util.LinkedHashMap;

import net.minidev.json.JSONObject;
import android.app.Activity;
import android.util.Log;
import com.huift.hfq.base.ErrorCode;
import com.huift.hfq.base.SzException;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.api.API;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.model.SzAsyncTask;
import com.huift.hfq.base.pojo.UserToken;
import com.huift.hfq.cust.R;

/**
 * 实物券 和 体验券
 * @author ad
 *
 */
public class ZeroPayTask extends SzAsyncTask<String, Integer, Integer> {
	private final static String TAG = ZeroPayTask.class.getSimpleName();
	/** 用户编码为空*/
	private final static int NULL_USER = 50503; 
	/** 用户编码不存在*/
	private final static int EXTIS_USER = 50500; 
	/** 商店编码不存在*/
	private final static int EXTIS_SHOP = 50314; 
	/** 商店编码为空*/
	private final static int NULL_SHOP = 50317; 
	/**优惠券不可用*/
	private final static int COUPON_NOUSE = 80227; 
	/**优惠券过期*/
	private final static int COUPON_EXPIRED = 80220; 
	/** 优惠券为空*/
	private final static int EITIS_COUPON_CODE = 51000;
	
	/** 定义一个正确的返回结果码 **/
	private final static int RIGHT_RET_CODE = 1;
	/** 定义一个错误的返回结果码 **/
	private final static int ERROR_RET_CODE = 0;
	/** 获得一个用户信息对象 **/
	private UserToken mUserToken;
	/** 用户登录后获得的令牌 **/
	private String mTokenCode;
	/** 回调方法 **/
	private Callback mCallback;
	/** 调用API返回对象 **/
	private JSONObject mResult;

	public ZeroPayTask(Activity acti, Callback callback) {
		super(acti);
		this.mCallback = callback;
	}

	/**
	 * 回调方法的接口
	 * 
	 */
	public interface Callback {
		// 传递参数
		// 是异步请求的结果
		public void getResult(int code,JSONObject result);
	}
	
	@Override
	protected void onPreExecute() {
		if (mActivity != null) {
			if (mProcessDialog != null) {
				mProcessDialog.dismiss();
			}
		}
	}

	@Override
	protected Integer doInBackground(String... params) {
		mUserToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		mTokenCode = mUserToken.getTokenCode();// 用户登录后获得的令牌
		String userCode = mUserToken.getUserCode();
		LinkedHashMap<String, Object> reqparams = new LinkedHashMap<String, Object>();
		reqparams.put("userCode", userCode);
		reqparams.put("shopCode", params[0]);
		reqparams.put("userCouponCode", params[1]);
		reqparams.put("appType", "1"); // 1：代表是C端调用的方法
		reqparams.put("tokenCode", mTokenCode);
		try {
			// 调用API
			mResult = (JSONObject) API.reqCust("zeroPay", reqparams);
			
			int retCode = ERROR_RET_CODE;
			// 判断查询的一个对象不为空为空 就返回一个正确的编码
			if ( mResult != null || !"".equals(mResult.toJSONString())) {
				retCode = RIGHT_RET_CODE; // 1 代表访问成功
			}
			return retCode;
		} catch (SzException e) {
			Log.d(TAG, "e.getErrCode()="+e.getErrCode());
			this.mErrCode = e.getErrCode();
			return this.mErrCode.getCode();// 返回错误编码
		}
	}
	
	@Override
	protected void handldBuziRet(int retCode) {
		// RIGHT_RET_CODE的值是1 如果retCode访问成功的发 返回的是1
		if (retCode == RIGHT_RET_CODE) {
			int code = Integer.parseInt(mResult.get("code").toString());
			if (code == ErrorCode.SUCC) {
				mCallback.getResult(ErrorCode.SUCC,mResult);
			} else if (code == EXTIS_USER || code == NULL_USER){ // 用户不存在
				Util.getContentValidate(R.string.exist_user);
				mCallback.getResult(ErrorCode.API_INTERNAL_ERR,null);
			} else if (code == EXTIS_SHOP || code == NULL_SHOP) {
				Util.getContentValidate(R.string.exist_shop);
				mCallback.getResult(ErrorCode.API_INTERNAL_ERR,null);
			} else if (code == ErrorCode.API_INTERNAL_ERR) {
				Util.getContentValidate(R.string.service_exception);
				mCallback.getResult(ErrorCode.API_INTERNAL_ERR,null);
			} else if (code == EITIS_COUPON_CODE) {
				Util.getContentValidate(R.string.null_coupon);
				mCallback.getResult(ErrorCode.API_INTERNAL_ERR,null);
			} else if (code == COUPON_NOUSE) {
				Util.getContentValidate(R.string.no_use_coupon);
				mCallback.getResult(ErrorCode.FAIL,null);
			} else if (code == COUPON_EXPIRED) {
			Util.getContentValidate(R.string.coupon_expired);
			mCallback.getResult(ErrorCode.API_INTERNAL_ERR,null);
		}
		} else {
			mCallback.getResult(ErrorCode.FAIL,null);
		}
	}
}
