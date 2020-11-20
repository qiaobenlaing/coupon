package com.whhft.sysmanage.web.shiro;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import com.whhft.sysmanage.common.base.ShiroHelper;
import com.whhft.sysmanage.common.entity.SysAuth;
import com.whhft.sysmanage.common.entity.SysUser;
import com.whhft.sysmanage.common.rmi.SysAuthService;
import com.whhft.sysmanage.common.rmi.SysUserService;

import org.springframework.stereotype.Component;

@Component("shiroRealm")
public class ShiroRealm extends AuthorizingRealm{
	
	Log log = LogFactory.getLog(ShiroRealm.class);
	
	public final static  String PERMISSION_PREFIX = "PERMISSION_";

	@Resource
	private SysUserService sysUserService;
	
	@Resource
	private SysAuthService sysAuthService;
	

	public void setSysUserService(SysUserService sysUserService) {
		this.sysUserService = sysUserService;
	}
	
	public void setSysAuthService(SysAuthService sysAuthService) {
		this.sysAuthService = sysAuthService;
	}

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		String loginName = (String) principalCollection.fromRealm(getName()).iterator().next();
		SysUser user = sysUserService.getUserByName(loginName);
		List<SysAuth> authList = null;
		if(user.getUserId() == 1){
			authList = sysAuthService.selectAll();
		}else{
			authList = sysAuthService.getAuthsByUserId(user.getUserId());
		}
		for(SysAuth auth: authList){
			System.out.println("PERMISSION:" + PERMISSION_PREFIX+auth.getAuthId());
			info.addStringPermission(PERMISSION_PREFIX+auth.getAuthId());
		}
		//info.addRole("authc");
		return info;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
		UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
		String loginName = token.getUsername();
		SysUser user = sysUserService.getUserByName(loginName);
		if (user != null && user.getEnabled() != null) {
			return new SimpleAuthenticationInfo(
					loginName, user.getUserPwd(), ShiroHelper.getSalt(), getName()
			);
		} else {
			return null;
		}
	}
	
	
	
}
