package com.whhft.sysmanage.web.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import com.whhft.sysmanage.common.base.CommonException;
import com.whhft.sysmanage.common.entity.SysUser;
import com.whhft.sysmanage.common.model.SysUserModel;
import com.whhft.sysmanage.common.page.SysUserPage;
import com.whhft.sysmanage.common.rmi.SysUserService;
import com.whhft.sysmanage.web.base.BaseController;
import com.whhft.sysmanage.web.utils.EasyGrid;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

@Controller
@RequestMapping(value="/sysuser")
public class SysUserController extends BaseController{
	@Resource
	private SysUserService sysUserService;
	
	@ResponseBody
	@RequestMapping(value="/page",method=RequestMethod.POST)
	public String find(@ModelAttribute SysUserPage sysUserPage) throws Exception{//@PathVariable
		System.err.println("加载页面");
		return JSON.toJSONString(new EasyGrid<SysUser>(sysUserService.page(sysUserPage)));
	}
	
	@ResponseBody
	@RequestMapping(value="/save",method=RequestMethod.POST)
	public String save(@ModelAttribute SysUserModel model) throws Exception{//@PathVariable
		//主键小于0说明是临时节点，进行新增操作		
		if(model.getUserId() == null || model.getUserId() <= 0){
			System.err.println(model);
			// 对插入的参数进行校验，主要是loginName,userName,校验是否为空
			if(null == model.getLoginName() || "".equals(model.getLoginName()) || null == model.getUserName() || "".equals(model.getUserName())){
				return FAILED;
			}
			sysUserService.insert(model);
		}else{
			sysUserService.update(model);
		}
		return SUNCCESS;
	}
	
	@ResponseBody
	@RequestMapping(value="/load")
	public String load(@RequestParam Integer userId) throws Exception{//@PathVariable
		return JSON.toJSONString(sysUserService.load(userId));
	}
	
	@ResponseBody
	@RequestMapping(value="/removeAll",method=RequestMethod.POST)
	public void removeAll(@RequestParam String userIds) throws Exception{
		sysUserService.removeAll(userIds);
	}
	
	@ResponseBody
	@RequestMapping(value="/loginNameValid",method=RequestMethod.POST)
	public String loginNameValid(@RequestParam String id, @RequestParam String fieldValue) throws Exception{
		Integer userId = null;
		if(!"undefined".equals(id) && StringUtils.isNoneEmpty(id)){
			userId = Integer.parseInt(id);
		}
		return sysUserService.loginNameValid(userId, fieldValue) ? "true": "false";
	}
	
	@ResponseBody
	@RequestMapping(value="/changePassword",method=RequestMethod.POST)
	public String changePassword(HttpServletRequest request, @RequestParam String userPwd, @RequestParam String newPwd) throws CommonException{
		SysUser user = (SysUser)request.getSession().getAttribute("currentUser");
		if(sysUserService.changePassword(user.getUserId(), userPwd, newPwd)){
			return SUNCCESS;
		}else{
			return FAILED;
		}
	}
	
	@ResponseBody
	@RequestMapping(value="/initPassword",method=RequestMethod.POST)
	public String initPassword(@RequestParam String userId) throws CommonException{
		if(sysUserService.initPassword(Integer.parseInt(userId))){
			return SUNCCESS;
		}else{
			return FAILED;
		}
	}
	
}
