package cn.suanzi.baomi.cust.model;

import java.util.LinkedHashMap;

import net.minidev.json.JSONObject;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import cn.suanzi.baomi.base.ErrorCode;
import cn.suanzi.baomi.base.SzException;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.api.API;
import cn.suanzi.baomi.base.data.DB;
import cn.suanzi.baomi.base.model.LoginTask;
import cn.suanzi.baomi.base.model.SzAsyncTask;
import cn.suanzi.baomi.base.pojo.UserToken;
import cn.suanzi.baomi.cust.R;
import cn.suanzi.baomi.cust.activity.HomeActivity;
import cn.suanzi.baomi.cust.application.CustConst;

/**
 * 抢优惠券
 * @author yanfang.li
 */
public class GrabCouponScanTask extends SzAsyncTask<String, Integer, Integer> {
	private final static String TAG = "GrabCouponScanTask";

	/** 调用API返回对象 **/
	private JSONObject mResult;

	/** 回调方法 **/
	private Callback mCallback; 

	public GrabCouponScanTask(Activity acti, Callback callback) {
		super(acti);
		this.mCallback = callback;
	}

	/**
	 * 回调方法的接口
	 * 
	 */
	public interface Callback {
		public void getResult(JSONObject result);
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
		String tokenCode = userToken.getTokenCode();
		LinkedHashMap<String, Object> reqparams = new LinkedHashMap<String, Object>();
		reqparams.put("batchCouponCode", params[0]);
		reqparams.put("userCode", userCode);
		reqparams.put("sharedLvl", params[1]);
		reqparams.put("tokenCode", tokenCode);

		try {
			// 调用API
			mResult = (JSONObject) API.reqCust("grabCoupon", reqparams);
			int retCode = Integer.parseInt(mResult.get("code").toString());
			return retCode;
		} catch (SzException e) {
			this.mErrCode = e.getErrCode();
			Log.d(TAG, "************retCode="+e.getErrCode());
			return this.mErrCode.getCode();// 返回错误编码
		}
	}

	@Override
	protected void handldBuziRet(int retCode) {
		if (mProcessDialog != null) {
			mProcessDialog.dismiss();
		}
		// RIGHT_RET_CODE的值是1 如果retCode访问成功的发 返回的是1
		if (retCode == ErrorCode.SUCC) {
			mCallback.getResult(mResult);
		} else {
			if(CustConst.Coupon.GRAB_LIMIT == retCode){
				Util.getContentValidate(R.string.coupon_grabnum_over);
				Intent intentHome = new Intent(mActivity,HomeActivity.class);
				intentHome.putExtra(LoginTask.ALL_LOGIN, CustConst.Login.HOME_LOGIN);
			    mActivity.finish();
			} else if(CustConst.Coupon.GRAB_EXPIRED == retCode){
				Util.getContentValidate(R.string.coupon_date_over);
				Intent intentHome = new Intent(mActivity,HomeActivity.class);
				intentHome.putExtra(LoginTask.ALL_LOGIN, CustConst.Login.HOME_LOGIN);
			    mActivity.finish();
			} else if(CustConst.Coupon.GRAB_OVER == retCode){
				Util.getContentValidate(R.string.coupon_grab);
				Intent intentHome = new Intent(mActivity,HomeActivity.class);
				intentHome.putExtra(LoginTask.ALL_LOGIN, CustConst.Login.HOME_LOGIN);
			    mActivity.finish();
			} else if(CustConst.Coupon.GRAB_NOEXIT == retCode){
				Util.getContentValidate(R.string.coupon_no_exit);
				Intent intentHome = new Intent(mActivity,HomeActivity.class);
				intentHome.putExtra(LoginTask.ALL_LOGIN, CustConst.Login.HOME_LOGIN);
			    mActivity.finish();
			} 
			mCallback.getResult(null);
		}
	}
}
