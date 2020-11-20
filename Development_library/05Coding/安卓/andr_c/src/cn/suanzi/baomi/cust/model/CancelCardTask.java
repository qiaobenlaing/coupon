package cn.suanzi.baomi.cust.model;

import java.util.LinkedHashMap;

import net.minidev.json.JSONObject;
import android.R.integer;
import android.app.Activity;
import cn.suanzi.baomi.base.ErrorCode;
import cn.suanzi.baomi.base.SzException;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.api.API;
import cn.suanzi.baomi.base.data.DB;
import cn.suanzi.baomi.base.model.SzAsyncTask;
import cn.suanzi.baomi.base.pojo.UserToken;
import cn.suanzi.baomi.cust.R;

/**
 * 点击取消关注立即调用
 * @author qian.zhou
 */
public class CancelCardTask extends SzAsyncTask<String, integer, Integer> {
	public static final int INVALID_ITEM_POS = -1;
	
	/**创建一个JSONObject对象**/
	private JSONObject mResult ;
	/** 待取消的item在itemList中的position，从0开始。-1表示未赋值 */
	private int itemPos = INVALID_ITEM_POS;
	/**
	 * 构造函数
	 * @param acti
	 */
	public CancelCardTask(Activity acti) {
		super(acti);
	}
	
	/**
	 * 回调方法 	
	 */
	private Callback callback ;
		
	/**
	 * @param acti 上下文
	 * @param callback 回调方法
	 */
	public CancelCardTask(Activity acti , int position, Callback callback) {
		super(acti) ;
		this.itemPos = position;
		this.callback = callback ;
	}
	
	@Override
	protected void onPreExecute() {
		if (mActivity != null && mProcessDialog != null) {
			mProcessDialog.dismiss();
		}
	}
	
	@Override
	protected void handldBuziRet(int retCode) {
		if (retCode == ErrorCode.SUCC) {
			callback.getResult(ErrorCode.SUCC, itemPos) ;
			Util.getContentValidate(R.string.cancel_succ);
		}else{
			callback.getResult(ErrorCode.FAIL, itemPos) ;
			Util.getContentValidate(R.string.cancel_fail);
		}
	}
	/**  
     * 回调方法的接口  
     */  
    public interface Callback{  
        public void getResult(int object, int itemPos) ;  
    }
    
	@Override
	protected Integer doInBackground(String... params) {
		LinkedHashMap<String, Object> reqParams = new LinkedHashMap<String, Object>() ;
		UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		String userCode = userToken.getUserCode();
		String tokenCode = userToken.getTokenCode();
		reqParams.put("userCode" , userCode) ;
		reqParams.put("shopCode" , params[0]) ;
		reqParams.put("tokenCode" , tokenCode) ;
		try {
			mResult =  (JSONObject) API.reqCust("cancelFollowShop", reqParams);
			int retCode = Integer.parseInt(mResult.get("code").toString());
			return retCode;
		} catch (SzException e) {
			this.mErrCode = e.getErrCode() ;
			return this.mErrCode.getCode() ;
		}
	}
}
