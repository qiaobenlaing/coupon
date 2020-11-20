/**
 *
 * @Author: Jianping Chen
 * @Date: 2015.5.8
 * @Version: 1.0.0
 * @Copyright Suanzi Co.,Ltd. @ 2015
 * 
 */

package com.huift.hfq.base.pojo;

import java.util.Date;

public class ShopFollowing implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	/** 
	 * 主键.
	 */
	private String followingCode;

	/** 
	 * 用户.
	 */
	private String userCode;

	/** 
	 * 商家.
	 */
	private String shopCode;

	/** 
	 * 关注时间.
	 */
	private Date createTime;

	public ShopFollowing() {
	}

	public ShopFollowing(String followingCode) {
		this.followingCode = followingCode;
	}

	public ShopFollowing(String followingCode, String userCode,
			String shopCode, Date createTime) {
		this.followingCode = followingCode;
		this.userCode = userCode;
		this.shopCode = shopCode;
		this.createTime = createTime;
	}

	/**
	 * 获取 主键. 
	 */
	public String getFollowingCode() {
		return this.followingCode;
	}

	/**
	 * 设置 主键. 
	 */
	public void setFollowingCode(String followingCode) {
		this.followingCode = followingCode;
	}

	/**
	 * 获取 用户. 
	 */
	public String getUserCode() {
		return this.userCode;
	}

	/**
	 * 设置 用户. 
	 */
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	/**
	 * 获取 商家. 
	 */
	public String getShopCode() {
		return this.shopCode;
	}

	/**
	 * 设置 商家. 
	 */
	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}

	/**
	 * 获取 关注时间. 
	 */
	public Date getCreateTime() {
		return this.createTime;
	}

	/**
	 * 设置 关注时间. 
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}
