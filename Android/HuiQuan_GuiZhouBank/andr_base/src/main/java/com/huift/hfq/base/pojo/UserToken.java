/**
 *
 * @Author: Jianping Chen
 * @Date: 2015.5.8
 * @Version: 1.0.0
 * @Copyright Suanzi Co.,Ltd. @ 2015
 * 
 */

package com.huift.hfq.base.pojo;

/**
 * 用于存储用户验证及token信息到本地，用于自动登录。
 * 
 * @author Jianping Chen
 * 			Weiping modified
 */
public class UserToken implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 商家的编码.
	 */
	private String staffCode;

	/**
	 * 用户手机号码
	 */
	private String mobileNbr;
	
	/**
	 * 加密过的用户密码，跟User中的Password冗余，便于app存取。
	 */
	private String password = null;

	/** 
	 * Token.
	 */
	private String tokenCode;
	
	/** 
	 * 商店Code
	 */
	private String shopCode;
	/**
	 * id
	 */
	private String id;

	/**
	 * 商圈id
	 */
	private String zoneId;
	
	/** 
	 * 过期时间，unix_timestamp，精确到秒.
	 */
	private long mExpiresAt;

	/**
	 * 商家端登陆状态
	 */
	private String userLvl;

	public String getTokenCode() {
		return this.tokenCode;
	}

	public void setTokenCode(String tokenCode) {
		this.tokenCode = tokenCode;
	}

	public String getMobileNbr() {
		return mobileNbr;
	}

	public void setMobileNbr(String mobileNbr) {
		this.mobileNbr = mobileNbr;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getStaffCode() {
		return staffCode;
	}

	public void setStaffCode(String staffCode) {
		this.staffCode = staffCode;
	}

	public String getShopCode() {
		return shopCode;
	}

	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}

	public long getExpiresAt() {
		return mExpiresAt;
	}

	public void setExpiresAt(long expireTime) {
		this.mExpiresAt = expireTime;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserLvl() {
		return userLvl;
	}

	public void setUserLvl(String userLvl) {
		this.userLvl = userLvl;
	}

	public String getZoneId() {
		return zoneId;
	}

	public void setZoneId(String zoneId) {
		this.zoneId = zoneId;
	}
}
