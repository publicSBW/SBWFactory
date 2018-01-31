package com.itheima.store.dao.impl;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import com.itheima.store.dao.CategoryDao;
import com.itheima.store.domain.Category;
import com.itheima.store.utils.JDBCUtils;
/**
 * 商品分类的Dao的实现类
 * @author 趴布里克
 *
 */
public class CategoryDaoImpl implements CategoryDao {

	/**
	 * 查询所有分类的方法
	 * @throws SQLException 
	 */
	@Override
	public List<Category> findAll() throws SQLException {
		QueryRunner qr = new QueryRunner(JDBCUtils.getDataSource());
		String sql = "select * from category";
		List<Category> list = qr.query(sql, new BeanListHandler<Category>(Category.class));
		
		return list;
	}

	/**
	 * 添加分类的方法
	 */
	@Override
	public void save(Category category) throws SQLException {
		QueryRunner qr = new QueryRunner(JDBCUtils.getDataSource());
		String sql = "insert into category values (?,?)";
		Object[] params = {category.getCid(), category.getCname()};
		qr.update(sql, params);
	}

	/**
	 * 编辑分类查询的方法
	 */
	@Override
	public Category findByCid(String cid) throws SQLException {
		QueryRunner qr = new QueryRunner(JDBCUtils.getDataSource());
		String sql = "select * from category where cid=?";
		Category category = qr.query(sql, new BeanHandler<Category>(Category.class), cid);
		return category;
	}

	/**
	 * 修改分类的方法
	 */
	@Override
	public void update(Category category) throws SQLException {
		QueryRunner qr = new QueryRunner(JDBCUtils.getDataSource());
		String sql = "update category set cname=? where cid=?";
		Object[] params = {category.getCname(), category.getCid()};
		qr.update(sql, params);
		
	}

	/**
	 * 删除分类的方法
	 * 	因为该分类下可能有商品,有外键关联无法删除,
	 * 	所有要先将该分类下的商品的外键设置为null再删除该分类
	 ******************************************** 
	 * 		一般不会删除分类：						*
	 * 			在分类的数据表中设置一个字段-->"isDel"	*
	 * 			删除的设置为0,未删除的设置为1			*
	 * 			查询分类的时候查询字段'isDel'为1的分类即可	*
	 ********************************************
	 */
	@Override
	public void delete(String cid) throws SQLException {
		QueryRunner qr = new QueryRunner(JDBCUtils.getDataSource());
		String sql = "update product set cid=? where cid=?";
		qr.update(sql, null, cid);
		
		sql = "delete from category where cid=?";
		qr.update(sql, cid);
	}

	
}
