package com.huift.hfq.base.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * 订单管理的实体类
 * @author qian.zhou 
 */
public class MyOrderItem implements Serializable {
	
	/**
	 * 时间
	 */
	private String orderTime;
	
	/**
	 * 昵称
	 */
	private String nickName;
	
	/**
	 * 真是姓名
	 */
	private String realName;
	
	/**
	 * 用户log
	 */
	private String avatarUrl;
	
	/**
	 * 实际金额
	 */
	private String realPay;
	
	/**
	 * 商家名称
	 */
	private String shopName;
	
	/**
	 * 支付编码
	 */
	private String consumeCode;
	
	/**
	 * 商家log
	 */
	private String logoUrl;
	/**
	 * 状态
	 */
	private String userConsumeStatus;
	
	/**
	 * 订单编码
	 */
	private String orderNbr;
	
	/**
	 * 消费金额消费金额
	 */
	private String orderAmount;
	
	/**
	 * 有无使用优惠劵
	 */
	private String couponUsed;
	
	/**
	 * 优惠券折扣
	 */
	private String couponDeduction;
	
	/**
	 * 会员卡折扣
	 */
	private String cardDeduction;
	
	/**
	 * 红包折扣
	 */
	private String bonusDeduction;
	
	/**
	 * 总抵扣金额
	 */
	private String deduction;
	
	/**
	 * 优惠券类型
	 */
	private String couponType;
	
	/**
	 * 说明
	 */
	private String function;
	
	/**
	 * 电话
	 */
	private String userMobileNbr;
	
	/**
	 * 支付时间
	 */
	private String consumeTime;
	
	/**
	 * 标题 
	 */
	private String title;
	
	/**
	 * 订单内容
	 */
	private List<OrderListItem> orderList;
	/**
	 * 订单数量
	 */
	private String count;
	
	/**
	 * 兑换券或者代金券
	 */
	private String usedCouponName;
	
	
	public MyOrderItem() {
		super();
	}
	
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	public String getConsumeCode() {
		return consumeCode;
	}
	public void setConsumeCode(String consumeCode) {
		this.consumeCode = consumeCode;
	}
	public String getLogoUrl() {
		return logoUrl;
	}
	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}
	
	public String getOrderTime() {
		return orderTime;
	}
	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public String getAvatarUrl() {
		return avatarUrl;
	}
	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}
	public String getRealPay() {
		return realPay;
	}
	public void setRealPay(String realPay) {
		this.realPay = realPay;
	}
	public String getUserConsumeStatus() {
		return userConsumeStatus;
	}
	public void setUserConsumeStatus(String userConsumeStatus) {
		this.userConsumeStatus = userConsumeStatus;
	}
	public String getOrderNbr() {
		return orderNbr;
	}
	public void setOrderNbr(String orderNbr) {
		this.orderNbr = orderNbr;
	}
	public String getOrderAmount() {
		return orderAmount;
	}
	public void setOrderAmount(String orderAmount) {
		this.orderAmount = orderAmount;
	}
	public String getCouponUsed() {
		return couponUsed;
	}
	public void setCouponUsed(String couponUsed) {
		this.couponUsed = couponUsed;
	}
	public String getCouponDeduction() {
		return couponDeduction;
	}
	public void setCouponDeduction(String couponDeduction) {
		this.couponDeduction = couponDeduction;
	}
	public String getCardDeduction() {
		return cardDeduction;
	}
	public void setCardDeduction(String cardDeduction) {
		this.cardDeduction = cardDeduction;
	}
	public String getBonusDeduction() {
		return bonusDeduction;
	}
	public void setBonusDeduction(String bonusDeduction) {
		this.bonusDeduction = bonusDeduction;
	}
	public String getDeduction() {
		return deduction;
	}
	public void setDeduction(String deduction) {
		this.deduction = deduction;
	}
	public String getCouponType() {
		return couponType;
	}
	public void setCouponType(String couponType) {
		this.couponType = couponType;
	}
	public String getFunction() {
		return function;
	}
	public void setFunction(String function) {
		this.function = function;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public List<OrderListItem> getOrderList() {
		return orderList;
	}
	public void setOrderList(List<OrderListItem> orderList) {
		this.orderList = orderList;
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public String getUserMobileNbr() {
		return userMobileNbr;
	}
	public void setUserMobileNbr(String userMobileNbr) {
		this.userMobileNbr = userMobileNbr;
	}

	public String getConsumeTime() {
		return consumeTime;
	}

	public void setConsumeTime(String consumeTime) {
		this.consumeTime = consumeTime;
	}

	public String getUsedCouponName() {
		return usedCouponName;
	}

	public void setUsedCouponName(String usedCouponName) {
		this.usedCouponName = usedCouponName;
	}
	
}
