<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!doctype html>
<html>

	<head>
		<meta charset="utf-8" />
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<title>会员登录</title>
		<link rel="stylesheet" href="${ pageContext.request.contextPath }/css/bootstrap.min.css" type="text/css" />
		<script src="${ pageContext.request.contextPath }/js/jquery-1.11.3.min.js" type="text/javascript"></script>
		<script src="${ pageContext.request.contextPath }/js/bootstrap.min.js" type="text/javascript"></script>
		<!-- 引入自定义css文件 style.css -->
		<link rel="stylesheet" href="${ pageContext.request.contextPath }/css/style.css" type="text/css" />
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
				color: #3164af;
				font-size: 18px;
				font-weight: normal;
				padding: 0 10px;
			}
		</style>
		<script type="text/javascript">
			function del(pid){
				var flag = confirm("您确定删除吗？");
				if(flag) {
					window.location.href="${ pageContext.request.contextPath }/CartServlet?method=removeCart&pid="+pid;
				}
			}
		</script>
		<!-- 购物车模态框异步登录的js -->
		<script type="text/javascript">
			$(function(){
				$("#inLogin").click(function(){
					$("#myModal1").modal("show");
				});
				$("#asyncLogin1").click(function(){
					$.post(
						"${ pageContext.request.contextPath }/UserServlet",
						{
							"method":"asyncLogin",
							"username":$("#username1").val(),
							"password":$("#password1").val()
						},
						function(data){
							if(data.flag) {
								$("#myModal1").modal("hide");
								location.reload();
							}else {
								$("#asyncLoginMessage1").html(data.message).fadeIn();
							}
						} 
						,"json");
				});
				
				//提交订单的异步登录
				$(".asyncLogin").click(function(){
					var isLogin = "${ not empty existUser }";
					if(isLogin==null||isLogin==""||isLogin=="false"){
						//未登录
						$("#myModal").modal('show');
						return ;
					}
					
					saveOrder();
				});
				
				$("#asyncLogin").bind("click",function(){
					$.post(
						"${pageContext.request.contextPath}/UserServlet",
						{
							"method":"asyncLogin",
							"username":$("#username").val(),
							"password":$("#password").val()
						},
						function(data){
							if(data.flag){
								//$("#myModal").modal('hide');
								saveOrder();
							}else{
								$("#asyncLoginMessage").html(data.message).fadeIn();
							}	
						},
					"json")
				});
				
				function saveOrder(){
					location.href="${ pageContext.request.contextPath }/OrderServlet?method=saveOrder";
				}
				
				//手动在文本框输入改变商品数量
				$("input[name='count']").blur(function(){
					var thisElement = $(this);
					//获得当前输入的值,判断输入是否合法
					var count = thisElement.val();
					//判断是否为数字的正则表达式
					var r = /^\+?[1-9][0-9]*$/;
					if(!r.test(count)) {
						location.reload();
						return;
					}
					//是数字
					var pid = thisElement.prop("id");
					$.post(
						"${ pageContext.request.contextPath }/CartServlet",
						{
							"method":"blurCount",
							"pid":pid,
							"count":count
						},
						function(data){
							//刷新当前购物项的小计
							thisElement.parent().next().children().text("￥"+data.subtotal+".0");
							//$(this).parent().next().children().html(data.subtotal);
							$("#st1").html("￥"+data.total+".0元");
							$("#e1").text(data.total+".0");
						}
					,"json");
				});
				
				//点击"+"添加商品数量
				$("input[name='addOne']").click(function(){
					var thisElement = $(this);
					//获得当前商品的id
					var pid = thisElement.prop("id");
					//获得文本框的值-->prev()取得一个包含匹配的元素集合中每一个元素紧邻的前一个同辈元素的元素集合
					var count = thisElement.prev().val();
					//文本框数字加一-->Number操作数字******
					thisElement.prev().val(Number(count)+1);
					$.post(
						"${ pageContext.request.contextPath }/CartServlet",
						{
							"method":"addOne",
							"pid":pid,
							"count":count
						},
						function(data){
							//刷新当前购物项的小计
							thisElement.parent().next().children().text("￥"+data.subtotal+".0");
							//$(this).parent().next().children().html(data.subtotal);
							$("#st1").html("￥"+data.total+".0元");
							$("#e1").text(data.total+".0");
						}
					,"json");
				});
				
				//点击"-"减少商品
				$("input[name='removeOne']").click(function(){
					var thisElement = $(this);
					//获得当前商品的id
					var pid = thisElement.prop("id");
					//获得文本框的值
					var count = thisElement.next().val();
					//先判断数字是否小于等于1
					if(count <= 1) {
						return;
					}
					//文本框数字加一-->Number操作数字******
					thisElement.next().val(Number(count)-1);
					$.post(
						"${ pageContext.request.contextPath }/CartServlet",
						{
							"method":"removeOne",
							"pid":pid,
							"count":count
						},
						function(data){
							//刷新当前购物项的小计
							thisElement.parent().next().children().text("￥"+data.subtotal+".0");
							//$(this).parent().next().children().html(data.subtotal);
							$("#st1").html("￥"+data.total+".0元");
							$("#e1").text(data.total+".0");
						}
					,"json");
				});
			});
		</script>
	</head>

	<body>
		
		
			<%@ include file="menu.jsp" %>

		<c:if test="${ fn:length(cart.map)==0 }">
		<center>
			<h1>购物车空空的哦~，去看看心仪的商品吧~ </h1>
			<img src="${ pageContext.request.contextPath }/image/xx.jpg" />
		</center>
		</c:if>
		<c:if test="${ fn:length(cart.map)!=0 }">
			
		<div class="container">
			<div class="row">
			
				<div style="margin:0 auto; margin-top:10px;width:950px;">
					<strong style="font-size:16px;margin:5px 0;">购物车</strong>
					<table class="table table-bordered">
						<tbody>
							<tr class="warning">
								<th>图片</th>
								<th>商品</th>
								<th>价格</th>
								<th>数量</th>
								<th>小计</th>
								<th>操作</th>
							</tr>
							
							<c:forEach var="entry" items="${ cart.map }">
							<tr class="active">
								<td width="60" width="40%">
									<img src="${ pageContext.request.contextPath }/${ entry.value.product.pimage }" width="70" height="60">
								</td>
								<td width="30%">
									<a target="_blank">${ entry.value.product.pname }</a>
								</td>
								<td width="20%">
									￥${ entry.value.product.shop_price }
								</td>
								<td width="20%" style="padding:30px">
								<!-- 商品的数量 的操作地方-->
									<input id="${ entry.value.product.pid }" type="button" name="removeOne"  class="btn btn-info" value="-" style="display:inline" />
									<input id="${ entry.value.product.pid }" type="text"   name="count"  value="${ entry.value.count }" style="width:50px;display:inline;text-align:center"/>
									<input id="${ entry.value.product.pid }" type="button" name="addOne" class="btn btn-info" value="+" style="display:inline" />
								</td>
								<td width="15%">
									<span class="subtotal">￥${ entry.value.subtotal }</span>
								</td>
								<td>
									<a href="#" onclick="del(${ entry.value.product.pid })" class="delete">删除</a>
								</td>
							</tr>
							
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>

			<div style="margin-right:130px;">
				<div style="text-align:right;">
					<em style="color:#ff6600;">
					<c:if test="${ empty existUser }">
						您还未登录!登录后购物车信息会保存到你的的账号<input type="button" class="btn btn-primary btn-md" value="登录哟" id="inLogin">
					</c:if>
					<c:if test="${ not empty existUser }">
						登录成功享有优惠哟！！
					</c:if>
			</em> 赠送积分: <em id="e1" style="color:#ff6600;">${ cart.total }</em>&nbsp; 商品金额: <strong id="st1" style="color:#ff6600;">￥${ cart.total }元</strong>
				
				</div>
				<div style="text-align:right;margin-top:10px;margin-bottom:10px;">
					<a href="${ pageContext.request.contextPath }/CartServlet?method=clearCart" id="clear" class="clear">清空购物车</a>
					<%-- <a href="order_info.htm">
						 <input type="submit" width="100" value="提交订单" name="submit" border="0" style="background: url('${ pageContext.request.contextPath }/images/register.gif') no-repeat scroll 0 0 rgba(0, 0, 0, 0);
						height:35px;width:100px;color:white;">
						</a> --%>
						<input type="button" width="100" value="提交订单" class="asyncLogin" border="0" style="background: url('${ pageContext.request.contextPath }/images/register.gif') no-repeat scroll 0 0 rgba(0, 0, 0, 0);
						height:35px;width:100px;color:white;">
					
				</div>
			</div>

		</div>
