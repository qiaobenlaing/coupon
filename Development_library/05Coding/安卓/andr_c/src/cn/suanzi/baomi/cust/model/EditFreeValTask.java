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
 * 设置用户免验证码支付的异步任务类
 * @author yingchen
 *
 */
public class EditFreeValTask extends SzAsyncTask<String, Integer, Integer> {
	private static final String TAG = EditFreeValTask.class.getSimpleName();
	/**定义一个成功的结果码*/
	private static final int RIGHT_RET_CODE  = 50000;
	/**定义一个失败的结果码*/
	private static final int ERR0R_RET_CODE = 20000;
	/**定义一个密码错误的结果码*/
	private static final int ERROR_PWD_CODE = 60011;
	
	/** 调用API返回对象 **/
	private JSONObject mResult;
	
	public EditFreeValTask(Activity acti) {
		super(acti);
	}
	
	public interface CallBack{
		public void getResult(boolean success);
	}
	
	private CallBack mCallBack;
	
	
	public EditFreeValTask(Activity acti, CallBack mCallBack) {
		super(acti);
		this.mCallBack = mCallBack;
	}


	@Override
	protected Integer doInBackground(String... params) {
		UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		String userCode = userToken.getUserCode();
		String tokenCode = userToken.getTokenCode();
		
		LinkedHashMap<String, Object> reqparams = new LinkedHashMap<String, Object>();
		reqparams.put("userCode", userCode);
		reqparams.put("freeValCodePay",Integer.parseInt(params[0]));
		reqparams.put("pwd",params[1]);
		reqparams.put("tokenCode", tokenCode);
	
		try {
			mResult = (JSONObject) API.reqCust("editFreeVal", reqparams);
			int retCode = ErrorCode.ERROR_RET_CODE;
			if(null != mResult){
				Log.d(TAG, "EditFreeValTask==="+mResult.toString());
				retCode = Integer.parseInt(mResult.get("code").toString());
			}else{
			}
			return retCode;
		} catch (SzException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	@Override
	protected void handldBuziRet(int retCode) {
		switch (retCode) {
		case RIGHT_RET_CODE:
			mCallBack.getResult(true);
			break;
		case ERR0R_RET_CODE:
			mCallBack.getResult(false);
			break;
		case ERROR_PWD_CODE:
			Util.getContentValidate(R.string.setting_no_iden_error_code);
			mCallBack.getResult(false);
			break;

		default:
			Util.getContentValidate(R.string.setting_no_iden_error_net);
			break;
		}
	}

}
