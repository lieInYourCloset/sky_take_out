package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SetmealMapper {

    /**
     * 插入套餐数据
     *
     * @param setmeal
     */
    @Insert("insert into setmeal (name, category_id, price, image, description, status, create_time, update_time, create_user, update_user) " +
            "values (#{name}, #{categoryId}, #{price}, #{image}, #{description}, #{status}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @AutoFill(value = OperationType.INSERT)
    Long insert(Setmeal setmeal);

    /**
     * 修改套餐数据
     *
     * @param setmeal
     */
    @AutoFill(value = OperationType.INSERT)
    void updateSetmeal(Setmeal setmeal);

    /**
     * 根据id查询菜品
     *
     * @param id
     */
    @Select("select * from setmeal where id = #{id} order by create_time asc")
    Setmeal querySetmealById(Long id);

    /**
     * 根据id删除菜品
     *
     * @param id
     */
    @Delete("delete from setmeal where id = #{id}")
    void deleteSetmealById(Long id);

    /**
     * 分类分页查询
     *
     * @param setmealPageQueryDTO
     * @return
     */
    Page<SetmealVO> pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 根据分类id查询套餐的数量
     * @param id
     * @return
     */
    @Select("select count(id) from setmeal where category_id = #{categoryId}")
    Integer querySetmealCountByCategoryId(Long id);

    /**
     * 根据分类id查询套餐
     * @param categoryId
     * @return
     */
    @Select("select * from setmeal where category_id = #{categoryId}")
    List<Setmeal> querySetmealByCategoryId(Long categoryId);
}