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

public class SubmitActOrderTask extends SzAsyncTask<String, Integer, Integer> {
	private static final String TAG = SubmitActOrderTask.class.getSimpleName();
	/**定义一个成功的结果码*/
	private static final int RIGHT_CODE = 50000;
	/**定义一个失败的结果码*/
	private static final int ERROR_CODE = 20000;
	/**定义活动过期的结果码*/
	private static final int OUT_TIME_CODE = 50222;
	/**定义活动单人报名人数超限的结果码*/
	private static final int OUT_SINGLE_BUY = 50223;
	/**定义活动报名人数超限的结果码*/
	private static final int OUT_TOTAL_BUY = 50224;
	/**定义平台红包不够用的结果码*/
	private static final int OVER_PLAT_BONUDS = 50725;
	/**定义商家红包不够用的结果码*/
	private static final int OVER_SHOP_BONUDS = 50726;
	
	private CallBack mCallBack;
	
	private JSONObject mResult;
	
	private String mOrderNbr;
	
	private String mConsumeCode;
	
	private String mRealPay;
	
	/**商家剩余活动的数量*/
	private String mRemaining = "";
	
	public SubmitActOrderTask(Activity acti) {
		super(acti);
	}

	public SubmitActOrderTask(Activity acti, CallBack mCallBack) {
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
		reqparams.put("actCode", params[1]);
		reqparams.put("orderInfo", params[2]);
		/*reqparams.put("nbr", params[3]);
		reqparams.put("price", params[4]);*/
		reqparams.put("bookingName", params[3]);
		reqparams.put("mobileNbr", params[4]);
		reqparams.put("orderAmount",  Double.parseDouble(params[5]));
		reqparams.put("platBonus", Double.parseDouble(params[6]));
		reqparams.put("shopBonus", Double.parseDouble(params[7]));
		reqparams.put("tokenCode", tokenCode);
		
		int retCode = ERROR_CODE;
		try {
			mResult = (JSONObject) API.reqCust("submitActOrder", reqparams);
			if(null!=mResult){
				Log.d(TAG, "SubmitActOrderTask==="+mResult.toString());
				retCode = Integer.parseInt(mResult.get("code").toString());
				if(retCode == RIGHT_CODE){
					mOrderNbr = mResult.get("orderNbr").toString();
					mConsumeCode = mResult.get("consumeCode").toString();
					mRealPay = mResult.get("realPay").toString();
				}else if(retCode ==OUT_TOTAL_BUY ){ //超过商家剩余的优惠券数量
					mRemaining = (String) mResult.get("remaining");
				}
				
			}
		} catch (SzException e) {
			this.mErrCode = e.getErrCode() ;
			return this.mErrCode.getCode() ;
		}
		return retCode;
	}
	
	@Override
	protected void handldBuziRet(int retCode) {
		Log.d(TAG, "retCode=="+retCode);
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
			Util.getContentValidate(R.string.out_time_activity);
			break;
		case OUT_SINGLE_BUY:
			mCallBack.getResult(null);
			Util.getContentValidate(R.string.out_single_buy);
			break;
		case OUT_TOTAL_BUY:
			mCallBack.getResult(null);
			String message = mActivity.getResources().getString(R.string.actvity_shop_left)+mRemaining+"张";
			Toast.makeText(mActivity,message , Toast.LENGTH_SHORT).show();
			break;
		case OVER_PLAT_BONUDS:
			mCallBack.getResult(null);
			Util.getContentValidate(R.string.huiquan_insufficient_amount);
			break;
		case OVER_SHOP_BONUDS:
			mCallBack.getResult(null);
			Util.getContentValidate(R.string.insufficient_amount);
			break;
		default:
			break;
		}
	}

}
