package cn.suanzi.baomi.shop.model;

import net.minidev.json.JSONObject;
import android.app.Activity;
import cn.suanzi.baomi.base.model.SzAsyncTask;

/**
 * 优惠券对账
 * @author wensi.yu
 *
 */
public class AccntCouponTask extends SzAsyncTask<String, String, Integer>{

	private final static String TAG = "AccntCouponTask";
	/** 定义一个正确的返回结果码 **/ 
	private final static int RIGHT_RET_CODE = 1;
	/** 定义一个错误的返回结果码 **/ 
	private final static int ERROR_RET_CODE = 0;
	/** 回调方法 **/
	private Callback mCallback;
	/** 调用API返回对象 **/
	private JSONObject mResult;
	
	
	public AccntCouponTask(Activity acti) {
		super(acti);
	}
	
	/**
	 * 有参构造方法
	 * @param acti 调用者的上下文
	 * @param callback 回调方法
	 */
	public AccntCouponTask(Activity acti, Callback callback) {
		super(acti);
		mCallback = callback;
	}

	/**  
     * 回调方法的接口  
     *  
     */
	public interface Callback{
		/**
		 * 传递参数
		 * @param result 是异步请求的结果
		 */
		public void getResult(JSONObject result);
	}

	/**
	 * 优惠券对账
	 */
	@Override
	protected Integer doInBackground(String... params) {
		return null;
	}
	
	/**
	 * 业务逻辑处理
	 */
	@Override
	protected void handldBuziRet(int retCode) {
		if ( retCode == RIGHT_RET_CODE ) {
			mCallback.getResult(mResult);
			
		} else {
			if (retCode == ERROR_RET_CODE ) {
				mCallback.getResult(null);
			}
		}
	}
}
