package cn.suanzi.baomi.base.pojo;

import java.io.Serializable;

public class StaffToken implements Serializable {
	private String mobileNbr;
	private String realName;
	private String type;
	private String shopCode;
	public StaffToken() {
		super();
		// TODO Auto-generated constructor stub
	}
	public StaffToken(String mobileNbr, String realName, String type,
			String shopCode) {
		super();
		this.mobileNbr = mobileNbr;
		this.realName = realName;
		this.type = type;
		this.shopCode = shopCode;
	}
	public String getMobileNbr() {
		return mobileNbr;
	}
	public void setMobileNbr(String mobileNbr) {
		this.mobileNbr = mobileNbr;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getShopCode() {
		return shopCode;
	}
	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}
	
	
}
