package com.whhft.sysmanage.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.whhft.sysmanage.common.entity.NewsCatalog;
import com.whhft.sysmanage.common.entity.SysAuth;
import com.whhft.sysmanage.common.model.NewsCatalogExt;
import com.whhft.sysmanage.common.rmi.NewsCatalogService;
import com.whhft.sysmanage.common.rmi.NewsService;
import com.whhft.sysmanage.web.base.BaseController;
import com.whhft.sysmanage.web.model.NewsCatalogModel;
import com.whhft.sysmanage.web.model.SysAuthModel;
import com.whhft.sysmanage.web.utils.FastJSONUtil;
import com.whhft.sysmanage.web.utils.JsonUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value="/catalog",method=RequestMethod.POST)
public class CatalogController extends BaseController {

	@Resource
	private NewsCatalogService catalogService;
	

	@ResponseBody
	@RequestMapping("/catalogTree")
	public String catalogTree() throws Exception {
		Map<String, String> nameMap = new HashMap<String, String>();
		nameMap.put("id", "id");
		nameMap.put("name", "text");

		return FastJSONUtil
				.toJSONString(catalogService.loadCatalogTree(), nameMap);
	}
	

	@ResponseBody
	@RequestMapping("/save")
	public String save(HttpServletRequest request,
			@ModelAttribute NewsCatalogModel model) throws Exception {
		NewsCatalog catalog = model.getNewsCatalog();
		// 主键小于0说明是临时节点，进行新增操作
		if (catalog.getCatalogId() == null || catalog.getCatalogId() <= 0) {
			
			catalog.setCatalogId(null);
			catalogService.insert(catalog);
		} else {
			catalogService.update(catalog);
		}
		return SUNCCESS;
	}

	@ResponseBody
	@RequestMapping("/remove")
	public String remove(HttpServletRequest request,
			@RequestParam Integer catalogId) throws Exception {
		catalogService.delete(catalogId);
		return SUNCCESS;
	}
	
	@ResponseBody
	@RequestMapping("/catalogNameExist")
	public boolean catalogNameExist(String catalogName) throws Exception {
		if(catalogService.catalogNameExist(catalogName)){
			return true;
		}else{
			return false;
		}
	}
	
	@ResponseBody
	@RequestMapping("/getCatalogName")
	public String getCatalogName(Integer catalogId) throws Exception {
		return JsonUtil.objectToJson(catalogService.findOne(catalogId).getCatalogName());
	}

}
