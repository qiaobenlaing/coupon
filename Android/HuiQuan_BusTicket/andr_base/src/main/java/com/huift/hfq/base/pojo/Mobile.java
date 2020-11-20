package com.huift.hfq.base.pojo;

/**
 * 手机信息
 * 
 * @author liyanfang
 */
public class Mobile {

	/**
	 * 手机号归属城市
	 */
	private String city;
	/**
	 * 运营商
	 */
	private String operator;

	/**
	 * 手机号归属省
	 */
	private String province;

	/**
	 * 是否为空
	 */
	private boolean isNullFlag;
	
	/**
	 * 暂代价格
	 */
	private String price;
	
	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public boolean isNullFlag() {
		return isNullFlag;
	}

	public void setNullFlag(boolean isNullFlag) {
		this.isNullFlag = isNullFlag;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}
	
	

}
