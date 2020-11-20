package com.whhft.sysmanage.common.page;

import com.whhft.sysmanage.common.base.EasyPage;

public class SysLogPage extends EasyPage {
	
	private String loginName;
	
	private String userName;

	private String actionUrl;
	
	private String startTime;
	
	private String endTime;
	
	@Override
	public String getGridOrder(String sort) {
		String dbColumn  = null;
		switch(sort) {
		}
		return dbColumn;
	}

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

	public String getActionUrl() {
		return actionUrl;
	}

	public void setActionUrl(String actionUrl) {
		this.actionUrl = actionUrl;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
}
