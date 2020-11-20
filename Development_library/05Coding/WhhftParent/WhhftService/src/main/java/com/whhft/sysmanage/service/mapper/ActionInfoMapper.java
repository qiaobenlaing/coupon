package com.whhft.sysmanage.service.mapper;

import java.util.List;
import java.util.Map;

import com.whhft.sysmanage.common.base.BaseMapper;
import com.whhft.sysmanage.common.entity.ActionInfo;

public interface ActionInfoMapper extends BaseMapper<ActionInfo, Integer> {
	public List<ActionInfo> find(Map<String, String> params);
}