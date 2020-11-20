// ---------------------------------------------------------
// @author    yanfang.li
// @version   1.0.0
// @copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
// ---------------------------------------------------------
package com.huift.hfq.base.pojo;

/**
 * 会员卡统计信息
 * @author yanfang.li
 */
public class CountCard implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;

	/**
	 * 会员总人数 
	 */
	private String nbrOfVip;
	
	/**
	 * 近一个月新增人数
	 */
	private String nbrOfNewVip;
	
	/**
	 * 总消费金额
	 */
	private String amountOfConsumption;
	
	/**
	 * 总积分
	 */
	private String amountOfPoint;
	
	/**
	 * 三个月到期的积分
	 */
	private String amountOfExpiringPoint;

	public String getNbrOfVip() {
		return nbrOfVip;
	}

	public void setNbrOfVip(String nbrOfVip) {
		this.nbrOfVip = nbrOfVip;
	}

	public String getNbrOfNewVip() {
		return nbrOfNewVip;
	}

	public void setNbrOfNewVip(String nbrOfNewVip) {
		this.nbrOfNewVip = nbrOfNewVip;
	}

	public String getAmountOfConsumption() {
		return amountOfConsumption;
	}

	public void setAmountOfConsumption(String amountOfConsumption) {
		this.amountOfConsumption = amountOfConsumption;
	}

	public String getAmountOfPoint() {
		return amountOfPoint;
	}

	public void setAmountOfPoint(String amountOfPoint) {
		this.amountOfPoint = amountOfPoint;
	}

	public String getAmountOfExpiringPoint() {
		return amountOfExpiringPoint;
	}

	public void setAmountOfExpiringPoint(String amountOfExpiringPoint) {
		this.amountOfExpiringPoint = amountOfExpiringPoint;
	}
	
}
