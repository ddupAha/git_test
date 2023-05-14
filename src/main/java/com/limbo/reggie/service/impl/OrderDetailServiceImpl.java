package com.limbo.reggie.service.impl;

import com.limbo.reggie.entity.OrderDetail;
import com.limbo.reggie.mapper.OrderDetailMapper;
import com.limbo.reggie.service.OrderDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单明细表 服务实现类
 * </p>
 *
 * @author limbo
 * @since 2023-05-11
 */
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {

}
