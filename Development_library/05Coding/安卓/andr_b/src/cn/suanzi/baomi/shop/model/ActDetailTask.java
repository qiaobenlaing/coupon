package cn.suanzi.baomi.shop.model;

import java.util.LinkedHashMap;

import net.minidev.json.JSONObject;
import android.app.Activity;
import android.util.Log;
import cn.suanzi.baomi.base.SzException;
import cn.suanzi.baomi.base.api.API;
import cn.suanzi.baomi.base.model.SzAsyncTask;

/**
 * @author wensi.yu
 * 营销活动中的活动详情异步
 *
 */
public class ActDetailTask extends SzAsyncTask<String, String, Integer> {
	
	private final static String TAG = "ActDetailTask";
	/**创建一个JSONObject对象**/
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
	public ActDetailTask(Activity acti) {
		super(acti);
	}
	
	/**
	 * 回调方法的构造函数
	 * @param acti
	 */
	public ActDetailTask(Activity acti,Callback callback) {
		super(acti);
		this.callback=callback;
	}
	
	/**  
     * 回调方法的接口  
     *  
     */  
    public interface Callback{  
        public void getResult(JSONObject object);  
    }

	/**
	 * 调用api 根据商家编码查询所有信息
	 * [0]类型   [1]商家编码   
	 */
	@Override
	protected Integer doInBackground(String... params) {
		//获得所用活动详细信息
		LinkedHashMap<String, Object> reqParams = new LinkedHashMap<String, Object>();		
		reqParams.put("activityCode", params[0]);
		reqParams.put("tokenCode", params[1]);	
		Log.d("**********", "活动编码："+params[0]+"  tokenCode:"+params[1]);
		try {
			int retCode = ERROR_RET_CODE;
			if (!("[]".equals(API.reqShop("sGetActivityInfo", reqParams).toString()))) {
				mResult = (JSONObject)API.reqShop("sGetActivityInfo", reqParams);
				
				//判断查询的一个对象不为空为空 就返回一个正确的编码
				if (mResult != null || !"".equals(mResult.toJSONString()) ) {
					retCode = RIGHT_RET_CODE; //1 代表访问成功
				}
			}
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
		}else {
			if(retCode == ERROR_RET_CODE){
				callback.getResult(null);
			}
		}
	}
}
