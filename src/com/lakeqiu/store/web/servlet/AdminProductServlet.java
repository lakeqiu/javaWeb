package com.lakeqiu.store.web.servlet;

import com.lakeqiu.store.domain.Category;
import com.lakeqiu.store.domain.PageModel;
import com.lakeqiu.store.domain.Product;
import com.lakeqiu.store.service.CategoryService;
import com.lakeqiu.store.service.ProductService;
import com.lakeqiu.store.service.impl.CategoryServiceImpl;
import com.lakeqiu.store.service.impl.ProductServiceImpl;
import com.lakeqiu.store.utils.UUIDUtils;
import com.lakeqiu.store.utils.UploadUtils;
import com.lakeqiu.store.web.base.BaseServlet;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 后台商品管理
 * @author lakeqiu
 */
public class AdminProductServlet extends BaseServlet {

    /**
     * 返回当前页商品
     * @param req
     * @param resp
     * @return
     * @throws Exception
     */
    public String findAllProductsWithPage(HttpServletRequest req, HttpServletResponse resp) throws Exception{
        // 获取当前页
        int curNum = Integer.parseInt(req.getParameter("num"));
        // 调用业务层功能查询所有商品，返回PageModel（包含所有商品）
        ProductService productService = new ProductServiceImpl();
        PageModel pageModel = productService.findAllProductsWithPage(curNum);
        // 将PageModel放入request中
        req.setAttribute("page", pageModel);
        // 转发
        return "/admin/product/list.jsp";
    }

    /**
     * 获取全部分类，跳转添加商品页面
     * @param req
     * @param resp
     * @return
     * @throws Exception
     */
    public String addProductUI(HttpServletRequest req, HttpServletResponse resp) throws Exception{
        // 调用业务层功能，查询所有分类，返回所有分类信息
        CategoryService categoryService = new CategoryServiceImpl();
        List<Category> allCates = categoryService.getAllCates();
        // 将分类信息放入request中
        req.setAttribute("allCats", allCates);
        // 返回响应界面
        return "/admin/product/add.jsp";
    }

    /**
     * 上传商品
     * @param req
     * @param resp
     * @return
     * @throws Exception
     */
    public String addProduct(HttpServletRequest req, HttpServletResponse resp) throws Exception{
        // 存放，携带数据给业务层实现存入数据库
        Product product = new Product();
        // 创建map，存放数据
        Map<String, String> map = new HashMap<>();
        // 三句代码，利用req.getInputStream();获取请求体中全部数据，进行拆分和封装
        DiskFileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);
        List<FileItem> list = upload.parseRequest(req);
        // 遍历集合，取出数据
        for (FileItem item : list) {
            // 如果是普通表单数据（表单项）
            if (item.isFormField()){
                // 将普通项上name的值作为键，将获取的内容作为值，存入map中
                // 加utf-8是为了防止中文乱码
                map.put(item.getFieldName(), item.getString("utf-8"));
            }else { // 如果是上传项
                // 获取文件名
                String oldFiledname = item.getFieldName();
                // 变为新文件名
                String newFiledname = UploadUtils.getUUIDName(oldFiledname);

                // 通过FileItem获取输入流对象，通过输入流可以可以获取图片二进制数据
                InputStream is = item.getInputStream();
                // 获取当前项目下/product/3的真实路径（存放上传图片路径）
                String realPath = getServletContext().getRealPath("/product/3/");
                System.out.println(realPath);
                // 获取要创建的8层目录（多层目录存放图片提高性能）
                String dir = UploadUtils.getDir(realPath);
                // 组成要存放图片的具体目录名
                String path = realPath + dir;

                // 内存中声明一个目录名
                File newDir = new File(path);
                // 如果不存在这个目录
                if (!newDir.exists()){
                    // 创建这个目录
                    newDir.mkdirs();
                }
                // 在服务端创建一个空文件(后缀名要和上传的文件后缀名一致)
                File finalFile = new File(newDir, newFiledname);
                if (!finalFile.exists()){
                    finalFile.createNewFile();
                }
                // 建立和空文件对应的输出流
                OutputStream os = new FileOutputStream(finalFile);
                // 将输入流数据刷到输出流中
                IOUtils.copy(is, os);
                // 释放资源
                IOUtils.closeQuietly(is);
                IOUtils.closeQuietly(os);
                // 先map中存放图片与图片对应路径
                map.put("pimage", "/product/3/" + dir + "/" + newFiledname);
            }
        }
        // 将map中数据填充到product中
        BeanUtils.populate(product, map);
        // 将剩余信息补充完
        product.setPid(UUIDUtils.getId());
        product.setPdate(new Date());
        product.setPflag(0);

        // 调用业务层功能，将数据存入数据库
        ProductService productService = new ProductServiceImpl();
        productService.saveProductp(product);

        // 重定向
        resp.sendRedirect("/store_war_exploded/AdminProductServlet?method=findAllProductsWithPage&num=1");
        return null;
    }

    /**
     * 查询当前页下架商品
     * @param req
     * @param resp
     * @return
     * @throws Exception
     */
    public String findAllOutProductsWithPage(HttpServletRequest req, HttpServletResponse resp) throws Exception{
        // 获取当前页
        int curNum = Integer.parseInt(req.getParameter("num"));
        // 调用业务层功能，查询当前页下架商品，返回PageModel
        ProductService productService = new ProductServiceImpl();
        PageModel pageModel = productService.findAllOutProductsWithPage(curNum);
        // 将当前页下架商品放入request中
        req.setAttribute("page", pageModel);
        // 返回响应界面
        return "/admin/product/pushDown_list.jsp";
    }

    /**
     * 下架商品
     * @param req
     * @param resp
     * @return
     * @throws Exception
     */
    public String outProduct(HttpServletRequest req, HttpServletResponse resp) throws Exception{
        // 获取要下架的商品pid
        String pid = req.getParameter("pid");
        // 调用业务层功能，将商品状态设置为下架
        ProductService productService = new ProductServiceImpl();
        productService.outProduct(pid);
        // 重定向
        // 这里如果用下面链接在服务器内跳转会报404错误
        resp.sendRedirect("/store_war_exploded/AdminProductServlet?method=findAllProductsWithPage&num=1");
        return null;
    }

    /**
     * 上架商品
     * @param req
     * @param resp
     * @return
     * @throws Exception
     */
    public String inProduct(HttpServletRequest req, HttpServletResponse resp) throws Exception{
        // 获取要上架的商品pid
        String pid = req.getParameter("pid");
        // 调用业务层功能，将商品状态设置为下架
        ProductService productService = new ProductServiceImpl();
        productService.inProduct(pid);
        // 重定向
        // 这里如果用下面链接在服务器内跳转会报404错误
        resp.sendRedirect("/store_war_exploded/AdminProductServlet?method=findAllOutProductsWithPage&num=1");
        return null;
    }

}
