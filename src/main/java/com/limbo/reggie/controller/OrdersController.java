package com.limbo.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.limbo.reggie.common.Result;
import com.limbo.reggie.dto.DishDto;
import com.limbo.reggie.dto.OrdersDto;
import com.limbo.reggie.entity.Dish;
import com.limbo.reggie.entity.OrderDetail;
import com.limbo.reggie.entity.Orders;
import com.limbo.reggie.service.OrderDetailService;
import com.limbo.reggie.service.OrdersService;
import com.limbo.reggie.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 订单表 前端控制器
 * </p>
 *
 * @author limbo
 * @since 2023-05-08
 */
@RestController
@RequestMapping("/order")
public class OrdersController {
    @Autowired
    private OrdersService ordersService;

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    UserService userService;

    /**
     * 用户下单
     * @param orders
     * @return
     */
    @PostMapping("/submit")
    public Result<String> submit(@RequestBody Orders orders){
        ordersService.submit(orders);
        System.out.println("下单成功");
        return Result.success("下单成功");
    }

    /**
     * 分页查询订单，返回DTO类型
     * @param page
     * @param pageSize
     * @param session
     * @return
     */
    @GetMapping("/userPage")
    public Result<Page<OrdersDto>> get_user_order_page(int page, int pageSize, HttpSession session){
        Page<OrdersDto> return_page = ordersService.get_user_order_page(page, pageSize, session);
        return Result.success(return_page);
    }

    /**
     * 后台分页显示订单
     * @param page
     * @param pageSize
     * @param number
     * @param beginTime
     * @param endTime
     * @return
     */
    @GetMapping("/page")
    public Result<Page<OrdersDto>> page(int page, int pageSize, String number, String beginTime, String endTime) {
        Page<OrdersDto> return_page = ordersService.get_page(page, pageSize, number, beginTime, endTime);
        return Result.success(return_page);
    }


    @DeleteMapping("/{id}")
    public Result<String> delete_order_byId(@PathVariable Long id){
        ordersService.deleteById(id);
        return Result.success("删除订单成功");
    }


    @PutMapping
    public Result<String> editOrderStatus(@RequestBody Orders orders){
        LambdaUpdateWrapper<Orders> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Orders::getId, orders.getId())
                .set(Orders::getStatus, orders.getStatus());

        ordersService.update(updateWrapper);
        return Result.success("操作订单成功");
    }

}
