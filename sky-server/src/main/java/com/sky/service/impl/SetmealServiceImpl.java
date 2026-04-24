package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.RedisConstant;
import com.sky.entity.*;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.DishVO;
import com.sky.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealMapper setmealMapper;

    @Autowired
    private SetmealDishMapper setmealDishMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    @Transactional
    public void addSetmeal(com.sky.dto.SetmealDTO setmealDTO) {

        //向套餐表插入一条数据
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setmealMapper.insert(setmeal);
        //取得套餐id
        Long setmealId = setmeal.getId();
        //保存套餐菜品关系表
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        setmealDishes.forEach(setmealDish -> {
            setmealDish.setSetmealId(setmealId);
        });

        if (setmealDishes.size() > 0) {
        log.info("保存套餐和菜品的关联关系：{}", setmealDishes);
        //保存套餐和菜品的关联关系
        setmealDishMapper.insertBatch(setmealDishes); }

        deleteSetmealCache();
    }

    @Override
    @Transactional
    public void updateSetmeal(com.sky.dto.SetmealDTO setmealDTO) {
        //向套餐表更新一条数据
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setmealMapper.updateSetmeal(setmeal);

        //删除套餐和菜品的关联关系，操作setmeal_dish表，执行delete
        setmealDishMapper.deleteBySetmealId(setmeal.getId());

        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        setmealDishes.forEach(setmealDish -> {
            setmealDish.setSetmealId(setmeal.getId());
        });
        //重新插入套餐和菜品的关联关系，操作setmeal_dish表，执行insert
        setmealDishMapper.insertBatch(setmealDishes); //TODO: 先删除后插入的方式有问题，创建时间丢失了，创建时间变得固定和更新时间一样。另外也浪费性能

        deleteSetmealCache();
    }

    @Override
    public void updateSetmealStatus(Long id, Integer status) {
        Setmeal setmeal = new Setmeal();
        setmeal.setId(id);
        setmeal.setStatus(status);
        setmealMapper.updateSetmeal(setmeal);

        deleteSetmealCache();
    }

    @Override
    @Transactional
    public void deleteSetmealById(java.util.List<Long> idList) {
        if (idList != null && !idList.isEmpty()) {
            idList.forEach(id -> {
                setmealMapper.deleteSetmealById(id);
                //删除套餐和菜品的关联关系，操作setmeal_dish表，执行delete
                setmealDishMapper.deleteBySetmealId(id);
            });
        }
        deleteSetmealCache();
    }

    @Override
    public PageResult setmealPageQuery(com.sky.dto.SetmealPageQueryDTO setmealPageQueryDTO) {
        try (Page<Employee> page = PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize())) {
            setmealMapper.pageQuery(setmealPageQueryDTO);
            return new PageResult(page.getTotal(), page.getResult());
        }
    }


    @Override
    public SetmealVO querySetmealById(Long id) {
        Setmeal setmeal = setmealMapper.querySetmealById(id);

        SetmealVO setmealVO = new SetmealVO();
        org.springframework.beans.BeanUtils.copyProperties(setmeal, setmealVO);
        setmealVO.setSetmealDishes(setmealDishMapper.querySetmealDishesBySetmealId(id));
        return setmealVO;
    }

    @Override
    public List<Setmeal> querySetmealByCategoryId(Long categoryId) {
        //先查redis看有没有缓存
        List<Setmeal> setmealList = (List<Setmeal>) redisTemplate.opsForHash().get(RedisConstant.SETMEALS_IN_CATEGORY, categoryId.toString());
        //没有缓存时查sql并加入缓存
        if  (setmealList == null) {
            List<Setmeal> rawList = setmealMapper.querySetmealByCategoryId(categoryId);
            // 将结果包装成标准的 ArrayList
            setmealList = new ArrayList<>(rawList);
            redisTemplate.opsForHash().put(RedisConstant.DISHES_IN_CATEGORY, categoryId.toString(), setmealList);
        }
        return setmealList;
    }

    private void deleteSetmealCache(){
        redisTemplate.delete(RedisConstant.SETMEALS_IN_CATEGORY);
    }


}
