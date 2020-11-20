// ---------------------------------------------------------
// @author    yanfang.li
// @version   1.0.0
// @createTime 2015.5.22 
// @copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
// ---------------------------------------------------------
package cn.suanzi.baomi.shop.model;

import java.util.LinkedHashMap;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import android.app.Activity;
import cn.suanzi.baomi.base.SzException;
import cn.suanzi.baomi.base.api.API;
import cn.suanzi.baomi.base.data.DB;
import cn.suanzi.baomi.base.model.SzAsyncTask;
import cn.suanzi.baomi.base.pojo.UserToken;

/**
 * 获得会员列表,调用API
 * @author yanfang.li
 */
public class ListCardVipTask extends SzAsyncTask<String, Integer, Integer> {

	private final static String TAG = "ListCardVipTask";
	/** 定义一个正确的返回结果码 **/ 
	private final static int RIGHT_RET_CODE = 1;
	/** 定义一个错误的返回结果码 **/ 
	private final static int ERROR_RET_CODE = 0;
	/** 回调方法 **/
	private Callback mCallback;
	/** 调用API返回对象 **/
	private JSONObject mResult;
	
	/**
	 * 无参构造方法 
	 * @param acti 调用者的上下文
	 */
	public ListCardVipTask(Activity acti) {
		super(acti);
	}
	
	/**
	 * 有参构造方法
	 * @param acti 调用者的上下文
	 * @param callback 回调方法
	 */
	public ListCardVipTask(Activity acti, Callback callback) {
		super(acti);
		mCallback = callback;
	}

	/**  
     * 回调方法的接口  
     *  
     */
	public interface Callback{
		public void getResult(JSONObject rsult);
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (mProcessDialog != null) {
			mProcessDialog.dismiss();
		}
	}
	
	/**
	 * 调用API查询，统计会员卡
	 * params[0] 会员卡的id
	 * params[1] 用户姓名
	 * params[2] 排序
	 * params[3] 页码
	 * params[4] 是令牌认证的编码
	 */
	@Override
	protected Integer doInBackground(String... params) {
		UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		String tokenCode = userToken.getTokenCode();// 用户登录后获得的令牌
		LinkedHashMap<String, Object> reqparams = new LinkedHashMap<String,Object>();
		reqparams.put("cardCode", params[0]);
		reqparams.put("userName", params[1]);
		reqparams.put("orderType", params[2]);
		reqparams.put("page", params[3]);
		reqparams.put("tokenCode", tokenCode);
		
		try {
			//调用API
			mResult = (JSONObject) API.reqShop("listCardVip", reqparams);
			int retCode = ERROR_RET_CODE;
			//判断查询的一个对象不为空为空 就返回一个正确的编码
			JSONArray arResult = (JSONArray) mResult.get("cardVipList");
			//判断查询的一个对象不为空为空 就返回一个正确的编码
			if (mResult == null || !"[]".equals(arResult.toString()) || arResult.size() != 0) {
				retCode = RIGHT_RET_CODE; //1 代表访问成功
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
		
		if ( retCode == RIGHT_RET_CODE ) {
			mCallback.getResult(mResult);
			
		} else {
			mCallback.getResult(null);
		}
	}

}
