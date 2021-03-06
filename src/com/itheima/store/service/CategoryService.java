package com.itheima.store.service;

import java.sql.SQLException;
import java.util.List;

import com.itheima.store.domain.Category;

/**
 * 商品分类的Service的接口
 * @author 趴布里克
 *
 */
public interface CategoryService {

	/**
	 * 查询所有分类的方法
	 * @return
	 */
	List<Category> findAll() throws SQLException;

	/**
	 * 添加分类的方法
	 * @param category
	 * @throws SQLException
	 */
	void save(Category category)throws SQLException;

	/**
	 * 编辑分类的方法
	 * @param cid
	 * @throws SQLException
	 */
	Category findByCid(String cid)throws SQLException;

	/**
	 * 修改分类的方法
	 * @param category
	 * @throws SQLException
	 */
	void update(Category category)throws SQLException;

	/**
	 * 删除分类的方法
	 * @param cid
	 * @throws SQLException
	 */
	void delete(String cid)throws SQLException;

}
