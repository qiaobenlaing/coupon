package com.huift.hfq.base.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * 教育行业的老师
 * @author yingchen
 *
 */
public class Teacher implements Serializable{
	
	private String  teachCourse;
	private String  teacherCode;
	private String  teacherImgUrl;
	private String  teacherInfo;
	private String  teacherName;
	private String  teacherTitle;
	private List<Work> teacherWork;
	
	
	public Teacher() {
		super();
	}
	
	

	public Teacher(String teachCourse, String teacherCode, String teacherImgUrl, String teacherInfo, String teacherName, String teacherTitle, List<Work> teacherWork) {
		super();
		this.teachCourse = teachCourse;
		this.teacherCode = teacherCode;
		this.teacherImgUrl = teacherImgUrl;
		this.teacherInfo = teacherInfo;
		this.teacherName = teacherName;
		this.teacherTitle = teacherTitle;
		this.teacherWork = teacherWork;
	}



	public String getTeachCourse() {
		return teachCourse;
	}
	public void setTeachCourse(String teachCourse) {
		this.teachCourse = teachCourse;
	}
	public String getTeacherImgUrl() {
		return teacherImgUrl;
	}
	public void setTeacherImgUrl(String teacherImgUrl) {
		this.teacherImgUrl = teacherImgUrl;
	}
	public String getTeacherName() {
		return teacherName;
	}
	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}
	public String getTeacherTitle() {
		return teacherTitle;
	}
	public void setTeacherTitle(String teacherTitle) {
		this.teacherTitle = teacherTitle;
	}

	public String getTeacherCode() {
		return teacherCode;
	}

	public void setTeacherCode(String teacherCode) {
		this.teacherCode = teacherCode;
	}

	public String getTeacherInfo() {
		return teacherInfo;
	}

	public void setTeacherInfo(String teacherInfo) {
		this.teacherInfo = teacherInfo;
	}



	public List<Work> getTeacherWork() {
		return teacherWork;
	}



	public void setTeacherWork(List<Work> teacherWork) {
		this.teacherWork = teacherWork;
	}
	
	
}
