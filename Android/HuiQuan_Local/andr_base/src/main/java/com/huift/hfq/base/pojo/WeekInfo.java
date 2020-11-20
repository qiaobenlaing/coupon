package com.huift.hfq.base.pojo;

import java.io.Serializable;

/**
 * 上课时间
 * @author wensi.yu
 *
 */
public class WeekInfo  implements Serializable{

	/**
	 * 上课开始时间
	 */
	private String startTime;
	
	/**
	 * 上课结束时间
	 */
	private String endTine;
	
	/**
	 * 周几
	 */
	private String weekName;

	public WeekInfo(String startTime, String endTine, String weekName) {
		super();
		this.startTime = startTime;
		this.endTine = endTine;
		this.weekName = weekName;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTine() {
		return endTine;
	}

	public void setEndTine(String endTine) {
		this.endTine = endTine;
	}

	public String getWeekName() {
		return weekName;
	}

	public void setWeekName(String weekName) {
		this.weekName = weekName;
	}
	
	
}
