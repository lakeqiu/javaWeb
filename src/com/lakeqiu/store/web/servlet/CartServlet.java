package com.lakeqiu.store.web.servlet;

import com.lakeqiu.store.domain.Cart;
import com.lakeqiu.store.domain.CartItem;
import com.lakeqiu.store.domain.Product;
import com.lakeqiu.store.service.ProductService;
import com.lakeqiu.store.service.impl.ProductServiceImpl;
import com.lakeqiu.store.web.base.BaseServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author lakeqiu
 */
public class CartServlet extends BaseServlet {
    /**
     * 添加商品到购物车
     * @param req
     * @param resp
     * @return
     * @throws Exception
     */
    public String addCartItemToCart(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        // 从session里获取购物车对象
        Cart cart = (Cart) req.getSession().getAttribute("cart");
        if (cart == null){
            // 如果获取不到购物车，就创建购物车，放入session中
            cart = new Cart();
            req.getSession().setAttribute("cart", cart);
        }
        // 在有购物车的情况下
        // 获取商品pid，购买数量
        String pid = req.getParameter("pid");
        int num = Integer.parseInt(req.getParameter("quantity"));
        // 根据商品pid查询商品信息，获得商品详情类
        ProductService productService = new ProductServiceImpl();
        Product product = productService.findProductByPid(pid);
        // 将商品详情类与购买数量添加到购物项中
        CartItem cartItem = new CartItem(product, num);
        // 添加购物项到购物车
        cart.addCartItemToCart(cartItem);
        // 重定向到/jsp/cart.jsp
        resp.sendRedirect("/store_war_exploded/jsp/cart.jsp");

        return null;
    }

    /**
     * 移除购物项
     * @param req
     * @param resp
     * @return
     * @throws Exception
     */
    public String removeCartItem(HttpServletRequest req, HttpServletResponse resp) throws Exception{
        // 获取要移除商品的pid
        String pid = req.getParameter("pid");
        // 获取购物车
        Cart cart = (Cart) req.getSession().getAttribute("cart");
        // 移除要移除的购物项
        cart.removeCartItem(pid);
        // 重定向到/jsp/cart.jsp
        resp.sendRedirect("/store_war_exploded/jsp/cart.jsp");

        return null;
    }

    /**
     * 清空购物车
     * @param req
     * @param resp
     * @return
     * @throws Exception
     */
    public String clearCartItem(HttpServletRequest req, HttpServletResponse resp) throws Exception{
        // 获取购物车
        Cart cart = (Cart) req.getSession().getAttribute("cart");
        // 清空购物车
        cart.claerCart();
        // 重定向到/jsp/cart.jsp
        resp.sendRedirect("/store_war_exploded/jsp/cart.jsp");

        return null;
    }
}
