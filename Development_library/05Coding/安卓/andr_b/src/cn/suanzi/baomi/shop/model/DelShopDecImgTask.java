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
import cn.suanzi.baomi.shop.fragment.MyPhotoEnviromentFragment;

/**
 * 删除环境图片信息
 * @author qian.zhou 
 */
public class DelShopDecImgTask extends SzAsyncTask<String, String, Integer> {
	private final static String TAG = "DelShopDecImgTask";
	/**创建一个JSONArray对象**/
	private JSONObject mResult;
	//回调方法
	private Callback mCallback;
	/**删除数据的对象*/
	private Decoration mDecoration = null;
	public static final String DECORATION = "mDecoration";
	
	/**
	 * 构造函数
	 * @param acti
	 */
	public DelShopDecImgTask(Activity acti) {
		super(acti);
	}
	
	/**
	 * 构造函数
	 * @param acti
	 */
	public DelShopDecImgTask(Activity acti, Callback callback) {
		super(acti);
		mCallback = callback;
	}

    /**
     * 业务逻辑操作
     */
	@Override
	protected void handldBuziRet(int retCode) {
		if (retCode == ErrorCode.SUCC) {
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					mCallback.getResult(mResult) ;
				}
			}, 1000);
			Util.showToastZH( "删除成功");
			DB.saveBoolean(ShopConst.Key.UPP_ALBUM_PHOTO, true);
			Intent intent = new Intent();
			intent.putExtra(DECORATION, mDecoration);
			mActivity.setResult(MyPhotoEnviromentFragment.DEL_RESP_PHOTO, intent);
			mActivity.finish();
		} 
		else{
			mCallback.getResult(null);
			Util.showToastZH("删除失败了");
		}
	}
	
	/**  
     * 回调方法的接口  
     *  
     */
	public interface Callback{
		public void getResult(JSONObject result);
	}

	/**
	 * 调用API
	 */
	@Override
	protected Integer doInBackground(String... params) {
		UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		LinkedHashMap<String, Object> reqParams = new LinkedHashMap<String, Object>();		
		reqParams.put("decorationCode", params[0]);
		reqParams.put("tokenCode", userToken.getTokenCode());
		
		mDecoration = new Decoration();
		mDecoration.setDecorationCode(params[0]);
		
		try {
			mResult = (JSONObject) API.reqShop("delShopDec", reqParams);
			//如果调用api成功，返回正确的结果码
			return Integer.parseInt(String.valueOf(mResult.get("code")).toString()); 
		} catch (SzException e) {
			this.mErrCode = e.getErrCode();
			return this.mErrCode.getCode();
		}
	}
}
