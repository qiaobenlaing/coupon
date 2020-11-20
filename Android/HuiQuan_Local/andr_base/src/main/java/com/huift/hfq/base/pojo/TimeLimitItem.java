package com.huift.hfq.base.pojo;

public class TimeLimitItem implements java.io.Serializable  {

	/**
	 * 优惠券编码
	 */
	private String batchCouponCode;
	
	/**
	 * 优惠券名称
	 */
	private String couponName;
	
	/**
	 * 剩余数量
	 */
	private String restCouponNbr;
	
	/**
	 * 抵用金额
	 */
	private String insteadPrice;
	
	/**
	 * 打折数额*
	 */
	private String discountPercent;
	
	/**
	 * 达到多少金额可用
	 */
	private String availablePrice;
	
	/**
	 * 优惠券总量
	 */
	private String totalVolume;
	
	/**
	 * 优惠券开始使用时间
	 */
	private String startUsingTime;
	
	/**
	 * 图片
	 */
	private String url;
	
	/**
	 * 商店编码
	 */
	private String shopCode;
	
	/**
	 * 商户名称
	 */
	private String shopName;
	
	/**
	 * 距离
	 */
	private String distance;
	
	/**
	 * 失效时间
	 */
	private String expireTime;
	
	/**
	 * 商家联系电话
	 */
	private String mobileNbr;
	
	/**
	 * 商家地址
	 */
	private String street;
	
	/**
	 * 用户是否领
	 */
	private String  isMyReceived;
	
	/**
	 * 用户领了多少张
	 */
	private String countMyReceived;
	
	/**
	 * 使用范围
	 */
	private String exRuleDes;
	
	/**
	 * 使用说明
	 */
	private String remark;

	/**
	 * 每日开始时间
	 */
	private String dayStartUsingTime;
	
	/**
	 * 每日结束时间
	 * @return
	 */
	private String dayEndUsingTime;
	
	/**
	 * 商家log
	 */
	private String logoUrl;
	

	public TimeLimitItem() {
		super();
	}
	
	


	public TimeLimitItem(String batchCouponCode, String couponName, String restCouponNbr, String insteadPrice,
			String discountPercent, String availablePrice, String totalVolume, String startUsingTime, String url,
			String shopCode, String shopName, String distance, String expireTime, String mobileNbr, String street,
			String isMyReceived, String countMyReceived, String exRuleDes, String remark, String dayStartUsingTime,
			String dayEndUsingTime, String logoUrl) {
		super();
		this.batchCouponCode = batchCouponCode;
		this.couponName = couponName;
		this.restCouponNbr = restCouponNbr;
		this.insteadPrice = insteadPrice;
		this.discountPercent = discountPercent;
		this.availablePrice = availablePrice;
		this.totalVolume = totalVolume;
		this.startUsingTime = startUsingTime;
		this.url = url;
		this.shopCode = shopCode;
		this.shopName = shopName;
		this.distance = distance;
		this.expireTime = expireTime;
		this.mobileNbr = mobileNbr;
		this.street = street;
		this.isMyReceived = isMyReceived;
		this.countMyReceived = countMyReceived;
		this.exRuleDes = exRuleDes;
		this.remark = remark;
		this.dayStartUsingTime = dayStartUsingTime;
		this.dayEndUsingTime = dayEndUsingTime;
		this.logoUrl = logoUrl;
	}








	public String getCouponName() {
		return couponName;
	}



	public void setCouponName(String couponName) {
		this.couponName = couponName;
	}



	public String getRestCouponNbr() {
		return restCouponNbr;
	}


	public void setRestCouponNbr(String restCouponNbr) {
		this.restCouponNbr = restCouponNbr;
	}



	public String getDiscountPercent() {
		return discountPercent;
	}



	public void setDiscountPercent(String discountPercent) {
		this.discountPercent = discountPercent;
	}


	public String getAvailablePrice() {
		return availablePrice;
	}


	public void setAvailablePrice(String availablePrice) {
		this.availablePrice = availablePrice;
	}


	public String getTotalVolume() {
		return totalVolume;
	}


	public void setTotalVolume(String totalVolume) {
		this.totalVolume = totalVolume;
	}

	public String getStartUsingTime() {
		return startUsingTime;
	}

	public void setStartUsingTime(String startUsingTime) {
		this.startUsingTime = startUsingTime;
	}


	

	public String getDistance() {
		return distance;
	}



	public void setDistance(String distance) {
		this.distance = distance;
	}



	public String getBatchCouponCode() {
		return batchCouponCode;
	}

	public void setBatchCouponCode(String batchCouponCode) {
		this.batchCouponCode = batchCouponCode;
	}

	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}


	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getInsteadPrice() {
		return insteadPrice;
	}

	public void setInsteadPrice(String insteadPrice) {
		this.insteadPrice = insteadPrice;
	}

	public String getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(String expireTime) {
		this.expireTime = expireTime;
	}

	public String getMobileNbr() {
		return mobileNbr;
	}

	public void setMobileNbr(String mobileNbr) {
		this.mobileNbr = mobileNbr;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}




	public String getIsMyReceived() {
		return isMyReceived;
	}




	public void setIsMyReceived(String isMyReceived) {
		this.isMyReceived = isMyReceived;
	}




	public String getCountMyReceived() {
		return countMyReceived;
	}


	public void setCountMyReceived(String countMyReceived) {
		this.countMyReceived = countMyReceived;
	}



	public String getShopCode() {
		return shopCode;
	}



	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}


	public String getExRuleDes() {
		return exRuleDes;
	}



	public void setExRuleDes(String exRuleDes) {
		this.exRuleDes = exRuleDes;
	}



	public String getRemark() {
		return remark;
	}



	public void setRemark(String remark) {
		this.remark = remark;
	}



	public String getDayStartUsingTime() {
		return dayStartUsingTime;
	}



	public void setDayStartUsingTime(String dayStartUsingTime) {
		this.dayStartUsingTime = dayStartUsingTime;
	}



	public String getDayEndUsingTime() {
		return dayEndUsingTime;
	}



	public void setDayEndUsingTime(String dayEndUsingTime) {
		this.dayEndUsingTime = dayEndUsingTime;
	}




	public String getLogoUrl() {
		return logoUrl;
	}




	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	
}
