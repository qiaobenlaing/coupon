package com.huift.hfq.base.pojo;
/**
 * 优惠券的使用信息实体
 * @author yanfang.li
 *
 */
public class CouponCsmList implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;

	/**
	 * 券号
	 */
	private String couponNbr;
	
	/**
	 * 面值
	 */
	private String insteadPrice;
	
	/**
	 * 使用人
	 */
	private String userName;
	
	/**
	 * 消费金额
	 */
	private String consumeAmount;
	
	/**
	 * 使用时间
	 */
	private String consumeTime;

	public String getCouponNbr() {
		return couponNbr;
	}

	public void setCouponNbr(String couponNbr) {
		this.couponNbr = couponNbr;
	}

	public String getInsteadPrice() {
		return insteadPrice;
	}

	public void setInsteadPrice(String insteadPrice) {
		this.insteadPrice = insteadPrice;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getConsumeAmount() {
		return consumeAmount;
	}

	public void setConsumeAmount(String consumeAmount) {
		this.consumeAmount = consumeAmount;
	}

	public String getConsumeTime() {
		return consumeTime;
	}

	public void setConsumeTime(String consumeTime) {
		this.consumeTime = consumeTime;
	}

	
	
}
