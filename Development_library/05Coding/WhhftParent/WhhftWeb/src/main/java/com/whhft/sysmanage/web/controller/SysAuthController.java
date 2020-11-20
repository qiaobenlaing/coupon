package com.whhft.sysmanage.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.whhft.sysmanage.common.entity.SysAuth;
import com.whhft.sysmanage.common.entity.SysUser;
import com.whhft.sysmanage.common.rmi.SysAuthService;
import com.whhft.sysmanage.web.base.BaseController;
import com.whhft.sysmanage.web.model.SysAuthModel;
import com.whhft.sysmanage.web.utils.FastJSONUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value="/sysauth",method=RequestMethod.POST)
public class SysAuthController extends BaseController{
	@Resource
	private SysAuthService sysAuthService;
	
	@ResponseBody
	@RequestMapping(value="/userMainMenu",method=RequestMethod.POST)
	public String userMainMenu(HttpSession session) throws Exception{
		SysUser user = (SysUser)session.getAttribute("currentUser");
		//对于admin帐号，进行特殊处理，允许该用户获取所有菜单项
		if(user.getUserId() == 1){
			return mainMenu(1, user.getUserId());
		}else{
			return mainMenu(3, user.getUserId());
		}
		
	}
	
	@ResponseBody
	@RequestMapping(value="/authEditorTree",method=RequestMethod.POST)
	public String authEditorTree() throws Exception{
		return mainMenu(1, null);
	}
	
	@ResponseBody
	@RequestMapping(value="/roleAuthTree",method=RequestMethod.POST)
	public String roleAuthTree() throws Exception{
		return mainMenu(2, null);
	}
	
	@ResponseBody
	@RequestMapping(value="/save",method=RequestMethod.POST)
	public String save(HttpServletRequest request, @ModelAttribute SysAuthModel model) throws Exception{
		SysAuth auth = model.getSysAuth();
		//主键小于0说明是临时节点，进行新增操作
		if(auth.getAuthId() == null || auth.getAuthId() <= 0){
			auth.setAuthId(null);
			sysAuthService.insert(auth);
		}else{
			sysAuthService.update(auth);
		}
		return SUNCCESS;
	}
	
	@ResponseBody
	@RequestMapping(value="/remove",method=RequestMethod.POST)
	public String remove(HttpServletRequest request, @RequestParam Integer authId) throws Exception{
		sysAuthService.delete(authId);
		return SUNCCESS;
	}

	private String mainMenu(Integer type, Integer userId) throws Exception{//@PathVariable
		Map<String , String> nameMap = new HashMap<String , String>();
		nameMap.put("authId", "id");
		nameMap.put("authName", "text");
		nameMap.put("iconStyle", "iconCls");
		System.err.println(nameMap); 
		//主菜单JSON必须是一个数组，即使只有一个元素，也必须返回的是一个数组，否则EASYUI控件解析不正确
		return FastJSONUtil.toJSONString(sysAuthService.loadMainMenu(type, userId), nameMap);
	}
}
