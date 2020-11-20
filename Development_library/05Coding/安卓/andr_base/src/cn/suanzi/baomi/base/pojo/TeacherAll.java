package cn.suanzi.baomi.base.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * 名师堂
 * @author wensi.yu
 *
 */
public class TeacherAll implements Serializable{

	/**
	 * 商家编码
	 */
	private String shopCode;
	
	/**
	 * 所教课程
	 */
	private String teachCourse;
	
	/**
	 * 教师编码
	 */
	private String teacherCode;
	
	/**
	 * 形象照URL
	 */
	private String teacherImgUrl;
	
	/**
	 * 教师简介
	 */
	private String teacherInfo;
	
	/**
	 * 教师名字
	 */
	private String teacherName;
	
	/**
	 * 教师职称
	 */
	private String teacherTitle;
	
	/**
	 * 教师荣誉或作品
	 */
	private List<TeacherWork> teacherWork;
	

	public TeacherAll() {
		super();
	}

	public TeacherAll(String shopCode, String teachCourse, String teacherCode, String teacherImgUrl, String teacherInfo, String teacherName, String teacherTitle, List<TeacherWork> teacherWork) {
		super();
		this.shopCode = shopCode;
		this.teachCourse = teachCourse;
		this.teacherCode = teacherCode;
		this.teacherImgUrl = teacherImgUrl;
		this.teacherInfo = teacherInfo;
		this.teacherName = teacherName;
		this.teacherTitle = teacherTitle;
		this.teacherWork = teacherWork;
	}

	public String getShopCode() {
		return shopCode;
	}

	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}

	public String getTeachCourse() {
		return teachCourse;
	}

	public void setTeachCourse(String teachCourse) {
		this.teachCourse = teachCourse;
	}

	public String getTeacherCode() {
		return teacherCode;
	}

	public void setTeacherCode(String teacherCode) {
		this.teacherCode = teacherCode;
	}

	public String getTeacherImgUrl() {
		return teacherImgUrl;
	}

	public void setTeacherImgUrl(String teacherImgUrl) {
		this.teacherImgUrl = teacherImgUrl;
	}

	public String getTeacherInfo() {
		return teacherInfo;
	}

	public void setTeacherInfo(String teacherInfo) {
		this.teacherInfo = teacherInfo;
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

	public List<TeacherWork> getTeacherWork() {
		return teacherWork;
	}

	public void setTeacherWork(List<TeacherWork> teacherWork) {
		this.teacherWork = teacherWork;
	}
	
	
	
}
