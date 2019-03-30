package com.lakeqiu.store.service;

import com.lakeqiu.store.domain.Category;

import java.sql.SQLException;
import java.util.List;

/**
 * 业务层分类模块service接口
 * @author lakeqiu
 */
public interface CategoryService {
    /**
     * 获取全部分类
     * @return 分类信息集合
     * @throws SQLException 错误
     */
    List<Category> getAllCates() throws SQLException;

    /**
     * 添加新的分类
     * @param category 新分类
     */
    void addCategory(Category category) throws SQLException;
}
