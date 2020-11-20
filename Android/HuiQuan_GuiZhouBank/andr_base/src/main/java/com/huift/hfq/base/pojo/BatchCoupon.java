/**
 *
 * @Author: Jianping Chen
 * @Date: 2015.5.8
 * @Version: 1.0.0
 * @Copyright Suanzi Co.,Ltd. @ 2015
 * 
 */

package com.huift.hfq.base.pojo;

import java.util.List;

import android.util.Log;

public class BatchCoupon implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	/** 
	 * 主键.
	 */
	private String batchCouponCode;
	
	/** 
	 * 发行批次序号.
	 */
	private String batchNbr;
	
	/**
	 * 是否满就送
	 */
	private String isSend;
	
	/**
	 * 满就送的金额
	 */
	private String sendRequired;
	
	/**
	 * 每张券可以干什么  实物券和体验券
	 */
	private String function;
	
	/** 
	 * 优惠券名称.
	 */
	private String couponName;
	
	/** 
	 * 优惠券类型，同一门类下可能分为不同等级的优惠券.
	 */
	private String couponType;
	
	/**
	 * 优惠券类型对应的名称
	 */
	private String couponTypeName;

	/** 
	 * 同一类型下优惠券的排序关系.
	 */
	private Integer couponSubSeq;

	/** 
	 * 创建日期.
	 */
	private String createTime;

	/** 
	 * 创建者.
	 */
	private String creatorCode;

	/** 
	 * 归属于哪个商店.
	 */
	private String shopCode;

	/** 
	 * 剩余数量.
	 */
	private Integer remaining;

	/** 
	 * 发行总量.
	 */
	private int totalVolume;

	/** 
	 * 先到先得模式，设置优惠券为前N张有效。当N为-1时，表示不限制.
	 */
	private int validateForN;

	/** 
	 * 备注，优惠券的一些说明，如实物优惠券对实物的描述，以及体验券说明可写在这里.
	 */
	private String remark;

	/** 
	 * 背景样式.
	 */
	private String bgCode;

	/** 
	 * 优惠券失效时间.
	 */
	private String expireTime;


	/** 
	 * 最后可领取日期.
	 */
	private String endTakingTime;
	
	/**
	 * 优惠券开始领用日期
	 */
	private String startTakingTime;

	/** 
	 * 优惠券可以开始使用的日期.
	 */
	private String startUsingTime;

	/** 
	 * 打折数额.
	 */
	private String discountPercent;

	/** 
	 * 所属行业.
	 */
	private String industryCategory;

	/** 
	 * 抵用金额.
	 */
	private String insteadPrice;

	/** 
	 * 是否消费后才可以领取.
	 */
	private int isConsumeRequired;
	
	/**
	 * 消费多少才可以领取优惠券
	 */
	private String canTakePrice;

	/**
	 * 达到多少可用
	 */
	private String availablePrice;

	/** 
	 * 可用（优惠券）上限数量.
	 */
	private String limitedNbr;

	/** 
	 * 每人可领数量.
	 */
	private String nbrPerPerson;

	/** 
	 * 优惠券的扩展使用规则列表，若无，字段为NULL或空；若有，关联至CouponRule表，内容为CouponRule表的主键，以JSON格式存储.
	 */
	private String exRuleDes;

	/** 
	 * 是否需要关联银行卡.
	 */
	private Boolean isBankCardRequired;

	/** 
	 * 要求会员等级，如果值为0则不需要.
	 */
	private Byte memberLvl;

	/** 
	 * 可使用的阶段，如本次消费即可使用（0），或下次消费才可以使用（1），如果判断为下次消费才可使用，由于消费记录不一定会生成，所以如果设置为下一次消费才可使用，请以时间间隔判断：用户的领用优惠券后1小时认为用户是在下次消费中使用了。.
	 */
	private Byte avaliableStage;
	
	/**
	 * 商家电话
	 */
	private String mobileNbr;
	
	/**
	 * 商家地址
	 */
	private String street;
	
	/**
	 * 商家logo
	 */
	private String logoUrl;
	
	/**
	 * 商家名称
	 */
	private String shopName;
	
	/**
	 * 优惠券权限
	 */
	private String sharedLvl;
	
	/**
	 * 用户领取优惠券的编码
	 */
	private String userCouponCode;
	
	/**
	 * 共几批
	 */
	private String batchNbrAmt;
	
	/**
	 * 剩余张数
	 */
	private String restNbr;
	
	/**
	 * 优惠券的图片
	 */
	private String url;
	
	/**
	 * 商店距离你现在的位置
	 */
	private String distance;
	
	/**
	 * 每天使用的开始时间
	 */
	private String dayStartUsingTime;
	
	/**
	 * 每天使用的结束时间
	 */
	private String dayEndUsingTime;
	
	/**
	 * 是否有领取优惠券
	 */
	private String isMyReceived;
	
	/**
	 * 领取优惠券的张数
	 */
	private String countMyReceived;
	
	/**
	 * 已领取优惠券
	 */
	private String isDrawCoupon;
	
	/**
	 * 已使用的张数
	 */
	private String usedCoupon;
	
	/**
	 * 消费金额
	 */
	private String totalPrice;
	
	/**
	 * 折扣金额
	 */
	private String deductionPrice;
	
	/**
	 * 是否过期
	 */
	private int isExpire;
	
	/**
	 * 已领张数
	 */
	private String takenCount;
	
	/**
	 * 领用比例
	 */
	private String takenPercent;
	
	/**
	 * 使用数量
	 */
	private int usedCount;
	
	/**
	 * 使用数量的百分比
	 */
	private String usedPercent;
	
	/**
	 * 是否在顾客端显示
	 */
	private int isAvailable;
	
	/**
	 * 领取优惠券批次号
	 */
	private String userCouponNbr;
	
	/**
	 * 优惠券的现状
	 */
	private int status;
	
	/**
	 * 回头客
	 */
	private int repeatCustomers; 
	
	/**
	 * 人气
	 */
	private int popularity;
	
	/**
	 * 显示更多
	 */
	private int showMore;
	
	/**
	 * 商店描述
	 */
	private List<ShopDecoration> shopDecoration;
	
	/**
	 * 城市
	 */
	private String city;
	// limitedNbr = 1, $nbrPerPerson = 1, $
	
	/**
	 * 满就送的张数
	 */
	private String limitedSendNbr;
	/**
	 * 是否有抵扣券
	 */
	private int reduction;
	
	/**
	 * 是否有折扣券
	 */
	private int discount;
	
	/**
	 * 折扣券
	 */
	private double minDiscount;
	
	/**
	 *抵扣券
	 */
	private double minReduction;
	
	/**
	 * 付款时得到优惠券的信息
	 */
	private BatchCoupon couponInfo;
	
	/**
	 * 商家电话
	 */
	private String tel;
	
	/**
	 * 我拥有的优惠券
	 */
	private int userCount;
	
	/**
	 * 优惠券的图片
	 */
	private String couponImg;
	
	/**
	 * 得到这张优惠券需要付多少钱
	 */
	private String payPrice;
	
	/**
	 * 是否显示条形码
	 */
	private int showBarCode;
	
	/**
	 * 是否受理同行卡  0 没有 1 是受理了
	 */
	private  int isAcceptBankCard;
	
	/**
	 * 领取的张数
	 */
	private int countMyActiveReceived;
	
	/**
	 * 购买优惠券的订单编码
	 */
	private String orderCode;
	
	/**
	 * 退款的状态
	 */
	private int orderCouponStatus;
	
	/**
	 * 优惠券退款的一系列地址
	 */
	private String couponRefundUrl;
	
	public BatchCoupon() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public BatchCoupon(String couponType,int totalVolume,String startUsingTime,String expireTime,
						String dayStartUsingTime, String dayEndUsingTime,String startTakingTime, 
						String endTakingTime, String isSend,String sendRequired, String remark, 
						String discountPercent,String insteadPrice,String availablePrice,String function,
						String limitedNbr,String nbrPerPerson,String limitedSendNbr,String payPrice) {
		super();
		this.isSend = isSend;
		this.sendRequired = sendRequired;
		this.discountPercent = discountPercent;
		this.function = function;
		this.couponType = couponType;
		Log.d("TAG", "totalVolumesss = "+totalVolume);
		this.totalVolume = totalVolume;
		this.remark = remark;
		this.expireTime = expireTime;
		this.endTakingTime = endTakingTime;
		this.startTakingTime = startTakingTime;
		this.startUsingTime = startUsingTime;
		this.insteadPrice = insteadPrice;
		this.dayStartUsingTime = dayStartUsingTime;
		this.dayEndUsingTime = dayEndUsingTime;
		this.availablePrice = availablePrice;
		this.limitedNbr = limitedNbr;
		this.nbrPerPerson = nbrPerPerson;
		this.limitedSendNbr = limitedSendNbr;
		this.payPrice = payPrice;
	}
	
	public String getCouponRefundUrl() {
		return couponRefundUrl;
	}

	public void setCouponRefundUrl(String couponRefundUrl) {
		this.couponRefundUrl = couponRefundUrl;
	}

	public int getOrderCouponStatus() {
		return orderCouponStatus;
	}

	public void setOrderCouponStatus(int orderCouponStatus) {
		this.orderCouponStatus = orderCouponStatus;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public int getCountMyActiveReceived() {
		return countMyActiveReceived;
	}

	public void setCountMyActiveReceived(int countMyActiveReceived) {
		this.countMyActiveReceived = countMyActiveReceived;
	}

	public int getIsAcceptBankCard() {
		return isAcceptBankCard;
	}

	public void setIsAcceptBankCard(int isAcceptBankCard) {
		this.isAcceptBankCard = isAcceptBankCard;
	}

	public int getShowBarCode() {
		return showBarCode;
	}

	public void setShowBarCode(int showBarCode) {
		this.showBarCode = showBarCode;
	}

	public String getPayPrice() {
		return payPrice;
	}

	public void setPayPrice(String payPrice) {
		this.payPrice = payPrice;
	}

	public String getCouponImg() {
		return couponImg;
	}

	public void setCouponImg(String couponImg) {
		this.couponImg = couponImg;
	}

	public double getMinDiscount() {
		return minDiscount;
	}

	public void setMinDiscount(double minDiscount) {
		this.minDiscount = minDiscount;
	}

	public double getMinReduction() {
		return minReduction;
	}

	public void setMinReduction(double minReduction) {
		this.minReduction = minReduction;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public int getUserCount() {
		return userCount;
	}

	public void setUserCount(int userCount) {
		this.userCount = userCount;
	}

	public BatchCoupon getCouponInfo() {
		return couponInfo;
	}

	public void setCouponInfo(BatchCoupon couponInfo) {
		this.couponInfo = couponInfo;
	}

	public int getReduction() {
		return reduction;
	}

	public void setReduction(int reduction) {
		this.reduction = reduction;
	}

	public int getDiscount() {
		return discount;
	}

	public void setDiscount(int discount) {
		this.discount = discount;
	}
	
	public String getUserCouponNbr() {
		return userCouponNbr;
	}

	public void setUserCouponNbr(String userCouponNbr) {
		this.userCouponNbr = userCouponNbr;
	}
	
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public int getShowMore() {
		return showMore;
	}

	public void setShowMore(int showMore) {
		this.showMore = showMore;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getRepeatCustomers() {
		return repeatCustomers;
	}

	public void setRepeatCustomers(int repeatCustomers) {
		this.repeatCustomers = repeatCustomers;
	}

	public int getPopularity() {
		return popularity;
	}

	public void setPopularity(int popularity) {
		this.popularity = popularity;
	}

	public List<ShopDecoration> getShopDecoration() {
		return shopDecoration;
	}

	public void setShopDecoration(List<ShopDecoration> shopDecoration) {
		this.shopDecoration = shopDecoration;
	}

	public int getIsAvailable() {
		return isAvailable;
	}

	public void setIsAvailable(int isAvailable) {
		this.isAvailable = isAvailable;
	}

	public int getUsedCount() {
		return usedCount;
	}

	public void setUsedCount(int usedCount) {
		this.usedCount = usedCount;
	}

	public String getUsedPercent() {
		return usedPercent;
	}

	public void setUsedPercent(String usedPercent) {
		this.usedPercent = usedPercent;
	}

	public String getTakenCount() {
		return takenCount;
	}

	public void setTakenCount(String takenCount) {
		this.takenCount = takenCount;
	}

	public String getTakenPercent() {
		return takenPercent;
	}

	public void setTakenPercent(String takenPercent) {
		this.takenPercent = takenPercent;
	}

	public String getIsSend() {
		return isSend;
	}

	public void setIsSend(String isSend) {
		this.isSend = isSend;
	}

	public String getSendRequired() {
		return sendRequired;
	}

	public void setSendRequired(String sendRequired) {
		this.sendRequired = sendRequired;
	}

	public String getFunction() {
		return function;
	}

	public void setFunction(String function) {
		this.function = function;
	}

	public int getIsExpire() {
		return isExpire;
	}

	public void setIsExpire(int isExpire) {
		this.isExpire = isExpire;
	}

	public String getUsedCoupon() {
		return usedCoupon;
	}

	public void setUsedCoupon(String usedCoupon) {
		this.usedCoupon = usedCoupon;
	}

	public String getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(String totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getDeductionPrice() {
		return deductionPrice;
	}

	public void setDeductionPrice(String deductionPrice) {
		this.deductionPrice = deductionPrice;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getIsDrawCoupon() {
		return isDrawCoupon;
	}

	public void setIsDrawCoupon(String isDrawCoupon) {
		this.isDrawCoupon = isDrawCoupon;
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

	public String getStartTakingTime() {
		return startTakingTime;
	}

	public void setStartTakingTime(String startTakingTime) {
		this.startTakingTime = startTakingTime;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getBatchNbrAmt() {
		return batchNbrAmt;
	}

	public void setBatchNbrAmt(String batchNbrAmt) {
		this.batchNbrAmt = batchNbrAmt;
	}

	public String getRestNbr() {
		return restNbr;
	}

	public void setRestNbr(String restNbr) {
		this.restNbr = restNbr;
	}

	public String getAvailablePrice() {
		return availablePrice;
	}

	public void setAvailablePrice(String availablePrice) {
		this.availablePrice = availablePrice;
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

	public String getLogoUrl() {
		return logoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getSharedLvl() {
		return sharedLvl;
	}

	public void setSharedLvl(String sharedLvl) {
		this.sharedLvl = sharedLvl;
	}

	public String getUserCouponCode() {
		return userCouponCode;
	}

	public void setUserCouponCode(String userCouponCode) {
		this.userCouponCode = userCouponCode;
	}

	public String getCanTakePrice() {
		return canTakePrice;
	}

	public void setCanTakePrice(String canTakePrice) {
		this.canTakePrice = canTakePrice;
	}

	public String getBatchCouponCode() {
		return batchCouponCode;
	}

	public void setBatchCouponCode(String batchCouponCode) {
		this.batchCouponCode = batchCouponCode;
	}

	public String getBatchNbr() {
		return batchNbr;
	}

	public void setBatchNbr(String batchNbr) {
		this.batchNbr = batchNbr;
	}

	public String getCouponName() {
		return couponName;
	}

	public void setCouponName(String couponName) {
		this.couponName = couponName;
	}

	public String getCouponType() {
		return couponType;
	}

	public void setCouponType(String couponType) {
		this.couponType = couponType;
	}

	public String getCouponTypeName() {
		return couponTypeName;
	}

	public void setCouponTypeName(String couponTypeName) {
		this.couponTypeName = couponTypeName;
	}

	public Integer getCouponSubSeq() {
		return couponSubSeq;
	}

	public void setCouponSubSeq(Integer couponSubSeq) {
		this.couponSubSeq = couponSubSeq;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getCreatorCode() {
		return creatorCode;
	}

	public void setCreatorCode(String creatorCode) {
		this.creatorCode = creatorCode;
	}

	public String getShopCode() {
		return shopCode;
	}

	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}

	public Integer getRemaining() {
		return remaining;
	}

	public void setRemaining(Integer remaining) {
		this.remaining = remaining;
	}
	
	public int getTotalVolume() {
		return totalVolume;
	}

	public void setTotalVolume(int totalVolume) {
		this.totalVolume = totalVolume;
	}

	public int getValidateForN() {
		return validateForN;
	}

	public void setValidateForN(int validateForN) {
		this.validateForN = validateForN;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(String expireTime) {
		this.expireTime = expireTime;
	}

	public String getBgCode() {
		return bgCode;
	}

	public void setBgCode(String bgCode) {
		this.bgCode = bgCode;
	}

	public String getEndTakingTime() {
		return endTakingTime;
	}

	public void setEndTakingTime(String endTakingTime) {
		this.endTakingTime = endTakingTime;
	}

	public String getStartUsingTime() {
		return startUsingTime;
	}

	public void setStartUsingTime(String startUsingTime) {
		this.startUsingTime = startUsingTime;
	}

	public String getDiscountPercent() {
		return discountPercent;
	}

	public void setDiscountPercent(String discountPercent) {
		this.discountPercent = discountPercent;
	}

	public String getIndustryCategory() {
		return industryCategory;
	}

	public void setIndustryCategory(String industryCategory) {
		this.industryCategory = industryCategory;
	}

	public String getInsteadPrice() {
		return insteadPrice;
	}

	public void setInsteadPrice(String insteadPrice) {
		this.insteadPrice = insteadPrice;
	}
	
	public int getIsConsumeRequired() {
		return isConsumeRequired;
	}

	public void setIsConsumeRequired(int isConsumeRequired) {
		this.isConsumeRequired = isConsumeRequired;
	}

	public String getLimitedNbr() {
		return limitedNbr;
	}

	public void setLimitedNbr(String limitedNbr) {
		this.limitedNbr = limitedNbr;
	}

	public String getNbrPerPerson() {
		return nbrPerPerson;
	}

	public void setNbrPerPerson(String nbrPerPerson) {
		this.nbrPerPerson = nbrPerPerson;
	}

	public String getLimitedSendNbr() {
		return limitedSendNbr;
	}

	public void setLimitedSendNbr(String limitedSendNbr) {
		this.limitedSendNbr = limitedSendNbr;
	}

	public String getExRuleDes() {
		return exRuleDes;
	}

	public void setExRuleDes(String exRuleDes) {
		this.exRuleDes = exRuleDes;
	}

	public Boolean getIsBankCardRequired() {
		return isBankCardRequired;
	}

	public void setIsBankCardRequired(Boolean isBankCardRequired) {
		this.isBankCardRequired = isBankCardRequired;
	}

	public Byte getMemberLvl() {
		return memberLvl;
	}

	public void setMemberLvl(Byte memberLvl) {
		this.memberLvl = memberLvl;
	}

	public Byte getAvaliableStage() {
		return avaliableStage;
	}

	public void setAvaliableStage(Byte avaliableStage) {
		this.avaliableStage = avaliableStage;
	}

	@Override
	public String toString() {
		return "BatchCoupon [batchCouponCode=" + batchCouponCode + ", batchNbr=" + batchNbr + ", isSend=" + isSend + ", sendRequired=" + sendRequired + ", function=" + function + ", couponName=" + couponName + ", couponType=" + couponType + ", couponTypeName=" + couponTypeName + ", couponSubSeq=" + couponSubSeq + ", createTime=" + createTime + ", creatorCode=" + creatorCode + ", shopCode=" + shopCode + ", remaining=" + remaining + ", totalVolume=" + totalVolume + ", validateForN=" + validateForN + ", remark=" + remark + ", bgCode=" + bgCode + ", expireTime=" + expireTime + ", endTakingTime=" + endTakingTime + ", startTakingTime=" + startTakingTime + ", startUsingTime=" + startUsingTime + ", discountPercent=" + discountPercent + ", industryCategory=" + industryCategory + ", insteadPrice="
				+ insteadPrice + ", isConsumeRequired=" + isConsumeRequired + ", canTakePrice=" + canTakePrice + ", availablePrice=" + availablePrice + ", limitedNbr=" + limitedNbr + ", nbrPerPerson=" + nbrPerPerson + ", exRuleDes=" + exRuleDes + ", isBankCardRequired=" + isBankCardRequired + ", memberLvl=" + memberLvl + ", avaliableStage=" + avaliableStage + ", mobileNbr=" + mobileNbr + ", street=" + street + ", logoUrl=" + logoUrl + ", shopName=" + shopName + ", sharedLvl=" + sharedLvl + ", userCouponCode=" + userCouponCode + ", batchNbrAmt=" + batchNbrAmt + ", restNbr=" + restNbr + ", url=" + url + ", distance=" + distance + ", dayStartUsingTime=" + dayStartUsingTime + ", dayEndUsingTime=" + dayEndUsingTime + ", isMyReceived=" + isMyReceived + ", countMyReceived=" + countMyReceived
				+ ", isDrawCoupon=" + isDrawCoupon + ", usedCoupon=" + usedCoupon + ", totalPrice=" + totalPrice + ", deductionPrice=" + deductionPrice + ", isExpire=" + isExpire + ", takenCount=" + takenCount + ", takenPercent=" + takenPercent + ", usedCount=" + usedCount + ", usedPercent=" + usedPercent + ", isAvailable=" + isAvailable + ", userCouponNbr=" + userCouponNbr + ", status=" + status + ", repeatCustomers=" + repeatCustomers + ", popularity=" + popularity + ", showMore=" + showMore + ", shopDecoration=" + shopDecoration + ", city=" + city + ", limitedSendNbr=" + limitedSendNbr + ", reduction=" + reduction + ", discount=" + discount + ", minDiscount=" + minDiscount + ", minReduction=" + minReduction + ", couponInfo=" + couponInfo + ", tel=" + tel + ", userCount=" + userCount
				+ ", couponImg=" + couponImg + ", payPrice=" + payPrice + ", showBarCode=" + showBarCode + ", isAcceptBankCard=" + isAcceptBankCard + ", countMyActiveReceived=" + countMyActiveReceived + ", orderCode=" + orderCode + ", orderCouponStatus=" + orderCouponStatus + ", couponRefundUrl=" + couponRefundUrl + "]";
	}

	

}
