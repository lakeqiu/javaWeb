package com.lakeqiu.store.domain;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 购物车类
 * @author lakeqiu
 */
public class Cart {
    /**
     * 购物项集合<购物项pid，购物项>
     *  购物项pid（商品id）在使用添加到购物车方法时可以减少很多操作，减少代码量
     */
    private Map<String, CartItem> map = new HashMap<>();
    /**
     * 总计，积分
     */
    private double total;

    /**
     * 清空购物车
     */
    public void claerCart(){
        map.clear();
    }

    /**
     * 移除购物项
     * @param pid 购物项pid
     */
    public void removeCartItem(String pid){
        map.remove(pid);
    }

    /**
     * 返回map中所有值，方便jsp中判断购物车中有没有商品
     * @return map里的值
     */
    public Collection<CartItem> getCartItems(){
        return map.values();
    }

    /**
     * 添加购物项到购物车
     * @param cartItem 购物项
     */
    public void addCartItemToCart(CartItem cartItem){
        // 获取想要添加到购物车商品pid
        String pid = cartItem.getProduct().getPid();
        // 如果当前商品在购物车里已经有了
        if (map.containsKey(pid)){
            // 获取原先购物项
            CartItem old = map.get(pid);
            // 设置数量为新的（原先购物项与新购物项数量相加）
            old.setNum(old.getNum() + cartItem.getNum());
        }else { // 当前商品在购物车里没有
            // 添加当前商品
            map.put(pid, cartItem);
        }

    }

    /**
     * 获取合计，需要计算
     * @return 合计
     */
    public double getTotal() {
        // 将合计重置为0
        total = 0;
        // 获取所有购物车项
        Collection<CartItem> values = map.values();
        // 将购物项小计加起来
        for (CartItem value : values) {
            total += value.getSubTotal();
        }
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public Map<String, CartItem> getMap() {
        return map;
    }

    public void setMap(Map<String, CartItem> map) {
        this.map = map;
    }
}
