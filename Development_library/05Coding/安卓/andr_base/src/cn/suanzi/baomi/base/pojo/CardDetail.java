package cn.suanzi.baomi.base.pojo;

import java.io.Serializable;

/**
 * 会员卡详情实体类
 * @author qian.zhou
 */
public class CardDetail implements Serializable {
	
	private String qrCode;//二维码
	private String barCode;//条形码
	private String shopName;//商铺名称
	private String realName;//用户姓名
	private String point;//积分
	private String cardLvl;//卡等级
	private String discount;//折扣
	private String shopCode;//商家编码
	private String cardName;//卡的名称
	private String cardNbr;//卡的编码
	private String logoUrl;//商家头像
	private String discountRequire;//满多元打多少折
	private String pointDeduction;//可抵扣多少金额
	public CardDetail() {
		super();
		// TODO Auto-generated constructor stub
	}
	public CardDetail(String qrCode, String barCode, String shopName,
			String realName, String point, String cardLvl, String discount,
			String shopCode ,String cardName, String cardNbr, String logoUrl, String pointDeduction) {
		super();
		this.qrCode = qrCode;
		this.barCode = barCode;
		this.shopName = shopName;
		this.realName = realName;
		this.point = point;
		this.cardLvl = cardLvl;
		this.discount = discount;
		this.shopCode = shopCode;
		this.cardName = cardName;
		this.cardNbr = cardNbr;
		this.logoUrl = logoUrl;
		this.pointDeduction = pointDeduction;
	}
	public String getQrCode() {
		return qrCode;
	}
	public void setQrCode(String qrCode) {
		this.qrCode = qrCode;
	}
	public String getBarCode() {
		return barCode;
	}
	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public String getPoint() {
		return point;
	}
	public void setPoint(String point) {
		this.point = point;
	}
	public String getCardLvl() {
		return cardLvl;
	}
	public void setCardLvl(String cardLvl) {
		this.cardLvl = cardLvl;
	}
	public String getDiscount() {
		return discount;
	}
	public void setDiscount(String discount) {
		this.discount = discount;
	}
	public String getShopCode() {
		return shopCode;
	}
	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}
	public String getCardName() {
		return cardName;
	}
	public void setCardName(String cardName) {
		this.cardName = cardName;
	}
	public String getCardNbr() {
		return cardNbr;
	}
	public void setCardNbr(String cardNbr) {
		this.cardNbr = cardNbr;
	}
	public String getLogoUrl() {
		return logoUrl;
	}
	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}
	public String getPointDeduction() {
		return pointDeduction;
	}
	public void setPointDeduction(String pointDeduction) {
		this.pointDeduction = pointDeduction;
	}
	public String getDiscountRequire() {
		return discountRequire;
	}
	public void setDiscountRequire(String discountRequire) {
		this.discountRequire = discountRequire;
	}
}
