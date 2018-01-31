package com.itheima.store.dao.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.itheima.store.dao.OrderDao;
import com.itheima.store.domain.Order;
import com.itheima.store.domain.OrderItem;
import com.itheima.store.domain.Product;
import com.itheima.store.utils.JDBCUtils;
/**
 * 订单模块的Dao的实现类
 * @author 趴布里克
 *
 */
public class OrderDaoImpl implements OrderDao {

	/**
	 * 保存订单
	 * @throws SQLException 
	 */
	@Override
	public void saveOrder(Connection con, Order order) throws SQLException {
		QueryRunner qr = new QueryRunner();
		String sql = "insert into orders values (?,?,?,?,?,?,?,?)";
		Object[] params = {order.getOid(), order.getOrdertime(), order.getTotal(), order.getState(),
				order.getAddress(), order.getName(), order.getTelephone(), order.getUser().getUid()};
		qr.update(con, sql, params);
	}

	/**
	 * 保存订单项
	 * @throws SQLException 
	 */
	@Override
	public void saveOrderItem(Connection con, OrderItem orderItem) throws SQLException {
		QueryRunner qr = new QueryRunner();
		String sql = "insert into orderitem values(?,?,?,?,?)";
		Object[] params = {orderItem.getItemId(), orderItem.getCount(), orderItem.getSubtotal(), 
				orderItem.getProduct().getPid(), orderItem.getOrder().getOid()};
		qr.update(con, sql, params);
	}

	/**
	 * 查询该用户的总订单数
	 * @throws SQLException 
	 */
	@Override
	public Integer findCountByUid(String uid) throws SQLException {
		QueryRunner qr = new QueryRunner(JDBCUtils.getDataSource());
		String sql = "select count(*) from orders where uid=?";
		Long count = (Long) qr.query(sql, new ScalarHandler(), uid);
		return count.intValue();
	}

	/**
	 * 查询该用户每页显示的订单数据
	 * @throws SQLException 
	 */
	@Override
	public List<Order> findPageByUid(String uid, int begin, Integer pageSize) throws Exception {
		QueryRunner qr = new QueryRunner(JDBCUtils.getDataSource());
		String sql = "select * from orders where uid=? order by ordertime desc limit ?,?";
		List<Order> list = qr.query(sql, new BeanListHandler<Order>(Order.class), uid, begin, pageSize);
		//设置订单实体类的数据-->OrderItem
		for(Order order : list) {
			sql = "select * from orderitem o, product p where o.pid=p.pid and o.oid=?";
			List<Map<String, Object>> olist = qr.query(sql, new MapListHandler(), order.getOid());
			//遍历olist里的map集合，封装product和orderitem
			for(Map<String, Object> map : olist) {
				Product product = new Product();
				BeanUtils.populate(product, map);
				OrderItem orderItem = new OrderItem();
				BeanUtils.populate(orderItem, map);
				
				orderItem.setProduct(product);
				
				order.getOrderItems().add(orderItem);
			}
		}
		return list;
	}

	/**
	 * 根据订单oid查询订单的方法
	 */
	@Override
	public Order findByOid(String oid) throws Exception {
		QueryRunner qr = new QueryRunner(JDBCUtils.getDataSource());
		String sql = "select * from orders where oid=?";
		Order order = qr.query(sql, new BeanHandler<Order>(Order.class), oid);
		//封装订单里面的订单项
		sql = "select * from orderitem o, product p where o.pid=p.pid and o.oid=?";
		List<Map<String, Object>> oList = qr.query(sql, new MapListHandler(), order.getOid());
		for(Map<String, Object> map : oList) {
			Product product = new Product();
			BeanUtils.populate(product, map);
			OrderItem orderItem = new OrderItem();
			BeanUtils.populate(orderItem, map);
			
			orderItem.setProduct(product);
			
			order.getOrderItems().add(orderItem);
		}
		return order;
	}

	/**
	 * 用户付款后修改用户信息的方法
	 */
	@Override
	public void update(Order order) throws Exception {
		QueryRunner qr = new QueryRunner(JDBCUtils.getDataSource());
		String sql = "update orders set total = ?,state = ? ,address = ?,name=?,telephone = ? where oid = ?";
		Object[] params = {order.getTotal(), order.getState(), order.getAddress(), order.getName(), order.getTelephone(), order.getOid()};
		qr.update(sql, params);
	}

	/**
	 * 查询所有订单的方法
	 */
	@Override
	public List<Order> findAll() throws Exception {
		QueryRunner qr = new QueryRunner(JDBCUtils.getDataSource());
		String sql = "select * from orders order by ordertime desc";
		List<Order> list= qr.query(sql, new BeanListHandler<Order>(Order.class));
		return list;
	}

	/**
	 * 根据订单状态查询订单的方法
	 */
	@Override
	public List<Order> findByState(Integer pState) throws Exception {
		QueryRunner qr = new QueryRunner(JDBCUtils.getDataSource());
		String sql = "select * from orders where state=? order by ordertime desc";
		List<Order> list= qr.query(sql, new BeanListHandler<Order>(Order.class), pState);
		return list;
	}

	/**
	 * 显示订单详情的方法
	 */
	@Override
	public List<OrderItem> showDetail(String oid) throws Exception {
		QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());
		String sql = "SELECT * FROM orderitem o,product p WHERE o.pid = p.pid AND o.oid = ?";
		List<OrderItem> list = new ArrayList<OrderItem>();
		List<Map<String,Object>> oList = queryRunner.query(sql, new MapListHandler(),oid);
		for (Map<String, Object> map : oList) {
			Product product = new Product();
			BeanUtils.populate(product, map);
			
			OrderItem orderItem = new OrderItem();
			BeanUtils.populate(orderItem, map);
			
			orderItem.setProduct(product);
			
			list.add(orderItem);
		}
		return list;
	}

}
