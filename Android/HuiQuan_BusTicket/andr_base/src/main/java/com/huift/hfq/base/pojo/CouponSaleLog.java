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

public class CouponSaleLog implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	/** 
	 * 主键.
	 */
	private String logCode;

	/** 
	 * 出售方编码.
	 */
	private String sellerCode;

	/** 
	 * 买方编码.
	 */
	private String buyerCode;

	/** 
	 * 优惠券对象.
	 */
	private String couponCode;

	/** 
	 * 出售金额.
	 */
	private Integer price;

	/** 
	 * 交易时间.
	 */
	private Date tradeTime;

	public CouponSaleLog() {
	}

	public CouponSaleLog(String logCode) {
		this.logCode = logCode;
	}

	public CouponSaleLog(String logCode, String sellerCode, String buyerCode,
			String couponCode, Integer price, Date tradeTime) {
		this.logCode = logCode;
		this.sellerCode = sellerCode;
		this.buyerCode = buyerCode;
		this.couponCode = couponCode;
		this.price = price;
		this.tradeTime = tradeTime;
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
	 * 获取 出售方编码. 
	 */
	public String getSellerCode() {
		return this.sellerCode;
	}

	/**
	 * 设置 出售方编码. 
	 */
	public void setSellerCode(String sellerCode) {
		this.sellerCode = sellerCode;
	}

	/**
	 * 获取 买方编码. 
	 */
	public String getBuyerCode() {
		return this.buyerCode;
	}

	/**
	 * 设置 买方编码. 
	 */
	public void setBuyerCode(String buyerCode) {
		this.buyerCode = buyerCode;
	}

	/**
	 * 获取 优惠券对象. 
	 */
	public String getCouponCode() {
		return this.couponCode;
	}

	/**
	 * 设置 优惠券对象. 
	 */
	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}

	/**
	 * 获取 出售金额. 
	 */
	public Integer getPrice() {
		return this.price;
	}

	/**
	 * 设置 出售金额. 
	 */
	public void setPrice(Integer price) {
		this.price = price;
	}

	/**
	 * 获取 交易时间. 
	 */
	public Date getTradeTime() {
		return this.tradeTime;
	}

	/**
	 * 设置 交易时间. 
	 */
	public void setTradeTime(Date tradeTime) {
		this.tradeTime = tradeTime;
	}

}
