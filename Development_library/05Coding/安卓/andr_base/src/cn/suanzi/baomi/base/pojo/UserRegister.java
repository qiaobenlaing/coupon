package cn.suanzi.baomi.base.pojo;
/**
 * @author wensi.yu
 * 
 * 用户注册
 */

public class UserRegister implements java.io.Serializable {

	/**
	 * 手机号码
	 */
	private String mRegisterPhonenum;
	
	/**
	 * 验证码
	 */
	private String mRegisterIdencode;
	
	/**
	 * 密码
	 */
	private String mRegisterPwd;
	
	/**
	 * 营业执照
	 */
	private String mbusinessNum;
	
	public UserRegister() {
		super();
	}
	
	
	public UserRegister(String mRegisterPhonenum, String mRegisterIdencode) {
		super();
		this.mRegisterPhonenum = mRegisterPhonenum;
		this.mRegisterIdencode = mRegisterIdencode;
	}




	public UserRegister(String mRegisterPhonenum, String mRegisterIdencode,
			String mRegisterPwd, String mbusinessNum) {
		super();
		this.mRegisterPhonenum = mRegisterPhonenum;
		this.mRegisterIdencode = mRegisterIdencode;
		this.mRegisterPwd = mRegisterPwd;
		this.mbusinessNum = mbusinessNum;
	}


	

	public String getmRegisterPhonenum() {
		return mRegisterPhonenum;
	}



	public void setmRegisterPhonenum(String mRegisterPhonenum) {
		this.mRegisterPhonenum = mRegisterPhonenum;
	}



	public String getmRegisterIdencode() {
		return mRegisterIdencode;
	}

	public void setmRegisterIdencode(String mRegisterIdencode) {
		this.mRegisterIdencode = mRegisterIdencode;
	}

	public String getmRegisterPwd() {
		return mRegisterPwd;
	}

	public void setmRegisterPwd(String mRegisterPwd) {
		this.mRegisterPwd = mRegisterPwd;
	}

	public String getMbusinessNum() {
		return mbusinessNum;
	}

	public void setMbusinessNum(String mbusinessNum) {
		this.mbusinessNum = mbusinessNum;
	}
	
	
}
