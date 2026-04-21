package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

public interface DishService {

    /**
     * 新增菜品
     *
     * @param dishDTO
     */
    void addDish(DishDTO dishDTO);

    /**
     * 修改菜品
     *
     * @param dishDTO
     */
    void updateDish(DishDTO dishDTO);

    /**
     * 菜品起售、停售
     *
     * @param id
     * @param status
     */
     void updateDishStatus(Long id, Integer status);

    /**
     * 根据id批量删除菜品
     *
     * @param idList
     * @return
     */
     void deleteDishById(List<Long> idList);

    /**
     * 菜品分页查询
     *
     * @param DishPageQueryDTO
     * @return
     */
     PageResult dishPageQuery(DishPageQueryDTO DishPageQueryDTO);

    /**
     * 根据分类id查询菜品
     *
     * @param categoryId
     * @return
     */
    List<DishVO> queryDishByCategoryId(Long categoryId);

    /**
     * 根据id查询菜品
     *
     * @param id
     * @return
     */
    DishVO queryDishByIdWithFlavors(Long id);
}
