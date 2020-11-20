package cn.suanzi.baomi.shop.model;

import java.util.LinkedHashMap;

import net.minidev.json.JSONObject;
import android.app.Activity;
import android.os.Handler;
import cn.suanzi.baomi.base.ErrorCode;
import cn.suanzi.baomi.base.SzException;
import cn.suanzi.baomi.base.api.API;
import cn.suanzi.baomi.base.data.DB;
import cn.suanzi.baomi.base.model.SzAsyncTask;
import cn.suanzi.baomi.base.pojo.Decoration;
import cn.suanzi.baomi.base.pojo.UserToken;

/**
 * 修改子相册的图片
 * @author qian.zhou
 */
public class UpdateSubAlbumPhotoTask extends SzAsyncTask<String , String , Integer> {
	private final static String TAG = "UpdateSubAlbumPhotoTask" ;
	/**创建一个JSONObject对象**/
	private JSONObject mResult ;
	private Callback mCallback;
	/**修改数据的对象*/
	private Decoration mDecoration = null;
	public static final String DECORATION = "mDecoration";
	
	/**
	 * 构造函数
	 * @param acti
	 */
	public UpdateSubAlbumPhotoTask(Activity acti) {
		super(acti) ;
	}
	
	/**
	 * 有参构造方法
	 * @param acti 调用者的上下文
	 * @param callback 回调方法
	 */
	public UpdateSubAlbumPhotoTask(Activity acti, Callback callback) {
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
		} else{
			mCallback.getResult(retCode);
		}
	}
	
	/**
	 * 调用API，修改子相册图片
	 */
	@Override
	protected Integer doInBackground(String... params) {
		UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		String takenCode = userToken.getTokenCode();
		LinkedHashMap<String, Object> reqParams = new LinkedHashMap<String, Object>() ;
		reqParams.put("subAlbumCode", params[0]) ;
		reqParams.put("url", params[1]) ;
		reqParams.put("title", params[2]) ;
		reqParams.put("price", params[3]) ;
		reqParams.put("des", params[4]) ;
		reqParams.put("code", params[5]) ;
		reqParams.put("tokenCode", takenCode) ;
		
		mDecoration = new Decoration();
		mDecoration.setUrl(params[1]);
		mDecoration.setTitle(params[2]);
		mDecoration.setPrice(params[3]);
		mDecoration.setCode(params[5]);
		
		try {
			mResult = (JSONObject) API.reqShop("updateSubAlbumPhoto", reqParams) ;
			// 如果成功，保存到数据库
			return Integer.parseInt(String.valueOf(mResult.get("code").toString())) ;
		} catch (SzException e) {
			this.mErrCode = e.getErrCode() ;
			return this.mErrCode.getCode() ;
		}
	}
}
