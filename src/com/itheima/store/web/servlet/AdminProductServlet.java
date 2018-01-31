package com.itheima.store.web.servlet;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itheima.store.domain.Category;
import com.itheima.store.domain.PageBean;
import com.itheima.store.domain.Product;
import com.itheima.store.service.CategoryService;
import com.itheima.store.service.ProductService;
import com.itheima.store.utils.BaseServlet;
import com.itheima.store.utils.BeanFactory;

/**
 * 后台商品管理的Servlet
 * @author 趴布里克
 *
 */
public class AdminProductServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * 分页查询所有商品的方法
	 * @param req
	 * @param resp
	 * @return
	 */
	public String findByPage(HttpServletRequest req, HttpServletResponse resp) {
		try{
			//接收参数
			Integer currPage = Integer.parseInt(req.getParameter("currPage"));
			//调用业务层
			ProductService productService = (ProductService) BeanFactory.getBean("productService");
			PageBean<Product> pageBean = productService.findByPage(currPage);
			
			req.setAttribute("pageBean", pageBean);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return "/admin/product/list.jsp";
	}
	
	/**
	 * 跳转添加页面的方法
	 * @param req
	 * @param resp
	 * @return
	 */
	public String saveUI(HttpServletRequest req, HttpServletResponse resp) {
		try{
			//先查询所有分类
			CategoryService categoryService = (CategoryService) BeanFactory.getBean("categoryService");
			List<Category> list = categoryService.findAll();
			req.setAttribute("list", list);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return "/admin/product/add.jsp";
	}
	
	/**
	 * 商品下架的方法
	 * @param req
	 * @param resp
	 * @return
	 */
	public String pushDown(HttpServletRequest req, HttpServletResponse resp) {
		push(req, resp, 1);
		return null;
	}

	/**
	 * 查看所有下架商品的方法
	 * @param req
	 * @param resp
	 * @return
	 */
	public String findByPushDown(HttpServletRequest req, HttpServletResponse resp) {
		try{
			// 调用业务层:
			ProductService productService = (ProductService) BeanFactory.getBean("productService");
			List<Product> list = productService.findByPushDown();
			req.setAttribute("list", list);
		}catch(Exception e){
				e.printStackTrace();
		}
		return "/admin/product/pushDown_list.jsp";
	}
	
	/**
	 * 商品上架的方法
	 * @param req
	 * @param resp
	 * @return
	 */
	public String pushUp(HttpServletRequest req, HttpServletResponse resp) {
		push(req, resp, 0);
		return null;
		
	}
	
	/**
	 * 商品上下架的方法
	 * @param req
	 * @param resp
	 * @param pflag
	 */
	private void push(HttpServletRequest req, HttpServletResponse resp, Integer pflag) {
		try{
			//接收参数
			String pid = req.getParameter("pid");
			//调用业务层先查询
			ProductService productService = (ProductService) BeanFactory.getBean("productService");
			Product product = productService.findByPid(pid);
			product.setPflag(pflag);
			//调用业务层更新状态
			productService.update(product);
			//页面跳转
			resp.sendRedirect(req.getContextPath()+"/AdminProductServlet?method=findByPage&currPage=1");
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
