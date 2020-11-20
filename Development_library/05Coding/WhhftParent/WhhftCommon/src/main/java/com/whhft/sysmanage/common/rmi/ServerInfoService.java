package com.whhft.sysmanage.common.rmi;

import com.whhft.sysmanage.common.base.BaseService;
import com.whhft.sysmanage.common.entity.ServerInfo;
import com.whhft.sysmanage.common.page.ServerInfoPage;
import com.github.pagehelper.PageInfo;

public interface ServerInfoService extends BaseService<ServerInfo, Integer> {
	public PageInfo<ServerInfo> page(ServerInfoPage page);
}
