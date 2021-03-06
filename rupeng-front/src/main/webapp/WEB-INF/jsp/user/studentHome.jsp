<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/WEB-INF/jsp/header.jsp" %>
<title>学习中心</title>
</head>
<body>
	<%@ include file="/WEB-INF/jsp/nav.jsp" %>

	<div id="mainDiv" class="container" style="margin-top: 35px;">
		<style>
.panel-body a {
	cursor: pointer;
	display: block;
	margin-bottom4px;
}
</style>
		<div class="row">
			<div class="col-xs-6">
				<!-- 我的课程 -->
				<div class="panel panel-info">
					<div class="panel-heading">
						<h3 class="panel-title">我的课程</h3>
					</div>
					<div class="panel-body">
						<a onclick="ajaxSubmit('<%=ctxPath%>/userCard/applyNext.do')" target="_blank" style="color: red; margin-bottom: 20px;">申请新的学习卡&gt;&gt;</a> 
						<a href="<%=ctxPath%>/card/last.do" target="_blank" style="color: red; margin-bottom: 20px;">上次学到这里&gt;&gt;</a> 
						<c:forEach items="${cards}" var="card">
							<a href="<%=ctxPath%>/card/detail.do?id=${card.id}" target="_blank" style="margin-bottom: 6px;">${card.name}学习卡</a> 
						</c:forEach>
					</div>
				</div>
			</div>

			<div class="col-xs-6">
				<!-- 问题和提问 -->
				<div class="panel panel-danger">
					<div class="panel-heading">
						<h3 class="panel-title">问题和提问</h3>
					</div>
					<div class="panel-body">
						<div>
							<a href="<%=ctxPath%>/question/ask.do" target="_blank" style="color: red; margin-bottom: 20px;">我要提问 &gt;&gt;</a> 
							<a href="<%=ctxPath%>/question/list.do" target="_blank" style="color: red; margin-bottom: 20px;">所有问题列表 &gt;&gt;</a>
						</div>
						<div id="questionDiv" style="margin-top: 20px;">
						  <c:forEach items="${questionList }" var="question">	
							<a id="question${question.id }" onclick="$(this).remove()" href="<%=ctxPath%>/question/detail.do?id=${question.id }" target="_blank">您提问或者参与的此问题还没有解决</a>
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