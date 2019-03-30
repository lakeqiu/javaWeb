package com.lakeqiu.store.utils;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.List;

/**
 * 解耦用，可以配合application.xml配置创建不同数据库Dao类
 * @author lakeqiu
 */
public class BeanFactory {
    public static Object creatObject(String name){
        // 获取Document对象
        SAXReader reader = new SAXReader();
        // 获取application文件的输入流对象（文件必须放在src下）
        InputStream inputStream = BeanFactory.class.getClassLoader().getResourceAsStream("application.xml");
        try {
            Document document = reader.read(inputStream);
            // 通过Document对象获取根节点beans
            Element rootElement = document.getRootElement();
            // 通过根节点获取根节点下所有子节点bean，返回集合
            List<Element> list = rootElement.elements();
            // 遍历集合，判断每个元素上的id的值是否和当前的name一样
            for (Element element : list) {
                // element相当于beans节点下每个bean
                // 获取当前节点的id值
                String id = element.attributeValue("id");
                if (id.equals(name)){
                    // 获取id所对应的类的具体路径
                    String str = element.attributeValue("class");
                    // 反射创建类
                    Class clazz = Class.forName(str);
                    Object obj = clazz.newInstance();
                    // 返回类实例
                    return obj;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
