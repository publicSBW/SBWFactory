package com.itheima.store.dao.impl;

import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import com.itheima.store.dao.UserDao;
import com.itheima.store.domain.User;
import com.itheima.store.utils.JDBCUtils;
/**
 * 用户模块Dao的实现类
 * @author 趴布里克
 *
 */
public class UserDaoImpl implements UserDao {

	/**
	 * 异步校验用户名是否存在
	 */
	@Override
	public User findByUsername(String username) throws SQLException {
		QueryRunner qr = new QueryRunner(JDBCUtils.getDataSource());
		String sql = "select * from user where username = ?";
		User existUser = qr.query(sql, new BeanHandler<User>(User.class), username);
		return existUser;
	}

	/**
	 * 用户注册的方法
	 * @throws SQLException 
	 */
	@Override
	public void save(User user) throws SQLException {
		QueryRunner qr = new QueryRunner(JDBCUtils.getDataSource());
		String sql = "insert into user values(?,?,?,?,?,?,?,?,?,?)";
		Object[] params = {user.getUid(), user.getUsername(), user.getPassword(), user.getName(),
				user.getEmail(), user.getTelephone(), user.getBirthday(), user.getSex(),
				user.getState(), user.getCode()};
		qr.update(sql, params);
	}

	/**
	 * 查询激活码的方法
	 */
	@Override
	public User findByCode(String code) throws SQLException {
		QueryRunner qr = new QueryRunner(JDBCUtils.getDataSource());
		String sql = "select * from user where code=?";
		User existUser = qr.query(sql, new BeanHandler<User>(User.class), code);
		return existUser;
	}

	/**
	 * 修改用户信息的方法
	 */
	@Override
	public void update(User existUser) throws SQLException {
		QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());
		String sql = "update user set username = ?,password = ?,name=?,email=?,telephone=?,birthday=?,sex=?,state=?,code=? where uid = ?";
		Object[] params = {  existUser.getUsername(), existUser.getPassword(), existUser.getName(), existUser.getEmail(),existUser.getTelephone(),
				existUser.getBirthday(), existUser.getSex(), existUser.getState(), existUser.getCode(),existUser.getUid() };
		queryRunner.update(sql, params);
		
	}

	/**
	 * 用户登录的方法
	 */
	@Override
	public User login(User user) throws SQLException {
		QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());
		String sql = "select * from user where username=? and password=? and state=?";
		User existUser = queryRunner.query(sql, new BeanHandler<User>(User.class), user.getUsername(), user.getPassword(), 2);
		return existUser;
	}

}
