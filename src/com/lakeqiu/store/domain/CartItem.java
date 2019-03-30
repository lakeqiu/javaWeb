package com.lakeqiu.store.domain;

/**
 * 购物项类
 * @author lakeqiu
 */
public class CartItem {
    /**
     * 当前商品类
     */
    private Product product;
    /**
     * 要买的当前商品数量
     */
    private int num;
    /**
     * 小计，合计商品金额（数量*价格）
     */
    private double subTotal;

    /**
     * 通过计算得出（数量*价格）
     * @return 小计
     */
    public double getSubTotal() {
        return product.getShop_price()*num;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public CartItem(Product product, int num) {
        this.product = product;
        this.num = num;
    }

    public CartItem() {
    }
}
