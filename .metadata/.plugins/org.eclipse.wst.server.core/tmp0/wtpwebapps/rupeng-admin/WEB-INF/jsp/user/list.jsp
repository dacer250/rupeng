<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
<%@ include file="/WEB-INF/jsp/header.jsp" %>

<title>学生（老师）列表</title>
</head>
<body>
	<div class="pd-20">
		<form action="<%=ctxPath%>/user/list.do">
			<input type="hidden" id="curr" name="curr" />
			<div class="text-c">
				注册日期： <input type="text" onclick="WdatePicker()" name="beginTime"
					value="${param.beginTime}" class="input-text Wdate" style="width: 120px;" /> - <input
					type="text" onclick="WdatePicker()" name="endTime" value="${param.endTime}"
					class="input-text Wdate" style="width: 120px;" /> <input
					type="text" name="param" value="${param.param}" placeholder=" 学生姓名、学校、邮箱、手机等"
					style="width: 250px" class="input-text" />
				<button class="btn btn-success" type="submit">查找</button>
			</div>
		</form>
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
							<td><fmt:formatDate value="${user.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
							<td>${user.name}</td>
							<td>${user.school}</td>
							<td>${user.email}</td>
							<td>${user.phone}</td>
							<td>
								<button class="btn size-MINI radius"
									onclick="showLayer('修改用户','<%=ctxPath%>/user/update.do?id=${user.id}')">修改</button>
								<button class="btn size-MINI radius" 
									onclick="ajaxSubmit('<%=ctxPath%>/user/toggleTeacher.do','id=${user.id}')">${user.isTeacher?'取消':''}设置老师</button>
							</td>
						</tr>					
					</c:forEach>
				</tbody>
			</table>
			<c:if test="${pageInfo.pages>1}">
				<div id="pagination" style="margin: 20px;"></div>
				<script type="text/javascript"
					src="<%=ctxPath%>/lib/laypage/1.2/laypage.js"></script>
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
	</div>
</body>
</html>