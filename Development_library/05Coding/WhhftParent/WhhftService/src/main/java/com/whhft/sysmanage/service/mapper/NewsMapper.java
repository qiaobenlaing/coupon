package com.whhft.sysmanage.service.mapper;



import java.util.List;
import java.util.Map;

import com.whhft.sysmanage.common.base.BaseMapper;
import com.whhft.sysmanage.common.entity.News;
import com.whhft.sysmanage.common.model.NewsExt;

public interface NewsMapper extends BaseMapper<News, Long> {
	public List<NewsExt> find(Map<String, String> params);
}