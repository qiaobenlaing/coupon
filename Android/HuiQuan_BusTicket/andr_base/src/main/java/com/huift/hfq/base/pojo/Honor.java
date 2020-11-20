package com.huift.hfq.base.pojo;

import java.io.Serializable;

/**
 * 荣誉墙
 * @author wensi.yu
 *
 */
public class Honor implements Serializable{

	/**
	 * 荣誉编码
	 */
	private String honorCode;
	
	/**
	 * 荣誉图
	 */
	private String honorUrl;
	
	/**
	 * 商家编码
	 */
	private String shopCode;
	
	/**
	 * 上传时间
	 */
	private String uploadTime;
	

	public Honor(String honorCode, String honorUrl, String shopCode, String uploadTime) {
		super();
		this.honorCode = honorCode;
		this.honorUrl = honorUrl;
		this.shopCode = shopCode;
		this.uploadTime = uploadTime;
	}

	public String getHonorCode() {
		return honorCode;
	}

	public void setHonorCode(String honorCode) {
		this.honorCode = honorCode;
	}

	public String getHonorUrl() {
		return honorUrl;
	}

	public void setHonorUrl(String honorUrl) {
		this.honorUrl = honorUrl;
	}

	public String getShopCode() {
		return shopCode;
	}

	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}

	public String getUploadTime() {
		return uploadTime;
	}

	public void setUploadTime(String uploadTime) {
		this.uploadTime = uploadTime;
	}
	
	
	
}
