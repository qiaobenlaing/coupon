package com.huift.hfq.base.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * 学习时间
 * @author wensi.yu
 *
 */
public class ClassWeek implements Serializable {

	/**
	 * 学习时间
	 */
	private List<Classlearn> learnTime;
	
	/**
	 * 周几
	 */
	private String weekName;

	public ClassWeek(List<Classlearn> learnTime, String weekName) {
		super();
		this.learnTime = learnTime;
		this.weekName = weekName;
	}

	public List<Classlearn> getLearnTime() {
		return learnTime;
	}

	public void setLearnTime(List<Classlearn> learnTime) {
		this.learnTime = learnTime;
	}

	public String getWeekName() {
		return weekName;
	}

	public void setWeekName(String weekName) {
		this.weekName = weekName;
	}
	
}
