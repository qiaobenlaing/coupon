package com.huift.hfq.base.pojo;

/**
 * 工行卡的对象
 * @author yanfang.li
 */
public class Icbc {
	
	/**
	 * 工行卡的折扣
	 */
	private double onlinePaymentDiscount;
	
	/**
	 * 工行卡是否打折
	 */
	private int canDiscount;
	
	/**
	 * 工行卡打折的上线
	 */
	private double onlinePaymentDiscountUpperLimit;

	public double getOnlinePaymentDiscount() {
		return onlinePaymentDiscount;
	}

	public void setOnlinePaymentDiscount(double onlinePaymentDiscount) {
		this.onlinePaymentDiscount = onlinePaymentDiscount;
	}

	public int getCanDiscount() {
		return canDiscount;
	}

	public void setCanDiscount(int canDiscount) {
		this.canDiscount = canDiscount;
	}

	public double getOnlinePaymentDiscountUpperLimit() {
		return onlinePaymentDiscountUpperLimit;
	}

	public void setOnlinePaymentDiscountUpperLimit(double onlinePaymentDiscountUpperLimit) {
		this.onlinePaymentDiscountUpperLimit = onlinePaymentDiscountUpperLimit;
	}
}
