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

public class UserLoginLog implements java.io.Serializable {

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
	 * 登陆时间.
	 */
	private Date loginTime;

	/** 
	 * 登陆IP.
	 */
	private String ipAddr;

	/** 
	 * 登陆序号，逐次递增.
	 */
	private Long seq;

	public UserLoginLog() {
	}

	public UserLoginLog(String logCode) {
		this.logCode = logCode;
	}

	public UserLoginLog(String logCode, String userId, Date loginTime,
			String ipAddr, Long seq) {
		this.logCode = logCode;
		this.userId = userId;
		this.loginTime = loginTime;
		this.ipAddr = ipAddr;
		this.seq = seq;
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
	 * 获取登陆时间. 
	 */
	public Date getLoginTime() {
		return this.loginTime;
	}

	/**
	 * 设置登陆时间. 
	 */
	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
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

	/**
	 * 获取登陆序号，逐次递增. 
	 */
	public Long getSeq() {
		return this.seq;
	}

	/**
	 * 设置登陆序号，逐次递增. 
	 */
	public void setSeq(Long seq) {
		this.seq = seq;
	}

}
