package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;
import com.sky.vo.SetmealVO;

import java.util.List;

public interface SetmealService {

    /**
     * 新增菜品
     *
     * @param setmealDTO
     */
    void addSetmeal(SetmealDTO setmealDTO);

    /**
     * 修改菜品
     *
     * @param setmealDTO
     */
    void updateSetmeal(SetmealDTO setmealDTO);

    /**
     * 菜品起售、停售
     *
     * @param id
     * @param status
     */
     void updateSetmealStatus(Long id, Integer status);

    /**
     * 根据id批量删除菜品
     *
     * @param idList
     * @return
     */
     void deleteSetmealById(List<Long> idList);

    /**
     * 菜品分页查询
     *
     * @param setmealPageQueryDTO
     * @return
     */
     PageResult setmealPageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 根据id查询菜品
     *
     * @param id
     * @return
     */
    SetmealVO querySetmealById(Long id);
}
