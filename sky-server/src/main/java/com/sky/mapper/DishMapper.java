package com.sky.mapper;

import com.sky.entity.Dish;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface DishMapper {
//    /**
//     * 插入菜品数据
//     *
//     * @param dish
//     */
//    Long insert(Dish dish);
//
//    /**
//     * 修改菜品数据
//     *
//     * @param dish
//     */
//    void updateDish(Dish dish);
//
//    /**
//     * 启用禁用菜品
//     *
//     * @param dish
//     */
//    void updateDishStatus(Dish dish);
//
//    /**
//     * 根据id删除菜品
//     *
//     * @param id
//     */
//    void deleteDishById(Long id);

    /**
     * 根据类型查询菜品数量
     *
     * @param categoryId
     */
    @Select("select count(id) from dish where category_id = #{categoryId}")
    Integer queryDishCountByCategoryId(Long categoryId);
}
