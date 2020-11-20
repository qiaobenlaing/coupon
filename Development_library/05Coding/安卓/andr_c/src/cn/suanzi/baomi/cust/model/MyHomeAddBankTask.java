package cn.suanzi.baomi.cust.model;

import java.util.LinkedHashMap;

import net.minidev.json.JSONObject;
import android.app.Activity;
import android.util.Log;
import cn.suanzi.baomi.base.ErrorCode;
import cn.suanzi.baomi.base.SzException;
import cn.suanzi.baomi.base.api.API;
import cn.suanzi.baomi.base.model.SzAsyncTask;

/**
 * 添加银行卡的异步
 * 
 * @author wensi.yu
 *
 */
public class MyHomeAddBankTask extends SzAsyncTask<String, Integer, Integer> {

	private final static String TAG = MyHomeAddBankTask.class.getSimpleName();

	private JSONObject mResult;
	/**回调方法**/ 	
	private Callback callback;
	
	/**
	 * 构造函数
	 * @param acti
	 */
	public MyHomeAddBankTask(Activity acti) {
		super(acti);
		
	}
	
	/**
	 * 回调方法的构造函数
	 * @param acti
	 */
	public MyHomeAddBankTask(Activity acti,Callback callback) {
		super(acti);
		this.callback = callback;
	}

	/**  
     * 回调方法的接口  
     *  
     */  
    public interface Callback{  
        public void getResult(int succ);  
    }
	
	/**
	 * 添加银行卡
	 */
	@Override
	protected Integer doInBackground(String... params) {
		
		LinkedHashMap<String, Object> reqParams = new LinkedHashMap<String,Object>();
		reqParams.put("userCode", params[0]); //用户编码
		reqParams.put("accountName", params[1]); //账户姓名
		reqParams.put("idType", Integer.parseInt(params[2])); //证件类型
		reqParams.put("idNbr", params[3]); //证件号
		reqParams.put("accountNbrPre6", params[4]); //银行卡号前6位
		reqParams.put("accountNbrLast4", params[5]); //银行卡卡号后4位
		reqParams.put("mobileNbr", params[6]); //预留手机号
		reqParams.put("tokenCode",params[7]); //令牌认证
		try {
			mResult = (JSONObject) API.reqCust("addBankAccount", reqParams);
			Log.i(TAG, "mResult===================="+mResult);
			int retCode = Integer.parseInt(mResult.get("code").toString());
			Log.i("MyHomeAddBankTask", "********retCode:"+retCode);
			return retCode;
		} catch (SzException e) {
			this.mErrCode = e.getErrCode();
			return this.mErrCode.getCode();//返回错误编码
		}
	}

	/**    
	 * 结果处理
	 */
	@Override
	protected void handldBuziRet(int retCode) {
		if( retCode == ErrorCode.SUCC ){
			callback.getResult(ErrorCode.SUCC);
			/*mActivity.startActivity(new Intent(mActivity,MyHomeBankListActivity.class));
			mActivity.finish();*/
			
		}else{
			callback.getResult(ErrorCode.FAIL);
			//Toast.makeText(this.mActivity, "添加失败", Toast.LENGTH_SHORT).show();
		}
	}

}
