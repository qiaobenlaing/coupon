package cn.suanzi.baomi.cust.model;

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
 * 活动退款
 * @author yanfang.li
 */
public class ActCodeApplyRefundTask extends SzAsyncTask<String, String, Integer> {

	private final static String TAG = ActCodeApplyRefundTask.class.getSimpleName();
	/** 52200-不能够退款；*/
	private final static int NO_REFUND = 52200;
	/** 购买当日不能退款*/
	private final static int DAY_REFUND = 52201;
	/** 当日订单不能部分退款*/
	private final static int DAY_SECTION_REFUND = 55000;
	/** 调用API返回对象*/
	private JSONObject mResult;
	/** 回调方法 **/
	private Callback mCallback;
	
	/**
	 * 回调方法的接口
	 */
	public interface Callback {
		public void getResult(int retCode) ;
	}
	
	/**
	 * 构造函数
	 * @param acti
	 */
	public ActCodeApplyRefundTask(Activity acti) {
		super(acti);
	}
	
	/**
	 * @param acti 上下文
	 * @param mCallback 回调方法
	 */
	public ActCodeApplyRefundTask(Activity acti, Callback mCallback) {
		super(acti);
		this.mCallback = mCallback;
	}

	/**
	 * 业务逻辑操作
	 */
	@Override
	protected void handldBuziRet(int retCode) {
		if (retCode == ErrorCode.RIGHT_RET_CODE) {
			int code = Integer.parseInt(mResult.get("code").toString());
			
			switch (code) {
			case NO_REFUND: // 不能够退款
				Util.showToastZH("不能够退款");
				mCallback.getResult(NO_REFUND) ;
				break;
			case ErrorCode.SUCC: // 成功
				Util.showToastZH("退款成功");
				mCallback.getResult(ErrorCode.SUCC) ;
				break;
			case ErrorCode.API_INTERNAL_ERR: // 失败
				Util.showToastZH("失败");
				mCallback.getResult(ErrorCode.API_INTERNAL_ERR) ;
				break;
			case DAY_REFUND: // 失败
				Util.showToastZH("购买当日不能退款");
				mCallback.getResult(DAY_REFUND) ;
			case DAY_SECTION_REFUND: // 失败
				Util.showToastZH("当日订单不能部分退款");
				mCallback.getResult(DAY_SECTION_REFUND) ;
				break;

			default:
				break;
			}
		}else {
			Util.showToastZH("失败");
			mCallback.getResult(ErrorCode.FAIL);
		}
	}

	/**
	 * 调用Api
	 */
	@Override
	protected Integer doInBackground(String... params) {
		UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		String tokenCode = userToken.getTokenCode();// 用户登录后获得的令牌
		// 请求参数
		LinkedHashMap<String, Object> reqParams = new LinkedHashMap<String, Object>() ;
		reqParams.put("orderCode" , params[0]) ; // 订单编码
		reqParams.put("actCode" , params[1]) ; // 活动编码
		reqParams.put("bankcardRefund" , params[2]) ; // 工行金额
		reqParams.put("platBonusRefund" , params[3]) ; // 平台红包
		reqParams.put("shopBonusRefund" , params[4]) ; // 商家红包
		reqParams.put("refundReason" , params[5]) ; // 退款原因
		reqParams.put("refundRemark" , params[6]) ; // 退款备注
		reqParams.put("tokenCode", tokenCode) ;
		try {
			int retCode = ErrorCode.ERROR_RET_CODE;
			mResult = (JSONObject)API.reqCust("actCodeApplyRefund", reqParams);
			//判断查询的一个对象不为空为空 就返回一个正确的编码
			if ( mResult != null || !"".equals(mResult.toJSONString())) {
				retCode = ErrorCode.RIGHT_RET_CODE; //1 代表访问成功
			}
			return retCode;
		} catch (SzException e) {
			this.mErrCode = e.getErrCode() ;
			return this.mErrCode.getCode() ;
		}
	}
}
