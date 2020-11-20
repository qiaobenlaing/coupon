package com.whhft.sysmanage.common.page;

import com.whhft.sysmanage.common.base.EasyPage;

public class ServerInfoPage extends EasyPage {
	//定义页面查询要素
	private String hardWareName;

	public String getHardWareName() {
		return hardWareName;
	}

	public void setHardWareName(String hardWareName) {
		this.hardWareName = hardWareName;
	}
	//根据GRID的字段转换为表中排序的字段名
	@Override
	public String getGridOrder(String sort) {
		String dbColumn  = null;
		switch(sort) {
			//case "hardWareName": dbColumn = "DICT_DEFINE_KEY"; break;
	    }
		return dbColumn;
	}
}
