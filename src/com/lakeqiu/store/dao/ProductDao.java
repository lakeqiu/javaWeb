package com.lakeqiu.store.dao;

import com.lakeqiu.store.domain.Product;

import java.sql.SQLException;
import java.util.List;

/**
 * 商品模块Dao接口
 * @author lakeqiu
 */
public interface ProductDao {
    /**
     * 查找最热商品
     * @return
     * @throws SQLException
     */
    List<Product> findHots() throws SQLException;

    /**
     * 查询最新商品
     * @return
     * @throws SQLException
     */
    List<Product> findNews() throws SQLException;

    /**
     * 根据商品pid查询商品并返回商品详情类
     * @param pid
     * @return
     */
    Product findProductByPid(String pid) throws SQLException;

    /**
     * 返回所有商品数量
     * @return
     */
    int findTotalRecords() throws SQLException;

    /**
     * 返回当前分类下的商品总数
     * @param cid 分类id
     * @return
     * @throws SQLException
     */
    int findTotalRecords(String cid) throws SQLException;

    /**
     * 返回当前页数下要实现的商品集合
     * @param cid 分类id
     * @param startIndex 起始索引数
     * @param pageSize 当前页面下要显示的商品个数
     * @return
     * @throws SQLException
     */
    List findProductsByCidWithPage(String cid, int startIndex, int pageSize) throws SQLException;

    /**
     * 模糊匹配查询商品总数
     * \"\%\"?\"%"
     * 要这样写，不然会报错
     * @param name 匹配名字
     * @return 错误
     */
    int findTotalRecordsByName(String name) throws SQLException;

    /**
     * 模糊匹配查询当前页商品
     * @param startIndex 起始索引数
     * @param pageSize 当前页面下要显示的商品个数
     * @param name 匹配名字
     * @return 错误
     */
    List<Product> findProductsByNameWithPage(String name, int startIndex, int pageSize) throws SQLException;

    /**
     * 查询所有商品时查询当前页商品
     * @param startIndex 当前页参数
     * @param pageSize 当前页显示商品数量
     * @return
     */
    List<Product> findProductsWithPage(int startIndex, int pageSize) throws SQLException;

    /**
     * 添加商品
     * @param product 商品类
     */
    void saveProduct(Product product) throws SQLException;

    /**
     * 查询下架商品总数
     * @return
     */
    int findTotalOutRecords() throws SQLException;

    /**
     * 查询当前页下架商品
     * @param startIndex 当前页参数
     * @param pageSize 当前页显示商品数目
     * @return
     */
    List<Product> findAllOutProductsWithPage(int startIndex, int pageSize) throws SQLException;

    /**
     * 下架商品
     * @param pid 下架商品pid
     */
    void outProduct(String pid) throws SQLException;

    /**
     * 上架商品
     * @param pid 上架商品pid
     */
    void inProduct(String pid) throws SQLException;
}
