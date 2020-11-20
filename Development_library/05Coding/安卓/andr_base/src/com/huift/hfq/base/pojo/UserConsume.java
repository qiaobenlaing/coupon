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

public class UserConsume implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	/** 
	 * 主键.
	 */
	private String consumeCode;

	/** 
	 * 消费者编码.
	 */
	private String consumerCode;

	/** 
	 * 是否使用了会员卡.
	 */
	private Boolean isCard;

	/** 
	 * 使用了多少张优惠券.
	 */
	private Integer couponUsed;

	/** 
	 * 消费时间.
	 */
	private Date consumeTime;

	/** 
	 * 标识码.
	 */
	private String identityCode;

	/** 
	 * 抵扣金额.
	 */
	private Integer deduction;

	/** 
	 * 实缴金额.
	 */
	private Integer realPay;

	public UserConsume() {
	}

	public UserConsume(String consumeCode) {
		this.consumeCode = consumeCode;
	}

	public UserConsume(String consumeCode, String consumerCode, Boolean isCard,
			Integer couponUsed, Date consumeTime, String identityCode,
			Integer deduction, Integer realPay) {
		this.consumeCode = consumeCode;
		this.consumerCode = consumerCode;
		this.isCard = isCard;
		this.couponUsed = couponUsed;
		this.consumeTime = consumeTime;
		this.identityCode = identityCode;
		this.deduction = deduction;
		this.realPay = realPay;
	}

	/**
	 * 获取 主键. 
	 */
	public String getConsumeCode() {
		return this.consumeCode;
	}

	/**
	 * 设置 主键. 
	 */
	public void setConsumeCode(String consumeCode) {
		this.consumeCode = consumeCode;
	}

	/**
	 * 获取 消费者编码. 
	 */
	public String getConsumerCode() {
		return this.consumerCode;
	}

	/**
	 * 设置 消费者编码. 
	 */
	public void setConsumerCode(String consumerCode) {
		this.consumerCode = consumerCode;
	}

	/**
	 * 获取 是否使用了会员卡. 
	 */
	public Boolean getIsCard() {
		return this.isCard;
	}

	/**
	 * 设置 是否使用了会员卡. 
	 */
	public void setIsCard(Boolean isCard) {
		this.isCard = isCard;
	}

	/**
	 * 获取 使用了多少张优惠券. 
	 */
	public Integer getCouponUsed() {
		return this.couponUsed;
	}

	/**
	 * 设置 使用了多少张优惠券. 
	 */
	public void setCouponUsed(Integer couponUsed) {
		this.couponUsed = couponUsed;
	}

	/**
	 * 获取 消费时间. 
	 */
	public Date getConsumeTime() {
		return this.consumeTime;
	}

	/**
	 * 设置 消费时间. 
	 */
	public void setConsumeTime(Date consumeTime) {
		this.consumeTime = consumeTime;
	}

	/**
	 * 获取 标识码. 
	 */
	public String getIdentityCode() {
		return this.identityCode;
	}

	/**
	 * 设置 标识码. 
	 */
	public void setIdentityCode(String identityCode) {
		this.identityCode = identityCode;
	}

	/**
	 * 获取 抵扣金额. 
	 */
	public Integer getDeduction() {
		return this.deduction;
	}

	/**
	 * 设置 抵扣金额. 
	 */
	public void setDeduction(Integer deduction) {
		this.deduction = deduction;
	}

	/**
	 * 获取 实缴金额. 
	 */
	public Integer getRealPay() {
		return this.realPay;
	}

	/**
	 * 设置 实缴金额. 
	 */
	public void setRealPay(Integer realPay) {
		this.realPay = realPay;
	}

}
