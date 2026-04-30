package com.sky.service;

import com.sky.dto.*;
import com.sky.result.PageResult;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;

public interface OrderService {

    /**
     * 提交订单
     * @param ordersSubmitDTO
     * @return
     */
    OrderSubmitVO submit(OrdersSubmitDTO ordersSubmitDTO);

    /**
     *  订单条件查询
     * @param ordersPageQueryDTO
     * @return
     */
    PageResult conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 各个状态的订单数量统计
     * @return
     */
    OrderStatisticsVO  statistics();

    /**
     * 派送订单
     * @param ordersConfirmDTO
     */
    void confirmOrder(OrdersConfirmDTO ordersConfirmDTO);

     /**
      * 商户取消订单
      *
      * @param ordersCancelDTO
      */
    void cancelOrder(OrdersCancelDTO ordersCancelDTO);

    /**
     * 完成订单
     * @param id
     */
    void completeOrder(Long id);

    /**
     * 派送订单
     * @param id
     */
    void deliverOrder(Long id);

    /**
     * 商户拒单
     * @param ordersRejectionDTO
     */
    void rejectOrder(OrdersRejectionDTO ordersRejectionDTO);

    OrderVO getOrderDetailsByOrderId(Long id);
}
