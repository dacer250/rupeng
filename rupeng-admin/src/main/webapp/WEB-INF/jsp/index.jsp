<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
<%@ include file="/WEB-INF/jsp/header.jsp" %>

<link href="<%=ctxPath%>/skin/default/skin.css"
	rel="stylesheet" type="text/css" id="skin" />
<title>如鹏网后台管理系统</title>
</head>
<body>
	<header class="Hui-header cl">
		<a class="Hui-logo l" title="如鹏网 后台管理系统">如鹏网后台管理系统</a>
		<ul class="Hui-userbar">
			<li class="dropDown dropDown_hover"><a class="dropDown_A">${adminUser.account}
					<i class="Hui-iconfont">&#xe6d5;</i>
			</a>
				<ul class="dropDown-menu radius box-shadow">
					<li><a
						onclick="showLayer('修改密码','<%=ctxPath%>/adminUser/updatePassword.do',600,371)">修改密码</a></li>
					<li><a
						href="<%=ctxPath%>/adminUser/logout.do">退出</a></li>
				</ul></li>
			<li id="Hui-skin" class="dropDown right dropDown_hover"><a
				href="javascript:;" title="换肤"><i class="Hui-iconfont"
					style="font-size: 18px">&#xe62a;</i></a>
				<ul class="dropDown-menu radius box-shadow">
					<li><a href="javascript:;" data-val="default" title="默认（黑色）">默认（黑色）</a></li>
					<li><a href="javascript:;" data-val="blue" title="蓝色">蓝色</a></li>
					<li><a href="javascript:;" data-val="green" title="绿色">绿色</a></li>
					<li><a href="javascript:;" data-val="red" title="红色">红色</a></li>
					<li><a href="javascript:;" data-val="yellow" title="黄色">黄色</a></li>
					<li><a href="javascript:;" data-val="orange" title="绿色">橙色</a></li>
				</ul></li>
		</ul>
		<a aria-hidden="false" class="Hui-nav-toggle" href="#"></a>
	</header>
	<aside class="Hui-aside">
		<input runat="server" id="divScrollValue" type="hidden" value="" />
		<div class="menu_dropdown bk_2">
			<dl id="menu-picture">
				<dt>
					<i class="Hui-iconfont">&#xe613;</i> 教学管理<i
						class="Hui-iconfont menu_dropdown-arrow">&#xe6d5;</i>
				</dt>
				<dd>
					<ul>
						<li><a _href="<%=ctxPath%>/subject/list.do"
							href="javascript:;">学科管理</a></li>
						<li><a _href="<%=ctxPath%>/card/list.do"
							href="javascript:void(0)">学习卡管理</a></li>
						<li><a
							_href="<%=ctxPath%>/cardSubject/order.do"
							href="javascript:void(0)">学习卡排序</a></li>
						<li><a _href="<%=ctxPath%>/user/list.do"
							href="javascript:void(0)">前台用户管理</a></li>
						<li><a _href="<%=ctxPath%>/classes/list.do"
							href="javascript:void(0)">班级管理</a></li>
					</ul>
				</dd>
			</dl>

			<dl id="menu-comments">
				<dt>
					<i class="Hui-iconfont">&#xe62e;</i>系统管理<i
						class="Hui-iconfont menu_dropdown-arrow">&#xe6d5;</i>
				</dt>
				<dd>
					<ul>
						<li><a
							_href="<%=ctxPath%>/adminUser/list.do"
							href="javascript:void(0)">管理员用户管理</a></li>
						<li><a _href="<%=ctxPath%>/role/list.do"
							href="javascript:void(0)">角色管理</a></li>
						<li><a
							_href="<%=ctxPath%>/permission/list.do"
							href="javascript:void(0)">权限管理</a></li>
					</ul>
				</dd>
			</dl>
		</div>
	</aside>
	<div class="dislpayArrow">
		<a class="pngfix" href="javascript:void(0);"
			onClick="displaynavbar(this)"></a>
	</div>
	<section class="Hui-article-box">
		<div id="Hui-tabNav" class="Hui-tabNav">
			<div class="Hui-tabNav-wp">
				<ul id="min_title_list" class="acrossTab cl">
					<li class="active"><span title="我的桌面" data-href="welcome.do">我的桌面</span><em></em></li>
				</ul>
			</div>
			<div class="Hui-tabNav-more btn-group">
				<a id="js-tabNav-prev" class="btn radius btn-default size-S"
					href="javascript:;"><i class="Hui-iconfont">&#xe6d4;</i></a><a
					id="js-tabNav-next" class="btn radius btn-default size-S"
					href="javascript:;"><i class="Hui-iconfont">&#xe6d7;</i></a>
			</div>
		</div>
		<div id="iframe_box" class="Hui-article">
			<div class="show_iframe">
				<div style="display: none" class="loading"></div>
				<iframe scrolling="yes" frameborder="0" src="<%=ctxPath%>/welcome.do"></iframe>
			</div>
		</div>
	</section>

</body>
</html>