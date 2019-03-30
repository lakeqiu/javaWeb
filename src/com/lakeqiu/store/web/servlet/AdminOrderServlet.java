package com.lakeqiu.store.web.servlet;

import com.lakeqiu.store.domain.Order;
import com.lakeqiu.store.domain.PageModel;
import com.lakeqiu.store.service.OrderService;
import com.lakeqiu.store.service.impl.OrderServiceImpl;
import com.lakeqiu.store.web.base.BaseServlet;
import net.sf.json.JSONArray;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 后台订单管理
 * @author lakeqiu
 */
public class AdminOrderServlet extends BaseServlet {
    /**
     * 返回各种状态订单
     * @param req
     * @param resp
     * @return
     * @throws Exception
     */
    public String findOrdersWithPage(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        // 获取当前页
        int curNum = Integer.parseInt(req.getParameter("num"));
        // 获取要查询订单状态
        String str = req.getParameter("state");
        OrderService orderService = new OrderServiceImpl();
        PageModel pageModel = null;
        if (null == str || "".equals(str)){
            // 调用业务层功能，查询当前页订单，返回PageModel
            pageModel = orderService.findOrdersWithPage(curNum);
        }else {
            // 查询各种状态订单（未补款， 未发货等等）
            pageModel = orderService.findOrdersWithPage(curNum, str);
        }

        // 将数据放入request中
        req.setAttribute("page", pageModel);
        // 返回响应界面
        return "admin/order/list.jsp";
    }

    /**
     * 查询当前订单详情
     * @param req
     * @param resp
     * @return
     * @throws Exception
     */
    public String findOrderByOidWithAjax(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        // 获取订单oid
        String oid = req.getParameter("oid");
        // 调用业务层功能，查询当前订单详情
        OrderService orderService = new OrderServiceImpl();
        Order order = orderService.findOrderByOid(oid);
        // 转为json格式
        String s = JSONArray.fromObject(order.getList()).toString();
        // 响应客户端
        resp.setContentType("application/json;charset=utf-8");
        resp.getWriter().println(s);
        return null;
    }

    public String updateOrderByOid(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        // 获取订单oid
        String oid = req.getParameter("oid");
        // 调用业务层功能，根据oid查询订单
        OrderService orderService = new OrderServiceImpl();
        Order order = orderService.findOrderByOid(oid);
        // 修改订单状态
        order.setState(3);
        // 更新订单
        orderService.updateOrder(order);
        // 重定向到查询已发货订单
        resp.sendRedirect("/store_war_exploded/AdminOrderServlet?method=findOrdersWithPage&num=1&state=2");
        return null;
    }
}
