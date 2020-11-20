package cn.suanzi.baomi.base.pojo;

import java.io.Serializable;

/**
 * 惠圈账本
 * @author ad
 *
 */
public class HqBook implements Serializable{

	/**
	 * 消费金额
	 */
	private String consumptionAmount;
	
	/**
	 * 消费人次
	 */
	private String consumptionCount;
	
	/**
	 * 顾客支付	
	 */
	private String realPayAmount;
	
	/**
	 * 顾客支付未结算
	 */
	private String realPayUnliquidatedAmount;
	
	/**
	 * 平台补贴
	 */
	private String hqSubsidyAmount;
	
	/**
	 * 平台补贴未结算
	 */
	private String hqSubsidyUnliquidatedAmount;
	
	/**
	 * 本店让利
	 */
	private String shopSubsidyAmount;
	
	/**
	 * 顾客退款
	 */
	private String refundAmount;
	
	/**
	 * 已支付未消费
	 */
	private String payedUnconsumedAmount;
	
	/**
	 * 总计收入
	 */
	private String incomeAmount;

	public HqBook(String consumptionAmount, String consumptionCount, String realPayAmount, String realPayUnliquidatedAmount, String hqSubsidyAmount, String hqSubsidyUnliquidatedAmount, String shopSubsidyAmount, String refundAmount, String payedUnconsumedAmount, String incomeAmount) {
		super();
		this.consumptionAmount = consumptionAmount;
		this.consumptionCount = consumptionCount;
		this.realPayAmount = realPayAmount;
		this.realPayUnliquidatedAmount = realPayUnliquidatedAmount;
		this.hqSubsidyAmount = hqSubsidyAmount;
		this.hqSubsidyUnliquidatedAmount = hqSubsidyUnliquidatedAmount;
		this.shopSubsidyAmount = shopSubsidyAmount;
		this.refundAmount = refundAmount;
		this.payedUnconsumedAmount = payedUnconsumedAmount;
		this.incomeAmount = incomeAmount;
	}

	public String getConsumptionAmount() {
		return consumptionAmount;
	}

	public void setConsumptionAmount(String consumptionAmount) {
		this.consumptionAmount = consumptionAmount;
	}

	public String getConsumptionCount() {
		return consumptionCount;
	}

	public void setConsumptionCount(String consumptionCount) {
		this.consumptionCount = consumptionCount;
	}

	public String getRealPayAmount() {
		return realPayAmount;
	}

	public void setRealPayAmount(String realPayAmount) {
		this.realPayAmount = realPayAmount;
	}

	public String getRealPayUnliquidatedAmount() {
		return realPayUnliquidatedAmount;
	}

	public void setRealPayUnliquidatedAmount(String realPayUnliquidatedAmount) {
		this.realPayUnliquidatedAmount = realPayUnliquidatedAmount;
	}

	public String getHqSubsidyAmount() {
		return hqSubsidyAmount;
	}

	public void setHqSubsidyAmount(String hqSubsidyAmount) {
		this.hqSubsidyAmount = hqSubsidyAmount;
	}

	public String getHqSubsidyUnliquidatedAmount() {
		return hqSubsidyUnliquidatedAmount;
	}

	public void setHqSubsidyUnliquidatedAmount(String hqSubsidyUnliquidatedAmount) {
		this.hqSubsidyUnliquidatedAmount = hqSubsidyUnliquidatedAmount;
	}

	public String getShopSubsidyAmount() {
		return shopSubsidyAmount;
	}

	public void setShopSubsidyAmount(String shopSubsidyAmount) {
		this.shopSubsidyAmount = shopSubsidyAmount;
	}

	public String getRefundAmount() {
		return refundAmount;
	}

	public void setRefundAmount(String refundAmount) {
		this.refundAmount = refundAmount;
	}

	public String getPayedUnconsumedAmount() {
		return payedUnconsumedAmount;
	}

	public void setPayedUnconsumedAmount(String payedUnconsumedAmount) {
		this.payedUnconsumedAmount = payedUnconsumedAmount;
	}

	public String getIncomeAmount() {
		return incomeAmount;
	}

	public void setIncomeAmount(String incomeAmount) {
		this.incomeAmount = incomeAmount;
	}
	
}
