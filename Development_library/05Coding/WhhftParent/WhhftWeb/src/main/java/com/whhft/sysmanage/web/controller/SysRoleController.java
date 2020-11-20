package com.whhft.sysmanage.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.whhft.sysmanage.common.entity.SysRole;
import com.whhft.sysmanage.common.rmi.SysRoleService;
import com.whhft.sysmanage.web.base.BaseController;
import com.whhft.sysmanage.web.utils.FastJSONUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

@Controller
@RequestMapping("/sysrole")
public class SysRoleController extends BaseController{
	@Resource
	private SysRoleService sysRoleService;
	
	@ResponseBody
	@RequestMapping("/findAll")
	public String mainMenu(@ModelAttribute SysRole ext) throws Exception{//@PathVariable
		Map<String , String> nameMap = new HashMap<String , String>();
		//主菜单JSON必须是一个数组，即使只有一个元素，也必须返回的是一个数组，否则EASYUI控件解析不正确
		return FastJSONUtil.toJSONString(sysRoleService.findAll(), nameMap);
	}
	
	@ResponseBody
	@RequestMapping("/save")
	public String save(@ModelAttribute SysRole role) throws Exception{//@PathVariable
		//主键小于0说明是临时节点，进行新增操作
		if(role.getRoleId() == null || role.getRoleId() <= 0){
			role.setRoleId(null);
			sysRoleService.insert(role);
		}else{
			sysRoleService.update(role);
		}
		return SUNCCESS;
	}
	
	@ResponseBody
	@RequestMapping("/remove")
	public String remove(@RequestParam Integer roleId) throws Exception{
		sysRoleService.delete(roleId);
		return SUNCCESS;
	}
	
	@ResponseBody
	@RequestMapping("/getRoleAuthIds")
	public String getRoleAuthIds(@RequestParam Integer roleId) throws Exception{
		List<Integer> resList = sysRoleService.getRoleAuthIds(roleId);
		return JSON.toJSONString(resList);
	}
	
	
	
	@ResponseBody
	@RequestMapping("/saveRoleAuthIds")
	public String saveRoleAuthIds(@RequestParam Integer roleId, @RequestParam String authIds) throws Exception{
		sysRoleService.saveRoleAuthIds(roleId, authIds);
		return SUNCCESS;
	}
	
	
}
