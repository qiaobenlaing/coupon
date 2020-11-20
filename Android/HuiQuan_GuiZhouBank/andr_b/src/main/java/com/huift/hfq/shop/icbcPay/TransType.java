package com.huift.hfq.shop.icbcPay;

public class TransType {
    /***
     * 以下是定义总行应用交易类型
     */
    /***
     * 收银 包含支付方式有(银行卡消费、二维码消费、积分消费、分期付款)
     */
    public static final String TRANSTYPE_MULTI_PURCHASE = "MULTI_PURCHASE";
    /***
     * 银行卡消费
     */
    public static final String TRANSTYPE_PURCHASE = "PURCHASE";
    /***
     * 二维码消费
     */
    public static final String TRANSTYPE_QRPURCHASE = "QRPURCHASE";
    /***
     * 积分消费
     */
    public static final String TRANSTYPE_INTEGRAL_PURCHASE = "INTEGRAL_PURCHASE";
    /***
     * 分期付款
     */
    public static final String TRANSTYPE_INSTALLMENT = "INSTALLMENT";
    /***
     * 余额查询
     */
    public static final String TRANSTYPE_INQUERY = "INQUERY";
    /***
     * 积分查询
     */
    public static final String TRANSTYPE_INTEGRAL_QUERY = "INTEGRAL_QUERY";
    /***
     * 当日撤销
     */
    public static final String TRANSTYPE_POS_VOID = "POS_VOID";
    /***
     * 隔日退货
     */
    public static final String TRANSTYPE_REFUND = "REFUND";
    /***
     * 积分退货
     */
    public static final String TRANSTYPE_INTEGRAL_REFUND = "INTEGRAL_REFUND";
    /***
     * 二维码退货
     */
    public static final String TRANSTYPE_QRREFUND = "QRREFUND";
    /***
     * 预授权
     */
    public static final String TRANSTYPE_PREAUTH = "PREAUTH";
    /***
     * 追加预授权
     */
    public static final String TRANSTYPE_ADDPREAUTH = "ADDPREAUTH";
    /***
     * 预授权确认
     */
    public static final String TRANSTYPE_PREAUTHEND = "PREAUTHEND";
    /***
     * 预授权隔日取消
     */
    public static final String TRANSTYPE_PREAUTHVOID = "PREAUTHVOID";
    /***
     * 查询本机交易记录
     */
    public static final String TRANSTYPE_QUERY_TRANS_REC = "QUERY_TRANS_REC";
    /***
     * 获取终端信息
     */
    public static final String TRANSTYPE_TERMINAL_INFO = "TERMINAL_INFO";
    /***
     * 重打印凭条
     */
    public static final String TRANSTYPE_REPRINT = "REPRINT";
}
