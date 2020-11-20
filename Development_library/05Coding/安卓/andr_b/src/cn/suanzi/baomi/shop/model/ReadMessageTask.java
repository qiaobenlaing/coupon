// ---------------------------------------------------------
// @author    yanfang.li
// @version   1.0.0
// @createTime 2015.5.22 
// @copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
// ---------------------------------------------------------
package cn.suanzi.baomi.shop.model;

import java.util.LinkedHashMap;

import net.minidev.json.JSONObject;
import android.app.Activity;
import android.util.Log;
import cn.suanzi.baomi.base.ErrorCode;
import cn.suanzi.baomi.base.SzException;
import cn.suanzi.baomi.base.api.API;
import cn.suanzi.baomi.base.data.DB;
import cn.suanzi.baomi.base.model.SzAsyncTask;
import cn.suanzi.baomi.base.pojo.UserToken;

/**
 * 阅读消息
 * @author yanfang.li
 */
public class ReadMessageTask extends SzAsyncTask<String, Integer, Integer> {

	private final static String TAG = SendMsgTask.class.getSimpleName();
	private JSONObject mResult;
	private Callback mCallback;
	
	/**
	 * 无参构造方法 
	 * @param acti 调用者的上下文
	 */
	public ReadMessageTask(Activity acti) {
		super(acti);
	}
	/**
	 * 有参构造方法
	 * @param acti 调用者的上下文
	 * @param callback 回调方法
	 */
	public ReadMessageTask(Activity acti, Callback callback) {
		super(acti);
		mCallback = callback;
	}

	/**  
     * 回调方法的接口  
     */
	public interface Callback{
		public void getResult(int result);
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (mProcessDialog != null) {
			mProcessDialog.dismiss();
		}
	}
	
	@Override
	protected Integer doInBackground(String... params) {
		
		UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		String shopcode = userToken.getShopCode();
		String tokenCode = userToken.getTokenCode();
		LinkedHashMap<String, Object> reqparams = new LinkedHashMap<String,Object>();
		reqparams.put("shopCode", shopcode); // 商店编号
		reqparams.put("type", params[0]); // 商店编号
		reqparams.put("tokenCode", tokenCode); // 需要令牌认证
			
		try {
			//调用API
			mResult = (JSONObject) API.reqShop("readMessage", reqparams);
			Log.d(TAG, "***************  ssfsdfs ************** = "+mResult.toString());
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
		if( retCode == ErrorCode.SUCC ){
			mCallback.getResult(ErrorCode.SUCC);
		} else {
			mCallback.getResult(ErrorCode.API_INTERNAL_ERR);
		}
	}

}
