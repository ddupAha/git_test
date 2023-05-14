package com.limbo.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.limbo.reggie.dto.OrdersDto;
import com.limbo.reggie.entity.Orders;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpSession;

/**
 * <p>
 * 订单表 服务类
 * </p>
 *
 * @author limbo
 * @since 2023-05-08
 */
public interface OrdersService extends IService<Orders> {

    void submit(Orders orders);

    Page<OrdersDto> get_page(int page, int pageSize, String number, String beginTime, String endTime);

    Page<OrdersDto> get_user_order_page(int page, int pageSize, HttpSession session);

    void deleteById(Long id);
}
