/**
 *
 * @Author: Jianping Chen
 * @Date: 2015.5.8
 * @Version: 1.0.0
 * @Copyright Suanzi Co.,Ltd. @ 2015
 * 
 */

package cn.suanzi.baomi.base.pojo;

/**
 * 用于存储用户验证及token信息到本地，用于自动登录。
 * 
 * @author Jianping Chen
 * 			Weiping modified
 */
public class UserToken implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	/** 
	 * 主键.
	 */
	private String userTokenCode;

	/** 
	 * 用户的编码.
	 */
	private String userCode;

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
	 * 过期时间，unix_timestamp，精确到秒.
	 */
	private long mExpiresAt;
	/**
	 * 商家端登陆状态
	 */
	private String userLvl;

	public UserToken() {
	}
	
	public UserToken(String mobileNbr, String password) {
		this.mobileNbr = mobileNbr;
		this.password = password;
	}
	
	public UserToken(String userCode, String tokenCode, long expireTime) {
		this.tokenCode = tokenCode;
		this.userCode = userCode;
		this.mExpiresAt = expireTime;
	}

	public UserToken(String mobileNbr, String password, String userCode, String tokenCode, long expireTime) {
		this(mobileNbr, password);
		this.userCode = userCode;
		this.tokenCode = tokenCode;
		this.mExpiresAt = expireTime;
	}

	/**
	 * 取得 主键. 
	 */
	public String getUserTokenCode() {
		return this.userTokenCode;
	}

	/**
	 * 设置 主键. 
	 */
	public void setUserTokenCode(String userTokenCode) {
		this.userTokenCode = userTokenCode;
	}

	/**
	 * 取得 Token的编码. 
	 */
	public String getTokenCode() {
		return this.tokenCode;
	}

	/**
	 * 设置 Token的编码. 
	 */
	public void setTokenCode(String tokenCode) {
		this.tokenCode = tokenCode;
	}

	/**
	 * 取得 用户的编码. 
	 */
	public String getUserCode() {
		return this.userCode;
	}

	/**
	 * 设置 用户的编码. 
	 */
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	/**
	 * @return the mobileNbr
	 */
	public String getMobileNbr() {
		return mobileNbr;
	}

	/**
	 * @param mobileNbr the mobileNbr to set
	 */
	public void setMobileNbr(String mobileNbr) {
		this.mobileNbr = mobileNbr;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the staffCode
	 */
	public String getStaffCode() {
		return staffCode;
	}

	/**
	 * @param staffCode the staffCode to set
	 */
	public void setStaffCode(String staffCode) {
		this.staffCode = staffCode;
	}

	/**
	 * @return the shopCode
	 */
	public String getShopCode() {
		return shopCode;
	}

	/**
	 * @param shopCode the shopCode to set
	 */
	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}

	/**
	 * @return the expiresAt
	 */
	public long getExpiresAt() {
		return mExpiresAt;
	}

	/**
	 * 设置 过期时间. 
	 */
	public void setExpiresAt(long expireTime) {
		this.mExpiresAt = expireTime;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 登陆者的状态
	 */
	public String getUserLvl() {
		return userLvl;
	}

	public void setUserLvl(String userLvl) {
		this.userLvl = userLvl;
	}
	
}
