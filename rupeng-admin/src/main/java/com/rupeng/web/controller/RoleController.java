package com.rupeng.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.rupeng.pojo.Role;
import com.rupeng.service.PermissionService;
import com.rupeng.service.RoleService;
import com.rupeng.util.AjaxResult;
import com.rupeng.util.CommonUtils;

@Controller
@RequestMapping("/role")
public class RoleController {
	@Autowired
	private RoleService roleService;
	@Autowired
	private PermissionService permissionService;
	
	@RequestMapping("/list.do")
	public ModelAndView list(){
		return new ModelAndView("role/list", "roles", roleService.selectList());
	}
	
	@RequestMapping(value="/add.do",method=RequestMethod.GET)
	public ModelAndView addPage(){
		return new ModelAndView("role/add","permissions",permissionService.selectList());
	}
	
	@RequestMapping(value="/add.do",method=RequestMethod.POST)
	public @ResponseBody AjaxResult addSubmit(Long[] permissionIds,Role role){
		if(CommonUtils.isEmpty(role.getName())){
			return AjaxResult.errorInstance("角色名不能为空");
		}
		Role param = new Role();//创建专用于检查name唯一性的role对象
		param.setName(role.getName());
		if(roleService.isExisted(param)){
			return AjaxResult.errorInstance("角色名已经存在");
		}
		
		roleService.insert(role,permissionIds);//使用事务添加role及rolepermission数据
		
		return AjaxResult.successInstance("添加角色成功");
	}
	
	@RequestMapping(value="/update.do",method=RequestMethod.GET)
	public ModelAndView updatePage(Long id){
		return new ModelAndView("role/update", "role", roleService.selectOne(id));
	}
	
	@RequestMapping(value="/update.do",method=RequestMethod.POST)
	public @ResponseBody AjaxResult updateSubmit(Role role){
		if(CommonUtils.isEmpty(role.getName())){
			return AjaxResult.errorInstance("角色名不能为空");
		}
		
		Role param = new Role();//创建专用于检查name唯一性的role对象
		param.setName(role.getName());
		param = roleService.selectOne(param);
		if(param!=null&&param.getId()!=role.getId()){//查询出其他已经存在的name
			return AjaxResult.errorInstance("角色名已经存在");
		}
		
		//如果存在不可以修改的字段就先使用id查询出旧数据,再把可以修改的字段设置好后进行update
		//这里没有不能修改的字段,所以直接update
		roleService.update(role);
		return AjaxResult.successInstance("修改成功");
	}
	
	@RequestMapping("/delete.do")
	public @ResponseBody AjaxResult delete(Long id){
		roleService.delete(id);
		return AjaxResult.successInstance("删除角色成功");
	}
	
}
