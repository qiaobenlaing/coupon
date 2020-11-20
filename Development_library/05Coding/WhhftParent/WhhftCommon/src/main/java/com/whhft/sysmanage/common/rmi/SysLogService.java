package com.whhft.sysmanage.common.rmi;

import com.whhft.sysmanage.common.base.BaseService;
import com.whhft.sysmanage.common.entity.SysLog;
import com.whhft.sysmanage.common.model.SysLogExt;
import com.whhft.sysmanage.common.page.SysLogPage;
import com.github.pagehelper.PageInfo;

public interface SysLogService extends BaseService <SysLog, Integer> {
	PageInfo<SysLogExt> page(SysLogPage page);
}
