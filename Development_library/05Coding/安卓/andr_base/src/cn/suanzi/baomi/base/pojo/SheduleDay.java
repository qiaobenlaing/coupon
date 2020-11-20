package cn.suanzi.baomi.base.pojo;

import java.io.Serializable;

/**
 * 课程表每天的上课时间
 * @author yingchen
 *
 */
public class SheduleDay implements Serializable{
	private String startTime;
	private String endTime;
	public SheduleDay() {
		super();
	}
	public SheduleDay(String startTime, String endTime) {
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
