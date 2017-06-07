package com.rupeng.web.interception;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.rupeng.pojo.AdminUser;
import com.rupeng.service.AdminUserRoleService;
import com.rupeng.util.AjaxResult;
import com.rupeng.util.JsonUtils;

public class PermissionInterceptor extends HandlerInterceptorAdapter {
	@Autowired
	private AdminUserRoleService adminUserRoleService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		AdminUser adminUser = (AdminUser) request.getSession().getAttribute("adminUser");
		if (adminUser==null) {
			//用户未登录  判断是普通请求还是ajax请求返回不同形式的响应,而不能直接重定向到登录页面
			if(request.getHeader("X-Requested-With")!=null){
				//ajax请求
				response.getWriter().print(JsonUtils.toJson(AjaxResult.errorInstance("请先登录")));
			}else{
				//普通请求
				response.getWriter().print("请先登录");
			}
			return false;
		}
		
		Long adminUserId = adminUser.getId();
		String path = request.getServletPath();
		Map<String, Object> map = new HashMap<>();
		map.put("adminUserId", adminUserId);
		map.put("path", path);

		//为了方便后面测试,这里暂时不使用权限检查		
		return true;
		/*
		//权限检查
		boolean result = adminUserRoleService.checkPermission(map);
		
		if(result){
			return true;
		}else{
			//用户无权限
			if(request.getHeader("X-Requested-With")!=null){
				//ajax请求
				response.getWriter().print(JsonUtils.toJson(AjaxResult.errorInstance("权限不足")));
			}else{
				//普通请求
				response.getWriter().print("权限不足");
			}
			return false;
		}
		*/
	}
}
