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
import cn.suanzi.baomi.cust.application.CustConst;

/**
 * 抢优惠券
 * @author yanfang.li
 */
public class GrabCouponTask extends SzAsyncTask<String, Integer, Integer> {

	private final static String TAG = GrabCouponTask.class.getSimpleName();

	/** 调用API返回对象 **/
	private JSONObject mResult;

	/** 回调方法 **/
	private Callback mCallback;

	public GrabCouponTask(Activity acti) {
		super(acti);
	}
	public GrabCouponTask(Activity acti, Callback callback) {
		super(acti);
		this.mCallback = callback;
	}

	/**
	 * 回调方法的接口
	 * 
	 */
	public interface Callback {
		public void getResult(int resultCode);
	}

	@Override
	protected void onPreExecute() {
		if (null != mActivity && mProcessDialog != null) {
			mProcessDialog.dismiss();
		}
	}

	@Override
	protected Integer doInBackground(String... params) {
		UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		String userCode = userToken.getUserCode();
		String tokeCode = userToken.getTokenCode();

		LinkedHashMap<String, Object> reqparams = new LinkedHashMap<String, Object>();
		reqparams.put("batchCouponCode", params[0]);
		reqparams.put("userCode", userCode);
		reqparams.put("sharedLvl", params[1]);
		reqparams.put("tokenCode", tokeCode);

		try {
			// 调用API
			mResult = (JSONObject) API.reqCust("grabCoupon", reqparams);
			int retCode = ErrorCode.ERROR_RET_CODE;
			if (mResult != null && !"".equals(mResult.toString())) {
				retCode = ErrorCode.RIGHT_RET_CODE;
			}
			return retCode;
		} catch (SzException e) {
			this.mErrCode = e.getErrCode();
			Log.d(TAG, "************retCode=" + e.getErrCode());
			return this.mErrCode.getCode();// 返回错误编码
		}
	}

	@Override
	protected void handldBuziRet(int retCode) {
		if (retCode == ErrorCode.RIGHT_RET_CODE) {
			int resultCode = Integer.parseInt(mResult.get("code").toString());
			mCallback.getResult(resultCode);
			if (resultCode == ErrorCode.SUCC) { // 优惠券领取成功
				Util.getContentValidate(R.string.coupon_succeed);
			} else {
				if (resultCode == CustConst.Coupon.GRAB_OVER) { // 优惠券抢完了
					Util.getContentValidate(R.string.coupon_grab);
				} else if (resultCode == CustConst.Coupon.GRAB_EXPIRED) { // 优惠券过期
					Util.getContentValidate(R.string.coupon_date_over);
				} else if (resultCode == CustConst.Coupon.GRAB_LIMIT) { // 优惠券领取数量过多
					Util.getContentValidate(R.string.coupon_grabnum_over);
				} else if (resultCode == CustConst.Coupon.GRAB_NOEXIT) { // 优惠券不存在
					Util.getContentValidate(R.string.coupon_no_exit);
				} else if (resultCode == CustConst.Coupon.GRAB_SHOP_COUPON) { // 已经抢过该家店的优惠劵了
					Util.getContentValidate(R.string.grab_shop_coupon);
				} else if (resultCode == ErrorCode.API_INTERNAL_ERR) { // 优惠券领取失败
					Util.getContentValidate(R.string.coupon_failed);
				} 
			}
		} else {
			mCallback.getResult(ErrorCode.ERROR_RET_CODE);
		}
	}
}
