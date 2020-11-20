// ---------------------------------------------------------
// @author    yanfang.li
// @version   1.0.0
// @createTime 2015.5.22 
// @copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
// ---------------------------------------------------------
package cn.suanzi.baomi.shop.model;

import java.util.LinkedHashMap;

import net.minidev.json.JSONObject;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import cn.suanzi.baomi.base.ErrorCode;
import cn.suanzi.baomi.base.SzException;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.api.API;
import cn.suanzi.baomi.base.data.DB;
import cn.suanzi.baomi.base.model.SzAsyncTask;
import cn.suanzi.baomi.base.pojo.UserToken;
import cn.suanzi.baomi.shop.R;
import cn.suanzi.baomi.shop.ShopConst;

/**
 * 添加会员卡,调用API
 * @author yanfang.li
 */
public class AddCardTask extends SzAsyncTask<String, Integer, Integer> {

	private final static String TAG = SendMsgTask.class.getSimpleName();
	private JSONObject mResult;
	private Callback mCallback;
	private SharedPreferences mSharedPreferences;
	private  String mFlag;
	
	/**
	 * 无参构造方法 
	 * @param acti 调用者的上下文
	 */
	public AddCardTask(Activity acti) {
		super(acti);
	}
	/**
	 * 有参构造方法
	 * @param acti 调用者的上下文
	 * @param callback 回调方法
	 */
	public AddCardTask(Activity acti, Callback callback) {
		super(acti);
		mCallback = callback;
	}

	/**  
     * 回调方法的接口  
     */
	public interface Callback{
		/**
		 * 传递参数
		 * @param result 是异步请求的结果
		 */
		public void getResult(JSONObject result);
	}

	/**
	 * 调用API查询，添加会员卡    
	 * params[0] 会员卡的名称
	 * params[1] 会员卡的等级
	 * params[2] 创建人的编号
	 * params[3] 商店编号
	 * params[4] 会员卡样式图
	 * params[5] 需要多少积分才可以享受折扣
	 * params[6] 可享受折扣
	 * params[7] 是否实名制
	 * params[8] 是否多人使用
	 * params[9] 积分有效期
	 * params[10] 每消费1元积多少积分
	 * params[11] 需要令牌认证
	 * 
	 */
	@Override
	protected Integer doInBackground(String... params) {
		// 获得一个用户信息对象
		UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		String shopCode = userToken.getShopCode();// 商家编码
		String creatorCode = userToken.getStaffCode();
		String tokenCode = userToken.getTokenCode();// 用户登录后获得的令牌
		LinkedHashMap<String, Object> reqparams = new LinkedHashMap<String,Object>();
		reqparams.put("cardName", params[0]); // 会员卡的名称
		reqparams.put("cardType", Integer.parseInt("1000")); // 会员卡的类型，默认就是会员卡
		reqparams.put("cardLvl", params[1]); // 会员卡的等级
		reqparams.put("creatorCode", creatorCode); // 创建人的编号
		reqparams.put("shopCode", shopCode); // 商店编号
		reqparams.put("url", params[2]); // 会员卡样式图
		reqparams.put("discountRequire",params[3]); // 需要多少积分才可以享受折扣
		reqparams.put("discount", params[4]); // 可享受折扣
		reqparams.put("pointLifetime", params[5]); // 积分有效期
		reqparams.put("pointsPerCash", params[6]); // 每消费1元积多少积分
		reqparams.put("outPointsPerCash", params[7]); // 每消费1元积多少积分
		reqparams.put("remark", ""); // 备注
		reqparams.put("tokenCode", tokenCode); // 需要令牌认证
			
		try {
			//调用API
			mResult = (JSONObject) API.reqShop("editCard", reqparams);
			int retCode = Integer.parseInt(mResult.get("code").toString());
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
		mSharedPreferences = mActivity.getSharedPreferences(ShopConst.Card.CARD_FALG, Context.MODE_PRIVATE);
		mFlag = mSharedPreferences.getString(ShopConst.Card.CARD_FALG, null);
		//RIGHT_RET_CODE的值是1  如果retCode访问成功的发 返回的是1  
		if( retCode == ErrorCode.SUCC ){
			if (ShopConst.Card.CARD_UPP.equals(mFlag)) {
				Util.getContentValidate(R.string.toast_upp_succ);
			} else {
				Util.getContentValidate(R.string.toast_add_succ);
				
			}
			new Handler().postDelayed(new Runnable() {
				
				@Override
				public void run() {
					
					mCallback.getResult(mResult);
				}
			}, 1000);
			
		} else {
			mCallback.getResult(null);
			Util.getContentValidate(R.string.toast_add_fail);
		}
	}

}
