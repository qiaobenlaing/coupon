package cn.suanzi.baomi.shop.model;

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
import cn.suanzi.baomi.base.pojo.UserToken;
import cn.suanzi.baomi.shop.fragment.StaffManagerFragment;

/**
 * 添加店长信息
 * @author qian.zhou
 */
public class MyStaffAddTask extends SzAsyncTask<String , String , Integer> {

	private final static String TAG  = "MyStaffAddTask" ;
	/**创建一个JSONObject对象**/
	private JSONObject mResult ;
	private Callback mCallback;
	/** 获得一个用户信息对象 **/
	private UserToken mUserToken;
	/** 用户登录后获得的令牌 **/
	private String mTokenCode;
	/** 登录的员工编码*/
	private String mParentCode;
	
	/**
	 * 构造函数
	 * @param acti
	 */
	public MyStaffAddTask(Activity acti) {
		super(acti) ;
	}
	
	/**
	 * 有参构造方法
	 * @param acti 调用者的上下文
	 * @param callback 回调方法
	 */
	public MyStaffAddTask(Activity acti, Callback callback) {
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
		public void getResult(int retCode);
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
						mCallback.getResult(retCode);
					}
		   }, 1000);
		    Util.showToastZH("添加成功!");
			Intent intent = new Intent();
			mActivity.setResult(StaffManagerFragment.ADD_IS_OK, intent);
		    mActivity.finish();
		}else{
			mCallback.getResult(retCode);
		}
	}

	/**
	 * 调用API
	 */
	@Override
	protected Integer doInBackground(String... params) {
		// 获得一个用户信息对象
		mUserToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		mTokenCode = mUserToken.getTokenCode();// 用户登录后获得的令牌
		mParentCode = mUserToken.getStaffCode();//父店员编码
		LinkedHashMap<String, Object> reqParams = new LinkedHashMap<String , Object>() ;
		reqParams.put("staffCode" , params[0]) ;
		reqParams.put("mobileNbr" , params[1]) ;
		reqParams.put("realName" , params[2]) ;
		reqParams.put("userLvl" , params[3]) ;
		reqParams.put("shopCode" , params[4]) ;
		reqParams.put("parentCode" , mParentCode) ;
		reqParams.put("tokenCode" , mTokenCode) ;
		
		try {
			mResult=(JSONObject) API.reqShop("editStaffB" , reqParams) ;
			// 如果成功，保存到数据库
			return Integer.parseInt(String.valueOf(mResult.get("code"))) ;
		} catch (SzException e) {
			this.mErrCode = e.getErrCode() ;
			return this.mErrCode.getCode() ;
		}
	}
}
