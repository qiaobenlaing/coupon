package com.whhft.sysmanage.common.rmi;

import java.util.List;

import com.whhft.sysmanage.common.base.BaseService;
import com.whhft.sysmanage.common.entity.SysRole;

public interface SysRoleService extends BaseService <SysRole, Integer>{
	List<SysRole> findAll();
	List<Integer> getRoleAuthIds(Integer roleId);
	void saveRoleAuthIds(Integer roleId, String authIds);
}