<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>会员登录</title>
<link rel="stylesheet" href="${pageContext.request.contextPath }/css/bootstrap.min.css" type="text/css" />
<script src="${pageContext.request.contextPath }/js/jquery-1.11.3.min.js" type="text/javascript"></script>
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
</style>
<script type="text/javascript">
	function confirmOrder(){
		$("#orderForm").submit();
	}
</script>
</head>

<body>
	<!-- 引入header.jsp -->
	<jsp:include page="/WEB-INF/jsp/header.jsp"></jsp:include>
	
	<div class="container">
		<div class="row">
			<div style="margin: 0 auto; margin-top: 10px; width: 950px;">
				<strong>订单详情</strong>
				<table class="table table-bordered">
					<tbody>
						<tr class="warning">
							<th colspan="5">订单编号:${order.oid }</th>
						</tr>
						<tr class="warning">
							<th>图片</th>
							<th>商品</th>
							<th>价格</th>
							<th>数量</th>
							<th>小计</th>
						</tr>
						<c:forEach items="${order.orderItems }" var="orderitem">
							<tr class="active">
								<td width="60" width="40%"><input type="hidden" name="id"
									value="22"> <img src="/${orderitem.product.pimage }" width="70"
									height="60"></td>
								<td width="30%"><a target="_blank">${orderitem.product.pname }</a></td>
								<td width="20%">￥${orderitem.product.shopPrice }</td>
								<td width="10%">${orderitem.count }</td>
								<td width="15%"><span class="subtotal">￥${orderitem.subtotal}</span></td>
							</tr>
						</c:forEach>
						
					</tbody>
				</table>
			</div>

			<div style="text-align: right; margin-right: 120px;">
				商品金额: <strong style="color: #ff6600;">￥${order.total }元</strong>
			</div>

		</div>

		<div>
			<hr />
			<form id="orderForm" action="${pageContext.request.contextPath }/order/confirmOrder<%--${pageContext.request.contextPath }/pay--%>" method="post"  class="form-horizontal" style="margin-top: 5px; margin-left: 150px;">
				<!--传递订单oid -->
				<input type="hidden" name="oid" value="${order.oid }">
				<!--传递订单总金额  -->
				<input type="hidden" name="total" value="${order.total }">
				<div class="form-group">
					<label for="username" class="col-sm-1 control-label">地址</label>
					<div class="col-sm-5">
						<input type="text" class="form-control" id="addr" name="address" value="">
					</div>
				</div>
				<div class="form-group">
					<label for="inputPassword3" class="col-sm-1 control-label">收货人</label>
					<div class="col-sm-5">
						<input type="text" class="form-control" id="name" name="name" value="${user.username }">
					</div>
				</div>
				<div class="form-group">
					<label for="confirmpwd" class="col-sm-1 control-label">电话</label>
					<div class="col-sm-5">
						<input type="text" class="form-control" id="telephone" name="telephone" value="${user.telephone }">
					</div>
				</div>
			

				<hr />

				<div style="margin-top: 5px; margin-left: 150px;">

					<hr />
					<p style="text-align: right; margin-right: 100px;">
						<a href="javascript:void(0);" onclick="confirmOrder()">
							<img src="${pageContext.request.contextPath }/images/finalbutton.gif" width="204" height="51"
							border="0" />
						</a>
					</p>
					<hr />
	
				</div>
		</form>
		</div>

	</div>

	<!-- 引入footer.jsp -->
	<jsp:include page="/WEB-INF/jsp/footer.jsp"></jsp:include>

</body>

</html>