<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<HTML>
	<HEAD>
		<meta http-equiv="Content-Language" content="zh-cn">
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<link rel="stylesheet" href="${ pageContext.request.contextPath }/css/bootstrap.min.css" type="text/css" />
		<link href="${pageContext.request.contextPath}/css/Style1.css" rel="stylesheet" type="text/css" />
		<script language="javascript" src="${pageContext.request.contextPath}/js/public.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.11.3.min.js"></script>
		<script src="${ pageContext.request.contextPath }/js/bootstrap.min.js" type="text/javascript"></script>
		
		<script type="text/javascript">
			/* function showDetail(oid) {
				var vals = $("#but"+oid).val();
				if(vals == "订单详情") {
					$.post(
						"${pageContext.request.contextPath}/AdminOrderServlet",
						{
							"method":"showDetail",
							"oid":oid
						},
						function(data) {
							$(data).each(function(i,n){
								$("#div"+oid).append("<img width='60' height='65' src='${pageContext.request.contextPath}/"+n.product.pimage+"'>&nbsp;"+n.product.pname+"&nbsp;"+n.product.shop_price+"<br/>");
							});
							
						}
					,"json");
					$("#but"+oid).val("关闭");
				}else{
					$("#div"+oid).html("");
					$("#but"+oid).val("订单详情");
				}
			} */
			
			function showDetail(oid){
				$(".orderItemTable tbody").html("");
				$("#orderNo").html("");
				$.post("${pageContext.request.contextPath}/AdminOrderServlet",{"method":"showDetail","oid":oid},function(data){
					$(data).each(function(i,n){
						// 显示图片,名称,单价,数量 [] {} 
						//[{"itemid":"a","count":"1","subtotal":"10","product":{"pid":"","pname":}},{}]
						$(".orderItemTable tbody").append("<tr><td>"+i+"</td><td><img width='60' height='65' src='${pageContext.request.contextPath}/"+n.product.pimage+"'></td><td>"+n.product.pname+"</td><td>"+n.product.shop_price+"</td><td>"+n.count+"</td></tr>");
					});
				},"json");
				$("#orderNo").html(oid);
				$("#orderItemModal").modal("show");
			}
			
		</script>
	</HEAD>
	<body>
		<br>
		<form id="Form1" name="Form1" action="${pageContext.request.contextPath}/user/list.jsp" method="post">
			<table cellSpacing="1" cellPadding="0" width="100%" align="center" bgColor="#f5fafe" border="0">
				<TBODY>
					<tr>
						<td class="ta_01" align="center" bgColor="#afd1f3">
							<strong>订单列表</strong>
						</TD>
					</tr>
					
					<tr>
						<td class="ta_01" align="center" bgColor="#f5fafe">
							<table cellspacing="0" cellpadding="1" rules="all"
								bordercolor="gray" border="1" id="DataGrid1"
								style="BORDER-RIGHT: gray 1px solid; BORDER-TOP: gray 1px solid; BORDER-LEFT: gray 1px solid; WIDTH: 100%; WORD-BREAK: break-all; BORDER-BOTTOM: gray 1px solid; BORDER-COLLAPSE: collapse; BACKGROUND-COLOR: #f5fafe; WORD-WRAP: break-word">
								<tr
									style="FONT-WEIGHT: bold; FONT-SIZE: 12pt; HEIGHT: 25px; BACKGROUND-COLOR: #afd1f3">

									<td align="center" width="10%">
										序号
									</td>
									<td align="center" width="10%">
										订单编号
									</td>
									<td align="center" width="10%">
										订单金额
									</td>
									<td align="center" width="10%">
										收货人
									</td>
									<td align="center" width="10%">
										订单状态
									</td>
									<td align="center" width="50%">
										订单详情
									</td>
								</tr>
								<c:forEach var="order" items="${ list }" varStatus="status">
										<tr onmouseover="this.style.backgroundColor = 'white'"
											onmouseout="this.style.backgroundColor = '#F5FAFE';">
											<td style="CURSOR: hand; HEIGHT: 22px" align="center"
												width="18%">
												${ status.count }
											</td>
											<td style="CURSOR: hand; HEIGHT: 22px" align="center"
												width="17%">
												${ order.oid }
											</td>
											<td style="CURSOR: hand; HEIGHT: 22px" align="center"
												width="17%">
												${ order.total }
											</td>
											<td style="CURSOR: hand; HEIGHT: 22px" align="center"
												width="17%">
												${ order.name }
											</td>
											<td style="CURSOR: hand; HEIGHT: 22px" align="center"
												width="17%">
												<c:if test="${ order.state == 1 }">
													未付款
												</c:if>
												<c:if test="${ order.state == 2 }">
													<a href="${ pageContext.request.contextPath }/AdminOrderServlet?method=send&oid=${ order.oid }">发货</a>
												</c:if>
												<c:if test="${ order.state == 3 }">
													已发货
												</c:if>
												<c:if test="${ order.state == 4 }">
													订单结束
												</c:if>
											
											</td>
											<td align="center" style="HEIGHT: 22px">
												<input type="button" value="订单详情" id="but${ order.oid }" onclick="showDetail('${ order.oid }')"/>
												<div id="div${ order.oid }">
													
												</div>
											</td>
							
										</tr>
										</c:forEach>
							</table>
						</td>
					</tr>
					<tr align="center">
						
					</tr>
				</TBODY>
			</table>
		</form>
	</body>
	
	<div class="modal fade" tabindex="-1" role="dialog" id="orderItemModal">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title">订单号:<span id="orderNo"></span></h4>
      </div>
      <div class="modal-body">
	      <table class="table table-bordered table-hover table-condensed orderItemTable">
	      	<thead>
	      		<!-- 显示图片,名称,单价,数量 -->
	      		<tr class="success">
	      			<td>序号</td>
	      			<td>图片</td>
	      			<td>名称</td>
	      			<td>单价</td>
	      			<td>数量</td>
	      		</tr>
	      	</thead>
	      	<tbody>
	      		
	      	</tbody>
		  </table>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
        <button type="button" class="btn btn-primary">Save changes</button>
      </div>
    </div><!-- /.modal-content -->
  </div><!-- /.modal-dialog -->
</div><!-- /.modal -->
</HTML>

