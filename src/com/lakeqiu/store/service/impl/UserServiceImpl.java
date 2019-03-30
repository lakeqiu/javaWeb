package com.lakeqiu.store.service.impl;

import com.lakeqiu.store.dao.UserDao;
import com.lakeqiu.store.dao.impl.UserDaoImpl;
import com.lakeqiu.store.domain.User;
import com.lakeqiu.store.service.UserService;
import com.lakeqiu.store.utils.BeanFactory;

import java.sql.SQLException;

/**
 * 业务层用户模块service实现
 * @author lakeqiu
 */
public class UserServiceImpl implements UserService {
    // 用下面的方法，配合application.xml和BeanFactory.java可以解耦，使用不同数据库
//    private UserDao userDao = new UserDaoImpl();

    private UserDao userDao = (UserDao) BeanFactory.creatObject("UserDao");

    /**
     * 实现注册功能
     * @param user 用户信息类
     * @throws SQLException 错误
     */
    @Override
    public void userRegist(User user) throws SQLException {
        // 调用Dao
        userDao.userRegist(user);
    }

    /**
     * 实现激活功能
     * @param code 激活码
     * @throws SQLException 错误
     */
    @Override
    public Boolean userActive(String code) throws SQLException {
        // 调用dao层，查询有没有这个激活码的用户
        User user = userDao.userActive(code);
        // 有这个用户
        if (null != user){
            // 改变用户状态，未激活改为激活，并清空激活码
            user.setState(1);
            user.setCode(null);
            // 对数据库进行更新
            userDao.updateUser(user);
            // 激活成功，返回
            return true;
        }else { // 没有这一个用户，返回false
            return false;
        }
    }

    /**
     * 实现登录功能
     * @param user 用户类
     * @return
     * @throws SQLException
     */
    @Override
    public User userLogin(User user) throws SQLException {
        User user1 = userDao.userLogin(user);
        // 判断返回的用户类是否为空
        if (null == user1){
            throw new RuntimeException("密码错误！");
        }else if (0 == user1.getState()){
            // 判断是否未激活
            throw new RuntimeException("用户为激活");
        }else {
            return user1;
        }
    }
}
