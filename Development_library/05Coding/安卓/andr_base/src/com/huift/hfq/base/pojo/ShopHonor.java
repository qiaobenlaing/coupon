package com.huift.hfq.base.pojo;

import java.io.Serializable;

/**
 * 教育行业的荣誉
 * @author yingchen
 *
 */
public class ShopHonor implements  Serializable{
	private String  honorUrl;

	public ShopHonor() {
		super();
	}

	public ShopHonor(String honorUrl) {
		super();
		this.honorUrl = honorUrl;
	}

	public String getHonorUrl() {
		return honorUrl;
	}

	public void setHonorUrl(String honorUrl) {
		this.honorUrl = honorUrl;
	}
	
}
