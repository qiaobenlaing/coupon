/**
 *
 * @Author: Jianping Chen
 * @Date: 2015.5.8
 * @Version: 1.0.0
 * @Copyright Suanzi Co.,Ltd. @ 2015
 * 
 */

package cn.suanzi.baomi.base.pojo;

import java.util.List;

public class Shop implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * code
	 */
	private String shopCode;
	
	/**
	 * 店铺装修
	 */
	private ShopDecoration shopDecoration;
	
	/**
	 * 店铺装修的图片
	 */
	private List<ShopDecoration> shopImg;

	/**
	 * 商店ID.
	 */
	private String shopId;
	
	/**
	 * 索引id
	 */
	private int id;

	/**
	 * 商店名称.
	 */
	private String shopName;
	
	/**
	 * 截取后的商家标题
	 */
	private String shopTitle;

	/**
	 * 商店类型，如餐饮，服装等；APP首页将根据各类商店的数量来选择排列次序.
	 */
	private int type;

	/**
	 * 商店子类型，用于细分类型.
	 */
	private int subType;

	/**
	 * 所在国家.
	 */
	private String country;

	/**
	 * 省份.
	 */
	private String province;

	/**
	 * 城市.
	 */
	private String city;

	/**
	 * 区或县（如西湖区）.
	 */
	private String district;

	/**
	 * 具体街道地址.
	 */
	private String street;

	/**
	 * 电话.
	 */
	private String tel;

	/**
	 * 移动电话.
	 */
	private String mobileNbr;

	/**
	 * 店主编码.
	 */
	private String ownerCode;

	/**
	 * 会员数量.
	 */
	private Integer nbrOfMember;

	/**
	 * 经度.
	 */
	private Double longitude;

	/**
	 * 纬度.
	 */
	private Double latitude;

	/**
	 * 商店状态.
	 */
	private Byte status;

	/**
	 * 商家诚信值.
	 */
	private Long creditPoint;

	/**
	 * 被关注量，关注该让的用户数量.
	 */
	private Long followedCount;

	/**
	 * 微信公众号.
	 */
	private String wechatPublic;

	/**
	 * 是否签约用户.
	 */
	private Boolean isContractOn;

	/**
	 * 推广人.
	 */
	private String developerCode;

	/**
	 * 推广人类别.
	 */
	private Integer developerType;

	/**
	 * 是否支持送外卖.
	 */
	private String isOuttake;

	/**
	 * 店铺开门时间.
	 */
	private String open;

	/**
	 * 店铺关门时间.
	 */
	private String close;

	/**
	 * 是否可预定.
	 */
	private String isOrderOn;

	/**
	 * 活动数量上限，一个商家最多同时发起多少个活动.
	 */
	private Integer activityLimited;

	/**
	 * 商店logo(或头像)URL.
	 */
	private String logoUrl;

	/**
	 * 营业执照编码.
	 */
	private String licenseNbr;

	/**
	 * 离用户的距离.
	 */
	private String distance;
	
	/**
	 * 用户可不可以优惠券
	 */
	private int isUserCanGrab;
	
	/**
	 * 有没有会员卡
	 */
	private int isUserHasCard;
	
	/**
	 * 优惠券对应的金额
	 */
	private String insteadPrice;
	
	/**
	 * 优惠券编码
	 */
	private String batchCouponCode;
	
	/**
	 * 打折数额
	 */
	private String discountPercent;
	
	
	/**
	 * 卡的编码
	 */
	private String cardCode;
	
	/**
	 *是否是会员
	 */
	private String hasCard;
	
	/**
	 * 是否关注
	 */
	private String isFollowed;
	
	/**
	 * 首单是否立减
	 */
	private int isFirst;
	
	/**
	 * 人气
	 */
	private int popularity;
	
	/**
	 * 回头客
	 */
	private int repeatCustomers;
	
	/**
	 * 是否有惠圈
	 */
	private int hasCoupon;
	
	/**
	 * 是否有活动 
	 */
	private int hasAct;
	
	/**
	 * 是否有上新
	 */
	private int hasNew;
	
	/**
	 * 是否有工行卡折扣
	 */
	private int hasIcbcDiscount;
	
	/**
	 * 工行卡折扣
	 */
	private String onlinePaymentDiscount;
	
	/**
	 * 工行卡最高折扣金额
	 */
	private String onlinePaymentDiscountUpperLimit;
	
	/** 
	 * 消费金额
	 */
	private String orderAmount;
	
	/** 
	 * 订单编码
	 */
	private String orderCode;
	
	/** 
	 * 订单号
	 */
	private String orderNbr;
	
	/**
	 * 地址的Id 
	 */
	private String userAddressId;
	
	/**
	 * 点餐的备注
	 */
	private String remark;
	
	/**
	 * 餐前，餐后付账的标志
	 */
	private int eatPayType;
	
	/**
	 * 是否和银行签约
	 */
	private int isAcceptBankCard;
	
	/**
	 * 店铺简介
	 */
	private String shortDes;
	
	/**
	 * 是否开启外卖
	 */
	private String isOpenTakeout;
	
	/**
	 * 是否开启堂食
	 */
	private String isOpenEat;
	/**
	 * 上新的图片
	 */
	private List<Image> newProductList;
	
	/**
	 *  是否入驻
	 */
	private String shopStatus; //2 -入驻，1-未入驻
	
	/**
	 * 是否有上新的商家
	 */
	private int isNewShop; 
	
	/**
	 * 判断商家是否为餐饮商铺
	 */
	
	private int isCatering; 
	
	/**
	 * 桌号
	 */
	private String tableNbrSwitch;
	 
	/**
	 * 营业时间
	 */
	private String businessHoursString;
	
	/**
	 * 营业的开始时间
	 */
	private String shopOpeningTime;
	
	/**
	 * 营业结束时间
	 */
	private String shopClosedTime;
	
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
	
	/**
	 * 订单类型
	 */
	private String orderType;
	
	/***
	 * 
	 */
	private String iptTime;
	
	/**
	 * 
	 */
	private String lastEditTime;
	
	/**多个店铺开门关门时间*/
	private List<NewShopTime> businessHours;
	
	/**滚屏图片总数量*/
	private int decImgCount;
	
	/**是否显示支付按钮*/
	private int showPayBtn;
	
	/**是否可以支付*/
	private int ifCanPay;
	/**
	 * 是否营业
	 */
	private int isSuspended;
	
	public Shop() {
	}

	public Shop(String shopCode) {
		this.shopCode = shopCode;
	}
	
	
	
	public Shop(String shopCode, ShopDecoration shopDecoration, List<ShopDecoration> shopImg, String shopId, int id, String shopName, String shopTitle, int type, int subType, String country, String province, String city, String district, String street, String tel, String mobileNbr, String ownerCode, Integer nbrOfMember, Double longitude, Double latitude, Byte status, Long creditPoint, Long followedCount, String wechatPublic, Boolean isContractOn, String developerCode, Integer developerType, String isOuttake, String open, String close, String isOrderOn, Integer activityLimited, String logoUrl, String licenseNbr, String distance, int isUserCanGrab, int isUserHasCard, String insteadPrice, String batchCouponCode, String discountPercent, String cardCode, String hasCard, String isFollowed,
			int isFirst, int popularity, int repeatCustomers, int hasCoupon, int hasAct, int hasNew, int hasIcbcDiscount, String onlinePaymentDiscount, String onlinePaymentDiscountUpperLimit, String orderAmount, String orderCode, String orderNbr, String userAddressId, String remark, int eatPayType, int isAcceptBankCard, String shortDes, String isOpenTakeout, String isOpenEat, List<Image> newProductList, String shopStatus, int isNewShop, int isCatering, String tableNbrSwitch, String businessHoursString, String shopOpeningTime, String shopClosedTime, String deliveryId, String deliveryDistance, String requireMoney, String deliveryFee, String orderType, String iptTime, String lastEditTime, List<NewShopTime> businessHours, int decImgCount, int showPayBtn,int ifCanPay) {
		super();
		this.shopCode = shopCode;
		this.shopDecoration = shopDecoration;
		this.shopImg = shopImg;
		this.shopId = shopId;
		this.id = id;
		this.shopName = shopName;
		this.shopTitle = shopTitle;
		this.type = type;
		this.subType = subType;
		this.country = country;
		this.province = province;
		this.city = city;
		this.district = district;
		this.street = street;
		this.tel = tel;
		this.mobileNbr = mobileNbr;
		this.ownerCode = ownerCode;
		this.nbrOfMember = nbrOfMember;
		this.longitude = longitude;
		this.latitude = latitude;
		this.status = status;
		this.creditPoint = creditPoint;
		this.followedCount = followedCount;
		this.wechatPublic = wechatPublic;
		this.isContractOn = isContractOn;
		this.developerCode = developerCode;
		this.developerType = developerType;
		this.isOuttake = isOuttake;
		this.open = open;
		this.close = close;
		this.isOrderOn = isOrderOn;
		this.activityLimited = activityLimited;
		this.logoUrl = logoUrl;
		this.licenseNbr = licenseNbr;
		this.distance = distance;
		this.isUserCanGrab = isUserCanGrab;
		this.isUserHasCard = isUserHasCard;
		this.insteadPrice = insteadPrice;
		this.batchCouponCode = batchCouponCode;
		this.discountPercent = discountPercent;
		this.cardCode = cardCode;
		this.hasCard = hasCard;
		this.isFollowed = isFollowed;
		this.isFirst = isFirst;
		this.popularity = popularity;
		this.repeatCustomers = repeatCustomers;
		this.hasCoupon = hasCoupon;
		this.hasAct = hasAct;
		this.hasNew = hasNew;
		this.hasIcbcDiscount = hasIcbcDiscount;
		this.onlinePaymentDiscount = onlinePaymentDiscount;
		this.onlinePaymentDiscountUpperLimit = onlinePaymentDiscountUpperLimit;
		this.orderAmount = orderAmount;
		this.orderCode = orderCode;
		this.orderNbr = orderNbr;
		this.userAddressId = userAddressId;
		this.remark = remark;
		this.eatPayType = eatPayType;
		this.isAcceptBankCard = isAcceptBankCard;
		this.shortDes = shortDes;
		this.isOpenTakeout = isOpenTakeout;
		this.isOpenEat = isOpenEat;
		this.newProductList = newProductList;
		this.shopStatus = shopStatus;
		this.isNewShop = isNewShop;
		this.isCatering = isCatering;
		this.tableNbrSwitch = tableNbrSwitch;
		this.businessHoursString = businessHoursString;
		this.shopOpeningTime = shopOpeningTime;
		this.shopClosedTime = shopClosedTime;
		this.deliveryId = deliveryId;
		this.deliveryDistance = deliveryDistance;
		this.requireMoney = requireMoney;
		this.deliveryFee = deliveryFee;
		this.orderType = orderType;
		this.iptTime = iptTime;
		this.lastEditTime = lastEditTime;
		this.businessHours = businessHours;
		this.decImgCount = decImgCount;
		this.showPayBtn = showPayBtn;
		this.ifCanPay = ifCanPay;
	}
	
	public int getIsSuspended() {
		return isSuspended;
	}

	public void setIsSuspended(int isSuspended) {
		this.isSuspended = isSuspended;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public List<ShopDecoration> getShopImg() {
		return shopImg;
	}

	public void setShopImg(List<ShopDecoration> shopImg) {
		this.shopImg = shopImg;
	}

	public String getBusinessHoursString() {
		return businessHoursString;
	}

	public void setBusinessHoursString(String businessHoursString) {
		this.businessHoursString = businessHoursString;
	}

	public int getIsNewShop() {
		return isNewShop;
	}

	public void setIsNewShop(int isNewShop) {
		this.isNewShop = isNewShop;
	}

	public String getShopCode() {
		return shopCode;
	}

	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}

	public ShopDecoration getShopDecoration() {
		return shopDecoration;
	}

	public void setShopDecoration(ShopDecoration shopDecoration) {
		this.shopDecoration = shopDecoration;
	}

	public String getShopId() {
		return shopId;
	}

	public void setShopId(String shopId) {
		this.shopId = shopId;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getShopTitle() {
		return shopTitle;
	}

	public void setShopTitle(String shopTitle) {
		this.shopTitle = shopTitle;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getSubType() {
		return subType;
	}

	public void setSubType(int subType) {
		this.subType = subType;
	}

	public String getShortDes() {
		return shortDes;
	}

	public void setShortDes(String shortDes) {
		this.shortDes = shortDes;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
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

	public String getMobileNbr() {
		return mobileNbr;
	}

	public void setMobileNbr(String mobileNbr) {
		this.mobileNbr = mobileNbr;
	}

	public String getOwnerCode() {
		return ownerCode;
	}

	public void setOwnerCode(String ownerCode) {
		this.ownerCode = ownerCode;
	}

	public Integer getNbrOfMember() {
		return nbrOfMember;
	}

	public void setNbrOfMember(Integer nbrOfMember) {
		this.nbrOfMember = nbrOfMember;
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

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public Long getCreditPoint() {
		return creditPoint;
	}

	public void setCreditPoint(Long creditPoint) {
		this.creditPoint = creditPoint;
	}

	public Long getFollowedCount() {
		return followedCount;
	}

	public void setFollowedCount(Long followedCount) {
		this.followedCount = followedCount;
	}

	public String getWechatPublic() {
		return wechatPublic;
	}

	public void setWechatPublic(String wechatPublic) {
		this.wechatPublic = wechatPublic;
	}

	public Boolean getIsContractOn() {
		return isContractOn;
	}

	public void setIsContractOn(Boolean isContractOn) {
		this.isContractOn = isContractOn;
	}

	public String getDeveloperCode() {
		return developerCode;
	}

	public void setDeveloperCode(String developerCode) {
		this.developerCode = developerCode;
	}

	public Integer getDeveloperType() {
		return developerType;
	}

	public void setDeveloperType(Integer developerType) {
		this.developerType = developerType;
	}

	public String getIsOuttake() {
		return isOuttake;
	}

	public void setIsOuttake(String isOuttake) {
		this.isOuttake = isOuttake;
	}

	public String getIsOrderOn() {
		return isOrderOn;
	}

	public void setIsOrderOn(String isOrderOn) {
		this.isOrderOn = isOrderOn;
	}

	public Integer getActivityLimited() {
		return activityLimited;
	}

	public void setActivityLimited(Integer activityLimited) {
		this.activityLimited = activityLimited;
	}

	public String getLogoUrl() {
		return logoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	public String getLicenseNbr() {
		return licenseNbr;
	}

	public void setLicenseNbr(String licenseNbr) {
		this.licenseNbr = licenseNbr;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public int getIsUserCanGrab() {
		return isUserCanGrab;
	}

	public void setIsUserCanGrab(int isUserCanGrab) {
		this.isUserCanGrab = isUserCanGrab;
	}

	public int getIsUserHasCard() {
		return isUserHasCard;
	}

	public void setIsUserHasCard(int isUserHasCard) {
		this.isUserHasCard = isUserHasCard;
	}

	public String getInsteadPrice() {
		return insteadPrice;
	}

	public void setInsteadPrice(String insteadPrice) {
		this.insteadPrice = insteadPrice;
	}

	public String getBatchCouponCode() {
		return batchCouponCode;
	}

	public void setBatchCouponCode(String batchCouponCode) {
		this.batchCouponCode = batchCouponCode;
	}

	public String getDiscountPercent() {
		return discountPercent;
	}

	public void setDiscountPercent(String discountPercent) {
		this.discountPercent = discountPercent;
	}

	public String getCardCode() {
		return cardCode;
	}

	public void setCardCode(String cardCode) {
		this.cardCode = cardCode;
	}

	public String getHasCard() {
		return hasCard;
	}

	public void setHasCard(String hasCard) {
		this.hasCard = hasCard;
	}

	public String getIsFollowed() {
		return isFollowed;
	}

	public void setIsFollowed(String isFollowed) {
		this.isFollowed = isFollowed;
	}

	public int getIsFirst() {
		return isFirst;
	}

	public void setIsFirst(int isFirst) {
		this.isFirst = isFirst;
	}

	public int getPopularity() {
		return popularity;
	}

	public void setPopularity(int popularity) {
		this.popularity = popularity;
	}

	public int getRepeatCustomers() {
		return repeatCustomers;
	}

	public String getIsOpenTakeout() {
		return isOpenTakeout;
	}

	public void setIsOpenTakeout(String isOpenTakeout) {
		this.isOpenTakeout = isOpenTakeout;
	}

	public String getIsOpenEat() {
		return isOpenEat;
	}

	public void setIsOpenEat(String isOpenEat) {
		this.isOpenEat = isOpenEat;
	}
	public void setRepeatCustomers(int repeatCustomers) {
		this.repeatCustomers = repeatCustomers;
	}

	public int getHasCoupon() {
		return hasCoupon;
	}

	public void setHasCoupon(int hasCoupon) {
		this.hasCoupon = hasCoupon;
	}

	public int getHasAct() {
		return hasAct;
	}

	public void setHasAct(int hasAct) {
		this.hasAct = hasAct;
	}

	public int getHasNew() {
		return hasNew;
	}

	public void setHasNew(int hasNew) {
		this.hasNew = hasNew;
	}

	public int getHasIcbcDiscount() {
		return hasIcbcDiscount;
	}

	public void setHasIcbcDiscount(int hasIcbcDiscount) {
		this.hasIcbcDiscount = hasIcbcDiscount;
	}

	public String getOnlinePaymentDiscount() {
		return onlinePaymentDiscount;
	}

	public void setOnlinePaymentDiscount(String onlinePaymentDiscount) {
		this.onlinePaymentDiscount = onlinePaymentDiscount;
	}

	public String getOnlinePaymentDiscountUpperLimit() {
		return onlinePaymentDiscountUpperLimit;
	}

	public void setOnlinePaymentDiscountUpperLimit(String onlinePaymentDiscountUpperLimit) {
		this.onlinePaymentDiscountUpperLimit = onlinePaymentDiscountUpperLimit;
	}

	public String getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(String orderAmount) {
		this.orderAmount = orderAmount;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getOrderNbr() {
		return orderNbr;
	}

	public void setOrderNbr(String orderNbr) {
		this.orderNbr = orderNbr;
	}

	public String getUserAddressId() {
		return userAddressId;
	}

	public void setUserAddressId(String userAddressId) {
		this.userAddressId = userAddressId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getEatPayType() {
		return eatPayType;
	}

	public void setEatPayType(int eatPayType) {
		this.eatPayType = eatPayType;
	}

	public int getIsAcceptBankCard() {
		return isAcceptBankCard;
	}

	public void setIsAcceptBankCard(int isAcceptBankCard) {
		this.isAcceptBankCard = isAcceptBankCard;
	}

	public List<Image> getNewProductList() {
		return newProductList;
	}

	public void setNewProductList(List<Image> newProductList) {
		this.newProductList = newProductList;
	}

	public String getShopStatus() {
		return shopStatus;
	}

	public void setShopStatus(String shopStatus) {
		this.shopStatus = shopStatus;
	}

	public int getIsCatering() {
		return isCatering;
	}

	public void setIsCatering(int isCatering) {
		this.isCatering = isCatering;
	}

	public String getTableNbrSwitch() {
		return tableNbrSwitch;
	}

	public void setTableNbrSwitch(String tableNbrSwitch) {
		this.tableNbrSwitch = tableNbrSwitch;
	}

	public String getOpen() {
		return open;
	}

	public void setOpen(String open) {
		this.open = open;
	}

	public String getClose() {
		return close;
	}

	public void setClose(String close) {
		this.close = close;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	public String getShopOpeningTime() {
		return shopOpeningTime;
	}

	public void setShopOpeningTime(String shopOpeningTime) {
		this.shopOpeningTime = shopOpeningTime;
	}

	public String getShopClosedTime() {
		return shopClosedTime;
	}

	public void setShopClosedTime(String shopClosedTime) {
		this.shopClosedTime = shopClosedTime;
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

	public String getIptTime() {
		return iptTime;
	}

	public void setIptTime(String iptTime) {
		this.iptTime = iptTime;
	}

	public String getLastEditTime() {
		return lastEditTime;
	}

	public void setLastEditTime(String lastEditTime) {
		this.lastEditTime = lastEditTime;
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

	public int getShowPayBtn() {
		return showPayBtn;
	}

	public void setShowPayBtn(int showPayBtn) {
		this.showPayBtn = showPayBtn;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public int getIfCanPay() {
		return ifCanPay;
	}

	public void setIfCanPay(int ifCanPay) {
		this.ifCanPay = ifCanPay;
	}
	
	
}
