package com.huift.hfq.cust.model;

import java.util.LinkedHashMap;

import net.minidev.json.JSONObject;
import android.R.integer;
import android.app.Activity;
import com.huift.hfq.base.SzException;
import com.huift.hfq.base.api.API;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.model.SzAsyncTask;
import com.huift.hfq.base.pojo.UserToken;

/**
 * 获取个人信息
 * @author qian.zhou
 */
public class GetUserInfoTask extends SzAsyncTask<String, integer, Integer> {
	/**创建一个JSONObject对象**/
	private JSONObject mResult ;
	/**定义一个正确的返回结果码**/
	private final static int RIGHT_RET_CODE = 1 ;
	/**定义一个错误的返回结果码**/ 
	private final static int ERROR_RET_CODE = 0;
	/**回调方法**/
	private Callback callback ;

	/**
	 * 构造函数
	 * @param acti
	 */
	public GetUserInfoTask(Activity acti) {
		super(acti);
	}
	
	/**
	 * @param acti 上下文
	 * @param callback 回调方法
	 */
	public GetUserInfoTask(Activity acti , Callback callback) {
		super(acti) ;
		this.callback = callback ;
	}
	
	@Override
	protected void handldBuziRet(int retCode) {
		if (retCode == RIGHT_RET_CODE) {
			callback.getResult(mResult) ;
		}else if(retCode == ERROR_RET_CODE){
			callback.getResult(null);
		}
	}
	
	@Override
	protected void onPreExecute() {
		if(null != mActivity && mProcessDialog != null){
			mProcessDialog.dismiss();
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
		UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		LinkedHashMap<String, Object> reqParams = new LinkedHashMap<String, Object>() ;
		reqParams.put("userCode" , userToken.getUserCode()) ;
		reqParams.put("tokenCode" , userToken.getTokenCode()) ;
		
		try {
			int retCode = ERROR_RET_CODE;
			if (!("[]".equals(API.reqCust("getUserInfo", reqParams).toString()))) {
				mResult = (JSONObject)API.reqCust("getUserInfo", reqParams);
				//判断查询的一个对象不为空为空 就返回一个正确的编码
				if ( mResult != null || !"".equals(mResult.toJSONString()) ) {
					retCode = RIGHT_RET_CODE; //1 代表访问成功
				}
			}
			return retCode;
		} catch (SzException e) {
			e.printStackTrace();
			this.mErrCode = e.getErrCode() ;
			return this.mErrCode.getCode() ;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
}
