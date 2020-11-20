/**
 *
 * @Author: yanfang.li
 * @Date: 2015.12.14
 * @Version: 1.0.0
 * @Copyright Suanzi Co.,Ltd. @ 2015
 * 
 */
package com.huift.hfq.base.pojo;

import java.util.List;

/**
 *  批次优惠券详情对象
 * @author liyanfang
 */
public class BatchCouponDetail implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 优惠券详情
	 */
	private BatchCoupon batchCouponInfo;
	
	/**
	 * 商家详情
	 */
	private Shop shopInfo;
	
	/**
	 * 我领取的优惠券
	 */
	private List<BatchCoupon> userCouponList;
	
	/**
	 * 异业商家
	 */
	private List<Shop> recomShop;

	public BatchCoupon getBatchCouponInfo() {
		return batchCouponInfo;
	}

	public void setBatchCouponInfo(BatchCoupon batchCouponInfo) {
		this.batchCouponInfo = batchCouponInfo;
	}

	public Shop getShopInfo() {
		return shopInfo;
	}

	public void setShopInfo(Shop shopInfo) {
		this.shopInfo = shopInfo;
	}

	public List<BatchCoupon> getUserCouponList() {
		return userCouponList;
	}

	public void setUserCouponList(List<BatchCoupon> userCouponList) {
		this.userCouponList = userCouponList;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<Shop> getRecomShop() {
		return recomShop;
	}

	public void setRecomShop(List<Shop> recomShop) {
		this.recomShop = recomShop;
	}
	
}
