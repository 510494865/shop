<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
<!DOCTYPE html>
<script src="${pageContext.request.contextPath }/js/e3mall.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath }/js/jquery.cookie.js" type="text/javascript"></script>
<!-- 登录 注册 购物车... -->
<div class="container-fluid">
	<div class="col-md-4">
		<img src="${pageContext.request.contextPath }/images/ld.jpg" />
	</div>
	<div class="col-md-5">
		<img src="${pageContext.request.contextPath }/img/header.png" />
	</div>
	<div class="col-md-3" style="padding-top:20px">
		<ol class="list-inline">
					<li>
						<span id="loginbar" >
							<a href="${pageContext.request.contextPath }/login">登录</a>
							<a href="${pageContext.request.contextPath }/register">注册</a>
						</span>
					</li>
					
				
			
			<%-- <c:if test="${!empty user }">
				<li style="color:red">欢迎您，${user.username }</li>
				<li><a href="${pageContext.request.contextPath }/user/logout">退出</a></li>
			</c:if> --%>
			
			<li><a href="${pageContext.request.contextPath }/cart/cartList">购物车</a></li>
			<li><a href="${pageContext.request.contextPath }/order/myOrders">我的订单</a></li>
		</ol>
	</div>
</div>

<!-- 导航条 -->
<div class="container-fluid">
	<nav class="navbar navbar-inverse">
		<div class="container-fluid">
			<!-- Brand and toggle get grouped for better mobile display -->
			<div class="navbar-header">
				<button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
					<span class="sr-only">Toggle navigation</span>
					<span class="icon-bar"></span>
					<span class="icon-bar"></span>
					<span class="icon-bar"></span>
				</button>
				<a class="navbar-brand" href="${pageContext.request.contextPath }">首页</a>
			</div>

			<div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
				<ul class="nav navbar-nav" id="category">
					<%-- <c:forEach items="${categoryList }" var="category">
						<li><a href="#">${category.cname }</a></li>
					</c:forEach> --%>
					<!-- <li class="active"><a href="product_list.htm">手机数码<span class="sr-only">(current)</span></a></li>
					<li><a href="#">电脑办公</a></li>
					<li><a href="#">电脑办公</a></li>
					<li><a href="#">电脑办公</a></li> -->
				</ul>
				<!-- <form class="navbar-form navbar-right" role="search">
					<div class="form-group">
						<input type="text" class="form-control" placeholder="Search">
					</div>
					<button type="submit" class="btn btn-default">Submit</button>
				</form> -->
				
				<form class="navbar-form navbar-right" role="search" action="${pageContext.request.contextPath }/product/productListByPname" method="post">
					<div  class="form-group" style="position: relative;">
						<input id="search" type="text" name="search" class="form-control" placeholder="Search" onkeyup="searchWord(this)">
						<div id="showDiv" style=" display:none; position:absolute;z-index:1000;background:#fff; width: 173px;border: 1px solid red; " ></div>
					</div>
					<button type="submit" class="btn btn-default">Submit</button>
				</form>
			</div>
		</div>
		<script type="text/javascript">
			$(function(){
				var content="";
				$.post(
					"${pageContext.request.contextPath}/category/list",
					function(data){
						//动态创建<li><a href="#">${category.cname }</a></li>
						for(var i=0;i<data.length;i++){
							content+="<li><a href='${pageContext.request.contextPath}/product/productListByCid?cid="+data[i].cid+"'>"+data[i].cname+"</a></li>";
						}
						$("#category").html(content);
					},
					"json"
				);
			});
			
			function overFn(obj){
				$(obj).css("background","#DBEAF9");
				
			} 
			function outFn(obj){
				$(obj).css("background","#fff");
			}
			function clickFn(obj){
				$("#search").val($(obj).html());
				$("#showDiv").css("display","none");
			}
			function searchWord(obj){
				//1,获取输入框的内容
				var word=$(obj).val();
				//2,根据输入框的内容去数据库中模糊查询-----List<Product>
				var content="";
				$.post(
					"${pageContext.request.contextPath}/product/searchWord",
					{"word":word},
					function(data){	
						//3,将返回的商品的名称显示在showDiv
						for(var i=0;i<data.length;i++){
							content+="<div style='padding:5px;cursor:pointer' onclick='clickFn(this)' onmouseover='overFn(this)' onmouseout='outFn(this)'>"+data[i]+"</div>";
						}
						if(data.length>0){
							$("#showDiv").html(content);
							$("#showDiv").css("display","block");
						}
					},
					"json"
				);
			}
		</script>
	</nav>
</div>