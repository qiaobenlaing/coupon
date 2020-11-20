package com.whhft.sysmanage.common.page;

import com.whhft.sysmanage.common.base.EasyPage;

public class ActionInfoPage extends EasyPage{


	private String actionName;
	
	private String actionUrl;


	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public String getActionUrl() {
		return actionUrl;
	}

	public void setActionUrl(String actionUrl) {
		this.actionUrl = actionUrl;
	}
	
	public String getGridOrder(String sort) {
		String dbColumn  = null;
		switch(sort) {
		}
		return dbColumn;
	}
}
