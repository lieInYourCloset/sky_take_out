package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.entity.Employee;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.CategoryMapper;
import com.sky.result.PageResult;
import com.sky.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private com.sky.mapper.DishMapper dishMapper;
    @Autowired
    private com.sky.mapper.SetmealMapper setmealMapper;

    @Override
    public void addCategory(CategoryDTO categoryDTO) {
        Category category = new Category();

        BeanUtils.copyProperties(categoryDTO, category);
        //设置账户状态为启用状态
        category.setStatus(StatusConstant.ENABLE);

        //设置创建人和修改人
        category.setCreateUser(BaseContext.getCurrentId());
        category.setUpdateUser(BaseContext.getCurrentId());

        //设置创建时间和修改时间
        category.setCreateTime(LocalDateTime.now());
        category.setUpdateTime(LocalDateTime.now());

        //保存员工信息到数据库
        Long id = categoryMapper.insert(category);
        category.setId(id);
    }

    @Override
    public void updateCategory(CategoryDTO categoryDTO) {
        Category category = new Category();

        BeanUtils.copyProperties(categoryDTO, category);

        category.setUpdateUser(BaseContext.getCurrentId());
        category.setUpdateTime(LocalDateTime.now());

        //在数据库中修改分类
        categoryMapper.updateCategory(category);
    }

    @Override
    public void updateCategoryStatus(Long id, Integer status) {
        Category category = new Category();

        category.setId(id);
        category.setStatus(status);

        category.setUpdateUser(BaseContext.getCurrentId());
        category.setUpdateTime(LocalDateTime.now());

        //在数据库中修改分类
        categoryMapper.updateCategoryStatus(category);
    }

    @Override
    public void deleteCategoryById(Long id) {
        //检查套餐内的菜品是否关联了该分类，如果关联了则不能删除
        if (dishMapper.queryDishCountByCategoryId(id) > 0) {
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_DISH);
        }
        if (setmealMapper.querySetmealCountByCategoryId(id) > 0) {
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_SETMEAL);
        }

        categoryMapper.deleteCategoryById(id);
    }

    @Override
    public PageResult CategoryPageQuery(CategoryPageQueryDTO categoryPageQueryDTO) {
        try (Page<Employee> page = PageHelper.startPage(categoryPageQueryDTO.getPage(), categoryPageQueryDTO.getPageSize())) {
            categoryMapper.pageQuery(categoryPageQueryDTO);
            return new PageResult(page.getTotal(), page.getResult());
        }
    }

    @Override
    public List<Category> listCategoryByType(Integer type) {
        return categoryMapper.listCategoryByType(type);
    }

}
