package com.lakeqiu.store.web.servlet;

import com.lakeqiu.store.domain.PageModel;
import com.lakeqiu.store.domain.Product;
import com.lakeqiu.store.service.ProductService;
import com.lakeqiu.store.service.impl.ProductServiceImpl;
import com.lakeqiu.store.web.base.BaseServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author lakeqiu
 */
public class ProductServlet extends BaseServlet {
    /**
     * 点击查看商品详情
     * @param req
     * @param resp
     * @return
     * @throws Exception
     */
    public String findProductByPid(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        // 获取商品pid
        String pid = req.getParameter("pid");
        // 调用业务层功能，根据商品pid查询商品详情，返回商品详情类
        ProductService productService = new ProductServiceImpl();
        Product product = productService.findProductByPid(pid);
        // 将商品详情类放入request中
        req.setAttribute("product", product);

        // 添加到浏览历史记录中
        List<Product> history = (List<Product>) req.getSession().getAttribute("history");
        history.add(0,product);
        // 返回商品详情页
        return "/jsp/product_info.jsp";
    }

    /**
     * 分页显示商品，显示当前页面要显示的商品
     * @param req
     * @param resp
     * @return
     * @throws Exception
     */
    public String findProductsByCidWithPage(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        // 获取cid(分类id)，num(当前页面)
        String cid = req.getParameter("cid");
        int curNum = Integer.valueOf(req.getParameter("num"));
        // 调用业务层功能，以分页形式查询当前类别商品
        // 返回PageModel对象，（1.当前页面信息， 2.分页， 3.url）
        ProductService productService = new ProductServiceImpl();
        PageModel pageModel = productService.findProductsByCidWithPage(cid, curNum);
        // 将PageModel对象放入request中
        req.setAttribute("page", pageModel);
        // 将浏览历史记录放入request中
        List<Product> history = (List<Product>) req.getSession().getAttribute("history");
        req.setAttribute("history", history);
        // 返回响应界面
        return "/jsp/product_list.jsp";
    }

    public String searchProductByNameWithPage(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        // 获取搜索名字，当前页面
        String name = req.getParameter("name");
        int curNum = Integer.parseInt(req.getParameter("num"));
        // 调用业务层功能，以分页形式，模糊查询搜索的商品
        // 返回PageModel对象（1.当前页面信息， 2.分页， 3.url）
        ProductService productService = new ProductServiceImpl();
        PageModel pageModel = productService.searchProductByNameWithPage(name, curNum);
        // 将PageModel放入request中
        req.setAttribute("page", pageModel);
        // 返回响应界面
        return "/jsp/product_list.jsp";
    }
}
