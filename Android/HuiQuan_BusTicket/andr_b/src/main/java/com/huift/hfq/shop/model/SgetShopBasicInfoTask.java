package com.huift.hfq.shop.model;

import java.util.LinkedHashMap;

import com.huift.hfq.base.SzException;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.api.API;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.model.SzAsyncTask;
import com.huift.hfq.base.pojo.UserToken;

import net.minidev.json.JSONObject;
import android.app.Activity;
import android.util.Log;
import com.huift.hfq.shop.R;

/**
 * 根据商店编码查询所有信息
 * @author qian.zhou
 */
public class SgetShopBasicInfoTask extends SzAsyncTask<String , String , Integer> {
	private final static String TAG = "SgetShopBasicInfoTask" ;
	/**创建一个JSONObject对象**/
	private JSONObject mResult ;
	/**定义一个正确的返回结果码**/
	private final static int RIGHT_RET_CODE = 1 ;
	/**定义一个错误的返回结果码**/ 
	private final static int ERROR_RET_CODE = 0;
	/**回调方法**/
	private Callback callback ;

	/**
	 * 构造函数
	 * @param 启动本对象的Activity
	 */
	public SgetShopBasicInfoTask(Activity acti) {
		super(acti) ;
	}
	
	/**
	 *@param acti 上下文 
	 *@param callback 回调方法 
	 */
	public SgetShopBasicInfoTask(Activity acti ,Callback callback) {
		super(acti) ;
		this.callback = callback ;
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (mProcessDialog != null) {
			mProcessDialog.dismiss();
		}
	}
	
	/**
	 * 调用API 根据商店编码查询所有信息
	 */
	protected Integer doInBackground(String... params) {
			UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
			LinkedHashMap<String, Object> reqParams = new LinkedHashMap<String, Object>();
			reqParams.put("shopCode" , userToken.getShopCode()) ;
			reqParams.put("tokenCode" , userToken.getTokenCode()) ;
			try {
				int retCode = ERROR_RET_CODE;
				mResult = (JSONObject)API.reqShop("sGetShopBasicInfo", reqParams);
				//判断查询的一个对象不为空为空 就返回一个正确的编码
				if ( mResult != null) {
					retCode = RIGHT_RET_CODE; //1 代表访问成功
				}
				Log.d(TAG, "retCode===="+retCode);
				return retCode;
			}catch (SzException e) {
			this.mErrCode = e.getErrCode() ;
			return this.mErrCode.getCode() ;
		}
	}

	/**  
     * 回调方法的接口  
     */  
    public interface Callback{  
        public void getResult(JSONObject array) ;  
    }
	
	/**
	 * onPostExecute()中的正常业务逻辑处理.
	 */
	@Override
	protected void handldBuziRet(int retCode) {
		/*if (dialogUtil != null) {
			dialogUtil.dismiss();
		}*/
		if (retCode == RIGHT_RET_CODE) {
			callback.getResult(mResult) ;
		}else if(retCode==ERROR_RET_CODE){
			Util.getContentValidate(R.string.code_fail);
			callback.getResult(null) ;
		}
	}
}
