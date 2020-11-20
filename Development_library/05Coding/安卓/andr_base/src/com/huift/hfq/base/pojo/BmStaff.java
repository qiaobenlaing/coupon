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

public class BmStaff implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	/** 
	 * 主键.
	 */
	private String staffCode;

	/** 
	 * 用户ID；需建索引。.
	 */
	private String staffId;

	/** 
	 * 手机号码；需建索引。.
	 */
	private long mobileNbr;

	/** 
	 * 密码MD5.
	 */
	private String password;

	/** 
	 * 姓名.
	 */
	private String realName;

	/** 
	 * 性别；'M':男；'F'：女；'U':未知: unknown.
	 */
	private Character sex;

	/** 
	 * 所在省份.
	 */
	private Integer province;

	/** 
	 * 所在城市.
	 */
	private Integer city;

	/** 
	 * 区或县（如西湖区）.
	 */
	private Integer district;

	/** 
	 * 微信OpenID.
	 */
	private String wechatId;

	/** 
	 * 微信用户名.
	 */
	private String wechatUserName;

	/** 
	 * 注册时间.
	 */
	private Date registerTime;

	/** 
	 * 最后一次登陆日期和时间.
	 */
	private Date lastLoginTime;

	/** 
	 * 用户状态.
	 */
	private Byte status;

	/** 
	 * 用户邮箱.
	 */
	private String email;

	/** 
	 * 用户积分.
	 */
	private Long userPoints;

	/** 
	 * 用户等级.
	 */
	private Integer userLvl;

	/** 
	 * 用户头像.
	 */
	private String avatarUrl;

	/** 
	 * qq号.
	 */
	private Long qq;

	public BmStaff() {
	}

	public BmStaff(String staffCode, long mobileNbr) {
		this.staffCode = staffCode;
		this.mobileNbr = mobileNbr;
	}

	public BmStaff(String staffCode, String staffId, long mobileNbr,
			String password, String realName, Character sex, Integer province,
			Integer city, Integer district, String wechatId,
			String wechatUserName, Date registerTime, Date lastLoginTime,
			Byte status, String email, Long userPoints, Integer userLvl,
			String avatarUrl, Long qq) {
		this.staffCode = staffCode;
		this.staffId = staffId;
		this.mobileNbr = mobileNbr;
		this.password = password;
		this.realName = realName;
		this.sex = sex;
		this.province = province;
		this.city = city;
		this.district = district;
		this.wechatId = wechatId;
		this.wechatUserName = wechatUserName;
		this.registerTime = registerTime;
		this.lastLoginTime = lastLoginTime;
		this.status = status;
		this.email = email;
		this.userPoints = userPoints;
		this.userLvl = userLvl;
		this.avatarUrl = avatarUrl;
		this.qq = qq;
	}

	/**
	 * 获取主键. 
	 */
	public String getStaffCode() {
		return this.staffCode;
	}

	/**
	 * 设置主键. 
	 */
	public void setStaffCode(String staffCode) {
		this.staffCode = staffCode;
	}

	/**
	 * 获取用户ID；需建索引。. 
	 */
	public String getStaffId() {
		return this.staffId;
	}

	/**
	 * 设置用户ID；需建索引。. 
	 */
	public void setStaffId(String staffId) {
		this.staffId = staffId;
	}

	/**
	 * 获取手机号码；需建索引。. 
	 */
	public long getMobileNbr() {
		return this.mobileNbr;
	}

	/**
	 * 设置手机号码；需建索引。. 
	 */
	public void setMobileNbr(long mobileNbr) {
		this.mobileNbr = mobileNbr;
	}

	/**
	 * 获取密码MD5. 
	 */
	public String getPassword() {
		return this.password;
	}

	/**
	 * 设置密码MD5. 
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * 获取姓名. 
	 */
	public String getRealName() {
		return this.realName;
	}

	/**
	 * 设置姓名. 
	 */
	public void setRealName(String realName) {
		this.realName = realName;
	}

	/**
	 * 获取性别；'M':男；'F'：女；'U':未知: unknown. 
	 */
	public Character getSex() {
		return this.sex;
	}

	/**
	 * 设置性别；'M':男；'F'：女；'U':未知: unknown. 
	 */
	public void setSex(Character sex) {
		this.sex = sex;
	}

	/**
	 * 获取所在省份. 
	 */
	public Integer getProvince() {
		return this.province;
	}

	/**
	 * 设置所在省份. 
	 */
	public void setProvince(Integer province) {
		this.province = province;
	}

	/**
	 * 获取所在城市. 
	 */
	public Integer getCity() {
		return this.city;
	}

	/**
	 * 设置所在城市. 
	 */
	public void setCity(Integer city) {
		this.city = city;
	}

	/**
	 * 获取区或县（如西湖区）. 
	 */
	public Integer getDistrict() {
		return this.district;
	}

	/**
	 * 设置区或县（如西湖区）. 
	 */
	public void setDistrict(Integer district) {
		this.district = district;
	}

	/**
	 * 获取微信OpenID. 
	 */
	public String getWechatId() {
		return this.wechatId;
	}

	/**
	 * 设置微信OpenID. 
	 */
	public void setWechatId(String wechatId) {
		this.wechatId = wechatId;
	}

	/**
	 * 获取微信用户名. 
	 */
	public String getWechatUserName() {
		return this.wechatUserName;
	}

	/**
	 * 设置微信用户名. 
	 */
	public void setWechatUserName(String wechatUserName) {
		this.wechatUserName = wechatUserName;
	}

	/**
	 * 获取注册时间. 
	 */
	public Date getRegisterTime() {
		return this.registerTime;
	}

	/**
	 * 设置注册时间. 
	 */
	public void setRegisterTime(Date registerTime) {
		this.registerTime = registerTime;
	}

	/**
	 * 获取最后一次登陆日期和时间. 
	 */
	public Date getLastLoginTime() {
		return this.lastLoginTime;
	}

	/**
	 * 设置最后一次登陆日期和时间. 
	 */
	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	/**
	 * 获取用户状态. 
	 */
	public Byte getStatus() {
		return this.status;
	}

	/**
	 * 设置用户状态. 
	 */
	public void setStatus(Byte status) {
		this.status = status;
	}

	/**
	 * 获取用户邮箱. 
	 */
	public String getEmail() {
		return this.email;
	}

	/**
	 * 设置用户邮箱. 
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * 获取用户积分. 
	 */
	public Long getUserPoints() {
		return this.userPoints;
	}

	/**
	 * 设置用户积分. 
	 */
	public void setUserPoints(Long userPoints) {
		this.userPoints = userPoints;
	}

	/**
	 * 获取用户等级. 
	 */
	public Integer getUserLvl() {
		return this.userLvl;
	}

	/**
	 * 设置用户等级. 
	 */
	public void setUserLvl(Integer userLvl) {
		this.userLvl = userLvl;
	}

	/**
	 * 获取用户头像. 
	 */
	public String getAvatarUrl() {
		return this.avatarUrl;
	}

	/**
	 * 设置用户头像. 
	 */
	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	/**
	 * 获取qq号. 
	 */
	public Long getQq() {
		return this.qq;
	}

	/**
	 * 设置qq号. 
	 */
	public void setQq(Long qq) {
		this.qq = qq;
	}

}
