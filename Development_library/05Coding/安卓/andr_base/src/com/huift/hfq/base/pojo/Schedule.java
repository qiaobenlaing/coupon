package com.huift.hfq.base.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * 教育行业的课程表
 * @author yingchen
 *
 */
public class Schedule implements Serializable{
	/**课程编码*/
	private String classCode;
	
	/**班级名称*/
	private String  className;
	
	/**上课时间安排*/
	private List<SheduleWeek>  classWeekInfo;
	
	/**学习结束时间*/
	private String  learnEndDate;
	
	/**学习开始时间*/
	private String  learnStartDate;
	
	/**任课教师*/
	private String  teacherName;

	
	/**课程简介*/
	private String classInfo;
	
	/**上课时间*/
	private String classTime;
	
	/**课程形象URL*/
	private String classUrl;
	
	/**点赞数量*/
	private int clickNbr;
	
	/**咨询电话*/
	private String hotline;
	
	/**用户是否点赞*/
	private int isUserClick;
	
	/**合适年龄段*/
	private String learnMemo;
	
	/**所学课时*/
	private int learnNum;
	
	/**报名结束日期*/
	private String signEndDate;
	
	/**报名开始日期*/
	private String signStartDate;
	
	/**班级人数*/
	private int studentNum;
	
	/**报名费用*/
	private int learnFee;
	
	private boolean isChecked ;
	
	public Schedule() {
		super();
	}
	public Schedule(String classCode, String className, List<SheduleWeek> classWeekInfo, String learnEndDate, String learnStartDate, String teacherName, String classInfo, String classTime, String classUrl, int clickNbr, String hotline, int isUserClick, String learnMemo, int learnNum, String signEndDate, String signStartDate, int studentNum, int learnFee,boolean checked) {
		super();
		this.classCode = classCode;
		this.className = className;
		this.classWeekInfo = classWeekInfo;
		this.learnEndDate = learnEndDate;
		this.learnStartDate = learnStartDate;
		this.teacherName = teacherName;
		this.classInfo = classInfo;
		this.classTime = classTime;
		this.classUrl = classUrl;
		this.clickNbr = clickNbr;
		this.hotline = hotline;
		this.isUserClick = isUserClick;
		this.learnMemo = learnMemo;
		this.learnNum = learnNum;
		this.signEndDate = signEndDate;
		this.signStartDate = signStartDate;
		this.studentNum = studentNum;
		this.learnFee = learnFee;
		this.isChecked = checked;
	}


	public int getLearnFee() {
		return learnFee;
	}
	public void setLearnFee(int learnFee) {
		this.learnFee = learnFee;
	}
	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public List<SheduleWeek> getClassWeekInfo() {
		return classWeekInfo;
	}

	public void setClassWeekInfo(List<SheduleWeek> classWeekInfo) {
		this.classWeekInfo = classWeekInfo;
	}

	public String getLearnEndDate() {
		return learnEndDate;
	}

	public void setLearnEndDate(String learnEndDate) {
		this.learnEndDate = learnEndDate;
	}

	public String getLearnStartDate() {
		return learnStartDate;
	}

	public void setLearnStartDate(String learnStartDate) {
		this.learnStartDate = learnStartDate;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}
	public String getClassCode() {
		return classCode;
	}
	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}

	public String getClassInfo() {
		return classInfo;
	}

	public void setClassInfo(String classInfo) {
		this.classInfo = classInfo;
	}

	public String getClassTime() {
		return classTime;
	}

	public void setClassTime(String classTime) {
		this.classTime = classTime;
	}


	public String getClassUrl() {
		return classUrl;
	}

	public void setClassUrl(String classUrl) {
		this.classUrl = classUrl;
	}

	public int getClickNbr() {
		return clickNbr;
	}

	public void setClickNbr(int clickNbr) {
		this.clickNbr = clickNbr;
	}

	public String getHotline() {
		return hotline;
	}

	public void setHotline(String hotline) {
		this.hotline = hotline;
	}

	public int getIsUserClick() {
		return isUserClick;
	}

	public void setIsUserClick(int isUserClick) {
		this.isUserClick = isUserClick;
	}

	public String getLearnMemo() {
		return learnMemo;
	}

	public void setLearnMemo(String learnMemo) {
		this.learnMemo = learnMemo;
	}

	public int getLearnNum() {
		return learnNum;
	}

	public void setLearnNum(int learnNum) {
		this.learnNum = learnNum;
	}

	public String getSignEndDate() {
		return signEndDate;
	}

	public void setSignEndDate(String signEndDate) {
		this.signEndDate = signEndDate;
	}

	public String getSignStartDate() {
		return signStartDate;
	}

	public void setSignStartDate(String signStartDate) {
		this.signStartDate = signStartDate;
	}

	public int getStudentNum() {
		return studentNum;
	}


	public void setStudentNum(int studentNum) {
		this.studentNum = studentNum;
	}
	
	public boolean isChecked() {
		return isChecked;
	}
	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}
	
}
