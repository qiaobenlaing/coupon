package com.whhft.sysmanage.web.controller;

import javax.annotation.Resource;

import com.whhft.sysmanage.common.model.SysLogExt;
import com.whhft.sysmanage.common.page.SysLogPage;
import com.whhft.sysmanage.common.rmi.SysLogService;
import com.whhft.sysmanage.web.base.BaseController;
import com.whhft.sysmanage.web.utils.EasyGrid;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

@Controller
@RequestMapping(value="/syslog",method=RequestMethod.POST)
public class SysLogController extends BaseController{
	@Resource
	private SysLogService sysLogService;
	
	@ResponseBody
	@RequestMapping(value="/page",method=RequestMethod.POST)
	public String page(@ModelAttribute  SysLogPage page) throws Exception{
		return JSON.toJSONString(new EasyGrid<SysLogExt>(sysLogService.page(page)));
	}

}
