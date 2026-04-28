package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.Orders;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.*;

@Mapper
public interface OrdersMapper {
    /**
     * 插入订单数据
     *
     * @param orders
     */
    @AutoFill(value = OperationType.INSERT)
    Long insert(Orders orders);

//    /**
//     * 根据id删除菜品
//     *
//     * @param id
//     */
//    @Delete("delete from dish where id = #{id}")
//    void deleteDishById(Long id);
//
//    /**
//     * 分类分页查询
//     *
//     * @param ordersPageQueryDTO
//     * @return
//     */
//    Page<???????> historyOrderPageQuery(OrdersPageQueryDTO ordersPageQueryDTO);
}
