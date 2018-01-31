package com.itheima.store.service.impl;

import java.sql.SQLException;
import java.util.List;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.statistics.CacheUsageListener;

import com.itheima.store.dao.CategoryDao;
import com.itheima.store.dao.impl.CategoryDaoImpl;
import com.itheima.store.domain.Category;
import com.itheima.store.service.CategoryService;
import com.itheima.store.utils.BeanFactory;
import com.itheima.store.utils.CacheUtils;
/**
 * 商品分类的Service的实现类
 * @author 趴布里克
 *
 */
public class CategoryServiceImpl implements CategoryService {

	/**
	 * 查询所有分类的方法
	 */
	@Override
	public List<Category> findAll()  throws SQLException {
		/*CategoryDao categoryDao = new CategoryDaoImpl();
		return categoryDao.findAll();*/
		
		/**
		 * 缓存-->内存-->空间换时间
		 */
		//读取配置文件
		/*CacheManager cacheManager = CacheManager.create(CategoryServiceImpl.class.getClassLoader().getResourceAsStream("ehcache.xml"));
		//找到名称为categoryCache的缓存区
		Cache cache = cacheManager.getCache("categoryCache");
		Element element = cache.get("list");
		List<Category> list = null;
		if(element == null) {
			//缓存中没有
			System.out.println("缓存中没有");
			CategoryDao categoryDao = new CategoryDaoImpl();
			list = categoryDao.findAll();
			element = new Element("list",list);
			cache.put(element);
		}else {
			//缓存中有
			System.out.println("缓存中有");
			list = (List<Category>)element.getObjectValue();
		}
		return list;*/
		
		
		/**
		 * 优化
		 * -->工具类
		 */
		CategoryDao categoryDao = (CategoryDao) BeanFactory.getBean("categoryDao");
		List<Category> list = (List<Category>) CacheUtils.get("categoryCache", "list");
		if(list == null) {
			list = categoryDao.findAll();
			CacheUtils.save("categoryCache", "list", list);
		}
		return list;
	}

	/**
	 * 添加分类的方法
	 */
	@Override
	public void save(Category category) throws SQLException {
		CategoryDao categoryDao = (CategoryDao) BeanFactory.getBean("categoryDao");
		categoryDao.save(category);
		
		//保存后清除缓存
		CacheUtils.delete("categoryCache", "list");
	}

	/**
	 * 编辑分类的方法
	 */
	@Override
	public Category findByCid(String cid) throws SQLException {
		CategoryDao categoryDao = (CategoryDao) BeanFactory.getBean("categoryDao");
		return categoryDao.findByCid(cid);
	}

	/**
	 * 修改分类的方法
	 */
	@Override
	public void update(Category category) throws SQLException {
		CategoryDao categoryDao = (CategoryDao) BeanFactory.getBean("categoryDao");
		categoryDao.update(category);
		
		//修改后清除缓存
		CacheUtils.delete("categoryCache", "list");
	}

	/**
	 * 删除分类的方法
	 */
	@Override
	public void delete(String cid) throws SQLException {
		CategoryDao categoryDao = (CategoryDao) BeanFactory.getBean("categoryDao");
		categoryDao.delete(cid);
		
		//删除后清除缓存
		CacheUtils.delete("categoryCache", "list");
	}

}
