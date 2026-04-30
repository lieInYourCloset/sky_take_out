package com.sky.controller.admin;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.*;
import com.sky.entity.OrderDetail;
import com.sky.mapper.OrdersMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Size;
import java.util.List;

@RestController("adminOrderController")
@RequestMapping("/admin/order")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrdersMapper ordersMapper;

    @GetMapping("/conditionSearch")
    public Result conditionSearch(OrdersPageQueryDTO  ordersPageQueryDTO) {
        log.info("订单条件查询：{}", ordersPageQueryDTO);
        PageResult pageResult = orderService.conditionSearch(ordersPageQueryDTO);
        return Result.success(pageResult);
    }

    @GetMapping("/statistics")
    public Result statistics() {
        log.info("订单数量查询");
        return Result.success(orderService.statistics());
    }

    @PutMapping("/delivery/{id}")
    public Result deliverOrder(@PathVariable Long id) {
         orderService.deliverOrder(id);
         return Result.success();
    }

    @PutMapping("/cancel")
    public Result cancelOrder(@RequestBody OrdersCancelDTO  ordersCancelDTO) {
        orderService.cancelOrder(ordersCancelDTO);
        return Result.success();
    }

    @PutMapping("/confirm")
    public Result confirmOrder(@RequestBody OrdersConfirmDTO ordersConfirmDTO) {
        orderService.confirmOrder(ordersConfirmDTO);
        return Result.success();
    }

    @PutMapping("/rejection")
    public Result cancelOrder(@RequestBody OrdersRejectionDTO ordersRejectionDTO) {
        orderService.rejectOrder(ordersRejectionDTO);
        return Result.success();
    }

    @PutMapping("/complete/{id}")
    public Result completeOrder(@PathVariable Long id) {
        orderService.completeOrder(id);
        return Result.success();
    }

    @GetMapping("/details/{id}")
    public Result getOrderDetailsByOrderId(@PathVariable Long id) {
        OrderVO orderVO = orderService.getOrderDetailsByOrderId(id);
        return Result.success(orderVO);
    }
}
