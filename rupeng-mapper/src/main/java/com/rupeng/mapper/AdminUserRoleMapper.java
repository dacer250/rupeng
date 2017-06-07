package com.rupeng.mapper;

import java.util.Map;

import com.rupeng.pojo.AdminUser;
import com.rupeng.pojo.AdminUserRole;
import com.rupeng.pojo.Role;

public interface AdminUserRoleMapper extends IManyToManyMapper<AdminUserRole, AdminUser, Role> {

	int checkPermission(Map<String, Object> map);

}
