package com.huift.hfq.base.pojo;

import java.io.Serializable;

/**
 * 教育商家的招生启示
 * @author yingchen
 *
 */
public class RecruitInfo implements Serializable{
	private String advUrl;
	private String recruitUrl;
	private String recruitCode;
	
	public RecruitInfo() {
		super();
	}
	
	public RecruitInfo(String advUrl, String recruitUrl, String recruitCode) {
		super();
		this.advUrl = advUrl;
		this.recruitUrl = recruitUrl;
		this.recruitCode = recruitCode;
	}

	public String getRecruitUrl() {
		return recruitUrl;
	}
	public void setRecruitUrl(String recruitUrl) {
		this.recruitUrl = recruitUrl;
	}
	public String getRecruitCode() {
		return recruitCode;
	}
	public void setRecruitCode(String recruitCode) {
		this.recruitCode = recruitCode;
	}

	public String getAdvUrl() {
		return advUrl;
	}

	public void setAdvUrl(String advUrl) {
		this.advUrl = advUrl;
	}
	
	
}
