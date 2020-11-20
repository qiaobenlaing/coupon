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


public class AddBankAccountTask extends SzAsyncTask<String, Integer, Integer> {
	
	private JSONObject mResult;
	
	//定义返回成功的结果码
	private static final int RIGHT_RET_CODE = 50000;
	
	
	//定义账号姓名不正确的结果码
	private static final int ERROR_FALSE_NAME_CODE = 50050;
	
	//定义证件号码不正确的结果码
	private static final int ERROR_FALSE_NUMBER_CODE = 50051;
	
	//定义银行卡号不正确的结果码
	private static final int ERROR_FALSE_CARD_CODE = 50052;
	
	//定义证件类型不正确的结果码
	private static final int ERROE_FALSE_TYPE_CODE = 50054;
	
	//定义该卡已经签订支付协议的结果码
	private static final int ERROR_FALSE_REPEAT_CODE = 50055;
	
	//定义银行预留手机号码不正确的结果码
	private static final int ERROR_FALSE_PHONE_CODE = 60002;
	
	//银行卡绑定的账号太多
	private static final int ERROR_ADD_CARD_NUM = 50060 ;

	//定义失败的结果码
	private static final int ERROR_RET_CODE = 20000;
	
	private Callback callback;
	
	//添加银行卡产生的订单号
	private String orderNbr;
	
	public AddBankAccountTask(Activity acti) {
		super(acti);
	}
	
	public AddBankAccountTask(Activity acti, Callback callback) {
		super(acti);
		this.callback = callback;
	}



	public  interface Callback{
		public void getResult(String result);
	}
	
	/**
	 * 添加银行卡的异步任务
	 */
	@Override
	protected Integer doInBackground(String... params) {
		// 获得一个用户信息对象
		UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		String userCode = userToken.getUserCode();// 用户编码
		String tokenCode = userToken.getTokenCode();// 需要令牌认证
				
		LinkedHashMap<String, Object> reqparams = new LinkedHashMap<String, Object>();
		reqparams.put("userCode", userCode);
		reqparams.put("accountName", params[0]);
		reqparams.put("idType", Integer.parseInt(params[1]));
		reqparams.put("idNbr", params[2]);
		reqparams.put("bankCard", params[3]);
		reqparams.put("mobileNbr", params[4]);
		reqparams.put("tokenCode", tokenCode);
		
		try {
			mResult = (JSONObject) API.reqCust("addBankAccountModify", reqparams);
			if(mResult!=null){
				int retCode = Integer.parseInt(mResult.get("code").toString());
				if(retCode == RIGHT_RET_CODE){
					orderNbr = (String)mResult.get("orderNbr");
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
		if(callback==null){
			return;
		}
		switch (retCode) {
		case RIGHT_RET_CODE:
			callback.getResult(orderNbr);
			break;

		case ERROR_RET_CODE:
			Toast.makeText(mActivity, "失败，请重试", Toast.LENGTH_SHORT).show();
			callback.getResult(null);
			break;
			
		case ERROR_FALSE_NAME_CODE:
			Toast.makeText(mActivity, "账号姓名不正确", Toast.LENGTH_SHORT).show();
			callback.getResult(null);
			break;
			
		case ERROR_FALSE_NUMBER_CODE:
			Toast.makeText(mActivity, "证件号码不正确", Toast.LENGTH_SHORT).show();
			callback.getResult(null);
			break;
			
		case ERROR_FALSE_CARD_CODE:
			Toast.makeText(mActivity, "银行卡号不正确", Toast.LENGTH_SHORT).show();
			callback.getResult(null);
			break;
			
		case ERROE_FALSE_TYPE_CODE:
			Toast.makeText(mActivity, "证件类型不正确", Toast.LENGTH_SHORT).show();
			callback.getResult(null);
			break;
			
		case  ERROR_FALSE_REPEAT_CODE:
			Toast.makeText(mActivity, "该卡已经签订支付协议", Toast.LENGTH_SHORT).show();
			callback.getResult(null);
			break;	
			
		case  ERROR_FALSE_PHONE_CODE:
			Toast.makeText(mActivity, "银行预留手机号码不正确", Toast.LENGTH_SHORT).show();
			callback.getResult(null);
			break;	
			
		case  ERROR_ADD_CARD_NUM:
			Toast.makeText(mActivity, "该卡已经绑定了3个账号，不能再绑定了", Toast.LENGTH_SHORT).show();
			callback.getResult(null);
			break;
			
		default:
			break;
		}
	}

}
