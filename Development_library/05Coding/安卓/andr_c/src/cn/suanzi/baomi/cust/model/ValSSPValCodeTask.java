package cn.suanzi.baomi.cust.model;

import java.util.LinkedHashMap;

import net.minidev.json.JSONObject;
import android.app.Activity;
import android.util.Log;
import cn.suanzi.baomi.base.SzException;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.api.API;
import cn.suanzi.baomi.base.data.DB;
import cn.suanzi.baomi.base.model.SzAsyncTask;
import cn.suanzi.baomi.base.pojo.UserToken;
import cn.suanzi.baomi.cust.R;

/**
 * 验证获取的设置支付密码的短信验证码
 * @author yingchen
 *
 */
public class ValSSPValCodeTask extends SzAsyncTask<String, Integer, Integer> {
	private static final String TAG = ValSSPValCodeTask.class.getSimpleName();
	/**定义一个返回成功的结果码*/
	private static final int RIGHT_CODE = 1;
	/**定义一个请求失败的结果码*/
	private static final int ERROR_CODE = 0;
	private JSONObject mResult;
	
	private CallBack mCallBack;
	
	public ValSSPValCodeTask(Activity acti) {
		super(acti);
	}

	public ValSSPValCodeTask(Activity acti, CallBack mCallBack) {
		super(acti);
		this.mCallBack = mCallBack;
	}


	public interface CallBack{
		public void getResult(JSONObject result);
	}
	@Override
	protected Integer doInBackground(String... params) {
		UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		
		LinkedHashMap<String, Object> reqparams = new LinkedHashMap<String, Object>();
		reqparams.put("userCode", userToken.getUserCode());
		reqparams.put("mobileNbr", params[0]);
		reqparams.put("valCode", params[1]);
		reqparams.put("tokenCode", userToken.getTokenCode());
		
		try {
			mResult = (JSONObject) API.reqCust("valSSPValCode", reqparams);
			if(null!=mResult){
				Log.d(TAG, "ValSSPValCodeTask == "+mResult.toString());
				return RIGHT_CODE;
			}else{
				return ERROR_CODE;
			}
		} catch (SzException e) {
			this.mErrCode=e.getErrCode();
			return this.mErrCode.getCode();
		}
	}
	@Override
	protected void handldBuziRet(int retCode) {
		switch (retCode) {
		case RIGHT_CODE:
			mCallBack.getResult(mResult);
			break;

		case ERROR_CODE:
			mCallBack.getResult(null);
			Util.getContentValidate(R.string.check_iden_code_error_return);
			break;
		default:
			mCallBack.getResult(null);
			Util.getContentValidate(R.string.check_iden_code_error_return);
			break;
		}
	}

}
