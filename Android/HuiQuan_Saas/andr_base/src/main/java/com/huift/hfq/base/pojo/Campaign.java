package com.huift.hfq.base.pojo;

import java.util.List;

/**
 * 活动列表
 * @author wensi.yu
 *
 */
public class Campaign implements java.io.Serializable{
	
	/**
	 * 活动编码
	 */
	private String activityCode;
	
	/**
	 * 联系人
	 */
	private String contactName;
	
	/**
	 * 联系电话
	 */
	private String contactMobileNbr;
	
	/**
	 * 活动类型
	 */
	private String actType;
	
	/**
	 * 活动地址
	 */
	private String activityLocation;

	/**
	 * 开始时间
	 */
	private String startTime;
	
	/**
	 * 结束时间
	 */
	private String  endTime;
	
	/**
	 * 活动状态
	 */
	private String status;
	
	/**
	 * 活动图片
	 */
	private String activityImg;
	
	/**
	 * 活动名称
	 */
	private String activityName;
	
	/**
	 * 活动内容
	 */
	private String txtContent;
	
	/**
	 * 参与人数
	 */
	private String participators;
	
	/**
	 * 限制人数
	 */
	private String limitedParticipators;
	
	/**
	 * 活动总金额
	 */
	private String totalPayment;
	
	/**
	 * 金额的最小区间
	 */
	private String minPrice;
	
	/**
	 * 活动类型的value
	 */
	private String value;
	
	/**
	 * 活动类型的name
	 */
	private String name;
	
	/**
	 * 选中
	 */
	private boolean checked = false;
	
	/**
	 * 活动简介
	 */
	private String richTextContent ;
	
	/**
	 * 退款的key
	 */
	private String refundValue;
	
	/**
	 * 退款的name
	 */
	private String refundName;

	/**
	 * 活动规格
	 */
	private List<PromotionPrice> feeScale;

	/**
	 * 收藏量
	 */
	private String collectNbr;
	
	/**
	 * 阅读量
	 */
	private String pageviews;
	
	/**
	 * 活动内容
	 */
	private List<ActivityUpLaod> upload;
	
	/**
	 * 退款信息
	 */
	private String refundRequired;
	
	/**
	 * 提前多少天预约
	 */
	private String dayOfBooking;
	
	/**
	 * 单人报名人数限制
	 */
	private String registerNbrRequired;
	
	public Campaign() {
		super();
	}
	
	public Campaign(String activityCode, String contactName, String contactMobileNbr, String actType,
			String activityLocation, String startTime, String endTime, String status, String activityImg,
			String activityName, String txtContent, String participators, String limitedParticipators,
			String totalPayment, String minPrice, String value, String name, boolean checked, String refundValue,
			String refundName, List<PromotionPrice> feeScale, String collectNbr, String pageviews,
			List<ActivityUpLaod> upload, String refundRequired, String dayOfBooking, String registerNbrRequired,
			String richTextContent) {
		super();
		this.activityCode = activityCode;
		this.contactName = contactName;
		this.contactMobileNbr = contactMobileNbr;
		this.actType = actType;
		this.activityLocation = activityLocation;
		this.startTime = startTime;
		this.endTime = endTime;
		this.status = status;
		this.activityImg = activityImg;
		this.activityName = activityName;
		this.txtContent = txtContent;
		this.participators = participators;
		this.limitedParticipators = limitedParticipators;
		this.totalPayment = totalPayment;
		this.minPrice = minPrice;
		this.value = value;
		this.name = name;
		this.checked = checked;
		this.refundValue = refundValue;
		this.refundName = refundName;
		this.feeScale = feeScale;
		this.collectNbr = collectNbr;
		this.pageviews = pageviews;
		this.upload = upload;
		this.refundRequired = refundRequired;
		this.dayOfBooking = dayOfBooking;
		this.registerNbrRequired = this.registerNbrRequired;
		this.richTextContent = richTextContent;
	}

	public String getActivityCode() {
		return activityCode;
	}

	public void setActivityCode(String activityCode) {
		this.activityCode = activityCode;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getActivityImg() {
		return activityImg;
	}

	public void setActivityImg(String activityImg) {
		this.activityImg = activityImg;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public String getTxtContent() {
		return txtContent;
	}

	public void setTxtContent(String txtContent) {
		this.txtContent = txtContent;
	}

	public String getParticipators() {
		return participators;
	}

	public void setParticipators(String participators) {
		this.participators = participators;
	}

	public String getLimitedParticipators() {
		return limitedParticipators;
	}

	public void setLimitedParticipators(String limitedParticipators) {
		this.limitedParticipators = limitedParticipators;
	}

	public String getTotalPayment() {
		return totalPayment;
	}

	public void setTotalPayment(String totalPayment) {
		this.totalPayment = totalPayment;
	}

	public String getMinPrice() {
		return minPrice;
	}

	public void setMinPrice(String minPrice) {
		this.minPrice = minPrice;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean getChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public String getRefundValue() {
		return refundValue;
	}

	public void setRefundValue(String refundValue) {
		this.refundValue = refundValue;
	}

	public String getRefundName() {
		return refundName;
	}

	public void setRefundName(String refundName) {
		this.refundName = refundName;
	}



	public List<PromotionPrice> getFeeScale() {
		return feeScale;
	}

	public void setFeeScale(List<PromotionPrice> feeScale) {
		this.feeScale = feeScale;
	}
	public String getCollectNbr() {
		return collectNbr;
	}

	public void setCollectNbr(String collectNbr) {
		this.collectNbr = collectNbr;
	}

	public String getPageviews() {
		return pageviews;
	}

	public void setPageviews(String pageviews) {
		this.pageviews = pageviews;
	}

	public List<ActivityUpLaod> getUpload() {
		return upload;
	}

	public void setUpload(List<ActivityUpLaod> upload) {
		this.upload = upload;
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

	public String getRefundRequired() {
		return refundRequired;
	}

	public void setRefundRequired(String refundRequired) {
		this.refundRequired = refundRequired;
	}

	public String getDayOfBooking() {
		return dayOfBooking;
	}

	public void setDayOfBooking(String dayOfBooking) {
		this.dayOfBooking = dayOfBooking;
	}

	public String getRegisterNbrRequired() {
		return registerNbrRequired;
	}

	public void setRegisterNbrRequired(String registerNbrRequired) {
		this.registerNbrRequired = registerNbrRequired;
	}

	public String getRichTextContent() {
		return richTextContent;
	}

	public void setRichTextContent(String richTextContent) {
		this.richTextContent = richTextContent;
	}
}
