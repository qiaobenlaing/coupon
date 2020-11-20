package com.huift.hfq.base.pojo;

import java.io.Serializable;

/**
 * 惠圈账本
 * @author ad
 *
 */
public class HqBook implements Serializable{

	private String hasUsedMoney;
	private String needConfirmMoney;
	private String needConfirm;
	private String couponHasUsed;
	private String realGetMoney;


	public String getHasUsedMoney() {
		return hasUsedMoney;
	}

	public void setHasUsedMoney(String hasUsedMoney) {
		this.hasUsedMoney = hasUsedMoney;
	}

	public String getNeedConfirmMoney() {
		return needConfirmMoney;
	}

	public void setNeedConfirmMoney(String needConfirmMoney) {
		this.needConfirmMoney = needConfirmMoney;
	}

	public String getNeedConfirm() {
		return needConfirm;
	}

	public void setNeedConfirm(String needConfirm) {
		this.needConfirm = needConfirm;
	}

	public String getCouponHasUsed() {
		return couponHasUsed;
	}

	public void setCouponHasUsed(String couponHasUsed) {
		this.couponHasUsed = couponHasUsed;
	}

	public String getRealGetMoney() {
		return realGetMoney;
	}

	public void setRealGetMoney(String realGetMoney) {
		this.realGetMoney = realGetMoney;
	}
}
