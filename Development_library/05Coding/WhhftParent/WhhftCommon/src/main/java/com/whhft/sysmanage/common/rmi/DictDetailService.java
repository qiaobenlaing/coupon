package com.whhft.sysmanage.common.rmi;

import java.util.List;

import com.whhft.sysmanage.common.base.BaseService;
import com.whhft.sysmanage.common.entity.DictDetail;
import com.whhft.sysmanage.common.page.DictDetailPage;
import com.github.pagehelper.PageInfo;

public interface DictDetailService extends BaseService <DictDetail, Integer>{
	PageInfo<DictDetail> find(DictDetailPage page);
	List<DictDetail> getDictDetails(Integer defineId);
	public void removeAll(String detailIds);
}