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
				
				//异步添加商品数量
				$(".sbw").change(function(){
					var nameid = $(this).prop("name");
					$.post(
							
						"${ pageContext.request.contextPath }/CartServlet",
						{
							"method":"add",
							"count":$(this).val(),
							"pid":$(this).prop("name")
						},
						function(data){
							//alert(data.subtotal)
							//$(this).parent(".active").find(".td1").text("￥"+data.subtotal+".0");
							//$(".td1").text("￥"+data.subtotal+".0");
							$("span[id='"+nameid+"']").text("￥"+data.subtotal+".0");
							$("#e1").text(data.total);
							$("#st1").text("￥"+data.total+".0元");
						},
					"json");
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
				
				//添加一个商品数量
				$(".sbw1").click(function(){
					var tname = $(this).prop("id");
					var count = $("textarea[name='"+tname+"']").val();
					var pid = $(this).prop("id");
					$.post(
							"${ pageContext.request.contextPath }/CartServlet",
							{
								"method":"addOne",
								"count":count,
								"pid":pid
							},
							function(data){
								
								$("span[id='"+pid+"']").text("￥"+data.subtotal+".0");
								$("#e1").text(data.total);
								$("#st1").text("￥"+data.total+".0元");
								$("textarea[name='"+tname+"']").val(data.count)
							}
					,"json");
					
				});
				//减少一个商品数量
				$(".sbw2").click(function(){
					var tname = $(this).prop("id");
					var count = $("textarea[name='"+tname+"']").val();
					var pid = $(this).prop("id");
					$.post(
							"${ pageContext.request.contextPath }/CartServlet",
							{
								"method":"removeOne",
								"count":count,
								"pid":pid
							},
							function(data){
								
								$("span[id='"+pid+"']").text("￥"+data.subtotal+".0");
								$("#e1").text(data.total);
								$("#st1").text("￥"+data.total+".0元");
								$("textarea[name='"+tname+"']").val(data.count)
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
								<th><input type="checkbox"></th>
								<th>图片</th>
								<th>商品</th>
								<th>价格</th>
								<th>数量</th>
								<th>小计</th>
								<th>操作</th>
							</tr>
							
							<c:forEach var="entry" items="${ cart.map }">
							<tr class="active">
								<td><input type="checkbox"></td>
								<td width="60" width="40%">
									<img src="${ pageContext.request.contextPath }/${ entry.value.product.pimage }" width="70" height="60">
								</td>
								<td width="30%">
									<a target="_blank">${ entry.value.product.pname }</a>
								</td>
								<td width="20%">
									￥${ entry.value.product.shop_price }
								</td>
								<td width="10%">
								
<form class="form-inline">
 		<div class="form-group">
    	<label class="sr-only" for="exampleInputAmount"></label>
	    <div class="input-group">
	      	<div class="input-group-addon"><input type="button" id="${ entry.value.product.pid }" name="${ entry.value.count }" class="sbw1" value="+"/></div>
	      	<!-- <input type="text" class="form-control" id="exampleInputAmount" placeholder="Amount" > -->
 	<textarea  rows="1" cols="1" name="${ entry.value.product.pid }" id="pcount"  class="sbw">${ entry.value.count }</textarea>
    			<div class="input-group-addon"><input type="button" id="${ entry.value.product.pid }" name="${ entry.value.count }" class="sbw2" value="-"/></div>
    	</div>
 		</div>
</form>
									
									<%-- <textarea  rows="1" cols="1" name="${ entry.value.product.pid }" id="pcount"  class="sbw">${ entry.value.count }</textarea> --%>
									
									
								</td>
								<td width="15%" class="td1">
									<span class="subtotal" id="${ entry.value.product.pid }">￥${ entry.value.subtotal }</span> 
									
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
						<input type="button" width="100" value="将选中的商品提交到订单" class="asyncLogin" border="0">
					
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