package com.whhft.sysmanage.common.rmi;

import java.util.List;

import com.whhft.sysmanage.common.base.BaseService;
import com.whhft.sysmanage.common.entity.DictDefine;
import com.whhft.sysmanage.common.page.DictDefinePage;
import com.github.pagehelper.PageInfo;

public interface DictDefineService extends BaseService <DictDefine, Integer>{
	public PageInfo<DictDefine> page(DictDefinePage page);
	public List<DictDefine> getAll();
	public void removeAll(String defineIds) ;
}