package com.huift.hfq.base.pojo;

import java.io.Serializable;

public class BankList implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 银行卡编号
	 */
	private String bankAccountCode;

	/**
	 * 银行卡后四位
	 */
	private String accountNbr;

	/**
	 * 银行卡前六位
	 */
	private String accountNbrPre6;

	/**
	 * 银行卡后四位
	 */
	private String accountNbrLast4;

	/**
	 * 银行卡名称
	 */
	private String bankName;

	/**
	 * 手机号码
	 */
	private String mobileNbr;
	
	public BankList() {
		super();
	}

	public BankList(String bankAccountCode, String accountNbr, String bankName,String mobileNbr) {
		super();
		this.bankAccountCode = bankAccountCode;
		this.accountNbr = accountNbr;
		this.bankName = bankName;
		this.mobileNbr = mobileNbr;
	}

	public String getBankAccountCode() {
		return bankAccountCode;
	}

	public void setBankAccountCode(String bankAccountCode) {
		this.bankAccountCode = bankAccountCode;
	}

	public String getAccountNbr() {
		return accountNbr;
	}

	public void setAccountNbr(String accountNbr) {
		this.accountNbr = accountNbr;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getAccountNbrPre6() {
		return accountNbrPre6;
	}

	public void setAccountNbrPre6(String accountNbrPre6) {
		this.accountNbrPre6 = accountNbrPre6;
	}

	public String getAccountNbrLast4() {
		return accountNbrLast4;
	}

	public void setAccountNbrLast4(String accountNbrLast4) {
		this.accountNbrLast4 = accountNbrLast4;
	}

	public String getMobileNbr() {
		return mobileNbr;
	}

	public void setMobileNbr(String mobileNbr) {
		this.mobileNbr = mobileNbr;
	}
	
	
}
