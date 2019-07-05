<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>会员登录</title>
<link rel="stylesheet" href="${pageContext.request.contextPath }/css/bootstrap.min.css" type="text/css" />
<script src="${pageContext.request.contextPath }/js/jquery-1.11.3.min.js" type="text/javascript"></script>
<!-- 引入表单校验jquery插件 -->
<script src="${pageContext.request.contextPath}/js/jquery.validate.min.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath }/js/bootstrap.min.js" type="text/javascript"></script>
<!-- 引入自定义css文件 style.css -->
<link rel="stylesheet" href="${pageContext.request.contextPath }/css/style.css" type="text/css" />

<style>
body {
	margin-top: 20px;
	margin: 0 auto;
}

.carousel-inner .item img {
	width: 100%;
	height: 300px;
}

.container .row div {
	/* position:relative;
				 float:left; */
	
}

font {
	color: #666;
	font-size: 22px;
	font-weight: normal;
	padding-right: 17px;
}
</style>
<script type="text/javascript">
	//自定义校验规则
	$.validator.addMethod(
		//校验规则的名称
		"checkPir",
		//校验函数
		function(value,element,params){
			//定义一个标识
			var flag=false;
			
			//value:输入的内容
			//element:被校验的元素对象
			//params:规则对应的参数值
			//目的：对输入的username进行ajax校验
			$.ajax({
				"async":false,
				"url":"${pageContext.request.contextPath}/checkImg/checkCode",
				"data":{"checkCode":value},
				"type":"POST",
				"dataType":"json",
				"success":function(data){
					flag = data.ifSame;
					
				}
			});
		
			//返回false代表校验器不通过
			return flag;
		}
	)

	$(function(){
		$("#myform").validate({
			rules:{
				"username":{
					"required":true,
				},
				"password":{
					"required":true,
				},
				"checkCode":{
					"required":true,
					"checkPir":true
				}
			},
			messages:{
				"username":{
					"required":"用户名不能为空",
				},
				"password":{
					"required":"密码不能为空",
				},
				"checkCode":{
					"required":"验证码不能为空",
					"checkPir":"验证码错误"
				}
			}
		});
	})


	function reloadPir(){
		document.getElementById("CreateCheckCode").src = document  
	    .getElementById("CreateCheckCode").src  
	    + "?nocache=" + new Date().getTime();  
	}
</script>
</head>
<body>

	<!-- 引入header.jsp -->
	<jsp:include page="/WEB-INF/jsp/header.jsp"></jsp:include>


	<div class="container"
		style="width: 100%; height: 460px; background: #FF2C4C url('${pageContext.request.contextPath }/images/loginbg.jpg') no-repeat;">
		<div class="row">
			<div class="col-md-7">
				<!--<img src="./image/login.jpg" width="500" height="330" alt="会员登录" title="会员登录">-->
			</div>

			<div class="col-md-5">
				<div
					style="width: 440px; border: 1px solid #E7E7E7; padding: 20px 0 20px 30px; border-radius: 5px; margin-top: 60px; background: #fff;">
					<font>会员登录</font>USER LOGIN
					<div><%=request.getAttribute("loginInfo")==null?"":request.getAttribute("loginInfo")%></div>
					<form id="myform" class="form-horizontal" method="post" action="${pageContext.request.contextPath }/user/login">
						<div class="form-group">
							<label for="username" class="col-sm-2 control-label">用户名</label>
							<div class="col-sm-6">
								<input type="text" class="form-control" id="username" name="username"
									placeholder="请输入用户名">
							</div>
						</div>
						<div class="form-group">
							<label for="inputPassword3" class="col-sm-2 control-label">密码</label>
							<div class="col-sm-6">
								<input type="password" class="form-control" id="inputPassword3" name="password"
									placeholder="请输入密码">
							</div>
						</div>
						<div class="form-group">
							<label for="checkCode" class="col-sm-2 control-label">验证码</label>
							<div class="col-sm-3">
								<input type="text" class="form-control" id="checkCode" name="checkCode"
									placeholder="请输入验证码">
							</div>
							<div class="col-sm-3">
								<img id="CreateCheckCode" src="${pageContext.request.contextPath }/checkImg"/><a href="javascript:void();" onclick="reloadPir()">换一张</a>
							</div>
						</div>
						<div class="form-group">
							<div class="col-sm-offset-2 col-sm-10">
								<div class="checkbox">
									<label> <input type="checkbox" name="autoLogin" value="autoLogin"> 自动登录
									</label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <label> <input
										type="checkbox"> 记住用户名
									</label>
								</div>
							</div>
						</div>
						<div class="form-group">
							<div class="col-sm-offset-2 col-sm-10">
								<input type="submit" width="100" value="登录" name="submit"
									style="background: url('${pageContext.request.contextPath }/images/login.gif') no-repeat scroll 0 0 rgba(0, 0, 0, 0); height: 35px; width: 100px; color: white;">
							</div>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>

	<!-- 引入footer.jsp -->
	<jsp:include page="/WEB-INF/jsp/footer.jsp"></jsp:include>

</body>
</html>