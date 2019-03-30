package com.lakeqiu.store.web.servlet;

import com.lakeqiu.store.domain.User;
import com.lakeqiu.store.service.UserService;
import com.lakeqiu.store.service.impl.UserServiceImpl;
import com.lakeqiu.store.utils.CookUtils;
import com.lakeqiu.store.utils.MailUtils;
import com.lakeqiu.store.utils.MyBeanUtils;
import com.lakeqiu.store.utils.UUIDUtils;
import com.lakeqiu.store.web.base.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

/**
 * @author lakeqiu
 */
public class UserServlet extends BaseServlet {
    /**
     * /registUI
     * 返回注册页面
     * @param req 请求
     * @param resp 回复
     * @return 注册页面地址
     * @throws ServletException 错误
     * @throws IOException 错误
     */
    public String registUI(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        return "/jsp/register.jsp";
    }

    public String loginUI(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        return "/jsp/login.jsp";
    }

    /**
     * 用户注册功能
     * @param req
     * @param resp
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public String userRegist(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 获取表单信息
        Map<String, String[]> map = req.getParameterMap();
        User user = new User();
        // 将map里的对应数据添进user里
        MyBeanUtils.populate(user, map);
        // 设置需要后台填的信息，uid，激活码，激活状态
        user.setUid(UUIDUtils.getId());
        user.setState(0);
        user.setCode(UUIDUtils.getCode());

        // 调用业务层逻辑，进行注册
        try {
            UserService userService = new UserServiceImpl();
            // 注册
            userService.userRegist(user);
            // 发送激活码到用户邮箱
            MailUtils.sendMail(user.getEmail(), user.getCode());
            // 注册成功提示信息
            req.setAttribute("msg", "注册成功，请进行激活");
        }catch (Exception e){
            // 注册失败提示信息
            req.setAttribute("msg", "注册失败，请重新进行注册");
        }
        // 跳转提示页面
        return "/jsp/info.jsp";
    }

    /**
     * 用户激活
     * @param req
     * @param resp
     * @return
     * @throws ServletException
     * @throws IOException
     * @throws SQLException
     */
    public String active(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {
        // 获取激活码
        String code = req.getParameter("code");
        // 调用业务层逻辑，进行激活
        UserService userService = new UserServiceImpl();
        Boolean flag = userService.userActive(code);
        // 激活成功，跳转登录页面
        if (flag){
            req.setAttribute("msg", "激活成功，请登录");
            return "/jsp/login.jsp";
        }else { // 激活失败
            req.setAttribute("msg", "激活失败，请重新激活");
            return "/jsp/info.jsp";
        }
    }

    /**
     * 用户登录功能
     * @param req
     * @param resp
     * @return
     * @throws ServletException
     * @throws IOException
     * @throws SQLException
     */
    public String userLogin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {
        // 获取验证码
        String checkCode = req.getParameter("checkCode");
        // 判断验证码是否正确
        if (!req.getSession().getAttribute("checkCode").equals(checkCode)){
            // 不正确
            // 获取登录失败详情
            String msg = "验证码错误！";
            // 向request放入失败信息
            req.setAttribute("msg", msg);
            // 请求转发到登录页
            return "/jsp/login.jsp";
        }

        // 获取用户账户和密码并放入用户类
        User user = new User();
        MyBeanUtils.populate(user, req.getParameterMap());

        // 判断用户是否点击自动登录功能，是的话添加cookie
        String autoLogin = req.getParameter("auto_login");
        if ("on".equals(autoLogin)){
            String str = user.getUsername() + "#" + user.getPassword();
            Cookie cookie = new Cookie("auto_login", str);
            resp.addCookie(cookie);
        }

        // 判断用户是否点击记住用户名功能，是的话添加相应cookie
        if ("on".equals(req.getParameter("remUser"))){
            Cookie cookie = new Cookie("username", user.getUsername());
            resp.addCookie(cookie);
        }

        // 判断用户有没有记住用户名，有的话在登录页面显示
        Cookie[] cookies = req.getCookies();
        Cookie cookie = CookUtils.getCookieByName("username", cookies);
        System.out.println(cookie);
        if (null != cookie){
            req.getSession().setAttribute("username", user.getUsername());
        }

        // 调用业务层登录功能
        UserService userService = new UserServiceImpl();
        User user1 = null;
        try {
            user1 = userService.userLogin(user);
            // 用户登录成功
            // session放入用户信息
            req.getSession().setAttribute("User", user1);
            // 重定向到首页
            resp.sendRedirect("/store_war_exploded/index.jsp");
            return null;
        }catch (Exception e){ // 用户登录失败
            // 获取登录失败详情
            String msg = e.getMessage();
            // 向request放入失败信息
            req.setAttribute("msg", msg);
            // 请求转发到登录页
            return "/jsp/login.jsp";
        }
    }

    /**
     * 用户注销功能
     * @param req
     * @param resp
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public String logOut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 移除Session中信息
        req.getSession().removeAttribute("User");
        // 重定向到首页
        resp.sendRedirect("/store_war_exploded/index.jsp");
        return null;
    }
}
