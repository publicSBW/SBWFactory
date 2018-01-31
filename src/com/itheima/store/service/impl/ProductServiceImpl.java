package com.itheima.store.service.impl;

import java.sql.SQLException;
import java.util.List;

import com.itheima.store.dao.ProductDao;
import com.itheima.store.dao.impl.ProductDaoImpl;
import com.itheima.store.domain.PageBean;
import com.itheima.store.domain.Product;
import com.itheima.store.service.ProductService;
import com.itheima.store.utils.BeanFactory;
/**
 * producr的Service的实现类
 * @author 趴布里克
 *
 */
public class ProductServiceImpl implements ProductService {

	/**
	 *查询最新商品
	 */
	@Override
	public List<Product> findByNew() throws SQLException {
		ProductDao productDao = (ProductDao) BeanFactory.getBean("productDao");
		
		return productDao.findByNew();
	}

	/**
	 * 查询热门商品
	 */
	@Override
	public List<Product> findByHot() throws SQLException {
		ProductDao productDao = (ProductDao) BeanFactory.getBean("productDao");
		
		return productDao.findByHot();
	}

	/**
	 * 分类查询
	 */
	@Override
	public PageBean<Product> findByPageCid(String cid, Integer currPage)throws SQLException {
		PageBean<Product> pageBean = new PageBean<Product>();
		//设置当前页数
		pageBean.setCurrPage(currPage);
		//设置每页显示的记录数
		Integer pageSize = 12;
		pageBean.setPageSize(pageSize);
		//设置总记录数
		ProductDao productDao = (ProductDao) BeanFactory.getBean("productDao");
		Integer totalCount = productDao.findCountByCid(cid);
		pageBean.setTotalCount(totalCount);
		//设置总页数
		double tc = totalCount;
		Double num = Math.ceil(tc/pageSize);
		pageBean.setTotalPage(num.intValue());
		//设置每页显示的数据
		int begin = (currPage - 1) * pageSize;
		List<Product> list = productDao.findPageByCid(cid,begin,pageSize);
		pageBean.setList(list);
		
		return pageBean;
	}

	/**
	 * 根据商品的pid查询商品的信息
	 */
	@Override
	public Product findByPid(String pid) throws SQLException {
		ProductDao productDao = (ProductDao) BeanFactory.getBean("productDao");
		Product product = productDao.findByPid(pid);
		return product;
	}

	/**
	 * 分页查询所有商品的方法
	 */
	@Override
	public PageBean<Product> findByPage(Integer currPage) throws SQLException {
		PageBean<Product> pageBean = new PageBean<Product>();
		//设置当前页数
		pageBean.setCurrPage(currPage);
		//设置每页显示的记录数
		Integer pageSize = 10;
		pageBean.setPageSize(pageSize);
		//设置总记录数
		ProductDao productDao = (ProductDao) BeanFactory.getBean("productDao");
		Integer totalCount = productDao.findCount();
		pageBean.setTotalCount(totalCount);
		//设置总页数
		double tc = totalCount;
		Double num = Math.ceil(tc/pageSize);
		pageBean.setTotalPage(num.intValue());
		//设置每页显示的数据
		int begin = (currPage - 1) * pageSize;
		List<Product> list = productDao.findByPage(begin,pageSize);
		pageBean.setList(list);
		
		return pageBean;
	}

	/**
	 * 添加商品的方法
	 */
	@Override
	public void save(Product product) throws SQLException {
		ProductDao productDao  = (ProductDao) BeanFactory.getBean("productDao");
		productDao.save(product);
	}

	/**
	 * 商品下架的方法
	 */
	@Override
	public void update(Product product) throws SQLException {
		ProductDao productDao = (ProductDao) BeanFactory.getBean("productDao");
		productDao.update(product);
	}

	/**
	 * 查看所有下架商品的方法
	 */
	@Override
	public List<Product> findByPushDown() throws SQLException {
		ProductDao productDao = (ProductDao) BeanFactory.getBean("productDao");
		return productDao.findByPushDown();
	}

}
