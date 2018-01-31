package com.itheima.store.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.itheima.store.domain.Order;
import com.itheima.store.domain.OrderItem;

/**
 * 订单模块的Dao的接口
 * @author 趴布里克
 *
 */
public interface OrderDao {

	/**
	 * 保存订单
	 * @param con
	 * @param order
	 */
	void saveOrder(Connection con, Order order) throws SQLException;

	/**
	 * 保存订单项
	 * @param con
	 * @param orderItem
	 */
	void saveOrderItem(Connection con, OrderItem orderItem) throws SQLException;

	/**
	 * 查询用户所有订单的数量
	 * @param uid
	 * @return
	 * @throws SQLException
	 */
	Integer findCountByUid(String uid) throws SQLException;

	/**
	 * 查询用户每页显示的订单的具体数据
	 * @param uid
	 * @param begin
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	List<Order> findPageByUid(String uid, int begin, Integer pageSize) throws Exception;

	/**
	 * 根据订单oid查询订单的方法
	 * @param oid
	 * @return
	 * @throws Exception
	 */
	Order findByOid(String oid)throws Exception;

	/**
	 * 用户付款后修改用户信息的方法
	 * @param order
	 * @throws Exception
	 */
	void update(Order order)throws Exception;

	/**
	 * 查询所有订单的方法
	 * @return
	 * @throws Exception
	 */
	List<Order> findAll()throws Exception;

	/**
	 * 根据订单状态查询订单的方法
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
