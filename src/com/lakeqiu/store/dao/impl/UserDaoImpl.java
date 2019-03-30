package com.lakeqiu.store.dao.impl;

import com.lakeqiu.store.dao.UserDao;
import com.lakeqiu.store.domain.User;
import com.lakeqiu.store.utils.JDBCUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import java.sql.SQLException;

/**
 * 用户模块Dao实现
 * @author lakeqiu
 */
public class UserDaoImpl implements UserDao {

    /**
     * 向数据库插入用户信息
     * @param user 用户信息类
     * @throws SQLException 错误
     */
    @Override
    public void userRegist(User user) throws SQLException {
        String sql = "INSERT INTO user VALUES(?,?,?,?,?,?,?,?,?,?)";
        // 获取数据库链接
        QueryRunner qr = new QueryRunner(JDBCUtils.getDataSource());
        // 将数据装进数据数组
        Object[] objects = {user.getUid(), user.getUsername(), user.getPassword(), user.getName(),
        user.getEmail(), user.getTelephone(), user.getBirthaday(), user.getSex(), user.getState(),
        user.getCode()};
        // 装填数据并插入
        qr.update(sql, objects);
    }

    /**
     * 根据激活码查询有没有这个用户，主要用于激活
     * @param code 激活码
     * @return 用户信息类
     * @throws SQLException 错误
     */
    @Override
    public User userActive(String code) throws SQLException {
        String sql = "SELECT * FROM user WHERE code=?";
        QueryRunner qr = new QueryRunner(JDBCUtils.getDataSource());
        return qr.query(sql, new BeanHandler<User>(User.class), code);
    }

    /**
     * 更新用户信息
     * @param user 用户信息类
     * @throws SQLException 错误
     */
    @Override
    public void updateUser(User user) throws SQLException {
        // 全都改，当用户信息改变时也可以用
        String sql = "update user set username=?, password=?, name=?, email=?, telephone=?, birthday=?," +
                " sex=?, state=?, code=? where uid=?";
        QueryRunner qr = new QueryRunner(JDBCUtils.getDataSource());
        // 将数据放入数组
        Object[] objects = {user.getUsername(), user.getPassword(), user.getName(), user.getEmail(),
        user.getTelephone(), user.getBirthaday(), user.getSex(), user.getState(), user.getCode(),
                user.getUid()};
        // 装填数据并更新
        qr.update(sql, objects);
    }

    /**
     * 用户登录
     * @param user 用户类
     * @return
     * @throws SQLException
     */
    @Override
    public User userLogin(User user) throws SQLException {
        String sql = "SELECT * FROM user WHERE username=? AND password=?";
        QueryRunner qr = new QueryRunner(JDBCUtils.getDataSource());
        return qr.query(sql, new BeanHandler<User>(User.class), user.getUsername(), user.getPassword());
    }
}
