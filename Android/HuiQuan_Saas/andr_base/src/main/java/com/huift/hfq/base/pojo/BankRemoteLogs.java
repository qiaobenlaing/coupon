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

public class BankRemoteLogs implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	/** 
	 * 主键.
	 */
	private String logCode;

	/** 
	 * 信息标头.
	 */
	private String title;

	/** 
	 * 信息内容.
	 */
	private String content;

	/** 
	 * 时间.
	 */
	private Date actionTime;

	public BankRemoteLogs() {
	}

	public BankRemoteLogs(String logCode) {
		this.logCode = logCode;
	}

	public BankRemoteLogs(String logCode, String title, String content,
			Date actionTime) {
		this.logCode = logCode;
		this.title = title;
		this.content = content;
		this.actionTime = actionTime;
	}

	/**
	 * 获取 主键. 
	 */
	public String getLogCode() {
		return this.logCode;
	}

	/**
	 * 设置 主键. 
	 */
	public void setLogCode(String logCode) {
		this.logCode = logCode;
	}

	/**
	 * 获取 信息标头. 
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * 设置 信息标头. 
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * 获取 信息内容. 
	 */
	public String getContent() {
		return this.content;
	}

	/**
	 * 设置 信息内容. 
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * 获取 时间. 
	 */
	public Date getActionTime() {
		return this.actionTime;
	}

	/**
	 * 设置 时间. 
	 */
	public void setActionTime(Date actionTime) {
		this.actionTime = actionTime;
	}

}
