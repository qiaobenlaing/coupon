// ---------------------------------------------------------
// @author    yanfang.li
// @version   1.0.0
// @createTime 2015.5.22 
// @copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
// ---------------------------------------------------------
package com.huift.hfq.shop.model;

import java.util.LinkedHashMap;

import com.huift.hfq.base.SzException;
import com.huift.hfq.base.api.API;

import net.minidev.json.JSONArray;
import android.content.Context;
import android.os.AsyncTask;

/**
 * 获得会员列表,调用API
 * @author yanfang.li
 */
public class QueryCardVipTask extends AsyncTask<String, Integer, Integer> {

	private final static String TAG = "QueryCardVipTask";
	/** 定义一个正确的返回结果码 **/ 
	private final static int RIGHT_RET_CODE = 1;
	/** 定义一个错误的返回结果码 **/ 
	private final static int ERROR_RET_CODE = 0;
	/** 回调方法 **/
	private Callback mCallback;
	/** 调用API返回对象 **/
	private JSONArray mResult;
	/** 上下文 **/
	private Context mContext;
	
	/**
	 * 无参构造
	 */
	public QueryCardVipTask() {
		super();
	}
	
	/**
	 *  带参构造方法  
	 * @param result  查询返回的结果
	 * @param context 上下文
	 */
	public QueryCardVipTask(Context context,Callback Callback) {
		mContext = context;
		mCallback = Callback;
	}


	/**  
     * 回调方法的接口  
     *  
     */
	public interface Callback{
		/**
		 * 传递参数
		 * @param result 是异步请求的结果
		 */
		public void getResult(JSONArray rsult);
	}
	@Override
	protected Integer doInBackground(String... params) {
		
		LinkedHashMap<String, Object> reqparams=new LinkedHashMap<String,Object>();
		reqparams.put("shopCode", params[0]);
		reqparams.put("userName", params[1]);
		reqparams.put("orderType", params[2]);
		reqparams.put("page", Integer.parseInt(params[3]));
		reqparams.put("tokenCode", params[4]);
		int retCode = ERROR_RET_CODE;//返回编码
		try {
			//调用API
			mResult = (JSONArray) API.reqShop("listCardVip", reqparams);
			
			//判断查询的一个对象不为空为空 就返回一个正确的编码
			if( !(mResult == null || mResult.size() == 0 || "".equals(mResult.toJSONString())) ){
				retCode = RIGHT_RET_CODE; //1 代表访问成功
			}
			return retCode;
		} catch (SzException e) {
			
			return  retCode=ERROR_RET_CODE;//返回错误编码
		}
		 
	}
	
	@Override
	protected void onPostExecute(Integer retCode) {
		if(retCode == RIGHT_RET_CODE){
			
			mCallback.getResult(mResult);
			
		}else{
			mCallback.getResult(null);
		}
	}
	
}
