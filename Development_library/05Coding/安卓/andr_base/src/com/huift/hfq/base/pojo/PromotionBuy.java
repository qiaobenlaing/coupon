package com.huift.hfq.base.pojo;



/**
 * 购买活动是  填写订单的实体类
 * @author yingchen
 *
 */
public class PromotionBuy {
	/**
	 * 活动信息
	 */
	private Activitys actInfo;
	
	/**
	 * 红包信息
	 */
	private Bonus userBonusInfo;

	/**
	 * 最小支付金额
	 */
	private Double minRealPay;
	
	/**
	 * 是否受理工行卡
	 */
	private String isAcceptBankCard;
	
	public PromotionBuy() {
		super();
	}

	

	
	
	public PromotionBuy(Activitys actInfo, Bonus userBonusInfo, Double minRealPay, String isAcceptBankCard) {
		super();
		this.actInfo = actInfo;
		this.userBonusInfo = userBonusInfo;
		this.minRealPay = minRealPay;
		this.isAcceptBankCard = isAcceptBankCard;
	}



	public Activitys getActInfo() {
		return actInfo;
	}

	public void setActInfo(Activitys actInfo) {
		this.actInfo = actInfo;
	}

	public Bonus getUserBonusInfo() {
		return userBonusInfo;
	}

	public void setUserBonusInfo(Bonus userBonusInfo) {
		this.userBonusInfo = userBonusInfo;
	}

	
	public Double getMinRealPay() {
		return minRealPay;
	}





	public void setMinRealPay(Double minRealPay) {
		this.minRealPay = minRealPay;
	}





	public String getIsAcceptBankCard() {
		return isAcceptBankCard;
	}





	public void setIsAcceptBankCard(String isAcceptBankCard) {
		this.isAcceptBankCard = isAcceptBankCard;
	}





	@Override
	public String toString() {
		return "PromotionBuy [actInfo=" + actInfo + ", userBonusInfo=" + userBonusInfo + ", minRealPay=" + minRealPay + ", isAcceptBankCard=" + isAcceptBankCard + "]";
	}




	
	
	
}
