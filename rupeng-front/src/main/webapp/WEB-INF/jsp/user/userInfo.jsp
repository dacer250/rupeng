<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/WEB-INF/jsp/header.jsp" %>
<title>个人信息</title>
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
				<!-- 个人信息显示与修改区域 -->
				<div class="panel panel-default">
					<div class="panel-body">
						<h4>
							<img style="max-width: 50px;"
								src="http://static.rupeng.com/static/imgs/msg.gif">
							请完善您的个人信息，我们将根据您的学校、年级、专业等向您推荐适合您的学习资料，当如鹏举办线下活动时也会通知相应城市的同学参加！如鹏不会向第三方透露您的个人信息，请放心填写！
						</h4>

						<form onsubmit="ajaxSubmitForm(this)" class="form-horizontal" action="<%=ctxPath%>/user/update.do">
							<div class="form-group">
								<label class="col-md-2 control-label">邮箱 :</label>
								<div class="col-md-7">
									<input name="email" type="text" class="form-control"
										value="${user.email}" disabled="disabled" />
								</div>
							</div>

							<div class="form-group">
								<label class="col-md-2 control-label">真实姓名:</label>
								<div class="col-md-7">
									<input name="name" type="text" class="form-control" value="${user.name}" />
								</div>
							</div>

							<div class="form-group">
								<label class="col-md-2 control-label">所在学校:</label>
								<div class="col-md-7">
									<input name="school" type="text" class="form-control"
										value="${user.school}" />
								</div>
							</div>

							<div class="form-group">
								<label class="col-md-2 control-label">性别:</label>
								<div class="col-md-7">
									<label class="radio-inline">
										<input type="radio" name="isMale" value="true" 
										<c:if test="${user.isMale}">checked="checked"</c:if>
										/>男</label> 
									<label class="radio-inline">
										<input type="radio" name="isMale" value="false" 
										<c:if test="${user.isMale eq false}">checked="checked"</c:if>
										/>女</label>
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

				<!-- 手机号显示与绑定区域 -->
				<div class="panel panel-default">
					<div class="panel-body">
						<h4>
							<img style="max-width: 50px;"
								src="<%=ctxPath%>/images/msg.gif">
							手机号一经绑定，不可修改。如果确实需要修改，请联系网站客服。
						</h4>
						<c:if test="${not empty user.phone}">
							<div class="row">
								<div class="col-md-2 text-right">手机号:</div>
								<div class="col-md-2">${user.phone}</div>
								<div class="col-md-8">已绑定</div>
							</div>
						</c:if>
						
						<c:if test="${empty user.phone}">
							<form onsubmit="ajaxSubmitForm(this)" class="form-horizontal" action="<%=ctxPath%>/user/bindPhone.do">
								<div class="form-group">
									<label class="col-md-2 control-label">手机号:</label>
									<div class="col-md-7">
										<input id="phone" name="phone" type="text" class="form-control"
											value="" />
									</div>
								</div>
	
								<div class="form-group">
									<label class="col-md-2 control-label">验证码 :</label>
									<div class="col-md-7">
										<div class="col-md-5" style="padding-left: 0px;">
											<input type="text" name="smsCode" class="form-control" />
										</div>
										<div class="col-md-4 text-center">
											<input type="button" class="btn btn-default" value="获取短信验证码"
												onclick="sendSMSCode('<%=ctxPath%>/smsCode.do',$('#phone').val(),this)" />
										</div>
										<div class="col-md-3"></div>
									</div>
								</div>
								<div class="form-group">
									<div class="col-md-offset-2 col-md-7">
										<input class="btn btn-success" type="submit" value="绑定" />
									</div>
								</div>
							</form>
						</c:if>
					</div>
				</div>

			</div>
		</div>
	</div>

	<%@ include file="/WEB-INF/jsp/footer.jsp" %>
</body>
</html>