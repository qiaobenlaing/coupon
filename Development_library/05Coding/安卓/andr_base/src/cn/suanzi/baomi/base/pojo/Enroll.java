package cn.suanzi.baomi.base.pojo;

public class Enroll implements java.io.Serializable{

	/**
	 * 顾客编码
	 */
	private String userCode;
	
	/**
	 * 顾客头像
	 */
	private String avatarUrl;
	
	/**
	 * 顾客昵称
	 */
	private String ninkName;
	
	/**
	 * 报名时间
	 */
	private String signUpTime;
	
	/**
	 * 男性大人人数
	 */
	private String adultM;
	
	/**
	 * 女性大人人数
	 */
	private String adultF;
	
	/**
	 * 男性小孩人数
	 */
	private String kidM;
	
	/**
	 * 女性小孩人数
	 */
	private String kidF;
	
	/**
	 * 总人数
	 */
	private String participantNbr;

	public Enroll() {
		super();
	}


	public Enroll(String userCode, String avatarUrl, String ninkName, String signUpTime, String adultM, String adultF,
			String kidM, String kidF, String participantNbr) {
		super();
		this.userCode = userCode;
		this.avatarUrl = avatarUrl;
		this.ninkName = ninkName;
		this.signUpTime = signUpTime;
		this.adultM = adultM;
		this.adultF = adultF;
		this.kidM = kidM;
		this.kidF = kidF;
		this.participantNbr = participantNbr;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public String getNinkName() {
		return ninkName;
	}

	public void setNinkName(String ninkName) {
		this.ninkName = ninkName;
	}

	public String getAdultM() {
		return adultM;
	}

	public void setAdultM(String adultM) {
		this.adultM = adultM;
	}

	public String getAdultF() {
		return adultF;
	}

	public void setAdultF(String adultF) {
		this.adultF = adultF;
	}

	public String getKidM() {
		return kidM;
	}

	public void setKidM(String kidM) {
		this.kidM = kidM;
	}

	public String getKidF() {
		return kidF;
	}

	public void setKidF(String kidF) {
		this.kidF = kidF;
	}

	public String getParticipantNbr() {
		return participantNbr;
	}

	public void setParticipantNbr(String participantNbr) {
		this.participantNbr = participantNbr;
	}


	public String getSignUpTime() {
		return signUpTime;
	}


	public void setSignUpTime(String signUpTime) {
		this.signUpTime = signUpTime;
	}
	
	
	
}
