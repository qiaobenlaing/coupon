package com.huift.hfq.base.pojo;

import java.io.Serializable;

/**
 * 红包
 * @author wensi.yu
 */
public class Bonus implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 获取人数
	 */
	private String getNbr;
	
	/**
	 * 领取率
	 */
	private String getPercent; 
	
	/**
	 * 已领取金额比例
	 */
	private String getValuePercent;
	
	/**
	 * 启用  和 停发
	 */
	private int status;
	
	/**
	 * 红包编码
	 */
	private String batchNbr;
	
	/**
	 * 发行总量
	 */
	private String totalVolume;
	
	/**
	 * 最低金额
	 */
	private String lowerPrice;
	
	/**
	 * 最高金额
	 */
	private String upperPrice;
	
	/**
	 * 开始时间
	 */
	private String startTime;
	
	/**
	 * 红包的Id
	 */
	private String bonusCode;

	/**
	 * 红包的Id
	 */
	private String userBonusCode;

	
	/**
	 * 抢红包结束市静安
	 */
	private String endTime;

	/**
	 * 抢红包之前照片
	 */
	private String preUrl;

	/**
	 * 抢红包中的照片
	 */
	private String ingUrl;

	/**
	 * 抢红包后的图片
	 */
	private String afterUrl;

	/**
	 * 金额
	 */
	private String value;
	/**
	 * 金额
	 */
	private String totalValue;

	/**
	 * 时间
	 */
	private String getDate;

	/**
	 * 商铺名称
	 */
	private String shopName;

	/**
	 * 商铺LOGO
	 */
	private String logoUrl;
	
	/**
	 * 红包领取的金额
	 */
	private String getValue;
	
	/**
	 * 可以使用商家红包
	 */
	private int canUseShopBonus;
	
	/**
	 * 可以使用平台红包
	 */
	private int canUsePlatBonus;
	
	/**
	 * 商家红包
	 */
	private String shopBonus;
	
	/**
	 * 平台红包
	 */
	private String platBonus;
	
	public int getCanUseShopBonus() {
		return canUseShopBonus;
	}

	public void setCanUseShopBonus(int canUseShopBonus) {
		this.canUseShopBonus = canUseShopBonus;
	}

	public int getCanUsePlatBonus() {
		return canUsePlatBonus;
	}

	public void setCanUsePlatBonus(int canUsePlatBonus) {
		this.canUsePlatBonus = canUsePlatBonus;
	}
	
	public String getShopBonus() {
		return shopBonus;
	}

	public void setShopBonus(String shopBonus) {
		this.shopBonus = shopBonus;
	}

	public String getPlatBonus() {
		return platBonus;
	}

	public void setPlatBonus(String platBonus) {
		this.platBonus = platBonus;
	}

	public String getGetValue() {
		return getValue;
	}

	public void setGetValue(String getValue) {
		this.getValue = getValue;
	}

	public String getGetDate() {
		return getDate;
	}

	public void setGetDate(String getDate) {
		this.getDate = getDate;
	}

	public String getPreUrl() {
		return preUrl;
	}

	public void setPreUrl(String preUrl) {
		this.preUrl = preUrl;
	}

	public String getIngUrl() {
		return ingUrl;
	}

	public void setIngUrl(String ingUrl) {
		this.ingUrl = ingUrl;
	}

	public String getAfterUrl() {
		return afterUrl;
	}

	public void setAfterUrl(String afterUrl) {
		this.afterUrl = afterUrl;
	}

	public String getBonusCode() {
		return bonusCode;
	}

	public void setBonusCode(String bonusCode) {
		this.bonusCode = bonusCode;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getUserBonusCode() {
		return userBonusCode;
	}

	public void setUserBonusCode(String userBonusCode) {
		this.userBonusCode = userBonusCode;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	

	public String getTotalValue() {
		return totalValue;
	}

	public void setTotalValue(String totalValue) {
		this.totalValue = totalValue;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getLogoUrl() {
		return logoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	public String getGetNbr() {
		return getNbr;
	}

	public void setGetNbr(String getNbr) {
		this.getNbr = getNbr;
	}

	public String getGetPercent() {
		return getPercent;
	}

	public void setGetPercent(String getPercent) {
		this.getPercent = getPercent;
	}

	public String getGetValuePercent() {
		return getValuePercent;
	}

	public void setGetValuePercent(String getValuePercent) {
		this.getValuePercent = getValuePercent;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getBatchNbr() {
		return batchNbr;
	}

	public void setBatchNbr(String batchNbr) {
		this.batchNbr = batchNbr;
	}

	public String getTotalVolume() {
		return totalVolume;
	}

	public void setTotalVolume(String totalVolume) {
		this.totalVolume = totalVolume;
	}

	public String getLowerPrice() {
		return lowerPrice;
	}

	public void setLowerPrice(String lowerPrice) {
		this.lowerPrice = lowerPrice;
	}

	public String getUpperPrice() {
		return upperPrice;
	}

	public void setUpperPrice(String upperPrice) {
		this.upperPrice = upperPrice;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
