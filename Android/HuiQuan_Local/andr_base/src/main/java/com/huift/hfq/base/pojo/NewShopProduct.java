package com.huift.hfq.base.pojo;

import java.io.Serializable;

public class NewShopProduct implements Serializable {
	/**月销售量*/
	private int monthlySalesVolume;
	
	/**产品图片url*/
	private String url;
	
	/**产品名称*/
	private String productName;
	
	/**产品原价*/
	private double originalPrice;
	
	/**产品现价*/
	private double finalPrice;
	
	/**产品id*/
	private int productId;
	
	/**产品描述*/
	private String des;
	
	/**产品推荐度*/
	private int recommendLevel;
	
	public NewShopProduct() {
		super();
	}

	public NewShopProduct(int monthlySalesVolume, String url, String productName, double originalPrice, double finalPrice, int productId, String des, int recommendLevel) {
		super();
		this.monthlySalesVolume = monthlySalesVolume;
		this.url = url;
		this.productName = productName;
		this.originalPrice = originalPrice;
		this.finalPrice = finalPrice;
		this.productId = productId;
		this.des = des;
		this.recommendLevel = recommendLevel;
	}

	public int getMonthlySalesVolume() {
		return monthlySalesVolume;
	}

	public void setMonthlySalesVolume(int monthlySalesVolume) {
		this.monthlySalesVolume = monthlySalesVolume;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public double getOriginalPrice() {
		return originalPrice;
	}

	public void setOriginalPrice(double originalPrice) {
		this.originalPrice = originalPrice;
	}

	public double getFinalPrice() {
		return finalPrice;
	}

	public void setFinalPrice(double finalPrice) {
		this.finalPrice = finalPrice;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}

	public int getRecommendLevel() {
		return recommendLevel;
	}

	public void setRecommendLevel(int recommendLevel) {
		this.recommendLevel = recommendLevel;
	}


}
