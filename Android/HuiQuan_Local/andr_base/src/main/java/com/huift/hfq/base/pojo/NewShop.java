package com.huift.hfq.base.pojo;

import java.util.List;

/**
 * 改版后店铺信息
 * @author yingchen
 *
 */
public class NewShop implements java.io.Serializable {
	/**多个店铺开门关门时间*/
	private List<NewShopTime> businessHours;
	
	/**滚屏图片总数量*/
	private int decImgCount;
	
	/**区县*/
	private String district;
	
	/**是否是会员*/
	private int hasCard;
	
	/**是否是餐饮商铺*/
	private String isCatering;
	
	/**是否关注该商铺*/
	private int isFollowed;
	
	/**外卖是否开通*/
	private String isOrderOn;
	
	/**点餐是否开通*/
	private String isOuttake;
	
	/**经度*/
	private Double longitude;

	/**纬度*/
	private Double latitude;
	
	/**工行卡折扣*/
	private double onlinePaymentDiscount;
	
	/**人气*/
	private int popularity;
	
	/**商户编码*/
	private String shopCode;
	
	/**商铺名称*/
	private String shopName;
	
	/**是否显示支付按钮*/
	private int showPayBtn;
	
	/**地址*/
	private String street;
	
	/**手机*/
	private String tel;
	
	public NewShop() {
		super();
	}

	public NewShop(List<NewShopTime> businessHours, int decImgCount, String district, int hasCard, String isCatering, int isFollowed, String isOrderOn, String isOuttake, Double longitude, Double latitude, double onlinePaymentDiscount, int popularity, String shopCode, String shopName, int showPayBtn, String street, String tel) {
		super();
		this.businessHours = businessHours;
		this.decImgCount = decImgCount;
		this.district = district;
		this.hasCard = hasCard;
		this.isCatering = isCatering;
		this.isFollowed = isFollowed;
		this.isOrderOn = isOrderOn;
		this.isOuttake = isOuttake;
		this.longitude = longitude;
		this.latitude = latitude;
		this.onlinePaymentDiscount = onlinePaymentDiscount;
		this.popularity = popularity;
		this.shopCode = shopCode;
		this.shopName = shopName;
		this.showPayBtn = showPayBtn;
		this.street = street;
		this.tel = tel;
	}

	public List<NewShopTime> getBusinessHours() {
		return businessHours;
	}

	public void setBusinessHours(List<NewShopTime> businessHours) {
		this.businessHours = businessHours;
	}

	public int getDecImgCount() {
		return decImgCount;
	}

	public void setDecImgCount(int decImgCount) {
		this.decImgCount = decImgCount;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public int getHasCard() {
		return hasCard;
	}

	public void setHasCard(int hasCard) {
		this.hasCard = hasCard;
	}

	public String getIsCatering() {
		return isCatering;
	}

	public void setIsCatering(String isCatering) {
		this.isCatering = isCatering;
	}

	public int getIsFollowed() {
		return isFollowed;
	}

	public void setIsFollowed(int isFollowed) {
		this.isFollowed = isFollowed;
	}

	public String getIsOrderOn() {
		return isOrderOn;
	}

	public void setIsOrderOn(String isOrderOn) {
		this.isOrderOn = isOrderOn;
	}

	public String getIsOuttake() {
		return isOuttake;
	}

	public void setIsOuttake(String isOuttake) {
		this.isOuttake = isOuttake;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public double getOnlinePaymentDiscount() {
		return onlinePaymentDiscount;
	}

	public void setOnlinePaymentDiscount(double onlinePaymentDiscount) {
		this.onlinePaymentDiscount = onlinePaymentDiscount;
	}

	public int getPopularity() {
		return popularity;
	}

	public void setPopularity(int popularity) {
		this.popularity = popularity;
	}

	public String getShopCode() {
		return shopCode;
	}

	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public int getShowPayBtn() {
		return showPayBtn;
	}

	public void setShowPayBtn(int showPayBtn) {
		this.showPayBtn = showPayBtn;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}
	
	
	
}
