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

public class BankAccount implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private String bankAccountCode;

	/** 
	 * 关联用户.
	 */
	private String userCode;

	/** 
	 * 卡号(加密后).
	 */
	private String accountNbr;

	/** 
	 * 账户名称.
	 */
	private String accountName;

	/** 
	 * 银行名称.
	 */
	private String bankName;

	/** 
	 * 状态.
	 */
	private Byte status;

	/** 
	 * 备注.
	 */
	private String remark;

	/** 
	 * 创建时间.
	 */
	private Date createTime;

	/** 
	 * 最后一次操作时间.
	 */
	private Date lastOperationTime;

	/** 
	 * 如果是信用卡，该字段表示卡的过期时间.
	 */
	private Date expireTime;

	/** 
	 * 手续费.
	 */
	private Integer fee;

	/** 
	 * 消费次数.
	 */
	private Integer consumeCount;

	private String realName;

	private String idCard;

	/** 
	 * 银行卡bin编号.
	 */
	private Long cardBin;

	public BankAccount() {
	}

	public BankAccount(String bankAccountCode) {
		this.bankAccountCode = bankAccountCode;
	}

	public BankAccount(String bankAccountCode, String userCode,
			String accountNbr, String accountName, String bankName,
			Byte status, String remark, Date createTime,
			Date lastOperationTime, Date expireTime, Integer fee,
			Integer consumeCount, String realName, String idCard, Long cardBin) {
		this.bankAccountCode = bankAccountCode;
		this.userCode = userCode;
		this.accountNbr = accountNbr;
		this.accountName = accountName;
		this.bankName = bankName;
		this.status = status;
		this.remark = remark;
		this.createTime = createTime;
		this.lastOperationTime = lastOperationTime;
		this.expireTime = expireTime;
		this.fee = fee;
		this.consumeCount = consumeCount;
		this.realName = realName;
		this.idCard = idCard;
		this.cardBin = cardBin;
	}

	/**
	 */
	public String getBankAccountCode() {
		return this.bankAccountCode;
	}

	/**
	 */
	public void setBankAccountCode(String bankAccountCode) {
		this.bankAccountCode = bankAccountCode;
	}

	/**
	 * 获取 关联用户. 
	 */
	public String getUserCode() {
		return this.userCode;
	}

	/**
	 * 设置 关联用户. 
	 */
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	/**
	 * 获取 卡号(加密后). 
	 */
	public String getAccountNbr() {
		return this.accountNbr;
	}

	/**
	 * 设置 卡号(加密后). 
	 */
	public void setAccountNbr(String accountNbr) {
		this.accountNbr = accountNbr;
	}

	/**
	 * 获取 账户名称. 
	 */
	public String getAccountName() {
		return this.accountName;
	}

	/**
	 * 设置 账户名称. 
	 */
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	/**
	 * 获取 银行名称. 
	 */
	public String getBankName() {
		return this.bankName;
	}

	/**
	 * 设置 银行名称. 
	 */
	public void setBankName(String bankName) {
		this.bankName = bankName;
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
	 * 获取 备注. 
	 */
	public String getRemark() {
		return this.remark;
	}

	/**
	 * 设置 备注. 
	 */
	public void setRemark(String remark) {
		this.remark = remark;
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
	 * 获取 最后一次操作时间. 
	 */
	public Date getLastOperationTime() {
		return this.lastOperationTime;
	}

	/**
	 * 设置 最后一次操作时间. 
	 */
	public void setLastOperationTime(Date lastOperationTime) {
		this.lastOperationTime = lastOperationTime;
	}

	/**
	 * 获取 如果是信用卡，该字段表示卡的过期时间. 
	 */
	public Date getExpireTime() {
		return this.expireTime;
	}

	/**
	 * 设置 如果是信用卡，该字段表示卡的过期时间. 
	 */
	public void setExpireTime(Date expireTime) {
		this.expireTime = expireTime;
	}

	/**
	 * 获取 手续费. 
	 */
	public Integer getFee() {
		return this.fee;
	}

	/**
	 * 设置 手续费. 
	 */
	public void setFee(Integer fee) {
		this.fee = fee;
	}

	/**
	 * 获取 消费次数. 
	 */
	public Integer getConsumeCount() {
		return this.consumeCount;
	}

	/**
	 * 设置 消费次数. 
	 */
	public void setConsumeCount(Integer consumeCount) {
		this.consumeCount = consumeCount;
	}

	/**
	 */
	public String getRealName() {
		return this.realName;
	}

	/**
	 */
	public void setRealName(String realName) {
		this.realName = realName;
	}

	/**
	 */
	public String getIdCard() {
		return this.idCard;
	}

	/**
	 */
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	/**
	 * 获取 银行卡bin编号. 
	 */
	public Long getCardBin() {
		return this.cardBin;
	}

	/**
	 * 设置 银行卡bin编号. 
	 */
	public void setCardBin(Long cardBin) {
		this.cardBin = cardBin;
	}

}
