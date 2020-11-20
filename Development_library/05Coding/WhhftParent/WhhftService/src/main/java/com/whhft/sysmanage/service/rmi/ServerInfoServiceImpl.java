package com.whhft.sysmanage.service.rmi;

import com.whhft.sysmanage.common.base.BaseMapper;
import com.whhft.sysmanage.common.base.BaseService;
import com.whhft.sysmanage.common.base.BaseServiceImpl;
import com.whhft.sysmanage.common.entity.ServerInfo;
import com.whhft.sysmanage.common.entity.ServerInfoExample;
import com.whhft.sysmanage.common.model.ServerInfoExt;
import com.whhft.sysmanage.common.page.ServerInfoPage;
import com.whhft.sysmanage.common.rmi.ServerInfoService;
import com.whhft.sysmanage.service.mapper.ServerInfoMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service("serverInfoServiceImpl")
public class ServerInfoServiceImpl extends BaseServiceImpl<ServerInfo, Integer> implements ServerInfoService {
	@Autowired
	private ServerInfoMapper serverInfoMapper;

	@Override
	public Integer insert(ServerInfo entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(ServerInfo entity) {
		// TODO Auto-generated method stub

	}

	@Override
	public ServerInfo findOne(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(Integer id) {
		// TODO Auto-generated method stub

	}

	@Override
	public PageInfo<ServerInfo> page(ServerInfoPage page) {
		ServerInfoExample example = new ServerInfoExample();
		// 获得页数行数
		PageHelper.startPage(page.getPage(), page.getRows());
		return new PageInfo<ServerInfo>(serverInfoMapper.selectByExample(example));
	}

	@Override
	public BaseMapper<ServerInfo, Integer> mapper() {
		return serverInfoMapper;
	}

}
