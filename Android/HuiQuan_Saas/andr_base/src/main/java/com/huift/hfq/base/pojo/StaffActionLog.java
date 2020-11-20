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

public class StaffActionLog implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	/** 
	 * 主键.
	 */
	private String logCode;

	/** 
	 * 管理员ID.
	 */
	private String staffId;

	/** 
	 * 操作描述.
	 */
	private String actionDes;

	/** 
	 * 操作时间.
	 */
	private Date actionTime;

	/** 
	 * 登陆IP.
	 */
	private String ipAddr;

	public StaffActionLog() {
	}

	public StaffActionLog(String logCode) {
		this.logCode = logCode;
	}

	public StaffActionLog(String logCode, String staffId, String actionDes,
			Date actionTime, String ipAddr) {
		this.logCode = logCode;
		this.staffId = staffId;
		this.actionDes = actionDes;
		this.actionTime = actionTime;
		this.ipAddr = ipAddr;
	}

	/**
	 * 获取主键. 
	 */
	public String getLogCode() {
		return this.logCode;
	}

	/**
	 * 设置主键. 
	 */
	public void setLogCode(String logCode) {
		this.logCode = logCode;
	}

	/**
	 * 获取管理员ID. 
	 */
	public String getStaffId() {
		return this.staffId;
	}

	/**
	 * 设置管理员ID. 
	 */
	public void setStaffId(String staffId) {
		this.staffId = staffId;
	}

	/**
	 * 获取操作描述. 
	 */
	public String getActionDes() {
		return this.actionDes;
	}

	/**
	 * 设置操作描述. 
	 */
	public void setActionDes(String actionDes) {
		this.actionDes = actionDes;
	}

	/**
	 * 获取操作时间. 
	 */
	public Date getActionTime() {
		return this.actionTime;
	}

	/**
	 * 设置操作时间. 
	 */
	public void setActionTime(Date actionTime) {
		this.actionTime = actionTime;
	}

	/**
	 * 获取登陆IP. 
	 */
	public String getIpAddr() {
		return this.ipAddr;
	}

	/**
	 * 设置登陆IP. 
	 */
	public void setIpAddr(String ipAddr) {
		this.ipAddr = ipAddr;
	}

}
