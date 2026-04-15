package com.sky.service.impl;

import com.sky.constant.ShopStatusConstant;
import com.sky.service.AdminShopService;
import com.sky.service.UserShopService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserShopServiceImpl implements UserShopService {

    @Autowired
    RedisTemplate redisTemplate;

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
