<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/WEB-INF/jsp/header.jsp" %>
<title>问题列表</title>
</head>
<body>
	<%@ include file="/WEB-INF/jsp/nav.jsp" %>

	<div id="mainDiv" class="container" style="margin-top: 35px;">
	<style>
		form lable {
			margin-right: 50px;
		}
		
		.panel-body span {
			margin-right: 30px;
		}					
	</style>
	<script type="text/javascript">
		$(function(){
			$("[name=condition]").click(function(){
				$("form").submit();				
			});
		});
	</script>
		<form action="<%=ctxPath %>/question/list.do" method="post">
			<input type="hidden" id="curr" name="pageNum" /> 
			<label class="checkbox-inline"><input type="radio" name="condition" value="allUnresolved"
				<c:if test="${condition eq 'allUnresolved' }">checked="checked"</c:if>
			 />解决中</label> 
			<label class="checkbox-inline"><input type="radio" name="condition" value="allResolved"
				<c:if test="${condition eq 'allResolved' }">checked="checked"</c:if>
			 />已解决</label> 
			<label class="checkbox-inline"><input type="radio" name="condition" value="myAsked" 
				<c:if test="${condition eq 'myAsked' }">checked="checked"</c:if>
			 />我提问的问题</label> 
			<label class="checkbox-inline"><input type="radio" name="condition" value="myAnswered"
				<c:if test="${condition eq 'myAnswered' }">checked="checked"</c:if>
			 />我回复的问题</label>
		</form>
		<div class="panel-group" style="margin-top: 20px;">
		  <c:forEach items="${pageInfo.list }" var="question">
			<div class="panel panel-default">
				<div class="panel-body">
					<a href="<%=ctxPath%>/question/detail.do?id=${question.id}" target="_blank"> 
						<span>${question.username }</span> 
						<span><fmt:formatDate value="${question.createTime }" pattern="yyyy-MM-dd HH:mm:ss"/></span> 
						<span>${question.courseInfo }</span>
					</a>
				</div>
			</div>
		  </c:forEach>
		</div>
		<c:if test="${pageInfo.pages>1 }">
            <div id="pagination" style="margin: 20px;"></div>
            <script type="text/javascript" src="<%=ctxPath %>/lib/laypage/1.2/laypage.js"></script>
            <script type="text/javascript">
                laypage({
                    cont:'pagination',
                    pages:${pageInfo.pages},
                    curr: ${pageInfo.pageNum},
                    jump:function(obj,first){
                        if(!first){
                            $("#curr").val(obj.curr);
                            $("form").submit();
                        }
                    }
                });
            </script>
        </c:if>
	</div>

	<%@ include file="/WEB-INF/jsp/footer.jsp" %>
</body>
</html>