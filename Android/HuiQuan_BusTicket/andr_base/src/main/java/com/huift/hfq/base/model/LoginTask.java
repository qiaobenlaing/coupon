package com.huift.hfq.base.model;

import java.util.LinkedHashMap;
import net.minidev.json.JSONObject;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.huift.hfq.base.Const;
import com.huift.hfq.base.ErrorCode;
import com.huift.hfq.base.R;
import com.huift.hfq.base.SzApplication;
import com.huift.hfq.base.SzException;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.api.API;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.model.SzAsyncTask;
import com.huift.hfq.base.model.UserTokenModel;
import com.huift.hfq.base.pojo.User;
import com.huift.hfq.base.pojo.UserToken;

/**
 * 登录API调用任务类
 *
 * @author Weiping
 */
public class LoginTask extends SzAsyncTask<String, Integer, Integer> {

	private final static String TAG = "LoginTask";

	public final static String ALL_LOGIN = "login";
	/** 跳转的对象 */
	private Class mSkipObj;
	/** 登陆前的界面 */
	private String mLoginFlag;
	/** 回调方法 **/
	private Callback mCallback;
	/** 调用api的结果 */
	private JSONObject mResult;

	/**
	 * 回调方法的接口
	 *
	 */
	public interface Callback {
		public void getResult(int result);
	}

	/**
	 * 无参数构造函数
	 *
	 * @param acti
	 *            启动本对象的Activity。
	 */
	public LoginTask(Activity acti) {
		super(acti);
	}

	/**
	 * 只有回调
	 *
	 * @param acti
	 */
	public LoginTask(Activity acti, Callback callback) {
		super(acti);
		this.mCallback = callback;
	}

	/**
	 * 回调方法的构造函数
	 *
	 * @param acti
	 */
	public LoginTask(Activity acti, Callback callback, Class obj, String loginFlag) {
		super(acti);
		this.mCallback = callback;
		this.mSkipObj = obj;
		this.mLoginFlag = loginFlag;
	}

	@Override
	protected void onPreExecute() {
		if (null != mActivity) {
			if (null != mProcessDialog) {
				mProcessDialog.dismiss();
			}
		}
	}

	/**
	 * 提交数据到服务器，进行登录验证。
	 *
	 * @param params
	 *            [0]手机号；[1]MD5(密码)。
	 */
	@Override
	protected Integer doInBackground(String... params) {
		try {
			LinkedHashMap<String, Object> reqParams = new LinkedHashMap<String, Object>();
			reqParams.put("mobileNbr", params[0]);
			reqParams.put("password", params[1]);
			reqParams.put("loginType", SzApplication.getInstance().getCurrAppType());
			reqParams.put("registrationId", params[2]);
			mResult = (JSONObject) API.reqComm("login", reqParams);
			Log.d(TAG, "请求的结果==" + mResult);
			int retCode = Integer.parseInt(String.valueOf(mResult.get("code")));
			if (retCode == ErrorCode.SUCC) {
				mResult.remove("code");
				UserToken userToken = Util.json2Obj(mResult.toJSONString(), UserToken.class);
				userToken.setMobileNbr(params[0]);
				userToken.setPassword(params[1]); // 重新覆盖一下md5(密码)
				userToken.setExpiresAt(Long.parseLong(mResult.get("expiresAt").toString()));// 有效期至
				DB.saveObj(DB.Key.CUST_USER_TOKEN, userToken);
				// 缓存
				UserToken mUserToken = new UserToken();
				mUserToken.setId("1001");
				mUserToken.setUserCode(userToken.getUserCode());
				UserTokenModel.saveToken(mUserToken);
				// user对象
				User User = Util.json2Obj(mResult.toString(), User.class);
				DB.saveObj(DB.Key.CUST_USER, User);
			}
			return retCode;
		} catch (SzException e) {
			this.mErrCode = e.getErrCode();
			if (this.mErrCode != null) { return this.mErrCode.getCode(); }
			return 0;
		}
	}

	/**
	 * onPostExecute()中的正常业务逻辑处理.
	 */
	@Override
	protected void handldBuziRet(int retCode) {
		if (retCode == ErrorCode.SUCC && mCallback != null) {
			mCallback.getResult(ErrorCode.SUCC);
			DB.saveBoolean(DB.Key.CUST_LOGIN, true);// 是否登录
			if (null != mSkipObj) {
				Intent it = new Intent(this.mActivity, mSkipObj);
				if (!Util.isEmpty(mLoginFlag)) {
					if (mLoginFlag.equals(Const.Login.SHOP_DETAIL) || mLoginFlag.equals("newShopInfo") || mLoginFlag.equals(Const.Login.H5_ACT) || mLoginFlag.equals(Const.Login.ACT_THEME)) { // 跳转到商家详情
						this.mActivity.finish();
					} else if (mLoginFlag.equals(Const.Login.EXIT_LOGIN)) { // 退出登录后在登录
						this.mActivity.startActivity(it);
						this.mActivity.finish();
					} else if (mLoginFlag.equals(Const.Login.ANDR_B_HOMEFRAGMENT)) {
						String shopCode = mResult.get("shopCode").toString();
						if (Util.isEmpty(shopCode)) {
							// 您没有权限进入惠圈管家
							Util.getContentValidate(R.string.no_huiquan_competence);
							mCallback.getResult(ErrorCode.FAIL);
						} else {
							this.mActivity.startActivity(it);
							this.mActivity.finish();
						}
					} else {
						it.putExtra(ALL_LOGIN, mLoginFlag);
						this.mActivity.startActivity(it);
						this.mActivity.finish();
						if (null != Util.homeActivityList || Util.homeActivityList.size() > 0) {
							Util.exitHome();
						}
					}
				} else {
					this.mActivity.startActivity(it);
					this.mActivity.finish();
				}
			}

		} else {
			DB.saveBoolean(DB.Key.CUST_LOGIN, false);
			if (null != mCallback) {
				mCallback.getResult(ErrorCode.FAIL);
			}
			switch (retCode) {
				case ErrorCode.USER_NOT_EXISTED:
					Util.getContentValidate(R.string.login_name_noexist);
					break;
				case ErrorCode.USER_PASS_ACTIVATE:
					Util.getContentValidate(R.string.login_custinternet_activate);
					break;
				case ErrorCode.USER_DISABLED:
					Util.getContentValidate(R.string.login_name_nothing);
					break;
				case ErrorCode.USER_PASS_NOTHING:
					Util.getContentValidate(R.string.login_pass_nothing);
					break;
				case ErrorCode.USER_PASS_INVALID:
					Util.getContentValidate(R.string.login_password_error);
					break;
				case ErrorCode.NETWORK_PROBLEM:
					Util.getContentValidate(R.string.login_internet_error);
					break;
				case ErrorCode.CLI_NET_NOT_CONN:
					Util.getContentValidate(R.string.login_custinternet_error);
					break;
				default:
					Util.showToastZH("登录失败，错误码："+retCode);
					break;
			}
		}
	}
}
