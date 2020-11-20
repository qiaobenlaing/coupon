package cn.suanzi.baomi.base.pojo;

import java.io.Serializable;
import java.util.List;

import android.R.integer;

/**
 * 开班
 * @author wensi.yu
 *
 */
public class ClassAll implements Serializable{

	/**
	 * 班级编码
	 */
	private String classCode;
	
	/**
	 * 课程简介
	 */
	private String classInfo;
	
	/**
	 * 班级名称
	 */
	private String className;
	
	/**
	 * 课程形象URL
	 */
	private String classUrl;
	
	/**
	 * 上课时间
	 */
	private List<ClassWeek> classWeekInfo;
	
	/**
	 * 学习开始日期
	 */
	private String learnStartDate;
	
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
	 * 商家编码
	 */
	private String shopCode;
	
	/**
	 * 报名开始日期
	 */
	private String signStartDate;
	
	/**
	 * 报名结束日期
	 */
	private String signEndDate;
	
	/**
	 * 班级人数
	 */
	private String studentNum;
	
	/**
	 * 老师编码
	 */
	private String teacherCode;
	
	/**
	 * 老师名字
	 */
	private String teacherName;
	
	/**
	 * 教师职称
	 */
	private String teacherTitle;
	
	/**
	 * 缴费标志  0 未交费  1缴费
	 */
	private int feeFlag;
	
	/**
	 * 处理标准 0-未处理；1-同意报名；2-不同意报名
	 */
	private int handFlag;
	
	/**
	 * 处理意见
	 */
	private String handMemo;
	
	/**
	 * 订单编码
	 */
	private String orderCode;
	
	/**
	 * 支付时间
	 */
	private String payTime;
	
	/**
	 * 商家名称
	 */
	private String shopName;
	
	/**
	 * 报名信息编码
	 */
	private String shopSignCode;
	
	/**
	 * 报名编码
	 */
	private String signCode;
	
	/**
	 * 报名费用
	 */
	private String signFee;
	
	/**
	 * 报名时间
	 */
	private String signTime;
	
	/**
	 * 电话号码
	 */
	private String tel;
	
	/**
	 * 订单编码
	 */
	private String orderNbr;
	
	/**
	 * 商家头像
	 */
	private String logoUrl;
	
	public ClassAll(String classCode, String classInfo, String className, String classUrl, List<ClassWeek> classWeekInfo, String learnStartDate, String learnEndDate, String learnFee, String learnMemo, String learnNum, String shopCode, String signStartDate, String signEndDate, String studentNum, String teacherCode, String teacherName, String teacherTitle) {
		super();
		this.classCode = classCode;
		this.classInfo = classInfo;
		this.className = className;
		this.classUrl = classUrl;
		this.classWeekInfo = classWeekInfo;
		this.learnStartDate = learnStartDate;
		this.learnEndDate = learnEndDate;
		this.learnFee = learnFee;
		this.learnMemo = learnMemo;
		this.learnNum = learnNum;
		this.shopCode = shopCode;
		this.signStartDate = signStartDate;
		this.signEndDate = signEndDate;
		this.studentNum = studentNum;
		this.teacherCode = teacherCode;
		this.teacherName = teacherName;
		this.teacherTitle = teacherTitle;
	}

	public String getOrderNbr() {
		return orderNbr;
	}

	public void setOrderNbr(String orderNbr) {
		this.orderNbr = orderNbr;
	}

	public String getLogoUrl() {
		return logoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	public int getFeeFlag() {
		return feeFlag;
	}

	public void setFeeFlag(int feeFlag) {
		this.feeFlag = feeFlag;
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

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getPayTime() {
		return payTime;
	}

	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getShopSignCode() {
		return shopSignCode;
	}

	public void setShopSignCode(String shopSignCode) {
		this.shopSignCode = shopSignCode;
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

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
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

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getClassUrl() {
		return classUrl;
	}

	public void setClassUrl(String classUrl) {
		this.classUrl = classUrl;
	}

	public List<ClassWeek> getClassWeekInfo() {
		return classWeekInfo;
	}

	public void setClassWeekInfo(List<ClassWeek> classWeekInfo) {
		this.classWeekInfo = classWeekInfo;
	}

	public String getLearnStartDate() {
		return learnStartDate;
	}

	public void setLearnStartDate(String learnStartDate) {
		this.learnStartDate = learnStartDate;
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

	public String getShopCode() {
		return shopCode;
	}

	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}

	public String getSignStartDate() {
		return signStartDate;
	}

	public void setSignStartDate(String signStartDate) {
		this.signStartDate = signStartDate;
	}

	public String getSignEndDate() {
		return signEndDate;
	}

	public void setSignEndDate(String signEndDate) {
		this.signEndDate = signEndDate;
	}

	public String getStudentNum() {
		return studentNum;
	}

	public void setStudentNum(String studentNum) {
		this.studentNum = studentNum;
	}

	public String getTeacherCode() {
		return teacherCode;
	}

	public void setTeacherCode(String teacherCode) {
		this.teacherCode = teacherCode;
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
	
	
	
	
}
