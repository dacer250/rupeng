<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
<%@ include file="/WEB-INF/jsp/header.jsp" %>

<title>篇章列表</title>
</head>
<body>
	<div class="pd-20">
		<table class="table table-border table-bordered table-bg table-hover">
			<thead>
				<tr>
					<th>序号</th>
					<th>篇章名称</th>
					<th>描述</th>
					<th>操作
						<button class="btn size-M radius"
							onclick="showLayer('添加篇章','<%=ctxPath%>/chapter/add.do?cardId=${cardId}')">
							添加</button>
					</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${chapters}" var="chapter">
					<tr>
						<td>${chapter.seqNum}</td>
						<td>${chapter.name}</td>
						<td>${chapter.description}</td>
						<td>
							<button class="btn size-MINI radius" onclick="ajaxDelete('<%=ctxPath%>/chapter/delete.do','id=${chapter.id}')">删除</button>
							<button class="btn size-MINI radius"
								onclick="showLayer('修改篇章','<%=ctxPath%>/chapter/update.do?id=${chapter.id}')">修改</button>
							<button class="btn size-MINI radius"
								onclick="showLayer('段落管理','<%=ctxPath%>/segment/list.do?chapterId=${chapter.id}')">段落管理</button>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<br>
	</div>
</body>
</html>