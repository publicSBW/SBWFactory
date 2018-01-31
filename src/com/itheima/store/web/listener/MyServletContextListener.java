package com.itheima.store.web.listener;

import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.itheima.store.dao.CategoryDao;
import com.itheima.store.dao.impl.CategoryDaoImpl;
import com.itheima.store.domain.Category;
import com.itheima.store.utils.CacheUtils;

/**
 * 缓存的Listener
 * 优化缓存：在服务器启动的时候就去数据库查询,存入缓存
 * @author 趴布里克
 *
 */
public class MyServletContextListener implements ServletContextListener {

    public void contextInitialized(ServletContextEvent sce)  { 
    	CategoryDao categoryDao = new CategoryDaoImpl();
    	try{
    		List<Category> list = categoryDao.findAll();
    		CacheUtils.save("categoryCache", "list", list);
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    }

    public void contextDestroyed(ServletContextEvent sce)  { 
    	
    }
	
}
