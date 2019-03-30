package com.lakeqiu.store.service;

import com.lakeqiu.store.domain.Order;
import com.lakeqiu.store.domain.PageModel;
import com.lakeqiu.store.domain.User;

import java.sql.SQLException;

/**
 * @author lakeqiu
 */
public interface OrderService {
    /**
     * 将订单和订单项插入数据库
     * 不需要关闭连接，因为有c3p0回收连接
     * @param order 订单（包含电订单项）
     * @throws SQLException 错误
     */
    void saveOrder(Order order) throws SQLException;

    /**
     * 查询用户当前页面要显示的订单及分页信息
     * @param user 当前用户
     * @param curNum 当前页面
     * @return PageModel
     * @throws Exception 错误
     */
    PageModel findMyOrderWithPage(User user, int curNum) throws Exception;

    /**
     * 根据oid查询订单
     * @param oid 订单oid
     * @return 订单
     * @throws Exception 错误
     */
    Order findOrderByOid(String oid) throws Exception;

    /**
     * 更新订单状态
     * @param order 订单类
     * @throws SQLException 错误
     */
    void updateOrder(Order order) throws SQLException;

    /**
     * 查询当前页订单
     * @param curNum 当前页
     * @return
     */
    PageModel findOrdersWithPage(int curNum) throws SQLException;

    /**
     * 查询当前页状态订单
     * @param curNum 当前页
     * @param str 订单状态
     * @return
     */
    PageModel findOrdersWithPage(int curNum, String str) throws SQLException;
}
