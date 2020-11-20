package com.whhft.sysmanage.web.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.whhft.sysmanage.common.entity.DictDetail;
import com.whhft.sysmanage.common.page.DictDetailPage;
import com.whhft.sysmanage.common.rmi.DictDetailService;
import com.whhft.sysmanage.web.base.BaseController;
import com.whhft.sysmanage.web.utils.EasyGrid;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

@Controller
@RequestMapping("/dictDetail")
public class DictDetailController extends BaseController{
	
	@Resource
	private DictDetailService dictDetailService;

	@ResponseBody
	@RequestMapping("/page")
	public String page(@ModelAttribute  DictDetailPage page) throws Exception{
		//DictDetail的项不会很多，所以这里其实不分页
		return JSON.toJSONString(new EasyGrid<DictDetail>(dictDetailService.find(page)));
	}
	
	@ResponseBody
	@RequestMapping("/save")
	public String save(HttpServletRequest request,@ModelAttribute  DictDetail dictDetail) throws Exception{
		if(dictDetail.getDictDetailId() == null){
			dictDetailService.insert(dictDetail);
		}else{
			dictDetailService.update(dictDetail);
		}
		return SUNCCESS;
	}
	
	@ResponseBody
	@RequestMapping("/load")
	public String load(@ModelAttribute  @RequestParam Integer dictDetailId) throws Exception{
		return JSON.toJSONString(dictDetailService.findOne(dictDetailId));
	}
	
	@ResponseBody
	@RequestMapping("/remove")
	public String remove(HttpServletRequest request,@ModelAttribute  @RequestParam String dictDetailIds) throws Exception{
		dictDetailService.removeAll(dictDetailIds);
		return SUNCCESS;
	}
	
}
