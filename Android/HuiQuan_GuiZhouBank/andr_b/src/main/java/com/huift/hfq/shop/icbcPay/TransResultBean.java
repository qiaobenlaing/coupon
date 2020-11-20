package com.huift.hfq.shop.icbcPay;

import android.os.Bundle;

import java.util.ArrayList;

/**
 * 工行支付结果实体类
 */
public class TransResultBean {
    // baseResult 基本结果数据
    private long result;// 结果类型 0-成功 1-失败
    private String description;// 交易描述，交易失败时将会输出对应的失败信息描述
    private String transTime;// 交易时间 格式：yyyyMMddHHmmss
    private String custName;// 终端对应的商户名称
    private String custNo;// 终端对应的12位商户号
    private String termNo;// 终端对应的15位终端号
    private long transSequence;// 交易顺序号,该值在同一台终端上保证不重复
    private String rspNo;
    private String transType;//交易类型

    // transResult 交易结果数据。交易成功(result=0)时存在
    private String payType;// 付款方式
    private long amount;// 交易金额，单位：分
    private long retAmount;// 清算金额，交易后台返回的实际扣款金额(受优惠、小费等因素影响)。单位：分
    private long feeAmount;// 小费金额，单位：分
    private String refNo;// 检索参考号（简写）
    private String qrCodeOrder;// 工行订单号(即手机收到支付平台返回的订单号)
    private ArrayList<String> printData;// 打印数据。传入参数PRINT_IN_BANKPAY为false，且收单应用认为部分数据必须打印时。
    private String traceNo;// 流水号

    // extraInfo 交易附加数据。交易成功(result=0)时存在
    private long inputMode;// 交易介质输入方式 1-刷卡 2-插卡 3-挥卡 4-手输卡号 5-二维码
    private String pan;//完整银行卡号,该数据返回受特别权限控制，如果不具备输出敏感信息签名权限将不会输出该信息。
    private String expDate;// 卡片有效期,该数据返回受特别权限控制，如果不具备输出敏感信息签名权限将不会输出该信息。
    private String shildedPan;//屏蔽卡号
    private String cardName;// 银行卡对应的卡种信息
    private String cardIssuer;// 银行卡对应的发卡行名称
    private String cardOrg;// 银行卡对应的卡组织名称
    private long bankReduceAmt;
    private boolean eSignature;
    private boolean reqSignature;
    private String refNoFull;// 检索参考号（完整）
    private long custReduceAmt;

    //设置/获取 基本结果数据 baseResult
    public void setResult(long result) {
        this.result = result;
    }

