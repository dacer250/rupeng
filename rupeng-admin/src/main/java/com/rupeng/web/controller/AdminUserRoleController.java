package com.rupeng.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.rupeng.pojo.AdminUserRole;
import com.rupeng.pojo.Role;
import com.rupeng.service.AdminUserRoleService;
import com.rupeng.service.RoleService;
import com.rupeng.util.AjaxResult;

@Controller
@RequestMapping("/adminUserRole")
public class AdminUserRoleController {
	@Autowired
	private AdminUserRoleService adminUserRoleService;
	@Autowired
	private RoleService roleService;
	
	//分配角色
	@RequestMapping(value="/update.do",method=RequestMethod.GET)
	public ModelAndView updatePage(Long adminUserId){
		ModelAndView modelAndView = new ModelAndView("adminUserRole/update");
		AdminUserRole adminUserRole = new AdminUserRole();
		adminUserRole.setAdminUserId(adminUserId);
		List<AdminUserRole> adminUserRoles = adminUserRoleService.selectList(adminUserRole);
		List<Role> roles = roleService.selectList();
		modelAndView.addObject("adminUserId", adminUserId);
		modelAndView.addObject("adminUserRoles",adminUserRoles);
		modelAndView.addObject("roles",roles);
		return modelAndView;
	}
	
	@RequestMapping(value="/update.do",method=RequestMethod.POST)
	public @ResponseBody AjaxResult updateSubmit(Long adminUserId,Long[] roleIds){
		adminUserRoleService.updateFirst(adminUserId, roleIds);
		
		return AjaxResult.successInstance("分配角色成功");
	}
}
