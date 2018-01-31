package com.itheima.store.service.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;

import com.itheima.store.dao.OrderDao;
import com.itheima.store.domain.Order;
import com.itheima.store.domain.OrderItem;
import com.itheima.store.domain.PageBean;
import com.itheima.store.service.OrderService;
import com.itheima.store.utils.BeanFactory;
import com.itheima.store.utils.JDBCUtils;
/**
 * 订单模块的Service的实现类
 * @author 趴布里克
 *
 */
public class OrderServiceImpl implements OrderService {

	/**
	 * 生成订单的方法
	 */
	@Override
	public void save(Order order) {
		Connection con = null;
		try{
			con = JDBCUtils.getConnection();
			con.setAutoCommit(false);
			OrderDao orderDao = (OrderDao) BeanFactory.getBean("orderDao");
			orderDao.saveOrder(con, order);
			for(OrderItem orderItem : order.getOrderItems()) {
				orderDao.saveOrderItem(con, orderItem);
			}
			DbUtils.commitAndCloseQuietly(con);
		}catch(Exception e) {
			DbUtils.rollbackAndCloseQuietly(con);
			e.printStackTrace();
		}
	}

	/**
	 * 根据用户uid查询用户订单的方法
	 * @throws SQLException 
	 */
	@Override
	public PageBean<Order> findByUid(Integer currPage, String uid) throws Exception {
		PageBean<Order> pageBean = new PageBean<Order>();
		//设置当前页数
		pageBean.setCurrPage(currPage);
		//设置每页显示的记录数
		Integer pageSize = 5;
		pageBean.setPageSize(pageSize);
		//设置总记录数
		OrderDao orderDao = (OrderDao) BeanFactory.getBean("orderDao");
		Integer totalCount = orderDao.findCountByUid(uid);
		pageBean.setTotalCount(totalCount);
		//设置总页数
		double tc = totalCount;
		Double num = Math.ceil(tc / pageSize);
		pageBean.setTotalPage(num.intValue());
		//设置每页显示的记录信息
		int begin = (currPage - 1) * pageSize;
		List<Order> list = orderDao.findPageByUid(uid, begin, pageSize);
		pageBean.setList(list);
		return pageBean;
	}

	/**
	 * 根据订单oid查询订单的方法
	 */
	@Override
	public Order findByOid(String oid) throws Exception {
		OrderDao orderDao = (OrderDao) BeanFactory.getBean("orderDao");
		return orderDao.findByOid(oid);
	}

	/**
	 * 用户付款后修改用户信息的方法
	 */
	@Override
	public void update(Order order) throws Exception {
		OrderDao orderDao = (OrderDao) BeanFactory.getBean("orderDao");
		orderDao.update(order);
	}

	/**
	 * 查询所有订单
	 */
	@Override
	public List<Order> findAll() throws Exception {
		OrderDao orderDao = (OrderDao) BeanFactory.getBean("orderDao");
		return orderDao.findAll();
	}

	/**
	 * 根据订单状态查询
	 */
	@Override
	public List<Order> findByState(Integer pState) throws Exception {
		OrderDao orderDao = (OrderDao) BeanFactory.getBean("orderDao");
		return orderDao.findByState(pState);
	}

	/**
	 * 显示订单详情的方法
	 */
	@Override
	public List<OrderItem> showDetail(String oid) throws Exception {
		OrderDao orderDao = (OrderDao) BeanFactory.getBean("orderDao");
		return orderDao.showDetail(oid);
	}

}
