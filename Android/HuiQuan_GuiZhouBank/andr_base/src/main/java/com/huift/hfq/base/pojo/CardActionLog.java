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

public class CardActionLog implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	/** 
	 * 主键.
	 */
	private String logCode;

	/** 
	 * 用户编码.
	 */
	private String userCode;

	/** 
	 * 会员卡编码.
	 */
	private String cardCode;

	/** 
	 * 使用情形.
	 */
	private String actionContent;

	/** 
	 * 消费金额，如果存在消费.
	 */
	private Integer consumeAmount;

	/** 
	 * 使用时间.
	 */
	private Date actionTime;

	/** 
	 * 使用地点，一般为商店编码.
	 */
	private String location;

	/** 
	 * 消费记录.
	 */
	private String consumeCode;

	public CardActionLog() {
	}

	public CardActionLog(String logCode) {
		this.logCode = logCode;
	}

	public CardActionLog(String logCode, String userCode, String cardCode,
			String actionContent, Integer consumeAmount, Date actionTime,
			String location, String consumeCode) {
		this.logCode = logCode;
		this.userCode = userCode;
		this.cardCode = cardCode;
		this.actionContent = actionContent;
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
	 * 获取 用户编码. 
	 */
	public String getUserCode() {
		return this.userCode;
	}

	/**
	 * 设置 用户编码. 
	 */
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	/**
	 * 获取 会员卡编码. 
	 */
	public String getCardCode() {
		return this.cardCode;
	}

	/**
	 * 设置 会员卡编码. 
	 */
	public void setCardCode(String cardCode) {
		this.cardCode = cardCode;
	}

	/**
	 * 获取 使用情形. 
	 */
	public String getActionContent() {
		return this.actionContent;
	}

	/**
	 * 设置 使用情形. 
	 */
	public void setActionContent(String actionContent) {
		this.actionContent = actionContent;
	}

	/**
	 * 获取 消费金额，如果存在消费. 
	 */
	public Integer getConsumeAmount() {
		return this.consumeAmount;
	}

	/**
	 * 设置 消费金额，如果存在消费. 
	 */
	public void setConsumeAmount(Integer consumeAmount) {
		this.consumeAmount = consumeAmount;
	}

	/**
	 * 获取 使用时间. 
	 */
	public Date getActionTime() {
		return this.actionTime;
	}

	/**
	 * 设置 使用时间. 
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
	 * 获取 消费记录. 
	 */
	public String getConsumeCode() {
		return this.consumeCode;
	}

	/**
	 * 设置 消费记录. 
	 */
	public void setConsumeCode(String consumeCode) {
		this.consumeCode = consumeCode;
	}

}
