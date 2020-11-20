package cn.suanzi.baomi.shop.model;

import java.util.LinkedHashMap;

import net.minidev.json.JSONObject;
import android.app.Activity;
import android.os.Handler;
import cn.suanzi.baomi.base.ErrorCode;
import cn.suanzi.baomi.base.SzException;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.api.API;
import cn.suanzi.baomi.base.model.SzAsyncTask;
import cn.suanzi.baomi.shop.R;
import cn.suanzi.baomi.shop.ShopConst;

/**
 * 申请POS服务
 * @author qian.zhou
 */
public class MyPosServTask extends SzAsyncTask<String , String , Integer> {
	private final static String TAG = "MyPosServTask" ;
	/**创建一个JSONObject对象**/
	private JSONObject mResult ;
	private Callback mCallback;
	
	/**
	 * 构造函数
	 * @param acti
	 */
	public MyPosServTask(Activity acti) {
		super(acti) ;
	}
	
	/**
	 * 有参构造方法
	 * @param acti 调用者的上下文
	 * @param callback 回调方法
	 */
	public MyPosServTask(Activity acti, Callback callback) {
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
		public void getResult(JSONObject result);
	}
	
	/**
	 * onPostExecute()中的正常业务逻辑处理.
	 */
	@Override
	protected void handldBuziRet(int retCode) {
		if (retCode == ErrorCode.SUCC) {
           new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					mCallback.getResult(mResult);
				}
			}, 1000);
		} else{
			if(ShopConst.PosService.SHOPCODE_ERROR == retCode){
				Util.getContentValidate(R.string.shopcode_error);
			} else if(ShopConst.PosService.SHOP_NOEXIT == retCode){
				Util.getContentValidate(R.string.shop_noexit);
			} else if(ShopConst.PosService.SERVICETYPE_ERROR == retCode){
				Util.getContentValidate(R.string.servicetype_error);
			}
		}
	}
	
	/**
	 * 调用API，添加pos服务
	 */
	@Override
	protected Integer doInBackground(String... params) {
		LinkedHashMap<String, Object> reqParams = new LinkedHashMap<String, Object>() ;
		reqParams.put("shopCode", params[0]) ;
		reqParams.put("type", params[1]) ;
		reqParams.put("remark", params[2]) ;
		reqParams.put("tokenCode", params[3]) ;
		
		try {
			mResult = (JSONObject) API.reqShop("applyPosServer", reqParams);
			// 如果成功，保存到数据库
			return Integer.parseInt(String.valueOf(mResult.get("code").toString())) ;
		} catch (SzException e) {
			this.mErrCode = e.getErrCode() ;
			return this.mErrCode.getCode() ;
		}
	}
}
