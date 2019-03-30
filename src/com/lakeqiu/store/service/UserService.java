package com.lakeqiu.store.service;

import com.lakeqiu.store.domain.User;

import java.sql.SQLException;

/**
 * 业务层用户模块service接口
 * @author lakeqiu
 */
public interface UserService {
    /**
     * 实现注册功能
     * @param user 用户信息类
     * @throws SQLException 错误
     */
    void userRegist(User user) throws SQLException;

    /**
     * 实现激活功能
     * @param code 激活码
     * @throws SQLException 错误
     */
    Boolean userActive(String code) throws SQLException;

    /**
     * 实现登录功能
     * @param user 用户类
     * @return
     * @throws SQLException
     */
    User userLogin(User user) throws SQLException;
}
