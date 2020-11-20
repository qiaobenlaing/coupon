package cn.suanzi.baomi.base.pojo;

public class actDetail implements java.io.Serializable {

	/**
	 * 活动主题
	 */
	private String activityName;
	
	/**
	 * 发起时间
	 */
	private String createTime;
	
	/**
	 * 商家名称
	 */
	private String shopName;
	
	/**
	 * 活动图片
	 */
	private String activityImg;
	
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
	 * 活动参与人数上限
	 */
	private String limitedParticipators;
	
	/**
	 * 是否需要报名
	 */
	private String isRegisterRequired;
	
	/**
	 * 是否需要预付款
	 */
	private String isPrepayRequired;
	
	/**
	 * 预付款金额
	 */
	private String prePayment;
	
	/**
	 * 活动说明
	 */
	private String txtContent;
	
	public actDetail() {
		super();
	}

	public actDetail(String activityName, String createTime, String shopName,
			String activityImg, String startTime, String endTime,
			String activityLocation, String limitedParticipators,
			String isRegisterRequired, String isPrepayRequired,
			String prePayment, String txtContent) {
		super();
		this.activityName = activityName;
		this.createTime = createTime;
		this.shopName = shopName;
		this.activityImg = activityImg;
		this.startTime = startTime;
		this.endTime = endTime;
		this.activityLocation = activityLocation;
		this.limitedParticipators = limitedParticipators;
		this.isRegisterRequired = isRegisterRequired;
		this.isPrepayRequired = isPrepayRequired;
		this.prePayment = prePayment;
		this.txtContent = txtContent;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getActivityImg() {
		return activityImg;
	}

	public void setActivityImg(String activityImg) {
		this.activityImg = activityImg;
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

	public String getLimitedParticipators() {
		return limitedParticipators;
	}

	public void setLimitedParticipators(String limitedParticipators) {
		this.limitedParticipators = limitedParticipators;
	}

	public String getIsRegisterRequired() {
		return isRegisterRequired;
	}

	public void setIsRegisterRequired(String isRegisterRequired) {
		this.isRegisterRequired = isRegisterRequired;
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

	public String getTxtContent() {
		return txtContent;
	}

	public void setTxtContent(String txtContent) {
		this.txtContent = txtContent;
	}
	
	
}
