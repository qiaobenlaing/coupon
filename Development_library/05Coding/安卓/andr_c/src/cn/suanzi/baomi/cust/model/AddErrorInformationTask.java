package cn.suanzi.baomi.cust.model;

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
 * 用户给平台提供商家错误信息
 * 
 * @author ad
 * 
 */
public class AddErrorInformationTask extends SzAsyncTask<String, String, Integer> {

	private final static String TAG = "AddErrorInformationTask";

	/** 创建一个JSONObject对象 **/
	private JSONObject mResult;
	private Callback mCallback;

	public AddErrorInformationTask(Activity acti) {
		super(acti);

	}

	/**
	 * 有参构造方法
	 * 
	 * @param acti
	 *            调用者的上下文
	 * @param callback
	 *            回调方法
	 */
	public AddErrorInformationTask(Activity acti, Callback callback) {
		super(acti);
		mCallback = callback;
	}

	@Override
	protected void onPreExecute() {
		if (mActivity != null && mProcessDialog != null) {
			mProcessDialog.dismiss();
		}
	}

	/**
	 * 回调方法的接口
	 */
	public interface Callback {
		public void getResult(JSONObject mResult);
	}

	@Override
	protected Integer doInBackground(String... params) {
		UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		String userCode = userToken.getUserCode();
		String tokenCode = userToken.getTokenCode();
		LinkedHashMap<String, Object> reqParams = new LinkedHashMap<String, Object>();
		reqParams.put("userCode", userCode);// 用户编码
		reqParams.put("errorInfo", params[0]);// 错误信息
		reqParams.put("errorImg", params[1]);// 错误图片url
		reqParams.put("toShopCode", params[2]);// 被反馈的商户编码
		reqParams.put("message", params[3]);// 输入的错误信息
		reqParams.put("tokenCode", tokenCode);

		try {
			mResult = (JSONObject) API.reqCust("addErrorInformation", reqParams);
			// 如果成功，保存到数据库
			return Integer.parseInt(String.valueOf(mResult.get("code")));
		} catch (SzException e) {
			this.mErrCode = e.getErrCode();
			return this.mErrCode.getCode();
		}
	}

	@Override
	protected void handldBuziRet(int retCode) {
		if (retCode == ErrorCode.SUCC) {
			mCallback.getResult(mResult);
		} else {
			mCallback.getResult(null);
		}
	}
}
