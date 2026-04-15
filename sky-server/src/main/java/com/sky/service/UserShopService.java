package com.sky.service;

import org.springframework.stereotype.Service;

@Service
public interface UserShopService {

    /**
     * 管理端查询店铺状态
     */
    Integer getStatus();
}
