package com.rupeng.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.rupeng.pojo.Permission;
import com.rupeng.pojo.RolePermission;
import com.rupeng.service.PermissionService;
import com.rupeng.service.RolePermissionService;
import com.rupeng.util.AjaxResult;
import com.rupeng.util.JsonUtils;

@Controller
@RequestMapping("/rolePermission")
public class RolePermissionController {
	@Autowired
	private RolePermissionService rolePermissionService;
	@Autowired
	private PermissionService permissionService;
	
	
	//操作Permission和Role的多对多关联关系时，一般都是以roleId去操作关联的Permission
	@RequestMapping(value="/update.do",method=RequestMethod.GET)
	public ModelAndView updatePage(Long roleId){
		ModelAndView modelAndView = new ModelAndView("rolePermission/update");
		
		List<Permission> permissions = permissionService.selectList();
		RolePermission rolePermission = new RolePermission();
		rolePermission.setRoleId(roleId);
		List<RolePermission> rolePermissions = rolePermissionService.selectList(rolePermission);
		modelAndView.addObject("permissions", permissions);
		modelAndView.addObject("rolePermissions", JsonUtils.toJson(rolePermissions));
		modelAndView.addObject("roleId",roleId);
		return modelAndView;
	}
	
	//分配权限
	@RequestMapping(value="/update.do",method=RequestMethod.POST)
	public @ResponseBody AjaxResult updateSubmit(Long roleId,Long[] permissionIds){
		rolePermissionService.updateFirst(roleId, permissionIds);
		
		return AjaxResult.successInstance("分配权限成功");
	}
}
