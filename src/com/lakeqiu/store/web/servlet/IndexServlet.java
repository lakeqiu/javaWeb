package com.lakeqiu.store.web.servlet;

import com.lakeqiu.store.domain.Product;
import com.lakeqiu.store.service.ProductService;
import com.lakeqiu.store.service.impl.ProductServiceImpl;
import com.lakeqiu.store.web.base.BaseServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 查询最新，最热商品并返回给首页
 * @author lakeqiu
 */
public class IndexServlet extends BaseServlet {
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        // 调用业务层功能，查询最热门，最新商品，放入两个集合中
        ProductService productService = new ProductServiceImpl();
        List<Product> lists1 = productService.findHots();
        List<Product> lists2 = productService.findNews();
        List<Product> lists3 = new ArrayList<>();

        // 将分类信息放入request中
        req.setAttribute("hots", lists1);
        req.setAttribute("news", lists2);
        req.getSession().setAttribute("history", lists3);
        // 转发到真实首页
        return "/jsp/index.jsp";
    }
}
