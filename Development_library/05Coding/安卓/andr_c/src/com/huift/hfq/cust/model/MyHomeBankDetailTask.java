package com.huift.hfq.cust.model;

import java.util.LinkedHashMap;

import net.minidev.json.JSONObject;
import android.app.Activity;
import android.widget.Toast;
import com.huift.hfq.base.SzException;
import com.huift.hfq.base.api.API;
import com.huift.hfq.base.model.SzAsyncTask;

/**
 * 银行卡的详情
 * 
 * @author wensi.yu
 * 
 */
public class MyHomeBankDetailTask extends SzAsyncTask<String, String, Integer> {

	private final static String TAG = MyHomeBankDetailTask.class.getSimpleName();

	/** 创建一个JSONObject对象 **/
	private JSONObject mResult;
	/** 定义一个正确的返回结果码 **/
	private final static int RIGHT_RET_CODE = 1;
	/** 定义一个错误的返回结果码 **/
	private final static int ERROR_RET_CODE = 0;
	/** 回调方法 **/
	private Callback mCallback;

	/**
	 * 构造方法
	 * 
	 * @param acti
	 */
	public MyHomeBankDetailTask(Activity acti) {
		super(acti);
	}

	/**
	 * 回调方法的构造函数
	 * 
	 * @param acti
	 */
	public MyHomeBankDetailTask(Activity acti, Callback callback) {
		super(acti);
		this.mCallback = callback;
	}

	/**
	 * 回调方法的接口
	 * 
	 */
	public interface Callback {
		public void getResult(JSONObject object);
	}

	/**
	 * 银行卡详情
	 */
	@Override
	protected Integer doInBackground(String... params) {
		LinkedHashMap<String, Object> reqParams = new LinkedHashMap<String, Object>();
		reqParams.put("bankAccountCode", params[0]);
		reqParams.put("userCode", params[1]);
		reqParams.put("tokenCode", params[2]);
		try {
			// 调用API
			mResult = (JSONObject) API.reqCust("getBankAccountInfo", reqParams);
			int retCode = ERROR_RET_CODE;
			// 判断结果是否为空
			if (mResult != null || !"".equals(mResult.toJSONString())) {
				retCode = RIGHT_RET_CODE;
			}
			return retCode;
		} catch (SzException e) {
			this.mErrCode = e.getErrCode();
			return this.mErrCode.getCode();// 返回错误编码
		} catch (Exception e) {
			return 0;// 返回错误编码
		}
	}

	/**
	 * 业务逻辑处理
	 */
	@Override
	protected void handldBuziRet(int retCode) {
		if (retCode == RIGHT_RET_CODE) {
			mCallback.getResult(mResult);
		} else {
			if (retCode == ERROR_RET_CODE) {
				Toast.makeText(this.mActivity, "没有银行卡详情信息", Toast.LENGTH_SHORT).show();
			}
		}

	}

}
