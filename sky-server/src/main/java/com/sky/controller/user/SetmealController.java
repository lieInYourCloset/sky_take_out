package com.sky.controller.user;

import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 菜品管理
 */
@RestController("userSetmealController")
@RequestMapping("/user/setmeal")
@Api(tags = "菜品管理相关接口")
@Slf4j
public class SetmealController {

    @Autowired
    private DishService dishService;

    /**
     * 根据分类id查询菜品信息
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("根据分类id查询套餐信息接口")
    public Result<List<DishVO>> getByCategoryId(Long categoryId) {
        log.info("根据分类id查询菜品信息：categoryId={}", categoryId);
        List<DishVO> list = dishService.queryDishVOByCategoryId(categoryId);
        return Result.success(list);
    }
}