    public long getResult() {
        return result;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setTransTime(String transTime) {
        this.transTime = transTime;
    }

    public String getTransTime() {
        return transTime;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustNo(String custNo) {
        this.custNo = custNo;
    }

    public String getCustNo() {
        return custNo;
    }

    public void setTermNo(String termNo) {
        this.termNo = termNo;
    }

    public String getTermNo() {
        return termNo;
    }

    public void setTransSequence(long transSequence) {
        this.transSequence = transSequence;
    }

    public long getTransSequence() {
        return transSequence;
    }

    public String getRspNo() {
        return rspNo;
    }

    public void setRspNo(String rspNo) {
        this.rspNo = rspNo;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    //设置/获取 交易结果数据 transResult
    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getPayType() {
        return payType;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public long getAmount() {
        return amount;
    }

    public void setRetAmount(long retAmount) {
        this.retAmount = retAmount;
    }

    public long getRetAmount() {
        return retAmount;
    }

    public void setFeeAmount(long feeAmount) {
        this.feeAmount = feeAmount;
    }

    public long getFeeAmount() {
        return feeAmount;
    }

    public void setPrintData(ArrayList<String> printData) {
        this.printData = printData;
    }

    public ArrayList<String> getPrintData() {
        return printData;
    }

    public void setRefNo(String refNo) {
        this.refNo = refNo;
    }

    public String getRefNo() {
        return refNo;
    }

    public void setQrCodeOrder(String qrCodeOrder) {
        this.qrCodeOrder = qrCodeOrder;
    }

    public String getQrCodeOrder() {
        return qrCodeOrder;
    }

    public String getTraceNo() {
        return traceNo;
    }

    public void setTraceNo(String traceNo) {
        this.traceNo = traceNo;
    }

    //设置/获取 交易附加数据 extraInfo
    public void setInputMode(long inputMode) {
        this.inputMode = inputMode;
    }

    public long getInputMode() {
        return inputMode;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public String getPan() {
        return pan;
    }

    public void setExpDate(String expDate) {
        this.expDate = expDate;
    }

    public String getExpDate() {
        return expDate;
    }

    public void setShildedPan(String shildedPan) {
        this.shildedPan = shildedPan;
    }

    public String getShildedPan() {
        return shildedPan;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardIssuer(String cardIssuer) {
        this.cardIssuer = cardIssuer;
    }

    public String getCardIssuer() {
        return cardIssuer;
    }

    public void setCardOrg(String cardOrg) {
        this.cardOrg = cardOrg;
    }

    public String getCardOrg() {
        return cardOrg;
    }

    public long getBankReduceAmt() {
        return bankReduceAmt;
    }

    public void setBankReduceAmt(long bankReduceAmt) {
        this.bankReduceAmt = bankReduceAmt;
    }

    public boolean iseSignature() {
        return eSignature;
    }

    public void seteSignature(boolean eSignature) {
        this.eSignature = eSignature;
    }

    public boolean isReqSignature() {
        return reqSignature;
    }

    public void setReqSignature(boolean reqSignature) {
        this.reqSignature = reqSignature;
    }

    public String getRefNoFull() {
        return refNoFull;
    }

    public void setRefNoFull(String refNoFull) {
        this.refNoFull = refNoFull;
    }

    public long getCustReduceAmt() {
        return custReduceAmt;
    }

    public void setCustReduceAmt(long custReduceAmt) {
        this.custReduceAmt = custReduceAmt;
    }

    //解析交易结果
    public void analysisResult(Bundle baseResult, Bundle transResult, Bundle extraInfo) {
        if (baseResult != null) {
            for (String keyname : baseResult.keySet()) {
                if (keyname.equals("RESULT")) {
                    result = baseResult.getLong("RESULT", 1); //交易结果 0-成功 1-失败
//                    LogUtils.i("交易结果:" + result);
                } else if (keyname.equals("DESCRIPTION")) {
                    description = baseResult.getString("DESCRIPTION", ""); //交易描述
//                    LogUtils.i("交易描述:" + description);
                } else if (keyname.equals("TRANS_TIME")) {
                    transTime = baseResult.getString("TRANS_TIME", ""); //交易时间 格式：yyyyMMddHHmmss
//                    LogUtils.i("交易时间:" + transTime);
                } else if (keyname.equals("CUST_NAME")) {
                    custName = baseResult.getString("CUST_NAME", ""); //商户名称
//                    LogUtils.i("商户名称:" + custName);
                } else if (keyname.equals("CUST_NO")) {
                    custNo = baseResult.getString("CUST_NO", ""); //商户编号 12位
//                    LogUtils.i("商户号:" + custNo);
                } else if (keyname.equals("TERM_NO")) {
                    termNo = baseResult.getString("TERM_NO", ""); //终端号 15位
//                    LogUtils.i("终端号:" + termNo);
                } else if (keyname.equals("TRANS_SEQUENCE")) {
                    transSequence = baseResult.getLong("TRANS_SEQUENCE", -1);
//                    LogUtils.i("交易顺序号:" + transSequence);
                } else if (keyname.equals("RSP_NO")) {
                    rspNo = baseResult.getString("RSP_NO", "");
//                    LogUtils.i("rspNo:" + rspNo);
                } else if (keyname.equals("TRANS_TYPE")) {
                    transType = baseResult.getString("TRANS_TYPE", "");
//                    LogUtils.i("交易类型:" + transType);
                }
            }
        }

        if (transResult != null) {
            for (String keyname : transResult.keySet()) {
                if (keyname.equals("PAY_TYPE")) {
                    payType = transResult.getString("PAY_TYPE", "");
//                    LogUtils.i("付款方式:" + payType);
                } else if (keyname.equals("AMOUNT")) {
                    amount = transResult.getLong("AMOUNT", 0);
//                    LogUtils.i("交易金额:" + amount);
                } else if (keyname.equals("RET_AMT")) {
                    retAmount = transResult.getLong("RET_AMT", 0);
//                    LogUtils.i("清算金额:" + retAmount);
                } else if (keyname.equals("FEE_AMT")) {
                    feeAmount = transResult.getLong("FEE_AMT", 0);
//                    LogUtils.i("小费金额:" + feeAmount);
                } else if (keyname.equals("PRINT_DATA")) {
                    printData = transResult.getStringArrayList("PRINT_DATA");
//                    LogUtils.i("打印数据:" + printData);
                } else if (keyname.equals("REF_NO")) {
                    refNo = transResult.getString("REF_NO", "");
//                    LogUtils.i("检索参考号:" + refNo);
                } else if (keyname.equals("QRCODE_ORDER")) {
                    qrCodeOrder = transResult.getString("QRCODE_ORDER", "");
//                    LogUtils.i("工行订单号:" + qrCodeOrder);
                } else if (keyname.equals("TRACE_NO")) {
                    traceNo = transResult.getString("TRACE_NO", "");
//                    LogUtils.i("流水号:" + traceNo);
                }
            }
        }

        if (extraInfo != null) {
            for (String keyname : extraInfo.keySet()) {
                if (keyname.equals("INPUT_MODE")) {
                    inputMode = extraInfo.getLong("INPUT_MODE", 0);
//                    LogUtils.i("介质输入方式:" + inputMode);
                } else if (keyname.equals("PAN")) {
                    pan = extraInfo.getString("PAN", "");
//                    LogUtils.i("完整银行卡号:" + pan);
                } else if (keyname.equals("EXP_DATE")) {
                    expDate = extraInfo.getString("EXP_DATE", "");
//                    LogUtils.i("卡有效期:" + expDate);
                } else if (keyname.equals("SHILDED_PAN")) {
                    shildedPan = extraInfo.getString("SHILDED_PAN", "");
//                    LogUtils.i("屏蔽银行卡号:" + shildedPan);
                } else if (keyname.equals("CARD_NAME")) {
                    cardName = extraInfo.getString("CARD_NAME", "");
//                    LogUtils.i("卡种信息:" + cardName);
                } else if (keyname.equals("CARD_ISSUER")) {
                    cardIssuer = extraInfo.getString("CARD_ISSUER", "");
//                    LogUtils.i("发卡行名称:" + cardIssuer);
                } else if (keyname.equals("CARD_ORG")) {
                    cardOrg = extraInfo.getString("CARD_ORG", "");
//                    LogUtils.i("卡组织名称:" + cardOrg);
                } else if (keyname.equals("BANK_REDUCE_AMT")) {
                    bankReduceAmt = extraInfo.getLong("BANK_REDUCE_AMT",0);
//                    LogUtils.i("bankReduceAmt:" + bankReduceAmt);
                } else if (keyname.equals("E_SIGNATURE")) {
                    eSignature = extraInfo.getBoolean("E_SIGNATURE", false);
//                    LogUtils.i("eSignature:" + eSignature);
                } else if (keyname.equals("REQ_SIGNATURE")) {
                    reqSignature = extraInfo.getBoolean("REQ_SIGNATURE", false);
//                    LogUtils.i("reqSignature:" + reqSignature);
                } else if (keyname.equals("REF_NO_FULL")) {
                    refNoFull = extraInfo.getString("REF_NO_FULL", "");
//                    LogUtils.i("完整检索号:" + refNoFull);
                } else if (keyname.equals("CUST_REDUCE_AMT")) {
                    custReduceAmt = extraInfo.getLong("CUST_REDUCE_AMT", 0);
//                    LogUtils.i("custReduceAmt:" + custReduceAmt);
                }
            }
        }
    }
}
