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

public class UserCoupon implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	/** 
	 * 主键.
	 */
	private String userCouponCode;

	/** 
	 * 用户编码.
	 */
	private String userCode;

	/** 
	 * 优惠券编码.
	 */
	private String batchCouponCode;

	/** 
	 * 申请时间.
	 */
	private Date applyTime;

	/** 
	 * 优惠券状态.
	 */
	private Byte status;

	/** 
	 * 优惠券的分享程度：所有人可见，朋友可见，其它人不可见.
	 */
	private Integer sharedLvl;

	/** 
	 * 在哪笔消费中使用了优惠券.
	 */
	private String consumeCode;

	public UserCoupon() {
	}

	public UserCoupon(String userCouponCode) {
		this.userCouponCode = userCouponCode;
	}

	public UserCoupon(String userCouponCode, String userCode,
			String batchCouponCode, Date applyTime, Byte status,
			Integer sharedLvl, String consumeCode) {
		this.userCouponCode = userCouponCode;
		this.userCode = userCode;
		this.batchCouponCode = batchCouponCode;
		this.applyTime = applyTime;
		this.status = status;
		this.sharedLvl = sharedLvl;
		this.consumeCode = consumeCode;
	}

	/**
	 * 获取 主键. 
	 */
	public String getUserCouponCode() {
		return this.userCouponCode;
	}

	/**
	 * 设置 主键. 
	 */
	public void setUserCouponCode(String userCouponCode) {
		this.userCouponCode = userCouponCode;
	}

	/**
	 * 获取 用户编码. 
	 */
	public String getUserCode() {
		return this.userCode;
	}

	/**
	 * 设置 用户编码. 
	 */
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	/**
	 * 获取 优惠券编码. 
	 */
	public String getBatchCouponCode() {
		return this.batchCouponCode;
	}

	/**
	 * 设置 优惠券编码. 
	 */
	public void setBatchCouponCode(String batchCouponCode) {
		this.batchCouponCode = batchCouponCode;
	}

	/**
	 * 获取 申请时间. 
	 */
	public Date getApplyTime() {
		return this.applyTime;
	}

	/**
	 * 设置 申请时间. 
	 */
	public void setApplyTime(Date applyTime) {
		this.applyTime = applyTime;
	}

	/**
	 * 获取 优惠券状态. 
	 */
	public Byte getStatus() {
		return this.status;
	}

	/**
	 * 设置 优惠券状态. 
	 */
	public void setStatus(Byte status) {
		this.status = status;
	}

	/**
	 * 获取 优惠券的分享程度：所有人可见，朋友可见，其它人不可见. 
	 */
	public Integer getSharedLvl() {
		return this.sharedLvl;
	}

	/**
	 * 设置 优惠券的分享程度：所有人可见，朋友可见，其它人不可见. 
	 */
	public void setSharedLvl(Integer sharedLvl) {
		this.sharedLvl = sharedLvl;
	}

	/**
	 * 获取 在哪笔消费中使用了优惠券. 
	 */
	public String getConsumeCode() {
		return this.consumeCode;
	}

	/**
	 * 设置 在哪笔消费中使用了优惠券. 
	 */
	public void setConsumeCode(String consumeCode) {
		this.consumeCode = consumeCode;
	}

}
