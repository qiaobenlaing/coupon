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
public class MposTask extends SzAsyncTask<String, Integer, Integer> {
	private final static String TAG = MposTask.class.getSimpleName();
	/** 用户编码不存在*/
	private final static int EXTIS_USER = 50503; 
	/** 商店编码不存在*/
	private final static int EXTIS_SHOP = 50314; 
	/** 会员卡不可用*/
	private final static int CARD_NOUSE = 80400; 
	/**优惠券不可用*/
	private final static int COUPON_NOUSE = 80227; 
	/**优惠券过期*/
	private final static int COUPON_EXPIRED = 80220; 
	/**红包不可用*/
	private final static int BOUNS_NOUSE = 50720; 
	/**红包不够用*/
	private final static int BOUNS_ENOUGH = 50725; 
	
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

	public MposTask(Activity acti, Callback callback) {
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
		mUserToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		mTokenCode = mUserToken.getTokenCode();// 用户登录后获得的令牌
		String userCode = mUserToken.getUserCode();
		LinkedHashMap<String, Object> reqparams = new LinkedHashMap<String, Object>();
		reqparams.put("userCode", userCode);
		reqparams.put("shopCode", params[0]);
		reqparams.put("userCouponCode", params[1]);
		reqparams.put("platBonus", params[2]);
		reqparams.put("shopBonus", params[3]);
		reqparams.put("price", params[4]);
		reqparams.put("tokenCode", mTokenCode);
		try {
			// 调用API
			mResult = (JSONObject) API.reqCust("posPay", reqparams);
			
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
			   mCallback.getResult(mResult);
			} else if (code == EXTIS_USER){ // 用户不存在
				Util.getContentValidate(R.string.exist_user);
				mCallback.getResult(null);
			} else if (code == EXTIS_SHOP) {
				Util.getContentValidate(R.string.exist_shop);
				mCallback.getResult(null);
			} else if (code == ErrorCode.API_INTERNAL_ERR) {
				Util.getContentValidate(R.string.service_exception);
				mCallback.getResult(null);
			} else if (code == CARD_NOUSE) {
				Util.getContentValidate(R.string.card_nouse);
				mCallback.getResult(null);
			} else if (code == COUPON_NOUSE) {
				Util.getContentValidate(R.string.no_use_coupon);
				mCallback.getResult(null);
			} else if (code == COUPON_EXPIRED) {
				Util.getContentValidate(R.string.coupon_expired);
				mCallback.getResult(null);
			} else if (code == BOUNS_NOUSE) {
				Util.getContentValidate(R.string.no_use_bouns);
				mCallback.getResult(null);
			} else if (code == BOUNS_ENOUGH) {
				Util.getContentValidate(R.string.bouns_enough);
				mCallback.getResult(null);
			} 
		} else {
			mCallback.getResult(null);
		}
	}
}
