package cn.suanzi.baomi.shop.model;

import java.util.LinkedHashMap;

import net.minidev.json.JSONObject;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import cn.suanzi.baomi.base.ErrorCode;
import cn.suanzi.baomi.base.SzException;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.api.API;
import cn.suanzi.baomi.base.data.DB;
import cn.suanzi.baomi.base.model.SzAsyncTask;
import cn.suanzi.baomi.base.pojo.UserToken;
import cn.suanzi.baomi.shop.R;
import cn.suanzi.baomi.shop.activity.SettledCommitSuccActivity;

/**
 *提交申请入驻 
 * @author wensi.yu
 *
 */
public class ApplyEntryTask extends SzAsyncTask<String , String , Integer> {
	
	private final static String TAG  = "ApplyEntry" ;
	
	/**创建一个JSONObject对象**/
	private JSONObject mResult ;
	private Callback mCallback;

	public ApplyEntryTask(Activity acti) {
		super(acti);
	}
	
	/**
	 * 有参构造方法
	 * @param acti 调用者的上下文
	 * @param callback 回调方法
	 */
	public ApplyEntryTask(Activity acti, Callback callback) {
		super(acti);
		mCallback = callback;
	}
	
	/**  
     * 回调方法的接口  
     */
	public interface Callback{
		public void getResult(JSONObject mResult);
	}
	
	@Override
	protected Integer doInBackground(String... params) {
		UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		String shopCode = "";
		if (DB.getBoolean(DB.Key.CUST_LOGIN)) {
			shopCode = userToken.getShopCode();
		}else{
			shopCode = "";
		}
		LinkedHashMap<String, Object> reqParams = new LinkedHashMap<String , Object>() ;
		reqParams.put("shopCode" , shopCode) ;
		reqParams.put("shopName" , params[0]) ;
		reqParams.put("tel" , params[1]) ;
		reqParams.put("startTime" , params[2]) ;
		reqParams.put("endTime" , params[3]) ;
		reqParams.put("street" , params[4]) ;
		reqParams.put("mobileNbr" , params[5]) ;
		
		try {
			mResult = (JSONObject) API.reqShop("applyEntry", reqParams) ;
			// 如果成功，保存到数据库
			return Integer.parseInt(String.valueOf(mResult.get("code"))) ;
		} catch (SzException e) {
			this.mErrCode = e.getErrCode() ;
			return this.mErrCode.getCode() ;
		}
	}
	
	@Override
	protected void handldBuziRet(int retCode) {
		if (retCode == ErrorCode.SUCC) {
			Log.d(TAG, "成功了。。。。。。。");
			mCallback.getResult(mResult);
		}else{
			if (retCode == ErrorCode.USER_NOT_AUTHORIZED) {
				Intent intent = new Intent(mActivity, SettledCommitSuccActivity.class);
				mActivity.startActivity(intent);
			}else{
				Util.getContentValidate(R.string.toast_commitsucc_fail);
			}
		}
	}
}
