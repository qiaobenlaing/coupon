package com.whhft.sysmanage.web.utils;

import java.util.Map;

import com.alibaba.fastjson.serializer.NameFilter;

public class FastJSONameFilter implements  NameFilter {
	
	 Map<String, String> nameMap;
	 
	public FastJSONameFilter( Map<String, String> nameMap){
		this.nameMap = nameMap;
	}

	@Override
	public String process(Object source, String name, Object value) {
		String newName = nameMap.get(name);
		if(newName == null) {
			newName = name;
		}
		return newName;
	}
	
}
