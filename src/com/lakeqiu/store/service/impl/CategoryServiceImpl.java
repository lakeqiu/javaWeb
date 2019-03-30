package com.lakeqiu.store.service.impl;

import com.lakeqiu.store.dao.CategoryDao;
import com.lakeqiu.store.dao.impl.CategoryDaoImpl;
import com.lakeqiu.store.domain.Category;
import com.lakeqiu.store.service.CategoryService;
import com.lakeqiu.store.utils.BeanFactory;
import com.lakeqiu.store.utils.JedisUtils;
import redis.clients.jedis.Jedis;

import java.sql.SQLException;
import java.util.List;

/**
 * 业务层分类模块service实现
 * @author lakeqiu
 */
public class CategoryServiceImpl implements CategoryService {
    // 用下面的方法，配合application.xml和BeanFactory.java可以解耦，使用不同数据库
//    private CategoryDao categoryDao = new CategoryDaoImpl();

    private CategoryDao categoryDao = (CategoryDao) BeanFactory.creatObject("CategoryDao");
    /**
     * 获取全部分类
     * @return
     * @throws SQLException
     */
    @Override
    public List<Category> getAllCates() throws SQLException {
        return categoryDao.getAllCats();
    }

    /**
     * 添加新的分类
     * @param category 新分类
     */
    @Override
    public void addCategory(Category category) throws SQLException {
        categoryDao.addCategory(category);

        /*// 更新redis（删除redis中相关缓存，下次访问redis中没有，让redis区数据库拿）
        Jedis jedis = JedisUtils.getJedis();
        jedis.del("allCats");
        JedisUtils.closeJedis(jedis);*/
    }
}
