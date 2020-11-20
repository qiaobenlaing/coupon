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
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.api.API;
import cn.suanzi.baomi.base.model.SzAsyncTask;
import cn.suanzi.baomi.shop.R;

/**
 * 获得优惠券列表,调用API getCouponList 方法
 * @author yanfang.li
 */
public class GetCouponListTask extends SzAsyncTask<String, Integer, Integer> {

	private final static String TAG = "GetCouponListTask";
	/** 定义一个正确的返回结果码 **/ 
	private final static int RIGHT_RET_CODE = 1;
	/** 定义一个错误的返回结果码 **/ 
	private final static int ERROR_RET_CODE = 0;
	/** 调用API返回对象 **/
	private JSONObject mResult;
	/** 回调方法 **/
	private Callback mCallback;
	private int mPage;
	
	
	/**
	 * 无参构造方法 
	 * @param acti 调用者的上下文
	 */
	public GetCouponListTask(Activity acti) {
		super(acti);
	}
	
	/**
	 * 有参构造方法
	 * @param acti 调用者的上下文
	 * @param callback 回调方法
	 */
	public GetCouponListTask(Activity acti, Callback callback) {
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

	/*@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (mProcessDialog != null) {
			mProcessDialog.dismiss();
		}
	}*/
	
	/**
	 * 调用API查询，统计会员卡
	 * params[0] 商家编码
	 * params[1] 查询时间
	 * params[2] 查询时间
	 * params[3] 页码
	 * params[4] 是令牌认证的编码
	 */
	@Override
	protected Integer doInBackground(String... params) {
		
		LinkedHashMap<String, Object> reqparams = new LinkedHashMap<String,Object>();
		mPage = Integer.parseInt(params[3]);
		reqparams.put("shopCode", params[0]);
		reqparams.put("couponType", params[1]);
		reqparams.put("time", params[2]);
		reqparams.put("page", Integer.parseInt(params[3]));
		reqparams.put("tokenCode", params[4]);
		
		try {
			//调用API
			mResult = (JSONObject) API.reqShop("getShopCouponList", reqparams);
			int retCode = ERROR_RET_CODE;
			//判断查询的一个对象不为空为空 就返回一个正确的编码
			JSONArray arResult = (JSONArray) mResult.get("couponList");
			//判断查询的一个对象不为空为空 就返回一个正确的编码
			if (mResult == null || !"[]".equals(arResult.toString()) || arResult.size() != 0) {
				retCode = RIGHT_RET_CODE; //1 代表访问成功
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
			if (retCode == ERROR_RET_CODE ) {
				// 返回一个空方便做为空判断
				mCallback.getResult(null);
				Util.addToast(R.string.toast_nulldata);
			}
		}
	}

}
