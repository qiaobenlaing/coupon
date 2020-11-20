package com.icbc.smartpos.transservice.aidl;

/**
 * 交易流程回调接口
 * @author: icbc
 */
interface TransHandler {

    void onFinish(in Bundle baseResult, in Bundle transResult, in Bundle extraInfo);
}