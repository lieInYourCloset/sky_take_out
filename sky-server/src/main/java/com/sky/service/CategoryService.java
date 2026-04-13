package com.sky.service;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;

import java.util.List;

public interface CategoryService {

    /**
     * 新增分类
     *
     * @param categoryDTO
     */
    void addCategory(CategoryDTO categoryDTO);

    /**
     * 修改分类
     *
     * @param categoryDTO
     */
    void updateCategory(CategoryDTO categoryDTO);

    /**
     * 启用禁用分类
     *
     * @param id
     * @param status
     */
    void updateCategoryStatus(Long id, Integer status);

    /**
     * 根据id删除分类
     *
     * @param id
     * @return
     */
    void deleteCategoryById(Long id);

    /**
     * 分类分页查询
     *
     * @param categoryPageQueryDTO
     * @return
     */
    PageResult CategoryPageQuery(CategoryPageQueryDTO categoryPageQueryDTO);

    /**
     * 根据类型查询分类列表
     *
     * @param type
     * @return
     */
    List<Category> listCategoryByType(Integer type);
}
