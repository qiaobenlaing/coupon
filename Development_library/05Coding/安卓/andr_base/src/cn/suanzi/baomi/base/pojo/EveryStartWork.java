package cn.suanzi.baomi.base.pojo;

import java.io.Serializable;

public class EveryStartWork implements Serializable{
	private String   starImgUrl;

	public EveryStartWork() {
		super();
	}

	public EveryStartWork(String starImgUrl) {
		super();
		this.starImgUrl = starImgUrl;
	}

	public String getStarImgUrl() {
		return starImgUrl;
	}

	public void setStarImgUrl(String starImgUrl) {
		this.starImgUrl = starImgUrl;
	}
	
	
}
