package com.lakeqiu.store.web.filter;


import com.lakeqiu.store.domain.User;
import com.lakeqiu.store.service.UserService;
import com.lakeqiu.store.service.impl.UserServiceImpl;
import com.lakeqiu.store.utils.CookUtils;
import com.lakeqiu.store.utils.MyBeanUtils;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * 过滤器自动登录功能
 * @author lakeqiu
 */
public class AutoLoginFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 强转
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        // 判断session中User为不为空
        if (null == req.getSession().getAttribute("User")){
            Cookie[] cookies = req.getCookies();
            // 找出auto_login,看用户有没有设置自动登录
            Cookie cookie = CookUtils.getCookieByName("auto_login", cookies);
            // 用户第一次来
            if (null == cookie){
                chain.doFilter(req, resp);
            }else { // 用户设置了自动登录
                String value = cookie.getValue();
                String username = value.split("#")[0];
                String password = value.split("#")[1];

                // 完成登录
                User user = new User();
                user.setUsername(username);
                user.setPassword(password);
                UserService userService = new UserServiceImpl();
                User user1 = null;
                try {
                    user1 = userService.userLogin(user);
                } catch (SQLException e) {
                    e.printStackTrace();
                    chain.doFilter(req, resp);
                }
                // 设置session
                req.getSession().setAttribute("User", user1);
                // 放行
                chain.doFilter(req, resp);

            }
        }else { // 不为空，放行
            chain.doFilter(req, resp);
        }
    }

    @Override
    public void destroy() {

    }
}
