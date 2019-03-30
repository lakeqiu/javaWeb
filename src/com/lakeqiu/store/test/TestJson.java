package com.lakeqiu.store.test;

import com.lakeqiu.store.domain.Category;
import net.sf.json.JSONArray;

import java.util.List;

/**
 * @author lakeqiu
 */
public class TestJson {
    public static void main(String[] args) {
        List<Category> list = null;
        list.add(new Category("1", "2"));
        list.add(new Category("2", "2"));
        list.add(new Category("3", "2"));
        String string = JSONArray.fromObject(list).toString();
        System.out.println(string);
    }
}
