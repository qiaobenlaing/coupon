package cn.suanzi.baomi.base.pojo;


public class AccntStatItem implements java.io.Serializable {

	/**
	 * 对账编号
	 */
	private String accntBankListCode;
	
	/**
	 * 对账日期
	 */
	private String date;
	
	/**
	 * 刷卡笔数
	 */
	private String consumeCount; 
	
	/**
	 * 刷卡金额
	 */
	private String consumeAmount; 
	
	/**
	 * 回佣金额
	 */
	private String rebateAmount; 
	
	/**
	 * 入账金额
	 */
	private String theAmountCredited;
	
	/**
	 * 总存款数
	 */
	private String saving;
	
	public AccntStatItem() {
		super();
	}
	

	public AccntStatItem(String accntBankListCode, String date,
			String consumeCount, String consumeAmount, String rebateAmount,
			String theAmountCredited, String saving) {
		super();
		this.accntBankListCode = accntBankListCode;
		this.date = date;
		this.consumeCount = consumeCount;
		this.consumeAmount = consumeAmount;
		this.rebateAmount = rebateAmount;
		this.theAmountCredited = theAmountCredited;
		this.saving = saving;
	}


	public String getAccntBankListCode() {
		return accntBankListCode;
	}

	public void setAccntBankListCode(String accntBankListCode) {
		this.accntBankListCode = accntBankListCode;
	}


	public String getDate() {
		return date;
	}


	public void setDate(String date) {
		this.date = date;
	}


	public String getConsumeCount() {
		return consumeCount;
	}


	public void setConsumeCount(String consumeCount) {
		this.consumeCount = consumeCount;
	}


	public String getConsumeAmount() {
		return consumeAmount;
	}


	public void setConsumeAmount(String consumeAmount) {
		this.consumeAmount = consumeAmount;
	}


	public String getRebateAmount() {
		return rebateAmount;
	}


	public void setRebateAmount(String rebateAmount) {
		this.rebateAmount = rebateAmount;
	}


	public String getTheAmountCredited() {
		return theAmountCredited;
	}


	public void setTheAmountCredited(String theAmountCredited) {
		this.theAmountCredited = theAmountCredited;
	}


	public String getSaving() {
		return saving;
	}


	public void setSaving(String saving) {
		this.saving = saving;
	}

	
}
