package cn.suanzi.baomi.cust.model;

import java.util.LinkedHashMap;

import net.minidev.json.JSONObject;
import android.app.Activity;
import android.content.Intent;
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
import cn.suanzi.baomi.cust.activity.ActMyContentActivity;

/**
 * 编辑活动
 * @author wensi.yu     
 *
 */
public class EditUserActInfoTask extends SzAsyncTask<String, String, Integer>{

	private final static String TAG = "ExitActTask";
	
	/**创建一个JSONObject对象*/
	private JSONObject mResult;
	/**回调方法*/ 	
	private Callback callback;
	
	public EditUserActInfoTask(Activity acti) {
		super(acti);
	}
	
	/**
	 * 回调方法的构造函数
	 * @param acti
	 */
	public EditUserActInfoTask(Activity acti,Callback callback) {
		super(acti);
		this.callback = callback;
	}
	
	/**  
     * 回调方法的接口  
     *  
     */  
    public interface Callback{  
        public void getResult(int mResult);  
    }
	
    /**
     * 编辑活动人数
     */
	@Override
	protected Integer doInBackground(String... params) {
		UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		String tokenCode = userToken.getTokenCode();
		LinkedHashMap<String, Object> reqParams = new LinkedHashMap<String, Object>();
		reqParams.put("userActCode", params[0]);
		reqParams.put("adultM", params[1]);	
		reqParams.put("adultF", params[2]);	
		reqParams.put("kidM", params[3]);	
		reqParams.put("kidF", params[4]);	
		reqParams.put("tokenCode", tokenCode);	
		Log.d(TAG, "userActCode"+params[0]);
		
		try {
			JSONObject result = (JSONObject)API.reqCust("editUserActInfo", reqParams);
			Log.i(TAG, "编辑活动人数==============="+result);
			int retCode = Integer.parseInt(result.get("code").toString());
			
			return retCode;
			
		} catch (SzException e) {
			this.mErrCode = e.getErrCode();
			return this.mErrCode.getCode();
		}
	}

	@Override
	protected void handldBuziRet(int retCode) {
		if (retCode == ErrorCode.SUCC) {
			Util.getContentValidate(R.string.actmycontent_edit);
			Intent intent = new Intent(mActivity, ActMyContentActivity.class);
			mActivity.startActivity(intent);
			ActivityUtils.finishAll();
			callback.getResult(ErrorCode.SUCC);
		}else{ 
			if(retCode == ErrorCode.FAIL) {
				callback.getResult(ErrorCode.FAIL);
			}
		}
	}
}
