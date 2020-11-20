
// ---------------------------------------------------------
// @author    yanfang.li
// @version   1.0.0
// @createTime 2015.5.22 
// @copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
// ---------------------------------------------------------
package com.huift.hfq.base.model;

import java.util.LinkedHashMap;

import com.huift.hfq.base.Const;
import com.huift.hfq.base.ErrorCode;
import com.huift.hfq.base.SzException;
import com.huift.hfq.base.api.API;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.pojo.UserToken;

import net.minidev.json.JSONObject;
import android.app.Activity;
import android.util.Log;

/**
 * 添加会员卡,调用API
 * @author yanfang.li
 */
public class SetPayResultTask extends SzAsyncTask<String, Integer, Integer> {
	private final static String TAG = SetPayResultTask.class.getSimpleName();
	private JSONObject mResult;
	private OnResultListener onResultListener;

	/**
	 * 无参构造方法
	 * 
	 * @param acti
	 *            调用者的上下文
	 */
	public SetPayResultTask(Activity acti) {
		super(acti);
	}

	/**
	 * 有参构造方法
	 * 
	 * @param acti
	 *            调用者的上下文
	 * @param onResultListener
	 *            回调方法
	 */
	public SetPayResultTask(Activity acti, OnResultListener onResultListener) {
		super(acti);
		this.onResultListener = onResultListener;
	}

	/**
	 * 回调方法的接口
	 */
	public interface OnResultListener {
		public void onSuccess();

		public void onError();
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (mActivity != null) {
			if (mProcessDialog != null ) {
				mProcessDialog.dismiss();
			}
		}
	}

	@Override
	protected Integer doInBackground(String... params) {
		UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		String tokenCode = userToken.getTokenCode();
		LinkedHashMap<String, Object> reqparams = new LinkedHashMap<String, Object>();
		reqparams.put("consumeCode", params[0]);
		reqparams.put("serialNbr", params[1]);
		reqparams.put("result", params[2]);
		reqparams.put("tokenCode", tokenCode);
		Log.d(TAG, "consumeCode="+params[0]+"serialNbr="+params[1]+"result="+ params[2]);
		
		try {
			int retCode = 0;
			int flag = 0;
			// 调用API
			while (true) {
				mResult = (JSONObject) API.reqComm("setPayResult", reqparams);
				Log.d(TAG, "mResult======"+mResult);
				retCode = Integer.parseInt(mResult.get("code").toString());
				if (retCode == ErrorCode.SUCC) {
					break;
				} else {
					flag++;
					if (flag > 5) {
						break;
					}
					continue;
				}
			}
			Log.d(TAG, "返回码：" + retCode);
			return retCode;
		} catch (SzException e) {
			this.mErrCode = e.getErrCode();
			return this.mErrCode.getCode();// 返回错误编码
		}

	}

	/**
	 * 处理查询结果的返回值
	 * 
	 * @param retCode 执行成功返回码
	 */
	@Override
	protected void handldBuziRet(int retCode) {
		// RIGHT_RET_CODE的值是1 如果retCode访问成功的发 返回的是1
		if (retCode == ErrorCode.SUCC) {
			if (null != onResultListener) {
				onResultListener.onSuccess();
			}
			Log.d(TAG, "交易成功：" + retCode);
		} else {
			if (null != onResultListener) {
				onResultListener.onError();
			}
			Log.d(TAG, "交易失败：" + retCode);
		}
	}
}
