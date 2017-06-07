<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
<%@ include file="/WEB-INF/jsp/header.jsp" %>

<title>添加学习卡</title>
</head>
<body>
	<div class="pd-20">
		<form action="<%=ctxPath%>/card/add.do" class="form form-horizontal">

			<div class="row cl">
				<label class="form-label col-2">学习卡名称</label>
				<div class="formControls col-5">
					<input type="text" class="input-text" name="name" />
				</div>
				<div class="col-5"></div>
			</div>

			<div class="row cl">
				<label class="form-label col-2">所属学科</label>
				<div class="formControls col-5">
					<c:forEach items="${subjects}" var="subject">
						<input id="subject${subject.id}" type="checkbox" name="subjectIds" value="${subject.id}" />
						<label for="subject${subject.id}" style="margin-right: 10px;">${subject.name}</label> 
					</c:forEach>
				</div>
				<div class="col-5"></div>
			</div>

			<div class="row cl">
				<label class="form-label col-2">描述</label>
				<div class="formControls col-5">
					<textarea name="description" class="textarea"></textarea>
				</div>
				<div class="col-5"></div>
			</div>

			<div class="row cl">
				<label class="form-label col-2">课件下载地址</label>
				<div class="formControls col-5">
					<input type="text" class="input-text" name="courseware" />
				</div>
				<div class="col-5"></div>
			</div>

			<div class="row cl">
				<div class="col-9 col-offset-2">
					<input class="btn btn-primary radius" type="submit" value="添加" />
					<input class="btn btn-default radius" type="button" value="关闭"
						onclick="parent.location.reload()" style="margin-left: 30px;" />
				</div>
			</div>
		</form>
	</div>
</body>
<script type="text/javascript">
	$(function(){
		$("form").validate({
			rules:{
				name:{
					required:true
				}
			},
			messages:{
				name:{
					required:"学习卡名称不能为空"
				}
			},
			submitHandler:function(form){
				ajaxSubmitForm(form,true);
			}
		});
	});
</script>
</html>