package com.itheima.store.web.servlet;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;

import com.itheima.store.domain.Category;
import com.itheima.store.service.CategoryService;
import com.itheima.store.utils.BaseServlet;
import com.itheima.store.utils.BeanFactory;
import com.itheima.store.utils.UUIDUtils;

/**
 * 管理员对分类的管理
 * @author 趴布里克
 *
 */
public class AdminCategoryServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 查询所有分类的方法
	 * @param req
	 * @param resp
	 * @return
	 */
	public String findAll(HttpServletRequest req, HttpServletResponse resp) {
		try{
			//调用分类管理的业务层
			CategoryService categoryService = (CategoryService) BeanFactory.getBean("categoryService");
			List<Category> list = categoryService.findAll();
			
			req.setAttribute("list", list);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return "/admin/category/list.jsp";
	}
	
	/**
	 * 添加页面跳转的方法
	 * @param req
	 * @param resp
	 * @return
	 */
	public String saveUI(HttpServletRequest req, HttpServletResponse resp) {
		return "/admin/category/add.jsp";
	}
	
	/**
	 * 添加分类的方法
	 * @param req
	 * @param resp
	 * @return
	 */
	public String save(HttpServletRequest req, HttpServletResponse resp) {
		try{
			//接收参数
			String cname = req.getParameter("cname");
			//封装数据
			Category category = new Category();
			category.setCid(UUIDUtils.getUUID());
			category.setCname(cname);
			
			//调用业务层
			CategoryService categoryService = (CategoryService) BeanFactory.getBean("categoryService");
			categoryService.save(category);
			
			resp.sendRedirect(req.getContextPath()+"/AdminCategoryServlet?method=findAll");
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * 跳转编辑页面的方法
	 * @param req
	 * @param resp
	 * @return
	 */
	public String edit(HttpServletRequest req, HttpServletResponse resp) {
		try{
			//接收参数
			String cid = req.getParameter("cid");
			//调用业务层查询
			CategoryService categoryService = (CategoryService) BeanFactory.getBean("categoryService");
			Category category = categoryService.findByCid(cid);
			
			req.setAttribute("category", category);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return "/admin/category/edit.jsp";
	}
	
	/**
	 * 修改分类的方法
	 * @param req
	 * @param resp
	 * @return
	 */
	public String update(HttpServletRequest req, HttpServletResponse resp) {
		try{
			//接收参数
			Map<String, String[]> map = req.getParameterMap();
			//封装数据
			Category category = new Category();
			BeanUtils.populate(category, map);
			
			//调用业务层
			CategoryService categoryService = (CategoryService) BeanFactory.getBean("categoryService");
			categoryService.update(category);
			
			//页面跳转
			resp.sendRedirect(req.getContextPath()+"/AdminCategoryServlet?method=findAll");
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 删除分类的方法
	 * @param req
	 * @param resp
	 * @return
	 */
	public String delete(HttpServletRequest req, HttpServletResponse resp) {
		try{
			//接收参数
			String cid = req.getParameter("cid");
			//System.out.println(cid);
			//调用业务层
			CategoryService categoryService = (CategoryService) BeanFactory.getBean("categoryService");
			categoryService.delete(cid);
			
			//页面跳转
			resp.sendRedirect(req.getContextPath()+"/AdminCategoryServlet?method=findAll");
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
