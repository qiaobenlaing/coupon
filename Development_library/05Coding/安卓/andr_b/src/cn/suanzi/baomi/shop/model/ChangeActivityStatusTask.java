package cn.suanzi.baomi.shop.model;

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
 * 改变活动状态
 * @author qian.zhou
 */
public class ChangeActivityStatusTask extends SzAsyncTask<String , String , Integer>{
	
	private final static String TAG  = ChangeActivityStatusTask.class.getSimpleName() ;
	
	/**创建一个JSONObject对象**/
	private JSONObject mResult ;
	private Callback mCallback;

	public ChangeActivityStatusTask(Activity acti) {
		super(acti);
	}
	
	/**
	 * 有参构造方法
	 * @param acti 调用者的上下文
	 * @param callback 回调方法
	 */
	public ChangeActivityStatusTask(Activity acti, Callback callback) {
		super(acti);
		mCallback = callback;
	}
	
	/**  
     * 回调方法的接口  
     */
	public interface Callback{
		public void getResult(JSONObject mResult);
	}

	/**
	 * 调用API，修改员工信息
	 * params[0] 活动编码
	 * params[1] 活动状态
	 * params[2] 需要令牌认证的编码
	 */
	@Override
	protected Integer doInBackground(String... params) {
		UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		String parentCode = userToken.getStaffCode();
		String tokenCode = userToken.getTokenCode();
		LinkedHashMap<String, Object> reqParams = new LinkedHashMap<String , Object>() ;
		reqParams.put("activityCode" , params[0]) ;
		reqParams.put("status" , params[1]) ;
		reqParams.put("tokenCode" , tokenCode) ;
		
		try {
			mResult = (JSONObject) API.reqShop("changeActivityStatus" , reqParams) ;
			// 如果成功，保存到数据库
			return Integer.parseInt(String.valueOf(mResult.get("code"))) ;
		} catch (SzException e) {
			this.mErrCode = e.getErrCode() ;
			return this.mErrCode.getCode() ;
		}
	}
	
	 /**
   	 * onPostExecute()中的正常业务逻辑处理.
   	 */
	@Override
	protected void handldBuziRet(final int retCode) {
		if (retCode == ErrorCode.SUCC) {
			mCallback.getResult(mResult);
		}else{
			mCallback.getResult(mResult);
		}
	}
}
