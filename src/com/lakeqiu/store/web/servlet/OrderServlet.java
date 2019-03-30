package com.lakeqiu.store.web.servlet;

import com.lakeqiu.store.domain.*;
import com.lakeqiu.store.service.OrderService;
import com.lakeqiu.store.service.impl.OrderServiceImpl;
import com.lakeqiu.store.utils.PaymentUtil;
import com.lakeqiu.store.utils.UUIDUtils;
import com.lakeqiu.store.web.base.BaseServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * 订单模块Servlet
 * @author lakeqiu
 */
public class OrderServlet extends BaseServlet {
    /**
     * 提交订单功能，把订单及其下面的订单项存入数据库
     * @param req request
     * @param resp response
     * @return 相应界面
     * @throws Exception 错误
     */
    public String saveOrder(HttpServletRequest req, HttpServletResponse resp) throws Exception{
        // 获取登录的用户
        User user = (User) req.getSession().getAttribute("User");
        // 判断用户有没有登录
        if (null == user){
            // 用户没有登录，跳转页面提醒用户登录
            req.setAttribute("msg", "请登录");
            return "/jsp/info.jsp";
        }
        // 用户登录了
        // 获取购物车
        Cart cart = (Cart) req.getSession().getAttribute("cart");
        // 创建订单类
        Order order = new Order();
        // 存放数据,一些从购物车中获取，一些从用户中获取
        order.setOid(UUIDUtils.getCode());
        order.setOrdertime(new Date());
        order.setTotal(cart.getTotal());
        order.setState(1);
        order.setUser(user);

        // 遍历购物项，同时创建订单项类，赋值，并添加到订单类
        for (CartItem cartItem : cart.getCartItems()) {
            // 创建订单项类
            OrderItem orderItem = new OrderItem();
            orderItem.setItemid(UUIDUtils.getCode());
            orderItem.setQuantity(cartItem.getNum());
            orderItem.setTotal(cartItem.getSubTotal());
            // 设置商品类
            orderItem.setProduct(cartItem.getProduct());
            // 设置属于哪个订单
            orderItem.setOrder(order);

            // 将订单项类加入订单类
            order.getList().add(orderItem);
        }

        // 调用业务层功能，保存订单项类与订单类（数据库）,订单项类在订单类的list中可以找到
        OrderService orderService = new OrderServiceImpl();
        orderService.saveOrder(order);

        // 清空购物车
        cart.claerCart();
        // 将订单类加入request中
        req.setAttribute("order", order);
        // 转发订单页面
        return "/jsp/order_info.jsp";
    }

    /**
     * 当前用户当前页面要显示的订单
     * @param req request
     * @param resp response
     * @return 响应界面
     * @throws Exception 错误
     */
    public String findMyOrderWithPage(HttpServletRequest req, HttpServletResponse resp) throws Exception{
        // 获取登录用户信息
        User user = (User) req.getSession().getAttribute("User");
        // 获取当前页
        int curNum = Integer.parseInt(req.getParameter("num"));
        // 调用业务层功能，查询当前用户订单，返回PageModel
        OrderService orderService = new OrderServiceImpl();
        // PageModel:1. 分页参数 2. 公共url 3.用户当前页的订单（集合），每笔订单对应的订单项，订单项上的商品信息
        PageModel pageModel = orderService.findMyOrderWithPage(user, curNum);
        // 将PageModel放入request中
        req.setAttribute("page", pageModel);
        // 返回
        return "/jsp/order_list.jsp";
    }

    /**
     * 查询订单
     * @param req request
     * @param resp response
     * @return 响应界面
     * @throws Exception 错误
     */
    public String findOrderByOid(HttpServletRequest req, HttpServletResponse resp) throws Exception{
        // 获取订单oid
        String oid = req.getParameter("oid");
        // 调用业务层功能，根据订单oid查询订单信息
        OrderService orderService = new OrderServiceImpl();
        Order order = orderService.findOrderByOid(oid);
        // 将订单放入request中
        req.setAttribute("order", order);
        // 返回响应界面
        return "/jsp/order_info.jsp";
    }

