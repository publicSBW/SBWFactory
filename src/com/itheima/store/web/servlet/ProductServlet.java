package com.itheima.store.web.servlet;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedList;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itheima.store.domain.PageBean;
import com.itheima.store.domain.Product;
import com.itheima.store.service.ProductService;
import com.itheima.store.service.impl.ProductServiceImpl;
import com.itheima.store.utils.BaseServlet;
import com.itheima.store.utils.BeanFactory;
import com.itheima.store.utils.CookieUtils;

/**
 * Product的Servlet
 * @author 趴布里克
 *
 */
public class ProductServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * 查询分类中的所有商品(分页查询)
	 * @param req
	 * @param resp
	 * @return
	 */
	public String findByCid(HttpServletRequest req, HttpServletResponse resp) {
		try{
			//接收参数
			String cid = req.getParameter("cid");
			Integer currPage = Integer.parseInt(req.getParameter("currPage"));
			//调用业务层
			ProductService productService = (ProductService) BeanFactory.getBean("productService");
			PageBean<Product> pageBean = productService.findByPageCid(cid, currPage);
			req.setAttribute("pageBean", pageBean);
			req.setAttribute("cid", cid);
		}catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		
		return "/jsp/product_list.jsp";
		
	}
	
	/**
	 * 根据商品的pid查询商品信息
	 * @param req
	 * @param resp
	 * @return
	 */
	public String findByPid(HttpServletRequest req, HttpServletResponse resp) {
		try{
			//接收参数
			String pid = req.getParameter("pid");
			//调用业务层
			ProductService productService = (ProductService) BeanFactory.getBean("productService");
			Product product = productService.findByPid(pid);
			
			history(req, resp, pid);
			
			req.setAttribute("p", product);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return "/jsp/product_info.jsp";
	}
	
	/**
	 * 清除浏览历史记录的方法
	 * @param req
	 * @param resp
	 * @return
	 */
	public String clearHistory(HttpServletRequest req, HttpServletResponse resp) {
		
		try {
			Cookie cookie = new Cookie("history", null);
			cookie.setPath(req.getContextPath());
			cookie.setMaxAge(0);
			//写回浏览器
			resp.addCookie(cookie);
			
			String cid = req.getParameter("cid");
			Integer currPage = Integer.parseInt(req.getParameter("currPage"));
			ProductService productService = (ProductService) BeanFactory.getBean("productService");
			PageBean<Product> pageBean = productService.findByPageCid(cid, currPage);
			/*req.setAttribute("pageBean", pageBean);
			req.setAttribute("cid", cid);*/
			/*
			 * 只能重定向,转发一次请求，浏览记录还存在,需刷新
			 */
			req.getSession().setAttribute("pageBean", pageBean);
			req.getSession().setAttribute("cid", cid);
			resp.sendRedirect(req.getContextPath()+"/jsp/product_list.jsp");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		//return "/jsp/product_list.jsp";
		return null;
	}

	private void history(HttpServletRequest req, HttpServletResponse resp,
			String pid) {
		//记录商品的浏览记录
		Cookie[] cookies = req.getCookies();
		Cookie cookie = CookieUtils.findCookie(cookies, "history");
		//判断cookie中有没有
		Cookie newCookie = null;
		if(cookie == null) {
			//尚未浏览商品
			newCookie = new Cookie("history",pid);
			
		}else{
			//已浏览过商品
			String value = cookie.getValue();
			String[] pids = value.split("-");
			//将数组转成集合
			LinkedList<String> list = new LinkedList<String>(Arrays.asList(pids));
			//判断商品是否已经在历史记录中
			if(list.contains(pid)) {
				//已在历史记录中
				list.remove(pid);
				list.addFirst(pid);
			}else {
				//不在浏览历史记录中
				//判断历史记录是否超过6
				if(list.size()>=6) {
					list.removeLast();
					list.addFirst(pid);
				}else {
					list.addFirst(pid);
				}
			}
			
			StringBuffer sb = new StringBuffer();
			//遍历集合,已特定格式存储到字符串
			for(String id : list) {
				sb.append(id).append("-");
			}
			String idStr = sb.toString().substring(0, sb.length()-1);
			newCookie = new Cookie("history",idStr);
			
		}
		//写回浏览器
		newCookie.setPath(req.getContextPath());
		newCookie.setMaxAge(60*60*24*7);
		resp.addCookie(newCookie);
	}

}
