package cn.suanzi.baomi.base.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * 一周的课表
 * @author liyanfang
 *
 */
public class ClassSchedule implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5887079519926581362L;
	
	/**
	 * 周几
	 */
	private String weekDay;
	/**
	 * 有数据
	 */
	private int havaData;
	
	/**
	 * 教室编码
	 */
	private String tableCode;
	
	/**
	 * 开始时间
	 */
	private String start;
	
	/**
	 * 结束时间
	 */
	private String end;
	
	/**
	 * 上课地址
	 */
	private String courseAddr;
	
	/**
	 * 上课内容
	 */
	private String courseName;
	
	/**
	 * 课程时间
	 */
	private List<ClassSchedule> courseList;
	
	/**
	 * 上课结束时间
	 */
	private String courseEndDate;
	
	/**
	 * 上课开始时间
	 */
	private String courseStartDate;
	
	/**
	 * 星期间隔 0-每周，1-每隔1周，以此类推
	 */
	private String weekStep;
	
	private ClassSchedule mainCourseInfo;
	
	public List<ClassSchedule> getCourseList() {
		return courseList;
	}

	public void setCourseList(List<ClassSchedule> courseList) {
		this.courseList = courseList;
	}

	public String getCourseEndDate() {
		return courseEndDate;
	}

	public void setCourseEndDate(String courseEndDate) {
		this.courseEndDate = courseEndDate;
	}

	public String getCourseStartDate() {
		return courseStartDate;
	}

	public void setCourseStartDate(String courseStartDate) {
		this.courseStartDate = courseStartDate;
	}

	public String getWeekStep() {
		return weekStep;
	}

	public void setWeekStep(String weekStep) {
		this.weekStep = weekStep;
	}

	public ClassSchedule getMainCourseInfo() {
		return mainCourseInfo;
	}

	public void setMainCourseInfo(ClassSchedule mainCourseInfo) {
		this.mainCourseInfo = mainCourseInfo;
	}

	public String getWeekDay() {
		return weekDay;
	}

	public void setWeekDay(String weekDay) {
		this.weekDay = weekDay;
	}

	public int getHavaData() {
		return havaData;
	}

	public void setHavaData(int havaData) {
		this.havaData = havaData;
	}

	public String getTableCode() {
		return tableCode;
	}

	public void setTableCode(String tableCode) {
		this.tableCode = tableCode;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public String getCourseAddr() {
		return courseAddr;
	}

	public void setCourseAddr(String courseAddr) {
		this.courseAddr = courseAddr;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
