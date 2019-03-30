package com.lakeqiu.store.dao.impl;

import com.lakeqiu.store.dao.OrderDao;
import com.lakeqiu.store.domain.Order;
import com.lakeqiu.store.domain.OrderItem;
import com.lakeqiu.store.domain.Product;
import com.lakeqiu.store.domain.User;
import com.lakeqiu.store.utils.JDBCUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 订单模块Dao实现
 * @author lakeqiu
 */
public class OrderDaoImpl implements OrderDao {

    /**
     * 插入订单类
     * @param conn 数据库连接，因为开启了事务，故连接要保持一致
     * @param order 订单类
     * @throws SQLException 错误
     */
    @Override
    public void saveOrder(Connection conn, Order order) throws SQLException {
        String sql = "INSERT INTO orders VALUES(?,?,?,?,?,?,?,?)";
        // 传过来一个连接，故不需要在获取连接了
        QueryRunner qr = new QueryRunner();
        // 将要填充的数据放入数组
        Object[] params = {order.getOid(), order.getOrdertime(), order.getTotal(), order.getState(),
                order.getAddress(), order.getName(), order.getTeltphone(), order.getUser().getUid()};
        // 插入数据
        qr.update(conn, sql, params);
    }


    /**
     * 插入订单项类
     * @param conn 数据库连接，因为开启了事务，故连接要保持一致
     * @param orderItem 订单项类
     * @throws SQLException 错误
     */
    @Override
    public void saveOrderItem(Connection conn, OrderItem orderItem) throws SQLException {
        String sql = "INSERT INTO orderitem VALUES(?,?,?,?,?)";
        // 传过来一个连接，故不需要在获取连接了
        QueryRunner qr = new QueryRunner();
        // 将要填充的数据放入数组
        Object[] params = {orderItem.getItemid(), orderItem.getQuantity(), orderItem.getTotal(),
                orderItem.getProduct().getPid(), orderItem.getOrder().getOid()};
        // 插入数据
        qr.update(conn, sql, params);
    }

    /**
     * 查询当前用户的订单数
     * @param user 用户
     * @return 订单总数
     * @throws SQLException 错误
     */
    @Override
    public int getTotalRecords(User user) throws SQLException {
        String sql = "SELECT COUNT(*) FROM orders WHERE uid=?";
        QueryRunner qr = new QueryRunner(JDBCUtils.getDataSource());
        Long num =(Long) qr.query(sql, new ScalarHandler(), user.getUid());
        return num.intValue();
    }

    /**
     * 查询当前用户当前页面下的所有订单（附有订单下的订单项，与订单项下的商品信息）
     * @param user 当前用户
     * @param startIndex 当前页面参数
     * @param pageSize 每页显示订单个数
     * @return 订单类集合
     * @throws Exception 错误
     */
    @Override
    public List findMyOrderWithPage(User user, int startIndex, int pageSize) throws Exception {
        String sql = "SELECT * FROM orders WHERE uid=? LIMIT ?,?";
        QueryRunner qr = new QueryRunner(JDBCUtils.getDataSource());
        // 查询得到用户所有订单
        List<Order> list = qr.query(sql, new BeanListHandler<Order>(Order.class), user.getUid(), startIndex, pageSize);

        // 遍历所有订单
        for (Order order : list) {
            // 获取订单oid
            String oid = order.getOid();
            // 根据订单oid查询（多表查询）出订单项与商品信息
            sql = "SELECT * FROM orderitem o, product p WHERE o.pid=p.pid AND o.oid=?";
            // 每个订单项与对应商品放在一个map中
            List<Map<String, Object>> mapList = qr.query(sql, new MapListHandler(), oid);
            // 遍历mapList
            for (Map<String, Object> map : mapList) {
                // 由于得到的信息中有时间格式的，所以需要设置时间转换器
                // 1_创建时间类型的转换器
                DateConverter dt = new DateConverter();
                // 2_设置转换的格式
                dt.setPattern("yyyy-MM-dd");
                // 3_注册转换器
                ConvertUtils.register(dt, java.util.Date.class);

                // new出两个类来存放对应信息
                OrderItem orderItem = new OrderItem();
                Product product = new Product();

                // 将map中对应数据放入对应实例中（利用反射看对应类有没有这个方法）
                BeanUtils.populate(orderItem, map);
                BeanUtils.populate(product, map);

                // 将商品信息与订单项类相关联
                orderItem.setProduct(product);
                // 将订单项加入订单中
                order.getList().add(orderItem);
            }

        }
        return list;
    }

