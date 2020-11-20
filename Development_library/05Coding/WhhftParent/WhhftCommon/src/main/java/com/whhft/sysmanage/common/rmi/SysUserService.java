package com.whhft.sysmanage.common.rmi;

import java.util.List;

import com.whhft.sysmanage.common.base.BaseService;
import com.whhft.sysmanage.common.entity.SysUser;
import com.whhft.sysmanage.common.model.SysUserModel;
import com.whhft.sysmanage.common.page.SysUserPage;
import com.github.pagehelper.PageInfo;

public interface SysUserService extends BaseService <SysUser, Integer>{
	PageInfo<SysUser> page(SysUserPage page);
	boolean loginNameValid(Integer userId, String loginName);
	List<Integer> getUserRoleIds(Integer userId);
	SysUserModel insert(SysUserModel model);
	void update(SysUserModel model);
	SysUserModel load(Integer userId);
	void removeAll(String userIds);
	SysUser getUserByName(String userName);
	boolean changePassword(Integer userId, String userPwd, String newPwd);
	boolean initPassword(Integer userId);
}