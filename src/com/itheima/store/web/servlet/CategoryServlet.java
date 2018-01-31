package com.itheima.store.web.servlet;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import com.itheima.store.domain.Category;
import com.itheima.store.service.CategoryService;
import com.itheima.store.service.ProductService;
import com.itheima.store.service.impl.CategoryServiceImpl;
import com.itheima.store.service.impl.ProductServiceImpl;
import com.itheima.store.utils.BaseServlet;
import com.itheima.store.utils.BeanFactory;

/**
 * 商品分类的Servlet
 * @author 趴布里克
 *
 */
public class CategoryServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * 查询所有分类的方法
	 * @param req
	 * @param resp
	 * @return
	 */
	public String findAll(HttpServletRequest req, HttpServletResponse resp) {
		try{
			//调用业务层
			CategoryService categoryService = (CategoryService) BeanFactory.getBean("categoryService");
			List<Category> list = categoryService.findAll();
			JSONArray jsonArray = JSONArray.fromObject(list);
			
			resp.getWriter().println(jsonArray.toString());
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
