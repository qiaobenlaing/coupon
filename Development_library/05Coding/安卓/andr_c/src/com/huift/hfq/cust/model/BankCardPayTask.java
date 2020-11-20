package com.huift.hfq.cust.model;

import java.util.LinkedHashMap;

import net.minidev.json.JSONObject;
import android.app.Activity;
import android.util.Log;
import android.widget.Toast;
import com.huift.hfq.base.SzException;
import com.huift.hfq.base.api.API;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.model.SzAsyncTask;
import com.huift.hfq.base.pojo.UserToken;

public class BankCardPayTask extends SzAsyncTask<String, Integer, Integer> {
	
	
	//定义正确的返回结果码
	private final static int RIGHT_RET_CODE = 50000;
	//定义商家编码错误的结果码
	private final static int ERROR_SHOPE_CODE = 50314;
	//定义请输入商家编码的结果码
	private final static int ERROR_INPUT_SHOPE_CODE = 50317;
	//定义请输入消费金额的的结果码
	private final static int ERROR_INPUT_CONSUME = 50400;
	//定义消费金额不正确的结果码
	private final static int ERROR_FALSE_AMOUNT = 50401;
	
	//定义请输入用户编码的结果码
	private final static int ERROR_INPUT_USER_CODE = 50500;
	//定义用户编码错误的结果码
	private final static int ERROR_USER_CODE = 50503;
	//定义红包不可用的结果码
	private final static int ERROR_REDBAG_UNUSE = 50720;
	//定义红包已经过期的结果码
	private final static int ERROR_REDBAG_OUTTIME = 50724;
	//定义优惠券已经过期的结果码
	private final static int ERROR_COUPON_OUTTIME = 80220;
	//定义优惠券不可用的结果码
	private final static int ERROR_COUPON_UNUSE = 80227;
	//定义用户会员卡不可用的结果码
	private final static int ERROR_CARD_UNUSE = 80400;
	
	
	
	private final static String TAG = "ShopListTask";
	/** 定义一个正确的返回结果码 **/
	//private final static int RIGHT_RET_CODE = 1;
	/** 定义一个错误的返回结果码 **/
	private final static int ERROR_RET_CODE = 0;

	/** 调用API返回对象 **/
	private JSONObject mResult;

	/** 回调方法 **/
	private Callback mCallback;

	private String consumeCode;//订单编码
	private String orderNbr;//订单号

	
	
	public BankCardPayTask(Activity acti, Callback callback) {
		super(acti);
		this.mCallback = callback;
	}

	@Override
	protected void handldBuziRet(int retCode) {
		if(mCallback==null){
			return;
		}
		
		switch (retCode) {
		case RIGHT_RET_CODE:
			mCallback.getResult(consumeCode+"##"+orderNbr);
			break;
			
		case ERROR_SHOPE_CODE:
			Toast.makeText(mActivity, "商家编码错误", Toast.LENGTH_SHORT).show();
			mCallback.getResult(null);
			break;
			
		case ERROR_INPUT_SHOPE_CODE:
			Toast.makeText(mActivity, "请输入商家编码", Toast.LENGTH_SHORT).show();
			mCallback.getResult(null);
			break;
			
		case ERROR_INPUT_CONSUME:
			Toast.makeText(mActivity, "请输入消费金额", Toast.LENGTH_SHORT).show();
			mCallback.getResult(null);
			break;
			
		case ERROR_FALSE_AMOUNT:
			Toast.makeText(mActivity, "消费金额不正确", Toast.LENGTH_SHORT).show();
			mCallback.getResult(null);
			break;
			
		case ERROR_INPUT_USER_CODE:
			Toast.makeText(mActivity, "请输入用户编码", Toast.LENGTH_SHORT).show();
			mCallback.getResult(null);
			break;
			
		case ERROR_USER_CODE:
			Toast.makeText(mActivity, "用户编码错误", Toast.LENGTH_SHORT).show();
			mCallback.getResult(null);
			break;
			
		
		case ERROR_REDBAG_UNUSE:
			Toast.makeText(mActivity, "红包不可用", Toast.LENGTH_SHORT).show();
			mCallback.getResult(null);
			break;
			
		case ERROR_REDBAG_OUTTIME:
			Toast.makeText(mActivity, "红包已经过期", Toast.LENGTH_SHORT).show();
			mCallback.getResult(null);
			break;
			
		case ERROR_COUPON_OUTTIME:
			Toast.makeText(mActivity, "优惠券已经过期", Toast.LENGTH_SHORT).show();
			mCallback.getResult(null);
			break;
			
		case ERROR_COUPON_UNUSE:
			Toast.makeText(mActivity, "优惠券不可用", Toast.LENGTH_SHORT).show();
			mCallback.getResult(null);
			break;
			
		
		case ERROR_CARD_UNUSE:
			Toast.makeText(mActivity, "用户会员卡不可用", Toast.LENGTH_SHORT).show();
			mCallback.getResult(null);
			break;
				
		default:
			break;
		}
	}

	@Override
	protected Integer doInBackground(String... params) {
		// 获得一个用户信息对象
		UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		String userCode = userToken.getUserCode();// 用户编码
		String tokenCode = userToken.getTokenCode();// 需要令牌认证
		
		LinkedHashMap<String, Object> reqparams = new LinkedHashMap<String, Object>();
		reqparams.put("userCode", userCode);
		reqparams.put("shopCode", params[0]);
		reqparams.put("orderAmount", params[1]);
		reqparams.put("batchCouponCode", params[2]);
		reqparams.put("nbrCoupon", params[3]);
		reqparams.put("platBonus", params[4]);	
		reqparams.put("shopBonus", params[5]);	
		reqparams.put("noDiscountPrice", params[6]);	
		reqparams.put("tokenCode", tokenCode);
		try {
			mResult = (JSONObject) API.reqCust("bankcardPayForAndroid", reqparams);
			if(mResult!=null){
				int retCode = Integer.parseInt(mResult.get("code").toString());
				if(retCode == RIGHT_RET_CODE){
					consumeCode = mResult.get("consumeCode").toString();
					orderNbr = mResult.get("orderNbr").toString();
				}
				return retCode;
			}
			
		} catch (SzException e) {
			this.mErrCode = e.getErrCode();
			return this.mErrCode.getCode();//返回错误编码
		}
		return null;
	}

	/**
	 * 回调方法的接口
	 * 
	 */
	public interface Callback {
		/**
		 * 传递参数
		 * 
		 * @param result
		 *            是异步请求的结果
		 */
		public void getResult(String result);
	}
}
