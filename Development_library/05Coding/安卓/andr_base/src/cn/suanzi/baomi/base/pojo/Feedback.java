/**
 *
 * @Author: Jianping Chen
 * @Date: 2015.5.8
 * @Version: 1.0.0
 * @Copyright Suanzi Co.,Ltd. @ 2015
 * 
 */

package cn.suanzi.baomi.base.pojo;

import java.util.Date;

public class Feedback implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	/** 
	 * 主键.
	 */
	private String feedbackCode;

	/** 
	 * 反馈者，支持匿名.
	 */
	private String creatorCode;

	/** 
	 * 创建时间.
	 */
	private Date createTime;

	/** 
	 * 反馈内容.
	 */
	private String content;

	/** 
	 * 状态.
	 */
	private Byte status;

	/** 
	 * 目标指向，可能是用户，也可能是商家，也可能是系统.
	 */
	private String targetCode;

	public Feedback() {
	}

	public Feedback(String feedbackCode) {
		this.feedbackCode = feedbackCode;
	}

	public Feedback(String feedbackCode, String creatorCode, Date createTime,
			String content, Byte status, String targetCode) {
		this.feedbackCode = feedbackCode;
		this.creatorCode = creatorCode;
		this.createTime = createTime;
		this.content = content;
		this.status = status;
		this.targetCode = targetCode;
	}

	/**
	 * 获取 主键. 
	 */
	public String getFeedbackCode() {
		return this.feedbackCode;
	}

	/**
	 * 设置 主键. 
	 */
	public void setFeedbackCode(String feedbackCode) {
		this.feedbackCode = feedbackCode;
	}

	/**
	 * 获取 反馈者，支持匿名. 
	 */
	public String getCreatorCode() {
		return this.creatorCode;
	}

	/**
	 * 设置 反馈者，支持匿名. 
	 */
	public void setCreatorCode(String creatorCode) {
		this.creatorCode = creatorCode;
	}

	/**
	 * 获取 创建时间. 
	 */
	public Date getCreateTime() {
		return this.createTime;
	}

	/**
	 * 设置 创建时间. 
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * 获取 反馈内容. 
	 */
	public String getContent() {
		return this.content;
	}

	/**
	 * 设置 反馈内容. 
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * 获取 状态. 
	 */
	public Byte getStatus() {
		return this.status;
	}

	/**
	 * 设置 状态. 
	 */
	public void setStatus(Byte status) {
		this.status = status;
	}

	/**
	 * 获取 目标指向，可能是用户，也可能是商家，也可能是系统. 
	 */
	public String getTargetCode() {
		return this.targetCode;
	}

	/**
	 * 设置 目标指向，可能是用户，也可能是商家，也可能是系统. 
	 */
	public void setTargetCode(String targetCode) {
		this.targetCode = targetCode;
	}

}
