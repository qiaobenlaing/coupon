package com.icbc.smartpos.transservice.aidl;

import com.icbc.smartpos.transservice.aidl.TransHandler;

/**
 * 交易调用接口
 * @author: icbc
 */
interface ITransService {

        long startTrans(String transType,
                        in Bundle ctrlData,
                        in Bundle transData,
                        in TransHandler handler);
}