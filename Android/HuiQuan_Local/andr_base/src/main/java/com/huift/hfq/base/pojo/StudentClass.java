package com.huift.hfq.base.pojo;

import java.io.Serializable;

/**
 * 学院报名信息
 * @author liyanfang
 */
public class StudentClass implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4530809467536319227L;

	/**
	 * 班级名称 string
	 */
	private String className;

	/**
	 * 处理标志 number 0-未处理；1-同意报名；2-不同意报名
	 */
	private int handFlag;

	/**
	 * 处理意见 string
	 */
	private String handMemo;

	/**
	 * 报名费用 string 如果为0，表示价格不透明，单位：元
	 */
	private String learnFee;

	/**
	 * 报名编码 string
	 */
	private String signCode;

	/**
	 * 实际报名费用 string 单位：元
	 */
	private String signFee;

	/**
	 * 报名时间 string
	 */
	private String signTime;

	/**
	 * 学员年龄 number 等于0，表示未知年龄
	 */
	private int studentAge;

	/**
	 * 学员就读年级 string
	 */
	private String studentGrade;

	/**
	 * 学员姓名 string
	 */
	private String studentName;

	/**
	 * 学员就读学校 string
	 */
	private String studentSchool;

	/**
	 * 学员性别 string
	 */
	private String studentSex;

	/**
	 * 学员联系方式 string
	 */
	private String studentTel;

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public int getHandFlag() {
		return handFlag;
	}

	public void setHandFlag(int handFlag) {
		this.handFlag = handFlag;
	}

	public String getHandMemo() {
		return handMemo;
	}

	public void setHandMemo(String handMemo) {
		this.handMemo = handMemo;
	}

	public String getLearnFee() {
		return learnFee;
	}

	public void setLearnFee(String learnFee) {
		this.learnFee = learnFee;
	}

	public String getSignCode() {
		return signCode;
	}

	public void setSignCode(String signCode) {
		this.signCode = signCode;
	}

	public String getSignFee() {
		return signFee;
	}

	public void setSignFee(String signFee) {
		this.signFee = signFee;
	}

	public String getSignTime() {
		return signTime;
	}

	public void setSignTime(String signTime) {
		this.signTime = signTime;
	}

	public int getStudentAge() {
		return studentAge;
	}

	public void setStudentAge(int studentAge) {
		this.studentAge = studentAge;
	}

	public String getStudentGrade() {
		return studentGrade;
	}

	public void setStudentGrade(String studentGrade) {
		this.studentGrade = studentGrade;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public String getStudentSchool() {
		return studentSchool;
	}

	public void setStudentSchool(String studentSchool) {
		this.studentSchool = studentSchool;
	}

	public String getStudentSex() {
		return studentSex;
	}

	public void setStudentSex(String studentSex) {
		this.studentSex = studentSex;
	}

	public String getStudentTel() {
		return studentTel;
	}

	public void setStudentTel(String studentTel) {
		this.studentTel = studentTel;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
