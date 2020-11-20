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
/**
 * 取消在线支付
 * @author yingchen
 *
 */
public class CancelBankcardPayTask extends SzAsyncTask<String, Integer, Integer> {
	//定义返回成功的结果码
	private static final int RIGHT_RET_CODE = 50000;
	//定义返回失败的结果码
	private static final int ERROR_RET_CODE = 20000;
	//定义支付已经取消的结果码
	private static final int ERROR_PAY_CANCLE_ALREADY = 50403;
	
	private JSONObject mResult;
	
	private Callback mCallback;
	
	/** 获得一个用户信息对象 **/
	private UserToken mUserToken;
	/** 用户登录后获得的令牌 **/
	private String mTokenCode;
	
	public interface Callback{
		public void getResult(boolean result);
	}
	
	public CancelBankcardPayTask(Activity acti, Callback mCallback) {
		super(acti);
		this.mCallback = mCallback;
	}

	public CancelBankcardPayTask(Activity acti) {
		super(acti);
	}

	@Override
	protected Integer doInBackground(String... params) {
		mUserToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		mTokenCode = mUserToken.getTokenCode();// 用户登录后获得的令牌
		LinkedHashMap<String, Object> reqparams = new LinkedHashMap<String, Object>();
		reqparams.put("consumeCode", params[0]);
		reqparams.put("tokenCode", mTokenCode);
		
		//调用API
		try {
			mResult = (JSONObject) API.reqCust("cancelBankcardPay", reqparams);
			int retCode = ERROR_RET_CODE;
			if(mResult!=null){
				retCode = Integer.parseInt( mResult.get("code").toString());
			}
			return retCode;
			
		} catch (SzException e) {
			this.mErrCode = e.getErrCode();
			return this.mErrCode.getCode();
		}
		
	}
	
	@Override
	protected void handldBuziRet(int retCode) {
		switch (retCode) {
		case RIGHT_RET_CODE:
			mCallback.getResult(true);
			break;
		case ERROR_RET_CODE:
			Toast.makeText(mActivity, "失败", Toast.LENGTH_SHORT).show();
			mCallback.getResult(false);
			break;
		case ERROR_PAY_CANCLE_ALREADY:
			Toast.makeText(mActivity, "该支付已经取消", Toast.LENGTH_SHORT).show();
			mCallback.getResult(false);
			break;
		default:
			break;
		}
	}


}
