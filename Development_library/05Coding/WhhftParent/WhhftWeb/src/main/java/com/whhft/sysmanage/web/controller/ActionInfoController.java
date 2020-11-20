package com.whhft.sysmanage.web.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import com.whhft.sysmanage.common.entity.ActionInfo;
import com.whhft.sysmanage.common.page.ActionInfoPage;
import com.whhft.sysmanage.common.rmi.ActionInfoService;
import com.whhft.sysmanage.web.base.BaseController;
import com.whhft.sysmanage.web.utils.EasyGrid;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping(value="/actionInfo",method=RequestMethod.POST)
public class ActionInfoController extends BaseController {

	@Resource
	private ActionInfoService actionInfoService;

	/**
	 * 查询一页信息
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value="/page",method=RequestMethod.POST)
	public String page(@ModelAttribute ActionInfoPage page) throws Exception {
		
		return JSON.toJSONString(new EasyGrid<ActionInfo>(actionInfoService
				.page(page)));
	}

	/**
	 * 保存信息（根据是否存在ID确定是增加还是修改）
	 * @param actionInfo
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value="/save",method=RequestMethod.POST)
	public String save(HttpServletRequest request,@ModelAttribute ActionInfo actionInfo) throws Exception {
		if (actionInfo.getActionId() == null) {
			actionInfoService.insert(actionInfo);
		}else{
			actionInfoService.update(actionInfo);
		}
		return SUNCCESS;
	}
	
	/**
	 * 删除信息（根据ID删除）
	 * @param actionIds
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value="/remove",method=RequestMethod.POST)
	public String remove(HttpServletRequest request ,@RequestParam String actionIds ) throws Exception{
		actionInfoService.removeAll(actionIds);
		return SUNCCESS;
	}
	


	
}
