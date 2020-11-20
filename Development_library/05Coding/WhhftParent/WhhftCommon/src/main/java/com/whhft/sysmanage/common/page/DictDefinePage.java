package com.whhft.sysmanage.common.page;

import com.whhft.sysmanage.common.base.EasyPage;

public class DictDefinePage extends EasyPage {
	
	private String dictDefineKey;
	private String dictDefineName;
	
	public String getDictDefineKey() {
		return dictDefineKey;
	}
	public void setDictDefineKey(String dictDefineKey) {
		this.dictDefineKey = dictDefineKey;
	}
	public String getDictDefineName() {
		return dictDefineName;
	}
	public void setDictDefineName(String dictDefineName) {
		this.dictDefineName = dictDefineName;
	}
	
	//根据GRID的字段转换为表中排序的字段名
	@Override
	public String getGridOrder(String sort) {
		String dbColumn  = null;
		switch(sort) {
			case "dictDefineKey": dbColumn = "DICT_DEFINE_KEY"; break;
			case "dictDefineName": dbColumn = "DICT_DEFINE_NAME"; break;
			case "dictDefineValue": dbColumn = "DICT_DEFINE_VALUE"; break;
			case "dictDefineType": dbColumn = "DICT_DEFINE_TYPE"; break;
		}
		return dbColumn;
	}
}
