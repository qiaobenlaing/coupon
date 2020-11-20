// ---------------------------------------------------------
// @author    yanfang.li
// @version   1.0.0
// @createTime 2015.5.22 
// @copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
// ---------------------------------------------------------
package com.huift.hfq.shop.model;

import java.util.LinkedHashMap;

import com.huift.hfq.base.ErrorCode;
import com.huift.hfq.base.SzException;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.api.API;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.model.SzAsyncTask;
import com.huift.hfq.base.pojo.UserToken;
import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.base.utils.DialogUtils;

import net.minidev.json.JSONObject;
import android.app.Activity;
import com.huift.hfq.shop.R;

/**
 * 添加每批次优惠卡,调用API
 * @author yanfang.li
 */
public class AddBatchCouponTask extends SzAsyncTask<String, Integer, Integer> {

	private final static String TAG = AddBatchCouponTask.class.getSimpleName();
	private Callback mCallback;
	/**
	 * 有参构造方法
	 * @param acti 调用者的上下文
	 * @param callback 回调方法
	 */
	public AddBatchCouponTask(Activity acti, Callback callback) {
		super(acti);
		mCallback = callback;
	}
	
	/**  
     * 回调方法的接口  
     *  
     */
	public interface Callback{
		public void getResult(int result);
	}
	/**
	 * 无参构造方法 
	 * @param acti 调用者的上下文
	 */
	public AddBatchCouponTask(Activity acti) {
		super(acti);
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (mActivity != null) {
			if (mProcessDialog != null) {
				mProcessDialog.setMessage(mActivity.getString(R.string.add_batchcoupon));
			}
		}
	}

	/**
	 * 调用API查询，添加会员卡    
	 * params[0] 优惠券名字
	 * params[1] 优惠券类型
	 * params[2] 优惠券开始使用日期
	 * params[3] 创建者编码
	 * params[4] 商店编码
	 * params[5] 总发行量
	 * params[6] 优惠券说明即备注
	 * params[7] 优惠券失效时间
	 * params[8] 券样即背景样式
	 * params[9] 最后可领用日期
	 * params[10] 打折数额
	 * params[11] 所属行业
	 * params[11] 抵用金额
	 * params[13] 达到多少金额可用
	 * params[14] 一次可用上限数量
	 * params[15] 每人可领数量
	 * params[16] 优惠券的扩展使用规则
	 * params[17] 是否消费后才可以领取
	 * params[18] 需要消费多少才可以领取
	 * params[19] 需要令牌认证
	 * 
	 */
	@Override
	protected Integer doInBackground(String... params) {
		
		LinkedHashMap<String, Object> reqparams = new LinkedHashMap<String,Object>();
		UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		String shopCode = userToken.getShopCode();
		String tokenCode = userToken.getTokenCode();
		String creatorCode = userToken.getStaffCode();
		reqparams.put("shopCode", shopCode); // 商店编码
		reqparams.put("couponType", params[0]); 
		reqparams.put("totalVolume", params[1]); // 共发多少张	
		reqparams.put("startUsingTime", params[2]); // 开始使用日期
		reqparams.put("expireTime", params[3]);  // 结束使用日期
		reqparams.put("dayStartUsingTime", params[4]); // 每天开始使用时间
		reqparams.put("dayEndUsingTime", params[5]); // 每天结束时间
		reqparams.put("startTakingTime", params[6]); // 每天结束时间
		reqparams.put("endTakingTime", params[7]);  // 截至领用日期
		reqparams.put("isSend", params[8]); // 是否满就送
		reqparams.put("sendRequired", params[9]);  // 满就送的金额
		reqparams.put("remark", params[10]);  // 优惠券说明
		reqparams.put("creatorCode", creatorCode); // 用户编码
		reqparams.put("discountPercent", params[11]); // 。除折扣券外，其他类型优惠券传0
		reqparams.put("insteadPrice", params[12]);  // 每张减免多少元或者每张面值多少元
		reqparams.put("availablePrice", params[13]); // 除抵扣券外其他类型优惠券传0
		reqparams.put("function", params[14]); // 折扣券，抵扣券，N元购传空字符串
		reqparams.put("limitedNbr", params[15]); // 限使用多少张
		reqparams.put("nbrPerPerson", params[16]); // 每人限领用张数
		reqparams.put("limitedSendNbr", params[17]); // 满就送 送多少张
		reqparams.put("payPrice", params[18]); // 得到一张优惠券需要多少钱
		reqparams.put("tokenCode", tokenCode); // 需要令牌认证
			
		try {
			//调用API
			JSONObject result = (JSONObject) API.reqShop("addBatchCoupon", reqparams);
			int retCode = Integer.parseInt(result.get("code").toString());
			
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
		if( retCode == ErrorCode.SUCC ){
			DialogUtils.showDialogSingle(AppUtils.getActivity(), Util.getString(R.string.toast_add_succ),
					R.string.dialog_title, R.string.dialog_ok,
					new DialogUtils().new OnResultListener() {
						@Override
						public void onOK() {
							mCallback.getResult(ErrorCode.SUCC);
							Util.exit();	
						}
					});
			
		}else{
			mCallback.getResult(ErrorCode.FAIL);
			Util.getContentValidate(R.string.toast_add_fail);
		}
	}

}
