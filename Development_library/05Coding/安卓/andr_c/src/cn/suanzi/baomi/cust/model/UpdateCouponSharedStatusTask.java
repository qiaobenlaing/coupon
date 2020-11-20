//---------------------------------------------------------
//@author    yanfang.li
//@version   1.0.0
//@createTime 2015.6.2
//@copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
//---------------------------------------------------------
package cn.suanzi.baomi.cust.model;

import java.util.LinkedHashMap;

import net.minidev.json.JSONObject;
import android.app.Activity;
import cn.suanzi.baomi.base.ErrorCode;
import cn.suanzi.baomi.base.SzException;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.api.API;
import cn.suanzi.baomi.base.model.SzAsyncTask;
import cn.suanzi.baomi.cust.R;

/**
 * 修改优惠券分享状态
 * @author yanfang.li
 */
public class UpdateCouponSharedStatusTask extends SzAsyncTask<String,String,Integer> {
	

	private final static String TAG = UpdateCouponSharedStatusTask.class.getSimpleName();
	/** 设置失败 **/
	private final static int FAIL_CODE = 20000;
	/** 优惠券被领走了 **/
	private final static int COUPON_GO = 80219;
	/** 优惠券过期了 **/
	private final static int COUPON_OVER = 80220;
	
	/**
	 * 构造函数
	 * @param acti
	 */
	public UpdateCouponSharedStatusTask(Activity activity) {
		super(activity);
	}
	
	/**
	 * 调用API
	 * @param params[0] 优惠券编码
	 * @param params[1] 用户编码
	 * @param params[2] 分享等级
	 * @param params[3] 需要令牌认证
	 */
	@Override
	protected Integer doInBackground(String... params) {
		LinkedHashMap<String, Object> reqParams = new LinkedHashMap<String, Object>() ;
		reqParams.put("userCouponCode" , params[0]) ;
		reqParams.put("userCode" , params[1]) ;
		reqParams.put("sharedLvl" , Integer.parseInt(params[2])) ;
		reqParams.put("tokenCode" , params[3]) ;
		try {
				JSONObject mResult = (JSONObject)API.reqCust("updateCouponSharedStatus", reqParams);
				
				return Integer.parseInt(mResult.get("code").toString());
		} catch (SzException e) {
			this.mErrCode = e.getErrCode() ;
			return this.mErrCode.getCode() ;
		}
	}
	
	/**
	 * 业务逻辑代码
	 */
	@Override
	protected void handldBuziRet(int retCode) {
		switch (retCode) {
		case ErrorCode.SUCC:
			Util.getContentValidate(R.string.set_succ);
			break;
		case FAIL_CODE:
			Util.getContentValidate(R.string.set_fail);
			break;
		case COUPON_GO:
			Util.getContentValidate(R.string.coupon_grab_go);
			break;
		case COUPON_OVER:
			Util.getContentValidate(R.string.coupon_expired);
			break;

		default:
			break;
		}
	}
}
