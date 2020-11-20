package com.whhft.sysmanage.service.mapper;

import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import com.whhft.sysmanage.common.base.BaseMapper;
import com.whhft.sysmanage.common.entity.SysUser;

public interface SysUserMapper extends BaseMapper<SysUser, Integer> {
	@Select("SELECT * FROM sys_user WHERE DEL_FLAG='1' AND LOGIN_NAME = #{loginName}")
    @ResultMap("BaseResultMap")
	SysUser getUserByName(String loginName);
}