package com.huift.hfq.shop.model;

import java.util.LinkedHashMap;

import com.huift.hfq.base.ErrorCode;
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
 * 添加活动
 * @author wensi.yu
 */
public class AddActivityTask extends SzAsyncTask<String, String, Integer>{

	private final static String TAG = "AddActivityTask";

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
	public AddActivityTask(Activity acti) {
		super(acti);
	}

	/**
	 * 回调方法的构造函数
	 * @param acti
	 */
	public AddActivityTask(Activity acti,Callback callback) {
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
	protected void onPreExecute() {
		if (mActivity != null && mProcessDialog != null) {
			mProcessDialog.dismiss();
		}
	}

	/**
	 * 业务处理
	 */
	@Override
	protected Integer doInBackground(String... params) {
		UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		String shopCode = userToken.getShopCode();
		String creatorCode = userToken.getStaffCode();
		String tokenCode = userToken.getTokenCode();
		LinkedHashMap<String, Object> reqParams = new LinkedHashMap<String, Object>();
		reqParams.put("activityName", params[0]);//活动主题。活动名称
		reqParams.put("startTime", params[1]);//活动开始时间
		reqParams.put("endTime", params[2]);//活动结束时间
		reqParams.put("activityLocation", params[3]);//活动地点
		reqParams.put("txtContent", params[4]);//活动说明
		reqParams.put("limitedParticipators", params[5]);//活动参与人数上限
		reqParams.put("isPrepayRequired", "0");//活动参与人数上限  1-需要；0-不需要
		reqParams.put("prePayment", "0");//预付金额
		reqParams.put("isRegisterRequired", "0");//是否需要报名。1-需要；0-不需要
		reqParams.put("activityImg", params[6]);//活动图片
		reqParams.put("activityLogo", "");//活动方形图片
		reqParams.put("shopCode", shopCode);//发起活动的商家编码
		reqParams.put("creatorCode", creatorCode);//活动发起人编码
		reqParams.put("activityBelonging", "3");//活动归属 1-平台活动；2-工行活动；3-商家活动
		reqParams.put("actType", params[7]);//活动类型 1-聚会，2-运动，3-户外，4-亲子，5-体验课，6-音乐
		reqParams.put("contactName", params[8]);//联系人
		reqParams.put("contactMobileNbr", params[9]);//联系方式
		reqParams.put("feeScale", params[10]);//收费规格
		reqParams.put("refundRequired", params[11]);//退款要求
		reqParams.put("registerNbrRequired", params[12]);//单人报名人数限制
		reqParams.put("dayOfBooking", params[13]);//活动类型 提前预约天数
		reqParams.put("tokenCode", tokenCode);
		try {
			mResult = (JSONObject)API.reqShop("addActivity", reqParams);
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
