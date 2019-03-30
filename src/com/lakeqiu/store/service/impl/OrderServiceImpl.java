package com.lakeqiu.store.service.impl;

import com.lakeqiu.store.dao.OrderDao;
import com.lakeqiu.store.dao.impl.OrderDaoImpl;
import com.lakeqiu.store.domain.Order;
import com.lakeqiu.store.domain.OrderItem;
import com.lakeqiu.store.domain.PageModel;
import com.lakeqiu.store.domain.User;
import com.lakeqiu.store.service.OrderService;
import com.lakeqiu.store.utils.BeanFactory;
import com.lakeqiu.store.utils.JDBCUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * 订单模块Service
 * @author lakeqiu
 */
public class OrderServiceImpl implements OrderService {

    // 用下面的方法，配合application.xml和BeanFactory.java可以解耦，使用不同数据库
//    private OrderDao orderDao = new OrderDaoImpl();

    private OrderDao orderDao = (OrderDao) BeanFactory.creatObject("OrderDao");

    /**
     * 将订单和订单项插入数据库
     * 不需要关闭连接，因为有c3p0回收连接
     * @param order 订单（包含电订单项）
     * @throws SQLException 错误
     */
    @Override
    public void saveOrder(Order order) throws SQLException {
        Connection conn = null;
        try {
            // 获取连接
            conn = JDBCUtils.getConnection();
            // 开启事务
            conn.setAutoCommit(false);
            // 保存订单类
            // 因为开启了事务，要保证连接是同一连接，故需把连接也传过去
            orderDao.saveOrder(conn, order);
            // 保存订单项类
            for (OrderItem orderItem : order.getList()) {
                orderDao.saveOrderItem(conn, orderItem);
            }
            // 提交事务
            conn.commit();
        } catch (Exception e) { // 事务失败的时候回滚事务
            conn.rollback();
        }
    }

    /**
     * 查询用户当前页面要显示的订单及分页信息
     * @param user 当前用户
     * @param curNum 当前页面
     * @return PageModel
     * @throws Exception 错误
     */
    @Override
    public PageModel findMyOrderWithPage(User user, int curNum) throws Exception {
        // 创建PageModel对象，目的：计算并携带分页参数
        int totalRecords = orderDao.getTotalRecords(user);
        PageModel pageModel = new PageModel(curNum, totalRecords, 3);
        // 关联集合
        List list = orderDao.findMyOrderWithPage(user, pageModel.getStartIndex(), pageModel.getPageSize());
        pageModel.setList(list);
        // 关联url,点击的时候再查询
        pageModel.setUrl("OrderServlet?method=findMyOrderWithPage");
        return pageModel;
    }

    /**
     * 根据oid查询订单
     * @param oid 订单oid
     * @return 订单
     * @throws Exception 错误
     */
    @Override
    public Order findOrderByOid(String oid) throws Exception {
        return orderDao.findOrderByOid(oid);
    }

    /**
     * 更新订单状态
     * @param order 订单类
     * @throws SQLException 错误
     */
    @Override
    public void updateOrder(Order order) throws SQLException {
        orderDao.updateOrder(order);
    }

    /**
     * 查询当前页订单
     * @param curNum 当前页
     * @return
     */
    @Override
    public PageModel findOrdersWithPage(int curNum) throws SQLException {
        // 获取订单总数
        int totalOrders = orderDao.getTotalOrders();
        // 创建PageModel对象
        PageModel pageModel = new PageModel(curNum, totalOrders, 10);
        // 获取当前页订单
        List list = orderDao.findOrdersWithPage(pageModel.getStartIndex(), pageModel.getPageSize());
        // 关联集合
        pageModel.setList(list);
        // 关联url
        pageModel.setUrl("AdminOrderServlet?method=findOrdersWithPage");
        return pageModel;
    }

    /**
     * 查询当前页状态订单
     * @param curNum 当前页
     * @param str 订单状态
     * @return
     */
    @Override
    public PageModel findOrdersWithPage(int curNum, String str) throws SQLException {
        // 获取状态订单总数
        int totalOrders = orderDao.getTotalOrders(str);
        // 创建PageModel对象
        PageModel pageModel = new PageModel(curNum, totalOrders, 10);
        // 获取当前页订单
        List list = orderDao.findOrdersWithPage(str ,pageModel.getStartIndex(), pageModel.getPageSize());
        // 关联集合
        pageModel.setList(list);
        // 关联url
        pageModel.setUrl("AdminOrderServlet?method=findOrdersWithPage&state=" + str);
        return pageModel;
    }
}
