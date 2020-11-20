/**
 *
 * @Author: Jianping Chen
 * @Date: 2015.5.8
 * @Version: 1.0.0
 * @Copyright Suanzi Co.,Ltd. @ 2015
 * 
 */

package com.huift.hfq.base.pojo;

import java.util.Date;

public class BankAccountLocalLog implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	/** 
	 * 主键.
	 */
	private String logCode;

	/** 
	 * 银行账号编码.
	 */
	private String accountCode;

	/** 
	 * 消费内容.
	 */
	private String consumeContent;

	/** 
	 * 消费金额.
	 */
	private Integer consumeAmount;

	/** 
	 * 消费时间.
	 */
	private Date actionTime;

	/** 
	 * 使用地点，一般为商店编码.
	 */
	private String location;

	/** 
	 * 消费记录编码.
	 */
	private String consumeCode;

	public BankAccountLocalLog() {
	}

	public BankAccountLocalLog(String logCode) {
		this.logCode = logCode;
	}

	public BankAccountLocalLog(String logCode, String accountCode,
			String consumeContent, Integer consumeAmount, Date actionTime,
			String location, String consumeCode) {
		this.logCode = logCode;
		this.accountCode = accountCode;
		this.consumeContent = consumeContent;
		this.consumeAmount = consumeAmount;
		this.actionTime = actionTime;
		this.location = location;
		this.consumeCode = consumeCode;
	}

	/**
	 * 获取 主键. 
	 */
	public String getLogCode() {
		return this.logCode;
	}

	/**
	 * 设置 主键. 
	 */
	public void setLogCode(String logCode) {
		this.logCode = logCode;
	}

	/**
	 * 获取 银行账号编码. 
	 */
	public String getAccountCode() {
		return this.accountCode;
	}

	/**
	 * 设置 银行账号编码. 
	 */
	public void setAccountCode(String accountCode) {
		this.accountCode = accountCode;
	}

	/**
	 * 获取 消费内容. 
	 */
	public String getConsumeContent() {
		return this.consumeContent;
	}

	/**
	 * 设置 消费内容. 
	 */
	public void setConsumeContent(String consumeContent) {
		this.consumeContent = consumeContent;
	}

	/**
	 * 获取 消费金额. 
	 */
	public Integer getConsumeAmount() {
		return this.consumeAmount;
	}

	/**
	 * 设置 消费金额. 
	 */
	public void setConsumeAmount(Integer consumeAmount) {
		this.consumeAmount = consumeAmount;
	}

	/**
	 * 获取 消费时间. 
	 */
	public Date getActionTime() {
		return this.actionTime;
	}

	/**
	 * 设置 消费时间. 
	 */
	public void setActionTime(Date actionTime) {
		this.actionTime = actionTime;
	}

	/**
	 * 获取 使用地点，一般为商店编码. 
	 */
	public String getLocation() {
		return this.location;
	}

	/**
	 * 设置 使用地点，一般为商店编码. 
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * 获取 消费记录编码. 
	 */
	public String getConsumeCode() {
		return this.consumeCode;
	}

	/**
	 * 设置 消费记录编码. 
	 */
	public void setConsumeCode(String consumeCode) {
		this.consumeCode = consumeCode;
	}

}
