<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
<%@ include file="/WEB-INF/jsp/header.jsp" %>

<title>权限列表</title>
</head>
<body>
	<div class="pd-20">
		<table class="table table-border table-bordered table-bg table-hover">
			<thead>
				<tr>
					<th>请求路径</th>
					<th>描述</th>
					<th>操作
						<button class="btn size-M radius"
							onclick="showLayer('添加新权限','<%=ctxPath%>/permission/add.do')">
							添加</button>
					</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${permissions}" var="permission">
					<tr>
						<td>${permission.path}</td>
						<td>${permission.description}</td>
						<td>
							<button class="btn size-MINI radius" onclick="ajaxDelete('<%=ctxPath%>/permission/delete.do','id=${permission.id}')">删除</button>
							<button class="btn size-MINI radius"
								onclick="showLayer('修改权限','<%=ctxPath%>/permission/update.do?id=${permission.id}')">修改</button>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
</body>
</html>