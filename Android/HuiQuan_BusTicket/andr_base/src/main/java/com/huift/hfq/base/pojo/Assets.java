package com.huift.hfq.base.pojo;

import java.io.Serializable;

/**
 * 我的资产
 * @author liyanfang
 */
public class Assets implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6184945892824746494L;
	
	/**
	 * 惠币说明
	 */
	private String coinIntro;
	
	/**
	 * 用户拥有的惠币
	 */
	private String currCoin;
	
	/**
	 * 用户总红包金额
	 */
	private String userBonus;
	
	/**
	 * 用户拥有的优惠券
	 */
	private String userCouponCount;

	public String getCoinIntro() {
		return coinIntro;
	}

	public void setCoinIntro(String coinIntro) {
		this.coinIntro = coinIntro;
	}

	public String getCurrCoin() {
		return currCoin;
	}

	public void setCurrCoin(String currCoin) {
		this.currCoin = currCoin;
	}

	public String getUserBonus() {
		return userBonus;
	}

	public void setUserBonus(String userBonus) {
		this.userBonus = userBonus;
	}

	public String getUserCouponCount() {
		return userCouponCount;
	}

	public void setUserCouponCount(String userCouponCount) {
		this.userCouponCount = userCouponCount;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
