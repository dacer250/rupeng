<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
<%@ include file="/WEB-INF/jsp/header.jsp" %>

<link href="<%=ctxPath%>/css/H-ui.login.css" rel="stylesheet"
	type="text/css" />
<title>后台登录 - 如鹏网</title>
</head>
<body>
	<div class="header"></div>
	<div class="loginWraper">
		<div id="loginform" class="loginBox">
			<form class="form form-horizontal" action="<%=ctxPath%>/adminUser/login.do" method="post">
				<div class="row cl">
					<label class="form-label col-3"><i class="Hui-iconfont">&#xe60d;</i></label>
					<div class="formControls col-8">
						<input type="text" name="account" placeholder="账户"
							class="input-text size-L" />
					</div>
				</div>
				<div class="row cl">
					<label class="form-label col-3"><i class="Hui-iconfont">&#xe60e;</i></label>
					<div class="formControls col-8">
						<input type="password" name="password" placeholder="密码"
							class="input-text size-L" />
					</div>
				</div>
				<div class="row cl">
					<div class="formControls col-8 col-offset-3">
						<input name="imageCode" class="input-text size-L" type="text"
							placeholder="验证码" style="width: 150px;" /> 
						<img src="<%=ctxPath%>/imageCode.do?t=<%=System.currentTimeMillis()%>" onclick="this.src='<%=ctxPath%>/imageCode.do?t='+new Date().getTime()" />
					</div>
				</div>
				<div class="row">
					<div class="formControls col-8 col-offset-3">
						<span style="color: red;">${message}</span>
					</div>
				</div>
				<div class="row">
					<div class="formControls col-8 col-offset-3">
						<input type="submit" class="btn btn-success radius size-L"
							value="&nbsp;登&nbsp;&nbsp;&nbsp;&nbsp;录&nbsp;" /> <input
							type="reset" class="btn btn-default radius size-L"
							value="&nbsp;取&nbsp;&nbsp;&nbsp;&nbsp;消&nbsp;" />
					</div>
				</div>
			</form>
		</div>
	</div>
	<div class="footer">®北京如鹏信息科技有限公司</div>
</body>
</html>