package cn.suanzi.baomi.shop.model;
import java.util.LinkedHashMap;
import net.minidev.json.JSONObject;
import android.app.Activity;
import android.util.Log;
import cn.suanzi.baomi.base.SzException;
import cn.suanzi.baomi.base.api.API;
import cn.suanzi.baomi.base.data.DB;
import cn.suanzi.baomi.base.model.SzAsyncTask;
import cn.suanzi.baomi.base.pojo.UserToken;

/**
 * 惠圈账本
 * @author wensi.yu
 *
 */
public class GetHqBookTask extends SzAsyncTask<String, String, Integer>{
	
	private final static String TAG = "GetHqBookTask";
	
	/** 创建一个JSONObject对象*/
	private JSONObject mResult;
	/** 定义一个正确的返回结果码 */ 
	private final static int RIGHT_RET_CODE = 1;
	/** 定义一个错误的返回结果码 */ 
	private final static int ERROR_RET_CODE = 0;
	/**回调方法**/ 	
	private Callback callback;

	public GetHqBookTask(Activity acti) {
		super(acti);
	}
	
	/**
	 * 回调方法的构造函数
	 * @param acti
	 */
	public GetHqBookTask(Activity acti,Callback callback) {
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

    @Override
   	protected Integer doInBackground(String... params) {
   		UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
   		String shopCode = userToken.getShopCode();//商家编码
   		String tokenCode = userToken.getTokenCode();//需要令牌认证
   		LinkedHashMap<String, Object> reqParams = new LinkedHashMap<String, Object>();
   		reqParams.put("shopCode",shopCode);
   		reqParams.put("tokenCode",tokenCode);		
   		try {
   			mResult = (JSONObject)API.reqShop("getHqBook", reqParams);
   			int retCode = ERROR_RET_CODE;
   			if (null != mResult) {
   				retCode = RIGHT_RET_CODE; //1 代表访问成功
   			}
   			Log.d(TAG, "retCode=="+retCode);
   			return retCode;
   		} catch (SzException e) {
   			this.mErrCode = e.getErrCode();
   			return this.mErrCode.getCode();
   		}
   	}

   	/**
   	 * 正常业务逻辑处理
   	 */
   	@Override
   	protected void handldBuziRet(int retCode) {
   		if (retCode == RIGHT_RET_CODE) {
   			callback.getResult(mResult);
   		} else if (retCode == ERROR_RET_CODE){ 
   			callback.getResult(null);
   		}
   	}

}
