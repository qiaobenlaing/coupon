package com.huift.hfq.base.pojo;
/**
 * @author wensi.yu
 * 
 * 用户重置密码
 */

public class UserReset implements java.io.Serializable {

	/**
	 * 手机号
	 */
	private String mResetNum;
	
	/**
	 * 验证码
	 */
	private String mResetIdencode;
	
	/**
	 * 密码
	 */
	private String mResetPwd;
	
	public UserReset() {
		super();
	}
	
	
	
	
	public UserReset(String mResetNum, String mResetIdencode) {
		super();
		this.mResetNum = mResetNum;
		this.mResetIdencode = mResetIdencode;
	}




	public UserReset(String mResetNum, String mResetIdencode, String mResetPwd) {
		super();
		this.mResetNum = mResetNum;
		this.mResetIdencode = mResetIdencode;
		this.mResetPwd = mResetPwd;
	}


	public String getmResetNum() {
		return mResetNum;
	}
	public void setmResetNum(String mResetNum) {
		this.mResetNum = mResetNum;
	}
	public String getmResetIdencode() {
		return mResetIdencode;
	}
	public void setmResetIdencode(String mResetIdencode) {
		this.mResetIdencode = mResetIdencode;
	}
	public String getmResetPwd() {
		return mResetPwd;
	}
	public void setmResetPwd(String mResetPwd) {
		this.mResetPwd = mResetPwd;
	}
	
	
}
