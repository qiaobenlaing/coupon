package com.whhft.sysmanage.common.rmi;

import java.util.List;

import com.whhft.sysmanage.common.base.BaseService;
import com.whhft.sysmanage.common.entity.NewsCatalog;
import com.whhft.sysmanage.common.model.NewsCatalogExt;

public interface NewsCatalogService extends BaseService <NewsCatalog, Integer>{
	public List<NewsCatalogExt> loadCatalogTree();	
	public boolean catalogNameExist(String catalogName);
}
