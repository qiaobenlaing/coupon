package com.whhft.sysmanage.service.rmi;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.whhft.sysmanage.common.base.BaseMapper;
import com.whhft.sysmanage.common.base.BaseService;
import com.whhft.sysmanage.common.base.BaseServiceImpl;
import com.whhft.sysmanage.common.entity.RoleAuthRs;
import com.whhft.sysmanage.common.entity.RoleAuthRsExample;
import com.whhft.sysmanage.common.entity.SysAuth;
import com.whhft.sysmanage.common.entity.SysAuthExample;
import com.whhft.sysmanage.common.model.SysAuthExt;
import com.whhft.sysmanage.common.rmi.SysAuthService;
import com.whhft.sysmanage.service.mapper.RoleAuthRsMapper;
import com.whhft.sysmanage.service.mapper.SysAuthMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("sysAuthServiceImpl")
@Transactional
public class SysAuthServiceImpl extends BaseServiceImpl<SysAuth, Integer> implements SysAuthService{
	
	@Autowired
	private SysAuthMapper sysAuthMapper;
	
	@Autowired
	private RoleAuthRsMapper authRsMapper;
	
	public BaseMapper<SysAuth, Integer> mapper(){
		return sysAuthMapper;
	}

	/*
	 * 读取菜单树，根据类型的不同，分为不同的处理情况
	 * type=1，代表需要读取完整菜单树，此时处于菜单树节点的编辑功能中，或者是admin用户登录
	 * （超级管理员默认可以查看所有功能，甚至是被禁用功能，防止操作员禁用屌权限管理功能导致所有人都无法操作菜单）
	 * type=2，表示读取所有可用的菜单节点，用于定义角色菜单
	 * type=3，用于读取某个系统操作员的个人权限菜单
	 * type=1或者2的时候，userId无意义，所以可以为空
	 */
	@Override
	public List<SysAuthExt> loadMainMenu(Integer type, Integer userId){
		List<SysAuthExt> resList = new ArrayList<SysAuthExt>();
		SysAuthExample sample = new SysAuthExample();
		SysAuthExample.Criteria cr = sample.createCriteria();
		if(type == 2 || type ==3){
			cr.andEnabledEqualTo("1");
		}
		sample.setOrderByClause("ORDER_NO DESC");
		List<Integer> authIdList = new ArrayList<Integer>();
		if(type == 3){
			for(SysAuth auth: sysAuthMapper.getAuthsByUserId(userId)){
				authIdList.add(auth.getAuthId());
			}
		}
		//普通用户只能访问被启用的菜单，管理员可以访问所有菜单，即使该菜单的数据配置有问题
		Map<Integer, SysAuthExt> sysAuthModelMap = new LinkedHashMap<Integer, SysAuthExt>();
		for(SysAuth auth: sysAuthMapper.selectByExample(sample)){
			if(type == 3 && !authIdList.contains(auth.getAuthId()) ){
				continue;
			}
			sysAuthModelMap.put(auth.getAuthId(), new SysAuthExt(auth));
		}
		for(Integer authId: sysAuthModelMap.keySet()){
			SysAuthExt auth = sysAuthModelMap.get(authId);
			Integer parentAuthId = sysAuthModelMap.get(authId).getParentAuthId();
			////根节点固定ID为1，父节点ID为NULL,如果不是根节点就可以组装树了
			if(parentAuthId != null){
				SysAuthExt ext = sysAuthModelMap.get(parentAuthId);
				if(ext != null){
					ext.getChildren().add(auth);
				}
				
			}
		}
		
		for(SysAuthExt authExt: sysAuthModelMap.values()){
			if(authExt.getParentAuthId() != null && authExt.getParentAuthId() == 1){
				resList.add(authExt);
			}
		}
		return resList;
	}

	@Override
	public List<SysAuth> getAuthsByUserId(Integer userId){
		return sysAuthMapper.getAuthsByUserId(userId);
	}
	
	@Override
	public List<SysAuth> selectAll(){
		return sysAuthMapper.selectByExample(new SysAuthExample());
	}
	
	@Override
	public void delete(Integer authId){
		RoleAuthRsExample example=new RoleAuthRsExample();
		example.createCriteria().andAuthIdEqualTo(authId);
		authRsMapper.deleteByExample(example);
		sysAuthMapper.deleteByPrimaryKey(authId);
	}
}