    /**
     * 根据oid查询订单（附有订单下的订单项，与订单项下的商品信息）
     * @param oid 订单oid
     * @return 订单
     * @throws Exception 错误
     */
    @Override
    public Order findOrderByOid(String oid) throws Exception {
        String sql = "SELECT * FROM orders WHERE oid=?";
        QueryRunner qr = new QueryRunner(JDBCUtils.getDataSource());
        // 查询出这个订单
        Order order = qr.query(sql, new BeanHandler<Order>(Order.class), oid);

        // 查询订单相关的信息（订单项，商品信息）
        sql = "SELECT * FROM orderitem o,product p WHERE o.pid=p.pid AND oid=?";
        List<Map<String, Object>> list = qr.query(sql, new MapListHandler(), oid);

        // 遍历订单项
        for (Map<String, Object> map : list) {
            // 由于得到的信息中有时间格式的，所以需要设置时间转换器
            // 1_创建时间类型的转换器
            DateConverter dt = new DateConverter();
            // 2_设置转换的格式
            dt.setPattern("yyyy-MM-dd");
            // 3_注册转换器
            ConvertUtils.register(dt, java.util.Date.class);

            // 创建两个类来存放对应信息
            Product product = new Product();
            OrderItem orderItem = new OrderItem();

            // 将map中对应数据放入对应实例中（利用反射看对应类有没有这个方法）
            BeanUtils.populate(product, map);
            BeanUtils.populate(orderItem, map);

            // 将商品信息与订单项类相关联
            orderItem.setProduct(product);
            // 将订单项加入订单中
            order.getList().add(orderItem);
        }
        return order;
    }

    /**
     * 更新订单状态
     * @param order 订单类
     * @throws SQLException 错误
     */
    @Override
    public void updateOrder(Order order) throws SQLException {
        String sql = "UPDATE orders SET ordertime=?, total=?, state=?, address=?, name=?, telephone=? WHERE oid=?";
        QueryRunner qr = new QueryRunner(JDBCUtils.getDataSource());
        Object[] params = {order.getOrdertime(), order.getTotal(), order.getState(), order.getAddress(),
        order.getName(), order.getTeltphone(), order.getOid()};
        qr.update(sql, params);
    }

    /**
     * 查询订单总数
     * @return
     */
    @Override
    public int getTotalOrders() throws SQLException {
        String sql = "SELECT COUNT(*) FROM orders";
        QueryRunner qr = new QueryRunner(JDBCUtils.getDataSource());
        Long num = (Long) qr.query(sql, new ScalarHandler());
        return num.intValue();
    }

    /**
     * 查询当前页订单
     * @param startIndex 当前页参数
     * @param pageSize 当前页显示订单数量
     * @return
     */
    @Override
    public List findOrdersWithPage(int startIndex, int pageSize) throws SQLException {
        String sql = "SELECT * FROM orders LIMIT ?,?";
        QueryRunner qr = new QueryRunner(JDBCUtils.getDataSource());
        return qr.query(sql, new BeanListHandler<Order>(Order.class), startIndex, pageSize);
    }

    /**
     * 查询当前页状态订单
     * @param str 订单状态
     * @return
     */
    @Override
    public int getTotalOrders(String str) throws SQLException {
        String sql = "SELECT COUNT(*) FROM orders WHERE state=?";
        QueryRunner qr = new QueryRunner(JDBCUtils.getDataSource());
        Long num = (Long) qr.query(sql, new ScalarHandler(), str);
        return num.intValue();
    }

    /**
     * 查询当前状态页订单
     * @param str 订单状态
     * @param startIndex 当前页参数
     * @param pageSize 当前页显示订单数量
     * @return
     */
    @Override
    public List findOrdersWithPage(String str, int startIndex, int pageSize) throws SQLException {
        String sql = "SELECT * FROM orders WHERE state=? LIMIT ?,?";
        QueryRunner qr = new QueryRunner(JDBCUtils.getDataSource());
        return qr.query(sql, new BeanListHandler<Order>(Order.class), str, startIndex, pageSize);
    }
}
