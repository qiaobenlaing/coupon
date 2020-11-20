package cn.suanzi.baomi.base.pojo;

import java.util.List;

public class ActivityEdit implements java.io.Serializable{

	/**
	 * 活动编码
	 */
	private String activityCode;
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
	private String richTextContent;
	/**
	 * 活动参与人数上限
	 */
	private String limitedParticipators;
	/**
	 * 活动图片
	 */
	private String activityImg;
	/**
	 * 活动类型
	 */
	private String actType;
	/**
	 * 联系人
	 */
	private String contactName;
	/**
	 * 联系方式
	 */
	private String contactMobileNbr;
	
	/**
	 * 收费规格
	 */
	private List<PromotionPrice> feeScale;
	
	/**
	 * 退款要求
	 */
	private String refundRequired;
	
	/**
	 * 单人报名人数限制
	 */
	private String registerNbrRequired;
	
	/**
	 * 活动类型 提前预约天数
	 */
	private String dayOfBooking;
	
	

	public ActivityEdit() {
		super();
	}



	public ActivityEdit(String activityCode, String activityName, String startTime, String endTime,
			String activityLocation, String richTextContent, String limitedParticipators, String activityImg,
			String actType, String contactName, String contactMobileNbr, List<PromotionPrice> feeScale,
			String refundRequired, String registerNbrRequired, String dayOfBooking) {
		super();
		this.activityCode = activityCode;
		this.activityName = activityName;
		this.startTime = startTime;
		this.endTime = endTime;
		this.activityLocation = activityLocation;
		this.richTextContent = richTextContent;
		this.limitedParticipators = limitedParticipators;
		this.activityImg = activityImg;
		this.actType = actType;
		this.contactName = contactName;
		this.contactMobileNbr = contactMobileNbr;
		this.feeScale = feeScale;
		this.refundRequired = refundRequired;
		this.registerNbrRequired = registerNbrRequired;
		this.dayOfBooking = dayOfBooking;
	}



	public String getActivityCode() {
		return activityCode;
	}

	public void setActivityCode(String activityCode) {
		this.activityCode = activityCode;
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

	public String getRichTextContent() {
		return richTextContent;
	}

	public void setRichTextContent(String richTextContent) {
		this.richTextContent = richTextContent;
	}

	public String getLimitedParticipators() {
		return limitedParticipators;
	}

	public void setLimitedParticipators(String limitedParticipators) {
		this.limitedParticipators = limitedParticipators;
	}

	public String getActivityImg() {
		return activityImg;
	}

	public void setActivityImg(String activityImg) {
		this.activityImg = activityImg;
	}

	public String getActType() {
		return actType;
	}

	public void setActType(String actType) {
		this.actType = actType;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getContactMobileNbr() {
		return contactMobileNbr;
	}

	public void setContactMobileNbr(String contactMobileNbr) {
		this.contactMobileNbr = contactMobileNbr;
	}
	public List<PromotionPrice> getFeeScale() {
		return feeScale;
	}

	public void setFeeScale(List<PromotionPrice> feeScale) {
		this.feeScale = feeScale;
	}



	public String getRefundRequired() {
		return refundRequired;
	}

	public void setRefundRequired(String refundRequired) {
		this.refundRequired = refundRequired;
	}

	public String getRegisterNbrRequired() {
		return registerNbrRequired;
	}

	public void setRegisterNbrRequired(String registerNbrRequired) {
		this.registerNbrRequired = registerNbrRequired;
	}

	public String getDayOfBooking() {
		return dayOfBooking;
	}

	public void setDayOfBooking(String dayOfBooking) {
		this.dayOfBooking = dayOfBooking;
	}
	
}
