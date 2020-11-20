package com.huift.hfq.base.pojo;

import java.io.Serializable;

/**
 * 开始时间和结束时间
 * @author wensi.yu
 *
 */
public class Classlearn implements Serializable{

	/**
	 * 上课结束时间
	 */
	private String startTime;
	
	/**
	 * 上课开始时间
	 */
	private String endTime;

	public Classlearn(String startTime, String endTime) {
		super();
		this.startTime = startTime;
		this.endTime = endTime;
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

	
	
}
