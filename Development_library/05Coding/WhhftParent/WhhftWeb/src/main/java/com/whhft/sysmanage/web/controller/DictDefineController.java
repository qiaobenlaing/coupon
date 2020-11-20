package com.whhft.sysmanage.web.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.whhft.sysmanage.common.entity.DictDefine;
import com.whhft.sysmanage.common.page.DictDefinePage;
import com.whhft.sysmanage.common.rmi.DictDefineService;
import com.whhft.sysmanage.web.base.BaseController;
import com.whhft.sysmanage.web.utils.EasyGrid;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

@Controller
@RequestMapping("/dictDefine")
public class DictDefineController extends BaseController{
	
	@Resource
	private DictDefineService dictDefineService;

	@ResponseBody
	@RequestMapping(value="/page",method=RequestMethod.POST)
	public String page(@ModelAttribute  DictDefinePage page) throws Exception{
		//主菜单JSON必须是一个数组，即使只有一个元素，也必须返回的是一个数组，否则EASYUI控件解析不正确
		return JSON.toJSONString(new EasyGrid<DictDefine>(dictDefineService.page(page)));
		
	}
	
	@ResponseBody
	@RequestMapping(value="/save",method=RequestMethod.POST)
	public String save(HttpServletRequest request, @ModelAttribute  DictDefine dictDefine) throws Exception{
		if(dictDefine.getDictDefineId() == null){
			dictDefineService.insert(dictDefine);
		}else{
			dictDefineService.update(dictDefine);
		}
		return SUNCCESS;
	}
	
	//没用使用
	@ResponseBody
	@RequestMapping(value="/load",method=RequestMethod.POST)
	public String load(@ModelAttribute  @RequestParam Integer dictDefineId) throws Exception{
		return JSON.toJSONString(dictDefineService.findOne(dictDefineId));
	}
	
	@ResponseBody
	@RequestMapping(value="/remove",method=RequestMethod.POST)
	public String remove(HttpServletRequest request,@ModelAttribute  @RequestParam Integer dictDefineId) throws Exception{
		dictDefineService.delete(dictDefineId);
		return SUNCCESS;
	}
	
	
	
	
}
