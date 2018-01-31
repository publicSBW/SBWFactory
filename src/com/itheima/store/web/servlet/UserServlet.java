package com.itheima.store.web.servlet;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;

import com.itheima.store.domain.User;
import com.itheima.store.service.UserService;
import com.itheima.store.service.impl.UserServiceImpl;
import com.itheima.store.utils.BaseServlet;
import com.itheima.store.utils.BeanFactory;
import com.itheima.store.utils.MyDateConverter;
import com.itheima.store.utils.StringUtils;

/**
 * 用户模块的Servlet
 * @author 趴布里克
 *
 */
public class UserServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * 跳转注册页面的方法：registUI
	 * @param req
	 * @param resp
	 * @return	注册页面
	 */
	public String registUI(HttpServletRequest req, HttpServletResponse resp) {
		return "/jsp/regist.jsp";
	}
	
	/**
	 * 异步校验用户名是否存在：checkUsername
	 * @param req
	 * @param resp
	 * @return	null
	 */
	public String checkUsername(HttpServletRequest req, HttpServletResponse resp) {
		try{
		//接收参数
		String username = req.getParameter("username");
		//调用业务层
		UserService userService = (UserService) BeanFactory.getBean("userService");
		User existUser = userService.findByUsername(username);
		//判断
		if(existUser == null) {
			//用户名可以使用
			resp.getWriter().println(1);
		}else{
			//用户名被占用
			resp.getWriter().println(2);
		}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 用户注册的方法
	 * @param req
	 * @param resp
	 * @return /jsp/msg.jsp
	 */
	public String regist(HttpServletRequest req, HttpServletResponse resp) {
		try{
			//验证码
			String code1 = req.getParameter("code");
			String code2 = (String)req.getSession().getAttribute("code");
			//System.out.println(code1+"--"+code2);
			req.getSession().removeAttribute("code");
			if(StringUtils.isEmpty(code1)||StringUtils.isEmpty(code2)||!code1.equalsIgnoreCase(code2)) {
				req.setAttribute("msg", "验证码错误！");
				return "/jsp/login.jsp";
			}
			
			//接收参数
			Map<String, String[]> map = req.getParameterMap();
			//封装数据
			User user = new User();
			ConvertUtils.register(new MyDateConverter(), Date.class);
			BeanUtils.populate(user, map);
			//调用业务层
			UserService userService = (UserService) BeanFactory.getBean("userService");
			userService.save(user);
			req.setAttribute("msg", "注册成功,请去邮箱激活！");
			return "/jsp/msg.jsp";
		}catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	/**
	 * 用户激活的方法
	 * @param req
	 * @param resp
	 * @return
	 */
	public String active(HttpServletRequest req, HttpServletResponse resp){
		try{
			//接收激活码
			String code = req.getParameter("code");
			//根据激活码查询
			UserService userService = (UserService) BeanFactory.getBean("userService");
			User existUser = userService.findByCode(code);
			//判断
			if(existUser == null) {
				//激活码错误
				req.setAttribute("msg", "激活码有误,请重新激活");
				return "/jsp/msg.jsp";
			}
			//激活码正确
			existUser.setState(2);
			existUser.setCode(null);
			userService.update(existUser);
			req.setAttribute("msg", "激活成功！请去登录");
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return "/jsp/msg.jsp";
	}
	
	/**
	 * 跳转登录页面的方法:loginUI
	 * @param req
	 * @param resp
	 * @return
	 */
	public String loginUI(HttpServletRequest req, HttpServletResponse resp) {
		return "/jsp/login.jsp";
	}
	
	/**
	 * 用户登录的方法：login
	 * @param req
	 * @param resp
	 * @return
	 */
	public String login(HttpServletRequest req, HttpServletResponse resp) {
		try{
			//验证码
			String code1 = req.getParameter("code");
			String code2 = (String)req.getSession().getAttribute("code");
			//System.out.println(code1+"--"+code2);
			req.getSession().removeAttribute("code");
			if(StringUtils.isEmpty(code1)||StringUtils.isEmpty(code2)||!code1.equalsIgnoreCase(code2)) {
				req.setAttribute("msg", "验证码错误！");
				return "/jsp/login.jsp";
			}
			
			//接收参数
			Map<String, String[]> map = req.getParameterMap();
			//封装数据
			User user = new User();
			BeanUtils.populate(user, map);
			//调用业务层
			UserService userService = (UserService) BeanFactory.getBean("userService");
			User existUser = userService.login(user);
			if(existUser == null) {
				req.setAttribute("msg", "用户名或密码错误或未激活账号！");
				return "/jsp/login.jsp";
			}
			//记住密码
			String autoLogin = req.getParameter("autoLogin");
			if("true".equals(autoLogin)) {
				Cookie cookie = new Cookie("autoLogin",existUser.getUsername()+"#"+existUser.getPassword());
				cookie.setPath(req.getContextPath());
				cookie.setMaxAge(60*60*24*7);
				resp.addCookie(cookie);
			}
			
			//记住用户名
			String remeber = req.getParameter("remeber");
			if("true".equals(remeber)) {
				Cookie cookie = new Cookie("username",existUser.getUsername());
				cookie.setPath(req.getContextPath());
				cookie.setMaxAge(60*60*24);
				resp.addCookie(cookie);
			}
			
			req.getSession().setAttribute("existUser", existUser);
			resp.sendRedirect(req.getContextPath()+"/index.jsp");
			return null;
		}catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	/**
	 * 用户退出登录的功能
	 * @param req
	 * @param resp
	 * @return
	 */
	public String logOut(HttpServletRequest req, HttpServletResponse resp) {
	
		try {
			//清除session
			//req.getSession().invalidate();
			req.getSession().removeAttribute("existUser");
			//清除Cookie
			Cookie cookie = new Cookie("autoLogin","");
			cookie.setPath(req.getContextPath());
			cookie.setMaxAge(0);
			resp.addCookie(cookie);
			
			resp.sendRedirect(req.getContextPath()+"/index.jsp");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 模态框异步登录的方法
	 * @param req
	 * @param resp
	 * @return
	 */
	public String asyncLogin(HttpServletRequest req, HttpServletResponse resp) {
		try{
			//接收参数
			Map<String, String[]> map = req.getParameterMap();
			//封装数据
			User user = new User();
			BeanUtils.populate(user, map);
			//调用用户的业务层
			UserService userService = (UserService) BeanFactory.getBean("userService");
			User existUser = userService.login(user);
			
			Map<String, Object> result = new HashMap<String, Object>();
			
			//判断
			if(existUser == null) {
				result.put("flag", false);
				result.put("message", "用户名或密码错误哟！！！");
			}else {
				req.getSession().setAttribute("existUser", existUser);
				result.put("flag", true);
				result.put("message", "登录成功了哟！！！");
			}
			
			JSONObject jsonObject = JSONObject.fromObject(result);
			resp.getWriter().println(jsonObject.toString());
		}catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		return null;
	}
}
