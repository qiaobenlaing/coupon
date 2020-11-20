package cn.suanzi.baomi.shop.model;

import java.util.LinkedHashMap;

import net.minidev.json.JSONObject;
import android.app.Activity;
import android.util.Log;
import cn.suanzi.baomi.base.ErrorCode;
import cn.suanzi.baomi.base.SzException;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.api.API;
import cn.suanzi.baomi.base.data.DB;
import cn.suanzi.baomi.base.model.SzAsyncTask;
import cn.suanzi.baomi.base.pojo.UserToken;
import cn.suanzi.baomi.shop.R;

/**
 * 扫描框内容
 * @author wensi.yu
 *
 */
public class SweepQrCodeTask extends SzAsyncTask<String, String, Integer>{
	
	private final static String TAG = "SweepQrCodeTask";
	
	/** 扫描失败*/
	private final static int SCAN_CODE_ERROR = 85001;
	/** 二维码不安全*/
	private final static int SCAN_CODE_SAFE = 85002;
	/** 二维码过期*/
	private final static int SCAN_CODE_OVERDUE = 85003;
	/** 用户不存在*/
	private final static int SCAN_CODE_NOEXIS = 85004;
	/** 用户未绑定该银行卡*/
	private final static int SCAN_CODE_NOBLIND = 85005;
	/** 该银行卡解绑或未成功绑定*/
	private final static int SCAN_CODE_BLINDERROR = 85006;
	/** 创建一个JSONObject对象*/
	private JSONObject mResult;
	/** 回调方法**/ 	
	private Callback callback;

	/**
	 * 构造函数
	 * @param acti
	 */
	public SweepQrCodeTask(Activity acti) {
		super(acti);
	}
	
	/**
	 * 回调方法的构造函数
	 * @param acti
	 */
	public SweepQrCodeTask(Activity acti,Callback callback) {
		super(acti);
		this.callback = callback;
	}
	
	/**  
     * 回调方法的接口  
     *  
     */  
    public interface Callback{  
        public void getResult(JSONObject mResult);  
    }

	/**
	 * 业务处理
	 */
	@Override
	protected Integer doInBackground(String... params) {
		UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		String tokenCode = userToken.getTokenCode();
		LinkedHashMap<String, Object> reqParams = new LinkedHashMap<String, Object>();
		reqParams.put("validateString", params[0]);
		reqParams.put("tokenCode", tokenCode);
		
		try {
			mResult = (JSONObject)API.reqShop("sweepQrCode", reqParams);
			Log.i(TAG, "mResult=="+mResult);
			return Integer.parseInt(String.valueOf(mResult.get("code"))) ;
			
		} catch (SzException e) {
			this.mErrCode = e.getErrCode();
			return this.mErrCode.getCode();
		}
	}

	@Override
	protected void handldBuziRet(int retCode) {
		if (retCode == ErrorCode.SUCC) {
			callback.getResult(mResult);
		} else {
			if(retCode == ErrorCode.FAIL){
				callback.getResult(null);
				Util.getContentValidate(R.string.toast_scanactivity_error);
				mActivity.finish();
			} else if (retCode == SCAN_CODE_ERROR) {
				callback.getResult(null);
				Util.getContentValidate(R.string.toast_scan_code_error);
				mActivity.finish();
			} else if (retCode == SCAN_CODE_SAFE) {
				callback.getResult(null);
				Util.getContentValidate(R.string.toast_scan_code_nosafe);
				mActivity.finish();
			} else if (retCode == SCAN_CODE_OVERDUE) {
				callback.getResult(null);
				Util.getContentValidate(R.string.toast_scan_code_overdue);
				mActivity.finish();
			} else if (retCode == SCAN_CODE_NOEXIS) {
				callback.getResult(null);
				Util.getContentValidate(R.string.toast_scan_code_noexis);
				mActivity.finish();
			} else if (retCode == SCAN_CODE_NOBLIND) {
				callback.getResult(null);
				Util.getContentValidate(R.string.toast_scan_code_noblind);
				mActivity.finish();
			} else if (retCode == SCAN_CODE_BLINDERROR) {
				callback.getResult(null);
				Util.getContentValidate(R.string.toast_scan_code_blinderror);
				mActivity.finish();
			}
		}
	}
}
