package com.itheima.store.service;

import java.util.List;

import com.itheima.store.domain.Order;
import com.itheima.store.domain.OrderItem;
import com.itheima.store.domain.PageBean;

/**
 * 订单模块的Service的接口
 * @author 趴布里克
 *
 */
public interface OrderService {

	/**
	 * 生成订单的方法
	 * @param order
	 */
	void save(Order order);

	/**
	 * 根据用户uid查询用户订单的方法
	 * @param currPage
	 * @param uid
	 * @return
	 */
	PageBean<Order> findByUid(Integer currPage, String uid) throws Exception;

	/**
	 * 根据订单oid查询订单的方法
	 * @param oid
	 * @return
	 * @throws Exception
	 */
	Order findByOid(String oid)throws Exception;

	/**
	 * 用户付款后修改数据信息的方法
	 * @param order
	 */
	void update(Order order)throws Exception;

	/**
	 * 查询所有订单
	 * @return
	 * @throws Exception
	 */
	List<Order> findAll()throws Exception;

	/**
	 * 根据订单状态查询订单
	 * @param pState
	 * @return
	 * @throws Exception
	 */
	List<Order> findByState(Integer pState)throws Exception;

	/**
	 * 显示订单详情的方法
	 * @param oid
	 * @return
	 * @throws Exception
	 */
	List<OrderItem> showDetail(String oid)throws Exception;
		
}
