package com.whhft.sysmanage.web.shiro;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;

import com.whhft.sysmanage.common.entity.SysAuth;
import com.whhft.sysmanage.common.rmi.SysAuthService;

public class ShiroFilterFactoryExtBean extends ShiroFilterFactoryBean{
	
	Log log = LogFactory.getLog(ShiroFilterFactoryExtBean.class);
	
	private String filterChainDefinitionsHead;
	
	private String filterChainDefinitionsEnd;
	
	@Resource
	private SysAuthService sysAuthService;
	
	public void setSysAuthService(SysAuthService sysAuthService) {
		this.sysAuthService = sysAuthService;
	}
	
	public void init(){
		StringBuffer s = new StringBuffer();
		
		for(SysAuth auth: sysAuthService.selectAll()){
			String urls = auth.getResources();
			if(urls != null){
				for(String url : urls.split(",")){
					url = url.trim();
					if(url.length() > 0){
						s.append(url).append("=").append("authc, perms[\""+ShiroRealm.PERMISSION_PREFIX+auth.getAuthId()+"\"]").append(System.getProperty("line.separator"));
					}
				}
			}
		}
		log.warn(s.toString());
		String filterChainDefinitions = filterChainDefinitionsHead + s.toString() + filterChainDefinitionsEnd;
		super.setFilterChainDefinitions(filterChainDefinitions);
	}

	public void setFilterChainDefinitionsHead(String filterChainDefinitionsHead) {
		this.filterChainDefinitionsHead = filterChainDefinitionsHead;
	}

	public void setFilterChainDefinitionsEnd(String filterChainDefinitionsEnd) {
		this.filterChainDefinitionsEnd = filterChainDefinitionsEnd;
	}

	//屏蔽父类的setsetFilterChainDefinitions方法
	private final void setsetFilterChainDefinitions(String s){
		
	}
}
