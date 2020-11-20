package cn.suanzi.baomi.cust.model;

import java.util.LinkedHashMap;

import net.minidev.json.JSONObject;
import android.app.Activity;
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
 * 修改密码
 * @author qian.zhou
 */
public class UpdatePwdTask extends SzAsyncTask<String, Integer, Integer> {
	private final static String TAG = "UpdatePwdTask";
	private Callback mCallback; 
	private JSONObject mResult;
	
	
	/**
	 * 构造函数
	 */
	public UpdatePwdTask(Activity acti) {
		super(acti);
	}
	
	/**
	 * 有参构造方法
	 * @param acti 调用者的上下文
	 * @param callback 回调方法
	 */
	public UpdatePwdTask(Activity acti, Callback callback) {
		super(acti);
		mCallback = callback;
	}
	
	/**  
     * 回调方法的接口  
     *  
     */
	public interface Callback{
		public void getResult(JSONObject object);
	}

	@Override
	protected Integer doInBackground(String... params) {
		// 获得一个用户信息对象
		UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		String userCode = userToken.getUserCode();
		String tokenCode = userToken.getTokenCode();
		LinkedHashMap<String, Object> reqparams = new LinkedHashMap<String, Object>();
		reqparams.put("mobileNbr", params[0]);
		reqparams.put("originalPwd", params[1]);
		reqparams.put("newPwd", params[2]);
		reqparams.put("type", params[3]);
		reqparams.put("tokenCode", tokenCode);

		try {
			// 调用API
			mResult = (JSONObject) API.reqComm("updatePwd", reqparams);
			int retCode = Integer.parseInt(mResult.get("code").toString());
			return retCode;
		} catch (SzException e) {
			this.mErrCode = e.getErrCode();
			return this.mErrCode.getCode();//返回错误编码
		}
	}

	@Override
	protected void handldBuziRet(int retCode) {
		if( retCode == ErrorCode.SUCC ){
			mCallback.getResult(mResult);
		} else{
			mCallback.getResult(null);
			if(CustConst.updatePwd.INPUT_NUM == retCode){
				Util.getContentValidate(R.string.input_phonenum);
			} else if(CustConst.updatePwd.ERROR_NUM == retCode){
				Util.getContentValidate(R.string.error_num);
			} else if(CustConst.updatePwd.ERROR_STARTPWD == retCode){
				Util.getContentValidate(R.string.error_startpwd);
			} else if(CustConst.updatePwd.INPUT_NEWPWD == retCode){
				Util.getContentValidate(R.string.input_new_password);
			} 
		}
	}
}
