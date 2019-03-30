package com.lakeqiu.store.web.servlet;

import com.lakeqiu.store.domain.Category;
import com.lakeqiu.store.service.CategoryService;
import com.lakeqiu.store.service.impl.CategoryServiceImpl;
import com.lakeqiu.store.utils.UUIDUtils;
import com.lakeqiu.store.web.base.BaseServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 后台
 * @author lakeqiu
 */
public class AdminCategoryServlet extends BaseServlet {
    /**
     * 返回分类信息
     * @param req
     * @param resp
     * @return
     * @throws Exception
     */
    public String findAllCats(HttpServletRequest req, HttpServletResponse resp) throws Exception{
        // 调用业务层功能，获取全部分类
        CategoryService categoryService = new CategoryServiceImpl();
        List<Category> allCates = categoryService.getAllCates();
        // 将分类信息放入request中
        req.setAttribute("allCats", allCates);
        // 转发
        return "/admin/category/list.jsp";
    }

    /**
     * 路径跳转（添加分类）
     * @param req
     * @param resp
     * @return
     * @throws Exception
     */
    public String addCategoryUI(HttpServletRequest req, HttpServletResponse resp) throws Exception{
        return "/admin/category/add.jsp";
    }

    /**
     * 添加新的分类信息
     * @param req
     * @param resp
     * @return
     * @throws Exception
     */
    public String addCategory(HttpServletRequest req, HttpServletResponse resp) throws Exception{
        // 获取新的分类名字
        String cname = req.getParameter("cname");
        // 为新分类获取cid
        String cid = UUIDUtils.getId();

        Category category = new Category();
        category.setCname(cname);
        category.setCid(cid);

        // 调用业务层功能，添加新的分类
        CategoryService categoryService = new CategoryServiceImpl();
        categoryService.addCategory(category);

        // 重定向到查询全部分类信息
        return "/store_war_exploded/admin";
    }
}
