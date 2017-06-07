<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
<%@ include file="/WEB-INF/jsp/header.jsp" %>

<title>班级成员列表</title>
</head>
<body>
	<div class="pd-20">
		<table class="table table-border table-bordered table-bg table-hover">
			<thead>
				<tr>
					<th>姓名</th>
					<th>邮箱</th>
					<th>手机</th>
					<th>操作
						<button class="btn size-M radius"
							onclick="showLayer('添加新成员','<%=ctxPath%>/classesUser/add.do?classesId=${classesId}')">
							添加</button>
					</th>
				</tr>
			</thead>
			<tbody>
			  	<c:forEach items="${users}" var="user">
				  	<tr>
						<td>${user.name}</td>
						<td>${user.email}</td>
						<td>${user.phone}</td>
						<td>
							<button class="btn size-MINI radius" onclick="ajaxDelete('<%=ctxPath%>/classesUser/delete.do','classesId=${classesId}&userId=${user.id}')">删除</button>
						</td>
					</tr>
			  	</c:forEach>
			</tbody>
		</table>
	</div>
</body>
</html>