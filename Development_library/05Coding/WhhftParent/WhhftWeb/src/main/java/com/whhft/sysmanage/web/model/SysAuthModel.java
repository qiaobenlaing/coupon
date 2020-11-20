package com.whhft.sysmanage.web.model;

import org.apache.commons.lang3.StringUtils;

import com.whhft.sysmanage.common.entity.SysAuth;
import com.whhft.sysmanage.web.utils.BeanUtils;

public class SysAuthModel extends SysAuth{
	private String id, text, iconCls;
	
	
	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getText() {
		return text;
	}


	public void setText(String text) {
		this.text = text;
	}


	public String getIconCls() {
		return iconCls;
	}


	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}


	public SysAuth getSysAuth(){
		SysAuth auth = new SysAuth();
		if(StringUtils.isNoneEmpty(id)){
			this.setAuthId(Integer.parseInt(id));
		}
		this.setAuthName(text);
		this.setIconStyle(iconCls);
		BeanUtils.copyPojoProperties(this, auth);
		return auth;
	}
}
