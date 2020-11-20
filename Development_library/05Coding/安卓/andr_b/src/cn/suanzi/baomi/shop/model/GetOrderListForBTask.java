package cn.suanzi.baomi.shop.model;

import java.util.LinkedHashMap;

import net.minidev.json.JSONObject;
import android.app.Activity;
import cn.suanzi.baomi.base.ErrorCode;
import cn.suanzi.baomi.base.SzException;
import cn.suanzi.baomi.base.api.API;
import cn.suanzi.baomi.base.data.DB;
import cn.suanzi.baomi.base.model.SzAsyncTask;
import cn.suanzi.baomi.base.pojo.UserToken;

/**
 * 不餐饮以外的订单
 * @author yanfang.li
 */
public class GetOrderListForBTask extends SzAsyncTask<String , String , Integer> {
	
	private static final String TAG = GetOrderListForBTask.class.getSimpleName();
	
	/**创建一个JSONObject对象**/
	private JSONObject mResult ;

	/**
	 * 构造函数
	 * @param acti
	 */
	public GetOrderListForBTask(Activity acti) {
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
	public GetOrderListForBTask(Activity acti , Callback callback) {
		super(acti) ;
		this.callback = callback ;
	}

	/**  
     * 回调方法的接口  
     */  
    public interface Callback{  
        public void getResult(JSONObject result) ;  
    }
    
    @Override
    protected void onPreExecute() {
    	if (mActivity == null && mProcessDialog == null) {
    		mProcessDialog.dismiss();
    	}
    }
    
    /**
   	 * onPostExecute()中的正常业务逻辑处理.
   	 */
	@Override
	protected void handldBuziRet(int retCode) {
		if (retCode == ErrorCode.RIGHT_RET_CODE) {
			callback.getResult(mResult);
		}else{
			callback.getResult(null);
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
		
		reqParams.put("shopCode" , userToken.getShopCode()) ; // 商家编码
		reqParams.put("keyWord" ,params[0]) ; // 输入参数
		reqParams.put("date" , params[1]) ; // 时间 eg：2014-02-04 
		reqParams.put("page" , params[2]) ; // 页码
		reqParams.put("tokenCode" , userToken.getTokenCode()) ;//需要令牌认证
		try {
			mResult = (JSONObject) API.reqShop("getOrderListForB" , reqParams) ;
			int retCode = ErrorCode.ERROR_RET_CODE;
			if (mResult != null && !"".equals(mResult.toJSONString())) {
				retCode = ErrorCode.RIGHT_RET_CODE;
			}
			return retCode;
		} catch (SzException e) {
			this.mErrCode = e.getErrCode() ;
			return this.mErrCode.getCode() ;
		}
	}
}
