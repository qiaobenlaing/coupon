// ---------------------------------------------------------
// @author    yanfang.li
// @version   1.0.0
// @createTime 2015.5.22 
// @copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
// ---------------------------------------------------------
package cn.suanzi.baomi.shop.model;

import java.util.LinkedHashMap;

import net.minidev.json.JSONObject;
import android.app.Activity;
import android.widget.Toast;
import cn.suanzi.baomi.base.ErrorCode;
import cn.suanzi.baomi.base.SzException;
import cn.suanzi.baomi.base.api.API;
import cn.suanzi.baomi.base.model.SzAsyncTask;

/**
 * 获得优惠券统计信息,调用API
 * @author yanfang.li
 */
public class GetAllCouponTask extends SzAsyncTask<String, Integer, Integer> {

	private final static String TAG = "GetAllCouponTesk";
	/** 定义一个正确的返回结果码 **/ 
	private final static int RIGHT_RET_CODE = 1;
	/** 定义一个错误的返回结果码 **/ 
	private final static int ERROR_RET_CODE = 0;
	/** 回调方法 **/
	private Callback mCallback;
	/** 调用API返回对象 **/
	private JSONObject mResult;
	
	/**
	 * 无参构造方法 
	 * @param acti 调用者的上下文
	 */
	public GetAllCouponTask(Activity acti) {
		super(acti);
	}
	
	/**
	 * 有参构造方法
	 * @param acti 调用者的上下文
	 * @param callback 回调方法
	 */
	public GetAllCouponTask(Activity acti, Callback callback) {
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
	 * params[0] 是统计会员卡信息方法的输入参数 shopCode
	 * params[1] 是令牌认证的编码
	 * 
	 */
	@Override
	protected Integer doInBackground(String... params) {
		
		LinkedHashMap<String, Object> reqparams = new LinkedHashMap<String,Object>();
		reqparams.put("shopCode", params[0]);
		reqparams.put("tokenCode", params[1]);
		
		try {
			//调用API
			int retCode = ERROR_RET_CODE;
			if ( !("[]".equals(API.reqShop("getAllCoupon", reqparams))) ) {
				mResult = (JSONObject) API.reqShop("getAllCoupon", reqparams);
				//判断查询的一个对象不为空为空 就返回一个正确的编码
				if ( mResult != null || mResult.size() != 0 || !"".equals(mResult.toString())) {
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
			if (retCode == ERROR_RET_CODE ) {
				
				Toast.makeText(this.mActivity, "该商店还没有优惠券",
						Toast.LENGTH_SHORT).show();
			}
			Toast.makeText(this.mActivity, "服务器异常" + ErrorCode.getMsg(retCode),
					Toast.LENGTH_SHORT).show();
		}
	}

}
