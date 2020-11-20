package cn.suanzi.baomi.shop.model;

import java.util.LinkedHashMap;

import net.minidev.json.JSONObject;
import android.app.Activity;
import android.util.Log;
import android.widget.Toast;
import cn.suanzi.baomi.base.ErrorCode;
import cn.suanzi.baomi.base.SzException;
import cn.suanzi.baomi.base.api.API;
import cn.suanzi.baomi.base.model.SzAsyncTask;

public class MyShopPicAddTask extends SzAsyncTask<String, String, Integer> {
	private final static String TAG = "MyShopPicAddTask";
	/** 创建一个JSONObject对象**/
	private JSONObject mResult;
	
	/**
	 * 构造函数
	 * @param acti
	 */
	public MyShopPicAddTask(Activity acti) {
		super(acti);
	}
	
	/**
	 * 回调方法	
	 */
	private Callback callback ;
		
	/***
	 * @param acti 上下文
	 * @param callback 回调方法
	 */
	public MyShopPicAddTask(Activity acti , Callback callback) {
		super(acti);
		this.callback = callback ;
	}
	
	/**  
     * 回调方法的接口 
     */  
    public interface Callback{  
        public void getResult(JSONObject object) ;  
    }
	
    /**
     * 基本的业务逻辑操作
     */
	@Override
	protected void handldBuziRet(int retCode) {
		if (retCode == ErrorCode.SUCC) {
			callback.getResult(mResult) ;
		} 
		else{
			Log.d(this.mActivity.getClass().getSimpleName(), "" + retCode+ " - " + ErrorCode.getMsg(retCode));
			Toast.makeText(this.mActivity, ErrorCode.getMsg(retCode),Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 调用API
	 */
	@Override
	protected Integer doInBackground(String... params) {
		LinkedHashMap<String, Object> reqParams = new LinkedHashMap<String , Object>() ;
		reqParams.put("shopCode" , params[0]) ;
		reqParams.put("imgUrl" , params[1]) ;
		reqParams.put("tokenCode" , params[2]) ;
		try {
			mResult = (JSONObject) API.reqShop("updateShopDecImg" , reqParams) ;
			// 如果成功，保存到数据库
			return Integer.parseInt(String.valueOf(mResult.get("code"))) ;
		} catch (SzException e) {
			this.mErrCode = e.getErrCode() ;
			return this.mErrCode.getCode() ;
		}
	}

}
