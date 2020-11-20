package cn.suanzi.baomi.base.pojo;

import java.io.Serializable;

public class Item implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * 店长状态
	 */
	private String status;
	
	/**
	 * 店长所属商店编码
	 */
	private String shopCode;
	
	/**
	 * 商家头像
	 */
	private String logoUrl;
	
	/**
	 * 商家名称
	 */
	private String shopName;

	public Item() {
		super();
	}

	public Item(String status, String shopCode, String logoUrl, String shopName) {
		super();
		this.status = status;
		this.shopCode = shopCode;
		this.logoUrl = logoUrl;
		this.shopName = shopName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getShopCode() {
		return shopCode;
	}

	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
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
}
