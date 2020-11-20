package com.huift.hfq.base.model;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import com.huift.hfq.base.Const;
import com.huift.hfq.base.ErrorCode;
import com.huift.hfq.base.R;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.pojo.UserToken;

/**
 * 异步任务类的基类。进行一些通用性处理。
 * 
 * @author Weiping
 */
public abstract class SzAsyncTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {

	/** 当前activity */
	protected Activity mActivity = null;
	/** “正在处理”进度条 */
	protected ProgressDialog mProcessDialog = null;
	/** 异常代码 */
	protected ErrorCode mErrCode = null;

	/**
	 * 构造函数
	 * 
	 * @param acti
	 *            启动本对象的Activity。
	 */
	public SzAsyncTask(Activity acti) {
		super();
		this.mActivity = acti;
	}

	@Override
	protected void onPreExecute() {
		if (this.mActivity != null) {
			mProcessDialog = new ProgressDialog(this.mActivity);
			if (mProcessDialog != null) {
				mProcessDialog.setCancelable(false);
				mProcessDialog.setMessage(mActivity.getString(R.string.msg_task_processing));
				mProcessDialog.show();
			}
		}
	}

	/**
	 * doInBackground()完成后的结果传递到这里处理。具体工作包括dismiss进度条，通用异常处理。
	 * 
	 * @param result
	 *            doInBackground()返回的结果
	 */
	@Override
	protected void onPostExecute(Result result) {
		try {

			if (mActivity != null) {
				if (mProcessDialog != null) {
					mProcessDialog.dismiss();
				}
				if (null != result) {
					if (result instanceof Integer) {
						this.handleRetCode((Integer) result);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * <p>
	 * API公共异常处理.
	 * </p>
	 * 以下异常必须跳转到登录页面：
	 * <ul>
	 * <li>ErrorCode.USER_NOT_EXISTED：用户不存在</li>
	 * <li>ErrorCode.USER_DISABLED：用户已经被禁了</li>
	 * <li>ErrorCode.USER_PASS_INVALID：自动登录时密码错误</li>
	 * <li>ErrorCode.USER_NOT_AUTHORIZED：需要登录才能访问，用户重未登录过</li>
	 * </ul>
	 * 以下异常，弹出toast，不进行任何操作：
	 * <ul>
	 * <li>ErrorCode.CLI_NET_NOT_CONN：客户端未联网</li>
	 * <li>ErrorCode.NETWORK_PROBLEM：请求API时网络异常</li>
	 * <li>ErrorCode.JSONRPC_INVALID_JSON,</li>
	 * <li>ErrorCode.JSONRPC_INVALID_PARAMS,</li>
	 * <li>ErrorCode.JSONRPC_INVALID_REQUEST,</li>
	 * <li>ErrorCode.JSONRPC_METHOD_NOT_FOUND</li>
	 * <li>ErrorCode.API_INTERNAL_ERR：API内部异常</li>
	 * </ul>
	 * 
	 * @param retCode
	 *            返回代码
	 */
	public void handleRetCode(int retCode) {
		// 如果是需要重新登录，清楚token信息，跳转到登录页面.
		switch (retCode) {
		/*
		 * case ErrorCode.USER_NOT_AUTHORIZED: //Toast.makeText(this.mActivity,
		 * "用户无权限访问资源，需要登录", Toast.LENGTH_SHORT).show();
		 * 
		 * API.clearToken(); try { String loginClassName =
		 * SzApplication.getInstance().getCurrAppType() == Const.AppType.SHOP ?
		 * Const.LoginActivity.SHOP : Const.LoginActivity.CUST; //
		 * 如果当前页面本身是LoginActivity，无需跳转 if
		 * (!loginClassName.equals(this.mActivity.getClass().getName())) {
		 * Class<?> loginClz = Class.forName(loginClassName); Intent it = new
		 * Intent(this.mActivity, loginClz); this.mActivity.startActivity(it); }
		 * // TODO 清空所有activity堆栈，只保留登录页面。 } catch (ClassNotFoundException e) {
		 * Log.w(TAG,
		 * "API通用异常处理跳转到LoginAcitivity时异常，请修改Const.LoginActivity的路径。"); } break;
		 */

		// case ErrorCode.CLI_NET_NOT_CONN:
		// Toast.makeText(this.mActivity, "请连接网络", Toast.LENGTH_SHORT).show();
		// break;
		// case ErrorCode.NETWORK_PROBLEM:
		// Toast.makeText(this.mActivity, "网络异常", Toast.LENGTH_SHORT).show();
		// break;
		case ErrorCode.USER_TOKEN_INVALID: // 20205
		case ErrorCode.USER_SESSION_OUT: // 20204
			// 重新登录一下
			String regId = Util.isEmpty(DB.getStr(Const.JPUSH_REGID)) ? "" : DB.getStr(Const.JPUSH_REGID);
			UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
			if (null != userToken && !Util.isEmpty(userToken.getMobileNbr()) && !Util.isEmpty(userToken.getPassword())) {
				String params[] = { userToken.getMobileNbr(), userToken.getPassword(), regId };
				new LoginTask(mActivity).execute(params);
			}
			break;
		case ErrorCode.JSONRPC_INVALID_JSON:
		case ErrorCode.JSONRPC_INVALID_PARAMS:
		case ErrorCode.JSONRPC_INVALID_REQUEST:
		case ErrorCode.JSONRPC_METHOD_NOT_FOUND:
			// case ErrorCode.API_INTERNAL_ERR:
			if (mActivity != null) {
				Toast.makeText(this.mActivity, ErrorCode.getMsg(retCode), Toast.LENGTH_SHORT).show();
			}
		default:
			try {
				handldBuziRet(retCode);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 处理正常业务逻辑
	 * 
	 * @param retCode
	 *            返回代码
	 */
	protected abstract void handldBuziRet(int retCode);

};
