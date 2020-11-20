package com.whhft.sysmanage.service.rmi;



import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.whhft.sysmanage.common.base.BaseMapper;
import com.whhft.sysmanage.common.base.BaseServiceImpl;
import com.whhft.sysmanage.common.entity.News;
import com.whhft.sysmanage.common.model.NewsExt;
import com.whhft.sysmanage.common.page.NewsPage;
import com.whhft.sysmanage.common.rmi.NewsService;
import com.whhft.sysmanage.service.mapper.NewsMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;


@Service("newsServiceImpl")
@Transactional
public class NewsServiceImpl  extends BaseServiceImpl<News, Long> 
		implements NewsService {

	@Autowired
	private NewsMapper newsMapper;

	@Override
	public BaseMapper<News, Long> mapper() {
		return newsMapper;
	}

	@Override
	public PageInfo<NewsExt> page(NewsPage page) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("catalogName", page.getCatalogName());
		params.put("newsTitle", page.getNewsTitle());
		PageHelper.startPage(page.getPage(), page.getRows());
		
		return new PageInfo<NewsExt>(newsMapper.find(params));
	}

	@Override
	public void removeAll(String newsIds) {
		for(String newsId: newsIds.split(",")){
			if(StringUtils.isNoneEmpty(newsId)){
				delete(Long.valueOf(newsId));
			}
		}
	}

	@Override
	public void update(News entity) {		
		newsMapper.updateByPrimaryKeySelective(entity);
	}

}