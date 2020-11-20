package cn.suanzi.baomi.base.pojo;
/***
 * @author wensi.yu
 * 
 * 活动列表
 */
public class ActListContentItem implements java.io.Serializable {

	/**
	 * 商家编码
	 */
	private String activityCode; 
	
	/**
	 * 商家名字
	 */
	private String shopName; 
	
	/**
	 * 活动主题
	 */
	private String activityName;
	
	/**
	 * 活动简介
	 */
	private String txtContent;
	
	/**
	 * 活动开始时间
	 */
	private String startTime;
	
	/**
	 * 活动结束时间
	 */
	private String endTime;
	
	/**
	 * 活动方形图片
	 */
	private String activityLogo; 
	
	/**
	 * 活动创建时间
	 */
	private String createTime; 
	
	/**
	 * 活动状态
	 */
	private String status; 
	
	/**
	 * 活动参与人数
	 */
	private String participators; 
	
	
	public ActListContentItem() {
		super();
	}
	
	
	public ActListContentItem(String activityCode, String shopName,
			String activityName, String txtContent, String startTime,
			String endTime, String activityLogo, String createTime,
			String status, String participators) {
		super();
		this.activityCode = activityCode;
		this.shopName = shopName;
		this.activityName = activityName;
		this.txtContent = txtContent;
		this.startTime = startTime;
		this.endTime = endTime;
		this.activityLogo = activityLogo;
		this.createTime = createTime;
		this.status = status;
		this.participators = participators;
	}


	public String getActivityCode() {
		return activityCode;
	}

	public void setActivityCode(String activityCode) {
		this.activityCode = activityCode;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
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

	public String getActivityLogo() {
		return activityLogo;
	}

	public void setActivityLogo(String activityLogo) {
		this.activityLogo = activityLogo;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getParticipators() {
		return participators;
	}

	public void setParticipators(String participators) {
		this.participators = participators;
	}

}
