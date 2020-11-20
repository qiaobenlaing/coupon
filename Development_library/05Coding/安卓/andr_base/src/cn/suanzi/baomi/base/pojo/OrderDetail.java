package cn.suanzi.baomi.base.pojo;

import java.io.Serializable;

/**
 * 订单详情
 * @author ad
 */
public class OrderDetail implements Serializable{
	
	private static final long serialVersionUID = 1L;
	/**
	 * userCode
	 */
	private String userCode;
	
	/**
	 * 店铺名称
	 */
	private String shopName;
	
	/**
	 * 订单编号
	 */
	private String orderNbr;
	
	/**
	 * 订单产生时间
	 */
	private String consumeTime;
	
	/**
	 * 消费金额
	 */
	private String orderAmount;
	
	/**
	 * 实缴金额
	 */
	private String realPay;
	
	/**
	 * 优惠券使用数量
	 */
	private String couponUsed;
	
	/**
	 * 优惠劵抵扣金额
	 */
	private String couponDeduction;
	
	/**
	 * 会员卡抵扣金额
	 */
	private String cardDeduction;
	
	/**
	 * 商家红包抵扣金
	 */
	private String shopBonusDeduction;
	
	/**
	 * 平台红包抵扣金额
	 */
	private String platBonusDeduction;
	
	/**
	 * 总抵扣金额
	 */
	private String deduction;
	
	/**
	 * 优惠劵类型
	 */
	private String couponType;
	
	/**
	 * 银行卡抵扣
	 */
	private String bankCardDeduction;
	
	/**
	 * 抵扣金额
	 */
	private String insteadPrice;
	
	/**
	 * 满多少元可以使用
	 */
	private String availablePrice;
	
	/**
	 * 折扣
	 */
	private String discountPercent;
	
	/**
	 * 优惠劵可以做什么
	 */
	private String function;
	
	/**
	 * 用户昵称
	 */
	private String nickName;
	
	/**
	 * 商店编码
	 */
	private String shopCode;
	
	/**
	 * 首单立减
	 */
	private String firstDeduction;
	
	/**
	 * 订单状态
	 */
	private String status;
	
	/**
	 * 电话号码
	 */
	private String userMobileNbr;
	
	/**
	 * 头像
	 */
	private String avatarUrl;
	
	/**
	 * 支付类型
	 */
	private String payType;
	
	/**
	 * 不参与优惠的金额
	 */
	private String noDiscountPrice;
	
	/**
	 * 优惠券批次
	 */
	private String batchNbr;
	
	/**
	 * 优惠券价格
	 */
	private String payPrice;
	
	/**
	 * 兑换码
	 */
	private String couponCode;
	
	
	public String getUserCode() {
		return userCode;
	}
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	public String getOrderNbr() {
		return orderNbr;
	}
	public void setOrderNbr(String orderNbr) {
		this.orderNbr = orderNbr;
	}
	public String getConsumeTime() {
		return consumeTime;
	}
	public void setConsumeTime(String consumeTime) {
		this.consumeTime = consumeTime;
	}
	public String getOrderAmount() {
		return orderAmount;
	}
	public void setOrderAmount(String orderAmount) {
		this.orderAmount = orderAmount;
	}
	public String getRealPay() {
		return realPay;
	}
	public void setRealPay(String realPay) {
		this.realPay = realPay;
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
	public String getShopBonusDeduction() {
		return shopBonusDeduction;
	}
	public void setShopBonusDeduction(String shopBonusDeduction) {
		this.shopBonusDeduction = shopBonusDeduction;
	}
	public String getPlatBonusDeduction() {
		return platBonusDeduction;
	}
	public void setPlatBonusDeduction(String platBonusDeduction) {
		this.platBonusDeduction = platBonusDeduction;
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
	public String getBankCardDeduction() {
		return bankCardDeduction;
	}
	public void setBankCardDeduction(String bankCardDeduction) {
		this.bankCardDeduction = bankCardDeduction;
	}
	public String getInsteadPrice() {
		return insteadPrice;
	}
	public void setInsteadPrice(String insteadPrice) {
		this.insteadPrice = insteadPrice;
	}
	public String getAvailablePrice() {
		return availablePrice;
	}
	public void setAvailablePrice(String availablePrice) {
		this.availablePrice = availablePrice;
	}
	public String getDiscountPercent() {
		return discountPercent;
	}
	public void setDiscountPercent(String discountPercent) {
		this.discountPercent = discountPercent;
	}
	public String getFunction() {
		return function;
	}
	public void setFunction(String function) {
		this.function = function;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getShopCode() {
		return shopCode;
	}
	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}
	public String getFirstDeduction() {
		return firstDeduction;
	}
	public void setFirstDeduction(String firstDeduction) {
		this.firstDeduction = firstDeduction;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getUserMobileNbr() {
		return userMobileNbr;
	}
	public void setUserMobileNbr(String userMobileNbr) {
		this.userMobileNbr = userMobileNbr;
	}
	public String getAvatarUrl() {
		return avatarUrl;
	}
	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public String getNoDiscountPrice() {
		return noDiscountPrice;
	}
	public void setNoDiscountPrice(String noDiscountPrice) {
		this.noDiscountPrice = noDiscountPrice;
	}
	public String getBatchNbr() {
		return batchNbr;
	}
	public void setBatchNbr(String batchNbr) {
		this.batchNbr = batchNbr;
	}
	public String getPayPrice() {
		return payPrice;
	}
	public void setPayPrice(String payPrice) {
		this.payPrice = payPrice;
	}
	public String getCouponCode() {
		return couponCode;
	}
	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}
	
}
