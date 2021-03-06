<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="rp" uri="http://www.rupeng.com/tag"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/WEB-INF/jsp/header.jsp" %>
<script type="text/javascript" src="<%=ctxPath%>/lib/ueditor/ueditor.config.js"></script>
<script type="text/javascript" src="<%=ctxPath%>/lib/ueditor/ueditor.all.min.js"></script>
<title>问题详情</title>
</head>
<body>
	<%@ include file="/WEB-INF/jsp/nav.jsp" %>

	<div id="mainDiv" class="container" style="margin-top: 35px;">
		<div class="panel panel-info">
			<div class="panel-heading">
				<h3 class="panel-title">问题详情</h3>
			</div>
			<div class="panel-body">
				<div class="row">
					<div class="col-sm-8" style="margin-bottom: 10px;">${question.username }
						(${classes.name }) &nbsp;&nbsp;&nbsp;&nbsp; 
						<fmt:formatDate value="${question.createTime }"/> &nbsp;&nbsp;&nbsp;&nbsp;
						${question.courseInfo }
					</div>
				</div>
				<br> <span class="label label-danger">问题描述：</span>
				<div class="panel panel-info" style="margin-top: 3px;">
					<div class="panel-body">
						<div>
							${question.description }
						</div>
					</div>
				</div>

				<br> <span class="label label-danger">相关代码：</span>
				<div class="panel panel-info" style="margin-top: 3px;">
					<div class="panel-body">
						<div>
							${question.errorCode }
						</div>
					</div>
				</div>

				<br> <span class="label label-danger">全部！！报错信息：</span>
				<div class="panel panel-info" style="margin-top: 3px;">
					<div class="panel-body">
						<div>
							${question.errorInfo }
						</div>
					</div>
				</div>

				<!-- 如果问题已被解决，就不能再回答了 -->
				<c:if test="${question.isResolved==null || question.isResolved==false }">
					<br>
					<br>
					<br>
					<!-- 我来回答 -->
					<span class="label label-success">我来回答：</span>
					<form onsubmit="ajaxSubmitForm(this)" action="<%=ctxPath %>/question/answer.do" method="post">
						<input type="hidden" name="questionId" value="${question.id }"/>
						<script id="content" name="content" type="text/plain"></script>
						<input type="submit" style="margin-top: 5px;" class="btn btn-default" value="提交答案" />
					</form>
					<script type="text/javascript">
	                        <!-- 实例化编辑器 -->
	                        var ue = UE.getEditor('content',{
	                        	"serverUrl":"<%=ctxPath%>/upload.do",
	                            "elementPathEnabled" : false,
	                            "wordCount":false,
	                            "initialFrameHeight":200,
	                            "toolbars":[ ['simpleupload','emotion','attachment'] ]
	                        });
	                </script>
				</c:if>
				
				<br>
				<br>
				<br>
				<!-- 所有回答 -->
				<span class="label label-success">所有回答：</span>
				<div class="panel panel-success">
					<div class="panel-body">
						<!-- 答案列表 -->
						<rp:questionAnswers question="${question }" rootAnswers="${rootAnswerList }"/>
					</div>
				</div>
				<!-- 隐藏的编辑器，当补充回答时显示出来 -->
				<div id="editor02" style="display: none;">
					<form onsubmit="ajaxSubmitForm(this)" action="<%=ctxPath %>/question/answer.do" method="post">
						<input type="hidden" name="questionId" value="${question.id }"/>
						<input type="hidden" id="parentId" name="parentId"/>
						<script id="content02" name="content" type="text/plain"></script>
						<input type="submit" class="btn btn-default" value="提交答案" />
					</form>
					<script type="text/javascript">
                        <!-- 实例化编辑器 -->
                        UE.getEditor('content02',{
                        	"serverUrl":"<%=ctxPath%>/upload.do",
                            "elementPathEnabled" : false,
                            "wordCount":false,
                            "initialFrameHeight":200,
                            "initialFrameWidth":900,
                            "toolbars":[ ['simpleupload','emotion','attachment'] ]
                        });
                        $(function(){
                            //给“补充回答、追问”按钮注册点击事件处理函数
                            $("[reanswer]").click(function(){
                                $("#editor02").css("display","block");
                                $("#parentId").val($(this).attr("reanswer"));
                                $("#editor02").insertAfter($(this));
                            });
                            //给“采纳”按钮注册点击事件处理函数
                            $("[adopt]").click(function(){
                                $.post("<%=ctxPath%>/question/adopt.do","questionAnswerId="+$(this).attr("adopt"),function(ajaxResult){
                                    alert(ajaxResult.data);
                                    if(ajaxResult.status=="success"){
                                        location.reload();
                                    }
                                },"json");
                            });
                        });
                    </script>
				</div>
			</div>
		</div>
	</div>

	<%@ include file="/WEB-INF/jsp/footer.jsp" %>
</body>
</html>