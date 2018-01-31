package com.itheima.store.dao.impl;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.itheima.store.dao.ProductDao;
import com.itheima.store.domain.Product;
import com.itheima.store.utils.JDBCUtils;
/**
 * producr的Dao的实现类
 * @author 趴布里克
 *
 */
public class ProductDaoImpl implements ProductDao {

	/**
	 * 查询热门商品
	 * @throws SQLException 
	 */
	@Override
	public List<Product> findByHot() throws SQLException {
		QueryRunner qr = new QueryRunner(JDBCUtils.getDataSource());
		String sql = "select * from product where pflag = ? and is_hot = ? order by pdate desc limit ?";
		List<Product> list = qr.query(sql, new BeanListHandler<Product>(Product.class), 0, 1, 9);
		return list;
	}

	/**
	 * 查询最新商品
	 * @throws SQLException 
	 */
	@Override
	public List<Product> findByNew() throws SQLException {
		QueryRunner qr = new QueryRunner(JDBCUtils.getDataSource());
		String sql = "select * from product where pflag = ? order by pdate desc limit ?";
		List<Product> list = qr.query(sql, new BeanListHandler<Product>(Product.class), 0, 9);
		return list;
	}

	/**
	 * 查询总记录数
	 */
	@Override
	public Integer findCountByCid(String cid) throws SQLException {
		QueryRunner qr = new QueryRunner(JDBCUtils.getDataSource());
		String sql = "select count(*) from product where cid=?";
		Long l = (Long) qr.query(sql, new ScalarHandler(), cid);
		return l.intValue();
	}

	@Override
	public List<Product> findPageByCid(String cid, int begin, Integer pageSize)throws SQLException {
		QueryRunner qr = new QueryRunner(JDBCUtils.getDataSource());
		String sql = "select * from product where pflag = ? and cid = ? order by pdate desc limit ?,?";
		List<Product> list = qr.query(sql, new BeanListHandler<Product>(Product.class), 0, cid, begin, pageSize);
		return list;
	}

	/**
	 * 根据商品的pid查询商品的信息
	 */
	@Override
	public Product findByPid(String pid) throws SQLException {
		QueryRunner qr = new QueryRunner(JDBCUtils.getDataSource());
		String sql = "select * from product where pid=?";
		Product product = qr.query(sql, new BeanHandler<Product>(Product.class), pid);
		return product;
	}

	/**
	 * 查询总记录数
	 */
	@Override
	public Integer findCount() throws SQLException {
		QueryRunner qr = new QueryRunner(JDBCUtils.getDataSource());
		String sql = "select count(*) from product where pflag=?";
		Long count = (Long) qr.query(sql, new ScalarHandler(), 0);
		return count.intValue();
	}

	/**
	 * 查询一页的数据信息
	 */
	@Override
	public List<Product> findByPage(int begin, Integer pageSize)throws SQLException {
		QueryRunner qr = new QueryRunner(JDBCUtils.getDataSource());
		String sql = "select * from product where pflag=? order by pdate desc limit ?,?";
		List<Product> list = qr.query(sql, new BeanListHandler<Product>(Product.class), 0, begin, pageSize);
		return list;
	}

	/**
	 * 添加商品的方法
	 */
	@Override
	public void save(Product product) throws SQLException {
		QueryRunner qr = new QueryRunner(JDBCUtils.getDataSource());
		String sql = "insert into product values(?,?,?,?,?,?,?,?,?,?)";
		Object[] params = { product.getPid(), product.getPname(), product.getMarket_price(), product.getShop_price(),
				product.getPimage(), product.getPdate(), product.getIs_hot(), product.getPdesc(), product.getPflag(),
				product.getCategory().getCid() };
		qr.update(sql, params);
	}

	/**
	 * 商品更新数据的方法
	 */
	@Override
	public void update(Product product) throws SQLException {
		QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());
		String sql = "update product set pname = ?,market_price=?,shop_price=?,pimage=?,is_hot=?,pdesc= ?,pflag=? where pid = ?";
		Object[] params = { product.getPname(), product.getMarket_price(), product.getShop_price(),
				product.getPimage(),product.getIs_hot(), product.getPdesc(), product.getPflag(),product.getPid()
				 };
		queryRunner.update(sql, params);
		
	}

	/**
	 * 查看所有下架商品的方法
	 */
	@Override
	public List<Product> findByPushDown() throws SQLException {
		QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());
		String sql = "select * from product where pflag = ? order by pdate desc";
		List<Product> list = queryRunner.query(sql, new BeanListHandler<Product>(Product.class), 1);
		return list;
	}

}
