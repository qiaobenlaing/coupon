package cn.suanzi.baomi.shop.model;

import java.util.LinkedHashMap;

import net.minidev.json.JSONObject;
import android.app.Activity;
import cn.suanzi.baomi.base.ErrorCode;
import cn.suanzi.baomi.base.SzException;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.api.API;
import cn.suanzi.baomi.base.data.DB;
import cn.suanzi.baomi.base.model.SzAsyncTask;
import cn.suanzi.baomi.base.pojo.UserToken;

/**
 * 验证优惠券，用于兑换券，代金券的使用
 * @author qian.zhou
 */
public class UseCouponTask extends SzAsyncTask<String , String , Integer> {
	/**创建一个JSONObject对象**/
	private JSONObject mResult ;
	
	/**
	 * 构造函数
	 * @param acti
	 */
	public UseCouponTask(Activity acti) {
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
	public UseCouponTask(Activity acti , Callback callback) {
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
			callback.getResult(mResult) ;
		}else{
			callback.getResult(mResult) ;
		}
	}
	
	/**
	 * 调用API,用于验证兑换券、代金券的验证和使用
	 */
	@Override
	protected Integer doInBackground(String... params) {
		UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		LinkedHashMap<String , Object> reqParams = new LinkedHashMap<String , Object>() ;
		reqParams.put("userCode" , params[0]) ;//用户编码
		reqParams.put("shopCode" , userToken.getShopCode()) ;//商家编码
		reqParams.put("userCouponCode" , params[1]) ;//商家编码
		reqParams.put("tokenCode" , userToken.getTokenCode()) ;//需要令牌认证
		try {
			mResult = (JSONObject) API.reqShop("useCoupon" , reqParams) ;
			// 如果成功，保存到数据库
			return Integer.parseInt(String.valueOf(mResult.get("code"))) ;
		} catch (SzException e) {
			this.mErrCode = e.getErrCode() ;
			return this.mErrCode.getCode() ;
		}
	}
}
