package com.lakeqiu.store.service.impl;

import com.lakeqiu.store.dao.ProductDao;
import com.lakeqiu.store.dao.impl.ProductDaoImpl;
import com.lakeqiu.store.domain.PageModel;
import com.lakeqiu.store.domain.Product;
import com.lakeqiu.store.service.ProductService;
import com.lakeqiu.store.utils.BeanFactory;

import java.sql.SQLException;
import java.util.List;

/**
 * 业务层商品模块service实现
 * @author lakeqiu
 */
public class ProductServiceImpl implements ProductService {
    // 用下面的方法，配合application.xml和BeanFactory.java可以解耦，使用不同数据库
//    ProductDao productDao = new ProductDaoImpl();

    ProductDao productDao = (ProductDao) BeanFactory.creatObject("ProductDao");

    /**
     * 查找最热商品
     * @return
     * @throws SQLException
     */
    @Override
    public List<Product> findHots() throws SQLException {
        return productDao.findHots();
    }

    /**
     * 查询最新商品
     * @return
     * @throws SQLException
     */
    @Override
    public List<Product> findNews() throws SQLException {
        return productDao.findNews();
    }

    /**
     * 通过pid查询商品
     * @param pid 商品pid
     * @return
     * @throws SQLException
     */
    @Override
    public Product findProductByPid(String pid) throws SQLException {
        return productDao.findProductByPid(pid);
    }

    /**
     * 分类，返回PageModel对象，（1.当前页面信息， 2.分页， 3.url）
     * @param cid 分类cid
     * @param curNum 当前页
     * @return
     * @throws Exception
     */
    @Override
    public PageModel findProductsByCidWithPage(String cid, int curNum) throws Exception {
        // 获取当前分类下商品总数
        int totalRecords = productDao.findTotalRecords(cid);
        // 创建PageModel对象, 目的：计算分页参数
        PageModel pageModel = new PageModel(curNum, totalRecords, 12);
        // 查询当前页面所有商品详情，并将所有商品信息类放入集合中(分类id，当前页索引开始参数（new的时候算好）， 每页商品个数)
        List<Product> list = productDao.findProductsByCidWithPage(cid, pageModel.getStartIndex(), pageModel.getPageSize());
        // 将集合放入PageModel中
        pageModel.setList(list);
        // 关联url, 分页点击相应链接用
        pageModel.setUrl("ProductServlet?method=findProductsByCidWithPage&cid=" + cid);
        // 返回
        return pageModel;
    }

    /**
     * 模糊查询，返回PageModel对象，（1.当前页面信息， 2.分页， 3.url）
     * @param name 商品名字
     * @param curNum 当前页
     * @return
     * @throws Exception
     */
    @Override
    public PageModel searchProductByNameWithPage(String name, int curNum) throws SQLException {
        // 获取模糊查询到的所有商品数量
        int totalRecords = productDao.findTotalRecordsByName(name);
        // 创建PageModel对象, 目的：计算分页参数
        PageModel pageModel = new PageModel(curNum, totalRecords, 12);
        // 查询当前页面所有商品详情，并将所有商品信息类放入集合中
        List<Product> list = productDao.findProductsByNameWithPage(name, pageModel.getStartIndex(), pageModel.getPageSize());
        // 将集合放入PageModel中
        pageModel.setList(list);
        // 关联url, 分页点击相应链接用
        pageModel.setUrl("ProductServlet?method=findProductsByNameWithPage&name=" + name);
        // 返回
        return pageModel;
    }

    /**
     * 分页查询所有商品
     * @param curNum 当前页
     * @return
     */
    @Override
    public PageModel findAllProductsWithPage(int curNum) throws SQLException {
        // 查询所有商品数量
        int totalRecords = productDao.findTotalRecords();
        // 创建PageModel类，目的：计算分页参数并携带
        PageModel pageModel = new PageModel(curNum, totalRecords, 10);
        // 查询当前页面所有商品信息，并放入集合中
        List<Product> list = productDao.findProductsWithPage(pageModel.getStartIndex(), pageModel.getPageSize());
        // 关联集合
        pageModel.setList(list);
        // 关联url
        pageModel.setUrl("AdminProductServlet?method=findAllProductsWithPage");
        return pageModel;
    }

    /**
     * 添加商品
     * @param product 商品类
     */
    @Override
    public void saveProductp(Product product) throws SQLException {
        productDao.saveProduct(product);
    }

    /**
     * 查询当前页下架商品，返回pageModel对象
     * @param curNum 当前页
     * @return
     */
    @Override
    public PageModel findAllOutProductsWithPage(int curNum) throws SQLException {
        // 查询下架商品总数
        int totalRecords = productDao.findTotalOutRecords();
        // 创建pageModel对象，获取相关参数
        PageModel pageModel = new PageModel(curNum, totalRecords, 10);
        // 查询当前页下架商品
        List<Product> list = productDao.findAllOutProductsWithPage(pageModel.getStartIndex(), pageModel.getPageSize());
        // 关联集合
        pageModel.setList(list);
        // 关联url
        pageModel.setUrl("AdminProductServlet?method=findAllOutProductsWithPage");
        return pageModel;
    }

    /**
     * 下架商品
     * @param pid 下架商品pid
     */
    @Override
    public void outProduct(String pid) throws SQLException {
        productDao.outProduct(pid);
    }

    /**
     * 上架商品
     * @param pid 上架商品pid
     */
    @Override
    public void inProduct(String pid) throws SQLException {
        productDao.inProduct(pid);
    }
}
