package com.huift.hfq.base.pojo;

import java.io.Serializable;

/**
 * 开店时间
 * @author qian.zhou
 *
 */
public class ShopTime implements Serializable{

	private static final long serialVersionUID = 1L;
	
	/**
	 * id
	 */
	private int id;
	/**
	 * 开门时间
	 */
	private String openTime;
	
	/**
	 * 关门时间
	 */
	private String cloeTime;
	
	/**
	 * 配送方案ID
	 */
	private String deliveryId;
	
	/**
	 * 配送范围(单位：米)
	 */
	private String deliveryDistance;
	
	/**
	 * 起送价（单位：元）
	 */
	private String requireMoney;
	
	/**
	 * 配送费
	 */
	private String deliveryFee;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getOpenTime() {
		return openTime;
	}

	public void setOpenTime(String openTime) {
		this.openTime = openTime;
	}

	public String getCloeTime() {
		return cloeTime;
	}

	public void setCloeTime(String cloeTime) {
		this.cloeTime = cloeTime;
	}

	public String getDeliveryId() {
		return deliveryId;
	}

	public void setDeliveryId(String deliveryId) {
		this.deliveryId = deliveryId;
	}

	public String getDeliveryDistance() {
		return deliveryDistance;
	}

	public void setDeliveryDistance(String deliveryDistance) {
		this.deliveryDistance = deliveryDistance;
	}

	public String getRequireMoney() {
		return requireMoney;
	}

	public void setRequireMoney(String requireMoney) {
		this.requireMoney = requireMoney;
	}

	public String getDeliveryFee() {
		return deliveryFee;
	}

	public void setDeliveryFee(String deliveryFee) {
		this.deliveryFee = deliveryFee;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		ShopTime other = (ShopTime) obj;
		if (id != other.id) return false;
		return true;
	}
	
	
}
