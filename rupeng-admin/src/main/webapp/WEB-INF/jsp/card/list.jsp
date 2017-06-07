<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt"%>
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
					<th>添加日期</th>
					<th>学习卡名称</th>
					<th>课件下载地址</th>
					<th>操作
						<button class="btn size-M radius"
							onclick="showLayer('添加学习卡','<%=ctxPath%>/card/add.do')">
							添加</button>
					</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${cards}" var="card">
					<tr>
						<td><f:formatDate value="${card.createTime}"/></td>
						<td>${card.name}</td>
						<td>${card.courseware}</td>
						<td>
							<button class="btn size-MINI radius" onclick="ajaxSubmit('<%=ctxPath%>/card/delete.do','id=${card.id}')">删除</button>
							<button class="btn size-MINI radius"
								onclick="showLayer('修改学习卡','<%=ctxPath%>/card/update.do?id=${card.id}')">修改</button>
							<button class="btn size-MINI radius"
								onclick="showLayer('章节管理','<%=ctxPath%>/chapter/list.do?cardId=${card.id}')">篇章管理</button>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
</body>
</html>