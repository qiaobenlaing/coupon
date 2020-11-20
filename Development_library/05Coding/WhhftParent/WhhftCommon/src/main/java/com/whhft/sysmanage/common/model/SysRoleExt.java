package com.whhft.sysmanage.common.model;

import org.springframework.beans.BeanUtils;

import com.whhft.sysmanage.common.entity.SysRole;

public class SysRoleExt extends SysRole {
	
	public SysRoleExt(SysRole role){
		BeanUtils.copyProperties(role, this);
	}
	
	private String cascadeAuthIds;
	private String cascadeAuthNames;
	private boolean checked;
	
	
	public String getCascadeAuthIds() {
		return cascadeAuthIds;
	}
	public void setCascadeAuthIds(String cascadeAuthIds) {
		this.cascadeAuthIds = cascadeAuthIds;
	}
	public String getCascadeAuthNames() {
		return cascadeAuthNames;
	}
	public void setCascadeAuthNames(String cascadeAuthNames) {
		this.cascadeAuthNames = cascadeAuthNames;
	}
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
}
