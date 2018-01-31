package com.itheima.store.web.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import com.itheima.store.domain.Cart;
import com.itheima.store.domain.CartItem;
import com.itheima.store.domain.Product;
import com.itheima.store.service.ProductService;
import com.itheima.store.utils.BaseServlet;
import com.itheima.store.utils.BeanFactory;

/**
 * 购物车的Servlet
 * @author 趴布里克
 *
 */
public class CartServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * 将商品加入购物车的方法
	 * @param req
	 * @param resp
	 * @return
	 */
	
	
	public String addCart(HttpServletRequest req, HttpServletResponse resp) {
		try{
			//接收参数
			String pid = req.getParameter("pid");
			Integer count = Integer.parseInt(req.getParameter("count"));
			//封装CartItem
			CartItem cartItem = new CartItem();
			cartItem.setCount(count);
			ProductService productService = (ProductService) BeanFactory.getBean("productService");
			Product product = productService.findByPid(pid);
			cartItem.setProduct(product);
			
			//获得购物车
			Cart cart = getCart(req);
			//调用添加到购物车的方法
			cart.addCart(cartItem);
			//页面跳转
			resp.sendRedirect(req.getContextPath()+"/jsp/cart.jsp");
			
			
			return null;
		}catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	/**
	 * 清空购物车的方法
	 * @param req
	 * @param resp
	 * @return
	 */
	public String clearCart(HttpServletRequest req, HttpServletResponse resp) {
		
		try {
			Cart cart = getCart(req);
			//调用Cart的清空购物车的方法
			cart.clearCart();
			//页面跳转
			resp.sendRedirect(req.getContextPath()+"/jsp/cart.jsp");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 删除购物车中购物项的方法
	 * @param req
	 * @param resp
	 * @return
	 */
	public String removeCart(HttpServletRequest req, HttpServletResponse resp) {
		try{
			//接收参数
			String pid = req.getParameter("pid");
			
			//调用购物车中的方法
			Cart cart = getCart(req);
			cart.removeCart(pid);
			//页面跳转
			resp.sendRedirect(req.getContextPath()+"/jsp/cart.jsp");
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Cart getCart(HttpServletRequest req) {
		HttpSession session = req.getSession();
		Cart cart = (Cart) session.getAttribute("cart");
		//判断session中是否有购物车信息
		if(cart == null) {
			cart = new Cart();
			session.setAttribute("cart",cart);
		}
		return cart;
	}
	
	
	/**
	 * 修改文本框更改商品数量
	 * @param req
	 * @param resp
	 * @return
	 */
	public String blurCount(HttpServletRequest req, HttpServletResponse resp) {
		try{
			//接收参数
			String pid = req.getParameter("pid");
			Integer count = Integer.parseInt(req.getParameter("count"));
			//封装CartItem
			//System.out.println(pid + "+" + count);
			change(req, resp, pid, count);
			return null;
		}catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	/**
	 * 点"+"单个添加商品数量
	 * @param req
	 * @param resp
	 * @return
	 */
	public String addOne(HttpServletRequest req, HttpServletResponse resp){
		try{
			//接收参数
			String pid = req.getParameter("pid");
			Integer count = Integer.parseInt(req.getParameter("count"))+1;
			//封装CartItem
			//System.out.println(pid + "+" + count);
			change(req, resp, pid, count);
			return null;
		}catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	/**
	 * 点"-"单个减少商品数量
	 * @param req
	 * @param resp
	 * @return
	 */
	public String removeOne(HttpServletRequest req, HttpServletResponse resp) {
		try{
			//接收参数
			String pid = req.getParameter("pid");
			Integer count = Integer.parseInt(req.getParameter("count"))-1;
			//封装CartItem
			//System.out.println(pid + "+" + count);
			change(req, resp, pid, count);
			return null;
		}catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	private void change(HttpServletRequest req, HttpServletResponse resp,
			String pid, Integer count) throws SQLException, IOException {
		CartItem cartItem = new CartItem();
		cartItem.setCount(count);
		ProductService productService = (ProductService) BeanFactory.getBean("productService");
		Product product = productService.findByPid(pid);
		cartItem.setProduct(product);
		
		//获得购物车
		Cart cart = getCart(req);
		//调用添加到购物车的方法
		cart.removeCart(pid);
		cart.addCart(cartItem);
		//页面跳转
		//resp.sendRedirect(req.getContextPath()+"/jsp/cart.jsp");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("subtotal", cartItem.getSubtotal());
		map.put("total", cart.getTotal());
		JSONObject jsonObject = JSONObject.fromObject(map);
		resp.getWriter().print(jsonObject.toString());
	}
	
	
}
