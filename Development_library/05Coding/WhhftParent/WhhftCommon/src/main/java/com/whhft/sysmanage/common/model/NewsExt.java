package com.whhft.sysmanage.common.model;

import com.whhft.sysmanage.common.entity.News;

public class NewsExt extends News{
	private String catalogName;

	public String getCatalogName() {
		return catalogName;
	}

	public void setCatalogName(String catalogName) {
		this.catalogName = catalogName;
	}
	
}
