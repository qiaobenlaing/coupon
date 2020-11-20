package com.huift.hfq.base.pojo;

import java.io.Serializable;

/**
 * 上课时间
 * @author ad
 *
 */
public class Time implements Serializable{
	
	/**
	 * 时间段
	 */
	private String duration;
	
	/**
	 * 上课开始时间
	 */
	private String startTime;
	
	/**
	 * 上课开始时间
	 */
	private String endTime;
	
	/**
	 * 周几
	 */
	private String weekName;

	public Time(String duration, String startTime, String endTime, String weekName) {
		super();
		this.duration = duration;
		this.startTime = startTime;
		this.endTime = endTime;
		this.weekName = weekName;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
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

	public String getWeekName() {
		return weekName;
	}

	public void setWeekName(String weekName) {
		this.weekName = weekName;
	}

	
}
