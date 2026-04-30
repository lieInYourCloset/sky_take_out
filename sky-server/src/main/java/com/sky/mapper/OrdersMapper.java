package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.Orders;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface OrdersMapper {
    /**
     * 插入订单数据
     *
     * @param orders
     */
    @AutoFill(value = OperationType.INSERT)
    void insert(Orders orders);

    @Select("SELECT * FROM orders WHERE order_time <= NOW() - INTERVAL 15 MINUTE AND status = 1")
    List<Orders> findTimeoutPendingOrders();

    void updateOrder(Orders orders);

    @Select("SELECT * FROM orders WHERE status = 4")
    List<Orders> findDeliveringOrders();

    Object pageQuery(OrdersPageQueryDTO ordersPageQueryDTO);

    @Select("SELECT COUNT(*) FROM orders WHERE status = #{status}")
    Integer getOrdersCountByStatus(Integer status);

    @Select("SELECT * FROM orders WHERE id = #{id}")
    Orders getById(Long id);

    @Select("SELECT status FROM orders WHERE id = #{id}")
    Integer getStatusById(Long id);

    @Update("UPDATE orders SET status = #{status} WHERE id = #{id}")
    void updateStatusById(Long id, Integer status);


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
