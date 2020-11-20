package cn.suanzi.baomi.cust.model;

import java.util.LinkedHashMap;

import net.minidev.json.JSONObject;
import android.app.Activity;
import android.util.Log;
import android.widget.Toast;
import cn.suanzi.baomi.base.SzException;
import cn.suanzi.baomi.base.api.API;
import cn.suanzi.baomi.base.data.DB;
import cn.suanzi.baomi.base.model.SzAsyncTask;
import cn.suanzi.baomi.base.pojo.UserToken;
/**
 * 绑定银行卡并且支付
 * @author yingchen
 */
public class SignAndPay extends SzAsyncTask<String, Integer, Integer> {
	private static final String TAG = SignAndPay.class.getSimpleName();
	/**定义成功的结果码*/
	private final static int RIGHT_RET_CODE = 50000;
	/**定义错误的结果码*/
	private final static int ERROR_RET_CODE = 20000;
	/**定义银行的错误结果码*/
	private final static int ERROR_BANK_CODE = 0;
	
	/**定义错误的结果码*/
	private final static int ERROR_CODE = -1;
	
	private Callback mCallback;
	
	private JSONObject mResult;
	
	private String errormsg = "";
	
	private String orderCode = "";
	public SignAndPay(Activity acti) {
		super(acti);
	}
	
	
	
	public SignAndPay(Activity acti, Callback mCallback) {
		super(acti);
		this.mCallback = mCallback;
	}


	public interface Callback{
		public void getResult(boolean result,String orderCode);
	}

	@Override
	protected Integer doInBackground(String... params) {
		// 获得一个用户信息对象
		UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		String tokenCode = userToken.getTokenCode();// 需要令牌认证
				
		
		LinkedHashMap<String, Object> reqparams = new LinkedHashMap<String, Object>();
		reqparams.put("consumeCode", params[0]);
		reqparams.put("bankAccountCode", params[1]);
		reqparams.put("valCode", params[2]);
		reqparams.put("tokenCode", tokenCode);
		
		
		//请求API
		try {
			mResult = (JSONObject) API.reqCust("signAndPay", reqparams);
			if(mResult!=null){
				Log.d(TAG, "SignAndPay=="+mResult.toString());
				if(mResult.get("code")==null){ 
					return ERROR_CODE;
				}
				String code = mResult.get("code").toString();
				if("50000".equals(code)||
						"20000".equals(code)){
					if("50000".equals(code)){
						orderCode = mResult.get("orderCode").toString();
					}
					return Integer.parseInt(code);
				}else{ //B*** 银行错误
					if(mResult.get("retmsg")!=null){
						errormsg = mResult.get("retmsg").toString();
						return ERROR_BANK_CODE;
					}
				}
			}
			
		} catch (SzException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	@Override
	protected void handldBuziRet(int retCode) {
		switch (retCode) {
		case RIGHT_RET_CODE:   //成功
			mCallback.getResult(true,orderCode);
			break;
		case ERROR_RET_CODE://2000  程序内部错误
			mCallback.getResult(false,"");
			break;
			
		case ERROR_BANK_CODE: //银行错误
			mCallback.getResult(false,"");
			if("".equals(errormsg)){
				Toast.makeText(mActivity, "请联系客服。。。", Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(mActivity, errormsg, Toast.LENGTH_SHORT).show();
			}
			break;
		case ERROR_CODE: //code 为null
			mCallback.getResult(false,"");
			Toast.makeText(mActivity, "请联系客服。。。", Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}
	}

}
