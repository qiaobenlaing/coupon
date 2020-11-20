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

public class Friends implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	/** 
	 * 主键.
	 */
	private String friendCode;

	/** 
	 * 朋友User.
	 */
	private String userFriendCode;

	/** 
	 * 主用户.
	 */
	private String userBasicCode;

	/** 
	 * 朋友User从主用户那里获取的分享优惠券.
	 */
	private Integer sharedCoupon;

	/** 
	 * 主用户销售给朋友用户的优惠券数量，注意可匿名.
	 */
	private Integer saledCoupon;

	/** 
	 * 关系生成日期.
	 */
	private Date createTime;

	public Friends() {
	}

	public Friends(String friendCode) {
		this.friendCode = friendCode;
	}

	public Friends(String friendCode, String userFriendCode,
			String userBasicCode, Integer sharedCoupon, Integer saledCoupon,
			Date createTime) {
		this.friendCode = friendCode;
		this.userFriendCode = userFriendCode;
		this.userBasicCode = userBasicCode;
		this.sharedCoupon = sharedCoupon;
		this.saledCoupon = saledCoupon;
		this.createTime = createTime;
	}

	/**
	 * 获取 主键. 
	 */
	public String getFriendCode() {
		return this.friendCode;
	}

	/**
	 * 设置 主键. 
	 */
	public void setFriendCode(String friendCode) {
		this.friendCode = friendCode;
	}

	/**
	 * 获取 朋友User. 
	 */
	public String getUserFriendCode() {
		return this.userFriendCode;
	}

	/**
	 * 设置 朋友User. 
	 */
	public void setUserFriendCode(String userFriendCode) {
		this.userFriendCode = userFriendCode;
	}

	/**
	 * 获取 主用户. 
	 */
	public String getUserBasicCode() {
		return this.userBasicCode;
	}

	/**
	 * 设置 主用户. 
	 */
	public void setUserBasicCode(String userBasicCode) {
		this.userBasicCode = userBasicCode;
	}

	/**
	 * 获取 朋友User从主用户那里获取的分享优惠券. 
	 */
	public Integer getSharedCoupon() {
		return this.sharedCoupon;
	}

	/**
	 * 设置 朋友User从主用户那里获取的分享优惠券. 
	 */
	public void setSharedCoupon(Integer sharedCoupon) {
		this.sharedCoupon = sharedCoupon;
	}

	/**
	 * 获取 主用户销售给朋友用户的优惠券数量，注意可匿名. 
	 */
	public Integer getSaledCoupon() {
		return this.saledCoupon;
	}

	/**
	 * 设置 主用户销售给朋友用户的优惠券数量，注意可匿名. 
	 */
	public void setSaledCoupon(Integer saledCoupon) {
		this.saledCoupon = saledCoupon;
	}

	/**
	 * 获取 关系生成日期. 
	 */
	public Date getCreateTime() {
		return this.createTime;
	}

	/**
	 * 设置 关系生成日期. 
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}
