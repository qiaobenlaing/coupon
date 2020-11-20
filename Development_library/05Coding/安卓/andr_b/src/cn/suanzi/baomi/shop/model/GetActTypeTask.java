package cn.suanzi.baomi.shop.model;

import java.util.LinkedHashMap;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import android.app.Activity;
import android.util.Log;
import cn.suanzi.baomi.base.SzException;
import cn.suanzi.baomi.base.api.API;
import cn.suanzi.baomi.base.model.SzAsyncTask;

/**
 * @author wensi.yu
 * 活动类型
 */
public class GetActTypeTask extends SzAsyncTask<String, String, Integer> {
	
	private final static String TAG = "GetActTypeTask";
	
	/** 创建一个JSONObject对象*/
	private JSONArray mResult;
	/** 定义一个正确的返回结果码 */ 
	private final static int RIGHT_RET_CODE = 1;
	/** 定义一个错误的返回结果码 */ 
	private final static int ERROR_RET_CODE = 0;
	/**回调方法**/ 	
	private Callback callback;
	
	/**
	 * 构造函数
	 * @param acti
	 */
	public GetActTypeTask(Activity acti) {
		super(acti);
	}
	
	/**
	 * 回调方法的构造函数
	 * @param acti
	 */
	public GetActTypeTask(Activity acti,Callback callback) {
		super(acti);
		this.callback = callback;
	}
	
	/**  
     * 回调方法的接口  
     *  
     */  
    public interface Callback{  
        public void getResult(JSONArray mResult);  
    }
    
	@Override
	protected Integer doInBackground(String... params) {
		try {
			int retCode = ERROR_RET_CODE;
			LinkedHashMap<String, Object> reqParams = new LinkedHashMap<String, Object>();
			mResult = (JSONArray)API.reqShop("getActType",reqParams);
			Log.d(TAG, "mResult type == " +mResult);
			if (null != mResult) {
				retCode = RIGHT_RET_CODE; //1 代表访问成功
			} 
			Log.d(TAG, "retCode=="+retCode);
			return retCode;
			
		} catch (SzException e) {
			this.mErrCode = e.getErrCode();
			return this.mErrCode.getCode();
		}
	}

	/**
	 * 正常业务逻辑处理
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
