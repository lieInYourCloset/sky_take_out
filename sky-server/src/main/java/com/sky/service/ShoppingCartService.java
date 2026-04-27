package com.sky.service;

import org.springframework.stereotype.Service;

@Service
public interface ShoppingCartService {

    /**
     * 添加购物车
     *
     * @param status
     */
    void addShoppingCart(Integer status);

    /**
     * 管理端查询店铺状态
     */
    Integer getStatus();
}
