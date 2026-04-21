package com.sky.service.impl;

import com.sky.constant.ShopStatusConstant;
import com.sky.service.ShopService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ShopServiceImpl implements ShopService {

    @Autowired
    RedisTemplate redisTemplate;

    @Override
    public void updateStatus(Integer status) {
        redisTemplate.opsForValue().set(ShopStatusConstant.SHOP_STATUS_KEY, status);
    }

    @Override
    public Integer getStatus() {
        int shopStatus;
        try{
            shopStatus = (int) redisTemplate.opsForValue().get(ShopStatusConstant.SHOP_STATUS_KEY);
        }catch (Exception e){
            log.error("redis存储错误", e);
            return -1;
        }
        return shopStatus;
    }
}
