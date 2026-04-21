package com.sky.service;

import org.springframework.stereotype.Service;

@Service
public interface ShopService {

    /**
     * 更新店铺状态
     *
     * @param status
     */
    void updateStatus(Integer status);

    /**
     * 管理端查询店铺状态
     */
    Integer getStatus();
}
