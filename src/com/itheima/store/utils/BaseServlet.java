package com.itheima.store.utils;

import java.io.IOException;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 通用的Servlet
 * 接收参数-->method
 * 获取子类的Class对象
 * 通过反射获取子类中方法名为method,参数为HttpServletRequest和HttpServletResponse,返回值类型是String
 * 执行该方法,获取返回值,如果返回值不为空,页面跳转
 * @author 趴布里克
 *
 */
public class BaseServlet extends HttpServlet{
	@Override
	public void service(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html;charset=UTF-8");
		//接收参数
		String methodName = req.getParameter("method");
		if(methodName == null || "".equals(methodName)) {
			resp.getWriter().println("method参数为null!!!");
			return;
		}
		//获得子类的Class对象
		Class clazz = this.getClass();
		//获得子类对象的方法
		try {
			Method method = clazz.getMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);
			//执行方法
			String path = (String) method.invoke(this, req, resp);
			if(path != null) {
				req.getRequestDispatcher(path).forward(req, resp);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
}
