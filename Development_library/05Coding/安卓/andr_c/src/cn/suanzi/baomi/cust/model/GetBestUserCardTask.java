// ---------------------------------------------------------
// @author    yanfang.li
// @version   1.0.0
// @createTime 2015.5.22 
// @copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
// ---------------------------------------------------------
package cn.suanzi.baomi.cust.model;

import java.util.LinkedHashMap;

import net.minidev.json.JSONArray;
import android.app.Activity;
import cn.suanzi.baomi.base.SzException;
import cn.suanzi.baomi.base.api.API;
import cn.suanzi.baomi.base.data.DB;
import cn.suanzi.baomi.base.model.SzAsyncTask;
import cn.suanzi.baomi.base.pojo.UserToken;

/**
 * 9.11	获得顾客在某商店拥有的最高级的会员卡
 * @author yanfang.li
 */
public class GetBestUserCardTask extends SzAsyncTask<String, Integer, Integer> {

	private final static String TAG = GetBestUserCardTask.class.getSimpleName();
	private JSONArray mResult;
	private Callback mCallback;
	/** 定义一个正确的返回结果码 **/ 
	private final static int RIGHT_RET_CODE = 1;
	/** 定义一个错误的返回结果码 **/ 
	private final static int ERROR_RET_CODE = 0;
	/**
	 * 无参构造方法 
	 * @param acti 调用者的上下文
	 */
	public GetBestUserCardTask(Activity acti) {
		super(acti);
	}
	
	/**
	 * 有参构造方法
	 * @param acti 调用者的上下文
	 * @param callback 回调方法
	 */
	public GetBestUserCardTask(Activity acti, Callback callback) {
		super(acti);
		mCallback = callback;
	}

	/**  
     * 回调方法的接口  
     *  
     */
	public interface Callback{
		public void getResult(JSONArray result);
	}

	/**
	 * 调用API查询，添加会员卡    
	 * params[0] 会员卡的名称
	 * params[1] 会员卡的等级
	 * 
	 */
	@Override
	protected Integer doInBackground(String... params) {
		UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		String userCode = userToken.getUserCode();
		String tokenCode = userToken.getTokenCode();
		LinkedHashMap<String, Object> reqparams = new LinkedHashMap<String,Object>();
		reqparams.put("userCode", userCode);
		reqparams.put("shopCode",  params[0]); 
		reqparams.put("tokenCode",  tokenCode); 
		try {
				mResult = (JSONArray) API.reqCust("getBestUserCard", reqparams);
				int retCode = ERROR_RET_CODE;
				if (mResult.size() != 0 || !"[]".equals(mResult.toString())) {
					retCode = RIGHT_RET_CODE; 
				}
			return retCode;
		} catch (SzException e) {
			
			this.mErrCode = e.getErrCode();
			return this.mErrCode.getCode();//返回错误编码
		}
		 
	}
	
	/**
	 * 处理查询结果的返回值 
	 * @param retCode执行成功返回码
	 */
	@Override
	protected void handldBuziRet(int retCode) {
		//RIGHT_RET_CODE的值是1  如果retCode访问成功的发 返回的是1  
		if( retCode == RIGHT_RET_CODE ){
			mCallback.getResult(mResult);
			
		}else{
			mCallback.getResult(null);
		}
	}

}
