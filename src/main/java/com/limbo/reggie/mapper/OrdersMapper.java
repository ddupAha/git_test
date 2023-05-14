package com.limbo.reggie.mapper;

import com.limbo.reggie.entity.Orders;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 订单表 Mapper 接口
 * </p>
 *
 * @author limbo
 * @since 2023-05-08
 */
@Mapper
public interface OrdersMapper extends BaseMapper<Orders> {

}
