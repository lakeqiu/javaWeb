package com.lakeqiu.store.web.filter;

import com.lakeqiu.store.domain.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 权限访问过滤器
 * @author lakeqiu
 */
public class PriviledgeFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 强转
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        // 获取用户，判断用户是否登录
        User user = (User) req.getSession().getAttribute("User");
        // 用户已经登录
        if (user != null){
            // 放行
            chain.doFilter(req, resp);
        }else { // 用户未登录
            // 重定向到登录页
            resp.sendRedirect("/store_war_exploded/jsp/login.jsp");
        }
    }

    @Override
    public void destroy() {

    }
}
