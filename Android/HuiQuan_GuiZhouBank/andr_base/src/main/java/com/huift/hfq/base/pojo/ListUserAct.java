package com.huift.hfq.base.pojo;

/**
 * 顾客的活动列表
 * @author wensi.yu
 *
 */
public class ListUserAct  implements java.io.Serializable{
	
	/**
	 * 活动编码
	 */
	private String activityCode;

	/**
	 * 用户活动编码
	 */
	private String userActivityCode;
	
	/**
	 * 活动图片
	 */
	private String activityLogo;
	
	/**
	 * 活动标题
	 */
	private String activityName;
	
	/**
	 * 店铺名称
	 */
	private String shopName;
	
	/**
	 * 距离
	 */
	private String distance;
	
	/**
	 * 报名时间
	 */
	private String signUpTime;
	
	/**
	 * 人数
	 */
	private String personCount;

	public ListUserAct() {
		super();
	}


	
	
	
	public ListUserAct(String activityCode, String userActivityCode,
			String activityLogo, String activityName, String shopName,
			String distance, String signUpTime, String personCount) {
		super();
		this.activityCode = activityCode;
		this.userActivityCode = userActivityCode;
		this.activityLogo = activityLogo;
		this.activityName = activityName;
		this.shopName = shopName;
		this.distance = distance;
		this.signUpTime = signUpTime;
		this.personCount = personCount;
	}


	


	public String getActivityCode() {
		return activityCode;
	}





	public void setActivityCode(String activityCode) {
		this.activityCode = activityCode;
	}





	public String getUserActivityCode() {
		return userActivityCode;
	}


	public void setUserActivityCode(String userActivityCode) {
		this.userActivityCode = userActivityCode;
	}


	public String getActivityLogo() {
		return activityLogo;
	}

	public void setActivityLogo(String activityLogo) {
		this.activityLogo = activityLogo;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getSignUpTime() {
		return signUpTime;
	}

	public void setSignUpTime(String signUpTime) {
		this.signUpTime = signUpTime;
	}

	public String getPersonCount() {
		return personCount;
	}

	public void setPersonCount(String personCount) {
		this.personCount = personCount;
	}
	
	
	
}
