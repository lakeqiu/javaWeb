package com.lakeqiu.store.domain;

/**
 * 分页信息类
 * @author lakeqiu
 */
public class Category {
    /**
     * 分类编号
     */
    private String cid;
    /**
     * 分类名字
     */
    private String cname;

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public Category() {
    }

    public Category(String cid, String cname) {
        this.cid = cid;
        this.cname = cname;
    }

    @Override
    public String toString() {
        return "Category{" +
                "cid='" + cid + '\'' +
                ", cname='" + cname + '\'' +
                '}';
    }
}
