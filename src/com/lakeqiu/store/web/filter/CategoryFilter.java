package com.lakeqiu.store.web.filter;

import com.lakeqiu.store.domain.Category;
import com.lakeqiu.store.service.CategoryService;
import com.lakeqiu.store.service.impl.CategoryServiceImpl;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * 给页面添加上分类信息
 * @author lakeqiu
 */
public class CategoryFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        // 强转
        HttpServletRequest req = (HttpServletRequest) servletRequest;

        // 调用业务层逻辑分类查询功能，返回集合
        CategoryService categoryService = new CategoryServiceImpl();
        List<Category> lists = null;
        try {
            lists = categoryService.getAllCates();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 将分类信息放入request中
        req.setAttribute("allCats", lists);
        filterChain.doFilter(req, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
