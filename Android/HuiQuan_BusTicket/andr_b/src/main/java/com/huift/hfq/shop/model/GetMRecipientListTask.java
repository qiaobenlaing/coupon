package com.huift.hfq.shop.model;

import java.util.LinkedHashMap;

import com.huift.hfq.base.ErrorCode;
import com.huift.hfq.base.SzException;
import com.huift.hfq.base.api.API;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.model.SzAsyncTask;
import com.huift.hfq.base.pojo.UserToken;

import net.minidev.json.JSONArray;
import android.app.Activity;
import android.util.Log;

/**
 * 短信接受者列表
 * @author yanfang.li
 */
public class GetMRecipientListTask extends SzAsyncTask<String , String , Integer> {
	
	private static final String TAG = GetMRecipientListTask.class.getSimpleName();
	/**创建一个JSONArray对象**/
	private JSONArray mResult ;
	
	/**
	 * 构造函数
	 * @param acti
	 */
	public GetMRecipientListTask(Activity acti) {
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
	public GetMRecipientListTask(Activity acti , Callback callback) {
		super(acti) ;
		this.callback = callback ;
	}

	/**  
     * 回调方法的接口  
     */  
    public interface Callback{  
        public void getResult(JSONArray result) ;  
    }
    
    @Override
    protected void onPreExecute() {
    	if (null != mActivity && null != mProcessDialog) {
    		mProcessDialog.dismiss();
    	}
    }
    
    /**
   	 * onPostExecute()中的正常业务逻辑处理.
   	 */
	@Override
	protected void handldBuziRet(int retCode) {
		if (retCode == ErrorCode.RIGHT_RET_CODE) {
			callback.getResult(mResult) ;
		}else{
			callback.getResult(null) ;
		}
	}
	
	/**
	 * 调用API,用于验证兑换券、代金券的验证和使用
	 */
	@Override
	protected Integer doInBackground(String... params) {
		UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		if (null == userToken) {
			return ErrorCode.RIGHT_RET_CODE;
		}
		LinkedHashMap<String , Object> reqParams = new LinkedHashMap<String , Object>() ;
		reqParams.put("shopCode" , userToken.getShopCode()) ;//商家编码
		reqParams.put("tokenCode" , userToken.getTokenCode()) ;//需要令牌认证
		try {
			mResult = (JSONArray) API.reqShop("getMRecipientList", reqParams) ;
			int retCode = ErrorCode.ERROR_RET_CODE;
			if (mResult.size() > 0 && !"[]".equals(mResult.toJSONString())) {
				retCode = ErrorCode.RIGHT_RET_CODE;
			}
			return retCode;
		} catch (SzException e) {
			this.mErrCode = e.getErrCode() ;
			return this.mErrCode.getCode() ;
		}
	}
}
