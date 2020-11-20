package cn.suanzi.baomi.cust.model;

import java.util.LinkedHashMap;

import net.minidev.json.JSONObject;
import android.app.Activity;
import android.util.Log;
import cn.suanzi.baomi.base.SzException;
import cn.suanzi.baomi.base.api.API;
import cn.suanzi.baomi.base.model.SzAsyncTask;

/**
 * 查询金额
 * @author wensi.yu
 *
 */
public class ScanTask extends SzAsyncTask<String, String, Integer> {

	private final static String TAG = "ScanTask";
	
	/**创建一个JSONObject对象*/
	private JSONObject mResult;
	/** 定义一个正确的返回结果码 */ 
	private final static int RIGHT_RET_CODE = 1;
	/** 定义一个错误的返回结果码 */ 
	private final static int ERROR_RET_CODE = 0;
	/**回调方法*/ 	
	private Callback callback;
	
	public ScanTask(Activity acti) {
		super(acti);
	}

	/**
	 * 回调方法的构造函数
	 * @param acti
	 */
	public ScanTask(Activity acti,Callback callback) {
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
	
	/**
	 * 查询金额
	 */
	@Override
	protected Integer doInBackground(String... params) {
		//获得所用活动信息
		LinkedHashMap<String, Object> reqParams = new LinkedHashMap<String, Object>();
		reqParams.put("consumeCode", params[0]);
		reqParams.put("tokenCode", params[1]);		
		
		try {
			int retCode = ERROR_RET_CODE;
			mResult = (JSONObject)API.reqShop("getRealPay", reqParams);
			Log.i(TAG, "mResult==============="+mResult);
			//判断查询的一个对象不为空为空 就返回一个正确的编码
			/*JSONArray ArResult = (JSONArray) mResult.get("activityList");
			if (mResult == null || ArResult.size() != 0 || !"[]".equals(ArResult.toString())) {
				retCode = RIGHT_RET_CODE; //1 代表访问成功
			}*/
			
			if (null == mResult) {
				retCode = RIGHT_RET_CODE; //1 代表访问成功
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
		if (retCode == RIGHT_RET_CODE) {
			callback.getResult(mResult);
		}else{ 
			if(retCode == ERROR_RET_CODE) {
				callback.getResult(null);
			}
		}
	}
}
