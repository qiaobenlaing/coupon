package com.whhft.sysmanage.web.controller;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.whhft.sysmanage.common.rmi.SysUserService;

@Controller
@RequestMapping("/")
public class IndexController {
	@Value("${WhHft.LOGIN_PAGE}") 
	private String loginPage;
	@Resource
	private SysUserService sysUserService;
	
	@RequestMapping("/login")
	public String login() throws Exception{
		return "login";
	}
	
	@RequestMapping("/logout")
	public String logout() throws Exception{
		SecurityUtils.getSubject().logout();
		return "login";
	}
	
	private static Random r = new Random();
	private static char[] chs = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
	private static final int NUMBER_OF_CHS = 4;
	private static final int IMG_WIDTH = 65;
	private static final int IMG_HEIGHT = 25;
	
	
	
	@RequestMapping("/main")
	public String main( @RequestParam String loginname, @RequestParam String password,HttpServletRequest request) throws Exception{
		UsernamePasswordToken token = new UsernamePasswordToken(loginname,password);
		Subject currentUser = SecurityUtils.getSubject();
		String loginFailedInfo = "";
		try {
		    currentUser.login(token);
		} catch ( AuthenticationException ae ) {
			if(ae instanceof UnknownAccountException ){
				loginFailedInfo = "帐号错误！";
			}else if(ae instanceof AuthenticationException ){
				loginFailedInfo = "认证失败！";
			}else if(ae instanceof IncorrectCredentialsException ){
				loginFailedInfo = "证书错误！";
			}else if(ae instanceof LockedAccountException ){
				loginFailedInfo = "帐号被锁定！";
			}else if(ae instanceof ExcessiveAttemptsException ){
				loginFailedInfo = "尝试次数过多，请稍候再试！";
			}else{
				loginFailedInfo = "登录失败！";
			}
			request.setAttribute("loginFailedInfo", loginFailedInfo);
			return "forward:"+loginPage;
		}
		if(!ValidateAuthCode(request)){//对验证码校验
	    	request.setAttribute("loginFailedInfo", "验证码错误");
			return "forward:"+loginPage;
	    }
		request.getSession().setAttribute("currentUser",  sysUserService.getUserByName(loginname));
		return "main";
	}
	
	@RequestMapping("/p/{module}/{page}")
	public String templateUrl(@PathVariable String module, @PathVariable String page) throws Exception{
		return module + "/" + page;
	}
	
	@RequestMapping(value="/include/{page}")
	public String include(@PathVariable String page) throws Exception{
		return "include/" + page;
	}
	
	@RequestMapping("/")
    public String index() {
        return "forward:"+loginPage;
    }
	
	
	
	/**
	 * 获取验证码
	 * @author xiangli
	 */
	@ResponseBody
	@RequestMapping("/getAuthCode")
	public void getAuthCode(HttpServletRequest request,HttpServletResponse response){
		BufferedImage image = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, BufferedImage.TYPE_INT_RGB);
		Graphics g = image.getGraphics();//画图对象
		Color c = new Color(200, 200, 255);//验证码图片的背景颜
		g.setColor(c);//设置图片背景色
		g.fillRect(0, 0, IMG_WIDTH, IMG_HEIGHT);// 图片的边框
		StringBuffer sb = new StringBuffer();// 用于保存验证码字符串
		int index;// 数组的下标
		for (int i = 0; i < NUMBER_OF_CHS; i++) {
			index = r.nextInt(chs.length);// 随机一个下标
			g.setColor(new Color(r.nextInt(88), r.nextInt(210), r.nextInt(150)));// 随机一个颜色
			g.drawString(chs[index] + "", 15 * i + 3, 18);// 画出字符
			sb.append(chs[index]);// 验证码字符串
		}
		request.getSession().setAttribute("piccode", sb.toString());// 将验证码字符串保存到session中
		try {
			ImageIO.write(image, "jpg", response.getOutputStream());// 向页面输出图像
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//校验验证码
	private boolean ValidateAuthCode(HttpServletRequest request){
		boolean flag = true;
		String code = (String)request.getSession().getAttribute("piccode");
		String authCode = request.getParameter("authCode");//得到输入框的验证码值
		if(!authCode.toUpperCase().equals(code.toUpperCase())){//不区分大小写
			flag = false;
		}
		return flag;
	}
	
	
	
}
