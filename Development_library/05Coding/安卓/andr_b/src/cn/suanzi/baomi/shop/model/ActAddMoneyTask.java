package cn.suanzi.baomi.shop.model;

import java.util.LinkedHashMap;

import net.minidev.json.JSONObject;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import cn.suanzi.baomi.base.ErrorCode;
import cn.suanzi.baomi.base.SzException;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.api.API;
import cn.suanzi.baomi.base.model.SzAsyncTask;
import cn.suanzi.baomi.shop.R;
import cn.suanzi.baomi.shop.activity.ActListActivity;

/**
 * 添加红包的异步
 * @author wensi.yu
 *
 */
public class ActAddMoneyTask extends SzAsyncTask<String, String, Integer> {

	private final static String TAG = "ActAddMoneyTask";
	
	/** 红包发行总额过低*/
	public static final int MONEY_MIN = 50719;
	/** 红包发行总额过高*/
	public static final int MONEY_MAX = 50727;
	
	/** 创建一个JSONObject对象**/
	private JSONObject mResult;
	/**回调方法**/ 	
	private Callback callback;
	
	/**
	 * 构造函数
	 * @param acti
	 */
	public ActAddMoneyTask(Activity acti) {
		super(acti);
	}
	
	/**
	 * 回调方法的构造函数
	 * @param acti
	 */
	public ActAddMoneyTask(Activity acti,Callback callback) {
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
	 * 添加红包
	 */
	@Override
	protected Integer doInBackground(String... params) {
		//添加红包的详情
		LinkedHashMap<String, Object> reqParams = new LinkedHashMap<String, Object>();
		reqParams.put("bonusBelonging",Integer.parseInt("1")); //红包归属
		reqParams.put("shopCode", params[0]); //归属商店编码
		reqParams.put("creatorCode", params[1]); //创建者编码
		reqParams.put("upperPrice", params[2]); //金额区间（上限）
		reqParams.put("lowerPrice", params[3]); //金额区间（下限）
		reqParams.put("totalValue", params[4]); //发行红包总额
		reqParams.put("nbrPerDay", "0"); //每天限制发红包数量
		reqParams.put("totalVolume", params[5]); //发行总数量
		reqParams.put("validityPeriod", params[6]); //红包使用有效期
		reqParams.put("startTime", params[7]); //红包活动开始时间
		reqParams.put("endTime", params[8]); //红包活动结束时间
		reqParams.put("tokenCode", params[9]); //令牌认证
		try {
			mResult = (JSONObject) API.reqShop("addBonus", reqParams);
			Log.i(TAG , "ActAddMoneyTask.mResult========="+mResult);
			// 如果成功，保存到数据库
			int retCode = Integer.parseInt(mResult.get("code").toString());
			Log.i(TAG, "ActAddMoneyTask.retCode========="+retCode);
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
			Util.getContentValidate(R.string.toast_actmoney_success);
			callback.getResult(mResult);
			Intent intent = new Intent(mActivity, ActListActivity.class);
			this.mActivity.startActivity(intent);
			this.mActivity.finish();
		}else {
			if(retCode == ErrorCode.FAIL){
				callback.getResult(null);
				Util.addToast(R.string.toast_actmoney_erroe);
			}
			else if(retCode == MONEY_MIN){
				callback.getResult(null);
				Util.addToast(R.string.toast_actmoney_min);
			}
			else if(retCode == MONEY_MAX){
				callback.getResult(null);
				Util.addToast(R.string.toast_actmoney_max);
			}
		}
	}
}
