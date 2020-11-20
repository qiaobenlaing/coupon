package cn.suanzi.baomi.shop.model;

import java.util.LinkedHashMap;

import com.google.gson.JsonArray;

import net.minidev.json.JSONObject;
import android.app.Activity;
import cn.suanzi.baomi.base.SzException;
import cn.suanzi.baomi.base.api.API;
import cn.suanzi.baomi.base.data.DB;
import cn.suanzi.baomi.base.model.SzAsyncTask;
import cn.suanzi.baomi.base.pojo.UserToken;

/**
 * 获得活动详情
 * @author qian.zhou
 */
public class GetActivityInfoTask extends SzAsyncTask<String, String, Integer>{

	private final static String TAG = GetActivityInfoTask.class.getSimpleName();
	/***创建一个JSONObject对象**/
	private JSONObject mResult;
	/** 定义一个正确的返回结果码 **/ 
	private final static int RIGHT_RET_CODE = 1;
	/** 定义一个错误的返回结果码 **/ 
	private final static int ERROR_RET_CODE = 0;
	/**回调方法**/ 	
	private Callback callback;
	
	/**
	 * 构造函数
	 * @param acti
	 */
	public GetActivityInfoTask(Activity acti) {
		super(acti);
	}
	
	/**
	 * 回调方法的构造函数
	 * @param acti
	 */
	public GetActivityInfoTask(Activity acti,Callback callback) {
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

    /**
	 * 红包详情
	 */
	@Override
	protected Integer doInBackground(String... params) {
		LinkedHashMap<String, Object> reqParams = new LinkedHashMap<String, Object>();
		UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		String tokenCode = userToken.getTokenCode();
		reqParams.put("activityCode", params[0]);
		reqParams.put("tokenCode", tokenCode);	
		
		try {
			int retCode = ERROR_RET_CODE;
			Object result = (Object)API.reqShop("getActivityInfo", reqParams);
			if (null != result.toString() && !"[]".equals(result.toString())) {
				mResult = (JSONObject) result;
				//判断查询的一个对象不为空为空 就返回一个正确的编码
				if (mResult != null || !"".equals(mResult.toString())) {
					retCode = RIGHT_RET_CODE; //1 代表访问成功
				}
			}
			return retCode;
			
		} catch (SzException e) {
			this.mErrCode = e.getErrCode();
			return this.mErrCode.getCode();
		}
	}

	@Override
	protected void handldBuziRet(int retCode) {
		if (retCode == RIGHT_RET_CODE) {
			callback.getResult(mResult);
		}else{ 
			callback.getResult(null);
		}
	}
}
