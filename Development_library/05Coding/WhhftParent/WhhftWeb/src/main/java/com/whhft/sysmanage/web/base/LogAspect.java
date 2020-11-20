package com.whhft.sysmanage.web.base;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

import com.whhft.sysmanage.common.entity.SysLog;
import com.whhft.sysmanage.common.entity.SysUser;
import com.whhft.sysmanage.common.rmi.SysLogService;
import com.whhft.sysmanage.web.utils.DateUtils;
import com.whhft.sysmanage.web.utils.HtmlUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogAspect {
	
	public LogAspect(){
		actionList = new ArrayList<String>();
		regExpList = new ArrayList<String>();
		paramList = new ArrayList<String>();
		
		actionList.add("main");
		
		regExpList.add("\\S*save\\S*");
		regExpList.add("\\S*insert\\S*");
		regExpList.add("\\S*update\\S*");
		regExpList.add("\\S*remove\\S*");
		regExpList.add("\\S*delete\\S*");
		
		paramList.add("password");
		paramList.add("pwd");
	};
	
	@Autowired
	private SysLogService sysLogService;
	
	//type= 1:黑名单， 2:白名单；目前只记录用户对数据进行改动的操作，对数据的查看没有做记录
	private int actionListType = 2;
	//名单内容（以indexOf匹配）
	private List<String> actionList;
	//名单内容（以正则表达式匹配）
	private List<String> regExpList;
	//参数名称过滤
	private List<String> paramList;

	@Pointcut("execution(* com.whhft..*Controller.*(..))")
	private void doLog() {
		//* com.whhft..web.*.*(..))"
	}

	@Before("doLog()&& args(request,..)")
	public void doBefore(HttpServletRequest request) {
		
	}
	
	/**
	 * 注意拦截的请求最后一个参数必须是request
	 * @param request
	 */
	@After("doLog() && args(request,..)")
	public void doAfter(HttpServletRequest request) {
		String actionUrl = request.getServletPath();
		
		//判断该URL是否需要记入日志		
		boolean needLog = isNeedLog( actionUrl );
		//不用记录就结束
		if(needLog) {
			//写日志，从SESSION中获取当前用户信息，如果该用户没有登录，则ID=-1，用户名=匿名用户
			HttpSession session = ((HttpServletRequest) request).getSession(false);
	    	SysUser currentUser = new SysUser();
			currentUser.setUserId(-1);
	    	if(session != null){
	    		Object user = session.getAttribute("currentUser");
	    		if(user != null){
	    			currentUser = (SysUser)user;
	    		}
	    	}    	
	    	
	    	SysLog log = new SysLog();
	    	//获取请求ID
	    	log.setUserIp(request.getRemoteAddr());
	    	log.setActionUrl(actionUrl);
	    	//当前系统时间
	    	log.setLogTime(DateUtils.getCurrentTime());
	    	//请求发起的用户
	    	log.setUserId(currentUser.getUserId());
	    	
	    	//请求的参数
	    	String queryString ;
	    	if("GET".equalsIgnoreCase(request.getMethod())){
	    		 queryString = request.getQueryString();
	    	}else{
	    		Enumeration<String> paramNames = request.getParameterNames();
	    		List<String> postParams = new ArrayList<String>();
	        	while(paramNames.hasMoreElements()){
	        		String paramName = paramNames.nextElement();
	        		//日志中不要记录密码等敏感信息
	        		
	        		if(isIgnoredParam(paramName.toLowerCase())){
	        			continue;
	        		}
	        		String paramValue = org.apache.commons.lang3.StringUtils.join(request.getParameterValues(paramName), ",");
	        		postParams.add(paramName+"="+StringUtils.substring(paramValue, 0, 64));
	        	}
	        	queryString = org.apache.commons.lang3.StringUtils.join(postParams, "&");
	    	}
	    	//过滤掉参数中可能含有的HTML标签
	    	String paramsText = HtmlUtils.Html2Text(queryString);
	    	log.setActionParams(StringUtils.substring(paramsText,0, 512));
	    	
	    	sysLogService.insert(log);
		}
		
    	
	}
	
	/**
	 * 判断请求地址，是否需要日志记录
	 * @param actionUrl
	 * @return
	 */
	private boolean isNeedLog(String actionUrl ){
		String url = actionUrl.toLowerCase();
		boolean res;
		if(actionListType == 1){
			//当URL与指定项匹配时，不记录到LOG
			res = true;
			if(actionList != null){
				for(String action: actionList){
					if(url.indexOf(action) >= 0){
						res = false;
						break;
					}
				}
			}
			//如果actionList检查该ACION无需记录，则可以直接跳过正则表达式检测
			if( res && regExpList != null ){
				for(String regExp: regExpList){
					if(url.matches(regExp)){
						res = false;
						break;
					}
				}
			}
		}else{
			//仅当URL与指定项匹配时，记录到LOG
			res = false;
			if(actionList != null){
				for(String action: actionList){
					if(url.indexOf(action) >= 0){
						res = true;
						break;
					}
				}
			}
			//如果actionList检查该ACION需要记录，则可以直接跳过正则表达式检测
			if( !res && regExpList != null ){
				for(String regExp: regExpList){
					if(url.matches(regExp)){
						res = true;
						break;
					}
				}
			}
		}
		return res;
	}
	
	private boolean isIgnoredParam(String paramName){
		boolean res =false;
			//当URL与指定项匹配时，不记录到LOG
			if(actionList != null){
				for(String ignoredParamName: paramList){
					if(paramName.indexOf(ignoredParamName) >= 0){
						res = true;
						break;
					}
				}
			}
		return res;
	}
}
