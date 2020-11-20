package com.huift.hfq.cust.model;

import java.util.LinkedHashMap;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import android.app.Activity;
import com.huift.hfq.base.SzException;
import com.huift.hfq.base.api.API;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.model.SzAsyncTask;
import com.huift.hfq.base.pojo.UserToken;

/**
 * 限时购
 * @author wensi.yu
 *
 */
public class TimeLimitTask extends SzAsyncTask<String, String, Integer> {

	private final static String TAG = TimeLimitTask.class.getSimpleName();
	
	/**创建一个JSONObject对象*/
	private JSONObject mResult;
	/**定义一个正确的返回结果码 */ 
	private final static int RIGHT_RET_CODE = 1;
	/**定义一个错误的返回结果码 */ 
	private final static int ERROR_RET_CODE = 0;
	/**回调方法**/ 	
	private Callback callback;
	
	/**
	 * 构造方法
	 * @param acti
	 */
	public TimeLimitTask(Activity acti) {
		super(acti);
	}
	
	/**
	 * 回调方法的构造函数
	 * @param acti
	 */
	public TimeLimitTask(Activity acti,Callback callback) {
		super(acti);
		this.callback=callback;
	}

	/**  
     * 回调方法的接口  
     *  
     */  
    public interface Callback{  
        public void getResult(JSONObject mResult);  
    }
    
    @Override
	protected void onPreExecute() {
		if (null != mActivity) {
			if (null != mProcessDialog) {
				mProcessDialog.dismiss();
			}
		}
	}
    
	/**
	 * 限时购的异步任务
	 */
	@Override
	protected Integer doInBackground(String... params) {
		UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		String userCode = "";
		if(DB.getBoolean(DB.Key.CUST_LOGIN)){
			userCode = userToken.getUserCode();
		}else{
			userCode = "";
		}
		LinkedHashMap<String, Object> reqParams = new LinkedHashMap<String, Object>();
		reqParams.put("couponType", Integer.parseInt("4")); //优惠券类型   限时购
		reqParams.put("searchWord",params[0]); //检索关键字
		reqParams.put("longitude",params[1]); //用户所在经度
		reqParams.put("latitude",params[2]); //用户所在纬度
		reqParams.put("page",Integer.parseInt(params[3])); //页码
		reqParams.put("city",params[4]); //城市
		reqParams.put("userCode",userCode); //用户
		
		try {
			mResult = (JSONObject) API.reqCust("listCoupon", reqParams);
			int retCode = ERROR_RET_CODE;
			JSONArray ArResult = (JSONArray) mResult.get("couponList");
			//判断返回的对象是否为空
			if (mResult == null || ArResult.size() != 0 || !"[]".equals(ArResult.toString())) {
				//返回正确结果码
				retCode = RIGHT_RET_CODE;
			}
			return retCode;
			
		} catch (SzException e) {
			this.mErrCode = e.getErrCode();
			return this.mErrCode.getCode();
		}
	}
	
	/**
	 * 业务逻辑处理
	 */
	@Override
	protected void handldBuziRet(int retCode) {
		if(retCode == RIGHT_RET_CODE){
			callback.getResult(mResult);
		}
		else if(retCode == ERROR_RET_CODE){
			callback.getResult(null);
		}
	}
}
