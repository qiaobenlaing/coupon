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
import cn.suanzi.baomi.base.pojo.Decoration;
import cn.suanzi.baomi.base.pojo.UserToken;
import cn.suanzi.baomi.shop.ShopConst;
import cn.suanzi.baomi.shop.activity.UploadEnvironmentPhotoActivity;

/**
 * 上传店铺装修的图片信息
 * @author qian.zhou 
 */
public class AddShopDecImgTask extends SzAsyncTask<String, String, Integer> {
	private final static String TAG = "AddShopDecImgTask";
	/**创建一个JSONArray对象**/
	private JSONObject mResult;
	private Callback mCallback;
	/**添加数据的对象*/
	private Decoration mDecoration = null;
	public static final String DECORATION = "mDecoration";
	
	/**
	 * 构造函数
	 * @param acti
	 */
	public AddShopDecImgTask(Activity acti) {
		super(acti);
	}
	
	/**
	 * 有参构造方法
	 * @param acti 调用者的上下文
	 * @param callback 回调方法
	 */
	public AddShopDecImgTask(Activity acti, Callback callback) {
		super(acti);
		mCallback = callback;
	}
	
	/**  
     * 回调方法的接口  
     */
	public interface Callback{
		public void getResult(int retCode);
	}
	
    /**
     * 业务逻辑操作
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
			Util.showToastZH("上传图片成功");
			DB.saveBoolean(ShopConst.Key.UPP_ALBUM_PHOTO, true);
			Intent intent = new Intent();
			intent.putExtra(DECORATION, mDecoration);
			mActivity.setResult(UploadEnvironmentPhotoActivity.INTENT_RESP_SAVED, intent);
			mActivity.finish();
		} else{
			Util.showToastZH("上传图片失败");
			mCallback.getResult(retCode);
		}
	}

	/**
	 * 调用API
	 */
	@Override
	protected Integer doInBackground(String... params) {
		UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		LinkedHashMap<String, Object> reqParams = new LinkedHashMap<String, Object>();		
		reqParams.put("shopCode", userToken.getShopCode());
		reqParams.put("imgUrl", params[0]);
		reqParams.put("title", params[1]);
		reqParams.put("tokenCode", userToken.getTokenCode());
		
		mDecoration = new Decoration();
		mDecoration.setImgUrl(params[0]);
		mDecoration.setTitle(params[1]);
		
		try {
			mResult = (JSONObject) API.reqShop("addShopDecImg", reqParams);
			//如果调用api成功，返回正确的结果码
			return Integer.parseInt(String.valueOf(mResult.get("code")).toString()); 
		} catch (SzException e) {
			this.mErrCode = e.getErrCode();
			return this.mErrCode.getCode();
		}
	}
}
