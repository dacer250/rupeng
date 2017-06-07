package com.rupeng.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rupeng.pojo.Role;

@Service
public class RoleService extends BaseService<Role> {

	@Autowired
	private RolePermissionService rolePermissionService;
	
	public void insert(Role role, Long[] permissionIds) {
		insert(role);//此时role没有id
		
		Role param = new Role();//创建用于只使用name查询role的参数对象(适用于role字段较多时)
		param.setName(role.getName());
		role = selectOne(param);//查询出带有id的role
		
		rolePermissionService.updateFirst(role.getId(), permissionIds);
	}

}
