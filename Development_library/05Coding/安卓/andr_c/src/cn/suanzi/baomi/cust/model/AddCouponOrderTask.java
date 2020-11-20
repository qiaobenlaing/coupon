package cn.suanzi.baomi.cust.model;

import java.util.LinkedHashMap;

import net.minidev.json.JSONObject;
import android.app.Activity;
import android.util.Log;
import android.widget.Toast;
import cn.suanzi.baomi.base.SzException;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.api.API;
import cn.suanzi.baomi.base.data.DB;
import cn.suanzi.baomi.base.model.SzAsyncTask;
import cn.suanzi.baomi.base.pojo.UserToken;
import cn.suanzi.baomi.cust.R;

public class AddCouponOrderTask extends SzAsyncTask<String, Integer, Integer> {
	private static final String TAG = AddCouponOrderTask.class.getSimpleName();
	/**定义一个成功的结果码*/
	private static final int RIGHT_CODE = 50000;
	/**定义一个失败的结果码*/
	private static final int ERROR_CODE = 20000;
	/**定义一个优惠券过期的结果吗*/
	private static final int OUT_TIME_CODE = 80220;
	/**定义一个优惠券已经没有的结果吗*/
	private static final int NO_LEFT_CODE = 802211;
	/**定义一个购买数量超过限制的结果码*/
	private static final int SUPER_LIMIT_CODE = 80238;
	/**定义一个购买数量超过商户剩余数量的限制的结果码*/
	private static final int SUPER_LIMIT_LEFT = 80240;
	
	private JSONObject mResult;
	
	/**商家剩余优惠券的数量*/
	private String mRemaining = "";
	
	public AddCouponOrderTask(Activity acti) {
		super(acti);
	}

	private CallBack mCallBack;
	
	private String mOrderNbr;
	
	private String mConsumeCode;
	
	private String mRealPay;
	
	public AddCouponOrderTask(Activity acti, CallBack mCallBack) {
		super(acti);
		this.mCallBack = mCallBack;
	}

	public interface CallBack{
		public void getResult(String result);
	}
	
	@Override
	protected Integer doInBackground(String... params) {
		// 获得一个用户信息对象
		UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		String tokenCode = userToken.getTokenCode();
		String userCode = userToken.getUserCode();
		
		LinkedHashMap<String, Object> reqparams = new LinkedHashMap<String, Object>();
		reqparams.put("userCode", userCode);
		reqparams.put("shopCode", params[0]);
		reqparams.put("batchCouponCode", params[1]);
		reqparams.put("couponNbr", Integer.parseInt(params[2]));
		reqparams.put("platBonus", Double.parseDouble(params[3]));
		reqparams.put("shopBonus",  Double.parseDouble(params[4]));
		reqparams.put("tokenCode", tokenCode);
		int retCode = ERROR_CODE;
		try {
			mResult =(JSONObject) API.reqCust("addCouponOrder", reqparams);
			if(null != mResult){
				Log.d(TAG, "AddCouponOrderTask==="+mResult.toString());
				retCode = Integer.parseInt(mResult.get("code").toString());
				if(retCode == RIGHT_CODE){
					mOrderNbr = mResult.get("orderNbr").toString();
					mConsumeCode = mResult.get("consumeCode").toString();
					mRealPay = mResult.get("realPay").toString();
				}else if(retCode ==SUPER_LIMIT_LEFT ){ //超过商家剩余的优惠券数量
					mRemaining = (String) mResult.get("remaining");
				}
			}
		} catch (SzException e) {
			e.printStackTrace();
			retCode = ERROR_CODE;
		}
		return retCode;
	}
	
	@Override
	protected void handldBuziRet(int retCode) {
		switch (retCode) {
		case RIGHT_CODE:
			mCallBack.getResult(mOrderNbr+"||"+mConsumeCode+"||"+mRealPay);
			break;
		case ERROR_CODE:
			mCallBack.getResult(null);
			Util.getContentValidate(R.string.fail_to_requst);
			break;
		case OUT_TIME_CODE:
			mCallBack.getResult(null);
			Util.getContentValidate(R.string.use_time_fail);
			break;
		case NO_LEFT_CODE:
			mCallBack.getResult(null);
			Util.getContentValidate(R.string.no_coupon_leave);
			break;
		case SUPER_LIMIT_CODE:
			mCallBack.getResult(null);
			Util.getContentValidate(R.string.uper_coupon_limit);
			break;
		case SUPER_LIMIT_LEFT:	
			mCallBack.getResult(null);
			String message = mActivity.getResources().getString(R.string.uper_coupon_shop_left)+mRemaining+"张";
			Toast.makeText(mActivity,message , Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}
	}

}
