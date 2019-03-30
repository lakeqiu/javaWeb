package com.lakeqiu.store.service;

import com.lakeqiu.store.domain.PageModel;
import com.lakeqiu.store.domain.Product;

import java.sql.SQLException;
import java.util.List;

/**
 * 业务层商品模块service接口
 * @author lakeqiu
 */
public interface ProductService {
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
     * 通过pid查询商品
     * @param pid 商品pid
     * @return
     * @throws SQLException
     */
    Product findProductByPid(String pid) throws SQLException;

    /**
     * 分类，返回PageModel对象，（1.当前页面信息， 2.分页， 3.url）
     * @param cid 分类cid
     * @param curNum 当前页
     * @return
     * @throws Exception
     */
    PageModel findProductsByCidWithPage(String cid, int curNum) throws Exception;

    /**
     * 模糊查询，返回PageModel对象，（1.当前页面信息， 2.分页， 3.url）
     * @param name 商品名字
     * @param curNum 当前页
     * @return
     * @throws Exception
     */
    PageModel searchProductByNameWithPage(String name, int curNum) throws SQLException;

    /**
     * 分页查询所有商品
     * @param curNum 当前页
     * @return
     */
    PageModel findAllProductsWithPage(int curNum) throws SQLException;

    /**
     * 添加商品
     * @param product 商品类
     */
    void saveProductp(Product product) throws SQLException;

    /**
     * 查询当前页下架商品，返回pageModel对象
     * @param curNum 当前页
     * @return
     */
    PageModel findAllOutProductsWithPage(int curNum) throws SQLException;

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
