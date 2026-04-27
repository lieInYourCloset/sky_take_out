package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;


@Service
@Slf4j
public class ShoppingCartServiceImpl implements ShoppingCartService {
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    @Override
    public void addShoppingCart(com.sky.dto.ShoppingCartDTO shoppingCartDTO) {
        // 看购物车中有没有这个菜品或者套餐，如果有，就在原来数量基础上加一，如果没有，就添加到购物车，数量默认就是一
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO,shoppingCart);
        Long userId = BaseContext.getCurrentId();
        shoppingCart.setUserId(userId);


        List<ShoppingCart> list = shoppingCartMapper.listShoppingCart(shoppingCart);
        if (list != null && list.size() > 0) {
            // 已有此商品
            ShoppingCart shoppingCart1 = list.get(0);
            shoppingCart1.setNumber(shoppingCart1.getNumber() + 1);
            shoppingCartMapper.updateShoppingCartNumber(shoppingCart1);
        }
        else {
            // 没有此商品
            shoppingCart.setNumber(1);
            if (shoppingCart.getDishId() != null) {
                // 购物车添加的是菜品
                Dish dish = dishMapper.queryDishById(shoppingCart.getDishId());
                shoppingCart.setAmount(dish.getPrice());
                shoppingCart.setImage(dish.getImage());
                shoppingCart.setName(dish.getName());
            }
            else {
                // 购物车添加的是套餐
                Setmeal setmeal = setmealMapper.querySetmealById(shoppingCart.getSetmealId());
                shoppingCart.setAmount(setmeal.getPrice());
                shoppingCart.setImage(setmeal.getImage());
                shoppingCart.setName(setmeal.getName());
            }
            shoppingCart.setCreateTime(java.time.LocalDateTime.now());
            shoppingCartMapper.insertShoppingCart(shoppingCart);
        }
    }

    @Override
    public void subShoppingCart(com.sky.dto.ShoppingCartDTO shoppingCartDTO) {
        // 检查购物车中有没有这个菜品或者套餐
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO,shoppingCart);
        Long userId = BaseContext.getCurrentId();
        shoppingCart.setUserId(userId);

        List<ShoppingCart> list = shoppingCartMapper.listShoppingCart(shoppingCart);
        if (list != null && list.size() > 0) {
            // 已有此商品
            ShoppingCart shoppingCart1 = list.get(0);
            shoppingCartMapper.deleteShoppingCartById(shoppingCart1.getId());
        }
        else {
            log.error("购物车中没有这个菜品或者套餐！删除失败！");
        }
    }

    @Override
    public List<ShoppingCart> getShoppingCart() {
        Long userId = BaseContext.getCurrentId();
        return shoppingCartMapper.getShoppingCartByUserId(userId);
    }

    @Override
    public void cleanShoppingCart() {
        Long userId = BaseContext.getCurrentId();
        shoppingCartMapper.cleanShoppingCartByUserId(userId);
    }
}
