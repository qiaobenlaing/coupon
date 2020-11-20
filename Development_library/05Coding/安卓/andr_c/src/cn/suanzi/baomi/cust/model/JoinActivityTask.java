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
import cn.suanzi.baomi.base.utils.ActivityUtils;
import cn.suanzi.baomi.cust.R;
import cn.suanzi.baomi.cust.application.CustConst;

/**
 * 报名 
 * @author wensi.yu
 */
public class JoinActivityTask extends SzAsyncTask<String, Integer, Integer> {
	
	private final static String TAG = JoinActivityTask.class.getSimpleName();
	/**创建一个JSONObject对象*/
	private JSONObject mResult;
	/**回调方法*/
	private Callback callback;

	/**
	 * 构造函数
	 */
	public JoinActivityTask(Activity acti) {
		super(acti);
	}
	
	/**
	 * 回调方法的构造函数
	 * @param acti
	 */
	public JoinActivityTask(Activity acti,Callback callback) {
		super(acti);
		this.callback = callback;
	}
	
	/**  
     * 回调方法的接口  
     *  
     */  
    public interface Callback{  
        public void getResult(JSONObject mResult);  
    }

	@Override
	protected Integer doInBackground(String... params) {
		UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		String tokenCode = userToken.getTokenCode();
		LinkedHashMap<String, Object> reqparams = new LinkedHashMap<String, Object>();
		reqparams.put("userCode", params[0]);
		reqparams.put("activityCode", params[1]);
		reqparams.put("adultM", params[2]);
		reqparams.put("adultF", params[3]);
		reqparams.put("kidM", params[4]);
		reqparams.put("kidF", params[5]);
		reqparams.put("tokenCode", tokenCode);
		
		try {
			// 调用API
			mResult = (JSONObject) API.reqCust("joinActivity", reqparams);
			Log.d(TAG, "报名==========="+mResult);
			int retCode = Integer.parseInt(mResult.get("code").toString());
			Log.d(TAG, "报名的code============"+retCode);
			return retCode;
		} catch (SzException e) {
			this.mErrCode = e.getErrCode();
			return this.mErrCode.getCode();
		}
	}

	@Override
	protected void handldBuziRet(int retCode) {
		if( retCode == ErrorCode.SUCC ){
			Util.getContentValidate(R.string.toast_enroll_succ);
			callback.getResult(mResult);
			ActivityUtils.finishAll();
		}else{
			if(CustConst.ENTROLL.BEEN_ENTRPLL == retCode){
				callback.getResult(null);
				Util.getContentValidate(R.string.been_entroll);
			} else if(retCode == ErrorCode.API_INTERNAL_ERR ){
				callback.getResult(null);
				Util.getContentValidate(R.string.toast_enroll_erroe);
			}
		}
	}
}
