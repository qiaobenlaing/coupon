package com.whhft.sysmanage.common.page;

import com.whhft.sysmanage.common.base.EasyPage;

public class DictDetailPage extends EasyPage {
	
	private String dictDefineId;

	public String getDictDefineId() {
		return dictDefineId;
	}

	public void setDictDefineId(String dictDefineId) {
		this.dictDefineId = dictDefineId;
	}

	@Override
	public String getGridOrder(String sort) {
		String dbColumn  = null;
		switch(sort) {
			case "dictDetailKey": dbColumn = "DICT_DETAIL_KEY"; break;
			case "dictDetailName": dbColumn = "DICT_DETAIL_NAME"; break;
		}
		return dbColumn;
	}
}
