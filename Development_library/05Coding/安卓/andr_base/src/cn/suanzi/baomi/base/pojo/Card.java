/**
 *
 * @Author: Jianping Chen
 * @Date: 2015.5.8
 * @Version: 1.0.0
 * @Copyright Suanzi Co.,Ltd. @ 2015
 * 
 */

package cn.suanzi.baomi.base.pojo;

import java.io.Serializable;

public class Card  implements Serializable{

	private static final long serialVersionUID = 1L;

	/** 
	 * 主键.
	 */
	private String cardCode;

	/** 
	 * 卡名.
	 */
	private String cardName;

	/** 
	 * 卡的类型；1000表示会员卡，2000表示储值卡.
	 */
	private String cardType;

	/** 
	 * 一个类型的卡可能分为不同级别.
	 */
	private String cardLvl;
	
	/**
	 * 会员总共等级多少
	 */
	private String grade;

	/** 
	 * 创建时间.
	 */
	private String createTime;

	/** 
	 * 创建者编码.
	 */
	private String creatorCode;

	/** 
	 * 创建该卡的商店.
	 */
	private String shopCode;

	/** 
	 * 卡状态.
	 */
	private String status;

	/** 
	 * 背景图片.
	 */
	private String bgImgCode;

	/** 
	 * 需要多少积分才可以享受折扣.
	 */
	private String discountRequire;

	/** 
	 * 可享受折扣.
	 */
	private String discount;
	
	/**
	 * 可不可以使用会员卡打折
	 */
	private int canUseCard;

	/** 
	 * 是否要求实名.
	 */
	private String isRealNameRequired;

	/** 
	 * 是否可以多人使用.
	 */
	private String isSharable;

	/** 
	 * 积分有效期，单位：天.
	 */
	private String pointLifeTime;

	/** 
	 * 基础进阶分数要求，达到多少积分后可以进阶下一等级的会员卡.
	 */
	private String forwardPoint;

	/** 
	 * 每消费1元积多少积分.
	 */
	private String pointsPerCash;
	
	/** 
	 * 每多少分积1元
	 */            
	private String outPointsPerCash;

	/** 
	 * 备注.
	 */
	private String remark;
	
	/**
	 * 会员卡图片
	 */
	private String url;

	/**
	 * 会员卡的人数
	 */
	private String vipNbr;
	
	/**
	 * 消费总金额
	 */
	private String consumeAmountCount;
	
	/**
	 * 优惠券使用总张数
	 */
	private String couponUsedAmount;
	
	/**
	 * 消费次数
	 */
	private String consumeTimes;
	
	/**
	 * 积分数
	 */
	private String points;
	
	/**
	 * 会员卡抵扣金额
	 * @return
	 */
	private String cardInsteadPrice;
	
	/**
	 * 会员卡折扣
	 * @return
	 */
	private String cardDiscount;
 	
	public int getCanUseCard() {
		return canUseCard;
	}

	public void setCanUseCard(int canUseCard) {
		this.canUseCard = canUseCard;
	}

	public String getConsumeAmountCount() {
		return consumeAmountCount;
	}

	public void setConsumeAmountCount(String consumeAmountCount) {
		this.consumeAmountCount = consumeAmountCount;
	}

	public String getCouponUsedAmount() {
		return couponUsedAmount;
	}

	public void setCouponUsedAmount(String couponUsedAmount) {
		this.couponUsedAmount = couponUsedAmount;
	}

	public String getConsumeTimes() {
		return consumeTimes;
	}

	public void setConsumeTimes(String consumeTimes) {
		this.consumeTimes = consumeTimes;
	}

	public String getPoints() {
		return points;
	}

	public void setPoints(String points) {
		this.points = points;
	}

	public String getOutPointsPerCash() {
		return outPointsPerCash;
	}

	public void setOutPointsPerCash(String outPointsPerCash) {
		this.outPointsPerCash = outPointsPerCash;
	}

	public String getVipNbr() {
		return vipNbr;
	}

	public void setVipNbr(String vipNbr) {
		this.vipNbr = vipNbr;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getCardCode() {
		return cardCode;
	}

	public void setCardCode(String cardCode) {
		this.cardCode = cardCode;
	}

	public String getCardName() {
		return cardName;
	}

	public void setCardName(String cardName) {
		this.cardName = cardName;
	}

	public String getCardLvl() {
		return cardLvl;
	}

	public void setCardLvl(String cardLvl) {
		this.cardLvl = cardLvl;
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
	
	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getBgImgCode() {
		return bgImgCode;
	}

	public void setBgImgCode(String bgImgCode) {
		this.bgImgCode = bgImgCode;
	}

	public String getDiscountRequire() {
		return discountRequire;
	}

	public void setDiscountRequire(String discountRequire) {
		this.discountRequire = discountRequire;
	}

	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public String getIsRealNameRequired() {
		return isRealNameRequired;
	}

	public void setIsRealNameRequired(String isRealNameRequired) {
		this.isRealNameRequired = isRealNameRequired;
	}

	public String getIsSharable() {
		return isSharable;
	}

	public void setIsSharable(String isSharable) {
		this.isSharable = isSharable;
	}

	public String getPointLifeTime() {
		return pointLifeTime;
	}

	public void setPointLifeTime(String pointLifeTime) {
		this.pointLifeTime = pointLifeTime;
	}

	public String getForwardPoint() {
		return forwardPoint;
	}

	public void setForwardPoint(String forwardPoint) {
		this.forwardPoint = forwardPoint;
	}

	public String getPointsPerCash() {
		return pointsPerCash;
	}

	public void setPointsPerCash(String pointsPerCash) {
		this.pointsPerCash = pointsPerCash;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getCardInsteadPrice() {
		return cardInsteadPrice;
	}

	public void setCardInsteadPrice(String cardInsteadPrice) {
		this.cardInsteadPrice = cardInsteadPrice;
	}

	public String getCardDiscount() {
		return cardDiscount;
	}

	public void setCardDiscount(String cardDiscount) {
		this.cardDiscount = cardDiscount;
	}
}
