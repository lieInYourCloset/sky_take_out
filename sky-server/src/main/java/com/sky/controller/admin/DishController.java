package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.Employee;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜品管理
 */
@RestController("adminDishController")
@RequestMapping("/admin/dish")
@Api(tags = "菜品管理相关接口")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;

    @PostMapping
    @ApiOperation(value = "新增菜品接口")
    public Result addDish(@RequestBody DishDTO  dishDTO) {
        log.info("新增菜品:{}",  dishDTO);
        dishService.addDish(dishDTO);
        return Result.success();
    }

     @PutMapping
     @ApiOperation(value = "修改菜品接口")
     public Result updateDish(@RequestBody DishDTO  dishDTO) {
         log.info("修改菜品:{}", dishDTO);
         dishService.updateDish(dishDTO);
         return Result.success();
     }

    /**
     * 菜品查询分页
     *
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("菜品查询页面")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO) {
        log.info("菜品查询页面：{}", dishPageQueryDTO);
        PageResult pageResult = dishService.dishPageQuery(dishPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 菜品起售、停售
     *
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation("菜品起售停售接口")
    public Result updateDishStatus(@PathVariable Integer status, @RequestParam Long id) {
        log.info("菜品起售停售：id={}, status={}", id, status);
        dishService.updateDishStatus(id, status);
        return Result.success();
    }

    /**
     * 批量删除菜品
     *
     * @param ids
     * @return
     */
    @DeleteMapping
    @ApiOperation("批量删除菜品接口")
    public Result deleteDishById(@RequestParam List<Long> ids) {
        log.info("批量删除菜品：ids={}", ids);
        dishService.deleteDishById(ids);
        return Result.success();
    }

    /**
     * 根据id查询菜品信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("根据id查询菜品列表接口")
    public Result<DishVO> getById(@PathVariable Long id) {
        log.info("根据id查询菜品列表：id={}", id);
        DishVO dishVO = dishService.queryDishByIdWithFlavors(id);
        return Result.success(dishVO);
    }

//    /**
//     * 根据分类id查询菜品信息
//     * @param categoryId
//     * @return
//     */
//    @GetMapping("/list")
//    @ApiOperation("根据分类id查询dishVO接口")
//    public Result<List<DishVO>> getDishVOListByCategoryId(Long categoryId) {
//        log.info("根据分类id查询dishVO接口：CategoryId={}", categoryId);
//        List<DishVO> list = dishService.queryDishVOByCategoryId(categoryId);
//        return Result.success(list);
//    }

    /**
     * 根据分类id查询菜品
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("根据分类id查询菜品")
    public Result<List<Dish>> getDishListByCategoryId(Long categoryId){
        List<Dish> list = dishService.queryDishByCategoryId(categoryId);
        return Result.success(list);
    }
}
