// ---------------------------------------------------------
// @author    yanfang.li
// @version   1.0.0
// @copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
// ---------------------------------------------------------
package cn.suanzi.baomi.shop.model;

import java.util.LinkedHashMap;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import android.app.Activity;
import cn.suanzi.baomi.base.SzException;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.api.API;
import cn.suanzi.baomi.base.model.SzAsyncTask;
import cn.suanzi.baomi.shop.R;

/**
 * 获得某一批次优惠券的使用详情列表,调用API
 * @author yanfang.li
 */
public class GetCouponCsmListTask extends SzAsyncTask<String, Integer, Integer> {

	private final static String TAG = "GetCouponCsmListTask";
	/** 定义一个正确的返回结果码 **/ 
	private final static int RIGHT_RET_CODE = 1;
	/** 定义一个错误的返回结果码 **/ 
	private final static int ERROR_RET_CODE = 0;
	/** 回调方法 **/
	private Callback mCallback;
	/** 调用API返回对象 **/
	private JSONObject mResult;
	/** 当前页*/
	private int mPage;
	
	/**
	 * 无参构造方法 
	 * @param acti 调用者的上下文
	 */
	public GetCouponCsmListTask(Activity acti) {
		super(acti);
	}
	
	/**
	 * 有参构造方法
	 * @param acti 调用者的上下文
	 * @param callback 回调方法
	 */
	public GetCouponCsmListTask(Activity acti, Callback callback) {
		super(acti);
		mCallback = callback;
	}

	/**  
     * 回调方法的接口  
     *  
     */
	public interface Callback{
		/**
		 * 传递参数
		 * @param result 是异步请求的结果
		 */
		public void getResult(JSONObject result);
	}

	/**
	 * 调用API查询，统计会员卡
	 * params[0] 每批次优惠券编码
	 * params[1] 页码
	 * params[2] 是令牌认证的编码
	 * 
	 */
	@Override
	protected Integer doInBackground(String... params) {
		
		LinkedHashMap<String, Object> reqparams = new LinkedHashMap<String,Object>();
		reqparams.put("batchCouponCode", params[0]);
		mPage = Integer.parseInt(params[1]);
		reqparams.put("page", params[1]);
		reqparams.put("tokenCode", params[2]);
		try {
			int retCode = ERROR_RET_CODE;
			if (!("[]".equals(API.reqShop("getCouponConsumeList", reqparams).toString()))) {
				mResult = (JSONObject)API.reqShop("getCouponConsumeList", reqparams);
				
				JSONArray arResult = (JSONArray) mResult.get("couponConsumeList");
				//判断查询的一个对象不为空为空 就返回一个正确的编码
				if (mResult == null || !"[]".equals(arResult.toString()) || arResult.size() != 0) {
					retCode = RIGHT_RET_CODE; //1 代表访问成功
				}
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
			if (mPage >1) {
				Util.getContentValidate(R.string.toast_moredata);
			} else {
				Util.getContentValidate(R.string.toast_nulldata);
		} 
		}
	}

}
