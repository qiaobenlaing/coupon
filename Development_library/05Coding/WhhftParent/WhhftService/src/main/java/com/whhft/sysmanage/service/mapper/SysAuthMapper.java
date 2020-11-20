package com.whhft.sysmanage.service.mapper;

import java.util.List;

import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import com.whhft.sysmanage.common.base.BaseMapper;
import com.whhft.sysmanage.common.entity.SysAuth;

public interface SysAuthMapper extends BaseMapper<SysAuth, Integer> {
	@Select("select a.* from sys_user u left join user_role_rs ur on u.user_id = ur.user_id left join role_auth_rs ra on ur.role_id = ra.role_id left join sys_auth a on ra.auth_id = a.auth_id where u.user_id = #{userId}")
    @ResultMap("BaseResultMap")
	List<SysAuth> getAuthsByUserId(Integer userId);
}