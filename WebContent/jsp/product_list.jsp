<%@page import="com.itheima.store.utils.BeanFactory"%>
<%@page import="com.itheima.store.domain.Product"%>
<%@page import="com.itheima.store.service.impl.ProductServiceImpl"%>
<%@page import="com.itheima.store.service.ProductService"%>
<%@page import="com.itheima.store.utils.CookieUtils"%>
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
				width: 100%;
			}
			.carousel-inner .item img {
				width: 100%;
				height: 300px;
			}
		</style>
		<!-- <script type="text/javascript">
			$(function(){
				$(".disabled").click(function(){
					alert("都不能点了还瞎几把点啥呢点？？？")
					$(this).find("a").prop("href","");
				});
			});
		</script> -->
	</head>

	<body>
		
			<%@ include file="menu.jsp" %>


		<div class="row" style="width:1210px;margin:0 auto;">
			<div class="col-md-12">
				<ol class="breadcrumb">
					<li><a href="#">首页</a></li>
				</ol>
			</div>

			<c:forEach var="p" items="${ pageBean.list }">
			<div class="col-md-2" id="d1">
				<a href="${ pageContext.request.contextPath }/ProductServlet?method=findByPid&pid=${p.pid}">
					<img src="${ pageContext.request.contextPath }/${ p.pimage }" width="170" height="170" style="display: inline-block;">
				</a>
				<p><a href="${ pageContext.request.contextPath }/ProductServlet?method=findByPid&pid=${p.pid}" style='color:green'>${ fn:substring(p.pname,0,5) }......</a></p>
				<p><font color="#FF0000">商城价：&yen;${ p.shop_price }</font></p>
			</div>
			</c:forEach>
			

		</div>

		<!--分页 -->
		<div style="width:380px;margin:0 auto;margin-top:50px;">
			<ul class="pagination" style="text-align:center; margin-top:10px;">
			<c:if test="${ pageBean.currPage != 1 }">
				<li >
					<a href="${ pageContext.request.contextPath }/ProductServlet?method=findByCid&currPage=${ pageBean.currPage-1 }&cid=${ cid }" aria-label="Previous">
						<span aria-hidden="true">&laquo;</span>
					</a>
				</li>
			</c:if>	
			
			
			<c:if test="${ pageBean.currPage == 1 }">
				<li class="disabled">
					<a aria-label="Previous">
						<span aria-hidden="true">&laquo;</span>
					</a>
				</li>
			</c:if>	
			
			
			
				<c:forEach var="i" begin="1" end="${ pageBean.totalPage }"> 
					<c:if test="${ pageBean.currPage == i }">
						<li class="active"><a href="#">${ i }</a></li>
					</c:if>
					
					<c:if test="${ pageBean.currPage != i }">
						<li><a href="${ pageContext.request.contextPath }/ProductServlet?method=findByCid&currPage=${ i }&cid=${ cid }">${ i }</a></li>
					</c:if>
				
				</c:forEach>
				
				<c:if test="${ pageBean.currPage != pageBean.totalPage }">
				<li>
					<a href="${ pageContext.request.contextPath }/ProductServlet?method=findByCid&currPage=${ pageBean.currPage+1 }&cid=${ cid }" aria-label="Next">
						<span aria-hidden="true">&raquo;</span>
					</a>
				</li>
				</c:if>
				
				<c:if test="${ pageBean.currPage == pageBean.totalPage }">
				<li class="disabled">
					<a  aria-label="Next">
						<span aria-hidden="true">&raquo;</span>
					</a>
				</li>
				</c:if>
				
			</ul>
		</div>
		<!-- 分页结束=======================        -->

		<!--
       		商品浏览记录:
        -->
		<div style="width:1210px;margin:0 auto; padding: 0 9px;border: 1px solid #ddd;border-top: 2px solid #999;height: 246px;">

			<h4 style="width: 50%;float: left;font: 14px/30px " 微软雅黑 ";">浏览记录</h4>
			<div style="width: 50%;float: right;text-align: right;"><a href="">more</a></div>
			<div style="clear: both;"></div>

			<div style="overflow: hidden;">
				<a href="${ pageContext.request.contextPath }/ProductServlet?method=clearHistory&cid=${ cid }&currPage=${ pageBean.currPage }">清除浏览记录</a>
				<ul style="list-style: none;">
				<%  
					Cookie[] cookies = request.getCookies();
					Cookie cookie = CookieUtils.findCookie(cookies, "history");
					if(cookie == null) {
						out.print("您还没有浏览过商品!!!");
					}else {
						ProductService productService = (ProductService)BeanFactory.getBean("productService");
						String value = cookie.getValue();
						String[] ids = value.split("-");
						for(String id : ids) {
							Product p = productService.findByPid(id);
							pageContext.setAttribute("p", p);
				%>
				
				<li style="width: 150px;height: 216;float: left;margin: 0 8px 0 0;padding: 0 18px 15px;text-align: center;"><img src="${ pageContext.request.contextPath }/${p.pimage}" width="130px" height="130px" /></li>
				
				<%
						}
					}
				%>
				
				</ul>

			</div>
		</div>
		
		<%@ include file="footer.jsp" %>

	</body>

</html>