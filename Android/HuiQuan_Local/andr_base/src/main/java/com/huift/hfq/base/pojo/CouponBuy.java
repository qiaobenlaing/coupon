package com.huift.hfq.base.pojo;
/**
 * 购买优惠券实体类
 * @author yingchen
 *
 */
public class CouponBuy {
	private BatchCoupon batchCouponInfo;
	private Bonus bonus;
	private double minRealPay;
	private String isAcceptBankCard;
	public CouponBuy() {
		super();
	}
	

	public CouponBuy(BatchCoupon batchCouponInfo, Bonus bonus, double minRealPay, String isAcceptBankCard) {
		super();
		this.batchCouponInfo = batchCouponInfo;
		this.bonus = bonus;
		this.minRealPay = minRealPay;
		this.isAcceptBankCard = isAcceptBankCard;
	}


	public BatchCoupon getBatchCouponInfo() {
		return batchCouponInfo;
	}
	public void setBatchCouponInfo(BatchCoupon batchCouponInfo) {
		this.batchCouponInfo = batchCouponInfo;
	}
	public Bonus getBonus() {
		return bonus;
	}
	public void setBonus(Bonus bonus) {
		this.bonus = bonus;
	}

	public double getMinRealPay() {
		return minRealPay;
	}

	public void setMinRealPay(double minRealPay) {
		this.minRealPay = minRealPay;
	}


	public String getIsAcceptBankCard() {
		return isAcceptBankCard;
	}


	public void setIsAcceptBankCard(String isAcceptBankCard) {
		this.isAcceptBankCard = isAcceptBankCard;
	}
	
	
}
