package com.whhft.sysmanage.service.rmi;

import java.util.ArrayList;
import java.util.List;

import com.whhft.sysmanage.common.base.BaseMapper;
import com.whhft.sysmanage.common.base.BaseService;
import com.whhft.sysmanage.common.base.BaseServiceImpl;
import com.whhft.sysmanage.common.entity.RoleAuthRs;
import com.whhft.sysmanage.common.entity.RoleAuthRsExample;
import com.whhft.sysmanage.common.entity.SysRole;
import com.whhft.sysmanage.common.entity.SysRoleExample;
import com.whhft.sysmanage.common.entity.UserRoleRsExample;
import com.whhft.sysmanage.common.rmi.SysRoleService;
import com.whhft.sysmanage.service.mapper.RoleAuthRsMapper;
import com.whhft.sysmanage.service.mapper.SysRoleMapper;
import com.whhft.sysmanage.service.mapper.UserRoleRsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("sysRoleServiceImpl")
@Transactional
public class SysRoleServiceImpl extends BaseServiceImpl<SysRole, Integer> implements SysRoleService{
	
	@Autowired
	private SysRoleMapper sysRoleMapper;
	@Autowired
	private RoleAuthRsMapper roleAuthRsMapper;
	@Autowired
	private UserRoleRsMapper userRoleRsMapper;
	
	public BaseMapper<SysRole, Integer> mapper(){
		return sysRoleMapper;
	}
	@Override
	public List<SysRole> findAll(){
		SysRoleExample example = new SysRoleExample();
		return sysRoleMapper.selectByExample(example);
	}
	@Override
	public List<Integer> getRoleAuthIds(Integer roleId){
		RoleAuthRsExample example = new RoleAuthRsExample();
		example.createCriteria().andRoleIdEqualTo(roleId);
		List<Integer> resList = new ArrayList<Integer>();
		for(RoleAuthRs rs: roleAuthRsMapper.selectByExample(example)){
			resList.add(rs.getAuthId());
		}
		return resList;
	}
	
	@Override
	public void saveRoleAuthIds(Integer roleId, String authIds){
		RoleAuthRsExample example = new RoleAuthRsExample();
		example.createCriteria().andRoleIdEqualTo(roleId);
		roleAuthRsMapper.deleteByExample(example);
		
		for(String authId: authIds.split(",")){
			RoleAuthRs rs = new RoleAuthRs();
			rs.setAuthId(Integer.parseInt(authId));
			rs.setRoleId(roleId);
			roleAuthRsMapper.insert(rs);
		}
	}
	@Override
	public void delete(Integer id) {
		RoleAuthRsExample example = new RoleAuthRsExample();
		example.createCriteria().andRoleIdEqualTo(id);
		roleAuthRsMapper.deleteByExample(example);
		
		UserRoleRsExample urRsExample = new UserRoleRsExample();
		urRsExample.createCriteria().andRoleIdEqualTo(id);
		userRoleRsMapper.deleteByExample(example);
		
		super.delete(id);
	}
}
