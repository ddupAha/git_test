package com.limbo.reggie.service.impl;

import com.limbo.reggie.entity.User;
import com.limbo.reggie.mapper.UserMapper;
import com.limbo.reggie.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户信息 服务实现类
 * </p>
 *
 * @author limbo
 * @since 2023-05-11
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
