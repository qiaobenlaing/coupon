// ---------------------------------------------------------
// @author    yanfang.li
// @version   1.0.0
// @createTime 2015.5.22 
// @copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
// ---------------------------------------------------------
package cn.suanzi.baomi.shop.model;

import java.util.LinkedHashMap;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import android.app.Activity;
import cn.suanzi.baomi.base.SzException;
import cn.suanzi.baomi.base.api.API;
import cn.suanzi.baomi.base.data.DB;
import cn.suanzi.baomi.base.model.SzAsyncTask;
import cn.suanzi.baomi.base.pojo.UserToken;

/**
 * 8.22	获得商家发布的优惠券
 * @author yanfang.li
 */
public class ListShopCouponTask extends SzAsyncTask<String, Integer, Integer> {

	private final static String TAG = ListShopCouponTask.class.getSimpleName();
	/** 定义一个正确的返回结果码 **/ 
	private final static int RIGHT_RET_CODE = 1;
	/** 定义一个错误的返回结果码 **/ 
	private final static int ERROR_RET_CODE = 0;
	/** 调用API返回对象 **/
	private JSONObject mResult;
	/** 回调方法 **/
	private Callback mCallback;
	
	/**
	 * 无参构造方法 
	 * @param acti 调用者的上下文
	 */
	public ListShopCouponTask(Activity acti) {
		super(acti);
	}
	
	/**
	 * 有参构造方法
	 * @param acti 调用者的上下文
	 * @param callback 回调方法
	 */
	public ListShopCouponTask(Activity acti, Callback callback) {
		super(acti);
		mCallback = callback;
	}
	
	/**  
     * 回调方法的接口  
     *  
     */
	public interface Callback{
		public void getResult(JSONObject result);
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (mActivity != null) {
			if (mProcessDialog != null) {
				mProcessDialog.dismiss();
			}
		}
	}
	
	/**
	 * 调用API查询，统计会员卡
	 * params[0] 查询时间
	 * params[1] 页码
	 */
	@Override
	protected Integer doInBackground(String... params) {
		LinkedHashMap<String, Object> reqparams = new LinkedHashMap<String,Object>();
		// 获得一个用户信息对象
		UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
	    String shopCode = userToken.getShopCode();// 商家编码
	    String tokenCode = userToken.getTokenCode();// 用户登录后获得的令牌
		reqparams.put("shopCode", shopCode);
		reqparams.put("time", params[0]);
		reqparams.put("page", params[1]);
		reqparams.put("tokenCode", tokenCode);
		
		try {
			//调用API
			mResult = (JSONObject) API.reqShop("listShopCoupon", reqparams);
			int retCode = ERROR_RET_CODE;
			JSONArray arResult = (JSONArray) mResult.get("couponList");
			if (!"[]".equals(arResult.toString()) || arResult.size() != 0) {
				retCode = RIGHT_RET_CODE; 
			}
			
			return retCode;
		} catch (SzException e) {
			
			this.mErrCode = e.getErrCode();
			return this.mErrCode.getCode();//返回错误编码
		}
		 
	}
	
	/**
	 * 处理查询结果的返回值 
	 * @param retCode执行成功返回码
	 */
	@Override
	protected void handldBuziRet(int retCode) {
		
		//RIGHT_RET_CODE的值是1  如果retCode访问成功的发 返回的是1  
		if ( retCode == RIGHT_RET_CODE ) {
			mCallback.getResult(mResult);
		} else {
			mCallback.getResult(null);
		}
	}

}
