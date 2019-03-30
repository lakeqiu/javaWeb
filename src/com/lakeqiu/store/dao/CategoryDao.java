package com.lakeqiu.store.dao;

import com.lakeqiu.store.domain.Category;

import java.sql.SQLException;
import java.util.List;

/**
 * 分类模块dao接口
 * @author lakeqiu
 */
public interface CategoryDao {
    List<Category> getAllCats() throws SQLException;

    /**
     * 添加新的分类
     * @param category 新分类
     */
    void addCategory(Category category) throws SQLException;
}
