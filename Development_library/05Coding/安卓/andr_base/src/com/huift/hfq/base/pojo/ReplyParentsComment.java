package com.huift.hfq.base.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * 回复家长的评论
 * @author liyanfang
 */
public class ReplyParentsComment implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8384849848731275525L;

	/**
	 * 班级编码
	 */
	private String classCode;

	/**
	 * 用户头像
	 */
	private String avatarUrl;
	
	/**
	 * 评论图片
	 */
	private List<CourseCommentImg> classRemarkImg;

	/**
	 * 效果星级 ,取值范围：0-5，1为一星，2为二星，最大值为5
	 */
	private int effectLvl;

	/**
	 * 环境星级 , 取值范围：0-5，1为一星，2为二星，最大值为5
	 */
	private int envLvl;

	/**
	 * 商家是否回复 number 商家是否回复，1-已回复（不能再回复）；0-未回复（可回复）
	 */
	private int isRemarkByShop;

	/**
	 * 用户昵称 string
	 */
	private String nickName;

	/**
	 * 用户评论内容 string
	 */

	private String remark;

	/**
	 * 评论编码 string
	 */
	private String remarkCode;

	/**
	 * 用户评论时间 string 格式：YYYY-MM-DD
	 */
	private String remarkTime;
	
	/**
	 * 商家回复内容 string
	 */
	private String shopRemark;
	
	/**
	 * 商家回复时间 string 格式：YYYY-MM-DD
	 */
	private String shopRemarkTime;
	
	/**
	 * 师资星级 number 取值范围：0-5，1为一星，2为二星，最大值为5
	 */
	private int teacherLvl;

	/**
	 * 用户编码 string
	 */
	private String userCode;

	/**
	 * 总体星级 number 取值范围：0-5，1为一星，2为二星，最大值为5
	 */
	private int wholeLvl;

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public String getClassCode() {
		return classCode;
	}

	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}

	public List<CourseCommentImg> getClassRemarkImg() {
		return classRemarkImg;
	}

	public void setClassRemarkImg(List<CourseCommentImg> classRemarkImg) {
		this.classRemarkImg = classRemarkImg;
	}

	public int getEffectLvl() {
		return effectLvl;
	}

	public void setEffectLvl(int effectLvl) {
		this.effectLvl = effectLvl;
	}

	public int getEnvLvl() {
		return envLvl;
	}

	public void setEnvLvl(int envLvl) {
		this.envLvl = envLvl;
	}

	public int getIsRemarkByShop() {
		return isRemarkByShop;
	}

	public void setIsRemarkByShop(int isRemarkByShop) {
		this.isRemarkByShop = isRemarkByShop;
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

	public String getRemarkCode() {
		return remarkCode;
	}

	public void setRemarkCode(String remarkCode) {
		this.remarkCode = remarkCode;
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

	public int getTeacherLvl() {
		return teacherLvl;
	}

	public void setTeacherLvl(int teacherLvl) {
		this.teacherLvl = teacherLvl;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public int getWholeLvl() {
		return wholeLvl;
	}

	public void setWholeLvl(int wholeLvl) {
		this.wholeLvl = wholeLvl;
	}
	
	
}
