package com.huift.hfq.base.pojo;

/**
 * 添加营销活动的实体
 * 
 * @author wensi.yu
 *
 */
public class ActAdd implements java.io.Serializable {

	/**
	 * 活动主题
	 */
	private String activityName;
	
	/**
	 * 活动开始时间
	 */
	private String startTime;
	
	/**
	 * 活动结束时间
	 */
	private String endTime;
	
	/**
	 * 活动地点
	 */
	private String activityLocation;
	
	/**
	 * 活动说明
	 */
	private String txtContent;
	
	/**
	 * 活动参与人数上限
	 */
	private String limitedParticipators;
	
	/**
	 * 是否需要预付费
	 */
	private String isPrepayRequired;
	
	/**
	 * 预付金额
	 */
	private String prePayment;
	
	/**
	 * 是否需要报名
	 */
	private String isRegisterRequired;
	
	/**
	 * 活动图片
	 */
	private String activityImg;
	
	/**
	 * 活动方形图片
	 */
	private String activityLogo;
	
	/**
	 * 发起活动的商家编码
	 */
	private String shopCode;
	
	/**
	 * 活动发起人编码
	 */
	private String creatorCode;
	
	
	public ActAdd() {
		super();
	}
	
	public ActAdd(String activityName, String startTime, String activityLocation) {
		super();
		this.activityName = activityName;
		this.startTime = startTime;
		this.activityLocation = activityLocation;
	}

	


	public ActAdd(String txtContent) {
		super();
		this.txtContent = txtContent;
	}




	public ActAdd(String activityName, String startTime, String endTime,
			String activityLocation, String txtContent,
			String limitedParticipators, String isPrepayRequired,
			String prePayment, String isRegisterRequired, String activityImg,
			String activityLogo, String shopCode, String creatorCode) {
		super();
		this.activityName = activityName;
		this.startTime = startTime;
		this.endTime = endTime;
		this.activityLocation = activityLocation;
		this.txtContent = txtContent;
		this.limitedParticipators = limitedParticipators;
		this.isPrepayRequired = isPrepayRequired;
		this.prePayment = prePayment;
		this.isRegisterRequired = isRegisterRequired;
		this.activityImg = activityImg;
		this.activityLogo = activityLogo;
		this.shopCode = shopCode;
		this.creatorCode = creatorCode;
	}
	public String getActivityName() {
		return activityName;
	}
	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getActivityLocation() {
		return activityLocation;
	}
	public void setActivityLocation(String activityLocation) {
		this.activityLocation = activityLocation;
	}
	public String getTxtContent() {
		return txtContent;
	}
	public void setTxtContent(String txtContent) {
		this.txtContent = txtContent;
	}
	public String getLimitedParticipators() {
		return limitedParticipators;
	}
	public void setLimitedParticipators(String limitedParticipators) {
		this.limitedParticipators = limitedParticipators;
	}
	public String getIsPrepayRequired() {
		return isPrepayRequired;
	}
	public void setIsPrepayRequired(String isPrepayRequired) {
		this.isPrepayRequired = isPrepayRequired;
	}
	public String getPrePayment() {
		return prePayment;
	}
	public void setPrePayment(String prePayment) {
		this.prePayment = prePayment;
	}
	public String getIsRegisterRequired() {
		return isRegisterRequired;
	}
	public void setIsRegisterRequired(String isRegisterRequired) {
		this.isRegisterRequired = isRegisterRequired;
	}
	public String getActivityImg() {
		return activityImg;
	}
	public void setActivityImg(String activityImg) {
		this.activityImg = activityImg;
	}
	
	public String getActivityLogo() {
		return activityLogo;
	}
	public void setActivityLogo(String activityLogo) {
		this.activityLogo = activityLogo;
	}
	public String getShopCode() {
		return shopCode;
	}
	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}
	public String getCreatorCode() {
		return creatorCode;
	}
	public void setCreatorCode(String creatorCode) {
		this.creatorCode = creatorCode;
	}
	
	
	
}

