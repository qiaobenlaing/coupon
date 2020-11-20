package com.huift.hfq.base.pojo;

public class ICBCResult {
	// 交易成功
	public static final String CODE_SUCCESS = "00";
	// 报文格式有误
	public static final String CODE_01 = "01";
	// 超时
	public static final String CODE_02 = "02";
	// 交易出错
	public static final String CODE_03 = "03";
	// 请先登陆工行mpos收单系统
	public static final String CODE_04 = "04";
	/**
	 * 交易码
	 */
	private String transCode;
	/**
	 * 订单号
	 */
	private String orderNo;
	/**
	 * 交易金额
	 */
	private String amount;
	/**
	 * 返回码
	 */
	private String returnCode;
	/**
	 * 返回信息
	 */
	private String returnMsg;

	private ICBCResult() {
	}

	public ICBCResult(String returnString) {
		if (null == returnString || "".equals(returnString)) {
			return;
		}
		String[] r = returnString.split("\\|");
		this.transCode = r[0];
		if (r[1].startsWith("011")) {
			this.orderNo = r[1].substring(3);
		}
		if (r[2].startsWith("004")) {
			this.amount = r[2].substring(3);
		}
		if (r[4].startsWith("039")) {
			this.returnCode = r[4].substring(3);
		}
		if (r[5].startsWith("999")) {
			this.returnMsg = r[5].substring(3);
		}
	}

	public String getTransCode() {
		return transCode;
	}

	public void setTransCode(String transCode) {
		this.transCode = transCode;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	public String getReturnMsg() {
		return returnMsg;
	}

	public void setReturnMsg(String returnMsg) {
		this.returnMsg = returnMsg;
	}
}
