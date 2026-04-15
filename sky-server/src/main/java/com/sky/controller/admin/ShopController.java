package com.sky.controller.admin;

import com.sky.constant.ShopStatusConstant;
import com.sky.result.Result;
import com.sky.service.AdminShopService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("adminShopController")
@Slf4j
@RequestMapping("/admin/shop")
@Api(value = "店铺相关接口", tags = "店铺相关接口")
public class ShopController {

    @Autowired
    AdminShopService adminShopService;

    /**
     * 设置店铺营业状态
     *
     * @param status
     * @return
     */
    @PutMapping("/{status}")
    @ApiOperation("value = \"设置店铺营业状态\", notes = \"设置店铺营业状态\")")
    public Result updateStatus(@PathVariable Integer status) {
        log.info("设置店铺营业状态，status：{}", status == 1 ? "营业中" : "打烊中");
        adminShopService.updateStatus(status);
        return Result.success();
    }

    /**
     * 查询店铺营业状态
     *
     * @return
     */
    @GetMapping("/status")
    @ApiOperation("value = \"查询店铺营业状态\", notes = \"查询店铺营业状态\")")
    public Result<Integer> getStatus() {
        Integer shopStatus = adminShopService.getStatus();
        log.info("查询店铺营业状态:{}", shopStatus == 1 ? "营业中" : "打烊中");
        return Result.success(shopStatus);
    }
}
