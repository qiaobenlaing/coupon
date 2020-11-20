package com.huift.hfq.cust.model;

import java.util.LinkedHashMap;

import net.minidev.json.JSONObject;
import android.app.Activity;
import android.widget.Toast;
import com.huift.hfq.base.SzException;
import com.huift.hfq.base.api.API;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.model.SzAsyncTask;
import com.huift.hfq.base.pojo.UserToken;

/**
 * 产品订单的在线支付
 * 
 * @author yingchen
 * 
 */
public class POBankcardPayTask extends SzAsyncTask<String, Integer, Integer> {
	// 定义正确的返回结果码
	private final static int RIGHT_RET_CODE = 50000;

	// 定义红包不可用的结果码
	private final static int ERROR_REDBAG_UNUSE = 50720;

	// 定义红包已经过期的结果码
	private final static int ERROR_REDBAG_OUTTIME = 50724;

	// 定义优惠券已经过期的结果码
	private final static int ERROR_COUPON_OUTTIME = 80220;

	// 定义优惠券不可用的结果码
	private final static int ERROR_COUPON_UNUSE = 80227;

	// 定义用户会员卡不可用的结果码
	private final static int ERROR_CARD_UNUSE = 80400;
	
	// 订单编码不存在
	private static final int NOT_ORDER_EXIST = 60501;

	private String consumeCode;// 订单编码
	private String orderNbr;// 订单号

	/** 回调方法 **/
	private Callback mCallback;

	/** 调用API返回对象 **/
	private JSONObject mResult;
	
	

	public POBankcardPayTask(Activity acti) {
		super(acti);
	}

	public POBankcardPayTask(Activity acti, Callback callback) {
		super(acti);
		this.mCallback = callback;
	}

	@Override
	protected Integer doInBackground(String... params) {
		// 获得一个用户信息对象
		UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		String tokenCode = userToken.getTokenCode();// 需要令牌认证
				
		
		LinkedHashMap<String, Object> reqparams = new LinkedHashMap<String, Object>();
		reqparams.put("orderCode", params[0]);
		reqparams.put("batchCouponCode", params[1]);
		reqparams.put("nbrCoupon", params[2]);
		reqparams.put("platBonus", params[3]);
		reqparams.put("shopBonus", params[4]);
		reqparams.put("noDiscountPrice", params[5]);	
		reqparams.put("tokenCode", tokenCode);

		try {
			mResult = (JSONObject) API.reqCust("pOBankcardPayForAndroid", reqparams);
			if (mResult != null) {
				int retCode = Integer.parseInt(mResult.get("code").toString());
				if (retCode == RIGHT_RET_CODE) {
					consumeCode = mResult.get("consumeCode").toString();
					orderNbr = mResult.get("orderNbr").toString();
				}
				return retCode;
			}
		} catch (SzException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void handldBuziRet(int retCode) {
		if (mCallback == null) { return; }
		switch (retCode) {
		case RIGHT_RET_CODE:
			mCallback.getResult(consumeCode + "##" + orderNbr);
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
		case NOT_ORDER_EXIST:
			Toast.makeText(mActivity, "订单编码已取消,请重新下单", Toast.LENGTH_SHORT).show();
			mCallback.getResult(null);
			break;

		default:
			break;
		}
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
