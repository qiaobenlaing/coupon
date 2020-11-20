package com.huift.hfq.base.pojo;

import java.io.Serializable;

/**
 * 图片
 * @author ad
 */
public class Image implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 图片URl
	 */
	private String ImageUrl;
	
	/**
	 * 产品图片
	 */
	private String productImg;
	
	/**
	 * 图片跳转的内容
	 */
	private String content;
	
	/**
	 * 图片跳转类型
	 */
	private int linkType;
	
	/**
	 * 家长评论的图片
	 */
	private String remarkImgUrl;
	
	
	public String getRemarkImgUrl() {
		return remarkImgUrl;
	}

	public void setRemarkImgUrl(String remarkImgUrl) {
		this.remarkImgUrl = remarkImgUrl;
	}

	public String getProductImg() {
		return productImg;
	}

	public void setProductImg(String productImg) {
		this.productImg = productImg;
	}

	public String getImageUrl() {
		return ImageUrl;
	}

	public void setImageUrl(String imageUrl) {
		ImageUrl = imageUrl;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getLinkType() {
		return linkType;
	}

	public void setLinkType(int linkType) {
		this.linkType = linkType;
	}
	
	
}
