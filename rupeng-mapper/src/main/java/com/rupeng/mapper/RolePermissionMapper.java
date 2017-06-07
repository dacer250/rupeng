package com.rupeng.mapper;

import com.rupeng.pojo.Permission;
import com.rupeng.pojo.Role;
import com.rupeng.pojo.RolePermission;

public interface RolePermissionMapper extends IManyToManyMapper<RolePermission, Role, Permission> {

}
