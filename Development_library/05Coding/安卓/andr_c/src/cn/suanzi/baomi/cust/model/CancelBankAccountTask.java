package cn.suanzi.baomi.cust.model;

import java.util.LinkedHashMap;

import net.minidev.json.JSONObject;
import android.app.Activity;
import android.content.Intent;
import cn.suanzi.baomi.base.ErrorCode;
import cn.suanzi.baomi.base.SzException;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.api.API;
import cn.suanzi.baomi.base.model.SzAsyncTask;
import cn.suanzi.baomi.cust.R;
import cn.suanzi.baomi.cust.activity.MyHomeBankListActivity;

/**
 * 取消添加银行卡
 * @author wensi.yu
 *
 */
public class CancelBankAccountTask extends SzAsyncTask<String, String, Integer> {
	
	private final static String TAG = CancelBankAccountTask.class.getSimpleName();
	
	/** 创建一个JSONObject对象**/
	private JSONObject mResult;
	/**
	 * 构造函数
	 * @param acti
	 */
	public CancelBankAccountTask(Activity acti) {
		super(acti);
	}


	/**
	 * 取消添加银行卡
	 */
	@Override
	protected Integer doInBackground(String... params) {
		LinkedHashMap<String, Object> reqParams = new LinkedHashMap<String, Object>();
		reqParams.put("orderNbr", params[0]);
		reqParams.put("tokenCode", params[1]);
		
		try {
			mResult = (JSONObject) API.reqShop("cancelBankAccount", reqParams);
			int retCode = Integer.parseInt(mResult.get("code").toString());
			return Integer.parseInt(String.valueOf(retCode));
		} catch (SzException e) {
			this.mErrCode = e.getErrCode();
			return this.mErrCode.getCode();
		}
	}

	/**
	 * 业务逻辑处理
	 */
	@Override
	protected void handldBuziRet(int retCode) {
		if(retCode == ErrorCode.SUCC){
			Util.getContentValidate(R.string.cancele_success);
			Intent intent = new Intent(mActivity, MyHomeBankListActivity.class);
			this.mActivity.startActivity(intent);
			this.mActivity.finish();
		}else{
			if(retCode == ErrorCode.FAIL){
				Util.getContentValidate(R.string.cancel_fail);
			}
		}
	}
}
