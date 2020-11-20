package cn.suanzi.baomi.cust.model;

import java.util.LinkedHashMap;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import android.app.Activity;
import android.util.Log;
import cn.suanzi.baomi.base.SzException;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.api.API;
import cn.suanzi.baomi.base.model.SzAsyncTask;
import cn.suanzi.baomi.cust.R;

/**
 * 体验券
 * @author wensi.yu
 *
 */
public class ExperienceMuseumTask extends SzAsyncTask<String, String, Integer> {

	private final static String TAG = ExperienceMuseumTask.class.getSimpleName();
	
	/** 创建一个JSONObject对象*/
	private JSONObject mResult;
	/** 定义一个正确的返回结果码 */ 
	private final static int RIGHT_RET_CODE = 1;
	/** 定义一个错误的返回结果码 */ 
	private final static int ERROR_RET_CODE = 0;
	/**回调方法**/ 	
	private Callback callback;
	
	/**
	 * 构造方法
	 * @param acti
	 */
	public ExperienceMuseumTask(Activity acti) {
		super(acti);
		
	}
	
	/**
	 * 回调方法的构造函数
	 * @param acti
	 */
	public ExperienceMuseumTask(Activity acti,Callback callback) {
		super(acti);
		this.callback=callback;
	}

	/**  
     * 回调方法的接口  
     *  
     */  
    public interface Callback{  
        public void getResult(JSONObject mResult);  
    }

	/**
	 * 体验券的异步任务
	 */
	@Override
	protected Integer doInBackground(String... params) {
		
		LinkedHashMap<String, Object> reqParams = new LinkedHashMap<String, Object>();
		reqParams.put("couponType", Integer.parseInt("6")); //优惠券类型   6体验券
		reqParams.put("searchWord",params[0]); //检索关键字
		reqParams.put("longitude",params[1]); //用户所在经度
		reqParams.put("latitude",params[2]); //用户所在纬度
		reqParams.put("page",Integer.parseInt(params[3])); //页码
		reqParams.put("city",params[4]); //页码
		
		Log.i(TAG, "shopName=="+params[0]+"longitude=="+params[1]+"latitude=="+params[2]+"page=="+Integer.parseInt(params[3]));
		
		try {
			int retCode = ERROR_RET_CODE;
			mResult = (JSONObject) API.reqCust("listCoupon", reqParams);
			JSONArray ArResult = (JSONArray) mResult.get("couponList");
			//判断返回的对象是否为空
			if (mResult != null || ArResult.size() != 0 || !"[]".equals(ArResult.toString())) {
				//返回正确结果码
				retCode = RIGHT_RET_CODE;
			}
			Log.i(TAG, "ddddd");
			return retCode;
			
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
	
		if(retCode == RIGHT_RET_CODE){
			callback.getResult(mResult);
		}
		else if(retCode == ERROR_RET_CODE) {
			callback.getResult(null);
			Util.getContentValidate(R.string.toast_nodata);
		}
	}
}
