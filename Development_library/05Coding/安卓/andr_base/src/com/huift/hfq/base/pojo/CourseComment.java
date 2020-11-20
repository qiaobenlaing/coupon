package com.huift.hfq.base.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * 课程评价
 * @author yingchen
 *
 */
public class CourseComment implements Serializable{
	private List<CourseCommentImg>  classRemarkImg;
	
	private String  nickName;
	
	private String  remark;
	
	private String  remarkTime;
	
	private String  shopRemark;
	
	private String  shopRemarkTime;
	
	private int  wholeLvl;

	public CourseComment() {
		super();
	}

	public CourseComment(List<CourseCommentImg> classRemarkImg, String nickName, String remark, String remarkTime, String shopRemark, String shopRemarkTime, int wholeLvl) {
		super();
		this.classRemarkImg = classRemarkImg;
		this.nickName = nickName;
		this.remark = remark;
		this.remarkTime = remarkTime;
		this.shopRemark = shopRemark;
		this.shopRemarkTime = shopRemarkTime;
		this.wholeLvl = wholeLvl;
	}

	public List<CourseCommentImg> getClassRemarkImg() {
		return classRemarkImg;
	}

	public void setClassRemarkImg(List<CourseCommentImg> classRemarkImg) {
		this.classRemarkImg = classRemarkImg;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getRemarkTime() {
		return remarkTime;
	}

	public void setRemarkTime(String remarkTime) {
		this.remarkTime = remarkTime;
	}

	public String getShopRemark() {
		return shopRemark;
	}

	public void setShopRemark(String shopRemark) {
		this.shopRemark = shopRemark;
	}

	public String getShopRemarkTime() {
		return shopRemarkTime;
	}

	public void setShopRemarkTime(String shopRemarkTime) {
		this.shopRemarkTime = shopRemarkTime;
	}

	public int getWholeLvl() {
		return wholeLvl;
	}

	public void setWholeLvl(int wholeLvl) {
		this.wholeLvl = wholeLvl;
	}
	
	
}
