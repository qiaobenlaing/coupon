package com.huift.hfq.shop.model;

import android.app.Activity;

import com.huift.hfq.base.Util;
import com.huift.hfq.base.api.API;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.model.SzAsyncTask;
import com.huift.hfq.base.pojo.BaseBean;
import com.huift.hfq.base.pojo.UserToken;

import java.util.LinkedHashMap;

public class UploadPayInfoTask extends SzAsyncTask<String, Integer, Integer> {
    private final static int RIGHT_RET_CODE = 1;
    private final static int ERROR_RET_CODE = 0;
    private BaseBean resultBean;
    private Callback mCallback;

    /**
     * 构造函数
     *
     * @param acti 启动本对象的Activity。
     */
    public UploadPayInfoTask(Activity acti, Callback callback) {
        super(acti);
        mCallback = callback;
    }

    /**
     * 回调方法的接口
     */
    public interface Callback {
        void getResult(BaseBean resultBean);
    }

    @Override
    protected void handldBuziRet(int retCode) {
        if (retCode == RIGHT_RET_CODE) {
            mCallback.getResult(resultBean);
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
        reqparams.put("orderNo", params[0]);
        reqparams.put("payType", params[1]);
        reqparams.put("extPayRefNo", params[2]);
        reqparams.put("extPayDateTime", params[3]);
        reqparams.put("extPayOrderNo", params[4]);
        reqparams.put("posTermId", params[5]);
        reqparams.put("tokenCode", tokenCode);

        try {
            //调用API
            Object result = API.reqWebShop("changeDataStatus", reqparams);
            resultBean = Util.json2Obj(result.toString(), BaseBean.class);
            if (resultBean == null) {
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