    /**
     * 支付
     * @param req
     * @param resp
     * @return
     * @throws Exception
     */
    public String payOrder(HttpServletRequest req, HttpServletResponse resp) throws Exception{
        // 获取订单oid，收货人，地址，电话，银行
        String oid = req.getParameter("oid");
        String address = req.getParameter("address");
        String name = req.getParameter("name");
        String telephone = req.getParameter("telephone");
        String pd_FrpId = req.getParameter("pd_FrpId");

        // 更新订单上收货人，地址，电话
        OrderService orderService = new OrderServiceImpl();
        Order order = orderService.findOrderByOid(oid);
        order.setName(name);
        order.setTeltphone(telephone);
        order.setAddress(address);
        orderService.updateOrder(order);

        // 向易宝支付发送参数
        // 把付款所需要的参数准备好:
        String p0_Cmd = "Buy";
        //商户编号
        String p1_MerId = "10001126856";
        //订单编号
        String p2_Order = oid;
        //金额
        String p3_Amt = "0.01";
        String p4_Cur = "CNY";
        String p5_Pid = "";
        String p6_Pcat = "";
        String p7_Pdesc = "";
        //接受响应参数的Servlet
        String p8_Url = "http://localhost:8080/store_war_exploded/OrderServlet?method=callBack";
        String p9_SAF = "";
        String pa_MP = "";
        String pr_NeedResponse = "1";
        //公司的秘钥
        String keyValue = "69cl522AV6q613Ii4W6u8K6XuW8vM1N6bFgyv769220IuYe9u37N4y7rI4Pl";

        //调用易宝的加密算法,对所有数据进行加密,返回电子签名
        String hmac = PaymentUtil.buildHmac(p0_Cmd, p1_MerId, p2_Order, p3_Amt, p4_Cur, p5_Pid, p6_Pcat, p7_Pdesc, p8_Url, p9_SAF, pa_MP, pd_FrpId, pr_NeedResponse, keyValue);

        StringBuffer sb = new StringBuffer("https://www.yeepay.com/app-merchant-proxy/node?");
        sb.append("p0_Cmd=").append(p0_Cmd).append("&");
        sb.append("p1_MerId=").append(p1_MerId).append("&");
        sb.append("p2_Order=").append(p2_Order).append("&");
        sb.append("p3_Amt=").append(p3_Amt).append("&");
        sb.append("p4_Cur=").append(p4_Cur).append("&");
        sb.append("p5_Pid=").append(p5_Pid).append("&");
        sb.append("p6_Pcat=").append(p6_Pcat).append("&");
        sb.append("p7_Pdesc=").append(p7_Pdesc).append("&");
        sb.append("p8_Url=").append(p8_Url).append("&");
        sb.append("p9_SAF=").append(p9_SAF).append("&");
        sb.append("pa_MP=").append(pa_MP).append("&");
        sb.append("pd_FrpId=").append(pd_FrpId).append("&");
        sb.append("pr_NeedResponse=").append(pr_NeedResponse).append("&");
        sb.append("hmac=").append(hmac);

        System.out.println(sb.toString());
        // 使用重定向：
        resp.sendRedirect(sb.toString());

        return null;
    }

    /**
     * 易宝支付响应
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public String callBack(HttpServletRequest request, HttpServletResponse response) throws Exception{
        // 接收易宝支付数据
        // 验证请求来源和数据有效性
        // 阅读支付结果参数说明
        // System.out.println("==============================================");
        String p1_MerId = request.getParameter("p1_MerId");
        String r0_Cmd = request.getParameter("r0_Cmd");
        String r1_Code = request.getParameter("r1_Code");
        String r2_TrxId = request.getParameter("r2_TrxId");
        String r3_Amt = request.getParameter("r3_Amt");
        String r4_Cur = request.getParameter("r4_Cur");
        String r5_Pid = request.getParameter("r5_Pid");
        String r6_Order = request.getParameter("r6_Order");
        String r7_Uid = request.getParameter("r7_Uid");
        String r8_MP = request.getParameter("r8_MP");
        String r9_BType = request.getParameter("r9_BType");
        String rb_BankId = request.getParameter("rb_BankId");
        String ro_BankOrderId = request.getParameter("ro_BankOrderId");
        String rp_PayDate = request.getParameter("rp_PayDate");
        String rq_CardNo = request.getParameter("rq_CardNo");
        String ru_Trxtime = request.getParameter("ru_Trxtime");

        // hmac
        String hmac = request.getParameter("hmac");
        // 利用本地密钥和加密算法 加密数据
        String keyValue = "69cl522AV6q613Ii4W6u8K6XuW8vM1N6bFgyv769220IuYe9u37N4y7rI4Pl";

        // 保证数据合法性
        boolean isValid = PaymentUtil.verifyCallback(hmac, p1_MerId, r0_Cmd,
                r1_Code, r2_TrxId, r3_Amt, r4_Cur, r5_Pid, r6_Order, r7_Uid,
                r8_MP, r9_BType, keyValue);
        if (isValid) {
            // 有效
            if (r9_BType.equals("1")) {
                // 支付成功，更改订单状态
                OrderService orderService = new OrderServiceImpl();
                Order order = orderService.findOrderByOid(r6_Order);
                order.setState(2);
                orderService.updateOrder(order);
                // 向request中放入提示信息
                request.setAttribute("msg", "支付成功！订单号：" + r6_Order + "金额：" + r3_Amt);
                // 转发
                return "/jsp/info.jsp";
            } else if (r9_BType.equals("2")) {
                // 修改订单状态:
                // 服务器点对点，来自于易宝的通知
                System.out.println("收到易宝通知，修改订单状态！");
                // 回复给易宝success，如果不回复，易宝会一直通知
                response.getWriter().print("success");
            }

        } else {
            throw new RuntimeException("数据被篡改！");
        }
        return null;
    }
}
