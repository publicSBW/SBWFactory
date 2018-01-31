package com.itheima.store.service.impl;

import java.sql.SQLException;

import com.itheima.store.dao.UserDao;
import com.itheima.store.dao.impl.UserDaoImpl;
import com.itheima.store.domain.User;
import com.itheima.store.service.UserService;
import com.itheima.store.utils.BeanFactory;
import com.itheima.store.utils.MailUtils;
import com.itheima.store.utils.UUIDUtils;
/**
 * 用户模块Service的实现类
 * @author 趴布里克
 *
 */
public class UserServiceImpl implements UserService {

	/**
	 * 异步校验用户名是否存在
	 */
	@Override
	public User findByUsername(String username) throws SQLException{
		UserDao userDao = (UserDao) BeanFactory.getBean("userDao");
		
		return userDao.findByUsername(username);
	}

	/**
	 * 用户注册的方法
	 */
	@Override
	public void save(User user)  throws SQLException{
		UserDao userDao = (UserDao) BeanFactory.getBean("userDao");
		user.setUid(UUIDUtils.getUUID());
		//1未激活,2激活
		user.setState(1);
		String code = UUIDUtils.getUUID()+UUIDUtils.getUUID();
		user.setCode(code);
		userDao.save(user);
		
		// 发送激活邮件:
		MailUtils.sendMail(user.getEmail(), code);
	}

	/**
	 * 查询激活码
	 */
	@Override
	public User findByCode(String code) throws SQLException {
		UserDao userDao = (UserDao) BeanFactory.getBean("userDao");
		
		return userDao.findByCode(code);
	}

	/**
	 * 修改用户信息(状态码)的方法
	 */
	@Override
	public void update(User existUser) throws SQLException {
		UserDao userDao = (UserDao) BeanFactory.getBean("userDao");
		userDao.update(existUser);
		
	}

	/**
	 * 用户登录的方法
	 */
	@Override
	public User login(User user) throws SQLException {
		UserDao userDao = (UserDao) BeanFactory.getBean("userDao");
		return userDao.login(user);
	}

}
