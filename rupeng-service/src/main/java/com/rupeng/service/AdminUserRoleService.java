package com.rupeng.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rupeng.mapper.AdminUserRoleMapper;
import com.rupeng.pojo.AdminUser;
import com.rupeng.pojo.AdminUserRole;
import com.rupeng.pojo.Role;

@Service
public class AdminUserRoleService extends ManyToManyBaseService<AdminUserRole, AdminUser, Role> {

	@Autowired
	private AdminUserRoleMapper adminUserRoleMapper;
	
	public boolean checkPermission(Map<String, Object> map) {//权限检查
		Integer count = adminUserRoleMapper.checkPermission(map);
		return count>0;
	}

}
