package com.lakeqiu.store.dao.impl;

import com.lakeqiu.store.dao.ProductDao;
import com.lakeqiu.store.domain.PageModel;
import com.lakeqiu.store.domain.Product;
import com.lakeqiu.store.utils.JDBCUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.SQLException;
import java.util.List;

/**
 * 商品模块Dao实现
 * @author lakeqiu
 */
public class ProductDaoImpl implements ProductDao {
    /**
     * 查找最热商品
     * @return
     * @throws SQLException
     */
    @Override
    public List<Product> findHots() throws SQLException {
        String sql = "SELECT * FROM product WHERE is_hot=0 AND pflag=0 ORDER BY pdate DESC LIMIT 0,9";
        QueryRunner qr = new QueryRunner(JDBCUtils.getDataSource());
        return qr.query(sql, new BeanListHandler<Product>(Product.class));
    }

    /**
     * 查询最新商品
     * @return
     * @throws SQLException
     */
    @Override
    public List<Product> findNews() throws SQLException {
        String sql = "SELECT * FROM product WHERE pflag=0 ORDER BY pdate DESC LIMIT 0,9";
        QueryRunner qr = new QueryRunner(JDBCUtils.getDataSource());
        return qr.query(sql, new BeanListHandler<Product>(Product.class));
    }

    /**
     * 根据商品pid查询商品并返回商品详情类
     * @param pid
     * @return
     */
    @Override
    public Product findProductByPid(String pid) throws SQLException {
        String sql = "SELECT * FROM product WHERE pid=?";
        QueryRunner qr = new QueryRunner(JDBCUtils.getDataSource());
        return qr.query(sql, new BeanHandler<Product>(Product.class), pid);
    }

    /**
     * 返回当前分类下的商品总数
     * @param cid 分类id
     * @return
     * @throws SQLException
     */
    @Override
    public int findTotalRecords(String cid) throws SQLException {
        String sql = "SELECT COUNT(*) FROM product WHERE cid=?";
        QueryRunner qr = new QueryRunner(JDBCUtils.getDataSource());
        // 返回的是长整型
        Long num = (Long) qr.query(sql, new ScalarHandler(), cid);
        // 转为整型并返回
        return num.intValue();
    }

    /**
     * 返回当前页数下要实现的商品集合
     * @param cid 分类id
     * @param startIndex 起始索引数
     * @param pageSize 当前页面下要显示的商品个数
     * @return
     * @throws SQLException
     */
    @Override
    public List findProductsByCidWithPage(String cid, int startIndex, int pageSize) throws SQLException {
        String sql = "SELECT * FROM product WHERE cid=? LIMIT ?,?";
        QueryRunner qr = new QueryRunner(JDBCUtils.getDataSource());
        return qr.query(sql, new BeanListHandler<Product>(Product.class), cid, startIndex, pageSize);
    }

    /**
     * 模糊匹配查询商品总数
     * \"\%\"?\"%"
     * 要这样写，不然会报错
     * @param name 匹配名字
     * @return 错误
     */
    @Override
    public int findTotalRecordsByName(String name) throws SQLException {
        String sql = "SELECT COUNT(*) FROM product WHERE pname LIKE \"%\"?\"%\"";
        QueryRunner qr = new QueryRunner(JDBCUtils.getDataSource());
        Long num = (Long) qr.query(sql, new ScalarHandler(), name);
        System.out.println(num);
        return num.intValue();
    }

    /**
     * 模糊匹配查询当前页商品
     * @param startIndex 起始索引数
     * @param pageSize 当前页面下要显示的商品个数
     * @param name 匹配名字
     * @return 错误
     */
    @Override
    public List<Product> findProductsByNameWithPage(String name, int startIndex, int pageSize) throws SQLException {
        String sql = "SELECT * FROM product WHERE pname LIKE \"%\"?\"%\" LIMIT ?,?";
        QueryRunner qr = new QueryRunner(JDBCUtils.getDataSource());
        return qr.query(sql, new BeanListHandler<Product>(Product.class), name, startIndex, pageSize);
    }

    /**
     * 返回所有商品数量
     * @return
     */
    @Override
    public int findTotalRecords() throws SQLException {
        String sql = "SELECT COUNT(*) FROM product WHERE pflag=0";
        QueryRunner qr = new QueryRunner(JDBCUtils.getDataSource());
        Long num = (Long) qr.query(sql, new ScalarHandler());
        return num.intValue();
    }

    /**
     * 查询所有商品时查询当前页商品
     * @param startIndex 当前页参数
     * @param pageSize 当前页显示商品数量
     * @return
     */
    @Override
    public List<Product> findProductsWithPage(int startIndex, int pageSize) throws SQLException {
        String sql = "SELECT * FROM product WHERE pflag=0 ORDER BY pdate DESC LIMIT ?, ?";
        QueryRunner qr = new QueryRunner(JDBCUtils.getDataSource());
        List<Product> list = qr.query(sql, new BeanListHandler<Product>(Product.class), startIndex, pageSize);
        return list;
    }

    /**
     * 添加商品
     * @param product 商品类
     */
    @Override
    public void saveProduct(Product product) throws SQLException {
        String sql = "INSERT INTO product values(?,?,?,?,?,?,?,?,?,?)";
        QueryRunner qr = new QueryRunner(JDBCUtils.getDataSource());
        Object[] params = {product.getPid(), product.getPname(), product.getMarket_price(), product.getShop_price(),
         product.getPimage(), product.getPdate(), product.getIs_hot(), product.getPdesc(), product.getPflag(),
        product.getCid()};
        qr.update(sql, params);
    }

    /**
     * 查询下架商品总数
     * @return
     */
    @Override
    public int findTotalOutRecords() throws SQLException {
        String sql = "SELECT COUNT(*) FROM product WHERE pflag=1";
        QueryRunner qr = new QueryRunner(JDBCUtils.getDataSource());
        Long num = (Long) qr.query(sql, new ScalarHandler());
        return num.intValue();
    }

    /**
     * 查询当前页下架商品
     * @param startIndex 当前页参数
     * @param pageSize 当前页显示商品数目
     * @return
     */
    @Override
    public List<Product> findAllOutProductsWithPage(int startIndex, int pageSize) throws SQLException {
        String sql = "SELECT * FROM product WHERE pflag=1 LIMIT ?,?";
        QueryRunner qr = new QueryRunner(JDBCUtils.getDataSource());
        return qr.query(sql, new BeanListHandler<Product>(Product.class), startIndex, pageSize);
    }

    /**
     * 下架商品
     * @param pid 下架商品pid
     */
    @Override
    public void outProduct(String pid) throws SQLException {
        String sql = "UPDATE product SET pflag=1 WHERE pid=?";
        QueryRunner qr = new QueryRunner(JDBCUtils.getDataSource());
        qr.update(sql, pid);
    }

    /**
     * 上架商品
     * @param pid 上架商品pid
     */
    @Override
    public void inProduct(String pid) throws SQLException {
        String sql = "UPDATE product SET pflag=0 WHERE pid=?";
        QueryRunner qr = new QueryRunner(JDBCUtils.getDataSource());
        qr.update(sql, pid);
    }
}
