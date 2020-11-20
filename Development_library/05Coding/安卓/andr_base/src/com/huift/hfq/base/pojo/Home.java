package com.huift.hfq.base.pojo;

import java.io.Serializable;

/**
 * C端首页
 * @author yanfang.li
 *
 */
public class Home implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	private String id;
	
	/**
	 * 城市  用jsonObject对象{[{"name",citysName}]}
	 */
	private String citys;
	
	/**
	 * 活动  {[{"activityImg",act , "activityCode",actCode}]}
	 */
	private String acts;
	
//	"imgUrl");
//	String moduleName = (String) jsonObject.get("moduleName");
//	String webAddress = (String) jsonObject.get("webAddress");

	/**
	 * 第一个按钮 {"moduleName",webAddress,"webAddress="=webAddress}
	 */
	private String theme;
	
	private String homeResultString;
	
	public String getHomeResultString() {
		return homeResultString;
	}

	public void setHomeResultString(String homeResultString) {
		this.homeResultString = homeResultString;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCitys() {
		return citys;
	}

	public void setCitys(String citys) {
		this.citys = citys;
	}

	public String getActs() {
		return acts;
	}

	public void setActs(String acts) {
		this.acts = acts;
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
