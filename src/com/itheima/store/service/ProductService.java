package com.itheima.store.service;

import java.sql.SQLException;
import java.util.List;

import com.itheima.store.domain.PageBean;
import com.itheima.store.domain.Product;

/**
 * producr的Service的接口
 * @author 趴布里克
 *
 */
public interface ProductService {

	/**
	 * 查询最新商品
	 * @return
	 */
	List<Product> findByNew() throws SQLException;

	/**
	 * 查询热门商品
	 * @return
	 */
	List<Product> findByHot() throws SQLException;

	/**
	 * 分类查询
	 * @param cid
	 * @param currPage
	 * @return
	 * @throws SQLException
	 */
	PageBean<Product> findByPageCid(String cid, Integer currPage)throws SQLException;

	/**
	 * 根据商品的Pid查询商品的信息
	 * @param pid
	 * @return
	 * @throws SQLException
	 */
	Product findByPid(String pid)throws SQLException;

	/**
	 * 分页查询所有商品
	 * @param currPage
	 * @return
	 * @throws SQLException
	 */
	PageBean<Product> findByPage(Integer currPage)throws SQLException;

	/**
	 * 添加商品的方法
	 * @param product
	 * @throws SQLException
	 */
	void save(Product product)throws SQLException;

	/**
	 * 商品下架的方法
	 * @param product
	 * @throws SQLException
	 */
	void update(Product product)throws SQLException;

	/**
	 * 查看所有下架商品的方法
	 * @return
	 * @throws SQLException
	 */
	List<Product> findByPushDown()throws SQLException;

}
