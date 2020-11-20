package cn.suanzi.baomi.base.pojo;

import java.io.Serializable;

/**
 * 账单的列表
 * @author ad
 *
 */
public class Bill implements Serializable{

	/**
	 * 用户头像
	 */
	private String avatarUrl;
	
	/**
	 * 用户昵称
	 */
	private String nickName;
	
	/**
	 * 手机号后四位
	 */
	private String mobileNbr;
	
	/**
	 * 消费次数
	 */
	private String consumptionNbr;
	
	/**
	 * 消费金额
	 */
	private String consumptionAmount;
	
	/**
	 * 支付金额
	 */
	private String payAmount;
	
	/**
	 * 优惠金额
	 */
	private String discountAmount;
	
	/**
	 * 最后消费时间
	 */
	private String lastConsumeTime;
	
	/**
	 * 退款金额
	 */
	private String refundAmount;
	
	/**
	 * 订单号
	 */
	private String orderNbr;
	
	/**
	 * 消费时间
	 */
	private String consumptionTime;
	
	/**
	 * 退款时间
	 */
	private String refundTime;
	
	/**
	 * 清算开始日期
	 */
	private String startDate;
	
	/**
	 * 清算结束日期
	 */
	private String endDate;
	
	/**
	 * 补贴金额
	 */
	private String subsidyAmount;
	
	/**
	 * 订单编码
	 */
	private String consumeCode;
	
	


	public Bill(String avatarUrl, String nickName, String mobileNbr, String consumptionNbr, String consumptionAmount, String payAmount, String discountAmount, String lastConsumeTime, String refundAmount, String orderNbr, String consumptionTime, String refundTime, String startDate, String endDate, String subsidyAmount, String consumeCode) {
		super();
		this.avatarUrl = avatarUrl;
		this.nickName = nickName;
		this.mobileNbr = mobileNbr;
		this.consumptionNbr = consumptionNbr;
		this.consumptionAmount = consumptionAmount;
		this.payAmount = payAmount;
		this.discountAmount = discountAmount;
		this.lastConsumeTime = lastConsumeTime;
		this.refundAmount = refundAmount;
		this.orderNbr = orderNbr;
		this.consumptionTime = consumptionTime;
		this.refundTime = refundTime;
		this.startDate = startDate;
		this.endDate = endDate;
		this.subsidyAmount = subsidyAmount;
		this.consumeCode = consumeCode;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getMobileNbr() {
		return mobileNbr;
	}

	public void setMobileNbr(String mobileNbr) {
		this.mobileNbr = mobileNbr;
	}

	public String getConsumptionNbr() {
		return consumptionNbr;
	}

	public void setConsumptionNbr(String consumptionNbr) {
		this.consumptionNbr = consumptionNbr;
	}

	public String getConsumptionAmount() {
		return consumptionAmount;
	}

	public void setConsumptionAmount(String consumptionAmount) {
		this.consumptionAmount = consumptionAmount;
	}

	public String getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(String payAmount) {
		this.payAmount = payAmount;
	}

	public String getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(String discountAmount) {
		this.discountAmount = discountAmount;
	}

	public String getLastConsumeTime() {
		return lastConsumeTime;
	}

	public String getRefundAmount() {
		return refundAmount;
	}

	public void setRefundAmount(String refundAmount) {
		this.refundAmount = refundAmount;
	}

	public String getOrderNbr() {
		return orderNbr;
	}

	public void setOrderNbr(String orderNbr) {
		this.orderNbr = orderNbr;
	}

	public String getConsumptionTime() {
		return consumptionTime;
	}

	public void setConsumptionTime(String consumptionTime) {
		this.consumptionTime = consumptionTime;
	}

	public String getRefundTime() {
		return refundTime;
	}

	public void setRefundTime(String refundTime) {
		this.refundTime = refundTime;
	}

	public void setLastConsumeTime(String lastConsumeTime) {
		this.lastConsumeTime = lastConsumeTime;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getSubsidyAmount() {
		return subsidyAmount;
	}

	public void setSubsidyAmount(String subsidyAmount) {
		this.subsidyAmount = subsidyAmount;
	}

	public String getConsumeCode() {
		return consumeCode;
	}

	public void setConsumeCode(String consumeCode) {
		this.consumeCode = consumeCode;
	}
	
	
	
	
}
