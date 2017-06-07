<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
<%@ include file="/WEB-INF/jsp/header.jsp" %>

<title>分配角色</title>
</head>
<body>
	<div class="pd-20">
		<form onsubmit="ajaxSubmitForm(this,true)" action="<%=ctxPath%>/adminUserRole/update.do">
			<input type="hidden" name="adminUserId" value="${adminUserId}" />
			<table class="table table-border table-bordered table-bg table-hover">
			<thead>
				<tr>
					<th>选中</th>
					<th>角色名称</th>
					<th>角色描述</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${roles}" var="role">
					<tr>
						<td><input type="checkbox" name="roleIds" value="${role.id}"
							<c:forEach items="${adminUserRoles}" var="adminUserRole">
								<c:if test="${adminUserRole.roleId eq role.id}">checked="checked" </c:if>
							</c:forEach>
							/>
						</td>
						<td>${role.name}</td>
						<td>${role.description}</td>
					</tr>
				</c:forEach>
			</tbody>
			</table>
			<br> <input class="btn btn-primary radius" type="submit"
				value="分配角色" /> <input class="btn btn-default radius" type="button"
				value="关闭" onclick="parent.location.reload()"
				style="margin-left: 30px;" />
		</form>
	</div>
</body>
</html>