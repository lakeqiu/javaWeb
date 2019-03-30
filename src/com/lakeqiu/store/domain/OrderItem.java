package com.lakeqiu.store.domain;

/**
 * 订单项类
 * @author lakeqiu
 */
public class OrderItem {
    /**
     * id
     */
    private String itemid;
    /**
     * 购买数量
     */
    private int quantity;
    /**
     * 小计
     */
    private double total;

    /**
     * 商品类
     * 对象对应对象（详情在Order）
     */
    private Product product;
    /**
     * 订单类
     */
    private Order order;

    public String getItemid() {
        return itemid;
    }

    public void setItemid(String itemid) {
        this.itemid = itemid;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
