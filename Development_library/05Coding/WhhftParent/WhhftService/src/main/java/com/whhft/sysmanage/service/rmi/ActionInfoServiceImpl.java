package com.whhft.sysmanage.service.rmi;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.whhft.sysmanage.common.base.BaseMapper;
import com.whhft.sysmanage.common.base.BaseServiceImpl;
import com.whhft.sysmanage.common.entity.ActionInfo;
import com.whhft.sysmanage.common.page.ActionInfoPage;
import com.whhft.sysmanage.common.rmi.ActionInfoService;
import com.whhft.sysmanage.service.mapper.ActionInfoMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service("actionInfoServiceImpl")
@Transactional
public class ActionInfoServiceImpl  extends BaseServiceImpl<ActionInfo, Integer> 
		implements ActionInfoService {

	@Autowired
	private ActionInfoMapper actionInfoMapper;

	@Override
	public BaseMapper<ActionInfo, Integer> mapper() {
		return actionInfoMapper;
	}

	/**
	 * 分页查询，可根据请求名称模糊查询
	 */
	@Override
	public PageInfo<ActionInfo> page(ActionInfoPage page) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("actionName", page.getActionName());
		params.put("actionUrl", page.getActionUrl());
		PageHelper.startPage(page.getPage(), page.getRows());
		return new PageInfo<ActionInfo>(actionInfoMapper.find(params));
	}

	/**
	 * 根据选择的id批量删除
	 */
	@Override
	public void removeAll(String actionIds) {
		for(String actionId: actionIds.split(",")){
			if(StringUtils.isNoneEmpty(actionId)){
				delete(Integer.parseInt(actionId));
			}
		}
		
	}

}
