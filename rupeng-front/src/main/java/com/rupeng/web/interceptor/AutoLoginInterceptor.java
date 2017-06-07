package com.rupeng.web.interceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.WebUtils;

import com.rupeng.pojo.User;
import com.rupeng.service.UserService;
import com.rupeng.util.CommonUtils;
//自动登录
public class AutoLoginInterceptor extends HandlerInterceptorAdapter {
	
	@Autowired
	private UserService userService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		User user = (User) request.getSession().getAttribute("user");
		if (user==null) {
			//取出cookie中携带的loginName 和password
	        Cookie loginNameCookie = WebUtils.getCookie(request, "loginName");
	        Cookie passwordCookie = WebUtils.getCookie(request, "password");
	        if (loginNameCookie != null && passwordCookie != null) {
	        	String loginName = loginNameCookie.getValue();
		        String password = passwordCookie.getValue();
		        user = new User();
		        if (CommonUtils.isEmail(loginName)) {
		            user.setEmail(loginName);
		        } else {
		            user.setPhone(loginName);
		        }
		        
		        user = userService.selectOne(user);
	            //密码已经是加密的了
	            if (user.getPassword().equals(password)) {
	                request.getSession().setAttribute("user", user);
	            }
	        }
		}
		//无论是否自动登录成功都返回true，因为此拦截器只是尝试自动登录，
		//登录失败也没关系，交给其他代码去继续处理
		return true;
	}
}
