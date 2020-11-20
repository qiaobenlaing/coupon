package com.huift.hfq.shop.model;

import java.util.LinkedHashMap;

import com.huift.hfq.base.SzException;
import com.huift.hfq.base.api.API;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.model.SzAsyncTask;
import com.huift.hfq.base.pojo.UserToken;

import net.minidev.json.JSONObject;
import android.app.Activity;
import android.widget.Toast;

/**
 * 查询该用户是否接收短信提醒
 * @author qian.zhou
 */
public class GetShopStaffSettingTask extends SzAsyncTask<String , String , Integer> {
	/**创建一个JSONObject对象**/
	private JSONObject mResult ;
	/**定义一个正确的返回结果码**/
	private final static int RIGHT_RET_CODE = 1 ;
	/**定义一个错误的返回结果码**/ 
	private final static int ERROR_RET_CODE = 0;
	/**回调方法**/
	private Callback mCallback ;

	/**
	 * 回调方法的接口
	 */
	public interface Callback {
		public void getResult(JSONObject JSONobject) ;
	}
	
	/**
	 * 构造函数
	 * @param acti启动本对象的Activity
	 */
	public GetShopStaffSettingTask(Activity acti) {
		super(acti) ;
	}

	/**
	 * @param acti 上下文
	 * @param callback 回调方法
	 */
	public GetShopStaffSettingTask(Activity acti , Callback callback) {
		super(acti) ;
		this.mCallback = callback;
	}

	/**
	 * onPostExecute()中的正常业务逻辑处理.
	 */
	@Override
	protected void handldBuziRet(int retCode) {
		if (retCode == RIGHT_RET_CODE) {
			mCallback.getResult(mResult) ;
		}else if(retCode == ERROR_RET_CODE){
			mCallback.getResult(null);
			Toast.makeText(this.mActivity, "没有数据加载了",Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 调用API，获得所有员工信息列表
	 *  params[0] 是查询所有员工信息的输入参数
	 *  params[1] 是查询员工信息的输入参数 从1开始
	 *  params[2] 需要令牌认证的编码
	 */
	@Override
	protected Integer doInBackground(String... params) {
		UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		LinkedHashMap<String, Object> reqParams = new LinkedHashMap<String, Object>() ;
		reqParams.put("mobileNbr" , userToken.getMobileNbr()) ;
		reqParams.put("tokenCode" , userToken.getTokenCode()) ;
		try {
			mResult = (JSONObject)API.reqShop("getShopStaffSetting", reqParams);
			int retCode = ERROR_RET_CODE;
			//判断查询的一个对象不为空为空 就返回一个正确的编码
			if (mResult != null ) {
				retCode = RIGHT_RET_CODE; //1 代表访问成功
			}
			return retCode;
		}catch (SzException e) {
			this.mErrCode = e.getErrCode() ;
			return this.mErrCode.getCode() ;
		}
	}
}
