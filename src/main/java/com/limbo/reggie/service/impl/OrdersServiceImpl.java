package com.limbo.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.limbo.reggie.common.BaseContext;
import com.limbo.reggie.common.CustomException;
import com.limbo.reggie.dto.OrdersDto;
import com.limbo.reggie.entity.*;
import com.limbo.reggie.mapper.OrdersMapper;
import com.limbo.reggie.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author limbo
 * @since 2023-05-08
 */
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private UserService userService;

    @Autowired
    private AddressBookService addressBookService;

    @Autowired
    private OrderDetailService orderDetailService;

    /**
     * 用户下单
     *
     * @param orders
     */
    @Transactional
    public void submit(Orders orders) {
        //获得当前用户id
        Long userId = BaseContext.get_user_id();

        //查询当前用户的购物车数据
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId, userId);
        List<ShoppingCart> shoppingCarts = shoppingCartService.list(wrapper);

        if (shoppingCarts == null || shoppingCarts.size() == 0) {
            throw new CustomException("购物车为空，不能下单");
        }

        //查询用户数据
        User user = userService.getById(userId);

        //查询地址数据
        Long addressBookId = orders.getAddressBookId();
        AddressBook addressBook = addressBookService.getById(addressBookId);
        if (addressBook == null) {
            throw new CustomException("用户地址信息有误，不能下单");
        }

        long orderId = IdWorker.getId();//订单号

        AtomicInteger amount = new AtomicInteger(0);

        List<OrderDetail> orderDetails = shoppingCarts.stream().map((item) -> {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(orderId);
            orderDetail.setNumber(item.getNumber());
            orderDetail.setDishFlavor(item.getDishFlavor());
            orderDetail.setDishId(item.getDishId());
            orderDetail.setSetmealId(item.getSetmealId());
            orderDetail.setName(item.getName());
            orderDetail.setImage(item.getImage());
            orderDetail.setAmount(item.getAmount());
            amount.addAndGet(item.getAmount().multiply(new BigDecimal(item.getNumber())).intValue());
            return orderDetail;
        }).toList();


        orders.setId(orderId);
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());
        orders.setStatus(2);  // 待发货状态
        orders.setAmount(new BigDecimal(amount.get()));//总金额
        orders.setUserId(userId);
        orders.setNumber(String.valueOf(orderId));
        orders.setUserName(user.getName());
        orders.setConsignee(addressBook.getConsignee());
        orders.setPhone(addressBook.getPhone());
        orders.setAddress((addressBook.getProvinceName() == null ? "" : addressBook.getProvinceName())
                + (addressBook.getCityName() == null ? "" : addressBook.getCityName())
                + (addressBook.getDistrictName() == null ? "" : addressBook.getDistrictName())
                + (addressBook.getDetail() == null ? "" : addressBook.getDetail()));
        //向订单表插入数据，一条数据
        this.save(orders);

        //向订单明细表插入数据，多条数据
        orderDetailService.saveBatch(orderDetails);

        //清空购物车数据
        shoppingCartService.remove(wrapper);
    }

    @Override
    public Page<OrdersDto> get_page(int page, int pageSize, String number, String beginTime, String endTime) {
        Page<Orders> pageInfo = new Page<>(page, pageSize);
        Page<OrdersDto> return_page = new Page<>();

        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(number!=null, Orders::getNumber, number)
                .between(beginTime != null && endTime != null, Orders::getOrderTime, beginTime, endTime)
                .orderByDesc(Orders::getOrderTime);

        List<Orders> ordersList = this.list(queryWrapper);

        this.page(pageInfo, queryWrapper);
        BeanUtils.copyProperties(pageInfo, return_page, "records");

        List<OrdersDto> ordersDtoList = get_orderDto_list(ordersList);
        return_page.setRecords(ordersDtoList);
        return return_page;
    }

    @Override
    public Page<OrdersDto> get_user_order_page(int page, int pageSize, HttpSession session) {
        Page<Orders> pageInfo = new Page<>(page, pageSize);
        Page<OrdersDto> return_page = new Page<>();

        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getUserId, session.getAttribute("user"));
        List<Orders> ordersList = this.list(queryWrapper);

        this.page(pageInfo, queryWrapper);
        BeanUtils.copyProperties(pageInfo, return_page, "records");

        List<OrdersDto> ordersDtoList = get_orderDto_list(ordersList);

        return_page.setRecords(ordersDtoList);
        return return_page;
    }

    @Override
    public void deleteById(Long id) {
        this.removeById(id);

        // 删除订单详情表对应的记录
        LambdaQueryWrapper<OrderDetail> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(id !=null, OrderDetail::getOrderId, id);
        orderDetailService.remove(queryWrapper);

    }


    public List<OrdersDto> get_orderDto_list(List<Orders> ordersList){
        List<OrdersDto> ordersDtoList = ordersList.stream().map(item -> {
            OrdersDto ordersDto = new OrdersDto();
            BeanUtils.copyProperties(item, ordersDto);

            LambdaQueryWrapper<OrderDetail> orderDetailLambdaQueryWrapper = new LambdaQueryWrapper<>();
            orderDetailLambdaQueryWrapper.eq(OrderDetail::getOrderId, item.getId());
            List<OrderDetail> orderDetailList = orderDetailService.list(orderDetailLambdaQueryWrapper);

            ordersDto.setOrderDetails(orderDetailList);
            String name = userService.getById(item.getUserId()).getName();
            if (name == null)
                name = "该用户未设置名称";
            ordersDto.setUserName(name);

            return ordersDto;
        }).toList();
        return ordersDtoList;
    }
}
