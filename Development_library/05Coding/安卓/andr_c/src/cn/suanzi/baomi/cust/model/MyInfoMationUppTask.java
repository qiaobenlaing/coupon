package cn.suanzi.baomi.cust.model;

import java.util.LinkedHashMap;

import net.minidev.json.JSONObject;
import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import cn.suanzi.baomi.base.ErrorCode;
import cn.suanzi.baomi.base.SzException;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.api.API;
import cn.suanzi.baomi.base.data.DB;
import cn.suanzi.baomi.base.model.SzAsyncTask;
import cn.suanzi.baomi.base.pojo.User;
import cn.suanzi.baomi.base.pojo.UserToken;
import cn.suanzi.baomi.cust.R;
import cn.suanzi.baomi.cust.activity.MyInfoMationActivity;
import cn.suanzi.baomi.cust.application.CustConst;

/**
 * 修改个人信息
 * @author qian.zhou
 */
public class MyInfoMationUppTask extends SzAsyncTask<String , String , Integer> {
	private final static String TAG  = MyInfoMationUppTask.class.getSimpleName() ;
	/**创建一个JSONObject对象**/
	private JSONObject mResult ;
	private Callback mCallback;
	private User mUser = null;
	public static final String USER = "mUser";
	
	/**
	 * 构造函数
	 * @param acti
	 */
	public MyInfoMationUppTask(Activity acti) {
		super(acti) ;
	}
	
	/**
	 * 有参构造方法
	 * @param acti 调用者的上下文
	 * @param callback 回调方法
	 */
	public MyInfoMationUppTask(Activity acti, Callback callback) {
		super(acti);
		mCallback = callback;
	}
	
	/**  
     * 回调方法的接口  
     */
	public interface Callback{
		/**
		 * @param result 是异步请求的结果
		 */
		public void getResult(JSONObject  mResult);
	}

    /**
   	 * onPostExecute()中的正常业务逻辑处理.
   	 */
	@Override
	protected void handldBuziRet(final int retCode) {
		if (retCode == ErrorCode.SUCC) {
			 new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						mCallback.getResult(mResult);
					}
				}, 1000);
			 	Util.getContentValidate(R.string.update_success);
				Intent intent = new Intent();
				intent.putExtra(USER, mUser);
				this.mActivity.setResult(MyInfoMationActivity.INTENT_RESP_SAVED, intent);
				this.mActivity.finish();
		}else{
			mCallback.getResult(mResult);
			if(CustConst.InfoMation.INFOMATION_NO_FULL == retCode){
				Util.getContentValidate(R.string.input_full);
			} else if(CustConst.InfoMation.INFOMATION_NO_UPPNOTHING == retCode){
				Util.getContentValidate(R.string.input_update_nothing);
			} 
		}
	}

	/**
	 * 调用API
	 */
	@Override
	protected Integer doInBackground(String... params) {
		UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		LinkedHashMap<String, Object> reqParams = new LinkedHashMap<String , Object>() ;
		JSONObject updateInfo = new JSONObject();
		updateInfo.put("realName", params[1]);
		updateInfo.put("nickName", params[2]);
		updateInfo.put("city", params[3]);
		updateInfo.put("signature", params[4]);
		updateInfo.put("sex", params[5]);
		updateInfo.put("avatarUrl", params[6]);
		reqParams.put("mobileNbr" , params[0]) ;
		reqParams.put("updateInfo" , updateInfo) ;
		reqParams.put("tokenCode" , userToken.getTokenCode()) ;
		
		mUser = new User();
		mUser.setMobileNbr(params[0]);
		mUser.setRealName(params[1]);
		mUser.setNickName(params[2]);
		mUser.setCity(params[3]);
		mUser.setSignature(params[4]);
		mUser.setSex(params[5]);
		mUser.setAvatarUrl(params[6]);
		
		try {
			mResult=(JSONObject) API.reqCust("updateUserInfo" , reqParams) ;
			// 如果成功，保存到数据库
			return Integer.parseInt(String.valueOf(mResult.get("code"))) ;
		} catch (SzException e) {
			this.mErrCode = e.getErrCode() ;
			return this.mErrCode.getCode() ;
		}
	}
}
