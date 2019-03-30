package com.lakeqiu.store.dao;

import com.lakeqiu.store.domain.User;

import java.sql.SQLException;

/**
 * 用户模块Dao接口
 * @author lakeqiu
 */
public interface UserDao {
    /**
     * 向数据库插入用户信息
     * @param user 用户信息类
     * @throws SQLException 错误
     */
    void userRegist(User user) throws SQLException;

    /**
     * 根据激活码查询有没有这个用户，主要用于激活
     * @param code 激活码
     * @return 用户信息类
     * @throws SQLException 错误
     */
    User userActive(String code) throws SQLException;

    /**
     * 更新用户信息
     * @param user 用户信息类
     * @throws SQLException 错误
     */
    void updateUser(User user) throws SQLException;

    /**
     * 用户登录
     * @param user 用户类
     * @return
     * @throws SQLException
     */
    User userLogin(User user) throws SQLException;
}
