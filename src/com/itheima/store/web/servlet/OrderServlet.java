package com.itheima.store.web.servlet;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itheima.store.domain.Cart;
import com.itheima.store.domain.CartItem;
import com.itheima.store.domain.Order;
import com.itheima.store.domain.OrderItem;
import com.itheima.store.domain.PageBean;
import com.itheima.store.domain.Pay;
import com.itheima.store.domain.User;
import com.itheima.store.service.OrderService;
import com.itheima.store.utils.BaseServlet;
import com.itheima.store.utils.BeanFactory;
import com.itheima.store.utils.PaymentUtil;
import com.itheima.store.utils.PropertiesUtils;
import com.itheima.store.utils.UUIDUtils;

/**
 * 订单模块的Servlet
 * @author 趴布里克
 *
 */
public class OrderServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * 生成订单的方法
	 * @param req
	 * @param resp
	 * @return
	 */
	public String saveOrder(HttpServletRequest req, HttpServletResponse resp) {
		//封装Order对象
		Order order = new Order();
		order.setOid(UUIDUtils.getUUID());
		order.setOrdertime(new Date());
		order.setState(1);
		//设置总金额-->从购物车中获取
		Cart cart = (Cart) req.getSession().getAttribute("cart");
		if(cart == null) {
			req.setAttribute("msg", "您的购物车空空如也哟！！！");
			return "/jsp/msg.jsp";
		}
		order.setTotal(cart.getTotal());
		
		//设置订单项-->从购物车中的购物项中获得
		for(CartItem cartItem : cart.getMap().values()) {
			OrderItem orderItem = new OrderItem();
			orderItem.setItemId(UUIDUtils.getUUID());
			orderItem.setCount(cartItem.getCount());
			orderItem.setSubtotal(cartItem.getSubtotal());
			orderItem.setProduct(cartItem.getProduct());
			orderItem.setOrder(order);
			
			order.getOrderItems().add(orderItem);
		}
		
		//设置用户信息-->从cookie获取-->页面采用了异步校验和登录,所有Seesion中必定有用户的信息
		User existUser = (User) req.getSession().getAttribute("existUser");
		order.setUser(existUser);
		
		//调用业务层
		OrderService orderService = (OrderService) BeanFactory.getBean("orderService");
		orderService.save(order);
		//清空购物车
		cart.clearCart();
		
		//页面跳转
		req.setAttribute("order", order);
		return "/jsp/order_info.jsp";
	}
	
	/**
	 * 根据用户信息查询用户订单的方法
	 * @param req
	 * @param resp
	 * @return
	 */
	public String findByUid(HttpServletRequest req, HttpServletResponse resp) {
		try{
			//接收参数
			Integer currPage = Integer.parseInt(req.getParameter("currPage"));
			//获取用户信息
			User existUser = (User) req.getSession().getAttribute("existUser");
			//调用业务层
			OrderService orderService = (OrderService) BeanFactory.getBean("orderService");
			PageBean<Order> pageBean = orderService.findByUid(currPage, existUser.getUid());
			
			req.setAttribute("pageBean", pageBean);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return "/jsp/order_list.jsp";
	}
	
	/**
	 * 点击'付款',根据订单oid查询订单的方法
	 * @param req
	 * @param resp
	 * @return
	 */
	public String findByOid(HttpServletRequest req, HttpServletResponse resp) {
		try{
			//接收参数
			String oid = req.getParameter("oid");
			//调用业务层
			OrderService orderService = (OrderService) BeanFactory.getBean("orderService");
			Order order = orderService.findByOid(oid);
			
			req.setAttribute("order", order);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return "/jsp/order_info.jsp";
	}
	
	/**
	 * 支付的方法
	 * @param req
	 * @param resp
	 * @return
	 */
	public String payOrder(HttpServletRequest req, HttpServletResponse resp) {
		try{
			//接收参数
			String oid = req.getParameter("oid");
			String name = req.getParameter("name");
			String address = req.getParameter("address");
			String telephone = req.getParameter("telephone");
			String pd_FrpId = req.getParameter("pd_FrpId");
			
			//修改数据库-->姓名,电话，地址
			OrderService orderService = (OrderService) BeanFactory.getBean("orderService");
			Order order = orderService.findByOid(oid);
			order.setName(name);
			order.setAddress(address);
			order.setTelephone(telephone);
			//修改信息
			orderService.update(order);
			
			//付款
			String p0_Cmd = "Buy";
			String p1_MerId = PropertiesUtils.getProperties("merchantInfo.properties", "p1_MerId");
			String p2_Order = oid;
			String p3_Amt = "0.01";
			String p4_Cur = "CNY";
			String p5_Pid = "";
			String p6_Pcat = "";
			String p7_Pdesc = "";
			String p8_Url = PropertiesUtils.getProperties("merchantInfo.properties", "responseURL");
			String p9_SAF = "";
			String pa_MP = "";
			String pr_NeedResponse = "1";
			String keyValue = PropertiesUtils.getProperties("merchantInfo.properties", "keyValue");
			String hmac = PaymentUtil.buildHmac(p0_Cmd, p1_MerId, p2_Order, p3_Amt, p4_Cur, p5_Pid,
					p6_Pcat, p7_Pdesc, p8_Url, p9_SAF, pa_MP, pd_FrpId, pr_NeedResponse, keyValue);
			//封装参数实体类
			Pay pay = new Pay(p0_Cmd, p1_MerId, p2_Order, p3_Amt, p4_Cur, p5_Pid, p6_Pcat, p7_Pdesc, 
					p8_Url, p9_SAF, pa_MP, pr_NeedResponse, keyValue, pd_FrpId, hmac);
			
			req.setAttribute("pay", pay);
			
		}catch(Exception e) {
			
		}
		return "/submitpay.jsp";
	}
	
	/**
	 * 支付成功后返回的方法
	 * @param req
	 * @param resp
	 * @return
	 */
	public String callBack(HttpServletRequest req, HttpServletResponse resp) {
		try{
			//接收返回的参数
			String oid = req.getParameter("r6_Order");
			String money = req.getParameter("r3_Amt"); 
			
			//修改订单状态
			OrderService orderService = (OrderService) BeanFactory.getBean("orderService");
			Order order = orderService.findByOid(oid);
			order.setState(2);
			orderService.update(order);
			
			req.setAttribute("msg", "您的订单:"+oid+"付款成功,付款的金额为:"+money);
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return "/jsp/msg.jsp";
	}
}
