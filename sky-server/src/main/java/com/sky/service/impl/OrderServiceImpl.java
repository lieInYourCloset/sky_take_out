package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.*;
import com.sky.entity.*;
import com.sky.exception.AddressBookBusinessException;
import com.sky.exception.ShoppingCartBusinessException;
import com.sky.mapper.*;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.service.OrderService;
import com.sky.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrdersMapper ordersMapper;
    @Autowired
    private OrderDetailsMapper orderDetailsMapper;
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private AddressBookMapper addressBookMapper;
    @Autowired
    private JwtProperties jwtProperties;

    @Override
    public OrderSubmitVO submit(OrdersSubmitDTO ordersSubmitDTO) {

        // 检测业务异常，购物车是否为空，地址簿是否为空
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUserId(BaseContext.getCurrentId());
        List<ShoppingCart> shoppingCarts = shoppingCartMapper.listShoppingCart(shoppingCart);
        if (shoppingCarts == null || shoppingCarts.isEmpty()) {
            throw new ShoppingCartBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
        }
        AddressBook addressBook = addressBookMapper.getById(ordersSubmitDTO.getAddressBookId());
        if (addressBook == null) {
            throw new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL); // 可以另外多一层检查，检查拿到的地址是否是当前用户的地址
        }

        Long userId = BaseContext.getCurrentId();

        // 插入Orders表
        Orders orders = new Orders();
        BeanUtils.copyProperties(ordersSubmitDTO,orders);

        orders.setAddress(addressBook.getProvinceName() + addressBook.getCityName() + addressBook.getDistrictName() + addressBook.getDetail());
        orders.setPhone(addressBook.getPhone());
        orders.setConsignee(addressBook.getConsignee());

        orders.setStatus(Orders.PENDING_PAYMENT);
        orders.setPayStatus(Orders.UN_PAID);

        orders.setUserId(userId);
        orders.setUserName(addressBook.getConsignee());
        orders.setOrderTime(LocalDateTime.now());
        orders.setNumber(String.valueOf(System.currentTimeMillis())); // 订单号可以使用雪花算法生成，这里为了简单直接使用当前时间戳

        ordersMapper.insert(orders);
        Long orderId = orders.getId();

        // 插入OrderDetails表
        shoppingCarts.forEach(shoppingCart1 -> {
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(shoppingCart1,orderDetail);
            orderDetail.setId(null);
            orderDetail.setOrderId(orderId);
            orderDetailsMapper.insert(orderDetail);
        });

        // 清空购物车
        shoppingCartMapper.cleanShoppingCartByUserId(userId);

        // 返回VO
        return OrderSubmitVO.builder()
                .id(orderId)
                .orderAmount(orders.getAmount())
                .orderNumber(orders.getNumber())
                .orderTime(orders.getOrderTime())
                .build();
    }

    @Override
    public PageResult conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO) {
        try (Page<Employee> page = PageHelper.startPage(ordersPageQueryDTO.getPage(), ordersPageQueryDTO.getPageSize())) {
            ordersMapper.pageQuery(ordersPageQueryDTO);
            return new PageResult(page.getTotal(), page.getResult());
        }
    }

    @Override
    public OrderStatisticsVO statistics() {
        return OrderStatisticsVO.builder()
                .toBeConfirmed(ordersMapper.getOrdersCountByStatus(Orders.TO_BE_CONFIRMED))
                .confirmed(ordersMapper.getOrdersCountByStatus(Orders.CONFIRMED))
                .deliveryInProgress(ordersMapper.getOrdersCountByStatus(Orders.DELIVERY_IN_PROGRESS))
                .build();
    }

    @Override
    public void confirmOrder(OrdersConfirmDTO ordersConfirmDTO) {
        log.info("确认订单：{}", ordersConfirmDTO);
        Long orderId = ordersConfirmDTO.getId();
        if (ordersMapper.getStatusById(orderId) != Orders.TO_BE_CONFIRMED) {
            log.error("订单状态异常，无法确认接单，订单id：{}，当前订单状态：{}", orderId, ordersMapper.getStatusById(orderId));
//            throw new RuntimeException(MessageConstant.ORDER_STATUS_ERROR);
            return;
        }
        ordersMapper.updateStatusById(orderId, Orders.CONFIRMED);
    }

    @Override
    public void cancelOrder(OrdersCancelDTO ordersCancelDTO) {
        log.info("取消订单：{}", ordersCancelDTO);
        Long orderId = ordersCancelDTO.getId();
        if (ordersMapper.getStatusById(orderId) == Orders.CANCELLED) {
            log.error("订单状态异常，订单本身已取消，无法重复取消，订单id：{}", orderId);
            return;
        }
        ordersMapper.updateStatusById(orderId, Orders.CANCELLED);
    }

    @Override
    public void completeOrder(Long id) {
        log.info("完成订单，订单id：{}", id);
        if (ordersMapper.getStatusById(id) != Orders.DELIVERY_IN_PROGRESS) {
            log.error("订单状态异常，订单不处于派送中状态，无法直接完成，订单id：{}，当前订单状态：{}", id, ordersMapper.getStatusById(id));
            return;
        }
        ordersMapper.updateStatusById(id, Orders.COMPLETED);
    }

    @Override
    public void deliverOrder(Long id) {
        log.info("派送订单，订单id：{}", id);
        if (ordersMapper.getStatusById(id) != Orders.CONFIRMED) {
            log.error("订单状态异常，订单不处于接单状态，无法进行派送，订单id：{}，当前订单状态：{}", id, ordersMapper.getStatusById(id));
            return;
        }
        ordersMapper.updateStatusById(id, Orders.DELIVERY_IN_PROGRESS);
    }

    @Override
    public void rejectOrder(OrdersRejectionDTO ordersRejectionDTO) {
        log.info("拒绝订单：{}", ordersRejectionDTO);
        Long orderId = ordersRejectionDTO.getId();
        if (ordersMapper.getStatusById(orderId) != Orders.TO_BE_CONFIRMED) {
            log.error("订单状态异常，无法拒单，订单id：{}，当前订单状态：{}", orderId, ordersMapper.getStatusById(orderId));
            return;
        }
        ordersMapper.updateStatusById(orderId, Orders.CANCELLED);
    }

    @Override
    public OrderVO getOrderDetailsByOrderId(Long id) {
        log.info("查询订单详情，订单id：{}", id);
        Orders orders = ordersMapper.getById(id);
        if (orders == null) {
            log.error("订单不存在，订单id：{}", id);
            return null;
        }
        List<OrderDetail> orderDetails = orderDetailsMapper.listOrderDetailsByOrderId(id);
        log.info("订单详情查询结果，订单信息：{}，订单详情列表：{}", orders, orderDetails);

        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(orders, orderVO);
        orderVO.setOrderDetailList(orderDetails);
        return orderVO;
    }
}
