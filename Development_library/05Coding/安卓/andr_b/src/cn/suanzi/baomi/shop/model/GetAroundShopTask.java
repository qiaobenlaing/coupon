// ---------------------------------------------------------
// @author    yanfang.li
// @version   1.0.0
// @copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
// ---------------------------------------------------------
package cn.suanzi.baomi.shop.model;

import java.util.LinkedHashMap;

import net.minidev.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.widget.Toast;
import cn.suanzi.baomi.base.ErrorCode;
import cn.suanzi.baomi.base.SzException;
import cn.suanzi.baomi.base.api.API;
import cn.suanzi.baomi.base.model.SzAsyncTask;

/**
 * 获取周边商家的信息，API调用方法
 * @author yanfang.li
 */
@SuppressLint("NewApi")
public class GetAroundShopTask extends SzAsyncTask<String, Integer, Integer> {

	private final static String TAG = "GetAroundShopTask";
	/** 定义一个正确的返回结果码 **/ 
	private final static int RIGHT_RET_CODE = 1;
	/** 定义一个错误的返回结果码 **/ 
	private final static int ERROR_RET_CODE = 0;
	
	/** 创建一个jSONObject对象 **/
	private JSONObject mResult;
	/** “正在处理”进度条 */
	protected ProgressDialog mProcessDialog = null;

	/** 回调方法 **/
	private Callback callback;  

	/**
	 * 构造方法
	 * 
	 * @param acti
	 */
	public GetAroundShopTask(Activity acti) {
		super(acti);
	}
	
	/**
	 * 惨构造方法
	 * @param acti 上下文
	 * @param callback 回调方法
	 */
	public GetAroundShopTask(Activity acti, Callback callback) {
		super(acti);
		this.callback = callback;
	}
	
/*	@Override
	protected void onPreExecute() {
		if(this.mActivity != null) {
		boolean firstFlag = DB.getBoolean(ShopConst.Key.IS＿FIRST_RUN);
		Log.d(TAG, "firstFlag=3"+firstFlag);
			if (firstFlag) {
				if (mProcessDialog != null) {
					mProcessDialog.dismiss();
				}
			} else {
				DB.saveBoolean(ShopConst.Key.IS＿FIRST_RUN,true);
			}
		}
	}*/
	
	/**
	 * 访问服务器的方法，调用API的入口 params[0] shopCode 是商家编码
	 */
	@Override
	protected Integer doInBackground(String... params) {
		try {
			LinkedHashMap<String, Object> reqParams = new LinkedHashMap<String, Object>();
			reqParams.put("shopCode", params[0]);
			reqParams.put("tokenCode", params[1]);
			//调用API
			mResult = (JSONObject) API.reqShop("getAroundShopInfo", reqParams);
			
			int retCode = ERROR_RET_CODE;// 定义doInBackground返回结果码
			// 查询所有商家信息不为空的话 就是查询成功
			if ( !(mResult == null || "".equals(mResult.toJSONString())) ) {

				retCode = RIGHT_RET_CODE;// 1 就代表查询有数据，请求api成功
			}
			return retCode;
		} catch (SzException e) {
			this.mErrCode = e.getErrCode();
			return this.mErrCode.getCode();
		}
	}
	  /**  
     * 回调方法的接口  
     *  
     */  
    public interface Callback{  
    	/**
		 * 传递参数
		 * @param result 是异步请求的结果
		 */
        public void getResult(JSONObject result);  
    }
	/**
	 * 处理正常的业务逻辑
	 */
	@Override
	protected void handldBuziRet(int retCode) {
		if (retCode == RIGHT_RET_CODE) {
			
			callback.getResult(mResult);//调用自己编写的回调方法
			
		} else {
			callback.getResult(null);//调用自己编写的回调方法
			Toast.makeText(this.mActivity, "服务器异常" + ErrorCode.getMsg(retCode),
					Toast.LENGTH_SHORT).show();
		}

	}

}
