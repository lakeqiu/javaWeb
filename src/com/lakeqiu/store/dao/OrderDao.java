package com.lakeqiu.store.dao;

import com.lakeqiu.store.domain.Order;
import com.lakeqiu.store.domain.OrderItem;
import com.lakeqiu.store.domain.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * 订单模块Dao接口
 * @author lakeqiu
 */
public interface OrderDao {

    /**
     * 插入订单类
     * @param conn 数据库连接，因为开启了事务，故连接要保持一致
     * @param order 订单类
     * @throws SQLException 错误
     */
    void saveOrder(Connection conn, Order order) throws SQLException;

    /**
     * 插入订单项类
     * @param conn 数据库连接，因为开启了事务，故连接要保持一致
     * @param orderItem 订单项类
     * @throws SQLException 错误
     */
    void saveOrderItem(Connection conn, OrderItem orderItem) throws SQLException;

    /**
     * 查询当前用户的订单数
     * @param user 用户
     * @return 订单总数
     * @throws SQLException 错误
     */
    int getTotalRecords(User user) throws SQLException;

    /**
     * 查询当前用户当前页面下的所有订单（附有订单下的订单项，与订单项下的商品信息）
     * @param user 当前用户
     * @param startIndex 当前页面参数
     * @param pageSize 每页显示订单个数
     * @return 订单类集合
     * @throws Exception 错误
     */
    List findMyOrderWithPage(User user, int startIndex, int pageSize) throws Exception;

    /**
     * 根据oid查询订单（附有订单下的订单项，与订单项下的商品信息）
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
     * 查询订单总数
     * @return
     */
    int getTotalOrders() throws SQLException;

    /**
     * 查询当前页订单
     * @param startIndex 当前页参数
     * @param pageSize 当前页显示订单数量
     * @return
     */
    List findOrdersWithPage(int startIndex, int pageSize) throws SQLException;

    /**
     * 查询订状态单总数
     * @param str 订单状态
     * @return
     */
    int getTotalOrders(String str) throws SQLException;

    /**
     * 查询当前状态页订单
     * @param str 订单状态
     * @param startIndex 当前页参数
     * @param pageSize 当前页显示订单数量
     * @return
     */
    List findOrdersWithPage(String str, int startIndex, int pageSize) throws SQLException;
}
