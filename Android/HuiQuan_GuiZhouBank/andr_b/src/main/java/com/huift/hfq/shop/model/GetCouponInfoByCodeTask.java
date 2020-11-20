// ---------------------------------------------------------
// @author    yanfang.li
// @version   1.0.0
// @createTime 2015.5.22 
// @copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
// ---------------------------------------------------------
package com.huift.hfq.shop.model;

import java.util.LinkedHashMap;

import com.huift.hfq.base.SzException;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.api.API;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.model.SzAsyncTask;
import com.huift.hfq.base.pojo.Coupon;
import com.huift.hfq.base.pojo.UserToken;

import android.app.Activity;

import com.huift.hfq.shop.R;
import com.huift.hfq.shop.ShopConst;

/**
 * 根据优惠券验证码获得优惠券的信息
 *
 * @author qian.zhou
 */
public class GetCouponInfoByCodeTask extends SzAsyncTask<String, Integer, Integer> {

    /**
     * 定义一个正确的返回结果码
     **/
    private final static int RIGHT_RET_CODE = 1;
    /**
     * 定义一个错误的返回结果码
     **/
    private final static int ERROR_RET_CODE = 0;
    /**
     * 调用API返回对象
     **/
    private Coupon coupon;
    /**
     * 回调方法
     **/
    private Callback mCallback;

    /**
     * 有参构造方法
     *
     * @param acti     调用者的上下文
     * @param callback 回调方法
     */
    public GetCouponInfoByCodeTask(Activity acti, Callback callback) {
        super(acti);
        mCallback = callback;
    }

    /**
     * 回调方法的接口
     */
    public interface Callback {
        void getResult(Coupon coupon);
    }

    /**
     * 调用API查询
     * params[0] 优惠券验证码
     * params[1] 是令牌认证的编码
     */
    @Override
    protected Integer doInBackground(String... params) {
        UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
        String tokenCode = userToken.getTokenCode();
        LinkedHashMap<String, Object> reqparams = new LinkedHashMap<String, Object>();
        reqparams.put("couponCode", params[0]);
        reqparams.put("tokenCode", tokenCode);

        try {
            //调用API
            Object mResult = (Object) API.reqWebShop("getCouponInfoByCode", reqparams);
            coupon = Util.json2Obj(mResult.toString(), Coupon.class);
            if (coupon == null) {
                return ERROR_RET_CODE;
            } else {
                return RIGHT_RET_CODE;
            }
        } catch (SzException e) {
            e.printStackTrace();
            return ERROR_RET_CODE;
        }
    }

    /**
     * 处理查询结果的返回值
     *
     * @param retCode 执行成功返回码
     */
    @Override
    protected void handldBuziRet(int retCode) {
        if (retCode == RIGHT_RET_CODE) {
            mCallback.getResult(coupon);
        } else if (retCode == ERROR_RET_CODE) {
            // 返回一个空方便做为空判断
            mCallback.getResult(null);
        }
    }

}
