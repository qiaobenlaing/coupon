package com.whhft.sysmanage.web.controller;





import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.whhft.sysmanage.common.entity.News;
import com.whhft.sysmanage.common.model.NewsExt;
import com.whhft.sysmanage.common.page.NewsPage;
import com.whhft.sysmanage.common.rmi.NewsService;
import com.whhft.sysmanage.web.base.BaseController;
import com.whhft.sysmanage.web.utils.DateUtils;
import com.whhft.sysmanage.web.utils.EasyGrid;
import com.whhft.sysmanage.web.utils.JsonUtil;
import com.whhft.sysmanage.web.utils.SequenceUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;

@Controller
@RequestMapping(value="/news",method=RequestMethod.POST)
public class NewsController extends BaseController{
	@Resource
	private NewsService newsService;

	/**
	 * 查询一页信息
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value="/page",method=RequestMethod.POST)
	public String page(@ModelAttribute NewsPage page) throws Exception {
		return JSON.toJSONString(new EasyGrid<NewsExt>(newsService
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
	public String save(HttpServletRequest request,@ModelAttribute News news) throws Exception {
		news.setModifiedDate(DateUtils.getCurrentTime());	
		news.setFileSeq(DateUtils.getCurrentTime("yyyy-MM-dd")+"/"+news.getCatalogId()+"/"+news.getNewsId());
		newsService.insert(news);
		return SUNCCESS;
	}
	
	
	@ResponseBody
	@RequestMapping(value="/update",method=RequestMethod.POST)
	public String update(HttpServletRequest request,@ModelAttribute News news) throws Exception {
		news.setModifiedDate(DateUtils.getCurrentTime());	
		news.setFileSeq(DateUtils.getCurrentTime("yyyy-MM-dd")+"/"+news.getCatalogId()+"/"+news.getNewsId());
		newsService.update(news);
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
	public String remove(HttpServletRequest request ,@RequestParam String newsIds ) throws Exception{
		newsService.removeAll(newsIds);
		return SUNCCESS;
	}
	
	/**
	 * 生成备用新闻ID
	 * @return
	 */
	@RequestMapping(value = "/generatorId",method=RequestMethod.POST)
	@ResponseBody	
	public String generatorId() {		
		return SequenceUtils.get();
	}
	
	/**
	 * 根据新闻id查询新闻
	 * @param newsId
	 * @return
	 */
	@RequestMapping(value = "/findone",method=RequestMethod.POST)
	@ResponseBody	
	public String findone(Long newsId) {		
		return JsonUtil.objectToJson(newsService.findOne(newsId));
	}
	
	
}
