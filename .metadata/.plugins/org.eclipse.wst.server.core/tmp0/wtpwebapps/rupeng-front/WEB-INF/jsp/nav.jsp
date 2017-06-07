<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!--登陆注册部分-->
<div class="nav-bar container-fluid">
	<div class="container center">
		<div style="float: right;">
			<ul class="nav-list">
				<c:if test="${not empty user}">
					<li class="f12"><a href="<%=request.getContextPath()%>/user/userInfo.do">${(empty user.name)?user.email:user.name}</a></li>
					<li class="f12"><a href="<%=request.getContextPath()%>/user/logout.do">退出</a></li>
				</c:if>
				<c:if test="${empty user}">
					<li class="f12"><a href="<%=request.getContextPath()%>/user/login.do">登录</a></li>
					<li class="f12"><a href="<%=request.getContextPath()%>/user/register.do">注册</a></li>
				</c:if>
			</ul>
		</div>
	</div>
</div>
<!-- 导航区域 -->
<div class="container-full logo-slogan">
	<div class="container logo-box">
		<div class="pull-left logo">
			<a href="<%=request.getContextPath()%>" title="如鹏网"> <img alt="如鹏网"
				src="<%=request.getContextPath()%>/images/logo.png">
			</a>
		</div>
		<div class="pull-left slogan">
			<h1 class="f16">专注于大学生就业的在线教育</h1>
		</div>
		<nav class="navbar  pull-right logo-nav">
			<ul class="nav nav-pills">
				<li><a class="f16" href="#" title="首页">首页</a></li>
				<li><a class="f16" href="#" title="0元学">“0元学”</a></li>
				<li><a class="f16" href="#" title="就业喜报">就业喜报</a></li>
				<li><a class="f16" href="#" title="关于如鹏">关于如鹏</a></li>
				<li><a class="f16" href="#" title="他们正在学">他们正在学</a></li>
			</ul>
		</nav>
	</div>
</div>
