package com.whhft.sysmanage.service.rmi;

import java.util.List;

import com.whhft.sysmanage.common.base.BaseMapper;
import com.whhft.sysmanage.common.base.BaseServiceImpl;
import com.whhft.sysmanage.common.entity.DictDefine;
import com.whhft.sysmanage.common.entity.DictDefineExample;
import com.whhft.sysmanage.common.entity.DictDetailExample;
import com.whhft.sysmanage.common.page.DictDefinePage;
import com.whhft.sysmanage.common.rmi.DictDefineService;
import com.whhft.sysmanage.service.mapper.DictDefineMapper;
import com.whhft.sysmanage.service.mapper.DictDetailMapper;
import com.whhft.sysmanage.service.utils.DictUtil;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tk.mybatis.mapper.util.StringUtil;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service("dictDefineServiceImpl")
@Transactional
public class DictDefineServiceImpl extends BaseServiceImpl<DictDefine, Integer>
		implements DictDefineService {

	@Autowired
	private DictDefineMapper dictDefineMapper;

	@Autowired
	private DictDetailMapper dictDetailMapper;

	@Autowired
	private DictUtil dictUtil;

	@Value("${REDIS_ENABLE}")
	private String redisEnable;

	public BaseMapper<DictDefine, Integer> mapper() {
		return dictDefineMapper;
	}

	@Override
	public void delete(Integer id){
		// 先删除detail，再删除define
		DictDetailExample example = new DictDetailExample();
		example.createCriteria().andDictDefineIdEqualTo(id);
		dictDetailMapper.deleteByExample(example);
		dictDefineMapper.deleteByPrimaryKey(id);

		// 将该id所对应的redis过期处理
		if (redisEnable.equalsIgnoreCase("true")) {
			dictUtil.delete(id);
		}
	};

	@Override
	public Integer insert(DictDefine entity) {
		// 添加并返回id
		super.insert(entity);
		Integer id = entity.getDictDefineId();

		if (redisEnable.equalsIgnoreCase("true")) {
			dictUtil.updateRedisByDefineId(id);
		}

		return id;
	}

	@Override
	public void update(DictDefine entity) {
		
		super.update(entity);// 修改		
		if (redisEnable.equalsIgnoreCase("true")) {
			dictUtil.updateRedisByDefineId(entity.getDictDefineId());
		}	
	}

	/**
	 * 查询一页信息
	 */

	@Override
	public PageInfo<DictDefine> page(DictDefinePage page) {
		
		DictDefineExample example = new DictDefineExample();
		DictDefineExample.Criteria cr = example.createCriteria();
		
		if (!StringUtil.isEmpty(page.getDictDefineKey())) {
			cr.andDictDefineKeyLike("%" + page.getDictDefineKey() + "%");
		}
		if (!StringUtil.isEmpty(page.getDictDefineName())) {
			cr.andDictDefineNameLike("%" + page.getDictDefineName() + "%");
		}
		
		example.setOrderByClause(page.getOrderByClause());
		PageHelper.startPage(page.getPage(), page.getRows());
		return new PageInfo<DictDefine>(dictDefineMapper.selectByExample(example));
	}

	@Override
	public List<DictDefine> getAll() {
		DictDefineExample defineExample = new DictDefineExample();
		return dictDefineMapper.selectByExample(defineExample);
	}

	/**
	 * 根据选择的id批量删除
	 */
	@Override
	public void removeAll(String defineIds) {
		for(String defineId: defineIds.split(",")){
			if(StringUtils.isNoneEmpty(defineId)){
				delete(Integer.parseInt(defineId));
			}
		}		
	}
}
