package com.rupeng.web.controller;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.rupeng.pojo.AdminUser;
import com.rupeng.service.AdminUserService;
import com.rupeng.service.RoleService;
import com.rupeng.util.AjaxResult;
import com.rupeng.util.CommonUtils;
import com.rupeng.util.ImageCodeUtils;
import com.rupeng.util.JedisUtils;

@Controller
@RequestMapping("/adminUser")
public class AdminUserController {
	@Autowired
	private AdminUserService adminUserService;
	@Autowired
	private RoleService roleService;
	
	@RequestMapping("/list.do")
	public ModelAndView list(){
		return new ModelAndView("adminUser/list","adminUsers",adminUserService.selectList());
	}
	
	@RequestMapping(value="/add.do",method=RequestMethod.GET)
	public ModelAndView addPage(){
		return new ModelAndView("adminUser/add","roles",roleService.selectList());
	}
	
	@RequestMapping(value="/add.do",method=RequestMethod.POST)
	public @ResponseBody AjaxResult addSubmit(String account,String password,Long[] roleIds){
		//数据验证
		if(CommonUtils.isEmpty(account)||CommonUtils.isEmpty(account)){
			return AjaxResult.errorInstance("账号或密码不能为空");
		}
		if(!CommonUtils.isLengthEnough(password, 6)){
			return AjaxResult.errorInstance("密码长度不能小于6位");
		}
		
		AdminUser adminUser = new AdminUser();
		adminUser.setAccount(account);
		
		//账号唯一性验证
		if(adminUserService.isExisted(adminUser)){
			return AjaxResult.errorInstance("账号已存在");
		}
		
		//添加管理员
		String salt = UUID.randomUUID().toString();
		String passwordMD5 = CommonUtils.calculateMD5(salt+password);
		
		adminUser.setPasswordSalt(salt);
		adminUser.setPassword(passwordMD5);
		
		adminUserService.insert(adminUser,roleIds);//事务
		
		return AjaxResult.successInstance("添加管理员成功");
	}
	
	@RequestMapping("/delete.do")
	public @ResponseBody AjaxResult delete(Long id){
		adminUserService.delete(id);
		return AjaxResult.successInstance("删除管理员成功");
	}
	
	//重置密码
	@RequestMapping(value="/resetPassword.do",method=RequestMethod.GET)
	public ModelAndView resetPasswordPage(Long id){
		return new ModelAndView("adminUser/resetPassword","adminUser",adminUserService.selectOne(id));
	}
	
	@RequestMapping(value="/resetPassword.do",method=RequestMethod.POST)
	public @ResponseBody AjaxResult resetPasswordSubmit(Long id,String password){
		//数据验证
		if(CommonUtils.isEmpty(password)){
			return AjaxResult.errorInstance("密码不能为空");
		}
		if(!CommonUtils.isLengthEnough(password, 6)){
			return AjaxResult.errorInstance("密码长度不能小于6位");
		}
		
		String salt = UUID.randomUUID().toString();
		String passwordMD5 = CommonUtils.calculateMD5(salt+password);
		AdminUser adminUser = adminUserService.selectOne(id);
		adminUser.setPasswordSalt(salt);
		adminUser.setPassword(passwordMD5);
		
		adminUserService.update(adminUser);
		
		return AjaxResult.successInstance("密码重置成功");
	}
	
	//管理员账号禁用,启用
	@RequestMapping("/toggleDisable.do")
	public @ResponseBody AjaxResult toggleDisable(Long id){
		AdminUser adminUser = adminUserService.selectOne(id);
		adminUser.setIsDisabled(!adminUser.getIsDisabled());
		adminUserService.update(adminUser);
		return AjaxResult.successInstance(adminUser.getIsDisabled()?"已禁用":"已启用");
	}
	
	//管理员登录
	@RequestMapping(value="/login.do",method=RequestMethod.GET)
	public ModelAndView loginPage(){
		return new ModelAndView("adminUser/login");
	}
	
	@RequestMapping(value="/login.do",method=RequestMethod.POST)
	public ModelAndView loginSubmit(String account,String password,String imageCode,HttpServletRequest request){
		ModelAndView modelAndView = new ModelAndView();
		
		//验证码校验
		if(!ImageCodeUtils.checkImageCode(request.getSession(), imageCode)){
			modelAndView.addObject("message", "验证码错误");
			return modelAndView;
		}
		
		//检查帐号密码
		AdminUser adminUser = adminUserService.login(account,password);
		if(adminUser==null){
			modelAndView.addObject("message", "用户名或密码错误");
			return modelAndView;
		}
		
		//检查账号是否被禁用
        if (adminUser.getIsDisabled()) {
        	modelAndView.addObject("message", "此账号已被禁用");
			return modelAndView;
        }
		
        //设置session
		request.getSession().setAttribute("adminUser", adminUser);
		
		//重定向到首页
		modelAndView.setViewName("redirect:/");
		return modelAndView;
	}
	
	//管理员退出登录
	@RequestMapping("/logout.do")
	public ModelAndView logout(HttpServletRequest request){
		//把保持在线所用的redis数据删掉
		JedisUtils.del("keepOnline_" + request.getSession().getId());
		
		request.getSession().invalidate();//让当前session失效
		return new ModelAndView("redirect:/adminUser/login.do");
	}
	
	//用户首页-修改密码
	@RequestMapping(value="/updatePassword.do",method=RequestMethod.GET)
	public ModelAndView updatePassword(){
		return new ModelAndView("adminUser/updatePassword");
	}
	
	@RequestMapping(value="/updatePassword.do",method=RequestMethod.POST)
	public @ResponseBody AjaxResult updatePassword(String password,String newpassword,String renewpassword,HttpServletRequest request){
		if (newpassword == null || newpassword.length() < 6) {
            return AjaxResult.errorInstance("新密码长度至少6位！");
        }
        if (!newpassword.equals(renewpassword)) {
            return AjaxResult.errorInstance("两次输入密码不一致！");
        }
		
		AdminUser adminUser = (AdminUser) request.getSession().getAttribute("adminUser");
		if(!adminUser.getPassword().equalsIgnoreCase(CommonUtils.calculateMD5(adminUser.getPasswordSalt()+password))){
			return AjaxResult.errorInstance("原密码错误");
		}
		adminUser.setPassword(CommonUtils.calculateMD5(adminUser.getPasswordSalt()+newpassword));
		adminUserService.update(adminUser);
		
		//更新成功后两种处理方案:一是直接更新session中的adminUser,二是重新登录
		request.getSession().setAttribute("adminUser", adminUser);
		
		return AjaxResult.successInstance("密码修改成功");
	}
}
