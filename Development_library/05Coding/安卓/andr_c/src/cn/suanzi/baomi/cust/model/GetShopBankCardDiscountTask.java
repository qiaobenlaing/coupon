package cn.suanzi.baomi.cust.model;

import java.util.LinkedHashMap;

import net.minidev.json.JSONObject;
import android.app.Activity;
import cn.suanzi.baomi.base.SzException;
import cn.suanzi.baomi.base.api.API;
import cn.suanzi.baomi.base.data.DB;
import cn.suanzi.baomi.base.model.SzAsyncTask;
import cn.suanzi.baomi.base.pojo.UserToken;

/**
 * 获取商店银行卡折扣的异步任务
 * 
 * @author yingchen
 * 
 */
public class GetShopBankCardDiscountTask extends SzAsyncTask<String, Integer, Integer> {
	private Callback mCallback;

	private JSONObject mResult;

	public GetShopBankCardDiscountTask(Activity acti) {
		super(acti);
	}

	public GetShopBankCardDiscountTask(Activity acti, Callback mCallback) {
		super(acti);
		this.mCallback = mCallback;
	}

	public interface Callback {
		public void getResult(JSONObject result);
	}

	@Override
	protected void onPreExecute() {
		if (mActivity != null && mProcessDialog != null) {
			mProcessDialog.dismiss();
		}
	}

	@Override
	protected Integer doInBackground(String... params) {
		UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		String tokenCode = userToken.getTokenCode();

		LinkedHashMap<String, Object> reqparams = new LinkedHashMap<String, Object>();
		reqparams.put("shopCode", params[0]);
		reqparams.put("tokenCode", tokenCode);

		try {
			mResult = (JSONObject) API.reqCust("getShopBankCardDiscount", reqparams);
			if (mResult != null) {
				mCallback.getResult(mResult);
			}
		} catch (SzException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void handldBuziRet(int retCode) {

	}

}
