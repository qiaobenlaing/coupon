package cn.suanzi.baomi.base.pojo;

/**
 * 推荐码
 * @author ad
 *
 */
public class Invite implements java.io.Serializable {

	/**
	 * 用户邀请码
	 */
	private String inviteCode;
	
	/**
	 * 推荐人数
	 */
	private String recomCount;
	
	/**
	 * 奖励红包金额
	 */
	private String bonusAmount;
	
	/**
	 * 奖励优惠券总额
	 */
	private String couponAmount;
	
	/**
	 * 奖励
	 */
	private String reward;
	
	/**
	 * 注册人数
	 */
	private String regNbr;
	
	/**
	 * 消费人数
	 */
	private String regACNbr;
	
	/**
	 * 规则
	 */
	private String rules;
	
	/**
	 * 图片的url
	 */
	private String imgUrl;
	
	/**
	 * 分享的标题
	 */
	private String shareTitle;
	
	/**
	 * 分享的内容
	 */
	private String shareContent;

	public Invite() {
		super();
	}
	

	public Invite(String inviteCode, String recomCount, String bonusAmount, String couponAmount, String reward, String regNbr, String regACNbr, String rules) {
		super();
		this.inviteCode = inviteCode;
		this.recomCount = recomCount;
		this.bonusAmount = bonusAmount;
		this.couponAmount = couponAmount;
		this.reward = reward;
		this.regNbr = regNbr;
		this.regACNbr = regACNbr;
		this.rules = rules;
	}
	
	public String getShareTitle() {
		return shareTitle;
	}


	public void setShareTitle(String shareTitle) {
		this.shareTitle = shareTitle;
	}


	public String getShareContent() {
		return shareContent;
	}


	public void setShareContent(String shareContent) {
		this.shareContent = shareContent;
	}


	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getInviteCode() {
		return inviteCode;
	}

	public void setInviteCode(String inviteCode) {
		this.inviteCode = inviteCode;
	}

	public String getRecomCount() {
		return recomCount;
	}

	public void setRecomCount(String recomCount) {
		this.recomCount = recomCount;
	}

	public String getBonusAmount() {
		return bonusAmount;
	}

	public void setBonusAmount(String bonusAmount) {
		this.bonusAmount = bonusAmount;
	}

	public String getCouponAmount() {
		return couponAmount;
	}

	public void setCouponAmount(String couponAmount) {
		this.couponAmount = couponAmount;
	}



	public String getReward() {
		return reward;
	}



	public void setReward(String reward) {
		this.reward = reward;
	}



	public String getRegNbr() {
		return regNbr;
	}



	public void setRegNbr(String regNbr) {
		this.regNbr = regNbr;
	}



	public String getRegACNbr() {
		return regACNbr;
	}



	public void setRegACNbr(String regACNbr) {
		this.regACNbr = regACNbr;
	}


	public String getRules() {
		return rules;
	}


	public void setRules(String rules) {
		this.rules = rules;
	}
	
	
	
}
