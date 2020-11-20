package cn.suanzi.baomi.cust.model;

import java.util.LinkedHashMap;

import net.minidev.json.JSONObject;
import android.app.Activity;
import android.util.Log;
import cn.suanzi.baomi.base.ErrorCode;
import cn.suanzi.baomi.base.SzException;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.api.API;
import cn.suanzi.baomi.base.data.DB;
import cn.suanzi.baomi.base.model.SzAsyncTask;
import cn.suanzi.baomi.base.pojo.UserToken;
import cn.suanzi.baomi.cust.R;

/**
 * 9.41	选择优惠券或者红包后返回实际需付款金额
 * @author ad
 *
 */
public class GetNewPriceTask extends SzAsyncTask<String, Integer, Integer> {
	private final static String TAG = GetNewPriceTask.class.getSimpleName();
	/** 50725 平台红包不够用*/
	private final static int PLAT_BOUNS_MANY = 50725;
	/** 50726 商家红包不够用*/
	private final static int SHOP_BOUNS_MANY = 50726;
	/** 优惠券使用超过上限*/
	private final static int COUPON_LIMITED = 80236;
	/** 使用金额不够*/
	private final static int PAY_MONEY_MALL = 60513;
	/** 参与优惠的实际支付金额不能小于0*/
	private final static int COUPON_SUM_LESS = 60515;
	/**优惠券不可用*/
	private final static int COUPON_CAN_NOT_USE = 80227;
	/** 定义一个正确的返回结果码 **/
	private final static int RIGHT_RET_CODE = 1;
	/** 定义一个错误的返回结果码 **/
	private final static int ERROR_RET_CODE = 0;
	
	/** 回调方法 **/
	private Callback mCallback;
	/** 调用API返回对象 **/
	private JSONObject mResult;

	public GetNewPriceTask(Activity acti, Callback callback) {
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
		public void getResult(JSONObject result);
	}

	@Override
	protected Integer doInBackground(String... params) {
		UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		String tokenCode = userToken.getTokenCode();// 用户登录后获得的令牌
		String userCode = userToken.getUserCode();
		LinkedHashMap<String, Object> reqparams = new LinkedHashMap<String, Object>();
		reqparams.put("userCode", userCode);
		reqparams.put("shopCode", params[0]);
		reqparams.put("consumeAmount", params[1]);
		reqparams.put("batchCouponCode", params[2]);
		reqparams.put("nbrCoupon", params[3]);
		reqparams.put("platBonus", params[4]);
		reqparams.put("shopBonus", params[5]);
		reqparams.put("payType", params[6]);
		reqparams.put("noDiscountPrice", params[7]);	
		reqparams.put("tokenCode", tokenCode);
		
		try {
			// 调用API
			mResult = (JSONObject) API.reqCust("getNewPriceForAndroid", reqparams);
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
			Log.d(TAG, "BOUNS_EXIST="+code);
			if (PLAT_BOUNS_MANY == code) {
				Util.getContentValidate(R.string.huiquan_bounsp);
				mCallback.getResult(null);
			} else if (code == ErrorCode.SUCC) {
			   mCallback.getResult(mResult);
			} else if (code == SHOP_BOUNS_MANY) {
				Util.getContentValidate(R.string.shop_bounsp);
				mCallback.getResult(null);
			} else if (code == COUPON_LIMITED) { // 优惠券使用超限
				Util.getContentValidate(R.string.coupon_limit_num);
				mCallback.getResult(null);
			} else if (code == PAY_MONEY_MALL) { // 实际支付金额小于最小支付金额
				Util.getContentValidate(R.string.pay_money_limit);
				mCallback.getResult(null);
			} else if (code == COUPON_SUM_LESS) {
				Util.getContentValidate(R.string.coupon_sum_less);
				mCallback.getResult(null);
			}else if(code == COUPON_CAN_NOT_USE){
				Util.getContentValidate(R.string.no_use_coupon);
				mCallback.getResult(null);
			}
		} else {
			mCallback.getResult(null);
		}
	}
}
