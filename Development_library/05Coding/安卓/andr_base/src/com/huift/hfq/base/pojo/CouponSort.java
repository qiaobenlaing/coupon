package com.huift.hfq.base.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * couponlist 对象 
 * @author yanfang.li
 *
 */
public class CouponSort implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 商店发布的优惠券有效 且可领取的
	 */
	private List<BatchCoupon> couponSort;
	
	/**
	 * 分类的名称
	 */
	private String sortName;
	
	/**
	 * 点击的次数
	 */
	private int clickNum;
	
	public int getClickNum() {
		return clickNum;
	}

	public void setClickNum(int clickNum) {
		this.clickNum = clickNum;
	}

	public List<BatchCoupon> getCouponSort() {
		return couponSort;
	}

	public void setCouponSort(List<BatchCoupon> couponSort) {
		this.couponSort = couponSort;
	}

	public String getSortName() {
		return sortName;
	}

	public void setSortName(String sortName) {
		this.sortName = sortName;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	

	
}
