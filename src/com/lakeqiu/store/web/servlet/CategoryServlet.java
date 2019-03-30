package com.lakeqiu.store.web.servlet;

import com.lakeqiu.store.domain.Category;
import com.lakeqiu.store.service.CategoryService;
import com.lakeqiu.store.service.impl.CategoryServiceImpl;
import com.lakeqiu.store.utils.JedisUtils;
import com.lakeqiu.store.web.base.BaseServlet;
import net.sf.json.JSONArray;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 可以用CategoryFilter过滤器代替这个功能
 * @author lakeqiu
 */
public class CategoryServlet extends BaseServlet {

    /**
     * findAllCats
     * 查询分类信息并转化为json格式，响应客户端
     * @param req
     * @param resp
     * @return
     * @throws Exception
     */
    public String findAllCats(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        // 在redis中获取全部分类信息
        Jedis jedis = JedisUtils.getJedis();
        String jsonStr = jedis.get("allCats");
        // 如果获取不到，就向数据库查询
        if (null == jsonStr || "".equals(jsonStr)){
            // 调用业务层逻辑分类查询功能，返回集合
            CategoryService categoryService = new CategoryServiceImpl();
            List<Category> lists = categoryService.getAllCates();
            for (Category list : lists) {
                System.out.println(list.toString());
            }
            // 将分类信息转为json格式
            jsonStr = JSONArray.fromObject(lists).toString();

            jedis.set("allCats", jsonStr);

            // 告诉浏览器本次响应数据为json格式字符串
            resp.setContentType("application/json;charset=utf-8");
            resp.getWriter().print(jsonStr);
        }else { // redis有，直接返回去
            resp.setContentType("application/json;charset=utf-8");
            resp.getWriter().print(jsonStr);
        }
        JedisUtils.closeJedis(jedis);
        return null;
    }
}
