package com.rupeng.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rupeng.pojo.AdminUser;
import com.rupeng.util.CommonUtils;

@Service
public class AdminUserService extends BaseService<AdminUser> {
	@Autowired
	private AdminUserRoleService adminUserRoleService; 
	
	public void insert(AdminUser adminUser, Long[] roleIds) {
		insert(adminUser);
		
		AdminUser param = new AdminUser();
		param.setAccount(adminUser.getAccount());
		adminUser = selectOne(param);//查询出新插入adminUser的id
		
		adminUserRoleService.updateFirst(adminUser.getId(), roleIds);
		
	}

	//登录成功返回adminUser对象,失败返回null
	public AdminUser login(String account, String password) {
		AdminUser adminUser = new AdminUser();
		adminUser.setAccount(account);
		adminUser = selectOne(adminUser);
		if(adminUser!=null){
			if(adminUser.getPassword().equalsIgnoreCase(CommonUtils.calculateMD5(adminUser.getPasswordSalt()+password))){
				return adminUser;
			}
		}
		return null;
	}

}