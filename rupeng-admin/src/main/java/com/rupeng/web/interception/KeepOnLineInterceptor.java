package com.rupeng.web.interception;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

import com.rupeng.pojo.AdminUser;
import com.rupeng.service.AdminUserService;
import com.rupeng.util.CommonUtils;
import com.rupeng.util.JedisUtils;

public class KeepOnLineInterceptor implements HandlerInterceptor {

	@Autowired
	private AdminUserService adminUserService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// 拦截器-保持在线功能
		// 使暂时离开的用户,即使session过时销毁,也能进行操作(配置拦截路径,决定哪些controller操作可以使用拦截器)

		// 先看session中是否还有adminUser数据
		AdminUser adminUser = (AdminUser) request.getSession().getAttribute("adminUser");
		if (adminUser == null) {

			// 到redis中查 存储时key-value:"keepOnLine_"+JSESSIONID:userId
			Cookie sessionIdCookie = WebUtils.getCookie(request, "JSESSIONID");// 客户端访问时会带的cookie:JSESSIONID

			if (sessionIdCookie == null) {
				return true;// 首次访问,继续执行
			}
			
			String sessionId = sessionIdCookie.getValue();
			String userId = JedisUtils.get("keepOnLine_" + sessionId);
			
			if (CommonUtils.isEmpty(userId)) {
                return true;//没登陆过或redis中超时
            }
			
			// 从数据库查出adminUser
			adminUser = adminUserService.selectOne(Long.parseLong(userId));

			if (adminUser != null) {// 说明:如果adminUser==null,查不出来此用户,可能是此用户软删除了或其他情况,不必理会让其继续执行
				request.getSession().setAttribute("adminUser", adminUser);
			}
		}

		if (adminUser != null) {
		// 重新设置userId以及有效时间
		JedisUtils.setex("keepOnLine_" + request.getSession().getId(), 60 * 60 * 24, adminUser.getId().toString());
		}
		
		return true;// 继续执行
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

	}

}