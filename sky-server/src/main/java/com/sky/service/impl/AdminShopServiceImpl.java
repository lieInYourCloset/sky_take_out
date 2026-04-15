package com.sky.service.impl;

import com.sky.constant.ShopStatusConstant;
import com.sky.service.AdminShopService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AdminShopServiceImpl implements AdminShopService {

    @Autowired
    RedisTemplate redisTemplate;

    @Override
    public void updateStatus(Integer status) {
        redisTemplate.opsForValue().set(ShopStatusConstant.SHOP_STATUS_KEY, status.toString());
    }

    @Override
    public Integer getStatus() {
        int shopStatus;
        try{
            shopStatus = Integer.parseInt((String) redisTemplate.opsForValue().get(ShopStatusConstant.SHOP_STATUS_KEY));
        }catch (NullPointerException e){
            log.error("redis存储错误", e);
            return -1;
        }
        return shopStatus;
    }
}
