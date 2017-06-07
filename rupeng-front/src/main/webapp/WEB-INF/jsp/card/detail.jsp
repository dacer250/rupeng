<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/WEB-INF/jsp/header.jsp" %>
<title>学习卡详情</title>
</head>
<body>
	<%@ include file="/WEB-INF/jsp/nav.jsp" %>

	<div id="mainDiv" class="container" style="margin-top: 35px;">
		<!-- 此学习卡描述信息 -->
		<div class="well">
			<span style="font-size: 28px;">${card.name}</span> <span class="label label-danger" style="margin-left: 4%;">还有${remainValidDays}天过期</span>
			<span class="label label-success" style="margin-left: 4%;">课件资料：${card.courseware}</span>
		</div>

		<!-- 此学习卡包含的课程章节列表 -->
		<div class="panel-group" id="accordion">
			<c:forEach items="${courseMap}" var="entry">
				<div class="panel panel-default">
					<!-- 章节标题 -->
					<div class="panel-heading">
						<div class="panel-title">
							<a style="display: block;" data-toggle="collapse"
								data-parent="#accordion" href="#chapter${entry.key.id}" aria-expanded="true"
								class=""> ${entry.key.name} </a>
						</div>
					</div>
					<!-- 课程列表 -->
					<div id="chapter${entry.key.id}" class="panel-collapse collapse 
					<c:forEach items="${entry.value}" var="segment">
						<c:if test="${segment.id eq lastSegmentId}">in</c:if>					
					</c:forEach>
					" aria-expanded="true">
						<div class="panel-body">
							<div class="list-group">
								<c:forEach items="${entry.value}" var="segment">
									<a class="list-group-item" href="<%=ctxPath%>/segment/detail.do?id=${segment.id}" target="_blank"> 
										<span>${segment.name}</span>
										<c:if test="${segment.id eq lastSegmentId}">
											<span class="label label-danger" style="margin-left: 100px;">上次学到这里 &gt;&gt;</span>
										</c:if>
									</a> 
								</c:forEach>
							</div>
						</div>
					</div>
				</div>
			</c:forEach>
		</div>
	</div>

	<%@ include file="/WEB-INF/jsp/footer.jsp" %>
</body>
</html>