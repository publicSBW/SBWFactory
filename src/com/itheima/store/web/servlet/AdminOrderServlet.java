package com.itheima.store.web.servlet;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;

import com.itheima.store.domain.Order;
import com.itheima.store.domain.OrderItem;
import com.itheima.store.service.OrderService;
import com.itheima.store.utils.BaseServlet;
import com.itheima.store.utils.BeanFactory;

/**
 * 后台订单管理的Servlet
 * @author 趴布里克
 *
 */
public class AdminOrderServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * 查询所有订单和根据订单状态查询订单的方法
	 * @param req
	 * @param resp
	 * @return
	 */
    public String findAll(HttpServletRequest req, HttpServletResponse resp) {
    	try{
    		//接收参数
    		String state = req.getParameter("state");
    		OrderService orderService = (OrderService) BeanFactory.getBean("orderService");
    		List<Order> list = new ArrayList<Order>();
    		//判断
    		if(state == null) {
    			//查询所有
    			list = orderService.findAll();
    		}else{
    			//根据订单状态查询
    			Integer pState = Integer.parseInt(state);
    			list = orderService.findByState(pState);
    		}
    		
    		req.setAttribute("list", list);
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    	return "/admin/order/list.jsp";
    }
    
    /**
     * 查询订单详情的方法
     * @param req
     * @param resp
     * @return
     */
    public String showDetail(HttpServletRequest req, HttpServletResponse resp) {
    	try{
    		//接收参数
    		String oid = req.getParameter("oid");
    		OrderService orderService = (OrderService) BeanFactory.getBean("orderService");
    		List<OrderItem> list = orderService.showDetail(oid);
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.setExcludes(new String[]{"order"});
			JSONArray jsonArray = JSONArray.fromObject(list,jsonConfig);
			resp.getWriter().println(jsonArray.toString());
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    	return null;
    }
    
    /**
     * 发货的方法
     * @param req
     * @param resp
     * @return
     */
    public String send(HttpServletRequest req, HttpServletResponse resp) {
    	try{
    		String oid = req.getParameter("oid");
    		OrderService orderService = (OrderService) BeanFactory.getBean("orderService");
    		Order order = orderService.findByOid(oid);
    		order.setState(3);
    		orderService.update(order);
    		resp.sendRedirect(req.getContextPath()+"/AdminOrderServlet?method=findAll");
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	return null;
    }
}
