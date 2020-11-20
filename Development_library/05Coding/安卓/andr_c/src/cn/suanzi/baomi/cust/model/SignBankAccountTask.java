package cn.suanzi.baomi.cust.model;

import java.util.LinkedHashMap;

import net.minidev.json.JSONObject;
import android.app.Activity;
import android.widget.Toast;
import cn.suanzi.baomi.base.SzException;
import cn.suanzi.baomi.base.api.API;
import cn.suanzi.baomi.base.data.DB;
import cn.suanzi.baomi.base.model.SzAsyncTask;
import cn.suanzi.baomi.base.pojo.UserToken;

public class SignBankAccountTask extends SzAsyncTask<String, Integer, Integer> {
	
	//定义返回成功的结果码
	private final static int RIGHT_RET_CODE = 50000;
	
	//定义返回失败的结果码
	private final static int ERROR_RET_CODE = 20000;
	
	//定义订单号错误的结果码
	private final static int ERROR_FALSE_NUMBER_CODE = 50058;
	
	private final static int BANK_ERROR_CODE = -99999;
	
	private String errormsg = "";
	
	private JSONObject mResult;
	

	
	private Callback callback;
	
	public SignBankAccountTask(Activity acti) {
		super(acti);
	}
	
	public SignBankAccountTask(Activity acti, Callback callback) {
		super(acti);
		this.callback = callback;
	}

	public  interface Callback{
		public  void  getResult(boolean result);
	}

	@Override
	protected Integer doInBackground(String... params) {
		// 获得一个用户信息对象
		UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		String tokenCode = userToken.getTokenCode();// 需要令牌认证
		
		LinkedHashMap<String, Object> reqparams = new LinkedHashMap<String, Object>();
		reqparams.put("orderNbr", params[0]);
		reqparams.put("valCode", params[1]);
		reqparams.put("tokenCode", tokenCode);
		
		try {
			mResult = (JSONObject) API.reqCust("signBankAccount", reqparams);
			
			
			if(mResult!=null){	
				/*int retCode = Integer.parseInt(mResult.get("code").toString());	
				return retCode;*/
				if("50000".equals(mResult.get("code").toString())
						||"20000".equals(mResult.get("code").toString())
						||"50053".equals(mResult.get("code").toString())
						||"50058".equals(mResult.get("code").toString())){
						int retCode = Integer.parseInt(mResult.get("code").toString());
						return retCode;
					}else{
						errormsg = mResult.get("retmsg").toString();
						return BANK_ERROR_CODE;
					}
			}
		} catch (SzException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	@Override
	protected void handldBuziRet(int retCode) {
		if(callback==null){
			return;
		}
		
		switch (retCode) {
		case RIGHT_RET_CODE:
			callback.getResult(true);
			break;
			
		case ERROR_RET_CODE:
			callback.getResult(false);
			break;
			
		case ERROR_FALSE_NUMBER_CODE:
			callback.getResult(false);
			break;
			
		case BANK_ERROR_CODE:
			callback.getResult(false);
			Toast.makeText(mActivity,errormsg, Toast.LENGTH_SHORT).show();
			break;

		default:
			break;
		}
	}

}
