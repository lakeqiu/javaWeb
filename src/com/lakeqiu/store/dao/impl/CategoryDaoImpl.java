package com.lakeqiu.store.dao.impl;

import com.lakeqiu.store.dao.CategoryDao;
import com.lakeqiu.store.domain.Category;
import com.lakeqiu.store.utils.JDBCUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.SQLException;
import java.util.List;

/**
 * 分类模块Dao实现
 * @author lakeqiu
 */
public class CategoryDaoImpl implements CategoryDao {
    /**
     * 查询分类信息
     * @return 分类信息类集合
     * @throws SQLException 错误
     */
    @Override
    public List<Category> getAllCats() throws SQLException {
        String sql = "SELECT * FROM category";
        QueryRunner qr = new QueryRunner(JDBCUtils.getDataSource());
        return qr.query(sql, new BeanListHandler<Category>(Category.class));
    }

    /**
     * 添加新的分类
     * @param category 新分类
     */
    @Override
    public void addCategory(Category category) throws SQLException {
        String sql = "INSERT INTO category VALUES(?, ?)";
        QueryRunner qr = new QueryRunner(JDBCUtils.getDataSource());
        qr.update(sql, category.getCid(), category.getCname());
    }


}
