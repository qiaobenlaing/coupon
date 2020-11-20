package com.whhft.sysmanage.common.rmi;

import java.util.List;

import com.whhft.sysmanage.common.base.BaseService;
import com.whhft.sysmanage.common.entity.SysAuth;
import com.whhft.sysmanage.common.model.SysAuthExt;

public interface SysAuthService extends BaseService <SysAuth, Integer> {
	List<SysAuthExt> loadMainMenu(Integer type, Integer userId);
	List<SysAuth> getAuthsByUserId(Integer userId);
	List<SysAuth> selectAll();
}