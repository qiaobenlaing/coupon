package cn.suanzi.baomi.base.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * 添加班级
 * @author wensi.yu
 *
 */
public class AddClass implements Serializable{

	/**
	 * 班级名称
	 */
	private String className;
	
	/**
	 * 课程简介
	 */
	private String classInfo;
	
	/**
	 * 课程形象URL
	 */
	private String classUrl;
	
	/**
	 * 上课时间
	 */
	private List<Time> classWeekInfo;
	
	/**
	 * 学习结束日期
	 */
	private String learnEndDate;
	
	/**
	 * 报名费用
	 */
	private String learnFee;
	
	/**
	 * 适合描述
	 */
	private String learnMemo;
	
	/**
	 * 所学课时
	 */
	private String learnNum;
	
	/**
	 * 学习开始日期
	 */
	private String learnStartDate;
	
	/**
	 * 报名结束日期
	 */
	private String signEndDate;
	
	/**
	 * 报名开始日期
	 */
	private String signStartDate;
	
	/**
	 * 老师编码
	 */
	private String teacherCode;
	
	

	public AddClass() {
		super();
	}

	

	public AddClass(String className, String classInfo, String classUrl, List<Time> classWeekInfo, String learnEndDate, String learnFee, String learnMemo, String learnNum, String learnStartDate, String signEndDate, String signStartDate, String teacherCode) {
		super();
		this.className = className;
		this.classInfo = classInfo;
		this.classUrl = classUrl;
		this.classWeekInfo = classWeekInfo;
		this.learnEndDate = learnEndDate;
		this.learnFee = learnFee;
		this.learnMemo = learnMemo;
		this.learnNum = learnNum;
		this.learnStartDate = learnStartDate;
		this.signEndDate = signEndDate;
		this.signStartDate = signStartDate;
		this.teacherCode = teacherCode;
	}



	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getClassInfo() {
		return classInfo;
	}

	public void setClassInfo(String classInfo) {
		this.classInfo = classInfo;
	}

	public String getClassUrl() {
		return classUrl;
	}

	public void setClassUrl(String classUrl) {
		this.classUrl = classUrl;
	}

	

	public List<Time> getClassWeekInfo() {
		return classWeekInfo;
	}



	public void setClassWeekInfo(List<Time> classWeekInfo) {
		this.classWeekInfo = classWeekInfo;
	}



	public String getLearnEndDate() {
		return learnEndDate;
	}

	public void setLearnEndDate(String learnEndDate) {
		this.learnEndDate = learnEndDate;
	}

	public String getLearnFee() {
		return learnFee;
	}

	public void setLearnFee(String learnFee) {
		this.learnFee = learnFee;
	}

	public String getLearnMemo() {
		return learnMemo;
	}

	public void setLearnMemo(String learnMemo) {
		this.learnMemo = learnMemo;
	}

	public String getLearnNum() {
		return learnNum;
	}

	public void setLearnNum(String learnNum) {
		this.learnNum = learnNum;
	}

	public String getLearnStartDate() {
		return learnStartDate;
	}

	public void setLearnStartDate(String learnStartDate) {
		this.learnStartDate = learnStartDate;
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

	public String getTeacherCode() {
		return teacherCode;
	}

	public void setTeacherCode(String teacherCode) {
		this.teacherCode = teacherCode;
	}
	
	
}
