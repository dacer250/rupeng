<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
<%@ include file="/WEB-INF/jsp/header.jsp" %>
<title>添加学科</title>
</head>
<body>
	<div class="pd-20">
		<form action="<%=ctxPath %>/subject/add.do" class="form form-horizontal">
			<div class="row cl">
				<label class="form-label col-2">学科名称</label>
				<div class="formControls col-5">
					<input name="name" type="text" class="input-text" />
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
		/* 表单校验 */
		$("form").validate({
			rules:{
				name:{
					required:true
				}
			},
			messages:{
				name:{
					required:"学科名不能为空"
				}
			},
			submitHandler:function(form){
				/* 发送ajax请求,提交数据 */
				ajaxSubmitForm(form,true);
			}
		});
	});
</script>
</html>