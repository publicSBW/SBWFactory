package com.itheima.store.web.filter;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.itheima.store.domain.User;
import com.itheima.store.service.UserService;
import com.itheima.store.service.impl.UserServiceImpl;
import com.itheima.store.utils.CookieUtils;
import com.itheima.store.utils.StringUtils;

/**
 * 自动登录的Filter
 * @author 趴布里克
 *
 */
public class AutoLoginFilter implements Filter {
	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
	/*//判断Session是否有用户信息
	HttpServletRequest req = (HttpServletRequest) request;
	HttpSession session = req.getSession();
	User existUser = (User) session.getAttribute("existUser");
	//Session中没有用户信息
	if(existUser == null) {
		//获得Cookie中的数据
		Cookie[] cookies = req.getCookies();
		Cookie cookie = CookieUtils.findCookie(cookies, "autoLogin");
		//如果cookie有用户信息
		if(cookie != null) {
			//获得用户信息
			String value = cookie.getValue();// aaa#111
			if(!StringUtils.isEmpty(value)) {
				// 获得用户名和密码:
				String username = value.split("#")[0];
				String password = value.split("#")[1];
				// 去数据库进行查询:
				User user = new User();
				user.setUsername(username);
				user.setPassword(password);
				UserService userService = new UserServiceImpl();
				try {
					existUser = userService.login(user);
					if(existUser != null) {
						// 将用户存到session中,放行
						session.setAttribute("existUser", existUser);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
		//System.out.println("00");
	chain.doFilter(req, response);*/
	
		
		
		
		/**
		 * 先判断session中是否有用户信息
		 * 没有则从cookie中获取，如果cookie中有则拿出来去数据库中查找并将其放在session中
		 */
		//注意这里的request对象是ServletRequest,想获得session需要将其转换伟HTTPServletRequest
		HttpServletRequest  req = (HttpServletRequest)request;
		User user = (User) req.getSession().getAttribute("existUser");
		if(user==null){
			Cookie [] cookies  = req.getCookies();
			Cookie userCookie = CookieUtils.findCookie(cookies, "autoLogin");
			if(userCookie!=null){
				String vlaue  = userCookie.getValue();
				if(vlaue!=null&&!"".equals(vlaue)){
					String [] values = vlaue.split("#");
					String username = values[0];
					String password = values[1];
					User newUser = new User();
					newUser.setUsername(username);
					newUser.setPassword(password);
					//调用业务层去查询
					UserService  service = new UserServiceImpl();
					try {
						User selectedResult = service.login(newUser);
						if(selectedResult !=null){
							req.getSession().setAttribute("existUser", selectedResult);
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		}
		chain.doFilter(request, response);
	}
	
	@Override
	public void init(FilterConfig fConfig) throws ServletException {
	}

}
