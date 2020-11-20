/**
 *
 * @Author: Jianping Chen
 * @Date: 2015.5.8
 * @Version: 1.0.0
 * @Copyright Suanzi Co.,Ltd. @ 2015
 * 
 */

package cn.suanzi.baomi.base.pojo;


public class Message implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private String msgCode;

	/** 
	 * 标题.
	 */
	private String title;

	/** 
	 * 内容.
	 */
	private String content;

	/** 
	 * 创建时间.
	 */
	private String createTime;

	/** 
	 * 发送者.
	 */
	private String senderCode;
	
	/**
	 * 头像
	 */
	private String avatarUrl;
	/**
	 * 会员消息
	 */
	private String message;
	
	/**
	 * 发送者编码
	 */
	private String creatorCode;

	/**
	 * 判断是谁的消息
	 */
	private boolean msgflag;
	
	/** 
	 * 消息类型.
	 */
	private Integer type;
	
	/**
	 * 未读消息数量
	 */
	private String count;

	public Message() {
	}

	public Message(String msgCode) {
		this.msgCode = msgCode;
	}

	public Message(String msgCode, String title, String content,
			String createTime, String senderCode, Integer type) {
		this.msgCode = msgCode;
		this.title = title;
		this.content = content;
		this.createTime = createTime;
		this.senderCode = senderCode;
		this.type = type;
	}

	/**
	 */
	public String getMsgCode() {
		return this.msgCode;
	}

	/**
	 */
	public void setMsgCode(String msgCode) {
		this.msgCode = msgCode;
	}

	/**
	 * 获取 标题. 
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * 设置 标题. 
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * 获取 内容. 
	 */
	public String getContent() {
		return this.content;
	}

	/**
	 * 设置 内容. 
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * 获取 创建时间. 
	 */
	public String getCreateTime() {
		return this.createTime;
	}

	/**
	 * 设置 创建时间. 
	 */
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	/**
	 * 获取 发送者. 
	 */
	public String getSenderCode() {
		return this.senderCode;
	}

	/**
	 * 设置 发送者. 
	 */
	public void setSenderCode(String senderCode) {
		this.senderCode = senderCode;
	}

	/**
	 * 获取 消息类型. 
	 */
	public Integer getType() {
		return this.type;
	}

	/**
	 * 设置 消息类型. 
	 */
	public void setType(Integer type) {
		this.type = type;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public String getCreatorCode() {
		return creatorCode;
	}

	public void setCreatorCode(String creatorCode) {
		this.creatorCode = creatorCode;
	}
	
	public boolean isMsgflag() {
		return msgflag;
	}

	public void setMsgflag(boolean msgflag) {
		this.msgflag = msgflag;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}
	
}
