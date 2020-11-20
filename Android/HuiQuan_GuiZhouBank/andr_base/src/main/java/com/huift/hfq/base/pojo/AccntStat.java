package com.huift.hfq.base.pojo;


public class AccntStat implements java.io.Serializable  {

	private String acctCode;
	private String acctYear;
	private String acctMonth;
	private String acctDay;
	public AccntStat() {
		super();
	}
	public AccntStat(String acctCode, String acctYear, String acctMonth,
			String acctDay) {
		super();
		this.acctCode = acctCode;
		this.acctYear = acctYear;
		this.acctMonth = acctMonth;
		this.acctDay = acctDay;
	}
	public String getAcctCode() {
		return acctCode;
	}
	public void setAcctCode(String acctCode) {
		this.acctCode = acctCode;
	}
	public String getAcctYear() {
		return acctYear;
	}
	public void setAcctYear(String acctYear) {
		this.acctYear = acctYear;
	}
	public String getAcctMonth() {
		return acctMonth;
	}
	public void setAcctMonth(String acctMonth) {
		this.acctMonth = acctMonth;
	}
	public String getAcctDay() {
		return acctDay;
	}
	public void setAcctDay(String acctDay) {
		this.acctDay = acctDay;
	}
	
	
	
}
