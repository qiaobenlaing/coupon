package com.huift.hfq.shop.model;

import java.util.LinkedHashMap;

import com.huift.hfq.base.ErrorCode;
import com.huift.hfq.base.SzException;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.api.API;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.model.SzAsyncTask;
import com.huift.hfq.base.pojo.Decoration;
import com.huift.hfq.base.pojo.UserToken;
import com.huift.hfq.shop.activity.ProductBigPhotoActivity;

import net.minidev.json.JSONObject;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

/**
 * 删除产品相册中的图片
 * @author qian.zhou
 */
public class DelSubAlbumPhotoTask extends SzAsyncTask<String , String , Integer> {
	private final static String TAG = "DelSubAlbumPhotoTask" ;
	/**创建一个JSONObject对象**/
	private JSONObject mResult ;
	private Context context ;
	/**删除数据的对象*/
	private Decoration mDecoration = null;
	public static final String DECORATION = "mDecoration";
	
	/**
	 * 构造函数
	 * @param acti
	 */
	public DelSubAlbumPhotoTask(Activity acti) {
		super(acti) ;
	}

	/**
	 * 回调方法 	
	 */
	private Callback callback ;
		
	/**
	 * @param acti 上下文
	 * @param callback 回调方法
	 */
	public DelSubAlbumPhotoTask(Activity acti , Callback callback) {
		super(acti) ;
		this.callback = callback ;
	}

	/**  
     * 回调方法的接口  
     */  
    public interface Callback{  
        public void getResult(JSONObject object) ;  
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
					callback.getResult(mResult) ;
				}
			}, 1000);
			Util.showToastZH( "删除成功");
			Intent intent = new Intent();
			intent.putExtra(DECORATION, mDecoration);
			mActivity.setResult(ProductBigPhotoActivity.DEL_RESP_PHOTO, intent);
			mActivity.finish();
		} 
		else{
			Util.showToastZH("删除失败了");
		}
	}

	/**
	 * 调用API，删除员工信息
	 */
	@Override
	protected Integer doInBackground(String... params) {
		UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		LinkedHashMap<String , Object> reqParams = new LinkedHashMap<String , Object>() ;
		reqParams.put("code" , params[0]) ;//商家编码
		reqParams.put("tokenCode" , userToken.getTokenCode()) ;//需要令牌认证
		
		mDecoration = new Decoration();
		mDecoration.setCode(params[0]);
		try {
			mResult = (JSONObject) API.reqShop("delSubAlbumPhoto" , reqParams) ;
			// 如果成功，保存到数据库
			return Integer.parseInt(String.valueOf(mResult.get("code"))) ;
		} catch (SzException e) {
			this.mErrCode = e.getErrCode() ;
			return this.mErrCode.getCode() ;
		}
	}
}
