package com.huift.hfq.base.pojo;

import java.io.Serializable;

/**
 * 短信设置
 * @author liyanfang
 */
public class TextMessage implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7447389893392363440L;

	/**
	 * 编码
	 */
	private String recipientId;
	
	/**
	 * 员工名册
	 */
	private String staffName;
	
	/**
	 * 电话号码
	 */
	private String mobileNbr;

	public String getRecipientId() {
		return recipientId;
	}

	public void setRecipientId(String recipientId) {
		this.recipientId = recipientId;
	}

	public String getStaffName() {
		return staffName;
	}

	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}

	public String getMobileNbr() {
		return mobileNbr;
	}

	public void setMobileNbr(String mobileNbr) {
		this.mobileNbr = mobileNbr;
	}
	
	
}
