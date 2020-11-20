package com.huift.hfq.shop.model;

import java.util.LinkedHashMap;

import com.huift.hfq.base.ErrorCode;
import com.huift.hfq.base.SzException;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.api.API;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.model.SzAsyncTask;
import com.huift.hfq.base.pojo.ShopDecoration;
import com.huift.hfq.base.pojo.UserToken;
import com.huift.hfq.shop.ShopConst;
import com.huift.hfq.shop.activity.ProductPhotoActivity;

import net.minidev.json.JSONObject;
import android.app.Activity;
import android.content.Intent;
import android.os.Handler;

/**
 * 添加子相册
 * @author qian.zhou
 */
public class AddSubAlbumTask extends SzAsyncTask<String , String , Integer> {
	private final static String TAG = "AddSubAlbumTask" ;
	/**创建一个JSONObject对象**/
	private JSONObject mResult ;
	private Callback mCallback;
	/**添加数据的对象*/
	private ShopDecoration mShopDecoration = null;
	public static final String SHOPDECORATION = "mShopDecoration";
	
	/**
	 * 构造函数
	 * @param acti
	 */
	public AddSubAlbumTask(Activity acti) {
		super(acti) ;
	}
	
	/**
	 * 有参构造方法
	 * @param acti 调用者的上下文
	 * @param callback 回调方法
	 */
	public AddSubAlbumTask(Activity acti, Callback callback) {
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
			Util.showToastZH("添加成功");
			DB.saveBoolean(ShopConst.Key.UPP_DECORATION, true);
			Intent intent = new Intent(mActivity, ProductPhotoActivity.class);
			intent.putExtra(ProductPhotoActivity.CODE, mShopDecoration.getCode());
			intent.putExtra(ProductPhotoActivity.NAME, mShopDecoration.getName());
			mActivity.startActivity(intent);
			mActivity.finish();
		} else{
			if(ShopConst.Photo.ALBUM_NAME == retCode){
				Util.showToastZH("子相册名称错误，请重新输入");
			}
			mCallback.getResult(retCode);
		}
	}
	
	/**
	 * 调用API
	 */
	@Override
	protected Integer doInBackground(String... params) {
		UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		LinkedHashMap<String, Object> reqParams = new LinkedHashMap<String, Object>() ;
		reqParams.put("shopCode", userToken.getShopCode()) ;
		reqParams.put("name", params[0]) ;
		reqParams.put("tokenCode", userToken.getTokenCode()) ;
		
		mShopDecoration = new ShopDecoration();
		mShopDecoration.setName(params[0]);
		
		try {
			mResult = (JSONObject) API.reqShop("addSubAlbum", reqParams) ;
			if(String.valueOf(ErrorCode.SUCC).equals(mResult.get("code").toString())) {
				mShopDecoration.setCode(mResult.get("subAlbumCode").toString());
			} 
			// 如果成功，保存到数据库
			return Integer.parseInt(String.valueOf(mResult.get("code").toString())) ;
		} catch (SzException e) {
			this.mErrCode = e.getErrCode() ;
			return this.mErrCode.getCode() ;
		}
	}
}
