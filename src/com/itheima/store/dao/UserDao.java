package com.itheima.store.dao;

import java.sql.SQLException;

import com.itheima.store.domain.User;

/**
 * 用户模块的Dao接口
 * @author 趴布里克
 *
 */
public interface UserDao {

	/**
	 * 异步校验用户名是否存在
	 * @param username
	 * @return
	 * @throws SQLException
	 */
	User findByUsername(String username)throws SQLException;

	/**
	 * 用户注册的方法
	 * @param user
	 */
	void save(User user) throws SQLException;

	/**
	 * 查询激活码的方法
	 * @param code
	 * @return
	 * @throws SQLException
	 */
	User findByCode(String code) throws SQLException;

	/**
	 * 修改用户信息的方法
	 * @param existUser
	 * @throws SQLException
	 */
	void update(User existUser)throws SQLException;

	/**
	 * 用户登录的方法
	 * @param user
	 * @return
	 * @throws SQLException
	 */
	User login(User user)throws SQLException;

}
