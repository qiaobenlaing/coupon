package com.whhft.sysmanage.web.controller;

import javax.annotation.Resource;

import com.whhft.sysmanage.common.entity.ServerInfo;
import com.whhft.sysmanage.common.page.ServerInfoPage;
import com.whhft.sysmanage.common.rmi.ServerInfoService;
import com.whhft.sysmanage.web.base.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.whhft.sysmanage.web.utils.EasyGrid;

import com.alibaba.fastjson.JSON;

/**
 * @author leijing
 * 硬件信息监控
 */
@Controller
@RequestMapping("/sysmonitor")
public class ServerInfoController extends BaseController {
	@Resource
	private ServerInfoService serverInfoService;
	@ResponseBody
	@RequestMapping("/chart")
	public String chart(@ModelAttribute ServerInfoPage page) throws Exception{
		return JSON.toJSONString(new EasyGrid<ServerInfo>(serverInfoService.page(page)));
	}
}
