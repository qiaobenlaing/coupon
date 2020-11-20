// ---------------------------------------------------------
// @author    yanfang.li
// @version   1.0.0
// @createTime 2015.5.22 
// @copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
// ---------------------------------------------------------
package cn.suanzi.baomi.shop.model;

import java.util.LinkedHashMap;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import android.app.Activity;
import cn.suanzi.baomi.base.SzException;
import cn.suanzi.baomi.base.api.API;
import cn.suanzi.baomi.base.model.SzAsyncTask;

/**
 * 获取商家和某一会员的消息记录
 * @author yanfang.li
 */
public class GetMsgTask extends SzAsyncTask<String, Integer, Integer> {

	private final static String TAG = GetMsgTask.class.getSimpleName();
	private JSONObject mResult;
	private Callback mCallback;
	/** 定义一个正确的返回结果码 **/ 
	private final static int RIGHT_RET_CODE = 1;
	/** 定义一个错误的返回结果码 **/ 
	private final static int ERROR_RET_CODE = 0;
	/**
	 * 无参构造方法 
	 * @param acti 调用者的上下文
	 */
	public GetMsgTask(Activity acti) {
		super(acti);
	}
	
	/**
	 * 有参构造方法
	 * @param acti 调用者的上下文
	 * @param callback 回调方法
	 */
	public GetMsgTask(Activity acti, Callback callback) {
		super(acti);
		mCallback = callback;
	}

	/**  
     * 回调方法的接口  
     *  
     */
	public interface Callback{
		public void getResult(JSONObject result);
	}
	
	/*@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (mActivity != null)
			if (mProcessDialog != null ) {
				mProcessDialog.dismiss();
			}
	}*/
	
	/**
	 * 调用API查询，添加会员卡    
	 * params[0] 会员卡的名称
	 * params[1] 会员卡的等级
	 * 
	 */
	@Override
	protected Integer doInBackground(String... params) {
		
		LinkedHashMap<String, Object> reqparams = new LinkedHashMap<String,Object>();
		reqparams.put("userCode", params[0]); // 接收者编码
		reqparams.put("shopCode",  params[1]); 
		reqparams.put("page", params[2]);  // 内容
		reqparams.put("staffCode", params[3]);  // 商家员工
		reqparams.put("tokenCode",  params[4]); 
		try {
				mResult = (JSONObject) API.reqShop("getMsg", reqparams);
				int retCode = ERROR_RET_CODE;
				JSONArray msgAr = (JSONArray) mResult.get("msgList");
				if (msgAr.size() != 0 || !"[]".equals(msgAr.toString())) {
					retCode = RIGHT_RET_CODE; 
				}
			return retCode;
		} catch (SzException e) {
			
			this.mErrCode = e.getErrCode();
			return this.mErrCode.getCode();//返回错误编码
		}
		 
	}
	
	/**
	 * 处理查询结果的返回值 
	 * @param retCode执行成功返回码
	 */
	@Override
	protected void handldBuziRet(int retCode) {
		//RIGHT_RET_CODE的值是1  如果retCode访问成功的发 返回的是1  
		if( retCode == RIGHT_RET_CODE ){
			mCallback.getResult(mResult);
			
		}else{
			mCallback.getResult(null);
		}
	}

}
