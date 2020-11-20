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
import com.huift.hfq.base.pojo.UserToken;

import android.app.Activity;
import com.huift.hfq.shop.R;

/**
 * 根据优惠券验证码获得优惠券的信息
 * @author qian.zhou
 */
public class GetCouponInfoByCodeTask extends SzAsyncTask<String, Integer, Integer> {

    private final static String TAG = GetCouponInfoByCodeTask.class.getSimpleName();
    /** 定义一个正确的返回结果码 **/
    private final static int RIGHT_RET_CODE = 1;
    /** 定义一个错误的返回结果码 **/
    private final static int ERROR_RET_CODE = 0;
    /** 调用API返回对象 **/
    private Object mResult;
    /** 回调方法 **/
    private Callback mCallback;

    /**
     * 无参构造方法
     * @param acti 调用者的上下文
     */
    public GetCouponInfoByCodeTask(Activity acti) {
        super(acti);
    }

    /**
     * 有参构造方法
     * @param acti 调用者的上下文
     * @param callback 回调方法
     */
    public GetCouponInfoByCodeTask(Activity acti, Callback callback) {
        super(acti);
        mCallback = callback;
    }

    /**
     * 回调方法的接口  
     *
     */
    public interface Callback{
        public void getResult(Object result);
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
        LinkedHashMap<String, Object> reqparams = new LinkedHashMap<String,Object>();
        reqparams.put("couponCode", params[0]);
        reqparams.put("tokenCode", tokenCode);

        try {
            //调用API
            mResult = (Object) API.reqShop("getCouponInfoByCode", reqparams);
            int retCode = ERROR_RET_CODE;
            if (!mResult.toString().equals("[]")) {//是jsonobject
                retCode = RIGHT_RET_CODE; //1 代表访问成功
            } else {
                retCode = ERROR_RET_CODE; //1 代表访问成功
            }
            return retCode;
        } catch (SzException e) {

            this.mErrCode = e.getErrCode();
            return this.mErrCode.getCode();//返回错误编码
        }
    }

    /**
     * 处理查询结果的返回值
     * @param retCode 执行成功返回码
     */
    @Override
    protected void handldBuziRet(int retCode) {

        //RIGHT_RET_CODE的值是1  如果retCode访问成功的发 返回的是1
        if ( retCode == RIGHT_RET_CODE ) {

            mCallback.getResult(mResult);

        } else {
            if (retCode == ERROR_RET_CODE ) {
                // 返回一个空方便做为空判断
                mCallback.getResult(null);
                Util.addToast(R.string.toast_data_error);
            }
        }
    }

}
