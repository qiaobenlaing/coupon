package com.huift.hfq.base.pojo;

import java.io.Serializable;

/**
 * 教师荣誉或作品
 * @author wensi.yu
 *
 */
public class TeacherWork implements Serializable{

	/**
	 * 作品编码
	 */
	private String workCode;
	
	/**
	 * 作品路径
	 */
	private String workUrl;

	public TeacherWork(String workCode, String workUrl) {
		super();
		this.workCode = workCode;
		this.workUrl = workUrl;
	}

	public String getWorkCode() {
		return workCode;
	}

	public void setWorkCode(String workCode) {
		this.workCode = workCode;
	}

	public String getWorkUrl() {
		return workUrl;
	}

	public void setWorkUrl(String workUrl) {
		this.workUrl = workUrl;
	}
	
	
}
