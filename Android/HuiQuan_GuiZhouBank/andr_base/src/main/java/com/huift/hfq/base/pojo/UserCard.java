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

public class UserCard implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private String userCardCode;

	/** 
	 * 用户编码.
	 */
	private String userCode;

	/** 
	 * 卡编码.
	 */
	private String cardCode;

	/** 
	 * 卡号.
	 */
	private String cardNbr;

	/** 
	 * 二维码.
	 */
	private String qrCode;

	/** 
	 * 条形码.
	 */
	private String barCode;

	/** 
	 * 申请时间.
	 */
	private Date applyTime;

	/** 
	 * 状态.
	 */
	private Byte status;

	/** 
	 * 积分.
	 */
	private Long point;

	/** 
	 * 储值金额，单位为分.
	 */
	private Long cash;

	/** 
	 * 使用次数.
	 */
	private Integer usedCount;

	/** 
	 * 本张卡的总的折扣金额.
	 */
	private Integer totalDeduction;

	public UserCard() {
	}

	public UserCard(String userCardCode) {
		this.userCardCode = userCardCode;
	}

	public UserCard(String userCardCode, String userCode, String cardCode,
			String cardNbr, String qrCode, String barCode, Date applyTime,
			Byte status, Long point, Long cash, Integer usedCount,
			Integer totalDeduction) {
		this.userCardCode = userCardCode;
		this.userCode = userCode;
		this.cardCode = cardCode;
		this.cardNbr = cardNbr;
		this.qrCode = qrCode;
		this.barCode = barCode;
		this.applyTime = applyTime;
		this.status = status;
		this.point = point;
		this.cash = cash;
		this.usedCount = usedCount;
		this.totalDeduction = totalDeduction;
	}

	/**
	 */
	public String getUserCardCode() {
		return this.userCardCode;
	}

	/**
	 */
	public void setUserCardCode(String userCardCode) {
		this.userCardCode = userCardCode;
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
	 * 获取 卡编码. 
	 */
	public String getCardCode() {
		return this.cardCode;
	}

	/**
	 * 设置 卡编码. 
	 */
	public void setCardCode(String cardCode) {
		this.cardCode = cardCode;
	}

	/**
	 * 获取 卡号. 
	 */
	public String getCardNbr() {
		return this.cardNbr;
	}

	/**
	 * 设置 卡号. 
	 */
	public void setCardNbr(String cardNbr) {
		this.cardNbr = cardNbr;
	}

	/**
	 * 获取 二维码. 
	 */
	public String getQrCode() {
		return this.qrCode;
	}

	/**
	 * 设置 二维码. 
	 */
	public void setQrCode(String qrCode) {
		this.qrCode = qrCode;
	}

	/**
	 * 获取 条形码. 
	 */
	public String getBarCode() {
		return this.barCode;
	}

	/**
	 * 设置 条形码. 
	 */
	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	/**
	 * 获取 申请时间. 
	 */
	public Date getApplyTime() {
		return this.applyTime;
	}

	/**
	 * 设置 申请时间. 
	 */
	public void setApplyTime(Date applyTime) {
		this.applyTime = applyTime;
	}

	/**
	 * 获取 状态. 
	 */
	public Byte getStatus() {
		return this.status;
	}

	/**
	 * 设置 状态. 
	 */
	public void setStatus(Byte status) {
		this.status = status;
	}

	/**
	 * 获取 积分. 
	 */
	public Long getPoint() {
		return this.point;
	}

	/**
	 * 设置 积分. 
	 */
	public void setPoint(Long point) {
		this.point = point;
	}

	/**
	 * 获取 储值金额，单位为分. 
	 */
	public Long getCash() {
		return this.cash;
	}

	/**
	 * 设置 储值金额，单位为分. 
	 */
	public void setCash(Long cash) {
		this.cash = cash;
	}

	/**
	 * 获取 使用次数. 
	 */
	public Integer getUsedCount() {
		return this.usedCount;
	}

	/**
	 * 设置 使用次数. 
	 */
	public void setUsedCount(Integer usedCount) {
		this.usedCount = usedCount;
	}

	/**
	 * 获取 本张卡的总的折扣金额. 
	 */
	public Integer getTotalDeduction() {
		return this.totalDeduction;
	}

	/**
	 * 设置 本张卡的总的折扣金额. 
	 */
	public void setTotalDeduction(Integer totalDeduction) {
		this.totalDeduction = totalDeduction;
	}

}
