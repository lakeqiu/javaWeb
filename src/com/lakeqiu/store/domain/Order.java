package com.lakeqiu.store.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 订单类表
 * @author lakeqiu
 */
public class Order {
    /**
     * 订单oid
     */
    private String oid;
    /**
     * 下单时间
     */
    private Date ordertime;
    /**
     * 总价
     */
    private double total;
    /**
     * 订单状态
     */
    private int state;
    /**
     * 收货地址
     */
    private String address;
    /**
     * 收货人
     */
    private String name;
    /**
     * 收货人电话
     */
    private String teltphone;
    /**
     * 下单用户
     * 尽管数据库这一项存的是下单用户uid
     * 却不用（private String uid）是因为：
     * 1. 程序是对象与对象发生关系，而不是对象与对象属性发生关系
     * 2. 设计Order目的：让order携带订单上的数据向service，dao传递，user对象可以携带更多数据
     */
    private User user;
    /**
     * 订单下的订单项
     */
    private List<OrderItem> list = new ArrayList<>();

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public Date getOrdertime() {
        return ordertime;
    }

    public void setOrdertime(Date ordertime) {
        this.ordertime = ordertime;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTeltphone() {
        return teltphone;
    }

    public void setTeltphone(String teltphone) {
        this.teltphone = teltphone;
    }

    public List<OrderItem> getList() {
        return list;
    }

    public void setList(List<OrderItem> list) {
        this.list = list;
    }
}
