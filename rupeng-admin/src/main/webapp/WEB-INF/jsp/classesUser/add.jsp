<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
<%@ include file="/WEB-INF/jsp/header.jsp" %>

<title>添加班级成员</title>
</head>
<body>
	<div class="pd-20">
		<div class="text-c">
			<form action="<%=ctxPath%>/classesUser/add.do" method="get">
				<input type="hidden" id="curr" name="curr" /> 
				<input type="hidden" id="classesId" name="classesId" value="${classesId}"/> 
				<input type="text"
					name="param" value="${param.param}" placeholder=" 学生姓名、学校、邮箱、手机等"
					style="width: 250px" class="input-text" />
				<button class="btn btn-success radius" type="submit">查找</button>
				<input class="btn btn-default radius" type="button" value="关闭"
					onclick="parent.location.reload()" style="margin-left: 30px;" />
			</form>
		</div>
		<div class="mt-20">
			<table class="table table-border table-bordered table-bg table-hover">
				<thead>
					<tr>
						<th>注册时间</th>
						<th>姓名</th>
						<th>毕业院校</th>
						<th>邮箱</th>
						<th>手机</th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${pageInfo.list}" var="user">
						<tr>
							<td><fmt:formatDate value="${user.createTime}"/></td>
							<td>${user.name}</td>
							<td>${user.school}</td>
							<td>${user.email}</td>
							<td>${user.phone}</td>
							<td>
								<button class="btn size-MINI radius" onclick="ajaxSubmit('<%=ctxPath%>/classesUser/add.do','classesId=${classesId}&userId=${user.id}')">添加到班级</button>
							</td>
						</tr>					
					</c:forEach>
				</tbody>
			</table>
			<c:if test="${pageInfo.pages > 1}">
				<div id="pagination" style="margin: 10px;"></div>
				<script type="text/javascript"
					src="<%=ctxPath%>/lib/laypage/1.2/laypage.js"></script>
				<script type="text/javascript">
	                laypage({
	                    cont: 'pagination',
	                    pages:${pageInfo.pages},
	                    curr: ${pageInfo.pageNum},
	                    jump:function(obj,first){
	                        if(!first){ //一定要加此判断，否则初始时会无限刷新
	                            $("#curr").val(obj.curr);
	                            $("form").submit();
	                          }
	                    }
	                  });
	            </script>
            </c:if>
		</div>
	</div>
</body>
</html>