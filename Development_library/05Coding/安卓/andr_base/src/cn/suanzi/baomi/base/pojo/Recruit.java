package cn.suanzi.baomi.base.pojo;

import java.io.Serializable;

/**
 * 招生启示
 * @author wensi.yu
 *
 */
public class Recruit implements Serializable{

	/**
	 * 广告图
	 */
	private String advUrl;
	
	/**
	 * 招生启示编码
	 */
	private String recruitCode;
	
	/**
	 * 招生图
	 */
	private String recruitUrl;
	
	/**
	 * 商家编码
	 */
	private String shopCode;

	public Recruit(String advUrl, String recruitCode, String recruitUrl, String shopCode) {
		super();
		this.advUrl = advUrl;
		this.recruitCode = recruitCode;
		this.recruitUrl = recruitUrl;
		this.shopCode = shopCode;
	}

	public String getAdvUrl() {
		return advUrl;
	}

	public void setAdvUrl(String advUrl) {
		this.advUrl = advUrl;
	}

	public String getRecruitCode() {
		return recruitCode;
	}

	public void setRecruitCode(String recruitCode) {
		this.recruitCode = recruitCode;
	}

	public String getRecruitUrl() {
		return recruitUrl;
	}

	public void setRecruitUrl(String recruitUrl) {
		this.recruitUrl = recruitUrl;
	}

	public String getShopCode() {
		return shopCode;
	}

	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}
	
	
	
}
