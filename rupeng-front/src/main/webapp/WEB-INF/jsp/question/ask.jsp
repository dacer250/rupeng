<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/WEB-INF/jsp/header.jsp" %>
<script type="text/javascript" src="<%=ctxPath%>/lib/ueditor/ueditor.config.js"></script>
<script type="text/javascript" src="<%=ctxPath%>/lib/ueditor/ueditor.all.min.js"></script>
<script type="text/javascript">
		function changeCard(cardId){
			$.post('<%=ctxPath%>/chapter/list.do','cardId='+cardId,function(result){
				if(result.status == 'success'){
					$("#chapter").html("");
					var chapters = result.data;
					for (var i = 0; i < chapters.length; i++) {
						var chapter = chapters[i];
						$("#chapter").append("<option value='"+chapter.id+"'>"+chapter.seqNum+" "+chapter.name+"</option>");
					}
					
					$("#segment").html("");
					
					if($("#chapter").val()){//避免chapter没有选中项也进行提交,服务器springMVC解析异常
						changeChapter($("#chapter").val());
					}
				}
			},'json');
		}
		
		function changeChapter(chapterId){
			$.post('<%=ctxPath%>/segment/list.do','chapterId='+chapterId,function(result){
				if(result.status == 'success'){
					$("#segment").html("");
					
					var segments = result.data;
					for (var i = 0; i < segments.length; i++) {
						var segment = segments[i];
						$("#segment").append("<option value='"+segment.id+"'>"+segment.seqNum+" "+segment.name+"</option>");
					}
				}
			},'json');	
		}
		
		function ajaxSubmitAsk(formDom){
			$.post(formDom.action,$(formDom).serialize(),function(result){
				alert(result.data);
				if(result.status=='success'){
					window.close();
				}
			},'json');
			
			event.preventDefault();//取消默认行为
		}
		
</script>
<title>我要提问</title>
</head>
<body>
	<%@ include file="/WEB-INF/jsp/nav.jsp" %>

	<div id="mainDiv" class="container" style="margin-top: 35px;">
		<div class="panel panel-info">
			<div class="panel-heading">
				<h3 class="panel-title">我要提问</h3>
			</div>
			<div class="panel-body">
				<form onsubmit="ajaxSubmitAsk(this)" action="<%=ctxPath %>/question/ask.do" method="post">
					<div class="row">
						<div class="col-sm-4">
							<select class="form-control" onchange="changeCard(this.value)">
							  <c:forEach items="${cardList }" var="card">	
								<option value="${card.id }" 
									<c:if test="${card.id eq lastCardId }">selected="selected"</c:if>
								>${card.name }学习卡</option>
							  </c:forEach>	
							</select>
						</div>
						<div class="col-sm-4">
							<select class="form-control" id="chapter" onchange="changeChapter(this.value)">
							  <c:forEach items="${chapterList }" var="chapter">	
								<option value="${chapter.id }" 
									<c:if test="${chapter.id eq lastChapterId }">selected="selected"</c:if>
								>${chapter.seqNum } ${chapter.name }</option>
							  </c:forEach>	
							</select>
						</div>
						<div class="col-sm-4">
							<select class="form-control" id="segment" name="segmentId">
							  <c:forEach items="${segmentList }" var="segment">
								<option value="${segment.id }"
									<c:if test="${segment.id eq lastSegmentId }">selected="selected"</c:if>
								>${segment.seqNum } ${segment.name }</option>
							  </c:forEach>
							</select>
						</div>
					</div>

					<br /> <span class="label label-danger">全部！！报错信息：</span>
					<script id="errorInfo" name="errorInfo" type="text/plain"></script>
					<script type="text/javascript">
                        <!-- 实例化编辑器 -->
                        var ue1 = UE.getEditor('errorInfo',{
                        	"serverUrl":"<%=ctxPath%>/upload.do",
                            "elementPathEnabled" : false,
                            "wordCount":false,
                            "initialFrameHeight":200,
                            "toolbars":[ ['simpleupload','emotion','attachment'] ]
                        });
                    </script>

					<br /> <span class="label label-danger">相关代码：</span>
					<script id="errorCode" name="errorCode" type="text/plain"></script>
					<script type="text/javascript">
                        <!-- 实例化编辑器 -->
                        var ue2 = UE.getEditor('errorCode',{
                        	"serverUrl":"<%=ctxPath%>/upload.do",
                            "elementPathEnabled" : false,
                            "wordCount":false,
                            "initialFrameHeight":200,
                            "toolbars":[ ['simpleupload','emotion','attachment'] ]
                        });
                    </script>

					<br /> <span class="label label-danger">问题描述：</span>
					<script id="description" name="description" type="text/plain"></script>
					<script type="text/javascript">
                        <!-- 实例化编辑器 -->
                        var ue3 = UE.getEditor('description',{
                        	"serverUrl":"<%=ctxPath%>/upload.do",
                            "elementPathEnabled" : false,
                            "wordCount":false,
                            "initialFrameHeight":200,
                            "toolbars":[ ['simpleupload','emotion','attachment'] ]
                        });
                    </script>
					<br /> <input type="submit" class="btn btn-default" value="提交问题" />
				</form>
			</div>
		</div>
	</div>

	<%@ include file="/WEB-INF/jsp/footer.jsp" %>
</body>
</html>