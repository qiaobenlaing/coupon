package cn.suanzi.baomi.cust.model;

import java.util.LinkedHashMap;

import net.minidev.json.JSONObject;
import android.app.Activity;
import android.widget.Toast;
import cn.suanzi.baomi.base.SzException;
import cn.suanzi.baomi.base.api.API;
import cn.suanzi.baomi.base.model.SzAsyncTask;

/**
 * 商家会员卡详情
 * @author qian.zhou
 */
public class MyMCardListTask extends SzAsyncTask<String, String, Integer>{
	/**创建一个JSONObject对象**/
	private JSONObject mResult;
	/**回调方法**/
	private Callback mCallback;
	/**定义一个正确的返回结果码**/
	private final static int RIGHT_RET_CODE = 1 ;
	/**定义一个错误的返回结果码**/ 
	private final static int ERROR_RET_CODE = 0;
	
	/**
	 * @param acti 启动上下文
	 * @param mCallback
	 */
	public MyMCardListTask(Activity acti, Callback mCallback) {
		super(acti);
		this.mCallback = mCallback;
	}
	
	/**  
     * 回调方法的接口  
     */  
    public interface Callback{  
        public void getResult(JSONObject object) ;  
    }
    
	/**
	 * 构造函数
	 * @param acti
	 */
	public MyMCardListTask(Activity acti) {
		super(acti);
	}

	/**
	 * 业务逻辑代码部分
	 */
	@Override
	protected void handldBuziRet(int retCode) {
		if (retCode == RIGHT_RET_CODE) {
			mCallback.getResult(mResult) ;
		}else if(retCode==ERROR_RET_CODE){
			mCallback.getResult(null);
			Toast.makeText(this.mActivity, "没有数据加载了",Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 调用Api
	 */
	@Override
	protected Integer doInBackground(String... params) {
		LinkedHashMap<String, Object> reqParams = new LinkedHashMap<String, Object>() ;
		reqParams.put("userCardCode" , params[0]) ;
		reqParams.put("tokenCode" , params[1]) ;
		try {
			int retCode = ERROR_RET_CODE;
				mResult = (JSONObject)API.reqCust("getUserCardInfo", reqParams);
				//判断查询的一个对象不为空为空 就返回一个正确的编码
				if ( !(mResult == null ||mResult.size() == 0 ||"".equals(mResult.toJSONString()))) {
					retCode = RIGHT_RET_CODE; //1 代表访问成功
				}
			return retCode;
		} catch (SzException e) {
			this.mErrCode = e.getErrCode() ;
			return this.mErrCode.getCode() ;
		}
	}
}
