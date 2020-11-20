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

/**
 * 活动
 * @author liyanfang
 */
public class Activitys implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	/** 
	 * 主键.
	 */
	private String activityCode;

	/** 
	 * 活动logo，保存图片url.
	 */
	private String activityLogo;
	
	/**
	 * 商家标志
	 */
	private String shopLogo;

	/** 
	 * 活动归属，所谓归属，目前主要分为平台活动，商户活动或银行活动；平台活动为1，银行活动为2，商户活动为3.
	 */
	private Integer activityBelonging;

	/** 
	 * 发起活动的商家.
	 */
	private String shopCode;

	/** 
	 * 发起者，活动可不依赖于商家，考虑普通用户也可以发起活动.
	 */
	private String creatorCode;

	/** 
	 * 活动名称.
	 */
	private String activityName;
	
	/**
	 * 商家名称
	 */
	private String shopName;

	/** 
	 * 用文本描述的活动的内容.
	 */
	private String txtContent;

	/** 
	 * 用富文本（存储为HTML）描述的活动内容.
	 */
	private String richTextContent;

	/** 
	 * 是否需要报名.
	 */
	private Boolean isRegisterRequired;
	
	/**
	 * 最小金额
	 */
	private double minPrice;

	/** 
	 * 活动图片.
	 */
	private String activityImg;

	/** 
	 * 发起时间.
	 */
	private String createTime;

	/** 
	 * 活动参与人数上限.
	 */
	private int limitedParticipators;
	
	/**
	 * 已领取得人数
	 */
	private int participators;

	/** 
	 * 活动开始时间.
	 */
	private String startTime;

	/** 
	 * 活动持续时间，单位分钟.
	 */
	private Integer duration;

	/** 
	 * 活动结束时间.
	 */
	private String endTime;

	/** 
	 * 活动状态.
	 */
	private String status;

	/** 
	 * 活动类型.
	 */
	private Integer type;

	/** 
	 * 是否需要预付款.
	 */
	private Boolean isPrepayRequired;

	/** 
	 * 预付款金额.
	 */
	private Integer prePayment;

	/** 
	 * 总共需付款金额.
	 */
	private double totalPayment;

	/** 
	 * 活动地点.
	 */
	private String activityLocation;
	
	/**
	 * 活动距离
	 */
	private String distance;
	
	/**
	 * 活动编码
	 */
	private int rank;
	
	/**
	 * 活动数目
	 */
	private String actNumber;
	
	/**
	 * 分享的连接
	 */
	private String webUrl;
	
	/**
	 * 活动价格规格
	 */
	private List<PromotionPrice> feeScale;
	
	/**
	 * 购买活动的列表
	 */
	private List<UserActivity> userActCodeList;
	
	/**
	 * 活动信息
	 */
	private Activitys activityInfo;
	
	/**
	 * 分享
	 */
	private Share shareArr;
	
	/**
	 * 活动各种价格规格总的限购人数 
	 */
	private int registerNbrRequired;
	
	/**

	 * 活动主键
	 */
	private String userActivityCode;
	
	/**
	 * 联系人
	 */
	private String name;
	
	/**
	 * 验证码
	 */
	private String userActCode;
	
	/**
	 * 活动验证码
	 */
	private String actCode;
	
	/**
	 * 活动验证码id
	 */
	private String userActCodeId;
	

	
	public Activitys() {
	}

	public Activitys(String activityCode, String activityLogo, String shopLogo, Integer activityBelonging,
			String shopCode, String creatorCode, String activityName, String shopName, String txtContent,
			String richTextContent, Boolean isRegisterRequired, double minPrice, String activityImg, String createTime,
			int limitedParticipators, int participators, String startTime, Integer duration, String endTime,
			String status, Integer type, Boolean isPrepayRequired, Integer prePayment, Integer totalPayment,
			String activityLocation, String distance, int rank, String actNumber, String webUrl,
			List<PromotionPrice> feeScale,int registerNbrRequired, String name, String userActCode,String userActivityCode,
			String actCode, String userActCodeId) {

		super();
		this.activityCode = activityCode;
		this.activityLogo = activityLogo;
		this.shopLogo = shopLogo;
		this.activityBelonging = activityBelonging;
		this.shopCode = shopCode;
		this.creatorCode = creatorCode;
		this.activityName = activityName;
		this.shopName = shopName;
		this.txtContent = txtContent;
		this.richTextContent = richTextContent;
		this.isRegisterRequired = isRegisterRequired;
		this.minPrice = minPrice;
		this.activityImg = activityImg;
		this.createTime = createTime;
		this.limitedParticipators = limitedParticipators;
		this.participators = participators;
		this.startTime = startTime;
		this.duration = duration;
		this.endTime = endTime;
		this.status = status;
		this.type = type;
		this.isPrepayRequired = isPrepayRequired;
		this.prePayment = prePayment;
		this.totalPayment = totalPayment;
		this.activityLocation = activityLocation;
		this.distance = distance;
		this.rank = rank;
		this.actNumber = actNumber;
		this.webUrl = webUrl;
		this.feeScale = feeScale;
		this.registerNbrRequired = registerNbrRequired;
		this.userActivityCode = userActivityCode;
		this.name = name;
		this.userActCode = userActCode;
		this.actCode = actCode;
		this.userActCodeId = userActCodeId;
	}
	
	public Share getShareArr() {
		return shareArr;
	}

	public void setShareArr(Share shareArr) {
		this.shareArr = shareArr;
	}

	public Activitys getActivityInfo() {
		return activityInfo;
	}

	public void setActivityInfo(Activitys activityInfo) {
		this.activityInfo = activityInfo;
	}

	public List<UserActivity> getUserActCodeList() {
		return userActCodeList;
	}

	public void setUserActCodeList(List<UserActivity> userActCodeList) {
		this.userActCodeList = userActCodeList;
	}

	public Activitys(String activityCode) {
		this.activityCode = activityCode;
	}

	public double getMinPrice() {
		return minPrice;
	}

	public void setMinPrice(double minPrice) {
		this.minPrice = minPrice;
	}

	public int getParticipators() {
		return participators;
	}

	public void setParticipators(int participators) {
		this.participators = participators;
	}

	public List<PromotionPrice> getFeeScale() {
		return feeScale;
	}

	public void setFeeScale(List<PromotionPrice> feeScale) {
		this.feeScale = feeScale;
	}

	public void setLimitedParticipators(int limitedParticipators) {
		this.limitedParticipators = limitedParticipators;
	}

	public String getWebUrl() {
		return webUrl;
	}

	public void setWebUrl(String webUrl) {
		this.webUrl = webUrl;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public String getActNumber() {
		return actNumber;
	}

	public void setActNumber(String actNumber) {
		this.actNumber = actNumber;
	}

	/**
	 * 获取主键. 
	 */
	public String getActivityCode() {
		return this.activityCode;
	}

	/**
	 * 设置主键. 
	 */
	public void setActivityCode(String activityCode) {
		this.activityCode = activityCode;
	}

	/**
	 * 获取活动logo，保存图片url. 
	 */
	public String getActivityLogo() {
		return this.activityLogo;
	}

	/**
	 * 设置活动logo，保存图片url. 
	 */
	public void setActivityLogo(String activityLogo) {
		this.activityLogo = activityLogo;
	}

	/**
	 * 获取活动归属，所谓归属，目前主要分为平台活动，商户活动或银行活动；平台活动为1，银行活动为2，商户活动为3. 
	 */
	public Integer getActivityBelonging() {
		return this.activityBelonging;
	}

	/**
	 * 设置活动归属，所谓归属，目前主要分为平台活动，商户活动或银行活动；平台活动为1，银行活动为2，商户活动为3. 
	 */
	public void setActivityBelonging(Integer activityBelonging) {
		this.activityBelonging = activityBelonging;
	}

	/**
	 * 获取发起活动的商家. 
	 */
	public String getShopCode() {
		return this.shopCode;
	}

	/**
	 * 设置发起活动的商家. 
	 */
	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}

	/**
	 * 获取发起者，活动可不依赖于商家，考虑普通用户也可以发起活动. 
	 */
	public String getCreatorCode() {
		return this.creatorCode;
	}

	/**
	 * 设置发起者，活动可不依赖于商家，考虑普通用户也可以发起活动. 
	 */
	public void setCreatorCode(String creatorCode) {
		this.creatorCode = creatorCode;
	}

	/**
	 * 获取活动名称. 
	 */
	public String getActivityName() {
		return this.activityName;
	}

	/**
	 * 设置活动名称. 
	 */
	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	/**
	 * 获取用文本描述的活动的内容. 
	 */
	public String getTxtContent() {
		return this.txtContent;
	}

	/**
	 * 设置用文本描述的活动的内容. 
	 */
	public void setTxtContent(String txtContent) {
		this.txtContent = txtContent;
	}

	/**
	 * 获取用富文本（存储为HTML）描述的活动内容. 
	 */
	public String getRichTextContent() {
		return this.richTextContent;
	}

	/**
	 * 设置用富文本（存储为HTML）描述的活动内容. 
	 */
	public void setRichTextContent(String richTextContent) {
		this.richTextContent = richTextContent;
	}

	/**
	 * 获取是否需要报名. 
	 */
	public Boolean getIsRegisterRequired() {
		return this.isRegisterRequired;
	}

	/**
	 * 设置是否需要报名. 
	 */
	public void setIsRegisterRequired(Boolean isRegisterRequired) {
		this.isRegisterRequired = isRegisterRequired;
	}

	/**
	 * 获取活动图片. 
	 */
	public String getActivityImg() {
		return this.activityImg;
	}

	/**
	 * 设置活动图片. 
	 */
	public void setActivityImg(String activityImg) {
		this.activityImg = activityImg;
	}

	/**
	 * 获取发起时间. 
	 */
	public String getCreateTime() {
		return this.createTime;
	}

	/**
	 * 设置发起时间. 
	 */
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	/**
	 * 获取活动参与人数上限. 
	 */
	public Integer getLimitedParticipators() {
		return this.limitedParticipators;
	}

	/**
	 * 设置活动参与人数上限. 
	 */
	public void setLimitedParticipators(Integer limitedParticipators) {
		this.limitedParticipators = limitedParticipators;
	}

	/**
	 * 获取活动开始时间. 
	 */
	public String getStartTime() {
		return this.startTime;
	}

	/**
	 * 设置活动开始时间. 
	 */
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	/**
	 * 获取活动持续时间，单位分钟. 
	 */
	public Integer getDuration() {
		return this.duration;
	}

	/**
	 * 设置活动持续时间，单位分钟. 
	 */
	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	/**
	 * 获取活动结束时间. 
	 */
	public String getEndTime() {
		return this.endTime;
	}

	/**
	 * 设置活动结束时间. 
	 */
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	/**
	 * 获取活动状态. 
	 */
	public String getStatus() {
		return this.status;
	}

	/**
	 * 设置活动状态. 
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * 获取活动类型. 
	 */
	public Integer getType() {
		return this.type;
	}

	/**
	 * 设置活动类型. 
	 */
	public void setType(Integer type) {
		this.type = type;
	}

	/**
	 * 获取是否需要预付款. 
	 */
	public Boolean getIsPrepayRequired() {
		return this.isPrepayRequired;
	}

	/**
	 * 设置是否需要预付款. 
	 */
	public void setIsPrepayRequired(Boolean isPrepayRequired) {
		this.isPrepayRequired = isPrepayRequired;
	}

	/**
	 * 获取预付款金额. 
	 */
	public Integer getPrePayment() {
		return this.prePayment;
	}

	/**
	 * 设置预付款金额. 
	 */
	public void setPrePayment(Integer prePayment) {
		this.prePayment = prePayment;
	}

	/**
	 * 获取总共需付款金额. 
	 */
	public double getTotalPayment() {
		return this.totalPayment;
	}

	/**
	 * 设置总共需付款金额. 
	 */
	public void setTotalPayment(double totalPayment) {
		this.totalPayment = totalPayment;
	}

	/**
	 * 获取活动地点. 
	 */
	public String getActivityLocation() {
		return this.activityLocation;
	}

	/**
	 * 设置活动地点. 
	 */
	public void setActivityLocation(String activityLocation) {
		this.activityLocation = activityLocation;
	}
	
	/**
	 * 商家名称
	 */
	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	
	/**
	 * 商家标志
	 * @return
	 */
	public String getShopLogo() {
		return shopLogo;
	}

	public void setShopLogo(String shopLogo) {
		this.shopLogo = shopLogo;
	}
	
	/**
	 * 活动距离
	 * @return
	 */
	public String getDistance() {
		return distance;
	}
	public void setDistance(String distance) {
		this.distance = distance;
	}

	public int getRegisterNbrRequired() {
		return registerNbrRequired;
	}

	public void setRegisterNbrRequired(int registerNbrRequired) {
		this.registerNbrRequired = registerNbrRequired;
	}


	public String getUserActivityCode() {
		return userActivityCode;
	}

	public void setUserActivityCode(String userActivityCode) {
		this.userActivityCode = userActivityCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUserActCode() {
		return userActCode;
	}

	public void setUserActCode(String userActCode) {
		this.userActCode = userActCode;
	}

	public String getActCode() {
		return actCode;
	}

	public void setActCode(String actCode) {
		this.actCode = actCode;
	}

	public String getUserActCodeId() {
		return userActCodeId;
	}

	public void setUserActCodeId(String userActCodeId) {
		this.userActCodeId = userActCodeId;
	}
}
