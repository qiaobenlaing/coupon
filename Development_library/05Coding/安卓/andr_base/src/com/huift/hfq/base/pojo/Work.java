package com.huift.hfq.base.pojo;

import java.io.Serializable;

public class Work implements Serializable{
	private String  workUrl;

	public Work() {
		super();
	}

	public Work(String workUrl) {
		super();
		this.workUrl = workUrl;
	}

	public String getWorkUrl() {
		return workUrl;
	}

	public void setWorkUrl(String workUrl) {
		this.workUrl = workUrl;
	}
	
	
}
