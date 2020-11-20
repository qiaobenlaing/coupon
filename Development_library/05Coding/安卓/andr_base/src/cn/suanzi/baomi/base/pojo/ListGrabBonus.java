package cn.suanzi.baomi.base.pojo;

/**
 * 红包领取人数
 * @author ad
 *
 */
public class ListGrabBonus implements java.io.Serializable {

	
	/**
	 * 用户昵称
	 */
	private String nickName;
	
	/**
	 * 用户头像
	 */
	private String avatarUrl;
	
	/**
	 * 领用时间
	 */
	private String getDate;
	
	/**
	 * 用户领取金额
	 */
	private String value;

	public ListGrabBonus() {
		super();
	}

	
	
	public ListGrabBonus(String nickName, String avatarUrl, String getDate, String value) {
		super();
		this.nickName = nickName;
		this.avatarUrl = avatarUrl;
		this.getDate = getDate;
		this.value = value;
	}


	

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}


	public String getGetDate() {
		return getDate;
	}


	public void setGetDate(String getDate) {
		this.getDate = getDate;
	}
	
	
}
