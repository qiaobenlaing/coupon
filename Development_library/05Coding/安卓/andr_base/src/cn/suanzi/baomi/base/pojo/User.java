/**
 *
 * @Author: Jianping Chen
 * @Date: 2015.5.8
 * @Version: 1.0.0
 * @Copyright Suanzi Co.,Ltd. @ 2015
 * 
 */

package cn.suanzi.baomi.base.pojo;

import java.util.List;

public class User implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	/** 
	 * 主键，用户编码.
	 */
	private String userCode;

	/** 
	 * 用户ID；需建索引。.
	 */
	private String userId;

	/** 
	 * 手机号码；需建索引。.
	 */
	private String mobileNbr;

	/** 
	 * 密码MD5.
	 */
	private String password;

	/** 
	 * 昵称.
	 */
	private String nickName;

	/** 
	 * 姓名.
	 */
	private String realName;

	/** 
	 * 性别；'M':男；'F'：女；'U':未知: unknown.
	 */
	private String sex;

	/** 
	 * 所在省份.
	 */
	private String province;

	/** 
	 * 所在城市.
	 */
	private String city;

	/** 
	 * 详细地址.
	 */
	private String addr;

	/** 
	 * 微信OpenID.
	 */
	private String wechatId;

	/** 
	 * 微信用户名.
	 */
	private String wechatUserName;

	/** 
	 * qq号.
	 */
	private String qq;

	/** 
	 * 注册时间.
	 */
	private String registerTime;

	/** 
	 * 最后一次登陆日期和时间.
	 */
	private String lastLoginTime;

	/** 
	 * 用户状态.
	 */
	private String status;

	/** 
	 * 用户邮箱.
	 */
	private String email;

	/** 
	 * 用户积分.
	 */
	private String userPoints;

	/** 
	 * 用户等级.
	 */
	private String userLvl;

	/** 
	 * 用户头像.
	 */
	private String avatarUrl;

	/** 
	 * 用户个性签名.
	 */
	private String signature;

	/** 
	 * 区或县（如西湖区）.
	 */
	private String district;
	
	/**
	 * 是否开启了免验证码支付
	 */
	private String freeValCodePay;
	
	/**
	 * 是否设置了支付密码
	 */
	private String isUserSetPayPwd;
	
	/**
	 * 用户订票具体信息
	 */
	private List<UserMessage> feeScale;
	
	/**
	 * 购票数量
	 */
	private int totalNbr;
	
	public User() {
	}

	public User(String userCode, String userId, String mobileNbr, String password, String nickName, String realName,
			String sex, String province, String city, String addr, String wechatId, String wechatUserName, String qq,
			String registerTime, String lastLoginTime, String status, String email, String userPoints, String userLvl,
			String avatarUrl, String signature, String district, String freeValCodePay, String isUserSetPayPwd,
			List<UserMessage> feeScale, int totalNbr) {
		super();
		this.userCode = userCode;
		this.userId = userId;
		this.mobileNbr = mobileNbr;
		this.password = password;
		this.nickName = nickName;
		this.realName = realName;
		this.sex = sex;
		this.province = province;
		this.city = city;
		this.addr = addr;
		this.wechatId = wechatId;
		this.wechatUserName = wechatUserName;
		this.qq = qq;
		this.registerTime = registerTime;
		this.lastLoginTime = lastLoginTime;
		this.status = status;
		this.email = email;
		this.userPoints = userPoints;
		this.userLvl = userLvl;
		this.avatarUrl = avatarUrl;
		this.signature = signature;
		this.district = district;
		this.freeValCodePay = freeValCodePay;
		this.isUserSetPayPwd = isUserSetPayPwd;
		this.feeScale = feeScale;
		this.totalNbr = totalNbr;
	}




	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getWechatId() {
		return wechatId;
	}

	public void setWechatId(String wechatId) {
		this.wechatId = wechatId;
	}

	public String getWechatUserName() {
		return wechatUserName;
	}

	public void setWechatUserName(String wechatUserName) {
		this.wechatUserName = wechatUserName;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(String registerTime) {
		this.registerTime = registerTime;
	}

	public String getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(String lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUserPoints() {
		return userPoints;
	}

	public void setUserPoints(String userPoints) {
		this.userPoints = userPoints;
	}

	public String getUserLvl() {
		return userLvl;
	}

	public void setUserLvl(String userLvl) {
		this.userLvl = userLvl;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	public String getFreeValCodePay() {
		return freeValCodePay;
	}


	public void setFreeValCodePay(String freeValCodePay) {
		this.freeValCodePay = freeValCodePay;
	}

	public String getIsUserSetPayPwd() {
		return isUserSetPayPwd;
	}

	public void setIsUserSetPayPwd(String isUserSetPayPwd) {
		this.isUserSetPayPwd = isUserSetPayPwd;
	}

	public List<UserMessage> getFeeScale() {
		return feeScale;
	}

	public void setFeeScale(List<UserMessage> feeScale) {
		this.feeScale = feeScale;
	}

	public int getTotalNbr() {
		return totalNbr;
	}

	public void setTotalNbr(int totalNbr) {
		this.totalNbr = totalNbr;
	}
}
