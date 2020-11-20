package com.huift.hfq.shop.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import net.minidev.json.JSONObject;
import android.app.Activity;
import android.util.Log;
import com.huift.hfq.shop.R;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huift.hfq.base.ErrorCode;
import com.huift.hfq.base.SzException;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.api.API;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.model.SzAsyncTask;
import com.huift.hfq.base.pojo.ActivityEdit;
import com.huift.hfq.base.pojo.PromotionPrice;
import com.huift.hfq.base.pojo.UserToken;

/**
 * 修改活动信息
 * @author qian.zhou
 */
public class UpdateActivityTask extends SzAsyncTask<String, String, Integer>{
	
	private final static String TAG = UpdateActivityTask.class.getSimpleName();
	
	/** 活动开始时间不正确*/
	private static final int CAMPAIGN_ADD_STARTTIME = 50201;
	/** 活动结束时间不正确*/
	private static final int CAMPAIGN_ADD_ENDTIME = 50202;
	/** 活动说明不正确*/
	private static final int CAMPAIGN_ADD_DEDTAIL = 50204;
	
	/** 创建一个JSONObject对象*/
	private JSONObject mResult;
	/**回调方法**/ 	
	private Callback callback;

	/**
	 * 构造函数
	 * @param acti
	 */
	public UpdateActivityTask(Activity acti) {
		super(acti);
	}
	
	/**
	 * 回调方法的构造函数
	 * @param acti
	 */
	public UpdateActivityTask(Activity acti,Callback callback) {
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
		JSONObject updateData = new JSONObject();
		updateData.put("activityCode", params[0]);//活动主题。活动名称
		updateData.put("activityName", params[1]);//活动主题。活动名称
		updateData.put("startTime", params[2]);//活动开始时间
		updateData.put("endTime", params[3]);//活动结束时间
		updateData.put("activityLocation", params[4]);//活动地点
		updateData.put("richTextContent", params[5]);//活动说明
		updateData.put("limitedParticipators", params[6]);//活动参与人数上限
		updateData.put("activityImg", params[7]);//活动图片
		updateData.put("actType", params[8]);//活动类型 1-聚会，2-运动，3-户外，4-亲子，5-体验课，6-音乐
		updateData.put("contactName", params[9]);//联系人
		updateData.put("contactMobileNbr", params[10]);//联系方式
		updateData.put("feeScale", params[11]);//收费规格
		updateData.put("refundRequired", params[12]);//退款要求
		updateData.put("registerNbrRequired", params[13]);//单人报名人数限制
		updateData.put("dayOfBooking", params[14]);//活动类型 提前预约天数
		updateData.put("activityBelonging", "3");//活动归属 1-平台活动；2-工行活动；3-商家活动
		updateData.put("shopCode", userToken.getShopCode());
		// api参数
		reqParams.put("activityCode", params[0]);//活动编码
		reqParams.put("updateData", updateData.toString());
		reqParams.put("tokenCode", tokenCode);
		try {
			mResult = (JSONObject)API.reqShop("editActivity", reqParams);
			Log.i(TAG, "mResult  Add =="+mResult);
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
				Util.getContentValidate(R.string.myafter_order_commit_fail);
			} else if (retCode == CAMPAIGN_ADD_STARTTIME) {
				callback.getResult(null);
				Util.getContentValidate(R.string.toast_actlist_startcamtime);
			} else if (retCode == CAMPAIGN_ADD_ENDTIME) {
				callback.getResult(null);
				Util.getContentValidate(R.string.toast_actlist_endtime);
			} else if (retCode == CAMPAIGN_ADD_DEDTAIL) {
				callback.getResult(null);
				Util.getContentValidate(R.string.toast_actlist_context);
			}
		}
	}

}
