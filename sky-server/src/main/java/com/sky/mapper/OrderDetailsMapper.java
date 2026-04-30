package com.sky.mapper;

import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrderDetailsMapper {
    /**
    * 插入订单详情数据
    *
    * @param orderDetail
    */
    @Insert("insert into order_detail (name, order_id, dish_id, setmeal_id, dish_flavor, number, amount, image) " +
            "values (#{name}, #{orderId}, #{dishId}, #{setmealId}, #{dishFlavor}, #{number}, #{amount}, #{image})")
    void insert(OrderDetail orderDetail);

    @Select("SELECT * FROM order_detail WHERE order_id = #{id}")
    List<OrderDetail> listOrderDetailsByOrderId(Long id);
}
