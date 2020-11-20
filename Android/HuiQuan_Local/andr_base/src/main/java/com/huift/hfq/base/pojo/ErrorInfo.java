package com.huift.hfq.base.pojo;

/**
 * 错误信息
 * @author wenis.yu
 *
 */
public class ErrorInfo implements java.io.Serializable{

	private String value;
	
	private String info;

	
	
	public ErrorInfo(String value, String info) {
		super();
		this.value = value;
		this.info = info;
	}
	
	

	public String getValue() {
		return value;
	}



	public void setValue(String value) {
		this.value = value;
	}



	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}
	
	
}
