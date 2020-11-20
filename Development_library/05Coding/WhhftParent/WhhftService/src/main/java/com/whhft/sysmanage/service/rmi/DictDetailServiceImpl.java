package com.whhft.sysmanage.service.rmi;


import java.util.List;

import com.whhft.sysmanage.common.base.BaseMapper;
import com.whhft.sysmanage.common.base.BaseServiceImpl;
import com.whhft.sysmanage.common.entity.DictDetail;
import com.whhft.sysmanage.common.entity.DictDetailExample;
import com.whhft.sysmanage.common.page.DictDetailPage;
import com.whhft.sysmanage.common.rmi.DictDetailService;
import com.whhft.sysmanage.service.mapper.DictDefineMapper;
import com.whhft.sysmanage.service.mapper.DictDetailMapper;
import com.whhft.sysmanage.service.utils.DictUtil;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import tk.mybatis.mapper.util.StringUtil;

@Service("dictDetailServiceImpl")
@Transactional
public class DictDetailServiceImpl extends BaseServiceImpl<DictDetail, Integer>
		implements DictDetailService {

	@Autowired
	private DictDetailMapper dictDetailMapper;

	@Autowired
	private DictDefineMapper defineMapper;
	
	@Value("${REDIS_ENABLE}")
	private String redisEnable;
	
	@Autowired
	private DictUtil dictUtil;
	

	public BaseMapper<DictDetail, Integer> mapper() {
		return dictDetailMapper;
	}

	// DictDetail不用分页，直接在列表中显示所有数据项
	@SuppressWarnings("unchecked")
	@Override
	public PageInfo<DictDetail> find(DictDetailPage page) {

		DictDetailExample example = new DictDetailExample();
		DictDetailExample.Criteria cr = example.createCriteria();
		if (!StringUtil.isEmpty(page.getDictDefineId())) {
			cr.andDictDefineIdEqualTo(Integer.parseInt(page.getDictDefineId()));
		}
		example.setOrderByClause(page.getOrderByClause());
		PageHelper.startPage(0, 0);
		return new PageInfo<DictDetail>(
				dictDetailMapper.selectByExample(example));
	}

	/**
	 * 增加一条dictDetail信息
	 */
	@Override
	public Integer insert(DictDetail entity) {		
		super.insert(entity);//增加		
		if (redisEnable.equalsIgnoreCase("true")) {
			dictUtil.updateRedisByDefineId(entity.getDictDefineId());
		}
				
		return entity.getDictDetailId();
	}

	@Override
	public void update(DictDetail entity) {		
		super.update(entity);
		if (redisEnable.equalsIgnoreCase("true")) {
			dictUtil.updateRedisByDefineId(entity.getDictDefineId());
		}
	}

	@Override
	public void delete(Integer id) {
		DictDetail detail = findOne(id);		
		super.delete(id);		
		if (redisEnable.equalsIgnoreCase("true")) {
			dictUtil.updateRedisByDefineId(detail.getDictDefineId());
		}		
	}

	@Override
	public List<DictDetail> getDictDetails(Integer defineId) {
		DictDetailExample example = new DictDetailExample();
		DictDetailExample.Criteria cr = example.createCriteria();
		cr.andDictDefineIdEqualTo(defineId);
		List<DictDetail> list = dictDetailMapper.selectByExample(example);
		return list;
	}

	/**
	 * 根据选择的id批量删除
	 */
	@Override
	public void removeAll(String detailIds) {
		for(String detailId: detailIds.split(",")){
			if(StringUtils.isNoneEmpty(detailId)){
				delete(Integer.parseInt(detailId));
			}
		}		
	}

}
