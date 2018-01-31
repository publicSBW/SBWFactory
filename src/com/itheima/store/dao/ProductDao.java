package com.itheima.store.dao;

import java.sql.SQLException;
import java.util.List;

import com.itheima.store.domain.Product;

/**
 * producr的Dao的接口
 * @author 趴布里克
 *
 */
public interface ProductDao {

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
	 * 查询总记录数
	 * @return
	 * @throws SQLException
	 */
	Integer findCountByCid(String cid)throws SQLException;

	/**
	 * 查询每页显示的数据
	 * @param cid
	 * @param begin
	 * @param pageSize
	 * @return
	 * @throws SQLException
	 */
	List<Product> findPageByCid(String cid, int begin, Integer pageSize)throws SQLException;

	/**
	 * 根据商品的pid查询商品的信息
	 * @param pid
	 * @return
	 * @throws SQLException
	 */
	Product findByPid(String pid)throws SQLException;

	/**
	 * 查询总记录数的方法
	 * @return
	 * @throws SQLException
	 */
	Integer findCount()throws SQLException;

	/**
	 * 查询一页的数据信息
	 * @param begin
	 * @param pageSize
	 * @return
	 * @throws SQLException
	 */
	List<Product> findByPage(int begin, Integer pageSize)throws SQLException;

	/**
	 * 添加商品的方法
	 * @param product
	 * @throws SQLException
	 */
	void save(Product product)throws SQLException;

	/**
	 * 更新商品数据的方法
	 * @param product
	 * @throws SQLException
	 */
	void update(Product product)throws SQLException;

	/**
	 * 查看所有商品的方法
	 * @return
	 * @throws SQLException
	 */
	List<Product> findByPushDown()throws SQLException;

}
