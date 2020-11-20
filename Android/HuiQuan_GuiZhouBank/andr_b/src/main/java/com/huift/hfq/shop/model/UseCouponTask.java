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

/**
 * 验证优惠券，用于兑换券，代金券的使用
 *
 * @author qian.zhou
 */
public class UseCouponTask extends SzAsyncTask<String, String, Integer> {
    /**
     * 创建一个JSONObject对象
     **/
    private JSONObject mResult;

    /**
     * 构造函数
     *
     * @param acti
     */
    public UseCouponTask(Activity acti) {
        super(acti);
    }

    /**
     * 回调方法
     */
    private Callback callback;

    /**
     * @param acti     上下文
     * @param callback 回调方法
     */
    public UseCouponTask(Activity acti, Callback callback) {
        super(acti);
        this.callback = callback;
    }

    /**
     * 回调方法的接口
     */
    public interface Callback {
        void getResult(JSONObject object);
    }

    /**
     * onPostExecute()中的正常业务逻辑处理.
     */
    @Override
    protected void handldBuziRet(int retCode) {
        callback.getResult(mResult);
    }

    /**
     * 调用API,用于验证兑换券、代金券的验证和使用
     */
    @Override
    protected Integer doInBackground(String... params) {
        UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
        LinkedHashMap<String, Object> reqParams = new LinkedHashMap<String, Object>();
        reqParams.put("userCode", params[0]);//用户编码
        reqParams.put("shopCode", userToken.getShopCode());//商家编码
        reqParams.put("userCouponCode", params[1]);
        reqParams.put("tokenCode", userToken.getTokenCode());//需要令牌认证
        reqParams.put("realPay", params[2]);//消费金额
        try {
            mResult = (JSONObject) API.reqWebShop("useCoupon", reqParams);
            // 如果成功，保存到数据库
            return Integer.parseInt(String.valueOf(mResult.get("code"))) ;
        } catch (SzException e) {
            this.mErrCode = e.getErrCode();
            return this.mErrCode.getCode();
        }
    }
}
