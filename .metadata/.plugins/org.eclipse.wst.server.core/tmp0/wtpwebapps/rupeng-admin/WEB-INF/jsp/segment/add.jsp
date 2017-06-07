<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
<%@ include file="/WEB-INF/jsp/header.jsp" %>

<title>添加段落</title>
</head>
<body>
	<div class="pd-20">

		<form action="<%=ctxPath%>/segment/add.do" class="form form-horizontal">
			<input type="hidden" name="chapterId" value="${chapterId}"/>
			<div class="row cl">
				<label class="form-label col-2">段落名称</label>
				<div class="formControls col-5">
					<input type="text" class="input-text" name="name" />
				</div>
				<div class="col-5"></div>
			</div>

			<div class="row cl">
				<label class="form-label col-2">序号</label>
				<div class="formControls col-5">
					<input type="text" class="input-text" name="seqNum" />
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
				<label class="form-label col-2">视频代码</label>
				<div class="formControls col-5">
					<textarea name="videoCode" class="textarea"></textarea>
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
				},
				seqNum:{
					required:true,
					number:true
				}
			},
			messages:{
				name:{
					required:"段落名不能为空"
				},
				seqNum:{
					required:"序号不能为空",
				}
			},
			submitHandler:function(form){
				ajaxSubmitForm(form,true);
			}
		});
	});
</script>
</html>