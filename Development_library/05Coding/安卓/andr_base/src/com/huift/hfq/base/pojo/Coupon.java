package com.huift.hfq.base.pojo;

import java.io.Serializable;

public class Coupon implements Serializable{

	/**
	 * 优惠券编码
	 */
	private String userCouponCode;
	/**
	 * 优惠券编码
	 */
	private String batchCouponCode;
	/**
	 * 优惠券名称
	 */
	private String couponName;
	/**
	 * 所属行业
	 */
	private String industryCategory;

	/**
	 * 抵用金额
	 */
	private String insteadPrice;

	/**
	 * 达到多少金额可用
	 */
	private String availablePrice;

	/**
	 * 最后领取时间
	 */
	private String endTakingTime;
	
	/**
	 * 最后使用日期
	 */
	private String expireTime;
	
	/**
	 * 创建时间
	 */
	private String createTime;
	
	/**
	 * 打折数额
	 */
	private String discountPercent;
	
	/**
	 * 是否拥有优惠券
	 */
	private String hasCoupon;
	
	/**
	 * 优惠券的类型
	 */
	private String couponType;
	
	/**
	 * 扩展规则
	 */
	private String exRuleDes;

	/**
	 * 可以干嘛
	 */
	private String function;
	
	/**
	 *  买单的时候 判断优惠券什么时候被选中
	 */
	private Integer selectFlag;
	
	/**
	 * 买单时优惠折扣和抵扣的金额
	 */
	private double coupouPrice;
	
	/**
	 * 优惠券数量
	 * @return
	 */
	private String useNbr;
	
	/**
	 * 优惠券描述
	 * @return
	 */
	private String couponString;
	
	/**
	 * 优惠券抵扣金额
	 * @return
	 */
	private String couponInsteadPrice;
	
	/**
	 * 订单优惠券编码
	 * @return
	 */
	private String orderCouponCode;
	
	/**
	 * 优惠券验证码
	 * @return
	 */
	private String couponCode;
	
	/**
	 * 状态
	 * @return
	 */
	private String status;
	
	/**
	 * 优惠券价格
	 * @return
	 */
	private String payPrice;
	
	/**
	 * 开始使用日期
	 * @return
	 */
	private String startUsingTime;
	
	/**
	 * 每日开始使用时间
	 * @return
	 */
	private String dayStartUsingTime;
	
	/**
	 * 每日结束使用时间
	 * @return
	 */
	private String dayEndUsingTime;
	
	/**
	 * 使用说明
	 * @return
	 */
	private String remark;
	
	/**
	 * 用户编码
	 * @return
	 */
	private String userCode;
	
	/**
	 * 是否过期
	 * @return
	 */
	private String isExpire;
	
	public double getCoupouPrice() {
		return coupouPrice;
	}

	public void setCoupouPrice(double coupouPrice) {
		this.coupouPrice = coupouPrice;
	}

	public String getFunction() {
		return function;
	}

	public void setFunction(String function) {
		this.function = function;
	}

	public String getExRuleDes() {
		return exRuleDes;
	}

	public void setExRuleDes(String exRuleDes) {
		this.exRuleDes = exRuleDes;
	}

	public String getCouponType() {
		return couponType;
	}

	public void setCouponType(String couponType) {
		this.couponType = couponType;
	}

	public String getHasCoupon() {
		return hasCoupon;
	}

	public void setHasCoupon(String hasCoupon) {
		this.hasCoupon = hasCoupon;
	}

	public String getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(String expireTime) {
		this.expireTime = expireTime;
	}

	public String getDiscountPercent() {
		return discountPercent;
	}

	public void setDiscountPercent(String discountPercent) {
		this.discountPercent = discountPercent;
	}

	public String getIndustryCategory() {
		return industryCategory;
	}

	public void setIndustryCategory(String industryCategory) {
		this.industryCategory = industryCategory;
	}

	public String getCouponName() {
		return couponName;
	}

	public void setCouponName(String couponName) {
		this.couponName = couponName;
	}

	public String getInsteadPrice() {
		return insteadPrice;
	}

	public void setInsteadPrice(String insteadPrice) {
		this.insteadPrice = insteadPrice;
	}

	public String getEndTakingTime() {
		return endTakingTime;
	}

	public void setEndTakingTime(String endTakingTime) {
		this.endTakingTime = endTakingTime;
	}

	public String getAvailablePrice() {
		return availablePrice;
	}

	public void setAvailablePrice(String availablePrice) {
		this.availablePrice = availablePrice;
	}

	public String getBatchCouponCode() {
		return batchCouponCode;
	}

	public void setBatchCouponCode(String batchCouponCode) {
		this.batchCouponCode = batchCouponCode;
	}

	public String getUserCouponCode() {
		return userCouponCode;
	}

	public void setUserCouponCode(String userCouponCode) {
		this.userCouponCode = userCouponCode;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public Integer getSelectFlag() {
		return selectFlag;
	}

	public void setSelectFlag(Integer selectFlag) {
		this.selectFlag = selectFlag;
	}

	public String getUseNbr() {
		return useNbr;
	}

	public void setUseNbr(String useNbr) {
		this.useNbr = useNbr;
	}

	public String getCouponString() {
		return couponString;
	}

	public void setCouponString(String couponString) {
		this.couponString = couponString;
	}

	public String getCouponInsteadPrice() {
		return couponInsteadPrice;
	}

	public void setCouponInsteadPrice(String couponInsteadPrice) {
		this.couponInsteadPrice = couponInsteadPrice;
	}

	public String getOrderCouponCode() {
		return orderCouponCode;
	}

	public void setOrderCouponCode(String orderCouponCode) {
		this.orderCouponCode = orderCouponCode;
	}

	public String getCouponCode() {
		return couponCode;
	}

	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPayPrice() {
		return payPrice;
	}

	public void setPayPrice(String payPrice) {
		this.payPrice = payPrice;
	}

	public String getStartUsingTime() {
		return startUsingTime;
	}

	public void setStartUsingTime(String startUsingTime) {
		this.startUsingTime = startUsingTime;
	}

	public String getDayStartUsingTime() {
		return dayStartUsingTime;
	}

	public void setDayStartUsingTime(String dayStartUsingTime) {
		this.dayStartUsingTime = dayStartUsingTime;
	}

	public String getDayEndUsingTime() {
		return dayEndUsingTime;
	}

	public void setDayEndUsingTime(String dayEndUsingTime) {
		this.dayEndUsingTime = dayEndUsingTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getIsExpire() {
		return isExpire;
	}

	public void setIsExpire(String isExpire) {
		this.isExpire = isExpire;
	}
}
