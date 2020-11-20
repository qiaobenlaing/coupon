package com.huift.hfq.shop.model;

import android.app.Activity;
import android.widget.Toast;

import com.huift.hfq.base.ErrorCode;
import com.huift.hfq.base.SzException;
import com.huift.hfq.base.api.API;
import com.huift.hfq.base.model.SzAsyncTask;

import net.minidev.json.JSONObject;

import java.util.LinkedHashMap;

public class MyShopPicAddTask extends SzAsyncTask<String, String, Integer> {
	/** 创建一个JSONObject对象**/
	private JSONObject mResult;

	/**
	 * 构造函数
	 * @param acti
	 */
	public MyShopPicAddTask(Activity acti) {
		super(acti);
	}

	/**
	 * 回调方法
	 */
	private Callback callback ;

	/***
	 * @param acti 上下文
	 * @param callback 回调方法
	 */
	public MyShopPicAddTask(Activity acti , Callback callback) {
		super(acti);
		this.callback = callback ;
	}

	/**
	 * 回调方法的接口
	 */
	public interface Callback{
		public void getResult(JSONObject object) ;
	}

	/**
	 * 基本的业务逻辑操作
	 */
	@Override
	protected void handldBuziRet(int retCode) {
		if (retCode == ErrorCode.SUCC) {
			callback.getResult(mResult) ;
		}
		else{
			Toast.makeText(this.mActivity, ErrorCode.getMsg(retCode),Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 调用API
	 */
	@Override
	protected Integer doInBackground(String... params) {
		LinkedHashMap<String, Object> reqParams = new LinkedHashMap<String , Object>() ;
		reqParams.put("shopCode" , params[0]) ;
		reqParams.put("imgUrl" , params[1]) ;
		reqParams.put("tokenCode" , params[2]) ;
		try {
			mResult = (JSONObject) API.reqShop("updateShopDecImg" , reqParams) ;
			// 如果成功，保存到数据库
			return Integer.parseInt(String.valueOf(mResult.get("code"))) ;
		} catch (SzException e) {
			this.mErrCode = e.getErrCode() ;
			return this.mErrCode.getCode() ;
		}
	}

}
