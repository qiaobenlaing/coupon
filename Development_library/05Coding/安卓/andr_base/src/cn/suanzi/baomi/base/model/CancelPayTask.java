package cn.suanzi.baomi.base.model;

import java.util.LinkedHashMap;

import net.minidev.json.JSONObject;
import android.app.Activity;
import cn.suanzi.baomi.base.ErrorCode;
import cn.suanzi.baomi.base.SzException;
import cn.suanzi.baomi.base.api.API;
import cn.suanzi.baomi.base.data.DB;
import cn.suanzi.baomi.base.pojo.UserToken;

/**
 * 取消订单
 * @author qian.zhou
 */
public class CancelPayTask extends SzAsyncTask<String, Integer, Integer> {
	/**创建一个JSONObject对象**/
	private JSONObject mResult ;

	/**
	 * 构造函数
	 * @param acti
	 */
	public CancelPayTask(Activity acti) {
		super(acti);
	}
	
	/**
	 * 回调方法 	
	 */
	private Callback callback ;
		
	/**
	 * @param acti 上下文
	 * @param callback 回调方法
	 */
	public CancelPayTask(Activity acti , Callback callback) {
		super(acti) ;
		this.callback = callback ;
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (mProcessDialog != null) {
			mProcessDialog.dismiss();
		}
	}
	
	@Override
	protected void handldBuziRet(int retCode) {
		if (retCode == ErrorCode.SUCC) {
			callback.getResult(mResult);
		}else{
			callback.getResult(null);
		}
	}
	
	/**  
     * 回调方法的接口  
     */  
    public interface Callback{  
        public void getResult(JSONObject object) ;  
    }
    
	@Override
	protected Integer doInBackground(String... params) {
		LinkedHashMap<String, Object> reqParams = new LinkedHashMap<String, Object>() ;
		UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		String tokenCode = userToken.getTokenCode();
		reqParams.put("consumeCode" , params[0]) ;
		reqParams.put("tokenCode" ,tokenCode) ;
		try {
			mResult =  (JSONObject) API.reqComm("cancelPay", reqParams);
			int retCode = Integer.parseInt(mResult.get("code").toString());
			return retCode;
		} catch (SzException e) {
			this.mErrCode = e.getErrCode() ;
			return this.mErrCode.getCode() ;
		}
	}
}
