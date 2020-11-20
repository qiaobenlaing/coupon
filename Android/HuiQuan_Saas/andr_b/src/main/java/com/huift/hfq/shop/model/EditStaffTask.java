package com.huift.hfq.shop.model;

import java.util.LinkedHashMap;

import com.huift.hfq.base.ErrorCode;
import com.huift.hfq.base.SzException;
import com.huift.hfq.base.api.API;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.model.SzAsyncTask;
import com.huift.hfq.base.pojo.UserToken;

import net.minidev.json.JSONObject;
import android.app.Activity;

/**
 * 添加员工
 * @author wensi.yu
 *
 */
public class EditStaffTask extends SzAsyncTask<String , String , Integer>{
	
	private final static String TAG  = "EditStaffTask" ;
	
	/**创建一个JSONObject对象**/
	private JSONObject mResult ;
	private Callback mCallback;

	public EditStaffTask(Activity acti) {
		super(acti);
	}
	
	/**
	 * 有参构造方法
	 * @param acti 调用者的上下文
	 * @param callback 回调方法
	 */
	public EditStaffTask(Activity acti, Callback callback) {
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
	 * params[0] 修改员工信息的输入参数 账号
	 * params[1] 修改员工信息的输入参数 姓名
	 * params[2] 修改员工信息的输入参数 类别
	 * params[3] 修改员工信息的输入参数 编码
	 * params[4] 需要令牌认证的编码
	 */
	@Override
	protected Integer doInBackground(String... params) {
		UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		String parentCode = userToken.getStaffCode();
		String tokenCode = userToken.getTokenCode();
		LinkedHashMap<String, Object> reqParams = new LinkedHashMap<String , Object>() ;
		reqParams.put("staffCode" , params[0]) ;
		reqParams.put("mobileNbr" , params[1]) ;
		reqParams.put("realName" , params[2]) ;
		reqParams.put("userLvl" , params[3]) ;
		reqParams.put("shopCode" , params[4]) ;
		reqParams.put("parentCode" ,parentCode) ;
		reqParams.put("tokenCode" , tokenCode) ;
		
		try {
			mResult = (JSONObject) API.reqShop("editStaffB" , reqParams) ;
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
