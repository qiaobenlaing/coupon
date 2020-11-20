package com.whhft.sysmanage.common.page;

import com.whhft.sysmanage.common.base.EasyPage;

public class SysUserPage extends EasyPage {
	
	private String loginName;
	private String userName;
	private String enabled;
	
	
	public String getLoginName() {
		return loginName;
	}


	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}


	public String getUserName() {
		return userName;
	}


	public void setUserName(String userName) {
		this.userName = userName;
	}


	public String getEnabled() {
		return enabled;
	}


	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}


	//根据GRID的字段转换为表中排序的字段名
	@Override
	public String getGridOrder(String sort) {
		String dbColumn  = null;
		switch(sort) {
			case "loginName": dbColumn = "LOGIN_NAME"; break;
			case "userName": dbColumn = "USER_NAME"; break;
		}
		return dbColumn;
	}
}
