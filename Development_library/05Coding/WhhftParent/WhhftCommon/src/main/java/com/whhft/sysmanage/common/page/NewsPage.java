package com.whhft.sysmanage.common.page;

import com.whhft.sysmanage.common.base.EasyPage;

public class NewsPage extends EasyPage{


	private String catalogName;
	
	private String newsTitle;



	public String getCatalogName() {
		return catalogName;
	}



	public void setCatalogName(String catalogName) {
		this.catalogName = catalogName;
	}



	public String getNewsTitle() {
		return newsTitle;
	}



	public void setNewsTitle(String newsTitle) {
		this.newsTitle = newsTitle;
	}



	public String getGridOrder(String sort) {
		String dbColumn  = null;
		switch(sort) {
		}
		return dbColumn;
	}
}
