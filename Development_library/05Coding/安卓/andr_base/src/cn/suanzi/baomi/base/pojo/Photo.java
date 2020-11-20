package cn.suanzi.baomi.base.pojo;

import java.io.Serializable;

/**
 * 会员卡列表实体类
 * @author qian.zhou
 */
public class Photo implements Serializable {

	private String url;//上新图片
	private String createTime;//上新图片编码
	private String activityCode;//活动编码
	private String activityName;//活动名称
	private String isNew;
	
	public Photo() {
		super();
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
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

	public String getIsNew() {
		return isNew;
	}

	public void setIsNew(String isNew) {
		this.isNew = isNew;
	}
}
