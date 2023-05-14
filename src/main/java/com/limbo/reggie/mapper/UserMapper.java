package com.limbo.reggie.mapper;

import com.limbo.reggie.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户信息 Mapper 接口
 * </p>
 *
 * @author limbo
 * @since 2023-05-11
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
