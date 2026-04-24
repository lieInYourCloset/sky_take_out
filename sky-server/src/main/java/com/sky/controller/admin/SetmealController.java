package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.service.SetmealService;
import com.sky.vo.DishVO;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜品管理
 */
@RestController("adminSetmealController")
@RequestMapping("/admin/setmeal")
@Api(tags = "套餐管理相关接口")
@Slf4j
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @PostMapping
    @ApiOperation(value = "新增套餐接口")
    public Result addDish(@RequestBody SetmealDTO setmealDTO) {
        log.info("新增菜品:{}",  setmealDTO);
        setmealService.addSetmeal(setmealDTO);
        return Result.success();
    }

    @PutMapping
    @ApiOperation(value = "修改套餐接口")
    public Result updateDish(@RequestBody SetmealDTO  setmealDTO) {
        log.info("修改菜品:{}", setmealDTO);
        setmealService.updateSetmeal(setmealDTO);
        return Result.success();
    }

    /**
     * 套餐查询分页
     *
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("套餐查询页面")
    public Result<PageResult> page(SetmealPageQueryDTO setmealPageQueryDTO) {
        log.info("套餐查询页面：{}", setmealPageQueryDTO);
        PageResult pageResult = setmealService.setmealPageQuery(setmealPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 套餐起售、停售
     *
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation("套餐起售停售接口")
    public Result updateDishStatus(@PathVariable Integer status, @RequestParam Long id) {
        log.info("套餐起售停售：id={}, status={}", id, status);
        setmealService.updateSetmealStatus(id, status);
        return Result.success();
    }

    /**
     * 批量删除套餐
     *
     * @param ids
     * @return
     */
    @DeleteMapping
    @ApiOperation("批量删除套餐接口")
    public Result deleteDishById(@RequestParam List<Long> ids) {
        log.info("批量删除套餐：ids={}", ids);
        setmealService.deleteSetmealById(ids);
        return Result.success();
    }

    /**
     * 根据id查询套餐信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("根据id查询套餐信息接口")
    public Result<SetmealVO> getById(@PathVariable Long id) {
        log.info("根据id查询套餐信息：id={}", id);
        SetmealVO setmealVO = setmealService.querySetmealById(id);
        return Result.success(setmealVO);
    }

}
