package com.whhft.sysmanage.common.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;

import com.whhft.sysmanage.common.entity.SysAuth;

public class SysAuthExt  extends SysAuth{
	//DUBBO要求每个类都必须有一个无参数的构造函数，否则报错dubbo could not be instantiated
	public SysAuthExt(){
		
	}
	
	public SysAuthExt(SysAuth auth){
		BeanUtils.copyProperties(auth, this);
		attributes.put("url", auth.getAuthUrl());
		attributes.put("leaf", auth.getLeaf());
	}
	
	private List<SysAuthExt> children;
	private Map<String, String> attributes = new HashMap<String, String>();
	
	public List<SysAuthExt> getChildren() {
		if(children == null) {
			children = new ArrayList<SysAuthExt>();
		}
		return children;
	}

	public void setChildren(List<SysAuthExt> children) {
		this.children = children;
	}

	public void addChildern(SysAuthExt child) {
		getChildren().add(child);
	}

	public Map<String, String> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}
	
	public void putAttribute(String key, String value) {
		attributes.put(key, value);
	}
}
