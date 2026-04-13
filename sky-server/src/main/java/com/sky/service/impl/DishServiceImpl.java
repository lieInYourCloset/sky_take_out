package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.entity.Category;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.entity.Employee;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    /**
     *  新增菜品
     * @param dishDTO
     */
    @Transactional //这里需要开启事务，因为新增菜品需要同时向dish表和dish_flavor表中插入数据，如果其中一个操作失败了，另一个操作也应该回滚
    @Override
    public void addDish(com.sky.dto.DishDTO dishDTO) {

        //向菜品表插入一条数据
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dishMapper.insert(dish);
        Long id = dish.getId();

        //向口味表插入n条数据
        List<DishFlavor> list = dishDTO.getFlavors();
        if (list != null && !list.isEmpty()) {
            list.forEach(flavor -> {flavor.setDishId(id);});
            dishFlavorMapper.insertBatch(list);
        }
        DishFlavor dishFlavor = new DishFlavor();
    }

    @Override
    public void updateDish(com.sky.dto.DishDTO dishDTO) {
        //向菜品表更新一条数据
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dishMapper.updateDish(dish);

        //向口味表更新n条数据
        List<DishFlavor> list = dishDTO.getFlavors();
        if (list != null && !list.isEmpty()) {
            list.forEach(flavor -> {flavor.setDishId(dish.getId());});
            dishFlavorMapper.deleteByDishId(dish.getId());
            dishFlavorMapper.insertBatch(list);
        }
    }

    @Override
    public void updateDishStatus(Long id, Integer status) {
        Dish dish = new Dish();
        dish.setId(id);
        dish.setStatus(status);
        dishMapper.updateDish(dish);
    }

    @Override
    public void deleteDishById(java.util.List<Long> idList) {
        if (idList != null && !idList.isEmpty()) {
            idList.forEach(id -> {
                dishMapper.deleteDishById(id);
                dishFlavorMapper.deleteByDishId(id);
            });
        }
    }

    @Override
    public PageResult dishPageQuery(com.sky.dto.DishPageQueryDTO dishPageQueryDTO) {
        try (Page<Employee> page = PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize())) {
            dishMapper.pageQuery(dishPageQueryDTO);
            return new PageResult(page.getTotal(), page.getResult());
        }
    }

    @Override
    public List<Dish> queryDishByCategoryId(Long categoryId) {
        return dishMapper.queryDishByCategoryId(categoryId);
    }

    @Override
    public DishVO queryDishByIdWithFlavors(Long id) {
        Dish dish = dishMapper.queryDishById(id);
        List<DishFlavor> flavors = dishFlavorMapper.queryByDishId(id);
        DishVO dishVO = new DishVO();
        org.springframework.beans.BeanUtils.copyProperties(dish, dishVO);
        dishVO.setFlavors(flavors);
        return dishVO;
    }
}
