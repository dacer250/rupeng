<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
<%@ include file="/WEB-INF/jsp/header.jsp" %>

<title>学习卡列表</title>
</head>
<body>
	<div class="pd-20">
		<table class="table table-border table-bordered table-bg table-hover">
			<thead>
				<tr>
					<th>角色名称</th>
					<th>描述</th>
					<th>操作
						<button class="btn size-M radius"
							onclick="showLayer('添加角色','<%=ctxPath%>/role/add.do')">
							添加</button>
					</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${roles}" var="role">
					<tr>
						<td>${role.name}</td>
						<td>${role.description}</td>
						<td>
							<button class="btn size-MINI radius" onclick="ajaxDelete('<%=ctxPath%>/role/delete.do','id=${role.id}')">删除</button>
							<button class="btn size-MINI radius"
								onclick="showLayer('修改角色','<%=ctxPath%>/role/update.do?id=${role.id}')">修改</button>
							<button class="btn size-MINI radius"
								onclick="showLayer('分配权限','<%=ctxPath%>/rolePermission/update.do?roleId=${role.id}')">分配权限</button>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
</body>
</html>