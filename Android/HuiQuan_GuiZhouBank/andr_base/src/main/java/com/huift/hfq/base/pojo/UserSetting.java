/**
 *
 * @Author: Jianping Chen
 * @Date: 2015.5.8
 * @Version: 1.0.0
 * @Copyright Suanzi Co.,Ltd. @ 2015
 * 
 */

package com.huift.hfq.base.pojo;

public class UserSetting implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	/** 
	 * 主键.
	 */
	private String settingCode;

	/** 
	 * 用户编码.
	 */
	private String userCode;

	/** 
	 * 是否接受广播消息.
	 */
	private Boolean isBroadcastOn;

	/** 
	 * 是否开启消息提醒声音.
	 */
	private Boolean isMsgBingOn;

	/** 
	 * 是否接受优惠券推送消息.
	 */
	private Boolean isCouponMsgOn;

	public UserSetting() {
	}

	public UserSetting(String settingCode) {
		this.settingCode = settingCode;
	}

	public UserSetting(String settingCode, String userCode,
			Boolean isBroadcastOn, Boolean isMsgBingOn, Boolean isCouponMsgOn) {
		this.settingCode = settingCode;
		this.userCode = userCode;
		this.isBroadcastOn = isBroadcastOn;
		this.isMsgBingOn = isMsgBingOn;
		this.isCouponMsgOn = isCouponMsgOn;
	}

	/**
	 * 获取 主键. 
	 */
	public String getSettingCode() {
		return this.settingCode;
	}

	/**
	 * 设置 主键. 
	 */
	public void setSettingCode(String settingCode) {
		this.settingCode = settingCode;
	}

	/**
	 * 获取 用户编码. 
	 */
	public String getUserCode() {
		return this.userCode;
	}

	/**
	 * 设置 用户编码. 
	 */
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	/**
	 * 获取 是否接受广播消息. 
	 */
	public Boolean getIsBroadcastOn() {
		return this.isBroadcastOn;
	}

	/**
	 * 设置 是否接受广播消息. 
	 */
	public void setIsBroadcastOn(Boolean isBroadcastOn) {
		this.isBroadcastOn = isBroadcastOn;
	}

	/**
	 * 获取 是否开启消息提醒声音. 
	 */
	public Boolean getIsMsgBingOn() {
		return this.isMsgBingOn;
	}

	/**
	 * 设置 是否开启消息提醒声音. 
	 */
	public void setIsMsgBingOn(Boolean isMsgBingOn) {
		this.isMsgBingOn = isMsgBingOn;
	}

	/**
	 * 获取 是否接受优惠券推送消息. 
	 */
	public Boolean getIsCouponMsgOn() {
		return this.isCouponMsgOn;
	}

	/**
	 * 设置 是否接受优惠券推送消息. 
	 */
	public void setIsCouponMsgOn(Boolean isCouponMsgOn) {
		this.isCouponMsgOn = isCouponMsgOn;
	}

}
