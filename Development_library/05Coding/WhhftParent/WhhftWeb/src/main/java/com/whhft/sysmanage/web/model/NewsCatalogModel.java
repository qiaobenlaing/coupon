package com.whhft.sysmanage.web.model;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import com.whhft.sysmanage.common.entity.NewsCatalog;
import com.whhft.sysmanage.common.entity.SysAuth;

public class NewsCatalogModel extends NewsCatalog {

	private String id, text;



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



	public NewsCatalog getNewsCatalog() {
		NewsCatalog catalog = new NewsCatalog();
		if (StringUtils.isNoneEmpty(id)) {
			this.setCatalogId(Integer.parseInt(id));
		}
		this.setCatalogName(text);
		BeanUtils.copyProperties(this, catalog);

		return catalog;
	}
}
