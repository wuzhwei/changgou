package com.changgou.order.service;

import java.util.Map;

/**
 * 购物车服务
 */
public interface CartService {
    /**
     * 添加购物车
     * @param skuId
     * @param number
     * @param username
     */
    void addCart(String skuId,Integer number, String username);

    /**
     * 查询购物车列表数据
     * @param username
     * @return
     */
    Map<String,Object> list(String username);
}
