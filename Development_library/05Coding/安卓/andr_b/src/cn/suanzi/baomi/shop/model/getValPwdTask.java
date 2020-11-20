package cn.suanzi.baomi.shop.model;

import java.util.LinkedHashMap;

import net.minidev.json.JSONObject;
import android.app.Activity;
import cn.suanzi.baomi.base.ErrorCode;
import cn.suanzi.baomi.base.SzException;
import cn.suanzi.baomi.base.api.API;
import cn.suanzi.baomi.base.data.DB;
import cn.suanzi.baomi.base.model.SzAsyncTask;
import cn.suanzi.baomi.base.pojo.UserToken;

/**
 * 获得原密码是否正确
 * @author qian.zhou
 */
public class getValPwdTask extends SzAsyncTask<String , String , Integer>{
	
	private final static String TAG  = getValPwdTask.class.getSimpleName() ;
	
	/**创建一个JSONObject对象**/
	private JSONObject mResult ;
	private Callback mCallback;

	public getValPwdTask(Activity acti) {
		super(acti);
	}
	
	/**
	 * 有参构造方法
	 * @param acti 调用者的上下文
	 * @param callback 回调方法
	 */
	public getValPwdTask(Activity acti, Callback callback) {
		super(acti);
		mCallback = callback;
	}
	
	/**  
     * 回调方法的接口  
     */
	public interface Callback{
		public void getResult(int  code);
	}

	/**
	 * 调用API
	 */
	@Override
	protected Integer doInBackground(String... params) {
		UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		String staffCode = userToken.getStaffCode();
		String tokenCode = userToken.getTokenCode();
		LinkedHashMap<String, Object> reqParams = new LinkedHashMap<String , Object>() ;
		reqParams.put("staffCode" , staffCode) ;
		reqParams.put("pwd" , params[0]) ;
		reqParams.put("tokenCode" , tokenCode) ;
		
		try {
			mResult = (JSONObject) API.reqShop("valPwd" , reqParams) ;
			// 如果成功，保存到数据库
			return Integer.parseInt(String.valueOf(mResult.get("code"))) ;
		} catch (SzException e) {
			this.mErrCode = e.getErrCode() ;
			return this.mErrCode.getCode() ;
		}
	}
	
	 /**
   	 * onPostExecute()中的正常业务逻辑处理.
   	 */
	@Override
	protected void handldBuziRet(final int retCode) {
		if (retCode == ErrorCode.SUCC) {
			mCallback.getResult(retCode);
		} else {
			mCallback.getResult(retCode);
		}
	}
}
