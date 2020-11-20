package com.huift.hfq.base.pojo;

/**
 * 
 * @author wensi.yu
 * 对账详情
 *
 */
public class AccntStatDetail implements java.io.Serializable {

	/**
	 * 交易时间
	 */
	private String time;
	
	/**
	 * 交易卡号
	 */
	private String accountNbr;
	
	/**
	 * 交易金额
	 */
	private String price;
	
	/**
	 * 发卡银行
	 */
	private String bankName;
	
	/**
	 * 交易类型
	 */
	private String Type;
	
	/**
	 * 交易渠道
	 */
	private String channel;
	
	/**
	 * 流水号
	 */
	private String serialNbr;
	
	/**
	 * 检索号
	 */
	private String coderievalNbr;
	
	/**
	 * 交易回佣
	 */
	private String rebate;
	
	/**
	 * 对账情况
	 */
	private String situation;
	
	/**
	 * 分期手续费
	 */
	private String installmentFee;
	
	/**
	 * 分期期数
	 */
	private String installmentNbr;
	
	/**
	 * 持卡人费率
	 */
	private String rate;
	
	/**
	 * 持卡人手续费
	 */
	private String fee;
	
	public AccntStatDetail() {
		super();
	}

	public AccntStatDetail(String time, String accountNbr, String price,
			String bankName, String type, String channel, String serialNbr,
			String coderievalNbr, String rebate, String situation,
			String installmentFee, String installmentNbr, String rate,
			String fee) {
		super();
		this.time = time;
		this.accountNbr = accountNbr;
		this.price = price;
		this.bankName = bankName;
		Type = type;
		this.channel = channel;
		this.serialNbr = serialNbr;
		this.coderievalNbr = coderievalNbr;
		this.rebate = rebate;
		this.situation = situation;
		this.installmentFee = installmentFee;
		this.installmentNbr = installmentNbr;
		this.rate = rate;
		this.fee = fee;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getAccountNbr() {
		return accountNbr;
	}

	public void setAccountNbr(String accountNbr) {
		this.accountNbr = accountNbr;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getType() {
		return Type;
	}

	public void setType(String type) {
		Type = type;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getSerialNbr() {
		return serialNbr;
	}

	public void setSerialNbr(String serialNbr) {
		this.serialNbr = serialNbr;
	}

	public String getCoderievalNbr() {
		return coderievalNbr;
	}

	public void setCoderievalNbr(String coderievalNbr) {
		this.coderievalNbr = coderievalNbr;
	}

	public String getRebate() {
		return rebate;
	}

	public void setRebate(String rebate) {
		this.rebate = rebate;
	}

	public String getSituation() {
		return situation;
	}

	public void setSituation(String situation) {
		this.situation = situation;
	}

	public String getInstallmentFee() {
		return installmentFee;
	}

	public void setInstallmentFee(String installmentFee) {
		this.installmentFee = installmentFee;
	}

	public String getInstallmentNbr() {
		return installmentNbr;
	}

	public void setInstallmentNbr(String installmentNbr) {
		this.installmentNbr = installmentNbr;
	}

	public String getRate() {
		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}

	public String getFee() {
		return fee;
	}

	public void setFee(String fee) {
		this.fee = fee;
	}

	

	


	
}
