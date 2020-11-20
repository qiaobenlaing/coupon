package com.huift.hfq.base.pojo;

import java.io.Serializable;

/*****
 * 订单管理的实体类
 * @author qian.zhou
 */
public class OrderListItem implements Serializable {
	
	private String status;//时间
	private String orderCode;
	private String orderTime;
	private String mealNbr;//餐号
	private String orderStatus;
	private String eatPayType;//判断是餐前订单详情还是餐后
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}
	public String getOrderTime() {
		return orderTime;
	}
	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}
	public String getMealNbr() {
		return mealNbr;
	}
	public void setMealNbr(String mealNbr) {
		this.mealNbr = mealNbr;
	}
	public String getEatPayType() {
		return eatPayType;
	}
	public void setEatPayType(String eatPayType) {
		this.eatPayType = eatPayType;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	
	
}
