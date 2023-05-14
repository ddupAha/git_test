package com.limbo.reggie.mapper;

import com.limbo.reggie.entity.OrderDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 订单明细表 Mapper 接口
 * </p>
 *
 * @author limbo
 * @since 2023-05-11
 */
@Mapper
public interface OrderDetailMapper extends BaseMapper<OrderDetail> {

}
