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
 * 投诉建议异步任务
 * @author qian.zhou
 */
public class SysSuggestTask extends SzAsyncTask<String, String, Integer> {
	private final static String TAG = "SysSuggestTask";
	/**创建一个JSONObject对象**/
	private JSONObject mResult ;
	private Callback mCallback;
	
	/**
	 * 构造函数
	 * @param acti
	 */
	public SysSuggestTask(Activity acti) {
		super(acti);
	}
	
	/**
	 * 有参构造方法
	 * @param acti 调用者的上下文
	 * @param callback 回调方法
	 */
	public SysSuggestTask(Activity acti, Callback callback) {
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
	
	@Override
	protected void handldBuziRet(int retCode) {
		if (retCode == ErrorCode.SUCC) {
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					mCallback.getResult(mResult);
				}
			}, 1000);
		} 
		else{
			mCallback.getResult(mResult);
			if(ShopConst.SysSuggest.FEEDBACK_CONTENT == retCode){
				Util.getContentValidate(R.string.feedback_content);
			} else if(retCode == ErrorCode.API_INTERNAL_ERR){
				Util.getContentValidate(R.string.submit_fail);
			}
		}
	}

	@Override
	protected Integer doInBackground(String... params) {
		//投诉建议
		LinkedHashMap<String, Object> reqParams = new LinkedHashMap<String, Object>();
		reqParams.put("creatorCode", params[0]);
		reqParams.put("content", params[1]);
		reqParams.put("tokenCode", params[2]);
		try {
			//调用API
			mResult = (JSONObject) API.reqComm("addFeedback" , reqParams) ;
			// 如果成功，保存到数据库
			return Integer.parseInt(String.valueOf(mResult.get("code"))) ;
		} catch (SzException e) {
			this.mErrCode = e.getErrCode() ;
			return this.mErrCode.getCode() ;
		}
	}
}
