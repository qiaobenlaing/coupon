package com.huift.hfq.shop.model;

import android.app.Activity;

import com.huift.hfq.base.Util;
import com.huift.hfq.base.api.API;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.model.SzAsyncTask;
import com.huift.hfq.base.pojo.PayOrderBean;
import com.huift.hfq.base.pojo.UserToken;

import java.util.LinkedHashMap;

public class CreateOrderTask extends SzAsyncTask<String, Integer, Integer> {
    private final static int RIGHT_RET_CODE = 1;
    private final static int ERROR_RET_CODE = 0;
    private PayOrderBean payOrder;
    private Callback mCallback;

    /**
     * 构造函数
     *
     * @param acti 启动本对象的Activity。
     */
    public CreateOrderTask(Activity acti, Callback callback) {
        super(acti);
        mCallback = callback;
    }

    /**
     * 回调方法的接口
     */
    public interface Callback {
        void getResult(PayOrderBean payOrder);
    }

    @Override
    protected void handldBuziRet(int retCode) {
        if (retCode == RIGHT_RET_CODE) {
            mCallback.getResult(payOrder);
        } else if (retCode == ERROR_RET_CODE) {
            // 返回一个空方便做为空判断
            mCallback.getResult(null);
        }
    }

    @Override
    protected Integer doInBackground(String... params) {
        UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
        String tokenCode = userToken.getTokenCode();
        LinkedHashMap<String, Object> reqparams = new LinkedHashMap<String, Object>();
        reqparams.put("shopCode", userToken.getShopCode());
        reqparams.put("orderAmount", params[0]);
        reqparams.put("couponCode", params[1]);
        reqparams.put("noDiscountPrice", params[2]);
        reqparams.put("tokenCode", tokenCode);

        try {
            //调用API
            Object result = API.reqWebShop("addConsumeOrder", reqparams);
            payOrder = Util.json2Obj(result.toString(), PayOrderBean.class);
            if (payOrder == null) {
                return ERROR_RET_CODE;
            } else {
                return RIGHT_RET_CODE;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ERROR_RET_CODE;
        }
    }
}
