package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Category;
import com.sky.entity.Employee;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.*;
import org.apache.xmlbeans.impl.xb.xmlconfig.Extensionconfig;

import java.util.List;

@Mapper
public interface CategoryMapper {
    /**
     * 插入分类数据
     *
     * @param category
     */
    @Insert("insert into category (type, name, sort, status, create_time, update_time, create_user, update_user) " +
            "values (#{type}, #{name}, #{sort}, #{status}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser})")
    @AutoFill(value = OperationType.INSERT)
    Long insert(Category category);

    /**
     * 修改分类数据
     *
     * @param category
     */
    @AutoFill(value = OperationType.UPDATE)
    void updateCategory(Category category);

    /**
     * 启用禁用分类
     *
     * @param category
     */
    @Update("update category set status = #{status}, update_time = #{updateTime}, update_user = #{updateUser} where id = #{id}")
    @AutoFill(value = OperationType.UPDATE)
    void updateCategoryStatus(Category category);

    /**
     * 根据id删除分类
     *
     * @param id
     */
    @Delete("delete from category where id = #{id}")
    void deleteCategoryById(Long id);



    /**
     * 分类分页查询
     *
     * @param categoryPageQueryDTO
     * @return
     */
    Page<Category> pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);

    /**
     * 根据类型查询分类列表
     *
     * @param type
     * @return
     */
    @Select("select id, type, name, sort, status from category where type = #{type} order by sort asc")
    List<Category> listCategoryByType(Integer type);

}
