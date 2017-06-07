package com.rupeng.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.rupeng.pojo.AdminUser;
import com.rupeng.util.ImageCodeUtils;

@Controller
public class OtherController {
	
	@RequestMapping("/")
	public ModelAndView index(HttpServletRequest request){
		//从session中获取用户登录状态数据
		AdminUser adminUser = (AdminUser) request.getSession().getAttribute("adminUser");
		if(adminUser==null){
			return new ModelAndView("redirect:/adminUser/login.do");
		}
		
		return new ModelAndView("index");
	}
	
	@RequestMapping("/welcome.do")
	public ModelAndView welcome(){
		return new ModelAndView("welcome");
	}
	
	//生成验证码
	@RequestMapping("/imageCode.do")
	public void imageCode(HttpServletRequest request,HttpServletResponse response){
		ImageCodeUtils.sendImageCode(request.getSession(), response);
	}
}
