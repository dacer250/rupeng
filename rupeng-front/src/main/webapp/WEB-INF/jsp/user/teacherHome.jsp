<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/WEB-INF/jsp/header.jsp" %>
<title>教学中心</title>
</head>
<body>
	<%@ include file="/WEB-INF/jsp/nav.jsp" %>

	<div class="container" id="mainDiv" style="margin-top: 35px;">
		<style>
.panel-body a {
	display: block;
	margin-bottom: 4px;
	cursor: pointer;
}
</style>
		<div class="row">
			<div class="col-xs-6">
				<!-- 老师：解答问题 -->
				<div class="panel panel-danger">
					<div class="panel-heading">
						<h3 class="panel-title">解答问题</h3>
					</div>
					<div class="panel-body">
						<a style="color: red;"
							href="<%=ctxPath%>/question/list.do?condition=allUnresolved"
							target="_blank">查看问题列表 &gt;&gt;</a>
						<div id="questionDiv" style="margin-top: 20px;">
						  <c:forEach items="${questionList }" var="question">
							<a id="question${question.id }" onclick="$(this).remove()" href="<%=ctxPath%>/question/detail.do?id=${question.id}" target="_blank">此问题未回复或者还没有解决</a> 
						  </c:forEach>	
						</div>
						<div id="audio01" style="display: none;"></div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript">
		//获取新消息通知,显示
		function getNotification(){
			$.post("<%=ctxPath%>/notification.do","",function(result){
				if (result.status=="success") {
					var notifications = result.data;
					if(!notifications || notifications.length<1){
						return;
					}
					//遍历所有的通知消息，把通知消息显示在页面上
					for (var i = 0; i < notifications.length; i++) {
						var questionId = notifications[i].questionId;
						var content = notifications[i].content;
						//删掉questionId相同的通知
						$("#question"+questionId).remove();//如果持久消息和一次性消息重复只保留一个
						//追加新通知
						$("#questionDiv").append('<a id="question'+questionId+'" class="blink" href="<%=ctxPath%>/question/detail.do?id='+questionId+'" target="_blank" onclick="$(this).remove()">'+content+'</a>');
					}
					//声音提示
					$("#audio01").html('<audio src="<%=ctxPath%>/audios/notify.mp3" autoplay="autoplay"></audio>');
				}
			},"json");
		}
	
        $(function(){
        	getNotification();//页面刷新获取通知消息
        	
        	//定时器，每隔10秒检查是否有新的通知
        	setInterval(function(){
        		getNotification();
        	},10000);
        	
            //通知消息闪烁
            var i=0;
            setInterval(function(){
                $(".blink").css("color",(i%2==0)?'red':'blue');
                i++;
            },500);
        });
    </script>
	<%@ include file="/WEB-INF/jsp/footer.jsp" %>
</body>
</html>