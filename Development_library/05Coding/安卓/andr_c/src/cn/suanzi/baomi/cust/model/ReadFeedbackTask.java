// ---------------------------------------------------------
// @author    yanfang.li
// @version   1.0.0
// @createTime 2015.5.22 
// @copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
// ---------------------------------------------------------
package cn.suanzi.baomi.cust.model;

import java.util.LinkedHashMap;

import net.minidev.json.JSONObject;
import android.app.Activity;
import cn.suanzi.baomi.base.ErrorCode;
import cn.suanzi.baomi.base.SzException;
import cn.suanzi.baomi.base.api.API;
import cn.suanzi.baomi.base.data.DB;
import cn.suanzi.baomi.base.model.SzAsyncTask;
import cn.suanzi.baomi.base.pojo.UserToken;

/**
 * 设置反馈为已读状态
 * @author qian.zhou
 */
public class ReadFeedbackTask extends SzAsyncTask<String, Integer, Integer> {

	private final static String TAG = "ReadFeedbackTask";
	private JSONObject mResult;
	private Callback mCallback;
	
	/**
	 * 无参构造方法 
	 * @param acti 调用者的上下文
	 */
	public ReadFeedbackTask(Activity acti) {
		super(acti);
	}
	
	/**
	 * 有参构造方法
	 * @param acti 调用者的上下文
	 * @param callback 回调方法
	 */
	public ReadFeedbackTask(Activity acti, Callback callback) {
		super(acti);
		mCallback = callback;
	}

	/**  
     * 回调方法的接口  
     *  
     */
	public interface Callback{
		public void getResult(JSONObject result);
	}

	/**
	 * 调用API
	 */
	@Override
	protected Integer doInBackground(String... params) {
		UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		String tokenCode = userToken.getTokenCode();
		String userCode = userToken.getUserCode();
		
		LinkedHashMap<String, Object> reqparams = new LinkedHashMap<String,Object>();
		reqparams.put("userCode", userCode); // 接收者编码
		reqparams.put("tokenCode", tokenCode); 
		
		try {
			//调用API
			mResult = (JSONObject) API.reqCust("readFeedback", reqparams);
			int retCode = Integer.parseInt(mResult.get("code").toString());
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
		if( retCode == ErrorCode.SUCC ){
			
			mCallback.getResult(mResult);
			
		}else{
			mCallback.getResult(null);
		}
	}

}
