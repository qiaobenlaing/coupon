package com.whhft.sysmanage.service.mapper;

import java.util.List;
import java.util.Map;

import com.whhft.sysmanage.common.base.BaseMapper;
import com.whhft.sysmanage.common.entity.SysLog;
import com.whhft.sysmanage.common.model.SysLogExt;

public interface SysLogMapper extends BaseMapper<SysLog, Integer> {
	public List<SysLogExt> find(Map<String, String> params);
}