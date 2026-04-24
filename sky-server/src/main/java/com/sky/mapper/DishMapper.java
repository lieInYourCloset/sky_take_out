package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Category;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.*;

import javax.annotation.PropertyKey;
import java.util.List;

@Mapper
public interface DishMapper {
    /**
     * 插入菜品数据
     *
     * @param dish
     */
    @Insert("insert into dish (name, category_id, price, image, description, status, create_time, update_time, create_user, update_user) " +
            "values (#{name}, #{categoryId}, #{price}, #{image}, #{description}, #{status}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @AutoFill(value = OperationType.INSERT)
    Long insert(Dish dish);

    /**
     * 修改菜品数据
     *
     * @param dish
     */
    void updateDish(Dish dish);

    /**
     * 根据id查询菜品
     *
     * @param id
     */
    @Select("select * from dish where id = #{id} order by create_time asc")
    Dish queryDishById(Long id);

    /**
     * 根据id删除菜品
     *
     * @param id
     */
    @Delete("delete from dish where id = #{id}")
    void deleteDishById(Long id);

    /**
     * 根据分类id查询菜品
     *
     * @param categoryId
     */
    List<DishVO> queryDishVOByCategoryId(Long categoryId);

    /**
     * 根据分类id查询菜品
     *
     * @param categoryId
     */
    List<Dish> queryDishByCategoryId(Long categoryId);

    /**
     * 根据类型查询菜品数量
     *
     * @param categoryId
     */
    @Select("select count(id) from dish where category_id = #{categoryId}")
    Integer queryDishCountByCategoryId(Long categoryId);

    /**
     * 分类分页查询
     *
     * @param dishPageQueryDTO
     * @return
     */
    Page<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO);
}
