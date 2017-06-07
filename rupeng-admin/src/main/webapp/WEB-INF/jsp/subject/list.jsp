<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
<%@ include file="/WEB-INF/jsp/header.jsp" %>

<title>学科列表</title>
</head>
<body>
	<div class="pd-20">

		<table class="table table-border table-bordered table-bg table-hover">
			<thead>
				<tr>
					<th>学科名称</th>
					<th>操作
						<button class="btn size-M radius"
							onclick="showLayer('添加学科','<%=ctxPath%>/subject/add.do')">
							添加</button>
					</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${subjects}" var="subject">
					<tr>
						<td>${subject.name}</td>
						<td>
							<button class="btn size-MINI radius"
								onclick="showLayer('修改学科','<%=ctxPath%>/subject/update.do?id=${subject.id}')">修改</button>
							<button class="btn size-MINI radius" onclick="ajaxDelete('<%=ctxPath%>/subject/delete.do','id=${subject.id}');">删除</button>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
</body>
</html>