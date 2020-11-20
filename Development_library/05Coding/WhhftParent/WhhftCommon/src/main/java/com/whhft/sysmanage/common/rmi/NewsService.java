package com.whhft.sysmanage.common.rmi;

import com.whhft.sysmanage.common.base.BaseService;
import com.whhft.sysmanage.common.entity.News;
import com.whhft.sysmanage.common.model.NewsExt;
import com.whhft.sysmanage.common.page.NewsPage;
import com.github.pagehelper.PageInfo;

public interface NewsService extends BaseService <News, Long>{
	PageInfo<NewsExt> page(NewsPage page);	
	void removeAll(String newsIds);
}
