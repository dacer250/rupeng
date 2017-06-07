<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/WEB-INF/jsp/header.jsp" %>
<title>学习课程</title>
</head>
<body>
	<%@ include file="/WEB-INF/jsp/nav.jsp" %>

	<div id="mainDiv" style="position: absolute;">
		<!-- 视频窗口 -->
		<div class="tab-content over-hide">
			${segment.videoCode}
		</div>
	</div>

	<div style="width: auto; height: 600px; margin-left: 800px;">

		<div style="margin: 10px;">
			<!-- 课程描述信息 -->
			<div>
				<span style="font-size: 16px;">${card.name} &gt;&gt; ${chapter.seqNum} ${chapter.name}
					&gt;&gt; ${segment.seqNum} ${segment.name}</span>
			</div>
			<br />
			<div>
				<a class="btn btn-default" href="#" target="_blank">我要提问</a> <a
					class="btn btn-default" style="margin-left: 1%;" href="<%=ctxPath%>/segment/next.do?currentSegmentId=${segment.id}">下一节</a>
			</div>
			<br />
			<div class="panel panel-default">
				<div class="panel-heading">注意点</div>
				<div class="panel-body">${segment.description}</div>
			</div>
			<div class="panel panel-default">
				<div class="panel-heading">
					<h3 class="panel-title">相关问题</h3>
				</div>
				<div class="panel-body">
					<div>
						<a href="#" target="_blank">1 &nbsp; 2016-10-26 12:11:54</a>
					</div>
					<div>
						<a href="#" target="_blank">2 &nbsp; 2016-10-26 12:12:03</a>
					</div>
				</div>
			</div>
		</div>
	</div>

	<%@ include file="/WEB-INF/jsp/footer.jsp" %>
</body>
</html>