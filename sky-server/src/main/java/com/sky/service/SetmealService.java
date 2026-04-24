package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;
import com.sky.vo.SetmealVO;

import java.util.List;

public interface SetmealService {

    /**
     * 新增套餐
     *
     * @param setmealDTO
     */
    void addSetmeal(SetmealDTO setmealDTO);

    /**
     * 修改套餐
     *
     * @param setmealDTO
     */
    void updateSetmeal(SetmealDTO setmealDTO);

    /**
     * 套餐起售、停售
     *
     * @param id
     * @param status
     */
     void updateSetmealStatus(Long id, Integer status);

    /**
     * 根据id批量删除套餐
     *
     * @param idList
     * @return
     */
     void deleteSetmealById(List<Long> idList);

    /**
     * 套餐分页查询
     *
     * @param setmealPageQueryDTO
     * @return
     */
     PageResult setmealPageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 根据id查询套餐
     *
     * @param id
     * @return
     */
    SetmealVO querySetmealById(Long id);

    /**
     * 根据分类id查询套餐
     *
     * @param categoryId
     */
    List<Setmeal> querySetmealByCategoryId(Long categoryId);
}
