/**
 *
 * @Author: Jianping Chen
 * @Date: 2015.5.8
 * @Version: 1.0.0
 * @Copyright Suanzi Co.,Ltd. @ 2015
 * 
 */

package cn.suanzi.baomi.base.pojo;

public class UserMessage implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private String userMsgCode;

	/** 
	 * 信息接收用户.
	 */
	private String receiverCode;

	/** 
	 * 对应的消息.
	 */
	private String msgCode;

	/** 
	 * 阅读状态.
	 */
	private Byte readingStatus;

	/** 
	 * 信息状态.
	 */
	private Byte msgStatus;
	
	/**
	 * 订票数量
	 */
	private String count;
	
	/**
	 * 订票人员类别
	 */
	private int  id;
	
	/**
	 * 订票人信息
	 */
	private String des;
	
	/**
	 * 票价
	 */
	private String price;

	public UserMessage() {
		
	}
	
	public UserMessage(String userMsgCode, String receiverCode, String msgCode, Byte readingStatus, Byte msgStatus,
			String count, int id, String des, String price) {
		super();
		this.userMsgCode = userMsgCode;
		this.receiverCode = receiverCode;
		this.msgCode = msgCode;
		this.readingStatus = readingStatus;
		this.msgStatus = msgStatus;
		this.count = count;
		this.id = id;
		this.des = des;
		this.price = price;
	}



	public UserMessage(String userMsgCode) {
		this.userMsgCode = userMsgCode;
	}

	/**
	 */
	public String getUserMsgCode() {
		return this.userMsgCode;
	}

	/**
	 */
	public void setUserMsgCode(String userMsgCode) {
		this.userMsgCode = userMsgCode;
	}

	/**
	 * 获取 信息接收用户. 
	 */
	public String getReceiverCode() {
		return this.receiverCode;
	}

	/**
	 * 设置 信息接收用户. 
	 */
	public void setReceiverCode(String receiverCode) {
		this.receiverCode = receiverCode;
	}

	/**
	 * 获取 对应的消息. 
	 */
	public String getMsgCode() {
		return this.msgCode;
	}

	/**
	 * 设置 对应的消息. 
	 */
	public void setMsgCode(String msgCode) {
		this.msgCode = msgCode;
	}

	/**
	 * 获取 阅读状态. 
	 */
	public Byte getReadingStatus() {
		return this.readingStatus;
	}

	/**
	 * 设置 阅读状态. 
	 */
	public void setReadingStatus(Byte readingStatus) {
		this.readingStatus = readingStatus;
	}

	/**
	 * 获取 信息状态. 
	 */
	public Byte getMsgStatus() {
		return this.msgStatus;
	}

	/**
	 * 设置 信息状态. 
	 */
	public void setMsgStatus(Byte msgStatus) {
		this.msgStatus = msgStatus;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

}
