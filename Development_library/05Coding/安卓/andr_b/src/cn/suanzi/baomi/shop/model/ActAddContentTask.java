package cn.suanzi.baomi.shop.model;

import java.util.LinkedHashMap;

import net.minidev.json.JSONObject;
import android.app.Activity;
import cn.suanzi.baomi.base.ErrorCode;
import cn.suanzi.baomi.base.SzException;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.api.API;
import cn.suanzi.baomi.base.data.DB;
import cn.suanzi.baomi.base.model.SzAsyncTask;
import cn.suanzi.baomi.base.pojo.UserToken;
import cn.suanzi.baomi.base.utils.ActivityUtils;
import cn.suanzi.baomi.shop.R;

/**
 * @author wensi.yu
 * 添加营销活动的异步
 */
public class ActAddContentTask extends SzAsyncTask<String, String, Integer> {
	
	private final static String TAG = "ActAddTask";
	
	private final static int ACTADD_PREPAYMENT = 20000;
	/** 创建一个JSONObject对象*/
	private JSONObject mResult;
	/** 回调方法*/
	private Callback callback;
	/** 定义一个正确的返回结果码 */ 
	private final static int RIGHT_RET_CODE = 1;
	/** 定义一个错误的返回结果码 */ 
	private final static int ERROR_RET_CODE = 0;

	/**
	 * 构造函数
	 * @param acti
	 */
	public ActAddContentTask(Activity acti) {
		super(acti);
	}
	
	/**
	 * 回调方法的构造函数
	 * @param acti
	 */
	public ActAddContentTask(Activity acti,Callback callback) {
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
	 * 调用api 进行添加操作
	 */
	@Override       
	protected Integer doInBackground(String... params) {
		UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		String shopCode = userToken.getShopCode();
		String creatorCode = userToken.getStaffCode();
		String tokenCode = userToken.getTokenCode();
		LinkedHashMap<String, Object> reqParams = new LinkedHashMap<String, Object>();
		reqParams.put("activityName", params[0]); //活动主题
		reqParams.put("startTime", params[1]); //活动开始时间
		reqParams.put("endTime", params[2]); //活动结束时间
		reqParams.put("activityLocation", params[3]); //活动地点      
		reqParams.put("txtContent", params[4]); //活动说明
		reqParams.put("limitedParticipators", params[5]); //活动参与人数上限
		reqParams.put("isPrepayRequired", params[6]); //是否需要预付费
		reqParams.put("prePayment", params[7]); //预付金额
		reqParams.put("isRegisterRequired", params[8]); //是否需要报名
		reqParams.put("activityImg", params[9]); //活动图片
		reqParams.put("activityLogo",params[10]); //活动方形图片 暂放
		reqParams.put("shopCode", shopCode); //发起活动的商家编码
		reqParams.put("creatorCode",creatorCode); //活动发起人编码
		reqParams.put("activityBelonging", Integer.parseInt("3"));	
		reqParams.put("tokenCode", tokenCode);	
		
		try {
			mResult = (JSONObject) API.reqShop("addActivity", reqParams);
			//调用成功
			Long retCode = (Long) mResult.get("code");
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
		if (retCode == ErrorCode.SUCC) {
			Util.getContentValidate(R.string.toast_actcontent_success);
			ActivityUtils.finishAll();
			callback.getResult(mResult);
		}else {
			if(retCode == ACTADD_PREPAYMENT){
				callback.getResult(null);
				Util.getContentValidate(R.string.toast_actadd_prepayment);
			}
		}
	}
}
