package com.rupeng.web.resolver;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.rupeng.util.AjaxResult;
import com.rupeng.util.JsonUtils;
@Component
public class MyHandlerExceptionResolver implements HandlerExceptionResolver{
	private static final Logger logger = LogManager.getLogger(MyHandlerExceptionResolver.class);
	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		//记录异常信息
		logger.error("服务器错误",ex);
		
		if(request.getHeader("X-Requested-With")!=null){
			 //如果是ajax请求，就返回一个json格式的出错提示信
			try {
				response.getWriter().print(JsonUtils.toJson(AjaxResult.errorInstance("服务器出错了")));
			} catch (IOException e) {
				// 需要记录日志
				logger.error("向ajax请求返回服务器出错信息时发生异常",e);
			}
			
			//返回一个空的ModelAndView表示已经手动生成响应
            //return null表示使用默认的处理方式，等于没处理
			return new ModelAndView();
		}else{
			//普通请求
			return new ModelAndView("500");
		}
	}
}
