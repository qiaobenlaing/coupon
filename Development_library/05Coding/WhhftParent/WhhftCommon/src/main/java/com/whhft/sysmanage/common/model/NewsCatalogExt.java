package com.whhft.sysmanage.common.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;

import com.whhft.sysmanage.common.entity.NewsCatalog;

public class NewsCatalogExt extends NewsCatalog {
	
	private String id;
	private String name;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	private List<NewsCatalogExt> children;
	private Map<String, String> attributes = new HashMap<String, String>();
	//DUBBO要求每个类都必须有一个无参数的构造函数，否则报错dubbo could not be instantiated
		public NewsCatalogExt(){
			
		}
		
		public NewsCatalogExt(NewsCatalog catalog){
			this.id=catalog.getCatalogId().toString();
			this.name=catalog.getCatalogName();
			BeanUtils.copyProperties(catalog, this);
			/*attributes.put("url", auth.getAuthUrl());
			attributes.put("leaf", auth.getLeaf());*/
		}
		
	
		
		public List<NewsCatalogExt> getChildren() {
			if(children == null) {
				children = new ArrayList<NewsCatalogExt>();
			}
			return children;
		}

		public void setChildren(List<NewsCatalogExt> children) {
			this.children = children;
		}

		public void addChildern(NewsCatalogExt child) {
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
