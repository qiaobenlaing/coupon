package cn.suanzi.baomi.cust.model;

import java.util.LinkedHashMap;

import net.minidev.json.JSONObject;
import android.app.Activity;
import android.widget.Toast;
import cn.suanzi.baomi.base.ErrorCode;
import cn.suanzi.baomi.base.SzException;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.api.API;
import cn.suanzi.baomi.base.data.DB;
import cn.suanzi.baomi.base.model.SzAsyncTask;
import cn.suanzi.baomi.base.pojo.UserToken;
import cn.suanzi.baomi.cust.R;

/**
 * 9.42	生成订单
 * @author ad
 *
 */
public class AddOrderTask extends SzAsyncTask<String, Integer, Integer> {
	private final static String TAG = AddOrderTask.class.getSimpleName();
	/** 50314-商家编码错误*/
	private final static int ERROR_SHOP_CODE = 50314;
	/** 50317-请输入商家编码；*/
	private final static int INPUT_SHOP_CODE = 50317;
	/** 50400-请输入消费金额*/
	private final static int INPUT_CSM_MONEY = 50400;
	/** 50401-消费金额不正确*/
	private final static int ERROR_CSM_MONEY = 50401;
	/** 定义一个正确的返回结果码 **/
	private final static int RIGHT_RET_CODE = 1;
	/** 定义一个错误的返回结果码 **/
	private final static int ERROR_RET_CODE = 0;
	/** 获得一个用户信息对象 **/
	private UserToken mUserToken;
	/** 用户登录后获得的令牌 **/
	private String mTokenCode;
	/** 回调方法 **/
	private Callback mCallback;
	/** 调用API返回对象 **/
	private JSONObject mResult;

	public AddOrderTask(Activity acti, Callback callback) {
		super(acti);
		this.mCallback = callback;
	}

	/**
	 * 回调方法的接口
	 * 
	 */
	public interface Callback {
		// 传递参数
		// 是异步请求的结果
		public void getResult(String result);
	}

	@Override
	protected void handldBuziRet(int retCode) {
		// RIGHT_RET_CODE的值是1 如果retCode访问成功的发 返回的是1
		if (retCode == RIGHT_RET_CODE) {
			int code = Integer.parseInt(mResult.get("code").toString());
			if (code == ErrorCode.SUCC) {
				mCallback.getResult(mResult.get("orderCode").toString());
			} else if (code == ErrorCode.API_INTERNAL_ERR) { // 支付失败
				mCallback.getResult(null);
				Util.getContentValidate(R.string.pay_fail);
			} else if (code == ERROR_SHOP_CODE) { // 商家编码有误
				mCallback.getResult(null);
				Util.getContentValidate(R.string.error_shop_code);
			} else if (code == INPUT_SHOP_CODE) { // 请输入商家编码
				mCallback.getResult(null);
				Util.getContentValidate(R.string.input_shop_code);
			} else if (code == INPUT_CSM_MONEY) { // 请输入消费金额
				mCallback.getResult(null);
				Util.getContentValidate(R.string.input_csm_money);
			} else if (code == ERROR_CSM_MONEY) { // 消费金额有误
				mCallback.getResult(null);
				Util.getContentValidate(R.string.error_csm_money);
			} 
			
		} else {
			mCallback.getResult(null);
			Toast.makeText(this.mActivity, "服务器异常" + ErrorCode.getMsg(retCode), Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	protected Integer doInBackground(String... params) {
		mUserToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		mTokenCode = mUserToken.getTokenCode();// 用户登录后获得的令牌
		String userCode =  mUserToken.getUserCode();// 用户登录后获得的令牌
		LinkedHashMap<String, Object> reqparams = new LinkedHashMap<String, Object>();
		reqparams.put("userCode", userCode);
		reqparams.put("price", params[0]);
		reqparams.put("shopCode", params[1]);
		reqparams.put("tokenCode", mTokenCode);
		try {
			// 调用API
			mResult = (JSONObject) API.reqCust("addOrder", reqparams);
			int retCode = ERROR_RET_CODE;
			// 判断查询的一个对象不为空为空 就返回一个正确的编码
			if (!(mResult == null || "".equals(mResult.toJSONString()))) {
				retCode = RIGHT_RET_CODE; // 1 代表访问成功
			}
			return retCode;
		} catch (SzException e) {
			this.mErrCode = e.getErrCode();
			return this.mErrCode.getCode();// 返回错误编码
		}
	}
}
