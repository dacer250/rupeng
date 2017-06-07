<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
<%@ include file="/WEB-INF/jsp/header.jsp" %>

<title>段落列表</title>
</head>
<body>
	<div class="pd-20">
		<table class="table table-border table-bordered table-bg table-hover">
			<thead>
				<tr>
					<th>序号</th>
					<th>段落名称</th>
					<th>描述</th>
					<th>操作
						<button class="btn size-M radius"
							onclick="showLayer('添加段落','<%=ctxPath%>/segment/add.do?chapterId=${chapterId}')">
							添加</button>
					</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${segments}" var="segment">
					<tr>
						<td>${segment.seqNum}</td>
						<td>${segment.name}</td>
						<td>${segment.description}</td>
						<td>
							<button class="btn size-MINI radius" onclick="ajaxDelete('<%=ctxPath%>/segment/delete.do','id=${segment.id}')">删除</button>
							<button class="btn size-MINI radius"
								onclick="showLayer('修改段落','<%=ctxPath%>/segment/update.do?id=${segment.id}')">修改</button>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<br>
	</div>
</body>
</html>