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

public class BackgroundTemplate implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	/** 
	 * 主键.
	 */
	private String bgCode;

	/** 
	 * 类型，基本分类：卡背景，优惠券背景.
	 */
	private Integer type;

	/** 
	 * 图片url.
	 */
	private String url;

	/** 
	 * 备注.
	 */
	private String remark;

	/** 
	 * 名称.
	 */
	private String title;

	/** 
	 * 创建者.
	 */
	private String creatorCode;

	/** 
	 * 创建时间.
	 */
	private Date createTime;

	public BackgroundTemplate() {
	}

	public BackgroundTemplate(String bgCode) {
		this.bgCode = bgCode;
	}

	public BackgroundTemplate(String bgCode, Integer type, String url,
			String remark, String title, String creatorCode, Date createTime) {
		this.bgCode = bgCode;
		this.type = type;
		this.url = url;
		this.remark = remark;
		this.title = title;
		this.creatorCode = creatorCode;
		this.createTime = createTime;
	}

	/**
	 * 获取 主键. 
	 */
	public String getBgCode() {
		return this.bgCode;
	}

	/**
	 * 设置 主键. 
	 */
	public void setBgCode(String bgCode) {
		this.bgCode = bgCode;
	}

	/**
	 * 获取 类型，基本分类：卡背景，优惠券背景. 
	 */
	public Integer getType() {
		return this.type;
	}

	/**
	 * 设置 类型，基本分类：卡背景，优惠券背景. 
	 */
	public void setType(Integer type) {
		this.type = type;
	}

	/**
	 * 获取 图片url. 
	 */
	public String getUrl() {
		return this.url;
	}

	/**
	 * 设置 图片url. 
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * 获取 备注. 
	 */
	public String getRemark() {
		return this.remark;
	}

	/**
	 * 设置 备注. 
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * 获取 名称. 
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * 设置 名称. 
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * 获取 创建者. 
	 */
	public String getCreatorCode() {
		return this.creatorCode;
	}

	/**
	 * 设置 创建者. 
	 */
	public void setCreatorCode(String creatorCode) {
		this.creatorCode = creatorCode;
	}

	/**
	 * 获取 创建时间. 
	 */
	public Date getCreateTime() {
		return this.createTime;
	}

	/**
	 * 设置 创建时间. 
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}
