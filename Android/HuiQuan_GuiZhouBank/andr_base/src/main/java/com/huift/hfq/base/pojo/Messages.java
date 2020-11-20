// ---------------------------------------------------------
// @author    yanfang.li
// @version   1.0.0
// @createTime 2015.5.21
// @copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
// ---------------------------------------------------------
package com.huift.hfq.base.pojo;

import java.io.Serializable;

/**
 * 消息 
 * @author yanfang.li
 */
public class Messages implements Serializable{
	
	/**
	 * 用户昵称
	 */
	private String nickName;
	/**
	 * 会员编码
	 */
	private String userCode;
	/**
	 * 判断是谁的消息
	 */
	private boolean msgflag;
	/**
	 * 会员姓名
	 */
	private String userName;
	
	/**
	 * 消息内容
	 */
	private String content;
	
	/**
	 * 会员消息
	 */
	private String message;
	
	/**
	 * 用户头像
	 */
	private String avatarUrl;
	
	
	/**
	 * 会员的头像
	 */
	private String userAvatar;
	
	/**
	 * 创建时间
	 */
	private String createTime;
	
	/**
	 * 商店名称
	 */
	private String shopName;
	
	/**
	 * 商家编码
	 */
	private String shopCode;
	/**
	 * 商店logo
	 */
	private String logoUrl;
	
	/**
	 * 员工姓名
	 */
	private String staffName;
	
	/**
	 * 员工头像
	 */
	private String staffAvatar;
	
	/**
	 * 判断是谁的消息
	 */
	private int from_where;
	
	/**
	 * 未读的消息数量
	 */
	private int unreadCount;
	
	/**
	 * 留言的消息数目
	 */
	private int communication;
   
	/**
    * 优惠券消息
    */
	private int  coupon;
   
	/**
	 * 会员卡
	 */
	private int card;
	
	/**
	 * 异业沟通
	 */
	private int shop;
	
	/**
	 * 惠圈建议
	 */
	private int feedback;
	
	/**
	 * 用户优惠券编码
	 */
	private String userCouponCode;
	
	/**
	 * 消息里面优惠券编码
	 */
	private String batchCouponCode;
	
	/**
	 * 消息里面优惠券类型
	 */
	private String couponType;
	
	public Messages() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Messages(String nickName, String content, String avatarUrl,
			String createTime, String shopName, String logoUrl) {
		super();
		this.nickName = nickName;
		this.content = content;
		this.avatarUrl = avatarUrl;
		this.createTime = createTime;
		this.shopName = shopName;
		this.logoUrl = logoUrl;
	}
	
	public String getBatchCouponCode() {
		return batchCouponCode;
	}

	public void setBatchCouponCode(String batchCouponCode) {
		this.batchCouponCode = batchCouponCode;
	}

	public String getCouponType() {
		return couponType;
	}

	public void setCouponType(String couponType) {
		this.couponType = couponType;
	}

	public int getFeedback() {
		return feedback;
	}

	public void setFeedback(int feedback) {
		this.feedback = feedback;
	}

	public String getUserCouponCode() {
		return userCouponCode;
	}

	public void setUserCouponCode(String userCouponCode) {
		this.userCouponCode = userCouponCode;
	}

	public int getCommunication() {
		return communication;
	}

	public void setCommunication(int communication) {
		this.communication = communication;
	}

	public int getCoupon() {
		return coupon;
	}

	public void setCoupon(int coupon) {
		this.coupon = coupon;
	}

	public int getCard() {
		return card;
	}

	public void setCard(int card) {
		this.card = card;
	}

	public int getShop() {
		return shop;
	}

	public void setShop(int shop) {
		this.shop = shop;
	}

	public int getUnreadCount() {
		return unreadCount;
	}

	public void setUnreadCount(int unreadCount) {
		this.unreadCount = unreadCount;
	}

	public String getStaffName() {
		return staffName;
	}

	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}

	public String getStaffAvatar() {
		return staffAvatar;
	}

	public void setStaffAvatar(String staffAvatar) {
		this.staffAvatar = staffAvatar;
	}

	public String getShopCode() {
		return shopCode;
	}

	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public void setFrom_where(int from_where) {
		this.from_where = from_where;
	}

	public int getFrom_where() {
		return from_where;
	}

	public boolean isMsgflag() {
		return msgflag;
	}

	public void setMsgflag(boolean msgflag) {
		this.msgflag = msgflag;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getUserAvatar() {
		return userAvatar;
	}

	public void setUserAvatar(String userAvatar) {
		this.userAvatar = userAvatar;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getLogoUrl() {
		return logoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
}
