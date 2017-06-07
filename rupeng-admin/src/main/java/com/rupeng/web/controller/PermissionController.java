package com.rupeng.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.rupeng.pojo.Permission;
import com.rupeng.service.PermissionService;
import com.rupeng.util.AjaxResult;
import com.rupeng.util.CommonUtils;

@Controller
@RequestMapping("/permission")
public class PermissionController {
	@Autowired
	private PermissionService permissionService;
	
	@RequestMapping("/list.do")
	public ModelAndView list(){
		List<Permission> permissions = permissionService.selectList();
		return new ModelAndView("permission/list", "permissions", permissions);
	}
	
	@RequestMapping(value="/add.do",method=RequestMethod.GET)
	public ModelAndView addPage(){
		return new ModelAndView("permission/add");
	}
	
	@RequestMapping(value="/add.do",method=RequestMethod.POST)
	public @ResponseBody AjaxResult addSubmit(String path,String description){
		//数据有效性验证
		if (CommonUtils.isEmpty(path)) {
			return AjaxResult.errorInstance("请求路径不能为空");
		}
		
		//path唯一性检查
		Permission permission = new Permission();
		permission.setPath(path);
		if (permissionService.isExisted(permission)) {
			return AjaxResult.errorInstance("请求路径已经存在");
		}
		
		permission.setDescription(description);
		//新增权限项
		permissionService.insert(permission);
		return AjaxResult.successInstance("添加权限成功");
	}
	
	@RequestMapping(value="/update.do",method=RequestMethod.GET)
	public ModelAndView updatePage(long id){
		Permission permission = permissionService.selectOne(id);
		return new ModelAndView("permission/update", "permission", permission);
	}

	@RequestMapping(value="/update.do",method=RequestMethod.POST)
	public @ResponseBody AjaxResult updateSubmit(long id,String path,String description){
		//数据有效性验证
		if (CommonUtils.isEmpty(path)) {
			return AjaxResult.errorInstance("请求路径不能为空");
		}
		
		//path唯一性检查:path不能修改为其他已经存在的path
		Permission permission = new Permission();
		permission.setPath(path);
		permission = permissionService.selectOne(permission);
		if (permission!=null&&permission.getId()!=id) {
			return AjaxResult.errorInstance("请求路径已经存在");
		}
		
		//执行业务逻辑,避免更改可能不能更改的字段,先从数据库查询出就数据再把需要更改的字段更新
		permission = permissionService.selectOne(id);
		permission.setPath(path);
		permission.setDescription(description);
		permissionService.update(permission);
		
		return AjaxResult.successInstance("修改权限成功");
	}
	
	@RequestMapping("/delete.do")
	public @ResponseBody AjaxResult delete(long id){
		permissionService.delete(id);
		return AjaxResult.successInstance("删除成功");
	}
}
