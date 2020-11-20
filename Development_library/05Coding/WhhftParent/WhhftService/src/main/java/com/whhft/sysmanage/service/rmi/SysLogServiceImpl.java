package com.whhft.sysmanage.service.rmi;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import com.whhft.sysmanage.common.base.BaseMapper;
import com.whhft.sysmanage.common.base.BaseService;
import com.whhft.sysmanage.common.base.BaseServiceImpl;
import com.whhft.sysmanage.common.entity.SysLog;
import com.whhft.sysmanage.common.model.SysLogExt;
import com.whhft.sysmanage.common.page.SysLogPage;
import com.whhft.sysmanage.common.rmi.SysLogService;
import com.whhft.sysmanage.service.mapper.SysLogMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service("sysLogServiceImpl")
public class SysLogServiceImpl extends BaseServiceImpl<SysLog, Integer> implements SysLogService{
	
	@Autowired
	private SysLogMapper sysLogMapper;
	
	public BaseMapper<SysLog, Integer> mapper(){
		return sysLogMapper;
	}
	
	@Override
	public PageInfo<SysLogExt> page(SysLogPage page){
		Map<String, String> params = new HashMap<String, String>();
		params.put("loginName", page.getLoginName());
		params.put("userName", page.getUserName());
		params.put("actionUrl", page.getActionUrl());
		params.put("startTime", page.getStartTime());	
		params.put("endTime", page.getEndTime());	
		PageHelper.startPage(page.getPage(), page.getRows()); 
		return new PageInfo<SysLogExt>(sysLogMapper.find(params));
	}
}
