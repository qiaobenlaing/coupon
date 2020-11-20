/**
 *
 * @Author: Jianping Chen
 * @Date: 2015.5.8
 * @Version: 1.0.0
 * @Copyright Suanzi Co.,Ltd. @ 2015
 * 
 */

package cn.suanzi.baomi.base.pojo;

import java.util.Date;

public class UserActionLog implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	/** 
	 * 主键.
	 */
	private String logCode;

	/** 
	 * 用户ID.
	 */
	private String userId;

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

	public UserActionLog() {
	}

	public UserActionLog(String logCode) {
		this.logCode = logCode;
	}

	public UserActionLog(String logCode, String userId, String actionDes,
			Date actionTime, String ipAddr) {
		this.logCode = logCode;
		this.userId = userId;
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
	 * 获取用户ID. 
	 */
	public String getUserId() {
		return this.userId;
	}

	/**
	 * 设置用户ID. 
	 */
	public void setUserId(String userId) {
		this.userId = userId;
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
