package com.whhft.sysmanage.common.rmi;


import com.whhft.sysmanage.common.base.BaseService;
import com.whhft.sysmanage.common.entity.ActionInfo;
import com.whhft.sysmanage.common.page.ActionInfoPage;
import com.github.pagehelper.PageInfo;


public interface ActionInfoService extends BaseService <ActionInfo,Integer>{
	
	PageInfo<ActionInfo> page(ActionInfoPage page);	
	void removeAll(String actionIds);
	
}
