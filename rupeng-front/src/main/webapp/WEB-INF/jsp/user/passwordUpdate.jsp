<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/WEB-INF/jsp/header.jsp" %>
<style type="text/css">
	/* 设置错误提示字体为红色 */
	label.error{
    	color:red;
	}
</style>
<script type="text/javascript" src="<%=ctxPath%>/lib/validation/jquery.validate.min.js"></script>
<script type="text/javascript" src="<%=ctxPath%>/lib/validation/additional-methods.min.js"></script>
<script type="text/javascript" src="<%=ctxPath%>/lib/validation/localization/messages_zh.min.js"></script>
<script type="text/javascript">
	/* 表单验证 */
	$(function(){
		$("form").validate({
			rules:{
				password:{
					required:true,
					minlength:6
				},
				newpassword:{
					required:true,
					minlength:6
				},
				renewpassword:{
					equalTo:"#newpassword"
				}
			},
			messages:{
				password:{
					required:"密码不能为空",
					minlength:"密码长度不能小于6位"
				},
				newpassword:{
					required:"密码不能为空",
					minlength:"密码长度不能小于6位"
				},
				renewpassword:{
					equalTo:"两次密码输入不一致"
				}
			},
			submitHandler:function(form){
				ajaxSubmitForm(form);
			}
		});
	});
</script>
<title>修改密码</title>
</head>
<body>
	<%@ include file="/WEB-INF/jsp/nav.jsp" %>

	<!-- 用户个人信息 -->
	<div id="mainDiv" class="container" style="margin-top: 35px;">

		<style type="text/css">
.row {
	padding: 5px;
	font-size: 16px;
}

.row div {
	padding-left: 5px;
}
</style>

		<div class="row">
			<!-- 左侧导航栏 -->
			<div class="col-md-3">
				<div class="list-group">
					<a class="list-group-item" href="<%=ctxPath%>/user/userInfo.do">
						<span class="glyphicon glyphicon-user">&nbsp;</span><span>个人信息</span></a> 
					<a class="list-group-item" href="<%=ctxPath%>/user/passwordUpdate.do">
						<span class="glyphicon glyphicon-lock">&nbsp;</span><span>修改密码</span></a> 
					<c:if test="${not user.isTeacher}">
						<a class="list-group-item" href="<%=ctxPath%>/user/studentHome.do">
						<span class="glyphicon glyphicon-home">&nbsp;</span><span>学习中心 &gt;&gt;</span></a> 
					</c:if>
					<c:if test="${user.isTeacher}">
						<a class="list-group-item" href="<%=ctxPath%>/user/teacherHome.do">
						<span class="glyphicon glyphicon-home">&nbsp;</span><span>教学中心 &gt;&gt;</span></a>
					</c:if>
				</div>
			</div>

			<!-- 右侧主题区域 -->
			<div class="col-md-9">
				<div class="panel panel-default">
					<div class="panel-body">
						<form class="form-horizontal"  action="<%=ctxPath%>/user/passwordUpdate.do">
							<div class="form-group">
								<label class="col-md-2 control-label">原密码</label>
								<div class="col-md-7">
									<input name="password" type="password" class="form-control" />
								</div>
							</div>

							<div class="form-group">
								<label class="col-md-2 control-label">新密码</label>
								<div class="col-md-7">
									<input id="newpassword" name="newpassword" type="password" class="form-control" />
								</div>
							</div>

							<div class="form-group">
								<label class="col-md-2 control-label">重复新密码</label>
								<div class="col-md-7">
									<input name="renewpassword" type="password"
										class="form-control" />
								</div>
							</div>

							<div class="form-group">
								<div class="col-md-offset-2 col-md-7">
									<input class="btn btn-success" type="submit" value="保存" />
								</div>
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>

	<%@ include file="/WEB-INF/jsp/footer.jsp" %>
</body>
</html>