</c:if>
		<%@ include file="footer.jsp" %>

	</body>
	
	<!-- 点击登陆弹出模态框开始 -->
	<div class="modal fade" tabindex="-1" role="dialog" id="myModal1" aria-labelledby="gridSystemModalLabel">
	  <div class="modal-dialog" role="document">
	    <div class="modal-content">
	      <div class="modal-header">
	      	<!-- 模态框头 -->
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        <h4 class="modal-title" id="gridSystemModalLabel">会员登陆 </h4>
	        <div id="asyncLoginMessage1"class="alert alert-danger" role="alert" style="display:none"></div>
	      </div>
	      <div class="modal-body">
	      	<!-- 模态框体 -->
	      		<form class="form-horizontal">
				  <div class="form-group">
				    <label for="inputEmail3" class="col-sm-2 control-label">用户名:</label>
				    <div class="col-sm-10">
				      <input type="text" class="form-control" id="username1" placeholder="请输入用户名">
				    </div>
				  </div>
				  <div class="form-group">
				    <label for="inputPassword3" class="col-sm-2 control-label">密码:</label>
				    <div class="col-sm-10">
				      <input type="password" class="form-control" id="password1" placeholder="请输入密码">
				    </div>
				  </div>
				</form>
	      </div>
	      <div class="modal-footer">
	      	<!-- 模态框脚 -->
	        <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
	        <button type="button" class="btn btn-primary" id="asyncLogin1">登陆</button>
	      </div>
	    </div><!-- /.modal-content -->
	  </div><!-- /.modal-dialog -->
	</div><!-- /.modal -->
	<!--  -->
	<!-- 点击登陆弹出模态框结束 -->
	
	<!-- 点击提交订单登陆模态框部位开始 -->
	<div class="modal fade" tabindex="-1" role="dialog" id="myModal" aria-labelledby="gridSystemModalLabel">
	  <div class="modal-dialog" role="document">
	    <div class="modal-content">
	      <div class="modal-header">
	      	<!-- 模态框头 -->
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        <h4 class="modal-title" id="gridSystemModalLabel">会员登陆 </h4>
	        <div id="asyncLoginMessage"class="alert alert-danger" role="alert" style="display:none"></div>
	      </div>
	      <div class="modal-body">
	      	<!-- 模态框体 -->
	      		<form class="form-horizontal">
				  <div class="form-group">
				    <label for="inputEmail3" class="col-sm-2 control-label">用户名:</label>
				    <div class="col-sm-10">
				      <input type="text" class="form-control" id="username" placeholder="请输入用户名">
				    </div>
				  </div>
				  <div class="form-group">
				    <label for="inputPassword3" class="col-sm-2 control-label">密码:</label>
				    <div class="col-sm-10">
				      <input type="password" class="form-control" id="password" placeholder="请输入密码">
				    </div>
				  </div>
				</form>
	      </div>
	      <div class="modal-footer">
	      	<!-- 模态框脚 -->
	        <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
	        <button type="button" class="btn btn-primary" id="asyncLogin">登陆</button>
	      </div>
	    </div><!-- /.modal-content -->
	  </div><!-- /.modal-dialog -->
	</div><!-- /.modal -->
	<!-- 点击提交订单登陆模态框部位结束 -->

</html>